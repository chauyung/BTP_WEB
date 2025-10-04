package nccc.btp.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import nccc.btp.dto.CategoryOptionDto;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.dto.PurchaseDetailDto;
import nccc.btp.dto.PurchaseRequestDto;
import nccc.btp.dto.SyncOuWithManagerDto;
import nccc.btp.dto.VendorQuoteDto;
import nccc.btp.entity.BpmPrBpList;
import nccc.btp.entity.BpmPrItComment;
import nccc.btp.entity.BpmPrM;
import nccc.btp.entity.BpmPrMD1;
import nccc.btp.entity.NcccCostCenter;
import nccc.btp.entity.NcccPurchaseCategoryNumber;
import nccc.btp.entity.SyncOU;
import nccc.btp.entity.SyncUser;
import nccc.btp.enums.AssigneeRole;
import nccc.btp.enums.ProcessDefinitionKey;
import nccc.btp.repository.BpmNcccMissionAssignerRepository;
import nccc.btp.repository.BpmPrBpListRepository;
import nccc.btp.repository.BpmPrItCommentRepository;
import nccc.btp.repository.BpmPrMD1Repository;
import nccc.btp.repository.BpmPrMRepository;
import nccc.btp.repository.NcccCostCenterRepository;
import nccc.btp.repository.NcccPurchaseCategoryNumberRepository;
import nccc.btp.repository.SyncOURepository;
import nccc.btp.repository.SyncUserRepository;
import nccc.btp.service.FlowableService;
import nccc.btp.service.PurchaseRequistionService;
import nccc.btp.util.DateUtil;
import nccc.btp.util.FileUtil;
import nccc.btp.util.SecurityUtil;
import nccc.btp.util.StringUtil;

@Transactional
@Service
public class PurchaseRequistionServiceImpl implements PurchaseRequistionService {

	private static final Logger logger = LoggerFactory.getLogger(PurchaseRequistionServiceImpl.class);
	private static final DateTimeFormatter PREFIX_FMT = DateTimeFormatter.ofPattern("yyMM");

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private FlowableService flowableService;
	@Autowired
	private BpmPrMRepository bpmPrMRepo;
	@Autowired
	private BpmPrMD1Repository bpmPrMDlRepo;
	@Autowired
	private BpmPrItCommentRepository itCommentRepo;
	@Autowired
	private BpmPrBpListRepository bpListRepo;
	@Autowired
	private NcccCostCenterRepository costCenterRepo;
	@Autowired
    NcccPurchaseCategoryNumberRepository ncccPurchaseCategoryNumberRepo;
	@Autowired
	private SyncUserRepository syncUserRepository;
    @Autowired
    private SyncOURepository syncOURepository;
    @Autowired
    private BpmNcccMissionAssignerRepository bpmNcccMissionAssignerRepository;

