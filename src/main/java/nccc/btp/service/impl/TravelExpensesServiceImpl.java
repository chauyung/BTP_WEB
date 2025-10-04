package nccc.btp.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.dto.SyncOuWithManagerDto;
import nccc.btp.entity.BpmBtM;
import nccc.btp.entity.BpmBtMD1;
import nccc.btp.entity.NcccCostcenterOrgMapping;
import nccc.btp.entity.NcccExpenseCategoryNumber;
import nccc.btp.entity.SapBpmBtStatus;
import nccc.btp.entity.SyncOU;
import nccc.btp.entity.SyncUser;
import nccc.btp.enums.AssigneeRole;
import nccc.btp.enums.Decision;
import nccc.btp.enums.Mode;
import nccc.btp.enums.ProcessDefinitionKey;
import nccc.btp.repository.BpmBtMD1Repository;
import nccc.btp.repository.BpmBtMRepository;
import nccc.btp.repository.BpmNcccMissionAssignerRepository;
import nccc.btp.repository.LevAgentRepository;
import nccc.btp.repository.NcccCostcenterOrgMappingRepository;
import nccc.btp.repository.NcccExpenseCategoryNumberRepository;
import nccc.btp.repository.NcccReceiptTypeRepository;
import nccc.btp.repository.SapBpmBtStatusRepository;
import nccc.btp.repository.SyncOURepository;
import nccc.btp.repository.SyncUserRepository;
import nccc.btp.rfc.SapUtil;
import nccc.btp.rfc.TInput;
import nccc.btp.rfc.ZHeader;
import nccc.btp.rfc.ZInvoice;
import nccc.btp.rfc.ZItem;
import nccc.btp.rfc.ZReturn;
import nccc.btp.service.FlowableService;
import nccc.btp.service.NcccBudgetService;
import nccc.btp.service.TravelExpensesService;
import nccc.btp.util.FileUtil;
import nccc.btp.util.MoneyUtil;
import nccc.btp.util.SecurityUtil;
import nccc.btp.vo.AssignTasksVo;
import nccc.btp.vo.BudgetVo.BudgetTranscation;
import nccc.btp.vo.DecisionVo;
import nccc.btp.vo.TravelExpensesAccountingDecisionVo;
import nccc.btp.vo.TravelExpensesVo;

@Service
@Transactional
public class TravelExpensesServiceImpl implements TravelExpensesService {

  @Autowired
  private FlowableService flowableService;

  @Autowired
  private NcccReceiptTypeRepository ncccReceiptTypeRepository;

  @Autowired
  private NcccCostcenterOrgMappingRepository ncccCostcenterOrgMappingRepository;

  @Autowired
  private BpmBtMRepository bpmBtMRepository;

  @Autowired
  private BpmBtMD1Repository bpmBtMD1Repository;

  @Autowired
  private LevAgentRepository levAgentRepository;

  @Autowired
  private SapBpmBtStatusRepository sapBpmBtStatusRepository;

  @Autowired
  private BpmNcccMissionAssignerRepository bpmNcccMissionAssignerRepository;

  @Autowired
  private SyncUserRepository syncUserRepository;

  @Autowired
  private SyncOURepository syncOURepository;

  @Autowired
  private NcccBudgetService ncccBudgetService;

  @Autowired
  private SapUtil sapUtil;

  protected static Logger LOG = LoggerFactory.getLogger(TravelExpensesServiceImpl.class);

  private static final String TEMPLATE_PATH = "template/travelExpenses_sample.csv";

  // 憑證自動產生
  private static final List<String> AUTO_GENERATE_CERTIFICATE_CODE =
      Arrays.asList("火(汽)車票根", "國內機票相關", "高鐵票根", "船舶票根");

  // V0 V9不需要跑GUI
  private static final List<String> GUI_SKIP = Arrays.asList("V0", "V9");

  @Autowired
  private NcccExpenseCategoryNumberRepository ncccExpenseCategoryNumberRepository;

  @Override
  public TravelExpensesVo init() {
    TravelExpensesVo travelExpensesVo = new TravelExpensesVo();
    travelExpensesVo.setNcccReceiptTypeList(ncccReceiptTypeRepository.findAll());
    travelExpensesVo.setNcccCostcenterOrgMappingList(ncccCostcenterOrgMappingRepository.findAll());
    List<String> types = Arrays.asList("00020", "00026", "00025");
    NcccUserDto user = SecurityUtil.getCurrentUser();
    travelExpensesVo.setLevAgentList(levAgentRepository
        .findAllByEmpIdAndEformStatusCodAndLevTypeCodInAndBtStatusIsNullAndBtNoIsNull(
            user.getHrid(), "通過", types));
    travelExpensesVo.setMode(Mode.ADDMODE);
    return travelExpensesVo;
  }

  @Override
  public String startProcess(TravelExpensesVo vo, List<MultipartFile> files) {
    // 檢查憑證是否有存在DB 有代表被使用過(不包含作廢的)
    List<String> codes =
        vo.getBpmBtMD1List().stream().map(BpmBtMD1::getCertificateCode).filter(Objects::nonNull)
            .map(String::trim).filter(s -> !s.isEmpty()).distinct().collect(Collectors.toList());
    if (!codes.isEmpty()) {
      List<String> conflicts = bpmBtMD1Repository.findConflictedCertificateCodes(codes);
      if (!conflicts.isEmpty()) {
        throw new IllegalStateException("以下憑證號已使用，請檢查：" + String.join(", ", conflicts));
      }
      // 呼叫SAP GUI檢核
      List<TInput> tInputList = buildTInputList(vo.getBpmBtMD1List());
      Map<String, String> result;
      try {
        result = sapUtil.callZAccfTwguiCheck01(tInputList);
        if (result.get("E_ERROR") != null && !"".equals(result.get("E_ERROR"))) {
          throw new RuntimeException("SAP GUI檢核失敗:" + result.get("E_XBLNR") + result.get("E_MSG"));
        }
      } catch (Exception e) {
        LOG.error(e.getMessage());
        throw new RuntimeException("SAP GUI檢核異常:" + e.getMessage());
      }
    }

    BpmBtM bpmBtM = vo.getBpmBtM();
    // 把多個檔壓成一個 ZIP
    if (files != null && !files.isEmpty()) {
      try {
        byte[] Bytes = FileUtil.compressToZip(files);
        bpmBtM.setAttachment(Bytes);
      } catch (IOException e) {
        LOG.error(e.toString());
        e.printStackTrace();
      }
    }
    NcccUserDto user = SecurityUtil.getCurrentUser();
    // 用來判斷是否是單位主管以上
    boolean isSupervisor = false;
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
    BigDecimal amount = bpmBtM.getApplyAmount();
    vars.put("amount", amount);
    // 用來判斷是否是董事長
    boolean isChairman = false;
    // 副總依照各個部門往上找會不一樣 額外處理
    String vicePresident = null;
    if (bpmBtM.getApplyType().equals("51020701")) {
      // 國內出差
      if (isSupervisor) {
        // 單位主管以上
        processKey = ProcessDefinitionKey.DOMESTIC_BUSINESS_TRIP_SUPERVISOR_AND_ABOVE.getKey();
        if (syncUser.getLevelId() == 16) {
          // 董事長
          isChairman = true;
        } else if (syncUser.getLevelId() == 15) {
          // 總經理(董事長當申請單位主管)
          vars.put(AssigneeRole.APPLICANT_SUPERVISOR.getKey(),
              getManagerHridByOuCode(AssigneeRole.CHAIRMAN.getCode()));
        } else if (syncUser.getLevelId() == 14 || syncUser.getLevelId() == 13) {
          // 副總經理(總經理當申請單位主管)
          vars.put(AssigneeRole.APPLICANT_SUPERVISOR.getKey(),
              getManagerHridByOuCode(AssigneeRole.PRESIDENT.getCode()));
        } else {
          // 其他主管(副總經理當申請單位主管)
          vars.put(AssigneeRole.APPLICANT_SUPERVISOR.getKey(),
              getManagerHridByOuCode(AssigneeRole.VICE_PRESIDENT.getCode()));
        }
        vars.put("isChairman", isChairman);
      } else {
        // 一般同仁
        processKey = ProcessDefinitionKey.DOMESTIC_BUSINESS_TRIP_GENERAL_COLLEAGUES.getKey();
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
      }
      // 國內出差共用設定
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
      if (amount.compareTo(BigDecimal.valueOf(10000)) > 0) {
        setAssignee(vars, AssigneeRole.ADMINISTRATIVE_MANAGER);
      }
      // 副總經理
      if (amount.compareTo(BigDecimal.valueOf(50000)) > 0) {
        vars.put(AssigneeRole.VICE_PRESIDENT.getKey(), vicePresident);
      }
      // 總經理
      if (amount.compareTo(BigDecimal.valueOf(1000000)) >= 0) {
        setAssignee(vars, AssigneeRole.PRESIDENT);
      }
      // 董事長
      if (amount.compareTo(BigDecimal.valueOf(10000000)) >= 0) {
        setAssignee(vars, AssigneeRole.CHAIRMAN);
      }
      vars.put(AssigneeRole.SAP_USER.getKey(), null);

    } else {
      // 國外出差
      if (isSupervisor) {
        // 單位主管以上
        processKey = ProcessDefinitionKey.OVERSEAS_BUSINESS_TRIP_SUPERVISOR_AND_ABOVE.getKey();
        if (syncUser.getLevelId() == 16) {
          // 董事長
          isChairman = true;
        }
        vars.put("isChairman", isChairman);
      } else {
        // 一般同仁
        processKey = ProcessDefinitionKey.DOMESTIC_BUSINESS_TRIP_GENERAL_COLLEAGUES.getKey();
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
      }
      // 國外出差共用設定
      // 分派任務(會計經辦)
      vars.put(AssigneeRole.ASSIGN_ACCOUNTING_CLERK.getKey(), bpmNcccMissionAssignerRepository
          .findByOuCode(AssigneeRole.ACCOUNTING_CLERK.getCode()).getHrid());
      // 會計經辦
      vars.put(AssigneeRole.ACCOUNTING_CLERK.getKey(), null);
      // 會計科長
      setAssignee(vars, AssigneeRole.ACCOUNTING_SECTION_CHIEF);
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
      if (amount.compareTo(BigDecimal.valueOf(50000)) >= 0) {
        vars.put(AssigneeRole.VICE_PRESIDENT.getKey(), vicePresident);
      }
      // 總經理
      if (amount.compareTo(BigDecimal.valueOf(1000000)) >= 0) {
        setAssignee(vars, AssigneeRole.PRESIDENT);
      }
      // 董事長
      if (amount.compareTo(BigDecimal.valueOf(10000000)) >= 0) {
        setAssignee(vars, AssigneeRole.CHAIRMAN);
      }
      vars.put(AssigneeRole.SAP_USER.getKey(), null);
    }
    String prefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));
    Integer nextSerial = bpmBtMRepository.findMaxSerialByPrefix(prefix) + 1;
    String serialStr = String.format("%04d", nextSerial);
    String btNo = "BT" + prefix + serialStr;
    int index = 0;
    for (BpmBtMD1 bpmBtMD1 : vo.getBpmBtMD1List()) {
      index += 1;
      if (bpmBtMD1.getCertificateType() != null && !GUI_SKIP.contains(bpmBtMD1.getMwskz())
          && AUTO_GENERATE_CERTIFICATE_CODE.contains(bpmBtMD1.getCertificateType())) {
        String formattedIndex = String.format("%02d", index);
        bpmBtMD1.setCertificateCode(prefix + formattedIndex);
      }
    }
    bpmBtM.setBtNo(btNo);
    bpmBtM.setFlowStatus("1");
    bpmBtM.setCreatedUser(user.getUserId());
    bpmBtM.setCreatedDate(LocalDate.now());
    BpmBtM saved = bpmBtMRepository.save(bpmBtM);
    List<BpmBtMD1> bpmBtMD1List = vo.getBpmBtMD1List();
    for (BpmBtMD1 bpmBtMD1 : bpmBtMD1List) {
      bpmBtMD1.setBtNo(btNo);
    }
    bpmBtMD1Repository.saveAll(bpmBtMD1List);
    String processId = flowableService.startProcess(processKey, btNo, vars);
    // 回寫 taskId
    saved.setTaskId(processId);
    bpmBtMRepository.save(saved);
    levAgentRepository.updateBtNoAndStatus(saved.getTravelNo(), saved.getBtNo(), "1");
    DecisionVo decisionVo = new DecisionVo();
    decisionVo.setProcessId(processId);
    decisionVo.setDecision(Decision.SUBMIT);
    flowableService.completeTask(decisionVo);

    // 動用增加
    BudgetTranscation budgetTranscation = new BudgetTranscation();
    budgetTranscation.setYear(bpmBtM.getYear());
    budgetTranscation.setVersion("2");
    NcccCostcenterOrgMapping ncccCostcenterOrgMapping =
        ncccCostcenterOrgMappingRepository.findByCostcenter(bpmBtMD1List.get(0).getCostCenter());
    budgetTranscation.setOuCode(ncccCostcenterOrgMapping.getBudgetOuCode());
    budgetTranscation.setAccounting(bpmBtM.getApplyType());
    budgetTranscation.setAmount(bpmBtM.getApplyAmount());
    budgetTranscation.setTranscationSource("出差");
    budgetTranscation.setTranscationNo(btNo);
    budgetTranscation.setTranscationDate(LocalDate.now());
    budgetTranscation.setTranscationType("動用");
    budgetTranscation.setBpNo(bpmBtM.getPayEmpNo());
    budgetTranscation.setBpName(bpmBtM.getPayEmpNo());
    budgetTranscation.setCreateUser(bpmBtM.getEmpNo());
    budgetTranscation.setCreateDate(LocalDateTime.now());
    ncccBudgetService.WriteBudgetTranscation(budgetTranscation);

    return btNo;
  }

  @Override
  public TravelExpensesVo query(String btNo) {
    TravelExpensesVo vo = new TravelExpensesVo();
    BpmBtM bpmBtM = bpmBtMRepository.findById(btNo)
        .orElseThrow(() -> new RuntimeException("找不到主檔: BT_NO=" + btNo));
    vo.setBpmBtM(bpmBtM);
    vo.setBpmBtMD1List(bpmBtMD1Repository.findByBtNo(btNo));
    if (bpmBtM.getAttachment() != null) {
      try {
        vo.setFileList(FileUtil.getAllFiles(bpmBtM.getAttachment()));
      } catch (IOException e) {
        LOG.error(e.getMessage());
        e.printStackTrace();
      }
    }
    vo.setTaskHistoryList(flowableService.getTaskHistory(bpmBtM.getTaskId()));
    if (flowableService.checkAtInitiatorTask(bpmBtM.getTaskId())) {
      vo.setMode(Mode.EDITMODE);
    } else {
      vo.setMode(Mode.VIEWMODE);
    }
    boolean isSetNextAssignee = flowableService.isSetNextAssignee(bpmBtM.getTaskId());
    vo.setSetNextAssignee(flowableService.isSetNextAssignee(bpmBtM.getTaskId()));
    if (isSetNextAssignee) {
      String nextDepId = flowableService.getNextDepId(bpmBtM.getTaskId());
      SyncOU syncOU = syncOURepository.findByOuCode(nextDepId);
      List<SyncUser> syncUserList = syncUserRepository
          .findByOuCodeAndAccountNotAndDisabled(syncOU.getOuCode(), syncOU.getMgrAccount(), "0");
      vo.setNextAssigneeList(syncUserList);
    }
    vo.setSapUser(flowableService.checkAtSapUserTask(bpmBtM.getTaskId()));
    // 如果是經辦 可以退回申請人 但不能退回上一層
    // 如果不是 可以退回上一層 不能退回申請人
    if (flowableService.canReturnToInitiator(bpmBtM.getTaskId())) {
      vo.setCanReturnToInitiator(true);
    } else {
      vo.setCanBackToPrevious(true);
    }
    SyncUser payEmpNo = syncUserRepository.findByHrid(vo.getBpmBtM().getPayEmpNo());
    if (payEmpNo != null) {
      vo.setPayEmpNoName(payEmpNo.getDisplayName());
    } else {
      vo.setPayEmpNoName("未知人員");
    }
    SyncUser tripMember = syncUserRepository.findByHrid(vo.getBpmBtM().getTripMember());
    if (tripMember != null) {
      vo.setTripMemberName(tripMember.getDisplayName());
    } else {
      vo.setTripMemberName("未知人員");
    }
    return vo;
  }

  @Override
  public String decision(DecisionVo vo) {
    boolean flag = flowableService.completeTask(vo);
    if (flag) {
      if (vo.getDecision().isReturn() || vo.getDecision().isBack()) {
        if (flowableService.checkAtInitiatorTask(vo.getProcessId())) {
          BpmBtM bpmBtM = bpmBtMRepository.findByTaskId(vo.getProcessId());
          if (bpmBtM == null) {
            throw new RuntimeException("找不到BpmBtM，taskId=" + vo.getProcessId());
          }
          List<BpmBtMD1> bpmBtMD1List = bpmBtMD1Repository.findByBtNo(bpmBtM.getBtNo());
          // 退回到申請人 要還預算
          BudgetTranscation budgetTranscation = new BudgetTranscation();
          budgetTranscation.setYear(bpmBtM.getYear());
          budgetTranscation.setVersion("2");
          NcccCostcenterOrgMapping ncccCostcenterOrgMapping = ncccCostcenterOrgMappingRepository
              .findByCostcenter(bpmBtMD1List.get(0).getCostCenter());
          budgetTranscation.setOuCode(ncccCostcenterOrgMapping.getBudgetOuCode());
          budgetTranscation.setAccounting(bpmBtM.getApplyType());
          budgetTranscation.setAmount(bpmBtM.getApplyAmount().negate());
          budgetTranscation.setTranscationSource("出差");
          budgetTranscation.setTranscationNo(bpmBtM.getBtNo());
          budgetTranscation.setTranscationDate(LocalDate.now());
          budgetTranscation.setTranscationType("動用");
          budgetTranscation.setBpNo(bpmBtM.getPayEmpNo());
          budgetTranscation.setBpName(bpmBtM.getPayEmpNo());
          budgetTranscation.setCreateUser(bpmBtM.getEmpNo());
          budgetTranscation.setCreateDate(LocalDateTime.now());
          ncccBudgetService.WriteBudgetTranscation(budgetTranscation);
        }
      }
      // 判斷流程是否結束
      if (flowableService.isProcessFinishedByHistory(vo.getProcessId())) {
        BpmBtM bpmBtM = bpmBtMRepository.findByTaskId(vo.getProcessId());
        if (bpmBtM == null) {
          throw new RuntimeException("找不到BpmBtM，taskId=" + vo.getProcessId());
        }
        if (vo.getDecision().isInvalid()) {
          bpmBtM.setFlowStatus("3");
        } else {
          bpmBtM.setFlowStatus("2");
        }
        bpmBtMRepository.save(bpmBtM);
      }
      return "簽核成功";
    } else {
      return "簽核失敗";
    }
  }

  @Override
  public String accountingDecision(TravelExpensesAccountingDecisionVo vo) {
    BpmBtM bpmBtM = vo.getBpmBtM();
    int updated = bpmBtMRepository.updateFields(bpmBtM.getBtNo(), bpmBtM.getTripMember(),
        bpmBtM.getPostingDate(), bpmBtM.getItemText(), bpmBtM.getApplyAmount(), bpmBtM.getTax(),
        bpmBtM.getUntaxAmount());
    if (updated != 1) {
      throw new RuntimeException("BpmBtM更新失敗，BT_NO=" + bpmBtM.getBtNo());
    }
    for (BpmBtMD1 bpmBtMD1 : vo.getBpmBtMD1List()) {
      int updated1 = bpmBtMD1Repository.updateFields(bpmBtMD1.getBtNo(), bpmBtMD1.getBtItemNo(),
          bpmBtMD1.getCostCenter(), bpmBtMD1.getDeduction(), bpmBtMD1.getRemark());
      if (updated1 != 1) {
        throw new RuntimeException(
            "BpmBtMD1更新失敗，BT_NO=" + bpmBtM.getBtNo() + " BT_ITEM_NO=" + bpmBtMD1.getBtItemNo());
      }
    }
    boolean flag = flowableService.completeTask(vo);
    if (flag) {
      if (vo.getDecision().isReturn() || vo.getDecision().isBack()) {
        if (flowableService.checkAtInitiatorTask(vo.getProcessId())) {
          // 退回到申請人 要還預算
          BudgetTranscation budgetTranscation = new BudgetTranscation();
          budgetTranscation.setYear(bpmBtM.getYear());
          budgetTranscation.setVersion("2");
          NcccCostcenterOrgMapping ncccCostcenterOrgMapping = ncccCostcenterOrgMappingRepository
              .findByCostcenter(vo.getBpmBtMD1List().get(0).getCostCenter());
          budgetTranscation.setOuCode(ncccCostcenterOrgMapping.getBudgetOuCode());
          budgetTranscation.setAccounting(bpmBtM.getApplyType());
          budgetTranscation.setAmount(bpmBtM.getApplyAmount().negate());
          budgetTranscation.setTranscationSource("出差");
          budgetTranscation.setTranscationNo(bpmBtM.getBtNo());
          budgetTranscation.setTranscationDate(LocalDate.now());
          budgetTranscation.setTranscationType("動用");
          budgetTranscation.setBpNo(bpmBtM.getPayEmpNo());
          budgetTranscation.setBpName(bpmBtM.getPayEmpNo());
          budgetTranscation.setCreateUser(bpmBtM.getEmpNo());
          budgetTranscation.setCreateDate(LocalDateTime.now());
          ncccBudgetService.WriteBudgetTranscation(budgetTranscation);
        }
      }
      // 判斷流程是否結束
      if (flowableService.isProcessFinishedByHistory(vo.getProcessId())) {
        if (vo.getDecision().isInvalid()) {
          bpmBtM.setFlowStatus("3");
        } else {
          bpmBtM.setFlowStatus("2");
        }
        bpmBtMRepository.save(bpmBtM);
      }
      return "簽核成功";
    } else {
      return "簽核失敗";
    }
  }

  @Override
  public String update(TravelExpensesVo vo, List<MultipartFile> files) {
    // 檢查憑證是否有存在DB 有代表被使用過(不包含作廢的)
    List<String> codes =
        vo.getBpmBtMD1List().stream().map(BpmBtMD1::getCertificateCode).filter(Objects::nonNull)
            .map(String::trim).filter(s -> !s.isEmpty()).distinct().collect(Collectors.toList());
    if (!codes.isEmpty()) {
      List<String> conflicts = bpmBtMD1Repository.findConflictedCertificateCodes(codes);
      if (!conflicts.isEmpty()) {
        throw new IllegalStateException("以下憑證號已使用，請檢查：" + String.join(", ", conflicts));
      }
      // 呼叫SAP GUI檢核
      List<TInput> tInputList = buildTInputList(vo.getBpmBtMD1List());
      Map<String, String> result;
      try {
        result = sapUtil.callZAccfTwguiCheck01(tInputList);
        if (result.get("E_ERROR") != null && !"".equals(result.get("E_ERROR"))) {
          throw new RuntimeException("SAP GUI檢核失敗:" + result.get("E_XBLNR") + result.get("E_MSG"));
        }
      } catch (Exception e) {
        LOG.error(e.getMessage());
        throw new RuntimeException("SAP GUI檢核異常:" + e.getMessage());
      }
    }
    BpmBtM bpmBtM = vo.getBpmBtM();
    String btNo = bpmBtM.getBtNo();
    String prefix = btNo.substring(2, btNo.length());
    int index = 0;
    for (BpmBtMD1 bpmBtMD1 : vo.getBpmBtMD1List()) {
      index += 1;
      if (bpmBtMD1.getCertificateType() != null && !GUI_SKIP.contains(bpmBtMD1.getMwskz())
          && AUTO_GENERATE_CERTIFICATE_CODE.contains(bpmBtMD1.getCertificateType())) {
        String formattedIndex = String.format("%02d", index);
        bpmBtMD1.setCertificateCode(prefix + formattedIndex);
      }
    }
    // 把多個檔壓成一個 ZIP
    if (files != null && !files.isEmpty()) {
      try {
        byte[] Bytes = FileUtil.compressToZip(files);
        bpmBtM.setAttachment(Bytes);
      } catch (IOException e) {
        LOG.error(e.toString());
        e.printStackTrace();
      }
    }
    NcccUserDto user = SecurityUtil.getCurrentUser();
    bpmBtM.setFlowStatus("1");
    bpmBtM.setModifiedUser(user.getUserId());
    bpmBtM.setModifiedDate(LocalDate.now());
    bpmBtMRepository.save(bpmBtM);
    // 明細檔 先刪除再新增
    bpmBtMD1Repository.deleteByBtNo(btNo);
    List<BpmBtMD1> bpmBtMD1List = vo.getBpmBtMD1List();
    for (BpmBtMD1 bpmBtMD1 : bpmBtMD1List) {
      bpmBtMD1.setBtNo(btNo);
    }
    bpmBtMD1Repository.saveAll(bpmBtMD1List);
    DecisionVo decisionVo = new DecisionVo();
    decisionVo.setProcessId(vo.getBpmBtM().getTaskId());
    decisionVo.setDecision(Decision.SUBMIT);
    flowableService.completeTask(decisionVo);
    // 動用增加
    BudgetTranscation budgetTranscation = new BudgetTranscation();
    budgetTranscation.setYear(bpmBtM.getYear());
    budgetTranscation.setVersion("2");
    NcccCostcenterOrgMapping ncccCostcenterOrgMapping =
        ncccCostcenterOrgMappingRepository.findByCostcenter(bpmBtMD1List.get(0).getCostCenter());
    budgetTranscation.setOuCode(ncccCostcenterOrgMapping.getBudgetOuCode());
    budgetTranscation.setAccounting(bpmBtM.getApplyType());
    budgetTranscation.setAmount(bpmBtM.getApplyAmount());
    budgetTranscation.setTranscationSource("出差");
    budgetTranscation.setTranscationNo(btNo);
    budgetTranscation.setTranscationDate(LocalDate.now());
    budgetTranscation.setTranscationType("動用");
    budgetTranscation.setBpNo(bpmBtM.getPayEmpNo());
    budgetTranscation.setBpName(bpmBtM.getPayEmpNo());
    budgetTranscation.setCreateUser(bpmBtM.getEmpNo());
    budgetTranscation.setCreateDate(LocalDateTime.now());
    ncccBudgetService.WriteBudgetTranscation(budgetTranscation);
    return btNo;
  }

  @Override
  public String toSAP(TravelExpensesVo vo) {
    BpmBtM bpmBtM = vo.getBpmBtM();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    ZHeader header = new ZHeader();
    // 來源系統
    header.setZSYSTEM("BTP");
    // 公司代碼
    header.setBUKRS("1010");
    // 文件日期
    header.setBLDAT(bpmBtM.getPostingDate().format(formatter));
    // 過帳日期
    header.setBUDAT(bpmBtM.getPostingDate().format(formatter));
    // 文件類型
    header.setBLART("KR");
    // 幣別
    header.setWAERS("TWD");
    // 參考
    header.setXBLNR("");
    // 參考
    // 當有一筆憑證種類為憑證後補時，固定填入: "憑證後補日:" 其餘留空
    for (BpmBtMD1 bpmBtMD1 : vo.getBpmBtMD1List()) {
      if (bpmBtMD1.getCertificateType().equals("憑證後補")) {
        header.setXBLNR("憑證後補日:");
      }
    }
    // 文件表頭的參考碼 1 內部
    bpmBtM.setBtNo(bpmBtM.getBtNo());
    header.setXREF1_HD(bpmBtM.getBtNo());

    ArrayList<ZItem> zitemList = new ArrayList<ZItem>();

    // 應付
    ZItem item = new ZItem();
    // 過帳碼
    item.setBSCHL("31");
    // 科目 (指定付款對象)
    item.setNEWKO("E" + bpmBtM.getPayEmpNo());
    // 文件幣別金額
    item.setWRBTR(bpmBtM.getApplyAmount().toString());
    // 先註解 未來可能改規格
    // 稅碼拿第一筆抵扣為Y的
    // boolean found = false;
    // for (BpmBtMD1 bpmBtMD1 : vo.getBpmBtMD1List()) {
    // if (bpmBtMD1.getDeduction().equals("Y")) {
    // item.setMWSKZ(bpmBtMD1.getMwskz());
    // found = true;
    // break;
    // }
    // }
    // // 如果都沒有 就塞第一筆稅碼
    // if (!found) {
    // item.setMWSKZ(vo.getBpmBtMD1List().get(0).getMwskz());
    // }
    // 到期日
    item.setZFBDT(bpmBtM.getSpecialPayDate().format(formatter));
    // 內文
    item.setSGTXT("應付" + bpmBtM.getItemText() + "等");
    zitemList.add(item);


    Map<String, List<BpmBtMD1>> map = new HashMap<>();
    for (BpmBtMD1 bpmBtMD1 : vo.getBpmBtMD1List()) {
      // 依照稅碼分類
      map.computeIfAbsent(bpmBtMD1.getMwskz(), k -> new ArrayList<>()).add(bpmBtMD1);
    }
    for (Map.Entry<String, List<BpmBtMD1>> entry : map.entrySet()) {
      List<BpmBtMD1> list = entry.getValue();
      BigDecimal untaxAmountTotal = new BigDecimal(0);
      BigDecimal taxTotal = new BigDecimal(0);
      for (BpmBtMD1 bpmBtMD1 : list) {
        untaxAmountTotal = untaxAmountTotal.add(bpmBtMD1.getUntaxAmount());
        taxTotal = taxTotal.add(bpmBtMD1.getTax());
      }

      // 品號品名
      ZItem item1 = new ZItem();
      // 過帳碼
      item1.setBSCHL("40");
      // 科目
      item1.setNEWKO(bpmBtM.getApplyType());
      // 文件幣別金額
      item1.setWRBTR(untaxAmountTotal.toString());
      // 稅碼
      item1.setMWSKZ(entry.getKey());
      // 成本中心
      item1.setKOSTL(vo.getBpmBtMD1List().get(0).getCostCenter());
      // 內文
      item1.setSGTXT(bpmBtM.getItemText());
      zitemList.add(item1);

      // 進項稅
      ZItem item2 = new ZItem();
      if (taxTotal.compareTo(BigDecimal.ZERO) == 0) {
        continue;
      }
      // 過帳碼
      item2.setBSCHL("40");
      // 科目
      item2.setNEWKO("11110601");
      // 文件幣別金額
      item2.setWRBTR(taxTotal.toString());
      // 稅碼
      item2.setMWSKZ(entry.getKey());
      // 稅基
      item2.setTXBFW(untaxAmountTotal.toString());
      // 內文
      item2.setSGTXT(bpmBtM.getItemText());
      zitemList.add(item2);
    }
    for (BpmBtMD1 bpmBtMD1 : vo.getBpmBtMD1List()) {
      if (bpmBtMD1.getDeduction() != null && bpmBtMD1.getDeduction().equals("Y")) {
        ZInvoice invoice = new ZInvoice();
        // 發票號碼
        invoice.setINVNO(bpmBtMD1.getCertificateCode());
        // 發票日期
        invoice.setGUIDATE(bpmBtMD1.getCertificateDate().format(formatter));
        // 稅基
        invoice.setTXBHW(null);
        // 稅額
        invoice.setMWSTS(null);
        // 稅格式
        invoice.setGUIFORMAT(null);
        // 統一編號
        invoice.setVATCODE(null);
      }
    }
    List<TInput> tInputList = buildTInputList(vo.getBpmBtMD1List());
    Map<String, String> result;
    try {
      result = sapUtil.callZAccfTwguiCheck01(tInputList);
      if (result.get("E_ERROR") == null || "".equals(result.get("E_ERROR"))) {
        ZReturn zreturn = sapUtil.callZcreateAccDocument(header, zitemList);
        SapBpmBtStatus sapBpmBtStatus = new SapBpmBtStatus();
        sapBpmBtStatus.setBtNo(bpmBtM.getBtNo());
        sapBpmBtStatus.setType(zreturn.getTYPE());
        sapBpmBtStatus.setBukrs(zreturn.getBUKRS());
        sapBpmBtStatus.setBelnr(zreturn.getBELNR());
        sapBpmBtStatus.setGjahr(zreturn.getGJAHR());
        sapBpmBtStatus.setMessage(zreturn.getMESSAGE());
        sapBpmBtStatusRepository.save(sapBpmBtStatus);
        if (zreturn.getTYPE().equals("S")) {
          sapUtil.callZAccfTwguiInsert01(sapBpmBtStatus.getBukrs(), sapBpmBtStatus.getBelnr(),
              sapBpmBtStatus.getGjahr(), tInputList);
          levAgentRepository.updateBtStatusByLeApplyNo(bpmBtM.getTravelNo(), "2");
          // 結束流程
          DecisionVo decisionVo = new DecisionVo();
          decisionVo.setProcessId(vo.getBpmBtM().getTaskId());
          decisionVo.setDecision(Decision.END);
          flowableService.completeTask(decisionVo);
          return "拋轉成功";
        } else {
          return "拋轉失敗，" + sapBpmBtStatus.getMessage();
        }
      } else {
        SapBpmBtStatus sapBpmBtStatus = new SapBpmBtStatus();
        sapBpmBtStatus.setBtNo(bpmBtM.getBtNo());
        sapBpmBtStatus.setType("E");
        sapBpmBtStatus.setMessage(result.get("E_XBLNR") + result.get("E_MSG"));
        sapBpmBtStatusRepository.save(sapBpmBtStatus);
        return "拋轉失敗，" + sapBpmBtStatus.getMessage();
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "";
  }

  /**
   * TODO
   * 
   * @param assignTasksVo
   * @return
   */
  @Override
  public String setNextAssignee(AssignTasksVo assignTasksVo) {
    flowableService.setNextAssignee(assignTasksVo.getProcessId(), assignTasksVo.getNextAssignee());
    return "指派任務完成";
  }

  @Override
  public Resource getTravelExpensesCsvSample() throws IOException {
    ClassPathResource resource = new ClassPathResource(TEMPLATE_PATH);
    if (!resource.exists()) {
      throw new IOException("template is not found : " + TEMPLATE_PATH);
    }
    return resource;
  }

  private Map<String, Object> setAssignee(Map<String, Object> vars, AssigneeRole assigneeRole) {
    vars.put(assigneeRole.getKey(),
        syncOURepository.findByOuCodeWithManager(assigneeRole.getCode()).getHrid());
    return vars;
  }

  private String getManagerHridByOuCode(String ouCode) {
    return syncUserRepository.findManagerUserByOuCode(ouCode).map(SyncUser::getHrid)
        .orElseThrow(() -> new RuntimeException("找不到 OU_CODE 為 " + ouCode + " 的主管帳號"));
  }

  private List<TInput> buildTInputList(List<BpmBtMD1> bpmBtMD1List) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    List<TInput> tInputList = new ArrayList<>();
    for (BpmBtMD1 bpmBtMD1 : bpmBtMD1List) {
      if (GUI_SKIP.contains(bpmBtMD1.getMwskz())) {
        // V0 V9不進GUI
        continue;
      }
      TInput tInput = new TInput();
      tInput.setBLDAT(bpmBtMD1.getCertificateDate().format(formatter));
      tInput.setVATDATE(bpmBtMD1.getCertificateDate().format(formatter));
      tInput.setXBLNR((bpmBtMD1.getCertificateCode()));
      if (bpmBtMD1.getZformCode() != null) {
        tInput.setZFORM_CODE(bpmBtMD1.getZformCode().toString());
      } else {
        tInput.setZFORM_CODE("");
      }
      tInput.setSTCEG(bpmBtMD1.getUniformNum());
      tInput.setHWBAS(bpmBtMD1.getUntaxAmount().toString());
      tInput.setHWSTE(bpmBtMD1.getTax().toString());
      tInput.setTAX_TYPE("1");
      tInput.setCUS_TYPE(bpmBtMD1.getCusType());
      tInput.setAM_TYPE("1");
      tInputList.add(tInput);
    }
    return tInputList;
  }

  // region 憑證黏貼單

  /**
   * 產生憑證黏貼單
   * 
   * @param btNo 差旅費單號
   * @return
   */
  public byte[] getVoucherStrickerWord(String btNo) {
    BpmBtM bpmBtM = bpmBtMRepository.findById(btNo)
        .orElseThrow(() -> new RuntimeException("找不到主檔: BT_NO=" + btNo));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    ClassPathResource resource = new ClassPathResource("template/voucherStickerTemplate.docx");
    try {

      // region 費用單類型

      NcccExpenseCategoryNumber expenseCategoryNumber =
          ncccExpenseCategoryNumberRepository.findByAccounting(bpmBtM.getApplyType());

      String costType = expenseCategoryNumber.getCategoryName();

      String applyType = "";

      switch (bpmBtM.getApplyType()) {
        case "51020701":

          applyType = "國內出差";

          break;

        case "51020702":

          applyType = "國外出差";

          break;

      }

      // endregion

      Map<String, String> replacements = new HashMap<>();
      replacements.put("{apartment}", bpmBtM.getDepartment());
      replacements.put("{name}", bpmBtM.getApplicant());
      replacements.put("{orderNo}", bpmBtM.getBtNo());
      replacements.put("{type}", applyType);
      replacements.put("{paidDate}",
          bpmBtM.getPostingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

      InputStream templateStream = resource.getInputStream();

      XWPFDocument document = new XWPFDocument(templateStream);

      // 替換段落中的文字
      for (XWPFParagraph paragraph : document.getParagraphs()) {
        replaceTextInParagraph(paragraph, replacements);
      }

      // 替換頁尾段落
      for (XWPFFooter footer : document.getFooterList()) {
        for (XWPFParagraph paragraph : footer.getParagraphs()) {
          replaceTextInParagraph(paragraph, replacements);
        }
      }

      // region 插入table值

      List<XWPFTable> tables = document.getTables();

      XWPFTable table = tables.get(0);

      List<BpmBtMD1> bpmBtMD1List = bpmBtMD1Repository.findByBtNo(btNo);

      BigDecimal totalApplyAmount = new BigDecimal(0);

      BigDecimal totalUntaxAmount = new BigDecimal(0);

      BigDecimal totalTaxAmount = new BigDecimal(0);

      BigDecimal totalDutyFreeAmount = new BigDecimal(0);

      BigDecimal totalZeroTaxAmount = new BigDecimal(0);

      int index = 1;

      for (BpmBtMD1 bpmBtMD1 : bpmBtMD1List) {
        totalApplyAmount = totalApplyAmount.add(bpmBtMD1.getApplyAmount());

        totalUntaxAmount = totalUntaxAmount.add(bpmBtMD1.getUntaxAmount());

        totalTaxAmount = totalTaxAmount.add(bpmBtMD1.getTax());

        // region 新增明細

        XWPFTableRow newRow = table.createRow();

        XWPFTableCell indexCell = newRow.getCell(0);

        indexCell.setText(String.valueOf(index));

        XWPFParagraph indexPara = indexCell.getParagraphs().get(0);

        indexPara.setAlignment(ParagraphAlignment.CENTER);

        XWPFTableCell certificateDateCell = newRow.getCell(1);

        certificateDateCell.setText(
            bpmBtMD1.getCertificateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        XWPFTableCell certificateCodeCell = newRow.getCell(2);

        certificateCodeCell.setText(bpmBtMD1.getCertificateCode());

        XWPFTableCell nameCell = newRow.getCell(3);

        nameCell.setText(costType);

        XWPFTableCell applyAmountCell = newRow.getCell(4);

        applyAmountCell.setText(MoneyUtil.formatTWD(bpmBtMD1.getApplyAmount()));

        XWPFParagraph applyAmountPara = applyAmountCell.getParagraphs().get(0);

        applyAmountPara.setAlignment(ParagraphAlignment.RIGHT);

        XWPFTableCell untaxAmountCell = newRow.getCell(5);

        untaxAmountCell.setText(MoneyUtil.formatTWD(bpmBtMD1.getUntaxAmount()));

        XWPFParagraph untaxAmountPara = untaxAmountCell.getParagraphs().get(0);

        untaxAmountPara.setAlignment(ParagraphAlignment.RIGHT);

        XWPFTableCell taxAmountCell = newRow.getCell(6);

        taxAmountCell.setText(MoneyUtil.formatTWD(bpmBtMD1.getTax()));

        XWPFParagraph taxAmountPara = taxAmountCell.getParagraphs().get(0);

        taxAmountPara.setAlignment(ParagraphAlignment.RIGHT);

        XWPFTableCell dutyFreeAmountCell = newRow.getCell(7);

        dutyFreeAmountCell.setText(MoneyUtil.formatTWD(new BigDecimal(0)));

        XWPFParagraph dutyFreeAmountPara = dutyFreeAmountCell.getParagraphs().get(0);

        dutyFreeAmountPara.setAlignment(ParagraphAlignment.RIGHT);

        XWPFTableCell zeroTaxAmountCell = newRow.getCell(8);

        zeroTaxAmountCell.setText(MoneyUtil.formatTWD(new BigDecimal(0)));

        XWPFParagraph zeroTaxAmountPara = zeroTaxAmountCell.getParagraphs().get(0);

        zeroTaxAmountPara.setAlignment(ParagraphAlignment.RIGHT);

        // 設定文字大小
        for (XWPFTableCell cell : newRow.getTableCells()) {
          for (XWPFParagraph paragraph : cell.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
              run.setFontSize(8); // 設定文字大小為 12pt
            }
          }
        }

        // endregion

        index++;

      }

      // region 合計值

      XWPFTable thisTable = tables.get(1);

      XWPFTableRow totalRow = thisTable.getRow(0);

      XWPFTableCell applyAmountCell = totalRow.getCell(4);

      applyAmountCell.setText(MoneyUtil.formatTWD(totalApplyAmount));

      XWPFParagraph applyAmountPara = applyAmountCell.getParagraphs().get(0);

      XWPFRun applyAmountRun = applyAmountPara.getRuns().get(0);

      applyAmountRun.setUnderline(UnderlinePatterns.SINGLE);

      XWPFTableCell untaxAmountCell = totalRow.getCell(5);

      untaxAmountCell.setText(MoneyUtil.formatTWD(totalUntaxAmount));

      XWPFParagraph untaxAmountPara = untaxAmountCell.getParagraphs().get(0);

      XWPFRun untaxAmountRun = untaxAmountPara.getRuns().get(0);

      untaxAmountRun.setUnderline(UnderlinePatterns.SINGLE);

      XWPFTableCell taxAmountCell = totalRow.getCell(6);

      taxAmountCell.setText(MoneyUtil.formatTWD(totalTaxAmount));

      XWPFParagraph taxAmountPara = taxAmountCell.getParagraphs().get(0);

      XWPFRun taxAmountRun = taxAmountPara.getRuns().get(0);

      taxAmountRun.setUnderline(UnderlinePatterns.SINGLE);

      XWPFTableCell dutyFreeAmountCell = totalRow.getCell(7);

      dutyFreeAmountCell.setText(MoneyUtil.formatTWD(totalDutyFreeAmount));

      XWPFParagraph dutyFreeAmountPara = dutyFreeAmountCell.getParagraphs().get(0);

      XWPFRun dutyFreeAmountRun = dutyFreeAmountPara.getRuns().get(0);

      dutyFreeAmountRun.setUnderline(UnderlinePatterns.SINGLE);

      XWPFTableCell zeroTaxAmountCell = totalRow.getCell(8);

      zeroTaxAmountCell.setText(MoneyUtil.formatTWD(totalZeroTaxAmount));

      XWPFParagraph zeroTaxAmountPara = zeroTaxAmountCell.getParagraphs().get(0);

      XWPFRun zeroTaxAmountRun = zeroTaxAmountPara.getRuns().get(0);

      zeroTaxAmountRun.setUnderline(UnderlinePatterns.SINGLE);

      // 設定文字大小
      for (XWPFTableCell cell : totalRow.getTableCells()) {
        for (XWPFParagraph paragraph : cell.getParagraphs()) {
          for (XWPFRun run : paragraph.getRuns()) {
            run.setFontSize(8); // 設定文字大小為 12pt
          }
        }
      }

      // endregion



      // endregion


      document.write(outputStream);
      document.close();



    } catch (Exception e) {
    }

    return outputStream.toByteArray();
  }

  private static void replaceTextInParagraph(XWPFParagraph paragraph,
      Map<String, String> replacements) {
    for (XWPFRun run : paragraph.getRuns()) {
      String text = run.getText(0);
      if (text != null) {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
          if (text.contains(entry.getKey())) {
            text = text.replace(entry.getKey(), entry.getValue());
            run.setText(text, 0);
          }
        }
      }
    }
  }

  // endregion

}