	public String generateNextPrNo() {
		// 1. 計算今天的 yymm，例如 2025-06 → "2506"
		String yymm = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));

		// 2. 對應前綴去查當天已存在的最大 prNo（鎖定那一筆）
		Pageable singleRow = PageRequest.of(0, 1);
		List<BpmPrM> list = bpmPrMRepo.findTopOneByPrefixForUpdate(yymm, singleRow);

		int nextSerial;
		if (list.isEmpty()) {
			// 當天還沒任何一筆，流水號從 1 開始
			nextSerial = 1;
		} else {
			// 拿到那筆目前最大 prNo，例如 "PR25060001"
			String maxPrNo = list.get(0).getPrNo();
			// PR(2) + yymm(4) = 6 → 從第 6 位開始是流水號
			String lastFour = maxPrNo.substring(6); // 取後 4 碼
			nextSerial = Integer.parseInt(lastFour) + 1;
		}

		// 3. 把流水號格式化成 4 碼，不足補零
		String serialPadded = String.format("%04d", nextSerial);

		// 4. 組成最終 prNo
		String newPrNo = "PR" + yymm + serialPadded; // e.g. "PR25060001"

		return newPrNo;
	}

	@Override
	public void createAndStartProcess(PurchaseRequestDto dto, List<MultipartFile> attachment,
			List<MultipartFile> quotationFile) {

      NcccUserDto user = SecurityUtil.getCurrentUser();

		// 1. 產生、設定 PR No
		String prNo = generateNextPrNo();
		dto.setPrNo(prNo);

		// 2. 儲存主檔
		BpmPrM master = new BpmPrM();
		master.setPrNo(prNo);
        master.setApplyDate(LocalDate.now());
		master.setDepartment(dto.getDepartment());
		master.setApplicant(dto.getApplicant());
		master.setEmpNo(dto.getEmpNo());
		master.setItType(dto.getRadio2()); // 1 或 2
		master.setTotalAmount(
				dto.getTotalAmount() != null ? StringUtil.parseByReplace(dto.getTotalAmount()) : BigDecimal.ZERO);
		master.setInquiryAmount(
				dto.getInquiryAmount() != null ? StringUtil.parseByReplace(dto.getInquiryAmount()) : BigDecimal.ZERO);
		master.setRemark(dto.getRemark());
		master.setDocNo(dto.getDocNo());
		master.setDemandOrder(dto.getDemandOrder());
		master.setCreatedDate(LocalDate.now());
		master.setCreatedUser(dto.getApplicant());
        master.setUpdateDate(LocalDate.now());
        master.setUpdateUser(user.getUserId());
        BpmPrM sevad = bpmPrMRepo.save(master);

		// 3. 批次處理多筆明細
		AtomicInteger detailCounter = new AtomicInteger(1);
		for (PurchaseDetailDto pd : dto.getDetails()) {

			// 3.1 儲存明細
			BpmPrMD1 detail = new BpmPrMD1();
			detail.setPrNo(prNo);
            detail.setPrItemNo(pd.getPrItemNo());
			detail.setItemCode(pd.getItemCode());
            detail.setRemark(pd.getDetailRemark());
            detail.setFlowDemandDate(pd.getDemandDateByFlow());
			if (pd.getDemandDate() != null && !pd.getDemandDate().isEmpty()) {
				DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
				LocalDate demandDate = LocalDate.parse(pd.getDemandDate(), fmt);
                detail.setDemandDate(demandDate);
			}
			detail.setLocation(pd.getLocation());
			detail.setQty(pd.getQty() != null ? new BigDecimal(pd.getQty()) : BigDecimal.ZERO);
            detail.setPrice(
                pd.getEstimatedAmount() != null ? StringUtil.parseByReplace(pd.getEstimatedAmount())
                    : BigDecimal.ZERO);
			// BigDecimal.ZERO);
			detail.setTax(pd.getTax() != null ? StringUtil.parseByReplace(pd.getTax()) : BigDecimal.ZERO);
			detail.setTotal(pd.getTotal() != null ? StringUtil.parseByReplace(pd.getTotal()) : BigDecimal.ZERO);
            detail.setCreatedDate(LocalDate.now());
			detail.setCreatedUser(dto.getApplicant());
            detail.setUpdateDate(LocalDate.now());
            detail.setUpdateUser(user.getUserId());
            detail.setYear(String.valueOf(LocalDate.now().getYear()));
            detail.setOuCode(pd.getBudgetDepartment());

            NcccPurchaseCategoryNumber ncccPurchaseCategoryNumber =
                ncccPurchaseCategoryNumberRepo.findByCategoryNumber(pd.getItemCode());
            detail.setAccounting(ncccPurchaseCategoryNumber.getAccounting());

			bpmPrMDlRepo.save(detail);

			// 3.2 儲存詢價廠商清單
			List<BpmPrBpList> bpLists = new ArrayList<>();
			for (VendorQuoteDto bpDto : pd.getVendorQuotes()) {
				BpmPrBpList bp = new BpmPrBpList();
				bp.setPrNo(prNo);
                bp.setPrItemNo(pd.getPrItemNo());
				bp.setBpName(bpDto.getBpListBpName());
				bp.setPrice(bpDto.getBpListPrice() != null ? StringUtil.parseByReplace(bpDto.getBpListPrice())
						: BigDecimal.ZERO);
				bp.setTotal(bpDto.getBpListTotal() != null ? StringUtil.parseByReplace(bpDto.getBpListTotal())
						: BigDecimal.ZERO);
				bp.setTax(bpDto.getBpListTax() != null ? StringUtil.parseByReplace(bpDto.getBpListTax())
						: BigDecimal.ZERO);
				// 如果有上傳的詢價單，存入 LOB
				try {
					if (quotationFile != null && !quotationFile.isEmpty()) {
						byte[] quotationFileZip = FileUtil.compressToZip(quotationFile);
						bp.setAttachment(quotationFileZip);
					}
				} catch (Exception e) {
					logger.error("Failed to read quotationFile", e);
				}
				bpLists.add(bp);
			}
			if (!bpLists.isEmpty()) {
				bpListRepo.saveAll(bpLists);
			}
		}

		// 4. 儲存資訊產品意見
		BpmPrItComment itComment = new BpmPrItComment();
		itComment.setPrNo(prNo);
		itComment.setRemark(dto.getItCommentRemark());
		try {
			if (attachment != null && attachment.isEmpty()) {
				byte[] attachmentZip = FileUtil.compressToZip(attachment);
				itComment.setAttachment(attachmentZip);
			}
		} catch (IOException e) {
			logger.error("Failed to read quotationFile", e);
		}
		itCommentRepo.save(itComment);

		//String taskId = startProcess(master);
		// 7. 回傳流程實例 ID
        sevad.setTaskId(LocalDateTime.now().toString());
        bpmPrMRepo.save(sevad);
	}

	@Override
    public String startProcess(PurchaseRequestDto dto) {


      NcccUserDto user = SecurityUtil.getCurrentUser();

      boolean isSupervisor = false;

      // 副總依照各個部門往上找會不一樣 額外處理
      String vicePresident = null;
      String processKey;
      String hrid = user.getHrid();
      SyncUser syncUser = syncUserRepository.findByHrid(hrid);
      SyncOU syncOU = syncOURepository.findByOuCode(syncUser.getOuCode());
      if (syncOU.getOuLevel() != null) {
        int level = Integer.parseInt(syncOU.getOuLevel());
        if (level <= 30) {
          isSupervisor = true;
        }
      } else {
        throw new RuntimeException("getSyncOu is null");
      }
      Map<String, Object> vars = new HashMap<>();
      vars.put(AssigneeRole.INITIATOR.getKey(), hrid);
      BigDecimal amount = StringUtil.parseByReplace(dto.getTotalAmount());
      vars.put("amount", amount);
      BigDecimal fivek = new BigDecimal("50000.00");
      BigDecimal thirtyk = new BigDecimal("300000.00");


      // 全體(一般未滿30萬<300000)
      if (dto.getRadio1() == "1" && dto.getRadio2().isEmpty()) {
        processKey = ProcessDefinitionKey.PURCHASEREQUISTION_GENERAL.getKey();
        List<SyncOuWithManagerDto> syncOuWithManagerDtoList = syncOURepository.findSupervisor(hrid);

        List<String> approvers = new ArrayList<>();

        for (SyncOuWithManagerDto syncOuWithManagerDto : syncOuWithManagerDtoList) {
          int level = Integer.parseInt(syncOuWithManagerDto.getOuLevel());
          if (level > 30) {
            approvers.add(syncOuWithManagerDto.getHrid());
          } else if (level == 30) {
            // 申請單位主管
            approvers.add(syncOuWithManagerDto.getHrid());
            vars.put(AssigneeRole.APPLICANT_SUPERVISOR.getKey(), syncOuWithManagerDto.getHrid());
          } else if (level == 20) {
            // 副總
            vicePresident = syncOuWithManagerDto.getHrid();
          } else {
            throw new RuntimeException("組織層級有誤");
          }
        }

        if (approvers.size() == 0) {
          throw new RuntimeException("非單位主管以上，但找不到上層主管");
        }

        vars.put(AssigneeRole.APPROVERS.getKey(), approvers);
        vars.put(AssigneeRole.CURRENT_INDEX.getKey(), 0);
        vars.put(AssigneeRole.CURRENT_APPROVER.getKey(), approvers.get(0));

        // 分派任務(採購經辦)
        vars.put(AssigneeRole.ASSIGN_PURCHASE_CLERK.getKey(), bpmNcccMissionAssignerRepository
            .findByOuCode(AssigneeRole.PURCHASE_CLERK.getCode()).getHrid());
        // 採購經辦
        vars.put(AssigneeRole.PURCHASE_CLERK.getKey(), null);
        // 採購科長
        setAssignee(vars, AssigneeRole.PURCHASE_SECTION_CHIEF);
        // 採購經理
        setAssignee(vars, AssigneeRole.PURCHASE_MANAGER);
        // 採購協理
        setAssignee(vars, AssigneeRole.PURCHASE_ASSOCIATE);
        // 採購資協
        setAssignee(vars, AssigneeRole.PURCHASE_SENIOR_ASSOCIATE);

        // 分派任務(成本經辦)
        vars.put(AssigneeRole.ASSIGN_COST_CLERK.getKey(), bpmNcccMissionAssignerRepository
            .findByOuCode(AssigneeRole.COST_CLERK.getCode()).getHrid());
        // 成本經辦
        vars.put(AssigneeRole.COST_CLERK.getKey(), null);
        // 成本科長
        setAssignee(vars, AssigneeRole.COST_SECTION_CHIEF);

        if (amount.compareTo(fivek) < 0) {
          // 行管部主管
          setAssignee(vars, AssigneeRole.ADMINISTRATIVE_MANAGER);
        } else if (amount.compareTo(fivek) >= 0 && amount.compareTo(thirtyk) < 0) {
          // 副總經理
          vars.put(AssigneeRole.VICE_PRESIDENT.getKey(), vicePresident);
        }

        // 購置物聯網設備 (一般未滿30萬<300000)
      } else if (dto.getRadio1() == "1" && dto.getRadio2() == "1") {

        processKey = ProcessDefinitionKey.PURCHASEREQUISTION_IOTEQUIPMENT.getKey();
        List<SyncOuWithManagerDto> syncOuWithManagerDtoList = syncOURepository.findSupervisor(hrid);

        List<String> approvers = new ArrayList<>();

        for (SyncOuWithManagerDto syncOuWithManagerDto : syncOuWithManagerDtoList) {
          int level = Integer.parseInt(syncOuWithManagerDto.getOuLevel());
          if (level > 30) {
            approvers.add(syncOuWithManagerDto.getHrid());
          } else if (level == 30) {
            // 申請單位主管
            approvers.add(syncOuWithManagerDto.getHrid());
            vars.put(AssigneeRole.APPLICANT_SUPERVISOR.getKey(), syncOuWithManagerDto.getHrid());
          } else if (level == 20) {
            // 副總
            vicePresident = syncOuWithManagerDto.getHrid();
          } else {
            throw new RuntimeException("組織層級有誤");
          }
        }

        if (approvers.size() == 0) {
          throw new RuntimeException("非單位主管以上，但找不到上層主管");
        }

        vars.put(AssigneeRole.APPROVERS.getKey(), approvers);
        vars.put(AssigneeRole.CURRENT_INDEX.getKey(), 0);
        vars.put(AssigneeRole.CURRENT_APPROVER.getKey(), approvers.get(0));

        // 分派任務(採購經辦)
        vars.put(AssigneeRole.ASSIGN_PURCHASE_CLERK.getKey(), bpmNcccMissionAssignerRepository
            .findByOuCode(AssigneeRole.PURCHASE_CLERK.getCode()).getHrid());
        // 採購經辦
        vars.put(AssigneeRole.PURCHASE_CLERK.getKey(), null);
        // 採購科長
        setAssignee(vars, AssigneeRole.PURCHASE_SECTION_CHIEF);
        // 採購經理
        setAssignee(vars, AssigneeRole.PURCHASE_MANAGER);
        // 採購協理
        setAssignee(vars, AssigneeRole.PURCHASE_ASSOCIATE);
        // 採購資協
        setAssignee(vars, AssigneeRole.PURCHASE_SENIOR_ASSOCIATE);

        // 分派任務(資訊經辦)
        vars.put(AssigneeRole.ASSIGN_IT_CLERK.getKey(), bpmNcccMissionAssignerRepository
            .findByOuCode(AssigneeRole.IT_CLERK.getCode()).getHrid());
        // 資訊經辦
        vars.put(AssigneeRole.IT_CLERK.getKey(), null);
        // 資訊科長
        setAssignee(vars, AssigneeRole.IT_SECTION_CHIEF);
        // 資訊經理
        setAssignee(vars, AssigneeRole.IT_MANAGER);
        // 資訊協理
        setAssignee(vars, AssigneeRole.IT_ASSOCIATE);
        // 資訊資協
        setAssignee(vars, AssigneeRole.IT_SENIOR_ASSOCIATE);

        // 分派任務(成本經辦)
        vars.put(AssigneeRole.ASSIGN_COST_CLERK.getKey(), bpmNcccMissionAssignerRepository
            .findByOuCode(AssigneeRole.COST_CLERK.getCode()).getHrid());
        // 成本經辦
        vars.put(AssigneeRole.COST_CLERK.getKey(), null);
        // 成本科長
        setAssignee(vars, AssigneeRole.COST_SECTION_CHIEF);
        // 會計經理
        setAssignee(vars, AssigneeRole.ACCOUNTING_MANAGER);
        // 會計協理
        setAssignee(vars, AssigneeRole.ACCOUNTING_ASSOCIATE);
        // 會計資協
        setAssignee(vars, AssigneeRole.ACCOUNTING_SENIOR_ASSOCIATE);
        // 行管部主管
        setAssignee(vars, AssigneeRole.ADMINISTRATIVE_MANAGER);
        // 副總經理
        vars.put(AssigneeRole.VICE_PRESIDENT.getKey(), vicePresident);
        // 總經理
        setAssignee(vars, AssigneeRole.PRESIDENT);
        // 董事長
        setAssignee(vars, AssigneeRole.CHAIRMAN);

        vars.put(AssigneeRole.SAP_USER.getKey(), null);
        // 購置電腦周邊軟硬體 (一般未滿30萬<300000)
      } else if (dto.getRadio1() == "1" && dto.getRadio2() == "2") {

        // 簽呈請購(電子化公文系統) && 電腦需求單請購 (線下作業紙本)
      } else if (dto.getRadio1() == "2" && dto.getRadio2().isEmpty()) {

      }


		
		//String taskId = flowableService.startProcess("請購單流程", prNo, vars);
		String taskId = "";
		return taskId;
	}

	@Override
	public List<NcccCostCenter> getCostCenters() {
		return costCenterRepo.findAll();
	}

	@Override
	public List<CategoryOptionDto> listCategoryOptions() {
      List<NcccPurchaseCategoryNumber> all = ncccPurchaseCategoryNumberRepo.findAll();
		return all.stream().map(e -> new CategoryOptionDto(e.getCategoryNumber(), // value = 品號
				e.getCategoryName() // label = 品名
		)).collect(Collectors.toList());
	}

    @Override
    public PurchaseRequestDto query(String prNo) {
      PurchaseRequestDto master = new PurchaseRequestDto();
      BpmPrM bpmPrM = bpmPrMRepo.findById(prNo)
          .orElseThrow(() -> new RuntimeException("找不到主檔: PR_NO=" + prNo));
      master.setPrNo(bpmPrM.getPrNo());
      master.setApplyDate(DateUtil.toYyyyMmDdString(bpmPrM.getApplyDate()));
      master.setEmpNo(bpmPrM.getEmpNo());
      master.setApplicant(bpmPrM.getApplicant());
      master.setDepartment(bpmPrM.getDepartment());
      master.setPoNo(bpmPrM.getPoNo());
      master.setRevNo(bpmPrM.getRevNo());
      master.setRadio1(bpmPrM.getPrType());
      master.setRadio2(bpmPrM.getItType());
      master.setRemark(bpmPrM.getRemark());
      master.setAccountComment(bpmPrM.getRemarkAcc());
      master.setDocNo(bpmPrM.getDocNo());
      master.setDemandOrder(bpmPrM.getDemandOrder());
      master.setDecisionDate(DateUtil.toYyyyMmDdString(bpmPrM.getApprovalDate()));
      master.setTotalAmount(StringUtil.bigDecimalToString(bpmPrM.getTotalAmount()));
      master.setProcurementHandler(bpmPrM.getEmpPoNo());
      master.setInquiryAmount(StringUtil.bigDecimalToString(bpmPrM.getInquiryAmount()));
      BpmPrItComment itComment = itCommentRepo.findByPrNo(prNo);
      if (itComment != null) {
        if (itComment.getRemark() != null) {
          master.setItCommentRemark(itComment.getRemark());
        }

        if (itComment.getAttachment() != null) {
          try {
            master.setAttachment(FileUtil.getAllFiles(itComment.getAttachment()));
          } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
          }
        }
      }

      List<BpmPrMD1> bpmPrMD1List = bpmPrMDlRepo.findByPrNo(prNo);
      List<PurchaseDetailDto> detailList = new ArrayList<>();
      for (BpmPrMD1 bpmPrMD1 : bpmPrMD1List) {
        PurchaseDetailDto detail = new PurchaseDetailDto();
        detail.setPrItemNo(bpmPrMD1.getPrItemNo());
        detail.setItemCode(bpmPrMD1.getItemCode());
        detail.setDetailRemark(bpmPrMD1.getRemark());
        detail.setDemandDateByFlow(bpmPrMD1.getFlowDemandDate());
        detail.setDemandDate(DateUtil.toYyyyMmDdString(bpmPrMD1.getDemandDate()));
        detail.setLocation(bpmPrMD1.getLocation());
        detail.setQty(StringUtil.bigDecimalToString(bpmPrMD1.getQty()));
        detail.setUnitOfMeasurement(bpmPrMD1.getQtyUnit());
        detail.setEstimatedAmount(StringUtil.bigDecimalToString(bpmPrMD1.getPrice()));
        detail.setTotal(StringUtil.bigDecimalToString(bpmPrMD1.getTotal()));
        detail.setTax(StringUtil.bigDecimalToString(bpmPrMD1.getTax()));
        detail.setAutoTax(true);
        detail.setBudgetDepartment(bpmPrMD1.getOuCode());
        List<BpmPrBpList> bpmPrBpList =
            bpListRepo.findByPrNoAndPrItemNo(prNo, bpmPrMD1.getPrItemNo());
        List<VendorQuoteDto> vendorQuoteDtoList = new ArrayList<>();
        for (BpmPrBpList bpmPrBp : bpmPrBpList) {
          VendorQuoteDto vendorQuoteDto = new VendorQuoteDto();
          vendorQuoteDto.setBpListBpName(bpmPrBp.getBpName());
          vendorQuoteDto.setBpListPrice(StringUtil.bigDecimalToString(bpmPrBp.getPrice()));
          vendorQuoteDto.setBpListTax(StringUtil.bigDecimalToString(bpmPrBp.getTax()));
          vendorQuoteDto.setBpListTotal(StringUtil.bigDecimalToString(bpmPrBp.getTotal()));
          vendorQuoteDtoList.add(vendorQuoteDto);
        }
        detail.setVendorQuotes(vendorQuoteDtoList);
        detailList.add(detail);
      }
      master.setDetails(detailList);
      return master;
    }

    // 指派
    private Map<String, Object> setAssignee(Map<String, Object> vars, AssigneeRole assigneeRole) {
      vars.put(assigneeRole.getKey(),
          syncOURepository.findByOuCodeWithManager(assigneeRole.getCode()).getHrid());
      return vars;
    }
}
