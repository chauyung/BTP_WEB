package nccc.btp.service.impl;

import nccc.btp.dto.SyncOuWithManagerDto;
import nccc.btp.enums.AssigneeRole;
import nccc.btp.enums.Decision;
import nccc.btp.ftp.FtpsUtil;
import nccc.btp.repository.*;
import nccc.btp.rfc.*;
import nccc.btp.service.NcccBudgetService;
import nccc.btp.util.MoneyUtil;
import nccc.btp.vo.BpmPrepaidOrderVo;
import nccc.btp.vo.BpmExMDetailWHVo;
import nccc.btp.vo.BpmExMPplVo;
import nccc.btp.vo.DSApplicationExpensesVo;
import nccc.btp.vo.CApplicationExpensesVo;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.dto.ChtAccTaxCoCostDto;
import nccc.btp.enums.ProcessDefinitionKey;
import nccc.btp.entity.*;
import nccc.btp.vo.DecisionVo;
import nccc.btp.enums.Mode;
import nccc.btp.service.FlowableService;
import nccc.btp.util.FileUtil;
import nccc.btp.util.SecurityUtil;
import nccc.btp.util.DateUtil;
import nccc.btp.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.service.ApplicationExpensesService;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xwpf.usermodel.*;
import com.sap.conn.jco.JCoDestination;

import java.nio.charset.Charset;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static nccc.btp.enums.Decision.INVALID;

@Service
@Transactional
public class ApplicationExpensesServiceImpl implements ApplicationExpensesService {

    protected static Logger LOG = LoggerFactory.getLogger(ApplicationExpensesServiceImpl.class);

    @Autowired
    private FlowableService flowableService;

    @Autowired
    private NcccPayMethodRepository ncccPayMethodRepository;

    @Autowired
    private NcccCurrencyRepository ncccCurrencyRepository;

    @Autowired
    private NcccReceiptTypeRepository ncccReceiptTypeRepository;

    @Autowired
    private NcccCostCenterRepository ncccCostCenterRepository;

    @Autowired
    private NcccExpenseCategoryNumberRepository ncccExpenseCategoryNumberRepository;

    @Autowired
    private ZmmtSupplierRepository zmmtSupplierRepository;

    @Autowired
    private BpmExMRepository bpmExMRepository;

    @Autowired
    private BpmExMD1Repository bpmExMD1Repository;

    @Autowired
    private BpmPrepaidOrderRepository bpmPrepaidOrderRepository;

    @Autowired
    private BpmSplitMD1Repository bpmSplitMD1Repository;

    @Autowired
    private BpmExPcD1Repository bpmExPcD1Repository;

    @Autowired
    private NcccUtilityBudgetAttributionRepository ncccUtilityBudgetAttributionRepository;

    @Autowired
    private NcccUtilityBudgetAttributionBRepository ncccUtilityBudgetAttributionBRepository;

    @Autowired
    private SyncUserRepository syncUserRepository;

    @Autowired
    private BpmCHTPaymentRepository bpmCHTPaymentRepository;

    @Autowired
    private NcccIncomeTaxCategoryRepository ncccIncomeTaxCategoryRepository;

    @Autowired
    private BpmExDWHRepository bpmExDWHRepository;

    @Autowired
    private BpmExMWHRepository bpmExMWHRepository;

    @Autowired
    private SyncOURepository syncOURepository;

    @Autowired
    private BpmNcccMissionAssignerRepository bpmNcccMissionAssignerRepository;

    @Autowired
    private NcccDirectorSupervisorListRepository ncccDirectorSupervisorListRepository;

    @Autowired
    private NcccCommitteeListRepository ncccCommitteeListRepository;

    @Autowired
    private NcccIncomeTaxParameterRepository ncccIncomeTaxParameterRepository;

    @Autowired
    private BpmExPsDRepository bpmExPsDRepository;

    @Autowired
    private NcccIncomePersonRepository ncccIncomePersonRepository;

    @Autowired
    private NcccIncomeTaxDetailRepository ncccIncomeTaxDetailRepository;

    @Autowired
    private NcccWithholdTypeRepository ncccWithholdTypeRepository;

    @Autowired
    private SapBpmExStatusRepository sapBpmExStatusRepository;

    @Autowired
    private NcccCostcenterOrgMappingRepository ncccCostcenterOrgMappingRepository;

    @Autowired
    private NcccCashControllerRepository ncccCashControllerRepository;

    @Autowired
    private BpmSplitMRepository bpmSplitMRepository;

    @Autowired
    private BpmTaskItemRepository bpmTaskItemRepository;

    @Autowired
    private NcccBudgetService ncccBudgetService;

    @Autowired
    private SapUtil sapUtil;

    @Autowired
    @Qualifier("isdFtpsUtil")
    private FtpsUtil ftpsUtilISD;

    @Value("${ftp.ISDFTP.path}")
    private String isdFtpPath;

    // V0 V9不需要跑GUI
    private static final List<String> GUI_SKIP = Arrays.asList("V0", "V9");

    // 憑證自動產生
    private static final List<String> AUTO_GENERATE_CERTIFICATE_CODE =
            Arrays.asList("火(汽)車票根", "本島出發之船舶票根", "本島出發之國內機票相關", "高鐵票根" , "分攤證明單");

    private static final String TEMPLATE_PATH = "template/applicationExpenses_sample.csv";

    // region 取資料

    @Override
    public ApplicationExpensesVo init() {
        ApplicationExpensesVo applicationExpensesVo = new ApplicationExpensesVo();
        applicationExpensesVo.setNcccPayMethodList(ncccPayMethodRepository.findAll());
        applicationExpensesVo.setNcccCurrencyList(ncccCurrencyRepository.findAll());
        applicationExpensesVo.setNcccReceiptTypeList(ncccReceiptTypeRepository.findAll());
        applicationExpensesVo.setNcccCostCenterList(ncccCostCenterRepository.findAll());
        applicationExpensesVo
                .setNcccExpenseCategoryNumberList(ncccExpenseCategoryNumberRepository.findAll());
        applicationExpensesVo.setZmmtSupplierList(zmmtSupplierRepository.findAll());
        applicationExpensesVo.setCostcenterOrgMappingList(ncccCostcenterOrgMappingRepository.findAll());
        applicationExpensesVo.setCertificateCategoryList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("8.證號別", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        applicationExpensesVo.setIncomeCategoryList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("2.所得格式", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        applicationExpensesVo.setIncomeNoteList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("1.所得註記", Sort.by(Sort.Direction.ASC, "id.taxCode")));

        applicationExpensesVo.setSoftwareNoteList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("3.軟體註記", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        applicationExpensesVo.setChargeCodeList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("7.費用代號", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        applicationExpensesVo.setOtherIncomeList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("5.其他所得", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        applicationExpensesVo.setBusinessSectorList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("6.執行業務所得", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        applicationExpensesVo.setCountryCodeList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("9.國家代碼", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        applicationExpensesVo.setErrorNoteList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("4.錯誤註記", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        applicationExpensesVo.setRevenueCategoryList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("10.所得類別", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        applicationExpensesVo.setWithholdTypeList(ncccWithholdTypeRepository.findAll());
        NcccUserDto user = SecurityUtil.getCurrentUser();
        List<NcccCashController> ncccCashControllerList = ncccCashControllerRepository.findAll();
        applicationExpensesVo.setPettyCashAllowance(ncccCashControllerList.stream().anyMatch(p -> p.getCashControllerNo().equals(user.getHrid())));
        applicationExpensesVo.setMode(Mode.ADDMODE);
        return applicationExpensesVo;
    }

    // 董監事Init
    @Override
    public DSApplicationExpensesVo initDS() {
        DSApplicationExpensesVo dsApplicationExpensesVo = new DSApplicationExpensesVo();
        dsApplicationExpensesVo.setNcccCurrencyList(ncccCurrencyRepository.findAll());
        dsApplicationExpensesVo.setCostcenterOrgMappingList(ncccCostcenterOrgMappingRepository.findAll());
        dsApplicationExpensesVo.setCertificateCategoryList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("8.證號別", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        dsApplicationExpensesVo.setIncomeCategoryList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("2.所得格式", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        dsApplicationExpensesVo.setIncomeNoteList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("1.所得註記", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        dsApplicationExpensesVo.setSoftwareNoteList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("3.軟體註記", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        dsApplicationExpensesVo.setChargeCodeList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("7.費用代號", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        dsApplicationExpensesVo.setOtherIncomeList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("5.其他所得", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        dsApplicationExpensesVo.setBusinessSectorList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("6.執行業務所得", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        dsApplicationExpensesVo.setCountryCodeList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("9.國家代碼", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        dsApplicationExpensesVo.setErrorNoteList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("4.錯誤註記", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        dsApplicationExpensesVo.setRevenueCategoryList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("10.所得類別", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        dsApplicationExpensesVo.setNcccDirectorSupervisorListList(ncccDirectorSupervisorListRepository.findAll());
        dsApplicationExpensesVo.setWithholdTypeList(ncccWithholdTypeRepository.findAll());
        dsApplicationExpensesVo.setMode(Mode.ADDMODE);
        return dsApplicationExpensesVo;
    }

    // 研發委員Init
    @Override
    public CApplicationExpensesVo initC() {
        CApplicationExpensesVo cApplicationExpensesVo = new CApplicationExpensesVo();
        cApplicationExpensesVo.setNcccCurrencyList(ncccCurrencyRepository.findAll());
        cApplicationExpensesVo.setCostcenterOrgMappingList(ncccCostcenterOrgMappingRepository.findAll());
        cApplicationExpensesVo.setCertificateCategoryList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("8.證號別", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        cApplicationExpensesVo.setIncomeCategoryList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("2.所得格式", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        cApplicationExpensesVo.setIncomeNoteList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("1.所得註記", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        cApplicationExpensesVo.setSoftwareNoteList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("3.軟體註記", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        cApplicationExpensesVo.setChargeCodeList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("7.費用代號", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        cApplicationExpensesVo.setOtherIncomeList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("5.其他所得", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        cApplicationExpensesVo.setBusinessSectorList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("6.執行業務所得", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        cApplicationExpensesVo.setCountryCodeList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("9.國家代碼", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        cApplicationExpensesVo.setErrorNoteList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("4.錯誤註記", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        cApplicationExpensesVo.setRevenueCategoryList(ncccIncomeTaxCategoryRepository.findByIdTaxCategory("10.所得類別", Sort.by(Sort.Direction.ASC, "id.taxCode")));
        cApplicationExpensesVo.setNcccCommitteeListList(ncccCommitteeListRepository.findAll());
        cApplicationExpensesVo.setWithholdTypeList(ncccWithholdTypeRepository.findAll());
        cApplicationExpensesVo.setMode(Mode.ADDMODE);
        return cApplicationExpensesVo;
    }

    @Override
    public ApplicationExpensesVo query(String exNo) {
        ApplicationExpensesVo vo = new ApplicationExpensesVo();
        BpmExM bpmExM = bpmExMRepository.findById(exNo).orElseThrow(() -> new RuntimeException("找不到主檔: EX_NO=" + exNo));
        if (bpmExM.getApplyType().equals("7") || bpmExM.getApplyType().equals("8")) {
            throw new RuntimeException("非對應費用申請單: EX_NO=" + exNo);
        }
        List<BpmExMD1> bpmExMD1List = bpmExMD1Repository.findByExNo(exNo);

        // region 主檔
        BpmExMVo bpmExMVo = new BpmExMVo();
        bpmExMVo.setExNo(bpmExM.getExNo());
        bpmExMVo.setTaskId(bpmExM.getTaskId());
        bpmExMVo.setApplyDate(bpmExM.getApplyDate());
        bpmExMVo.setApplyType(bpmExM.getApplyType());
        bpmExMVo.setApplicant(bpmExM.getApplicant());
        bpmExMVo.setEmpNo(bpmExM.getEmpNo());
        bpmExMVo.setDepartment(bpmExM.getDepartment());
        bpmExMVo.setPayEmpNo(bpmExM.getPayEmpNo());
        bpmExMVo.setPayName(bpmExM.getPayName());
        bpmExMVo.setPayWay(bpmExM.getPayWay());
        bpmExMVo.setSpecialPayDate(bpmExM.getSpecialPayDate());
        bpmExMVo.setApplyReason(bpmExM.getApplyReason());
        bpmExMVo.setTradeCurrency(bpmExM.getTradeCurrency());
        bpmExMVo.setCurrencyRate(bpmExM.getCurrencyRate());
        bpmExMVo.setTotal(bpmExM.getTotal());
        bpmExMVo.setPredictTwd(bpmExM.getPredictTwd());
        if (bpmExM.getPrepayNo() != null) {
            bpmExMVo.setPrepayNo(Arrays.asList((bpmExM.getPrepayNo().split(","))));
        }
        bpmExMVo.setPayAmount(bpmExM.getPayAmount());
        bpmExMVo.setRefundAmount(bpmExM.getRefundAmount());
        bpmExMVo.setRefundDate(bpmExM.getRefundDate());
        bpmExMVo.setBankAccount(bpmExM.getBankAccount());
        bpmExMVo.setApprovalMethod(bpmExM.getApprovalMethod());
        bpmExMVo.setApprovalNo(bpmExM.getApprovalNo());
        bpmExMVo.setPayWayMethod(bpmExM.getPayWayMethod());
        bpmExMVo.setWithholdTotal(bpmExM.getWithholdTotal());
        bpmExMVo.setPostingDate(bpmExM.getPostingDate());

        // region 代扣繳項目

        List<BpmExMWHVo> bpmExMWHVoList = new ArrayList<>();

        List<BpmExMWH> bpmExMWHList = bpmExMWHRepository.findAllByExNo(exNo);

        for (BpmExMWH bpmExMWH : bpmExMWHList) {
            BpmExMWHVo bpmExMWHVo = new BpmExMWHVo();
            bpmExMWHVo.setName(bpmExMWH.getName());
            bpmExMWHVo.setAccounting(bpmExMWH.getAccounting());
            bpmExMWHVo.setAmount(bpmExMWH.getAmount());
            bpmExMWHVo.setAccountingName(bpmExMWH.getAccountingName());
            bpmExMWHVo.setPayDate(bpmExMWH.getPayDate());
            bpmExMWHVo.setItemWHText(bpmExMWH.getItemWHText());
            bpmExMWHVoList.add(bpmExMWHVo);
        }
        bpmExMVo.setBpmExMWHVoList(bpmExMWHVoList);
        // endregion

        // region 明細
        List<BpmExMDetailVo> bpmExMDetailVoList = new ArrayList<>();
        for (BpmExMD1 bpmExMD1 : bpmExMD1List) {
            BpmExMDetailVo bpmExMDetailVo = new BpmExMDetailVo();
            bpmExMDetailVo.setExItemNo(bpmExMD1.getExItemNo());
            bpmExMDetailVo.setCopyExItemNo(bpmExMD1.getCopyExItemNo());
            bpmExMDetailVo.setCopyExNo(bpmExMD1.getCopyExNo());
            bpmExMDetailVo.setCertificateDate(bpmExMD1.getCertificateDate());
            bpmExMDetailVo.setCertificateCode(bpmExMD1.getCertificateCode());
            bpmExMDetailVo.setCertificateType(bpmExMD1.getCertificateType());
            bpmExMDetailVo.setUniformNum(bpmExMD1.getUniformNum());
            bpmExMDetailVo.setApplyAmount(bpmExMD1.getApplyAmount());
            bpmExMDetailVo.setUntaxAmount(bpmExMD1.getUntaxAmount());
            bpmExMDetailVo.setTax(bpmExMD1.getTax());
            bpmExMDetailVo.setTaxCode(bpmExMD1.getTaxCode());
            bpmExMDetailVo.setTaxRate(bpmExMD1.getTaxRate());
            bpmExMDetailVo.setItemCode(bpmExMD1.getItemCode());
            bpmExMDetailVo.setItemName(bpmExMD1.getItemName());
            bpmExMDetailVo.setCostCenter(bpmExMD1.getCostCenter());
            bpmExMDetailVo.setDescription(bpmExMD1.getDescription());
            bpmExMDetailVo.setDeduction(bpmExMD1.getDeduction());
            bpmExMDetailVo.setHasIncomeTax(bpmExMD1.getHasIncomeTax());
            bpmExMDetailVo.setRemark(bpmExMD1.getRemark());
            bpmExMDetailVo.setItemText(bpmExMD1.getItemText());
            bpmExMDetailVo.setZeroTaxAmount(bpmExMD1.getZeroTaxAmount());
            bpmExMDetailVo.setDutyFreeAmount(bpmExMD1.getDutyFreeAmount());
            bpmExMDetailVo.setChtCode(bpmExMD1.getChtCode());
            bpmExMDetailVo.setChtCollectAmount(bpmExMD1.getChtCollectAmount());
            bpmExMDetailVo.setCarrierNumber(bpmExMD1.getCarrierNumber());
            bpmExMDetailVo.setNotificationNumber(bpmExMD1.getNotificationNumber());
            bpmExMDetailVo.setYear(bpmExMD1.getYear());
            bpmExMDetailVo.setAccounting(bpmExMD1.getAccounting());
            bpmExMDetailVo.setOuCode(bpmExMD1.getOuCode());

            // region 多重分攤
            List<BpmExMDetailSplitVo> bpmExMDetailSplitVoList = new ArrayList<>();
            List<BpmSplitM> bpmSplitMList = bpmSplitMRepository.findByExNoAndExItemNo(bpmExMD1.getExNo(), bpmExMD1.getExItemNo());
            String multiShare = "N";
            for (BpmSplitM bpmSplitM : bpmSplitMList) {
                multiShare = "Y";

                List<BpmExMDetailSplitTaskItemVo> taskItemVoList = new ArrayList<>();
                List<BpmTaskItemRepository.BpmExMDetailSplitTaskItem> taskItemList = bpmTaskItemRepository.findByExNoAndExItemNoAndExItemSplitNo(bpmSplitM.getExNo(),bpmSplitM.getExItemNo(), bpmSplitM.getExItemSplitNo());

                for(BpmTaskItemRepository.BpmExMDetailSplitTaskItem  taskItem : taskItemList)
                {
                    BpmExMDetailSplitTaskItemVo taskItemVo = new BpmExMDetailSplitTaskItemVo();
                    taskItemVo.setCode(taskItem.getCode());
                    taskItemVo.setName(taskItem.getName());
                    taskItemVo.setDescription(taskItem.getDescription());
                    taskItemVo.setItemText(taskItem.getItemText());
                    taskItemVo.setAmount(taskItem.getAmount());
                    taskItemVo.setRatio(taskItem.getRatio());
                    taskItemVo.setRemark(taskItem.getRemark());
                    taskItemVoList.add(taskItemVo);
                }

                BpmExMDetailSplitVo bpmExMDetailSplitVo = new BpmExMDetailSplitVo();
                bpmExMDetailSplitVo.setYear(bpmSplitM.getYear());
                bpmExMDetailSplitVo.setAccounting(bpmSplitM.getAccounting());
                bpmExMDetailSplitVo.setOuCode(bpmSplitM.getOuCode());
                bpmExMDetailSplitVo.setUntaxAmount(bpmSplitM.getUntaxAmount());
                bpmExMDetailSplitVo.setDescription(bpmSplitM.getDescription());
                bpmExMDetailSplitVo.setItemText(bpmSplitM.getItemText());
                bpmExMDetailSplitVo.setRemark(bpmSplitM.getRemark());
                bpmExMDetailSplitVo.setAllocationMethod(bpmSplitM.getAllocationMethod());
                bpmExMDetailSplitVo.setTaskItemVoList(taskItemVoList);
                bpmExMDetailSplitVoList.add(bpmExMDetailSplitVo);

            }
            bpmExMDetailVo.setMultiShare(multiShare);
            bpmExMDetailVo.setBpmExMDetailSplitVoList(bpmExMDetailSplitVoList);
            // endregion

            // region 扣繳清單

            List<BpmExDWH> bpmExDWHList = bpmExDWHRepository.findAllByExNoAndExItemNo(bpmExMD1.getExNo(),bpmExMD1.getExItemNo());

            List<BpmExMDetailWHVo> bpmExMDetailWHVoList = new ArrayList<>();

            for (BpmExDWH bpmExDWH : bpmExDWHList) {
                BpmExMDetailWHVo bpmExMDetailWHVo = new BpmExMDetailWHVo();
                bpmExMDetailWHVo.setBan(bpmExDWH.getBan());
                bpmExMDetailWHVo.setErrorNote(bpmExDWH.getErrorNote());
                bpmExMDetailWHVo.setCertificateCategory(bpmExDWH.getCertificateCategory());
                bpmExMDetailWHVo.setGainerName(bpmExDWH.getGainerName());
                bpmExMDetailWHVo.setGainerType(bpmExDWH.getGainerType());
                bpmExMDetailWHVo.setHas183Days(bpmExDWH.getHas183Days());
                bpmExMDetailWHVo.setCountryCode(bpmExDWH.getCountryCode());
                bpmExMDetailWHVo.setHasPassport(bpmExDWH.getHasPassport());
                bpmExMDetailWHVo.setPassportNo(bpmExDWH.getPassportNo());
                bpmExMDetailWHVo.setHasAddress(bpmExDWH.getHasAddress());
                bpmExMDetailWHVo.setAddress(bpmExDWH.getAddress());
                bpmExMDetailWHVo.setContactAddress(bpmExDWH.getContactAddress());
                bpmExMDetailWHVo.setContactPhone(bpmExDWH.getContactPhone());
                bpmExMDetailWHVo.setPaymentYear(bpmExDWH.getPaymentYear());
                bpmExMDetailWHVo.setPaymentMonthSt(bpmExDWH.getPaymentMonthSt());
                bpmExMDetailWHVo.setPaymentMonthEd(bpmExDWH.getPaymentMonthEd());
                bpmExMDetailWHVo.setIncomeNote(bpmExDWH.getIncomeNote());
                bpmExMDetailWHVo.setIncomeType(bpmExDWH.getIncomeType());
                bpmExMDetailWHVo.setGrossPayment(bpmExDWH.getGrossPayment());
                bpmExMDetailWHVo.setWithholdingTaxRate(bpmExDWH.getWithholdingTaxRate());
                bpmExMDetailWHVo.setWithholdingTax(bpmExDWH.getWithholdingTax());
                bpmExMDetailWHVo.setNhRate(bpmExDWH.getNhRate());
                bpmExMDetailWHVo.setRevenueCategory(bpmExDWH.getRevenueCategory());
                bpmExMDetailWHVo.setNhWithHolding(bpmExDWH.getNhWithHolding());
                bpmExMDetailWHVo.setNetPayment(bpmExDWH.getNetPayment());
                bpmExMDetailWHVo.setPaymentDate(bpmExDWH.getPaymentDate());
                bpmExMDetailWHVo.setSoftwareNote(bpmExDWH.getSoftwareNote());
                bpmExMDetailWHVo.setTaxTreatyCode(bpmExDWH.getTaxTreatyCode());
                bpmExMDetailWHVo.setTaxCreditFlag(bpmExDWH.getTaxCreditFlag());
                bpmExMDetailWHVo.setCertificateIssueMethod(bpmExDWH.getCertificateIssueMethod());
                bpmExMDetailWHVo.setTaxIdentificationNo(bpmExDWH.getTaxIdentificationNo());
                bpmExMDetailWHVo.setShareColumn1(bpmExDWH.getShareColumn1());
                bpmExMDetailWHVo.setShareColumn2(bpmExDWH.getShareColumn2());
                bpmExMDetailWHVo.setShareColumn3(bpmExDWH.getShareColumn3());
                bpmExMDetailWHVo.setShareColumn4(bpmExDWH.getShareColumn4());
                bpmExMDetailWHVo.setShareColumn5(bpmExDWH.getShareColumn5());
                bpmExMDetailWHVo.setRemark(bpmExDWH.getRemark());
                bpmExMDetailWHVoList.add(bpmExMDetailWHVo);
            }

            bpmExMDetailVo.setBpmExMDetailWHVoList(bpmExMDetailWHVoList);

            // endregion

            bpmExMDetailVoList.add(bpmExMDetailVo);
        }
        bpmExMVo.setBpmExMDetailVoList(bpmExMDetailVoList);
        // endregion

        // endregion

        if (bpmExM.getAttachment() != null) {
            try {
                vo.setFileList(FileUtil.getAllFiles(bpmExM.getAttachment()));
            } catch (IOException e) {
                LOG.error(e.getMessage());
                e.printStackTrace();
            }
        }
        vo.setTaskHistoryList(flowableService.getTaskHistory(bpmExM.getTaskId()));
        if (flowableService.checkAtInitiatorTask(bpmExM.getTaskId())) {
            vo.setMode(Mode.EDITMODE);
        } else {
            vo.setMode(Mode.VIEWMODE);
        }
        boolean isSetNextAssignee = flowableService.isSetNextAssignee(bpmExM.getTaskId());
        vo.setSetNextAssignee(flowableService.isSetNextAssignee(bpmExM.getTaskId()));
        if (isSetNextAssignee) {
            String nextDepId = flowableService.getNextDepId(bpmExM.getTaskId());
            SyncOU syncOU = syncOURepository.findByOuCode(nextDepId);
            List<SyncUser> syncUserList = syncUserRepository
                    .findByOuCodeAndAccountNotAndDisabled(syncOU.getOuCode(), syncOU.getMgrAccount(), "0");
            vo.setNextAssigneeList(syncUserList);
        }
        vo.setSapUser(flowableService.checkAtSapUserTask(bpmExM.getTaskId()));
        // 如果是經辦 可以退回申請人 但不能退回上一層
        // 如果不是 可以退回上一層 不能退回申請人
        if (flowableService.canReturnToInitiator(bpmExM.getTaskId())) {
            vo.setCanReturnToInitiator(true);
        } else {
            vo.setCanBackToPrevious(true);
        }
        vo.setBpmExMVo(bpmExMVo);
        return vo;
    }

    @Override
    public DSApplicationExpensesVo queryDS(String exNo) {
        DSApplicationExpensesVo vo = new DSApplicationExpensesVo();
        BpmExM bpmExM = bpmExMRepository.findById(exNo).orElseThrow(() -> new RuntimeException("找不到主檔: EX_NO=" + exNo));
        if (!bpmExM.getApplyType().equals("7")) {
            throw new RuntimeException("非董監事費用申請單: EX_NO=" + exNo);
        }

        List<BpmExPsD> bpmExPsDList = bpmExPsDRepository.findByExNo(exNo);

        // region 主檔
        BpmExMVo bpmExMVo = new BpmExMVo();
        bpmExMVo.setExNo(bpmExM.getExNo());
        bpmExMVo.setTaskId(bpmExM.getTaskId());
        bpmExMVo.setApplyDate(bpmExM.getApplyDate());
        bpmExMVo.setApplyType(bpmExM.getApplyType());
        bpmExMVo.setApplicant(bpmExM.getApplicant());
        bpmExMVo.setEmpNo(bpmExM.getEmpNo());
        bpmExMVo.setDepartment(bpmExM.getDepartment());
        bpmExMVo.setPayEmpNo(bpmExM.getPayEmpNo());
        bpmExMVo.setPayName(bpmExM.getPayName());
        bpmExMVo.setPayWay(bpmExM.getPayWay());
        bpmExMVo.setSpecialPayDate(bpmExM.getSpecialPayDate());
        bpmExMVo.setApplyReason(bpmExM.getApplyReason());
        bpmExMVo.setTradeCurrency(bpmExM.getTradeCurrency());
        bpmExMVo.setCurrencyRate(bpmExM.getCurrencyRate());
        bpmExMVo.setTotal(bpmExM.getTotal());
        bpmExMVo.setPredictTwd(bpmExM.getPredictTwd());
        if (bpmExM.getPrepayNo() != null) {
            bpmExMVo.setPrepayNo(Arrays.asList((bpmExM.getPrepayNo().split(","))));
        }
        bpmExMVo.setPayAmount(bpmExM.getPayAmount());
        bpmExMVo.setRefundAmount(bpmExM.getRefundAmount());
        bpmExMVo.setRefundDate(bpmExM.getRefundDate());
        bpmExMVo.setBankAccount(bpmExM.getBankAccount());
        bpmExMVo.setApprovalMethod(bpmExM.getApprovalMethod());
        bpmExMVo.setApprovalNo(bpmExM.getApprovalNo());
        bpmExMVo.setPayWayMethod(bpmExM.getPayWayMethod());
        bpmExMVo.setWithholdTotal(bpmExM.getWithholdTotal());
        bpmExMVo.setPostingDate(bpmExM.getPostingDate());

        // region 代扣繳項目

        List<BpmExMWHVo> bpmExMWHVoList = new ArrayList<>();

        List<BpmExMWH> bpmExMWHList = bpmExMWHRepository.findAllByExNo(exNo);

        for (BpmExMWH bpmExMWH : bpmExMWHList) {
            BpmExMWHVo bpmExMWHVo = new BpmExMWHVo();
            bpmExMWHVo.setName(bpmExMWH.getName());
            bpmExMWHVo.setAccounting(bpmExMWH.getAccounting());
            bpmExMWHVo.setAmount(bpmExMWH.getAmount());
            bpmExMWHVo.setAccountingName(bpmExMWH.getAccountingName());
            bpmExMWHVo.setPayDate(bpmExMWH.getPayDate());
            bpmExMWHVo.setItemWHText(bpmExMWH.getItemWHText());
            bpmExMWHVoList.add(bpmExMWHVo);
        }
        bpmExMVo.setBpmExMWHVoList(bpmExMWHVoList);
        // endregion

        // region 明細
        List<BpmExMPplVo> bpmExMPplVoList = new ArrayList<>();
        for (BpmExPsD bpmExPsD : bpmExPsDList) {
            BpmExMPplVo bpmExMPplVo = new BpmExMPplVo();
            bpmExMPplVo.setExItemNo(bpmExPsD.getExItemNo());
            bpmExMPplVo.setPplId(bpmExPsD.getPplId());
            bpmExMPplVo.setIsCivil(bpmExPsD.getIsCivil());
            bpmExMPplVo.setName(bpmExPsD.getName());
            bpmExMPplVo.setUnit(bpmExPsD.getUnit());
            bpmExMPplVo.setJobTitle(bpmExPsD.getJobTitle());
            bpmExMPplVo.setAmount1(bpmExPsD.getAmount1());
            bpmExMPplVo.setAmount2(bpmExPsD.getAmount2());
            bpmExMPplVo.setAmount3(bpmExPsD.getAmount3());
            bpmExMPplVo.setAmount4(bpmExPsD.getAmount4());
            bpmExMPplVo.setTotalAmount(bpmExPsD.getTotalAmount());
            bpmExMPplVo.setYear(bpmExPsD.getYear());
            bpmExMPplVo.setOuCode(bpmExPsD.getOuCode());
            bpmExMPplVo.setAccounting1(bpmExPsD.getAccounting1());
            bpmExMPplVo.setAccounting2(bpmExPsD.getAccounting2());
            bpmExMPplVo.setAccounting3(bpmExPsD.getAccounting3());
            bpmExMPplVo.setAccounting4(bpmExPsD.getAccounting4());

            // region 扣繳清單

            List<BpmExDWH> bpmExDWHList = bpmExDWHRepository.findAllByExNoAndExItemNo(bpmExPsD.getExNo(), bpmExPsD.getExItemNo());

            List<BpmExMDetailWHVo> bpmExMDetailWHVoList = new ArrayList<>();

            for (BpmExDWH bpmExDWH : bpmExDWHList) {
                BpmExMDetailWHVo bpmExMDetailWHVo = new BpmExMDetailWHVo();
                bpmExMDetailWHVo.setBan(bpmExDWH.getBan());
                bpmExMDetailWHVo.setErrorNote(bpmExDWH.getErrorNote());
                bpmExMDetailWHVo.setCertificateCategory(bpmExDWH.getCertificateCategory());
                bpmExMDetailWHVo.setGainerName(bpmExDWH.getGainerName());
                bpmExMDetailWHVo.setGainerType(bpmExDWH.getGainerType());
                bpmExMDetailWHVo.setHas183Days(bpmExDWH.getHas183Days());
                bpmExMDetailWHVo.setCountryCode(bpmExDWH.getCountryCode());
                bpmExMDetailWHVo.setHasPassport(bpmExDWH.getHasPassport());
                bpmExMDetailWHVo.setPassportNo(bpmExDWH.getPassportNo());
                bpmExMDetailWHVo.setHasAddress(bpmExDWH.getHasAddress());
                bpmExMDetailWHVo.setAddress(bpmExDWH.getAddress());
                bpmExMDetailWHVo.setContactAddress(bpmExDWH.getContactAddress());
                bpmExMDetailWHVo.setContactPhone(bpmExDWH.getContactPhone());
                bpmExMDetailWHVo.setPaymentYear(bpmExDWH.getPaymentYear());
                bpmExMDetailWHVo.setPaymentMonthSt(bpmExDWH.getPaymentMonthSt());
                bpmExMDetailWHVo.setPaymentMonthEd(bpmExDWH.getPaymentMonthEd());
                bpmExMDetailWHVo.setIncomeNote(bpmExDWH.getIncomeNote());
                bpmExMDetailWHVo.setIncomeType(bpmExDWH.getIncomeType());
                bpmExMDetailWHVo.setGrossPayment(bpmExDWH.getGrossPayment());
                bpmExMDetailWHVo.setWithholdingTaxRate(bpmExDWH.getWithholdingTaxRate());
                bpmExMDetailWHVo.setWithholdingTax(bpmExDWH.getWithholdingTax());
                bpmExMDetailWHVo.setNhRate(bpmExDWH.getNhRate());
                bpmExMDetailWHVo.setRevenueCategory(bpmExDWH.getRevenueCategory());
                bpmExMDetailWHVo.setNhWithHolding(bpmExDWH.getNhWithHolding());
                bpmExMDetailWHVo.setNetPayment(bpmExDWH.getNetPayment());
                bpmExMDetailWHVo.setPaymentDate(bpmExDWH.getPaymentDate());
                bpmExMDetailWHVo.setSoftwareNote(bpmExDWH.getSoftwareNote());
                bpmExMDetailWHVo.setTaxTreatyCode(bpmExDWH.getTaxTreatyCode());
                bpmExMDetailWHVo.setTaxCreditFlag(bpmExDWH.getTaxCreditFlag());
                bpmExMDetailWHVo.setCertificateIssueMethod(bpmExDWH.getCertificateIssueMethod());
                bpmExMDetailWHVo.setTaxIdentificationNo(bpmExDWH.getTaxIdentificationNo());
                bpmExMDetailWHVo.setShareColumn1(bpmExDWH.getShareColumn1());
                bpmExMDetailWHVo.setShareColumn2(bpmExDWH.getShareColumn2());
                bpmExMDetailWHVo.setShareColumn3(bpmExDWH.getShareColumn3());
                bpmExMDetailWHVo.setShareColumn4(bpmExDWH.getShareColumn4());
                bpmExMDetailWHVo.setShareColumn5(bpmExDWH.getShareColumn5());
                bpmExMDetailWHVo.setRemark(bpmExDWH.getRemark());
                bpmExMDetailWHVoList.add(bpmExMDetailWHVo);
            }

            bpmExMPplVo.setBpmExMDetailWHVoList(bpmExMDetailWHVoList);

            // endregion

            bpmExMPplVoList.add(bpmExMPplVo);
        }
        bpmExMVo.setBpmExMPplVoList(bpmExMPplVoList);
        // endregion

        // endregion

        if (bpmExM.getAttachment() != null) {
            try {
                vo.setFileList(FileUtil.getAllFiles(bpmExM.getAttachment()));
            } catch (IOException e) {
                LOG.error(e.getMessage());
                e.printStackTrace();
            }
        }
        vo.setTaskHistoryList(flowableService.getTaskHistory(bpmExM.getTaskId()));
        if (flowableService.checkAtInitiatorTask(bpmExM.getTaskId())) {
            vo.setMode(Mode.EDITMODE);
        } else {
            vo.setMode(Mode.VIEWMODE);
        }
        boolean isSetNextAssignee = flowableService.isSetNextAssignee(bpmExM.getTaskId());
        vo.setSetNextAssignee(isSetNextAssignee);
        if (isSetNextAssignee) {
            String nextDepId = flowableService.getNextDepId(bpmExM.getTaskId());
            SyncOU syncOU = syncOURepository.findByOuCode(nextDepId);
            List<SyncUser> syncUserList = syncUserRepository
                    .findByOuCodeAndAccountNotAndDisabled(syncOU.getOuCode(), syncOU.getMgrAccount(), "0");
            vo.setNextAssigneeList(syncUserList);
        }
        vo.setSapUser(flowableService.checkAtSapUserTask(bpmExM.getTaskId()));
        // 如果是經辦 可以退回申請人 但不能退回上一層
        // 如果不是 可以退回上一層 不能退回申請人
        if (flowableService.canReturnToInitiator(bpmExM.getTaskId())) {
            vo.setCanReturnToInitiator(true);
        } else {
            vo.setCanBackToPrevious(true);
        }
        vo.setBpmExMVo(bpmExMVo);
        return vo;
    }

    @Override
    public CApplicationExpensesVo queryC(String exNo) {
        CApplicationExpensesVo vo = new CApplicationExpensesVo();
        BpmExM bpmExM = bpmExMRepository.findById(exNo).orElseThrow(() -> new RuntimeException("找不到主檔: EX_NO=" + exNo));
        if (!bpmExM.getApplyType().equals("8")) {
            throw new RuntimeException("非研發委員費用申請單: EX_NO=" + exNo);
        }

        List<BpmExPsD> bpmExPsDList = bpmExPsDRepository.findByExNo(exNo);

        // region 主檔
        BpmExMVo bpmExMVo = new BpmExMVo();
        bpmExMVo.setExNo(bpmExM.getExNo());
        bpmExMVo.setTaskId(bpmExM.getTaskId());
        bpmExMVo.setApplyDate(bpmExM.getApplyDate());
        bpmExMVo.setApplyType(bpmExM.getApplyType());
        bpmExMVo.setApplicant(bpmExM.getApplicant());
        bpmExMVo.setEmpNo(bpmExM.getEmpNo());
        bpmExMVo.setDepartment(bpmExM.getDepartment());
        bpmExMVo.setPayEmpNo(bpmExM.getPayEmpNo());
        bpmExMVo.setPayName(bpmExM.getPayName());
        bpmExMVo.setPayWay(bpmExM.getPayWay());
        bpmExMVo.setSpecialPayDate(bpmExM.getSpecialPayDate());
        bpmExMVo.setApplyReason(bpmExM.getApplyReason());
        bpmExMVo.setTradeCurrency(bpmExM.getTradeCurrency());
        bpmExMVo.setCurrencyRate(bpmExM.getCurrencyRate());
        bpmExMVo.setTotal(bpmExM.getTotal());
        bpmExMVo.setPredictTwd(bpmExM.getPredictTwd());
        if (bpmExM.getPrepayNo() != null) {
            bpmExMVo.setPrepayNo(Arrays.asList((bpmExM.getPrepayNo().split(","))));
        }
        bpmExMVo.setPayAmount(bpmExM.getPayAmount());
        bpmExMVo.setRefundAmount(bpmExM.getRefundAmount());
        bpmExMVo.setRefundDate(bpmExM.getRefundDate());
        bpmExMVo.setBankAccount(bpmExM.getBankAccount());
        bpmExMVo.setApprovalMethod(bpmExM.getApprovalMethod());
        bpmExMVo.setApprovalNo(bpmExM.getApprovalNo());
        bpmExMVo.setPayWayMethod(bpmExM.getPayWayMethod());
        bpmExMVo.setWithholdTotal(bpmExM.getWithholdTotal());
        bpmExMVo.setPostingDate(bpmExM.getPostingDate());

        // region 代扣繳項目

        List<BpmExMWHVo> bpmExMWHVoList = new ArrayList<>();

        List<BpmExMWH> bpmExMWHList = bpmExMWHRepository.findAllByExNo(exNo);

        for (BpmExMWH bpmExMWH : bpmExMWHList) {
            BpmExMWHVo bpmExMWHVo = new BpmExMWHVo();
            bpmExMWHVo.setName(bpmExMWH.getName());
            bpmExMWHVo.setAccounting(bpmExMWH.getAccounting());
            bpmExMWHVo.setAmount(bpmExMWH.getAmount());
            bpmExMWHVo.setAccountingName(bpmExMWH.getAccountingName());
            bpmExMWHVo.setPayDate(bpmExMWH.getPayDate());
            bpmExMWHVo.setItemWHText(bpmExMWH.getItemWHText());
            bpmExMWHVoList.add(bpmExMWHVo);
        }
        bpmExMVo.setBpmExMWHVoList(bpmExMWHVoList);
        // endregion

        // region 明細
        List<BpmExMPplVo> bpmExMPplVoList = new ArrayList<>();
        for (BpmExPsD bpmExPsD : bpmExPsDList) {
            BpmExMPplVo bpmExMPplVo = new BpmExMPplVo();
            bpmExMPplVo.setExItemNo(bpmExPsD.getExItemNo());
            bpmExMPplVo.setPplId(bpmExPsD.getPplId());
            bpmExMPplVo.setIsCivil(bpmExPsD.getIsCivil());
            bpmExMPplVo.setName(bpmExPsD.getName());
            bpmExMPplVo.setUnit(bpmExPsD.getUnit());
            bpmExMPplVo.setJobTitle(bpmExPsD.getJobTitle());
            bpmExMPplVo.setAmount1(bpmExPsD.getAmount1());
            bpmExMPplVo.setAmount2(bpmExPsD.getAmount2());
            bpmExMPplVo.setAmount3(bpmExPsD.getAmount3());
            bpmExMPplVo.setAmount4(bpmExPsD.getAmount4());
            bpmExMPplVo.setTotalAmount(bpmExPsD.getTotalAmount());
            bpmExMPplVo.setYear(bpmExPsD.getYear());
            bpmExMPplVo.setOuCode(bpmExPsD.getOuCode());
            bpmExMPplVo.setAccounting1(bpmExPsD.getAccounting1());
            bpmExMPplVo.setAccounting2(bpmExPsD.getAccounting2());
            bpmExMPplVo.setAccounting3(bpmExPsD.getAccounting3());
            bpmExMPplVo.setAccounting4(bpmExPsD.getAccounting4());

            // region 扣繳清單

            List<BpmExDWH> bpmExDWHList = bpmExDWHRepository.findAllByExNoAndExItemNo(bpmExPsD.getExNo(), bpmExPsD.getExItemNo());

            List<BpmExMDetailWHVo> bpmExMDetailWHVoList = new ArrayList<>();

            for (BpmExDWH bpmExDWH : bpmExDWHList) {
                BpmExMDetailWHVo bpmExMDetailWHVo = new BpmExMDetailWHVo();
                bpmExMDetailWHVo.setBan(bpmExDWH.getBan());
                bpmExMDetailWHVo.setErrorNote(bpmExDWH.getErrorNote());
                bpmExMDetailWHVo.setCertificateCategory(bpmExDWH.getCertificateCategory());
                bpmExMDetailWHVo.setGainerName(bpmExDWH.getGainerName());
                bpmExMDetailWHVo.setGainerType(bpmExDWH.getGainerType());
                bpmExMDetailWHVo.setHas183Days(bpmExDWH.getHas183Days());
                bpmExMDetailWHVo.setCountryCode(bpmExDWH.getCountryCode());
                bpmExMDetailWHVo.setHasPassport(bpmExDWH.getHasPassport());
                bpmExMDetailWHVo.setPassportNo(bpmExDWH.getPassportNo());
                bpmExMDetailWHVo.setHasAddress(bpmExDWH.getHasAddress());
                bpmExMDetailWHVo.setAddress(bpmExDWH.getAddress());
                bpmExMDetailWHVo.setContactAddress(bpmExDWH.getContactAddress());
                bpmExMDetailWHVo.setContactPhone(bpmExDWH.getContactPhone());
                bpmExMDetailWHVo.setPaymentYear(bpmExDWH.getPaymentYear());
                bpmExMDetailWHVo.setPaymentMonthSt(bpmExDWH.getPaymentMonthSt());
                bpmExMDetailWHVo.setPaymentMonthEd(bpmExDWH.getPaymentMonthEd());
                bpmExMDetailWHVo.setIncomeNote(bpmExDWH.getIncomeNote());
                bpmExMDetailWHVo.setIncomeType(bpmExDWH.getIncomeType());
                bpmExMDetailWHVo.setGrossPayment(bpmExDWH.getGrossPayment());
                bpmExMDetailWHVo.setWithholdingTaxRate(bpmExDWH.getWithholdingTaxRate());
                bpmExMDetailWHVo.setWithholdingTax(bpmExDWH.getWithholdingTax());
                bpmExMDetailWHVo.setNhRate(bpmExDWH.getNhRate());
                bpmExMDetailWHVo.setRevenueCategory(bpmExDWH.getRevenueCategory());
                bpmExMDetailWHVo.setNhWithHolding(bpmExDWH.getNhWithHolding());
                bpmExMDetailWHVo.setNetPayment(bpmExDWH.getNetPayment());
                bpmExMDetailWHVo.setPaymentDate(bpmExDWH.getPaymentDate());
                bpmExMDetailWHVo.setSoftwareNote(bpmExDWH.getSoftwareNote());
                bpmExMDetailWHVo.setTaxTreatyCode(bpmExDWH.getTaxTreatyCode());
                bpmExMDetailWHVo.setTaxCreditFlag(bpmExDWH.getTaxCreditFlag());
                bpmExMDetailWHVo.setCertificateIssueMethod(bpmExDWH.getCertificateIssueMethod());
                bpmExMDetailWHVo.setTaxIdentificationNo(bpmExDWH.getTaxIdentificationNo());
                bpmExMDetailWHVo.setShareColumn1(bpmExDWH.getShareColumn1());
                bpmExMDetailWHVo.setShareColumn2(bpmExDWH.getShareColumn2());
                bpmExMDetailWHVo.setShareColumn3(bpmExDWH.getShareColumn3());
                bpmExMDetailWHVo.setShareColumn4(bpmExDWH.getShareColumn4());
                bpmExMDetailWHVo.setShareColumn5(bpmExDWH.getShareColumn5());
                bpmExMDetailWHVo.setRemark(bpmExDWH.getRemark());
                bpmExMDetailWHVoList.add(bpmExMDetailWHVo);
            }

            bpmExMPplVo.setBpmExMDetailWHVoList(bpmExMDetailWHVoList);

            // endregion

            bpmExMPplVoList.add(bpmExMPplVo);
        }
        bpmExMVo.setBpmExMPplVoList(bpmExMPplVoList);
        // endregion

        // endregion

        if (bpmExM.getAttachment() != null) {
            try {
                vo.setFileList(FileUtil.getAllFiles(bpmExM.getAttachment()));
            } catch (IOException e) {
                LOG.error(e.getMessage());
                e.printStackTrace();
            }
        }
        vo.setTaskHistoryList(flowableService.getTaskHistory(bpmExM.getTaskId()));
        if (flowableService.checkAtInitiatorTask(bpmExM.getTaskId())) {
            vo.setMode(Mode.EDITMODE);
        } else {
            vo.setMode(Mode.VIEWMODE);
        }
        boolean isSetNextAssignee = flowableService.isSetNextAssignee(bpmExM.getTaskId());
        vo.setSetNextAssignee(isSetNextAssignee);
        if (isSetNextAssignee) {
            String nextDepId = flowableService.getNextDepId(bpmExM.getTaskId());
            SyncOU syncOU = syncOURepository.findByOuCode(nextDepId);
            List<SyncUser> syncUserList = syncUserRepository
                    .findByOuCodeAndAccountNotAndDisabled(syncOU.getOuCode(), syncOU.getMgrAccount(), "0");
            vo.setNextAssigneeList(syncUserList);
        }

        vo.setSapUser(flowableService.checkAtSapUserTask(bpmExM.getTaskId()));
        // 如果是經辦 可以退回申請人 但不能退回上一層
        // 如果不是 可以退回上一層 不能退回申請人
        if (flowableService.canReturnToInitiator(bpmExM.getTaskId())) {
            vo.setCanReturnToInitiator(true);
        } else {
            vo.setCanBackToPrevious(true);
        }
        vo.setBpmExMVo(bpmExMVo);
        return vo;
    }

    // endregion

    // region 建立編輯

    @Override
    public String startProcess(BpmExMVo vo, List<MultipartFile> files) {
        NcccUserDto user = SecurityUtil.getCurrentUser();
        String prefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));
        Integer nextSerial = bpmExMRepository.findMaxSerialByPrefix(prefix) + 1;
        String serialStr = String.format("%04d", nextSerial);
        String exNo = "EX" + prefix + serialStr;

        if (vo.getApplyType().equals("7") || vo.getApplyType().equals("8")) {
            throw new RuntimeException("非對應費用申請單: EX_NO=" + exNo);
        }

        // 檢查憑證是否有存在DB 有代表被使用過(不包含作廢的)
        List<String> codes =
                vo.getBpmExMDetailVoList().stream().map(BpmExMDetailVo::getCertificateCode).filter(Objects::nonNull)
                        .map(String::trim).filter(s -> !s.isEmpty()).distinct().collect(Collectors.toList());
        if (!codes.isEmpty()) {
            List<String> conflicts = bpmExMD1Repository.findConflictedCertificateCodes(codes);
            if (!conflicts.isEmpty()) {
                throw new IllegalStateException("以下憑證號已使用，請檢查：" + String.join(", ", conflicts));
            }
            // 呼叫SAP GUI檢核
            List<TInput> tInputList = buildTInputList(vo.getBpmExMDetailVoList());
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

        // region 主表處理
        BpmExM bpmExM = new BpmExM();
        bpmExM.setApplyDate(vo.getApplyDate());
        bpmExM.setApplyType(vo.getApplyType());
        bpmExM.setApplicant(vo.getApplicant());
        bpmExM.setEmpNo(vo.getEmpNo());
        bpmExM.setDepartment(vo.getDepartment());
        bpmExM.setPayEmpNo(vo.getPayEmpNo());
        bpmExM.setPayName(vo.getPayName());
        bpmExM.setPayWay(vo.getPayWay());
        bpmExM.setSpecialPayDate(vo.getSpecialPayDate());
        bpmExM.setApplyReason(vo.getApplyReason());
        bpmExM.setTradeCurrency(vo.getTradeCurrency());
        bpmExM.setCurrencyRate(vo.getCurrencyRate());
        bpmExM.setTotal(vo.getTotal());
        bpmExM.setPredictTwd(vo.getPredictTwd());
        bpmExM.setPrepayNo(String.join(",", vo.getPrepayNo()));
        bpmExM.setPayAmount(vo.getPayAmount());
        bpmExM.setRefundAmount(vo.getRefundAmount());
        bpmExM.setRefundDate(vo.getRefundDate());
        bpmExM.setBankAccount(vo.getBankAccount());
        bpmExM.setPayWayMethod(vo.getPayWayMethod());
        bpmExM.setApprovalMethod(vo.getApprovalMethod());
        bpmExM.setApprovalNo(vo.getApprovalNo());
        bpmExM.setWithholdTotal(vo.getWithholdTotal());
        bpmExM.setPostingDate(vo.getPostingDate());
        bpmExM.setCreatedUser(user.getUserId());
        bpmExM.setCreatedDate(LocalDate.now());
        bpmExM.setFlowStatus("1");

        // 把多個檔壓成一個 ZIP
        if (files != null && !files.isEmpty()) {
            try {
                byte[] Bytes = FileUtil.compressToZip(files);
                bpmExM.setAttachment(Bytes);
            } catch (IOException e) {
                LOG.error(e.toString());
                e.printStackTrace();
            }
        }
        bpmExM.setExNo(exNo);

        // endregion

        List<BudgetVo.BudgetTranscation> budgetTranscationList =  new ArrayList<>();

        int index = 1;

        for (BpmExMDetailVo bpmExMDetailVo : vo.getBpmExMDetailVoList()) {
            // region 明細

            //自動產號
            if(bpmExMDetailVo.getCertificateType() != null && AUTO_GENERATE_CERTIFICATE_CODE.contains(bpmExMDetailVo.getCertificateType()))
            {
                bpmExMDetailVo.setCertificateCode(prefix + serialStr + String.format("%03d", index));
            }

            BpmExMD1 bpmExMD1 = new BpmExMD1();
            bpmExMD1.setExNo(exNo);
            bpmExMD1.setExItemNo(String.format("%03d", index));
            bpmExMD1.setCopyExNo(bpmExMDetailVo.getCopyExNo());
            bpmExMD1.setCertificateDate(bpmExMDetailVo.getCertificateDate());
            bpmExMD1.setCertificateCode(bpmExMDetailVo.getCertificateCode());
            bpmExMD1.setCertificateType(bpmExMDetailVo.getCertificateType());
            bpmExMD1.setUniformNum(bpmExMDetailVo.getUniformNum());
            bpmExMD1.setApplyAmount(bpmExMDetailVo.getApplyAmount());
            bpmExMD1.setUntaxAmount(bpmExMDetailVo.getUntaxAmount());
            bpmExMD1.setTax(bpmExMDetailVo.getTax());
            bpmExMD1.setTaxCode(bpmExMDetailVo.getTaxCode());
            bpmExMD1.setTaxRate(bpmExMDetailVo.getTaxRate());
            bpmExMD1.setItemCode(bpmExMDetailVo.getItemCode());
            bpmExMD1.setItemName(bpmExMDetailVo.getItemName());
            bpmExMD1.setCostCenter(bpmExMDetailVo.getCostCenter());
            bpmExMD1.setDescription(bpmExMDetailVo.getDescription());
            bpmExMD1.setDeduction(bpmExMDetailVo.getDeduction());
            bpmExMD1.setHasIncomeTax(bpmExMDetailVo.getHasIncomeTax());
            bpmExMD1.setRemark(bpmExMDetailVo.getRemark());
            bpmExMD1.setItemText(bpmExMDetailVo.getItemText());
            bpmExMD1.setZeroTaxAmount(bpmExMDetailVo.getZeroTaxAmount());
            bpmExMD1.setDutyFreeAmount(bpmExMDetailVo.getDutyFreeAmount());
            bpmExMD1.setChtCode(bpmExMDetailVo.getChtCode());
            bpmExMD1.setChtCollectAmount(bpmExMDetailVo.getChtCollectAmount());
            bpmExMD1.setCarrierNumber(bpmExMDetailVo.getCarrierNumber());
            bpmExMD1.setNotificationNumber(bpmExMDetailVo.getNotificationNumber());
            bpmExMD1.setYear(bpmExMDetailVo.getYear());
            bpmExMD1.setOuCode(bpmExMDetailVo.getOuCode());
            bpmExMD1.setAccounting(bpmExMDetailVo.getAccounting());
            bpmExMD1.setMultiShare(!bpmExMDetailVo.getBpmExMDetailSplitVoList().isEmpty() ?"Y":"N");
            bpmExMD1.setCreatedUser(user.getUserId());
            bpmExMD1.setCreatedDate(LocalDate.now());
            BpmExMD1 newBpmExMD1 = bpmExMD1Repository.save(bpmExMD1);

            // region 預算動用
            // 若沒有多重分攤預算動用 (零用金與預付借支不會動到預算)
            if(bpmExMD1.getMultiShare().equals("N") && (!bpmExM.getApplyType().equals("2") && !bpmExM.getApplyType().equals("5")))
            {
                BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                budgetTranscation.setYear(bpmExMD1.getYear());
                budgetTranscation.setVersion("2");
                budgetTranscation.setOuCode(bpmExMD1.getOuCode());
                budgetTranscation.setAccounting(bpmExMD1.getAccounting());
                budgetTranscation.setAmount(bpmExMD1.getUntaxAmount());
                budgetTranscation.setTranscationSource("費用申請單");
                budgetTranscation.setTranscationNo(exNo);
                budgetTranscation.setTranscationDate(LocalDate.now());
                budgetTranscation.setTranscationType("動用");
                budgetTranscation.setBpNo(bpmExM.getEmpNo());
                budgetTranscation.setBpName(bpmExM.getEmpNo());
                budgetTranscation.setDocNo(bpmExM.getApprovalNo());
                budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                budgetTranscation.setCreateDate(LocalDateTime.now());
                budgetTranscationList.add(budgetTranscation);
            }

            // endregion

            // region 多種分攤

            int splitIndex = 1;
            for (BpmExMDetailSplitVo bpmExMDetailSplitVo : bpmExMDetailVo.getBpmExMDetailSplitVoList()) {
                BpmSplitM bpmSplitM = new BpmSplitM();
                bpmSplitM.setExItemNo(newBpmExMD1.getExItemNo());
                bpmSplitM.setExNo(exNo);
                bpmSplitM.setExItemSplitNo(String.format("%03d", splitIndex));
                bpmSplitM.setAccounting(bpmExMDetailSplitVo.getAccounting());
                bpmSplitM.setYear(bpmExMDetailSplitVo.getYear());
                bpmSplitM.setOuCode(bpmExMDetailSplitVo.getOuCode());
                bpmSplitM.setUntaxAmount(bpmExMDetailSplitVo.getUntaxAmount());
                bpmSplitM.setRemark(bpmExMDetailSplitVo.getRemark());
                bpmSplitM.setItemText(bpmExMDetailSplitVo.getItemText());
                bpmSplitM.setDescription(bpmExMDetailSplitVo.getDescription());
                bpmSplitM.setAllocationMethod(bpmExMDetailSplitVo.getAllocationMethod());
                bpmSplitM.setCreatedUser(user.getUserId());
                bpmSplitM.setCreatedDate(LocalDate.now());
                BpmSplitM newBpmSplitM = bpmSplitMRepository.save(bpmSplitM);

                // region 預算動用 (零用金與預付借支不會動到預算)

                if((!bpmExM.getApplyType().equals("2") && !bpmExM.getApplyType().equals("5")))
                {
                    BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                    budgetTranscation.setYear(bpmSplitM.getYear());
                    budgetTranscation.setVersion("2");
                    budgetTranscation.setOuCode(bpmSplitM.getOuCode());
                    budgetTranscation.setAccounting(bpmSplitM.getAccounting());
                    budgetTranscation.setAmount(bpmSplitM.getUntaxAmount());
                    budgetTranscation.setTranscationSource("費用申請單");
                    budgetTranscation.setTranscationNo(exNo);
                    budgetTranscation.setTranscationDate(LocalDate.now());
                    budgetTranscation.setTranscationType("動用");
                    budgetTranscation.setBpNo(bpmExM.getEmpNo());
                    budgetTranscation.setBpName(bpmExM.getEmpNo());
                    budgetTranscation.setDocNo(bpmExM.getApprovalNo());
                    budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                    budgetTranscation.setCreateDate(LocalDateTime.now());
                    budgetTranscationList.add(budgetTranscation);
                }
                
                // endregion 預算動用

                //多重分攤
                List<BpmTaskItem> bpmTaskItemList = new ArrayList<>();

                int splitTaskIndex = 1;
                for(BpmExMDetailSplitTaskItemVo taskItemVo : bpmExMDetailSplitVo.getTaskItemVoList())
                {
                    BpmTaskItem bpmTaskItem = new BpmTaskItem();
                    bpmTaskItem.setExItemNo(newBpmSplitM.getExItemNo());
                    bpmTaskItem.setExItemSplitNo(newBpmSplitM.getExItemSplitNo());
                    bpmTaskItem.setExItemSplitTaskNo(String.format("%03d", splitTaskIndex));
                    bpmTaskItem.setExNo(exNo);
                    bpmTaskItem.setOperateItemCode(taskItemVo.getCode());
                    bpmTaskItem.setUntaxAmount(taskItemVo.getAmount());
                    bpmTaskItem.setOperateRatio(taskItemVo.getRatio());
                    bpmTaskItem.setItemText(taskItemVo.getItemText());
                    bpmTaskItem.setDescription(taskItemVo.getDescription());
                    bpmTaskItem.setRemark(taskItemVo.getRemark());

                    bpmTaskItemList.add(bpmTaskItem);

                    splitTaskIndex++;
                }

                bpmTaskItemRepository.saveAll(bpmTaskItemList);

                splitIndex++;
            }
            // endregion

            // region 扣繳清單

            //若有所得稅
            if (!bpmExMDetailVo.getBpmExMDetailWHVoList().isEmpty()) {
                List<BpmExDWH> bpmExDWHList = new ArrayList();

                int itemWhIndex = 1;

                for (BpmExMDetailWHVo bpmExMDetailWHVo : bpmExMDetailVo.getBpmExMDetailWHVoList()) {
                    BpmExDWH bpmExDWH = new BpmExDWH();
                    bpmExDWH.setExItemNo(newBpmExMD1.getExItemNo());
                    bpmExDWH.setExNo(exNo);
                    bpmExDWH.setExItemWhNo(String.format("%03d", itemWhIndex));
                    bpmExDWH.setBan(bpmExMDetailWHVo.getBan());
                    bpmExDWH.setErrorNote(bpmExMDetailWHVo.getErrorNote());
                    bpmExDWH.setCertificateCategory(bpmExMDetailWHVo.getCertificateCategory());
                    bpmExDWH.setGainerName(bpmExMDetailWHVo.getGainerName());
                    bpmExDWH.setGainerType(bpmExMDetailWHVo.getGainerType());
                    bpmExDWH.setHas183Days(bpmExMDetailWHVo.getHas183Days());
                    bpmExDWH.setCountryCode(bpmExMDetailWHVo.getCountryCode());
                    bpmExDWH.setHasPassport(bpmExMDetailWHVo.getHasPassport());
                    bpmExDWH.setPassportNo(bpmExMDetailWHVo.getPassportNo());
                    bpmExDWH.setHasAddress(bpmExMDetailWHVo.getHasAddress());
                    bpmExDWH.setAddress(bpmExMDetailWHVo.getAddress());
                    bpmExDWH.setContactAddress(bpmExMDetailWHVo.getContactAddress());
                    bpmExDWH.setContactPhone(bpmExMDetailWHVo.getContactPhone());
                    bpmExDWH.setPaymentYear(bpmExMDetailWHVo.getPaymentYear());
                    bpmExDWH.setPaymentMonthSt(bpmExMDetailWHVo.getPaymentMonthSt());
                    bpmExDWH.setPaymentMonthEd(bpmExMDetailWHVo.getPaymentMonthEd());
                    bpmExDWH.setIncomeNote(bpmExMDetailWHVo.getIncomeNote());
                    bpmExDWH.setIncomeType(bpmExMDetailWHVo.getIncomeType());
                    bpmExDWH.setGrossPayment(bpmExMDetailWHVo.getGrossPayment());
                    bpmExDWH.setWithholdingTaxRate(bpmExMDetailWHVo.getWithholdingTaxRate());
                    bpmExDWH.setWithholdingTax(bpmExMDetailWHVo.getWithholdingTax());
                    bpmExDWH.setNhRate(bpmExMDetailWHVo.getNhRate());
                    bpmExDWH.setRevenueCategory(bpmExMDetailWHVo.getRevenueCategory());
                    bpmExDWH.setNhWithHolding(bpmExMDetailWHVo.getNhWithHolding());
                    bpmExDWH.setNetPayment(bpmExMDetailWHVo.getNetPayment());
                    bpmExDWH.setPaymentDate(bpmExMDetailWHVo.getPaymentDate());
                    bpmExDWH.setSoftwareNote(bpmExMDetailWHVo.getSoftwareNote());
                    bpmExDWH.setTaxTreatyCode(bpmExMDetailWHVo.getTaxTreatyCode());
                    bpmExDWH.setTaxCreditFlag(bpmExMDetailWHVo.getTaxCreditFlag());
                    bpmExDWH.setCertificateIssueMethod(bpmExMDetailWHVo.getCertificateIssueMethod());
                    bpmExDWH.setTaxIdentificationNo(bpmExMDetailWHVo.getTaxIdentificationNo());
                    bpmExDWH.setShareColumn1(bpmExMDetailWHVo.getShareColumn1());
                    bpmExDWH.setShareColumn2(bpmExMDetailWHVo.getShareColumn2());
                    bpmExDWH.setShareColumn3(bpmExMDetailWHVo.getShareColumn3());
                    bpmExDWH.setShareColumn4(bpmExMDetailWHVo.getShareColumn4());
                    bpmExDWH.setShareColumn5(bpmExMDetailWHVo.getShareColumn5());
                    bpmExDWH.setRemark(bpmExMDetailWHVo.getRemark());

                    bpmExDWHList.add(bpmExDWH);

                    itemWhIndex++;
                }

                bpmExDWHRepository.saveAll(bpmExDWHList);

            }

            // endregion

            // endregion

            index++;
        }

        // region 代扣繳項目
        List<BpmExMWH> bpmExMWHList = new ArrayList<>();
        int wIndex = 1;

        for (BpmExMWHVo bpmExMWHVo : vo.getBpmExMWHVoList()) {
            BpmExMWH bpmExMWH = new BpmExMWH();
            bpmExMWH.setExNo(exNo);
            bpmExMWH.setExWhNo(String.format("%03d",wIndex));
            bpmExMWH.setName(bpmExMWHVo.getName());
            bpmExMWH.setAccounting(bpmExMWHVo.getAccounting());
            bpmExMWH.setAmount(bpmExMWHVo.getAmount());
            bpmExMWH.setAccountingName(bpmExMWHVo.getAccountingName());
            bpmExMWH.setItemWHText(bpmExMWHVo.getItemWHText());
            bpmExMWH.setPayDate(bpmExMWHVo.getPayDate());
            bpmExMWHList.add(bpmExMWH);

            wIndex++;
        }

        bpmExMWHRepository.saveAll(bpmExMWHList);
        // endregion

        // region 預算動用寫入

        for(BudgetVo.BudgetTranscation budgetTranscation : budgetTranscationList)
        {
            ncccBudgetService.WriteBudgetTranscation(budgetTranscation);
        }

        // endregion

        // region 簽核流程判斷

        // 用來判斷是否是單位主管以上
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
        BigDecimal amount = bpmExM.getTotal();
        vars.put("amount", amount);

        //region 判斷核決流程

        //國際卡組織計收之費用會科
        List<String> ExclusiveAccounting = new ArrayList<>();
        ExclusiveAccounting.add("5102250101");
        ExclusiveAccounting.add("5102310101");
        ExclusiveAccounting.add("5102310401");
        ExclusiveAccounting.add("5102319901");
        ExclusiveAccounting.add("5103160201");
        ExclusiveAccounting.add("5103160301");

        //交際費會科
        List<String> SocialAccounting = new ArrayList<>();
        SocialAccounting.add("5102150101");
        SocialAccounting.add("5102150201");
        SocialAccounting.add("5102150301");
        SocialAccounting.add("5102230201");

        //教育訓練費
        String TrainingFeeAccounting = "5102190101";

        //國際卡組織計收費用
        if (vo.getBpmExMDetailVoList().stream().anyMatch(o -> ExclusiveAccounting.contains(o.getItemCode()))) {
            processKey = ProcessDefinitionKey.APPLICATIONEXPENSES_GENERALEXPENSESREQUESTPAYMENT_EXCLUSIVE.getKey();

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
            vars.put(AssigneeRole.VICE_PRESIDENT.getKey(), vicePresident);
            // 總經理
            setAssignee(vars, AssigneeRole.PRESIDENT);
            // 董事長
            setAssignee(vars, AssigneeRole.CHAIRMAN);

            vars.put(AssigneeRole.SAP_USER.getKey(), null);

        }
        else if (vo.getBpmExMDetailVoList().stream().anyMatch(o -> SocialAccounting.contains(o.getItemCode())))//交際費
        {
            processKey = ProcessDefinitionKey.APPLICATIONEXPENSES_GENERALEXPENSESREQUESTPAYMENT_SOCIAL.getKey();

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
            // 副總經理
            vars.put(AssigneeRole.VICE_PRESIDENT.getKey(), vicePresident);
            // 總經理
            setAssignee(vars, AssigneeRole.PRESIDENT);
            // 董事長
            setAssignee(vars, AssigneeRole.CHAIRMAN);

            vars.put(AssigneeRole.SAP_USER.getKey(), null);
        }
        else if (bpmExM.getApplyType().equals("2")) // 零用金申請單
        {
            processKey = ProcessDefinitionKey.APPLICATIONEXPENSES_PETTYCASH.getKey();

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

            if (approvers.isEmpty()) {
                throw new RuntimeException("非單位主管以上，但找不到上層主管");
            }

            vars.put(AssigneeRole.APPROVERS.getKey(), approvers);
            vars.put(AssigneeRole.CURRENT_INDEX.getKey(), 0);
            vars.put(AssigneeRole.CURRENT_APPROVER.getKey(), approvers.get(0));

            // 零用金保管人
            setAssignee(vars, AssigneeRole.CASHIER);
            // 行管部主管
            setAssignee(vars, AssigneeRole.ADMINISTRATIVE_MANAGER);
        }
        else if (((bpmExM.getApprovalMethod() == null || bpmExM.getApprovalMethod().equals("SENDAPPROVAL")) && bpmExM.getApplyType().equals("5"))) // 預付單(無簽呈)
        {
            processKey = ProcessDefinitionKey.APPLICATIONEXPENSES_PREPAYMENTWITHOUTAPPROVAL.getKey();

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

            if (approvers.isEmpty()) {
                throw new RuntimeException("非單位主管以上，但找不到上層主管");
            }

            vars.put(AssigneeRole.APPROVERS.getKey(), approvers);
            vars.put(AssigneeRole.CURRENT_INDEX.getKey(), 0);
            vars.put(AssigneeRole.CURRENT_APPROVER.getKey(), approvers.get(0));

            // 分派任務(會計經辦)
            vars.put(AssigneeRole.ASSIGN_ACCOUNTING_CLERK.getKey(), bpmNcccMissionAssignerRepository
                    .findByOuCode(AssigneeRole.ACCOUNTING_CLERK.getCode()).getHrid());
            // 會計經辦
            vars.put(AssigneeRole.ACCOUNTING_CLERK.getKey(), null);
            // 會計科長
            setAssignee(vars, AssigneeRole.ACCOUNTING_SECTION_CHIEF);
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

        }
        else if (bpmExM.getApprovalMethod().equals("APPROVED") || bpmExM.getApprovalMethod().equals("COLLECTIONPAYMENT")) // 預付單(簽呈)及代收代付
        {
            processKey = ProcessDefinitionKey.APPLICATIONEXPENSES_PREPAYMENTWITHAPPROVAL.getKey();

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

            if (approvers.isEmpty()) {
                throw new RuntimeException("非單位主管以上，但找不到上層主管");
            }

            vars.put(AssigneeRole.APPROVERS.getKey(), approvers);
            vars.put(AssigneeRole.CURRENT_INDEX.getKey(), 0);
            vars.put(AssigneeRole.CURRENT_APPROVER.getKey(), approvers.get(0));

            // 分派任務(會計經辦)
            vars.put(AssigneeRole.ASSIGN_ACCOUNTING_CLERK.getKey(), bpmNcccMissionAssignerRepository
                    .findByOuCode(AssigneeRole.ACCOUNTING_CLERK.getCode()).getHrid());
            // 會計經辦
            vars.put(AssigneeRole.ACCOUNTING_CLERK.getKey(), null);
            // 會計科長
            setAssignee(vars, AssigneeRole.ACCOUNTING_SECTION_CHIEF);
            // 會計經理
            setAssignee(vars, AssigneeRole.ACCOUNTING_MANAGER);
            // 會計協理
            setAssignee(vars, AssigneeRole.ACCOUNTING_ASSOCIATE);
            // 會計資協
            setAssignee(vars, AssigneeRole.ACCOUNTING_SENIOR_ASSOCIATE);

            vars.put(AssigneeRole.SAP_USER.getKey(), null);
        }
        else if (bpmExM.getApplyType().equals("4") || vo.getBpmExMDetailVoList().stream().anyMatch(o -> o.getItemCode().equals(TrainingFeeAccounting))
                || bpmExM.getApplyType().equals("3") || !bpmExM.getPrepayNo().equals("")) // 公共事業費 || 教育訓練費 || 零用金撥補申請單 || 預付借支單金額>=申請金額(有預付單)
        {
            processKey = ProcessDefinitionKey.APPLICATIONEXPENSES_UTILITYEXPENSES.getKey();

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

            if (approvers.isEmpty()) {
                throw new RuntimeException("非單位主管以上，但找不到上層主管");
            }

            vars.put(AssigneeRole.APPROVERS.getKey(), approvers);
            vars.put(AssigneeRole.CURRENT_INDEX.getKey(), 0);
            vars.put(AssigneeRole.CURRENT_APPROVER.getKey(), approvers.get(0));

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

            vars.put(AssigneeRole.SAP_USER.getKey(), null);
        }
        else //其他類歸入一般
        {
            processKey = ProcessDefinitionKey.APPLICATIONEXPENSES_GENERALEXPENSESREQUESTPAYMENT_GENERAL.getKey();

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

            if (approvers.isEmpty()) {
                throw new RuntimeException("非單位主管以上，但找不到上層主管");
            }

            vars.put(AssigneeRole.APPROVERS.getKey(), approvers);
            vars.put(AssigneeRole.CURRENT_INDEX.getKey(), 0);
            vars.put(AssigneeRole.CURRENT_APPROVER.getKey(), approvers.get(0));

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
            vars.put(AssigneeRole.VICE_PRESIDENT.getKey(), vicePresident);
            // 總經理
            setAssignee(vars, AssigneeRole.PRESIDENT);
            // 董事長
            setAssignee(vars, AssigneeRole.CHAIRMAN);

            vars.put(AssigneeRole.SAP_USER.getKey(), null);
        }

        //endregion

        // endregion

        BpmExM saved = bpmExMRepository.save(bpmExM);

        String processId = flowableService.startProcess(processKey, exNo, vars);

        saved.setTaskId(processId);

        bpmExMRepository.save(saved);

        DecisionVo decisionVo = new DecisionVo();

        decisionVo.setProcessId(processId);

        decisionVo.setDecision(Decision.SUBMIT);

        flowableService.completeTask(decisionVo);

        return exNo;
    }

    @Override
    public String startDSCProcess(BpmExMVo vo, List<MultipartFile> files) {
        NcccUserDto user = SecurityUtil.getCurrentUser();
        String prefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));
        Integer nextSerial = bpmExMRepository.findMaxSerialByPrefix(prefix) + 1;
        String serialStr = String.format("%04d", nextSerial);
        String exNo = "EX" + prefix + serialStr;

        if (!vo.getApplyType().equals("7") && !vo.getApplyType().equals("8")) {
            throw new RuntimeException("非對應費用申請單: EX_NO=" + exNo);
        }

        List<BudgetVo.BudgetTranscation> budgetTranscationList =  new ArrayList<>();

        // region 主表處理
        BpmExM bpmExM = new BpmExM();
        bpmExM.setApplyDate(vo.getApplyDate());
        bpmExM.setApplyType(vo.getApplyType());
        bpmExM.setApplicant(vo.getApplicant());
        bpmExM.setEmpNo(vo.getEmpNo());
        bpmExM.setDepartment(vo.getDepartment());
        bpmExM.setPayEmpNo(vo.getPayEmpNo());
        bpmExM.setPayName(vo.getPayName());
        bpmExM.setPayWay(vo.getPayWay());
        bpmExM.setSpecialPayDate(vo.getSpecialPayDate());
        bpmExM.setApplyReason(vo.getApplyReason());
        bpmExM.setTradeCurrency(vo.getTradeCurrency());
        bpmExM.setCurrencyRate(vo.getCurrencyRate());
        bpmExM.setTotal(vo.getTotal());
        bpmExM.setPredictTwd(vo.getPredictTwd());
        bpmExM.setPrepayNo(String.join(",", vo.getPrepayNo()));
        bpmExM.setPayAmount(vo.getPayAmount());
        bpmExM.setRefundAmount(vo.getRefundAmount());
        bpmExM.setRefundDate(vo.getRefundDate());
        bpmExM.setBankAccount(vo.getBankAccount());
        bpmExM.setPayWayMethod(vo.getPayWayMethod());
        bpmExM.setApprovalMethod(vo.getApprovalMethod());
        bpmExM.setApprovalNo(vo.getApprovalNo());
        bpmExM.setWithholdTotal(vo.getWithholdTotal());
        bpmExM.setPostingDate(vo.getPostingDate());
        bpmExM.setCreatedUser(user.getUserId());
        bpmExM.setCreatedDate(LocalDate.now());
        bpmExM.setFlowStatus("1");

        // 把多個檔壓成一個 ZIP
        if (files != null && !files.isEmpty()) {
            try {
                byte[] Bytes = FileUtil.compressToZip(files);
                bpmExM.setAttachment(Bytes);
            } catch (IOException e) {
                LOG.error(e.toString());
                e.printStackTrace();
            }
        }
        bpmExM.setExNo(exNo);

        // endregion

        // region 明細

        int index = 1;

        for (BpmExMPplVo detailVo : vo.getBpmExMPplVoList()) {
            // region 人員明細

            BpmExPsD bpmExPsD = new BpmExPsD();
            bpmExPsD.setExItemNo(String.format("%03d", index));
            bpmExPsD.setPplId(detailVo.getPplId());
            bpmExPsD.setIsCivil(detailVo.getIsCivil());
            bpmExPsD.setExNo(exNo);
            bpmExPsD.setName(detailVo.getName());
            bpmExPsD.setJobTitle(detailVo.getJobTitle());
            bpmExPsD.setUnit(detailVo.getUnit());
            bpmExPsD.setAmount1(detailVo.getAmount1());
            bpmExPsD.setAmount2(detailVo.getAmount2());
            bpmExPsD.setAmount3(detailVo.getAmount3());
            bpmExPsD.setAmount4(detailVo.getAmount4());
            bpmExPsD.setYear(detailVo.getYear());
            bpmExPsD.setOuCode(detailVo.getOuCode());
            bpmExPsD.setAccounting1(detailVo.getAccounting1());
            bpmExPsD.setAccounting2(detailVo.getAccounting2());
            bpmExPsD.setAccounting3(detailVo.getAccounting3());
            bpmExPsD.setAccounting4(detailVo.getAccounting4());
            bpmExPsD.setTotalAmount(detailVo.getTotalAmount());
            BpmExPsD newBpmExPsD = bpmExPsDRepository.save(bpmExPsD);

            // endregion

            // region 預算動用

            if(bpmExPsD.getAccounting1() != null && !bpmExPsD.getAccounting1().isEmpty() && bpmExPsD.getAmount1().compareTo(BigDecimal.ZERO) > 0)
            {
                BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                budgetTranscation.setYear(bpmExPsD.getYear());
                budgetTranscation.setVersion("2");
                budgetTranscation.setOuCode(bpmExPsD.getOuCode());
                budgetTranscation.setAccounting(bpmExPsD.getAccounting1());
                budgetTranscation.setAmount(bpmExPsD.getAmount1());
                budgetTranscation.setTranscationSource("費用申請單");
                budgetTranscation.setTranscationNo(exNo);
                budgetTranscation.setTranscationDate(LocalDate.now());
                budgetTranscation.setTranscationType("動用");
                budgetTranscation.setBpNo(bpmExPsD.getPplId());
                budgetTranscation.setBpName(bpmExPsD.getName());
                budgetTranscation.setDocNo("");
                budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                budgetTranscation.setCreateDate(LocalDateTime.now());
                budgetTranscationList.add(budgetTranscation);
            }

            if(bpmExPsD.getAccounting2() != null && !bpmExPsD.getAccounting2().isEmpty() && bpmExPsD.getAmount2().compareTo(BigDecimal.ZERO) > 0)
            {
                BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                budgetTranscation.setYear(bpmExPsD.getYear());
                budgetTranscation.setVersion("2");
                budgetTranscation.setOuCode(bpmExPsD.getOuCode());
                budgetTranscation.setAccounting(bpmExPsD.getAccounting2());
                budgetTranscation.setAmount(bpmExPsD.getAmount2());
                budgetTranscation.setTranscationSource("費用申請單");
                budgetTranscation.setTranscationNo(exNo);
                budgetTranscation.setTranscationDate(LocalDate.now());
                budgetTranscation.setTranscationType("動用");
                budgetTranscation.setBpNo(bpmExPsD.getPplId());
                budgetTranscation.setBpName(bpmExPsD.getName());
                budgetTranscation.setDocNo("");
                budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                budgetTranscation.setCreateDate(LocalDateTime.now());
                budgetTranscationList.add(budgetTranscation);
            }

            if(bpmExPsD.getAccounting3() != null && !bpmExPsD.getAccounting3().isEmpty() && bpmExPsD.getAmount3().compareTo(BigDecimal.ZERO) > 0)
            {
                BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                budgetTranscation.setYear(bpmExPsD.getYear());
                budgetTranscation.setVersion("2");
                budgetTranscation.setOuCode(bpmExPsD.getOuCode());
                budgetTranscation.setAccounting(bpmExPsD.getAccounting3());
                budgetTranscation.setAmount(bpmExPsD.getAmount3());
                budgetTranscation.setTranscationSource("費用申請單");
                budgetTranscation.setTranscationNo(exNo);
                budgetTranscation.setTranscationDate(LocalDate.now());
                budgetTranscation.setTranscationType("動用");
                budgetTranscation.setBpNo(bpmExPsD.getPplId());
                budgetTranscation.setBpName(bpmExPsD.getName());
                budgetTranscation.setDocNo("");
                budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                budgetTranscation.setCreateDate(LocalDateTime.now());
                budgetTranscationList.add(budgetTranscation);
            }

            if(bpmExPsD.getAccounting4() != null && !bpmExPsD.getAccounting4().isEmpty() && bpmExPsD.getAmount4().compareTo(BigDecimal.ZERO) > 0)
            {
                BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                budgetTranscation.setYear(bpmExPsD.getYear());
                budgetTranscation.setVersion("2");
                budgetTranscation.setOuCode(bpmExPsD.getOuCode());
                budgetTranscation.setAccounting(bpmExPsD.getAccounting4());
                budgetTranscation.setAmount(bpmExPsD.getAmount4());
                budgetTranscation.setTranscationSource("費用申請單");
                budgetTranscation.setTranscationNo(exNo);
                budgetTranscation.setTranscationDate(LocalDate.now());
                budgetTranscation.setTranscationType("動用");
                budgetTranscation.setBpNo(bpmExPsD.getPplId());
                budgetTranscation.setBpName(bpmExPsD.getName());
                budgetTranscation.setDocNo("");
                budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                budgetTranscation.setCreateDate(LocalDateTime.now());
                budgetTranscationList.add(budgetTranscation);
            }

            // endregion 預算動用

            // region 扣繳清單

            //若有所得稅
            if (!detailVo.getBpmExMDetailWHVoList().isEmpty()) {
                List<BpmExDWH> bpmExDWHList = new ArrayList<>();

                int itemWhIndex = 1;
                for (BpmExMDetailWHVo bpmExMDetailWHVo : detailVo.getBpmExMDetailWHVoList()) {
                    BpmExDWH bpmExDWH = new BpmExDWH();
                    bpmExDWH.setExItemNo(newBpmExPsD.getExItemNo());
                    bpmExDWH.setExItemWhNo(String.format("%03d", itemWhIndex));
                    bpmExDWH.setExNo(exNo);
                    bpmExDWH.setBan(bpmExMDetailWHVo.getBan());
                    bpmExDWH.setErrorNote(bpmExMDetailWHVo.getErrorNote());
                    bpmExDWH.setCertificateCategory(bpmExMDetailWHVo.getCertificateCategory());
                    bpmExDWH.setGainerName(bpmExMDetailWHVo.getGainerName());
                    bpmExDWH.setGainerType(bpmExMDetailWHVo.getGainerType());
                    bpmExDWH.setHas183Days(bpmExMDetailWHVo.getHas183Days());
                    bpmExDWH.setCountryCode(bpmExMDetailWHVo.getCountryCode());
                    bpmExDWH.setHasPassport(bpmExMDetailWHVo.getHasPassport());
                    bpmExDWH.setPassportNo(bpmExMDetailWHVo.getPassportNo());
                    bpmExDWH.setHasAddress(bpmExMDetailWHVo.getHasAddress());
                    bpmExDWH.setAddress(bpmExMDetailWHVo.getAddress());
                    bpmExDWH.setContactAddress(bpmExMDetailWHVo.getContactAddress());
                    bpmExDWH.setContactPhone(bpmExMDetailWHVo.getContactPhone());
                    bpmExDWH.setPaymentYear(bpmExMDetailWHVo.getPaymentYear());
                    bpmExDWH.setPaymentMonthSt(bpmExMDetailWHVo.getPaymentMonthSt());
                    bpmExDWH.setPaymentMonthEd(bpmExMDetailWHVo.getPaymentMonthEd());
                    bpmExDWH.setIncomeNote(bpmExMDetailWHVo.getIncomeNote());
                    bpmExDWH.setIncomeType(bpmExMDetailWHVo.getIncomeType());
                    bpmExDWH.setGrossPayment(bpmExMDetailWHVo.getGrossPayment());
                    bpmExDWH.setWithholdingTaxRate(bpmExMDetailWHVo.getWithholdingTaxRate());
                    bpmExDWH.setWithholdingTax(bpmExMDetailWHVo.getWithholdingTax());
                    bpmExDWH.setNhRate(bpmExMDetailWHVo.getNhRate());
                    bpmExDWH.setRevenueCategory(bpmExMDetailWHVo.getRevenueCategory());
                    bpmExDWH.setNhWithHolding(bpmExMDetailWHVo.getNhWithHolding());
                    bpmExDWH.setNetPayment(bpmExMDetailWHVo.getNetPayment());
                    bpmExDWH.setPaymentDate(bpmExMDetailWHVo.getPaymentDate());
                    bpmExDWH.setSoftwareNote(bpmExMDetailWHVo.getSoftwareNote());
                    bpmExDWH.setTaxTreatyCode(bpmExMDetailWHVo.getTaxTreatyCode());
                    bpmExDWH.setTaxCreditFlag(bpmExMDetailWHVo.getTaxCreditFlag());
                    bpmExDWH.setCertificateIssueMethod(bpmExMDetailWHVo.getCertificateIssueMethod());
                    bpmExDWH.setTaxIdentificationNo(bpmExMDetailWHVo.getTaxIdentificationNo());
                    bpmExDWH.setShareColumn1(bpmExMDetailWHVo.getShareColumn1());
                    bpmExDWH.setShareColumn2(bpmExMDetailWHVo.getShareColumn2());
                    bpmExDWH.setShareColumn3(bpmExMDetailWHVo.getShareColumn3());
                    bpmExDWH.setShareColumn4(bpmExMDetailWHVo.getShareColumn4());
                    bpmExDWH.setShareColumn5(bpmExMDetailWHVo.getShareColumn5());
                    bpmExDWH.setRemark(bpmExMDetailWHVo.getRemark());

                    bpmExDWHList.add(bpmExDWH);

                    //更改董監事或研發委員的電話,聯絡地址第1筆都是該所得人,只有董監事會有第2筆
                    if(itemWhIndex == 1)
                    {
                        switch (bpmExM.getApplyType())
                        {
                            //董監事
                            case "7":

                                ncccDirectorSupervisorListRepository.updateFields(bpmExMDetailWHVo.getBan(),bpmExMDetailWHVo.getContactPhone(),bpmExMDetailWHVo.getAddress(),bpmExMDetailWHVo.getContactAddress());

                                break;

                            //研發委員
                            case "8":

                                ncccCommitteeListRepository.updateFields(bpmExMDetailWHVo.getBan(),bpmExMDetailWHVo.getContactPhone(),bpmExMDetailWHVo.getAddress(),bpmExMDetailWHVo.getContactAddress());

                                break;
                        }

                    }

                    itemWhIndex++;
                }

                bpmExDWHRepository.saveAll(bpmExDWHList);

            }

            index++;

            // endregion
        }

        // endregion


        // region 代扣繳項目
        List<BpmExMWH> bpmExMWHList = new ArrayList<>();

        int wIndex = 1;
        for (BpmExMWHVo bpmExMWHVo : vo.getBpmExMWHVoList()) {
            BpmExMWH bpmExMWH = new BpmExMWH();
            bpmExMWH.setExNo(exNo);
            bpmExMWH.setExWhNo(String.format("%03d", wIndex));
            bpmExMWH.setName(bpmExMWHVo.getName());
            bpmExMWH.setAccounting(bpmExMWHVo.getAccounting());
            bpmExMWH.setAmount(bpmExMWHVo.getAmount());
            bpmExMWH.setAccountingName(bpmExMWHVo.getAccountingName());
            bpmExMWH.setItemWHText(bpmExMWHVo.getItemWHText());
            bpmExMWH.setPayDate(bpmExMWHVo.getPayDate());
            bpmExMWHList.add(bpmExMWH);
            wIndex++;
        }

        bpmExMWHRepository.saveAll(bpmExMWHList);
        // endregion

        // region 預算動用寫入

        for(BudgetVo.BudgetTranscation budgetTranscation : budgetTranscationList)
        {
            ncccBudgetService.WriteBudgetTranscation(budgetTranscation);
        }

        // endregion

        // region 簽核流程判斷

        // 用來判斷是否是單位主管以上
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
        }
        Map<String, Object> vars = new HashMap<>();
        vars.put(AssigneeRole.INITIATOR.getKey(), hrid);
        BigDecimal amount = bpmExM.getTotal();
        vars.put("amount", amount);

        processKey = ProcessDefinitionKey.APPLICATIONEXPENSES_UTILITYEXPENSES.getKey();

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

        if (approvers.isEmpty()) {
            throw new RuntimeException("非單位主管以上，但找不到上層主管");
        }

        vars.put(AssigneeRole.APPROVERS.getKey(), approvers);
        vars.put(AssigneeRole.CURRENT_INDEX.getKey(), 0);
        vars.put(AssigneeRole.CURRENT_APPROVER.getKey(), approvers.get(0));
        // 單位主管
        vars.put(AssigneeRole.APPLICANT_SUPERVISOR.getKey(),
                syncOURepository.findByOuCode(approvers.get(approvers.size() - 1)));
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

        vars.put(AssigneeRole.SAP_USER.getKey(), null);

        // endregion

        BpmExM saved = bpmExMRepository.save(bpmExM);

        String processId = flowableService.startProcess(processKey, exNo, vars);

        saved.setTaskId(processId);

        bpmExMRepository.save(saved);

        DecisionVo decisionVo = new DecisionVo();

        decisionVo.setProcessId(processId);

        decisionVo.setDecision(Decision.SUBMIT);

        flowableService.completeTask(decisionVo);

        return exNo;
    }

    @Override
    public String update(BpmExMVo vo, List<MultipartFile> files) {
        String exNo = vo.getExNo();
        NcccUserDto user = SecurityUtil.getCurrentUser();
        // region 主表處理
        BpmExM bpmExM = bpmExMRepository.findById(exNo).orElseThrow(() -> new RuntimeException("找不到主檔: EX_NO=" + exNo));
        if (bpmExM.getApplyType().equals("7") || bpmExM.getApplyType().equals("8")) {
            throw new RuntimeException("非對應費用申請單: EX_NO=" + exNo);
        }

        // 檢查憑證是否有存在DB 有代表被使用過(不包含作廢的)
        List<String> codes =
                vo.getBpmExMDetailVoList().stream().map(BpmExMDetailVo::getCertificateCode).filter(Objects::nonNull)
                        .map(String::trim).filter(s -> !s.isEmpty()).distinct().collect(Collectors.toList());
        if (!codes.isEmpty()) {
            List<String> conflicts = bpmExMD1Repository.findConflictedCertificateCodesWithoutExNo(exNo,codes);
            if (!conflicts.isEmpty()) {
                throw new IllegalStateException("以下憑證號已使用，請檢查：" + String.join(", ", conflicts));
            }
            // 呼叫SAP GUI檢核
            List<TInput> tInputList = buildTInputList(vo.getBpmExMDetailVoList());
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

        List<BudgetVo.BudgetTranscation> budgetTranscationList =  new ArrayList<>();

        bpmExM.setApplyDate(vo.getApplyDate());
        bpmExM.setApplyType(vo.getApplyType());
        bpmExM.setApplicant(vo.getApplicant());
        bpmExM.setEmpNo(vo.getEmpNo());
        bpmExM.setDepartment(vo.getDepartment());
        bpmExM.setPayEmpNo(vo.getPayEmpNo());
        bpmExM.setPayName(vo.getPayName());
        bpmExM.setPayWay(vo.getPayWay());
        bpmExM.setSpecialPayDate(vo.getSpecialPayDate());
        bpmExM.setApplyReason(vo.getApplyReason());
        bpmExM.setTradeCurrency(vo.getTradeCurrency());
        bpmExM.setCurrencyRate(vo.getCurrencyRate());
        bpmExM.setTotal(vo.getTotal());
        bpmExM.setPredictTwd(vo.getPredictTwd());
        bpmExM.setPrepayNo(String.join(",", vo.getPrepayNo()));
        bpmExM.setPayAmount(vo.getPayAmount());
        bpmExM.setRefundAmount(vo.getRefundAmount());
        bpmExM.setRefundDate(vo.getRefundDate());
        bpmExM.setBankAccount(vo.getBankAccount());
        bpmExM.setApprovalMethod(vo.getApprovalMethod());
        bpmExM.setApprovalNo(vo.getApprovalNo());
        bpmExM.setPayWayMethod(vo.getPayWayMethod());
        bpmExM.setWithholdTotal(vo.getWithholdTotal());
        bpmExM.setPostingDate(vo.getPostingDate());
        bpmExM.setModifiedUser(user.getUserId());
        bpmExM.setModifiedDate(LocalDate.now());
        bpmExM.setFlowStatus("1");
        // 把多個檔壓成一個 ZIP
        if (files != null && !files.isEmpty()) {
            try {
                byte[] Bytes = FileUtil.compressToZip(files);
                bpmExM.setAttachment(Bytes);
            } catch (IOException e) {
                LOG.error(e.toString());
                e.printStackTrace();
            }
        }

        // endregion

        // region 刪除明細及相關列表
        bpmSplitMRepository.deleteByExNo(exNo);
        bpmTaskItemRepository.deleteByExNo(exNo);
        bpmExMD1Repository.deleteByExNo(exNo);
        bpmExDWHRepository.deleteByExNo(exNo);
        bpmExMWHRepository.deleteByExNo(exNo);
        // endregion

        int index = 1;

        String prefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));

        String serialStr = exNo.substring(exNo.length() - 4);

        for (BpmExMDetailVo bpmExMDetailVo : vo.getBpmExMDetailVoList()) {
            // region 明細
            BpmExMD1 bpmExMD1 = new BpmExMD1();

            //自動產號
            if(bpmExMDetailVo.getCertificateType() != null && AUTO_GENERATE_CERTIFICATE_CODE.contains(bpmExMDetailVo.getCertificateType()))
            {
                //若為空值,代表新增明細或有所更改
                if(bpmExMDetailVo.getCertificateCode() == null || bpmExMDetailVo.getCertificateCode().isEmpty())
                {
                    bpmExMDetailVo.setCertificateCode(prefix + serialStr + String.format("%03d", index));
                }
            }

            bpmExMD1.setExNo(exNo);
            bpmExMD1.setExItemNo(String.format("%03d", index));
            bpmExMD1.setCopyExNo(bpmExMDetailVo.getCopyExNo());
            bpmExMD1.setCertificateDate(bpmExMDetailVo.getCertificateDate());
            bpmExMD1.setCertificateCode(bpmExMDetailVo.getCertificateCode());
            bpmExMD1.setCertificateType(bpmExMDetailVo.getCertificateType());
            bpmExMD1.setUniformNum(bpmExMDetailVo.getUniformNum());
            bpmExMD1.setApplyAmount(bpmExMDetailVo.getApplyAmount());
            bpmExMD1.setUntaxAmount(bpmExMDetailVo.getUntaxAmount());
            bpmExMD1.setTax(bpmExMDetailVo.getTax());
            bpmExMD1.setTaxCode(bpmExMDetailVo.getTaxCode());
            bpmExMD1.setTaxRate(bpmExMDetailVo.getTaxRate());
            bpmExMD1.setItemCode(bpmExMDetailVo.getItemCode());
            bpmExMD1.setItemName(bpmExMDetailVo.getItemName());
            bpmExMD1.setCostCenter(bpmExMDetailVo.getCostCenter());
            bpmExMD1.setDescription(bpmExMDetailVo.getDescription());
            bpmExMD1.setDeduction(bpmExMDetailVo.getDeduction());
            bpmExMD1.setHasIncomeTax(bpmExMDetailVo.getHasIncomeTax());
            bpmExMD1.setRemark(bpmExMDetailVo.getRemark());
            bpmExMD1.setItemText(bpmExMDetailVo.getItemText());
            bpmExMD1.setZeroTaxAmount(bpmExMDetailVo.getZeroTaxAmount());
            bpmExMD1.setDutyFreeAmount(bpmExMDetailVo.getDutyFreeAmount());
            bpmExMD1.setChtCode(bpmExMDetailVo.getChtCode());
            bpmExMD1.setChtCollectAmount(bpmExMDetailVo.getChtCollectAmount());
            bpmExMD1.setCarrierNumber(bpmExMDetailVo.getCarrierNumber());
            bpmExMD1.setNotificationNumber(bpmExMDetailVo.getNotificationNumber());
            bpmExMD1.setYear(bpmExMDetailVo.getYear());
            bpmExMD1.setOuCode(bpmExMDetailVo.getOuCode());
            bpmExMD1.setAccounting(bpmExMDetailVo.getAccounting());
            bpmExMD1.setMultiShare(!bpmExMDetailVo.getBpmExMDetailSplitVoList().isEmpty() ?"Y":"N");
            bpmExMD1.setModifiedUser(user.getUserId());
            bpmExMD1.setModifiedDate(LocalDate.now());
            BpmExMD1 newBpmExMD1 = bpmExMD1Repository.save(bpmExMD1);

            // region 預算動用
            // 若沒有多重分攤預算動用 (零用金與預付借支不會動到預算)
            if(bpmExMD1.getMultiShare().equals("N") && (!bpmExM.getApplyType().equals("2") && !bpmExM.getApplyType().equals("5")))
            {
                BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                budgetTranscation.setYear(bpmExMD1.getYear());
                budgetTranscation.setVersion("2");
                budgetTranscation.setOuCode(bpmExMD1.getOuCode());
                budgetTranscation.setAccounting(bpmExMD1.getAccounting());
                budgetTranscation.setAmount(bpmExMD1.getUntaxAmount());
                budgetTranscation.setTranscationSource("費用申請單");
                budgetTranscation.setTranscationNo(exNo);
                budgetTranscation.setTranscationDate(LocalDate.now());
                budgetTranscation.setTranscationType("動用");
                budgetTranscation.setBpNo(bpmExM.getEmpNo());
                budgetTranscation.setBpName(bpmExM.getEmpNo());
                budgetTranscation.setDocNo(bpmExM.getApprovalNo());
                budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                budgetTranscation.setCreateDate(LocalDateTime.now());
                budgetTranscationList.add(budgetTranscation);
            }

            // endregion

            // region 多種分攤

            int splitIndex = 1;
            for (BpmExMDetailSplitVo bpmExMDetailSplitVo : bpmExMDetailVo.getBpmExMDetailSplitVoList()) {
                BpmSplitM bpmSplitM = new BpmSplitM();
                bpmSplitM.setExItemNo(newBpmExMD1.getExItemNo());
                bpmSplitM.setExNo(exNo);
                bpmSplitM.setExItemSplitNo(String.format("%03d", splitIndex));
                bpmSplitM.setAccounting(bpmExMDetailSplitVo.getAccounting());
                bpmSplitM.setYear(bpmExMDetailSplitVo.getYear());
                bpmSplitM.setOuCode(bpmExMDetailSplitVo.getOuCode());
                bpmSplitM.setUntaxAmount(bpmExMDetailSplitVo.getUntaxAmount());
                bpmSplitM.setRemark(bpmExMDetailSplitVo.getRemark());
                bpmSplitM.setItemText(bpmExMDetailSplitVo.getItemText());
                bpmSplitM.setDescription(bpmExMDetailSplitVo.getDescription());
                bpmSplitM.setAllocationMethod(bpmExMDetailSplitVo.getAllocationMethod());
                bpmSplitM.setCreatedUser(user.getUserId());
                bpmSplitM.setCreatedDate(LocalDate.now());
                BpmSplitM newBpmSplitM = bpmSplitMRepository.save(bpmSplitM);

                // region 預算動用 (零用金與預付借支不會動到預算)

                if((!bpmExM.getApplyType().equals("2") && !bpmExM.getApplyType().equals("5")))
                {
                    BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                    budgetTranscation.setYear(bpmSplitM.getYear());
                    budgetTranscation.setVersion("2");
                    budgetTranscation.setOuCode(bpmSplitM.getOuCode());
                    budgetTranscation.setAccounting(bpmSplitM.getAccounting());
                    budgetTranscation.setAmount(bpmSplitM.getUntaxAmount());
                    budgetTranscation.setTranscationSource("費用申請單");
                    budgetTranscation.setTranscationNo(exNo);
                    budgetTranscation.setTranscationDate(LocalDate.now());
                    budgetTranscation.setTranscationType("動用");
                    budgetTranscation.setBpNo(bpmExM.getEmpNo());
                    budgetTranscation.setBpName(bpmExM.getEmpNo());
                    budgetTranscation.setDocNo(bpmExM.getApprovalNo());
                    budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                    budgetTranscation.setCreateDate(LocalDateTime.now());
                    budgetTranscationList.add(budgetTranscation);
                }

                // endregion 預算動用

                //多重分攤
                List<BpmTaskItem> bpmTaskItemList = new ArrayList<>();

                int splitTaskIndex = 1;
                for(BpmExMDetailSplitTaskItemVo taskItemVo : bpmExMDetailSplitVo.getTaskItemVoList())
                {
                    BpmTaskItem bpmTaskItem = new BpmTaskItem();
                    bpmTaskItem.setExItemNo(newBpmSplitM.getExItemNo());
                    bpmTaskItem.setExItemSplitNo(newBpmSplitM.getExItemSplitNo());
                    bpmTaskItem.setExItemSplitTaskNo(String.format("%03d", splitTaskIndex));
                    bpmTaskItem.setExNo(exNo);
                    bpmTaskItem.setOperateItemCode(taskItemVo.getCode());
                    bpmTaskItem.setUntaxAmount(taskItemVo.getAmount());
                    bpmTaskItem.setOperateRatio(taskItemVo.getRatio());
                    bpmTaskItem.setItemText(taskItemVo.getItemText());
                    bpmTaskItem.setDescription(taskItemVo.getDescription());
                    bpmTaskItem.setRemark(taskItemVo.getRemark());

                    bpmTaskItemList.add(bpmTaskItem);

                    splitTaskIndex++;
                }

                bpmTaskItemRepository.saveAll(bpmTaskItemList);

                splitIndex++;
            }
            // endregion

            // region 扣繳清單

            //若有所得稅
            if (bpmExMD1.getHasIncomeTax() != null && bpmExMD1.getHasIncomeTax().equals("1")) {
                List<BpmExDWH> bpmExDWHList = new ArrayList<BpmExDWH>();

                int itemWhIndex = 1;
                for (BpmExMDetailWHVo bpmExMDetailWHVo : bpmExMDetailVo.getBpmExMDetailWHVoList()) {
                    BpmExDWH bpmExDWH = new BpmExDWH();
                    bpmExDWH.setExItemNo(newBpmExMD1.getExItemNo());
                    bpmExDWH.setExNo(exNo);
                    bpmExDWH.setExItemWhNo(String.format("%03d", itemWhIndex));
                    bpmExDWH.setBan(bpmExMDetailWHVo.getBan());
                    bpmExDWH.setErrorNote(bpmExMDetailWHVo.getErrorNote());
                    bpmExDWH.setCertificateCategory(bpmExMDetailWHVo.getCertificateCategory());
                    bpmExDWH.setGainerName(bpmExMDetailWHVo.getGainerName());
                    bpmExDWH.setGainerType(bpmExMDetailWHVo.getGainerType());
                    bpmExDWH.setHas183Days(bpmExMDetailWHVo.getHas183Days());
                    bpmExDWH.setCountryCode(bpmExMDetailWHVo.getCountryCode());
                    bpmExDWH.setHasPassport(bpmExMDetailWHVo.getHasPassport());
                    bpmExDWH.setPassportNo(bpmExMDetailWHVo.getPassportNo());
                    bpmExDWH.setHasAddress(bpmExMDetailWHVo.getHasAddress());
                    bpmExDWH.setAddress(bpmExMDetailWHVo.getAddress());
                    bpmExDWH.setContactAddress(bpmExMDetailWHVo.getContactAddress());
                    bpmExDWH.setContactPhone(bpmExMDetailWHVo.getContactPhone());
                    bpmExDWH.setPaymentYear(bpmExMDetailWHVo.getPaymentYear());
                    bpmExDWH.setPaymentMonthSt(bpmExMDetailWHVo.getPaymentMonthSt());
                    bpmExDWH.setPaymentMonthEd(bpmExMDetailWHVo.getPaymentMonthEd());
                    bpmExDWH.setIncomeNote(bpmExMDetailWHVo.getIncomeNote());
                    bpmExDWH.setIncomeType(bpmExMDetailWHVo.getIncomeType());
                    bpmExDWH.setGrossPayment(bpmExMDetailWHVo.getGrossPayment());
                    bpmExDWH.setWithholdingTaxRate(bpmExMDetailWHVo.getWithholdingTaxRate());
                    bpmExDWH.setWithholdingTax(bpmExMDetailWHVo.getWithholdingTax());
                    bpmExDWH.setNhRate(bpmExMDetailWHVo.getNhRate());
                    bpmExDWH.setRevenueCategory(bpmExMDetailWHVo.getRevenueCategory());
                    bpmExDWH.setNhWithHolding(bpmExMDetailWHVo.getNhWithHolding());
                    bpmExDWH.setNetPayment(bpmExMDetailWHVo.getNetPayment());
                    bpmExDWH.setPaymentDate(bpmExMDetailWHVo.getPaymentDate());
                    bpmExDWH.setSoftwareNote(bpmExMDetailWHVo.getSoftwareNote());
                    bpmExDWH.setTaxTreatyCode(bpmExMDetailWHVo.getTaxTreatyCode());
                    bpmExDWH.setTaxCreditFlag(bpmExMDetailWHVo.getTaxCreditFlag());
                    bpmExDWH.setCertificateIssueMethod(bpmExMDetailWHVo.getCertificateIssueMethod());
                    bpmExDWH.setTaxIdentificationNo(bpmExMDetailWHVo.getTaxIdentificationNo());
                    bpmExDWH.setShareColumn1(bpmExMDetailWHVo.getShareColumn1());
                    bpmExDWH.setShareColumn2(bpmExMDetailWHVo.getShareColumn2());
                    bpmExDWH.setShareColumn3(bpmExMDetailWHVo.getShareColumn3());
                    bpmExDWH.setShareColumn4(bpmExMDetailWHVo.getShareColumn4());
                    bpmExDWH.setShareColumn5(bpmExMDetailWHVo.getShareColumn5());
                    bpmExDWH.setRemark(bpmExMDetailWHVo.getRemark());

                    bpmExDWHList.add(bpmExDWH);
                    itemWhIndex++;
                }

                bpmExDWHRepository.saveAll(bpmExDWHList);

            }

            // endregion

            // endregion

            index++;
        }

        // region 代扣繳項目

        List<BpmExMWH> bpmExMWHList = new ArrayList<>();
        int wIndex = 1;

        for (BpmExMWHVo bpmExMWHVo : vo.getBpmExMWHVoList()) {
            BpmExMWH bpmExMWH = new BpmExMWH();
            bpmExMWH.setExWhNo(String.format("%03d", wIndex));
            bpmExMWH.setExNo(exNo);
            bpmExMWH.setName(bpmExMWHVo.getName());
            bpmExMWH.setAccounting(bpmExMWHVo.getAccounting());
            bpmExMWH.setAmount(bpmExMWHVo.getAmount());
            bpmExMWH.setAccountingName(bpmExMWHVo.getAccountingName());
            bpmExMWH.setItemWHText(bpmExMWHVo.getItemWHText());
            bpmExMWH.setPayDate(bpmExMWHVo.getPayDate());
            bpmExMWHList.add(bpmExMWH);
            wIndex++;
        }

        bpmExMWHRepository.saveAll(bpmExMWHList);
        // endregion

        // region 預算動用寫入

        for(BudgetVo.BudgetTranscation budgetTranscation : budgetTranscationList)
        {
            ncccBudgetService.WriteBudgetTranscation(budgetTranscation);
        }

        // endregion

        bpmExMRepository.save(bpmExM);

        DecisionVo decisionVo = new DecisionVo();
        decisionVo.setProcessId(bpmExM.getTaskId());
        decisionVo.setDecision(Decision.SUBMIT);
        flowableService.completeTask(decisionVo);

        return exNo;
    }

    @Override
    @Transactional
    public String updateDSC(BpmExMVo vo, List<MultipartFile> files) {
        String exNo = vo.getExNo();
        NcccUserDto user = SecurityUtil.getCurrentUser();
        // region 主表處理
        BpmExM bpmExM = bpmExMRepository.findById(exNo).orElseThrow(() -> new RuntimeException("找不到主檔: EX_NO=" + exNo));
        if (!bpmExM.getApplyType().equals("7") && !bpmExM.getApplyType().equals("8")) {
            throw new RuntimeException("非對應費用申請單: EX_NO=" + exNo);
        }
        List<BudgetVo.BudgetTranscation> budgetTranscationList =  new ArrayList<>();

        bpmExM.setApplyDate(vo.getApplyDate());
        bpmExM.setApplyType(vo.getApplyType());
        bpmExM.setApplicant(vo.getApplicant());
        bpmExM.setEmpNo(vo.getEmpNo());
        bpmExM.setDepartment(vo.getDepartment());
        bpmExM.setPayEmpNo(vo.getPayEmpNo());
        bpmExM.setPayName(vo.getPayName());
        bpmExM.setPayWay(vo.getPayWay());
        bpmExM.setSpecialPayDate(vo.getSpecialPayDate());
        bpmExM.setApplyReason(vo.getApplyReason());
        bpmExM.setTradeCurrency(vo.getTradeCurrency());
        bpmExM.setCurrencyRate(vo.getCurrencyRate());
        bpmExM.setTotal(vo.getTotal());
        bpmExM.setPredictTwd(vo.getPredictTwd());
        bpmExM.setPrepayNo(String.join(",", vo.getPrepayNo()));
        bpmExM.setPayAmount(vo.getPayAmount());
        bpmExM.setRefundAmount(vo.getRefundAmount());
        bpmExM.setRefundDate(vo.getRefundDate());
        bpmExM.setBankAccount(vo.getBankAccount());
        bpmExM.setApprovalMethod(vo.getApprovalMethod());
        bpmExM.setApprovalNo(vo.getApprovalNo());
        bpmExM.setPayWayMethod(vo.getPayWayMethod());
        bpmExM.setWithholdTotal(vo.getWithholdTotal());
        bpmExM.setPostingDate(vo.getPostingDate());
        bpmExM.setModifiedUser(user.getUserId());
        bpmExM.setModifiedDate(LocalDate.now());
        bpmExM.setFlowStatus("1");
        // 把多個檔壓成一個 ZIP
        if (files != null && !files.isEmpty()) {
            try {
                byte[] Bytes = FileUtil.compressToZip(files);
                bpmExM.setAttachment(Bytes);
            } catch (IOException e) {
                LOG.error(e.toString());
                e.printStackTrace();
            }
        }
        // region 刪除明細及相關列表

        bpmExPsDRepository.deleteByExNo(exNo);
        bpmExDWHRepository.deleteByExNo(exNo);
        bpmExMWHRepository.deleteByExNo(exNo);
        // endregion

        // region 明細
        int index = 1;
        for (BpmExMPplVo detailVo : vo.getBpmExMPplVoList()) {
            // region 人員明細

            BpmExPsD bpmExPsD = new BpmExPsD();
            bpmExPsD.setExItemNo(String.format("%03d", index));
            bpmExPsD.setPplId(detailVo.getPplId());
            bpmExPsD.setIsCivil(detailVo.getIsCivil());
            bpmExPsD.setExNo(exNo);
            bpmExPsD.setName(detailVo.getName());
            bpmExPsD.setJobTitle(detailVo.getJobTitle());
            bpmExPsD.setUnit(detailVo.getUnit());
            bpmExPsD.setAmount1(detailVo.getAmount1());
            bpmExPsD.setAmount2(detailVo.getAmount2());
            bpmExPsD.setAmount3(detailVo.getAmount3());
            bpmExPsD.setAmount4(detailVo.getAmount4());
            bpmExPsD.setTotalAmount(detailVo.getTotalAmount());
            bpmExPsD.setYear(detailVo.getYear());
            bpmExPsD.setOuCode(detailVo.getOuCode());
            bpmExPsD.setAccounting1(detailVo.getAccounting1());
            bpmExPsD.setAccounting2(detailVo.getAccounting2());
            bpmExPsD.setAccounting3(detailVo.getAccounting3());
            bpmExPsD.setAccounting4(detailVo.getAccounting4());
            BpmExPsD newBpmExPsD = bpmExPsDRepository.save(bpmExPsD);

            // endregion

            // region 預算動用

            if(bpmExPsD.getAccounting1() != null && !bpmExPsD.getAccounting1().isEmpty() && bpmExPsD.getAmount1().compareTo(BigDecimal.ZERO) > 0)
            {
                BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                budgetTranscation.setYear(bpmExPsD.getYear());
                budgetTranscation.setVersion("2");
                budgetTranscation.setOuCode(bpmExPsD.getOuCode());
                budgetTranscation.setAccounting(bpmExPsD.getAccounting1());
                budgetTranscation.setAmount(bpmExPsD.getAmount1());
                budgetTranscation.setTranscationSource("費用申請單");
                budgetTranscation.setTranscationNo(exNo);
                budgetTranscation.setTranscationDate(LocalDate.now());
                budgetTranscation.setTranscationType("動用");
                budgetTranscation.setBpNo(bpmExPsD.getPplId());
                budgetTranscation.setBpName(bpmExPsD.getName());
                budgetTranscation.setDocNo("");
                budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                budgetTranscation.setCreateDate(LocalDateTime.now());
                budgetTranscationList.add(budgetTranscation);
            }

            if(bpmExPsD.getAccounting2() != null && !bpmExPsD.getAccounting2().isEmpty() && bpmExPsD.getAmount2().compareTo(BigDecimal.ZERO) > 0)
            {
                BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                budgetTranscation.setYear(bpmExPsD.getYear());
                budgetTranscation.setVersion("2");
                budgetTranscation.setOuCode(bpmExPsD.getOuCode());
                budgetTranscation.setAccounting(bpmExPsD.getAccounting2());
                budgetTranscation.setAmount(bpmExPsD.getAmount2());
                budgetTranscation.setTranscationSource("費用申請單");
                budgetTranscation.setTranscationNo(exNo);
                budgetTranscation.setTranscationDate(LocalDate.now());
                budgetTranscation.setTranscationType("動用");
                budgetTranscation.setBpNo(bpmExPsD.getPplId());
                budgetTranscation.setBpName(bpmExPsD.getName());
                budgetTranscation.setDocNo("");
                budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                budgetTranscation.setCreateDate(LocalDateTime.now());
                budgetTranscationList.add(budgetTranscation);
            }

            if(bpmExPsD.getAccounting3() != null && !bpmExPsD.getAccounting3().isEmpty() && bpmExPsD.getAmount3().compareTo(BigDecimal.ZERO) > 0)
            {
                BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                budgetTranscation.setYear(bpmExPsD.getYear());
                budgetTranscation.setVersion("2");
                budgetTranscation.setOuCode(bpmExPsD.getOuCode());
                budgetTranscation.setAccounting(bpmExPsD.getAccounting3());
                budgetTranscation.setAmount(bpmExPsD.getAmount3());
                budgetTranscation.setTranscationSource("費用申請單");
                budgetTranscation.setTranscationNo(exNo);
                budgetTranscation.setTranscationDate(LocalDate.now());
                budgetTranscation.setTranscationType("動用");
                budgetTranscation.setBpNo(bpmExPsD.getPplId());
                budgetTranscation.setBpName(bpmExPsD.getName());
                budgetTranscation.setDocNo("");
                budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                budgetTranscation.setCreateDate(LocalDateTime.now());
                budgetTranscationList.add(budgetTranscation);
            }

            if(bpmExPsD.getAccounting4() != null && !bpmExPsD.getAccounting4().isEmpty() && bpmExPsD.getAmount4().compareTo(BigDecimal.ZERO) > 0)
            {
                BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                budgetTranscation.setYear(bpmExPsD.getYear());
                budgetTranscation.setVersion("2");
                budgetTranscation.setOuCode(bpmExPsD.getOuCode());
                budgetTranscation.setAccounting(bpmExPsD.getAccounting4());
                budgetTranscation.setAmount(bpmExPsD.getAmount4());
                budgetTranscation.setTranscationSource("費用申請單");
                budgetTranscation.setTranscationNo(exNo);
                budgetTranscation.setTranscationDate(LocalDate.now());
                budgetTranscation.setTranscationType("動用");
                budgetTranscation.setBpNo(bpmExPsD.getPplId());
                budgetTranscation.setBpName(bpmExPsD.getName());
                budgetTranscation.setDocNo("");
                budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                budgetTranscation.setCreateDate(LocalDateTime.now());
                budgetTranscationList.add(budgetTranscation);
            }

            // endregion 預算動用

            // region 扣繳清單

            //若有所得稅
            if (!detailVo.getBpmExMDetailWHVoList().isEmpty()) {
                List<BpmExDWH> bpmExDWHList = new ArrayList<>();
                int itemWhIndex = 1;
                for (BpmExMDetailWHVo bpmExMDetailWHVo : detailVo.getBpmExMDetailWHVoList()) {
                    BpmExDWH bpmExDWH = new BpmExDWH();
                    bpmExDWH.setExItemNo(newBpmExPsD.getExItemNo());
                    bpmExDWH.setExNo(exNo);
                    bpmExDWH.setExItemWhNo(String.format("%03d", itemWhIndex));
                    bpmExDWH.setBan(bpmExMDetailWHVo.getBan());
                    bpmExDWH.setErrorNote(bpmExMDetailWHVo.getErrorNote());
                    bpmExDWH.setCertificateCategory(bpmExMDetailWHVo.getCertificateCategory());
                    bpmExDWH.setGainerName(bpmExMDetailWHVo.getGainerName());
                    bpmExDWH.setGainerType(bpmExMDetailWHVo.getGainerType());
                    bpmExDWH.setHas183Days(bpmExMDetailWHVo.getHas183Days());
                    bpmExDWH.setCountryCode(bpmExMDetailWHVo.getCountryCode());
                    bpmExDWH.setHasPassport(bpmExMDetailWHVo.getHasPassport());
                    bpmExDWH.setPassportNo(bpmExMDetailWHVo.getPassportNo());
                    bpmExDWH.setHasAddress(bpmExMDetailWHVo.getHasAddress());
                    bpmExDWH.setAddress(bpmExMDetailWHVo.getAddress());
                    bpmExDWH.setContactAddress(bpmExMDetailWHVo.getContactAddress());
                    bpmExDWH.setContactPhone(bpmExMDetailWHVo.getContactPhone());
                    bpmExDWH.setPaymentYear(bpmExMDetailWHVo.getPaymentYear());
                    bpmExDWH.setPaymentMonthSt(bpmExMDetailWHVo.getPaymentMonthSt());
                    bpmExDWH.setPaymentMonthEd(bpmExMDetailWHVo.getPaymentMonthEd());
                    bpmExDWH.setIncomeNote(bpmExMDetailWHVo.getIncomeNote());
                    bpmExDWH.setIncomeType(bpmExMDetailWHVo.getIncomeType());
                    bpmExDWH.setGrossPayment(bpmExMDetailWHVo.getGrossPayment());
                    bpmExDWH.setWithholdingTaxRate(bpmExMDetailWHVo.getWithholdingTaxRate());
                    bpmExDWH.setWithholdingTax(bpmExMDetailWHVo.getWithholdingTax());
                    bpmExDWH.setNhRate(bpmExMDetailWHVo.getNhRate());
                    bpmExDWH.setRevenueCategory(bpmExMDetailWHVo.getRevenueCategory());
                    bpmExDWH.setNhWithHolding(bpmExMDetailWHVo.getNhWithHolding());
                    bpmExDWH.setNetPayment(bpmExMDetailWHVo.getNetPayment());
                    bpmExDWH.setPaymentDate(bpmExMDetailWHVo.getPaymentDate());
                    bpmExDWH.setSoftwareNote(bpmExMDetailWHVo.getSoftwareNote());
                    bpmExDWH.setTaxTreatyCode(bpmExMDetailWHVo.getTaxTreatyCode());
                    bpmExDWH.setTaxCreditFlag(bpmExMDetailWHVo.getTaxCreditFlag());
                    bpmExDWH.setCertificateIssueMethod(bpmExMDetailWHVo.getCertificateIssueMethod());
                    bpmExDWH.setTaxIdentificationNo(bpmExMDetailWHVo.getTaxIdentificationNo());
                    bpmExDWH.setShareColumn1(bpmExMDetailWHVo.getShareColumn1());
                    bpmExDWH.setShareColumn2(bpmExMDetailWHVo.getShareColumn2());
                    bpmExDWH.setShareColumn3(bpmExMDetailWHVo.getShareColumn3());
                    bpmExDWH.setShareColumn4(bpmExMDetailWHVo.getShareColumn4());
                    bpmExDWH.setShareColumn5(bpmExMDetailWHVo.getShareColumn5());
                    bpmExDWH.setRemark(bpmExMDetailWHVo.getRemark());

                    bpmExDWHList.add(bpmExDWH);

                    //更改董監事或研發委員的電話,聯絡地址第1筆都是該所得人,只有董監事會有第2筆
                    if(itemWhIndex == 1)
                    {
                        switch (bpmExM.getApplyType())
                        {
                            //董監事
                            case "7":

                                ncccDirectorSupervisorListRepository.updateFields(bpmExMDetailWHVo.getBan(),bpmExMDetailWHVo.getContactPhone(),bpmExMDetailWHVo.getAddress(),bpmExMDetailWHVo.getContactAddress());

                                break;

                            //研發委員
                            case "8":

                                ncccCommitteeListRepository.updateFields(bpmExMDetailWHVo.getBan(),bpmExMDetailWHVo.getContactPhone(),bpmExMDetailWHVo.getAddress(),bpmExMDetailWHVo.getContactAddress());

                                break;
                        }

                    }

                    itemWhIndex++;
                }

                bpmExDWHRepository.saveAll(bpmExDWHList);

            }

            // endregion

            index++;
        }

        // endregion


        // region 代扣繳項目
        List<BpmExMWH> bpmExMWHList = new ArrayList<>();
        int wIndex = 1;
        for (BpmExMWHVo bpmExMWHVo : vo.getBpmExMWHVoList()) {
            BpmExMWH bpmExMWH = new BpmExMWH();
            bpmExMWH.setExNo(exNo);
            bpmExMWH.setExWhNo(String.format("%03d",wIndex));
            bpmExMWH.setName(bpmExMWHVo.getName());
            bpmExMWH.setAccounting(bpmExMWHVo.getAccounting());
            bpmExMWH.setAmount(bpmExMWHVo.getAmount());
            bpmExMWH.setAccountingName(bpmExMWHVo.getAccountingName());
            bpmExMWH.setItemWHText(bpmExMWHVo.getItemWHText());
            bpmExMWH.setPayDate(bpmExMWHVo.getPayDate());
            bpmExMWHList.add(bpmExMWH);

            wIndex++;
        }

        bpmExMWHRepository.saveAll(bpmExMWHList);
        // endregion

        // region 預算動用寫入

        for(BudgetVo.BudgetTranscation budgetTranscation : budgetTranscationList)
        {
            ncccBudgetService.WriteBudgetTranscation(budgetTranscation);
        }

        // endregion

        bpmExMRepository.save(bpmExM);

        DecisionVo decisionVo = new DecisionVo();
        decisionVo.setProcessId(bpmExM.getTaskId());
        decisionVo.setDecision(Decision.SUBMIT);
        flowableService.completeTask(decisionVo);
        return exNo;
    }

    // endregion

    // region 審核

    @Override
    public String decision(DecisionVo vo) {
        boolean flag = flowableService.completeTask(vo);
        if (flag) {

            BpmExM bpmExM = bpmExMRepository.findByTaskId(vo.getProcessId());

            if(bpmExM.getApplyType().equals("2"))
            {
                if(flowableService.isFinish(vo.getProcessId()))
                {
                    finishEx(bpmExM.getExNo());
                }
            }

            // 回到建單人,預算退回
            if(flowableService.checkAtInitiatorTask(vo.getProcessId()))
            {
                returnBudgetTranscation(bpmExM.getExNo());
            }

            // 作廢
            if(vo.getDecision().equals(INVALID))
            {
                bpmExM.setFlowStatus("3");

                bpmExMRepository.save(bpmExM);
            }

            return "簽核成功";
        } else {
            return "簽核失敗";
        }
    }

    @Override
    @Transactional
    public String accountingDecision(ApplicationExpensesDecisionVo vo) {

        BpmExMVo bpmExMVo = vo.getBpmExMVo();
        NcccUserDto user = SecurityUtil.getCurrentUser();
        int updated = bpmExMRepository.updateFields(bpmExMVo.getExNo(),bpmExMVo.getCurrencyRate(),
                bpmExMVo.getPostingDate(), bpmExMVo.getTotal());
        if (updated != 1) {
            throw new RuntimeException("更新失敗，Ex_NO=" + bpmExMVo.getExNo());
        }
        bpmExDWHRepository.deleteByExNo(bpmExMVo.getExNo());
        bpmSplitMRepository.deleteByExNo(bpmExMVo.getExNo());
        bpmTaskItemRepository.deleteByExNo(bpmExMVo.getExNo());
        for (BpmExMDetailVo bpmExMDetailVo : bpmExMVo.getBpmExMDetailVoList()) {
            int updated1 = bpmExMD1Repository.updateFields(bpmExMVo.getExNo(),bpmExMDetailVo.getExItemNo(), bpmExMDetailVo.getItemText(), bpmExMDetailVo.getRemark());
            if (updated1 != 1) {
                throw new RuntimeException(
                        "明細更新失敗，EX_NO=" + bpmExMVo.getExNo() + " EX_ITEM_NO=" + bpmExMDetailVo.getExItemNo());
            }

            // region 多種分攤

            int splitIndex = 1;
            for (BpmExMDetailSplitVo bpmExMDetailSplitVo : bpmExMDetailVo.getBpmExMDetailSplitVoList()) {
                BpmSplitM bpmSplitM = new BpmSplitM();
                bpmSplitM.setExItemNo(bpmExMDetailVo.getExItemNo());
                bpmSplitM.setExNo(bpmExMVo.getExNo());
                bpmSplitM.setExItemSplitNo(String.format("%03d", splitIndex));
                bpmSplitM.setAccounting(bpmExMDetailSplitVo.getAccounting());
                bpmSplitM.setYear(bpmExMDetailSplitVo.getYear());
                bpmSplitM.setOuCode(bpmExMDetailSplitVo.getOuCode());
                bpmSplitM.setUntaxAmount(bpmExMDetailSplitVo.getUntaxAmount());
                bpmSplitM.setRemark(bpmExMDetailSplitVo.getRemark());
                bpmSplitM.setItemText(bpmExMDetailSplitVo.getItemText());
                bpmSplitM.setDescription(bpmExMDetailSplitVo.getDescription());
                bpmSplitM.setAllocationMethod(bpmExMDetailSplitVo.getAllocationMethod());
                bpmSplitM.setCreatedUser(user.getUserId());
                bpmSplitM.setCreatedDate(LocalDate.now());
                BpmSplitM newBpmSplitM = bpmSplitMRepository.save(bpmSplitM);

                //多重分攤
                List<BpmTaskItem> bpmTaskItemList = new ArrayList<>();

                int splitTaskIndex = 1;
                for(BpmExMDetailSplitTaskItemVo taskItemVo : bpmExMDetailSplitVo.getTaskItemVoList())
                {
                    BpmTaskItem bpmTaskItem = new BpmTaskItem();
                    bpmTaskItem.setExItemNo(newBpmSplitM.getExItemNo());
                    bpmTaskItem.setExItemSplitNo(newBpmSplitM.getExItemSplitNo());
                    bpmTaskItem.setExItemSplitTaskNo(String.format("%03d", splitTaskIndex));
                    bpmTaskItem.setExNo(newBpmSplitM.getExNo());
                    bpmTaskItem.setOperateItemCode(taskItemVo.getCode());
                    bpmTaskItem.setUntaxAmount(taskItemVo.getAmount());
                    bpmTaskItem.setOperateRatio(taskItemVo.getRatio());
                    bpmTaskItem.setItemText(taskItemVo.getItemText());
                    bpmTaskItem.setDescription(taskItemVo.getDescription());
                    bpmTaskItem.setRemark(taskItemVo.getRemark());

                    bpmTaskItemList.add(bpmTaskItem);

                    splitTaskIndex++;
                }

                bpmTaskItemRepository.saveAll(bpmTaskItemList);

                splitIndex++;
            }
            // endregion

            //若有所得稅
            if (!bpmExMDetailVo.getBpmExMDetailWHVoList().isEmpty()) {
                List<BpmExDWH> bpmExDWHList = new ArrayList<BpmExDWH>();
                int itemWhIndex = 1;
                for (BpmExMDetailWHVo bpmExMDetailWHVo : bpmExMDetailVo.getBpmExMDetailWHVoList()) {
                    BpmExDWH bpmExDWH = new BpmExDWH();
                    bpmExDWH.setExItemNo(bpmExMDetailVo.getExItemNo());
                    bpmExDWH.setExItemWhNo(String.format("%03d",itemWhIndex));
                    bpmExDWH.setExNo(bpmExMVo.getExNo());
                    bpmExDWH.setBan(bpmExMDetailWHVo.getBan());
                    bpmExDWH.setErrorNote(bpmExMDetailWHVo.getErrorNote());
                    bpmExDWH.setCertificateCategory(bpmExMDetailWHVo.getCertificateCategory());
                    bpmExDWH.setGainerName(bpmExMDetailWHVo.getGainerName());
                    bpmExDWH.setGainerType(bpmExMDetailWHVo.getGainerType());
                    bpmExDWH.setHas183Days(bpmExMDetailWHVo.getHas183Days());
                    bpmExDWH.setCountryCode(bpmExMDetailWHVo.getCountryCode());
                    bpmExDWH.setHasPassport(bpmExMDetailWHVo.getHasPassport());
                    bpmExDWH.setPassportNo(bpmExMDetailWHVo.getPassportNo());
                    bpmExDWH.setHasAddress(bpmExMDetailWHVo.getHasAddress());
                    bpmExDWH.setAddress(bpmExMDetailWHVo.getAddress());
                    bpmExDWH.setContactAddress(bpmExMDetailWHVo.getContactAddress());
                    bpmExDWH.setContactPhone(bpmExMDetailWHVo.getContactPhone());
                    bpmExDWH.setPaymentYear(bpmExMDetailWHVo.getPaymentYear());
                    bpmExDWH.setPaymentMonthSt(bpmExMDetailWHVo.getPaymentMonthSt());
                    bpmExDWH.setPaymentMonthEd(bpmExMDetailWHVo.getPaymentMonthEd());
                    bpmExDWH.setIncomeNote(bpmExMDetailWHVo.getIncomeNote());
                    bpmExDWH.setIncomeType(bpmExMDetailWHVo.getIncomeType());
                    bpmExDWH.setGrossPayment(bpmExMDetailWHVo.getGrossPayment());
                    bpmExDWH.setWithholdingTaxRate(bpmExMDetailWHVo.getWithholdingTaxRate());
                    bpmExDWH.setWithholdingTax(bpmExMDetailWHVo.getWithholdingTax());
                    bpmExDWH.setNhRate(bpmExMDetailWHVo.getNhRate());
                    bpmExDWH.setRevenueCategory(bpmExMDetailWHVo.getRevenueCategory());
                    bpmExDWH.setNhWithHolding(bpmExMDetailWHVo.getNhWithHolding());
                    bpmExDWH.setNetPayment(bpmExMDetailWHVo.getNetPayment());
                    bpmExDWH.setPaymentDate(bpmExMDetailWHVo.getPaymentDate());
                    bpmExDWH.setSoftwareNote(bpmExMDetailWHVo.getSoftwareNote());
                    bpmExDWH.setTaxTreatyCode(bpmExMDetailWHVo.getTaxTreatyCode());
                    bpmExDWH.setTaxCreditFlag(bpmExMDetailWHVo.getTaxCreditFlag());
                    bpmExDWH.setCertificateIssueMethod(bpmExMDetailWHVo.getCertificateIssueMethod());
                    bpmExDWH.setTaxIdentificationNo(bpmExMDetailWHVo.getTaxIdentificationNo());
                    bpmExDWH.setShareColumn1(bpmExMDetailWHVo.getShareColumn1());
                    bpmExDWH.setShareColumn2(bpmExMDetailWHVo.getShareColumn2());
                    bpmExDWH.setShareColumn3(bpmExMDetailWHVo.getShareColumn3());
                    bpmExDWH.setShareColumn4(bpmExMDetailWHVo.getShareColumn4());
                    bpmExDWH.setShareColumn5(bpmExMDetailWHVo.getShareColumn5());
                    bpmExDWH.setRemark(bpmExMDetailWHVo.getRemark());

                    bpmExDWHList.add(bpmExDWH);

                    itemWhIndex++;
                }

                bpmExDWHRepository.saveAll(bpmExDWHList);

            }
        }

        // region 代扣繳項目

        bpmExMWHRepository.deleteByExNo(bpmExMVo.getExNo());
        int wIndex = 1;
        List<BpmExMWH> bpmExMWHList = new ArrayList<>();
        for (BpmExMWHVo bpmExMWHVo : bpmExMVo.getBpmExMWHVoList()) {
            BpmExMWH bpmExMWH = new BpmExMWH();
            bpmExMWH.setExNo(bpmExMVo.getExNo());
            bpmExMWH.setExWhNo(String.format("%03d",wIndex));
            bpmExMWH.setName(bpmExMWHVo.getName());
            bpmExMWH.setAccounting(bpmExMWHVo.getAccounting());
            bpmExMWH.setAmount(bpmExMWHVo.getAmount());
            bpmExMWH.setAccountingName(bpmExMWHVo.getAccountingName());
            bpmExMWH.setItemWHText(bpmExMWHVo.getItemWHText());
            bpmExMWH.setPayDate(bpmExMWHVo.getPayDate());
            bpmExMWHList.add(bpmExMWH);
            wIndex++;
        }

        bpmExMWHRepository.saveAll(bpmExMWHList);
        // endregion

        boolean flag = flowableService.completeTask(vo);
        if (flag) {

            BpmExM bpmExM = bpmExMRepository.findByTaskId(vo.getProcessId());

            if(bpmExM.getApplyType().equals("2"))
            {
                if(flowableService.isFinish(vo.getProcessId()))
                {
                    finishEx(bpmExM.getExNo());
                }
            }

            // 回到建單人,預算退回
            if(flowableService.checkAtInitiatorTask(vo.getProcessId()))
            {
                returnBudgetTranscation(bpmExM.getExNo());
            }

            // 作廢
            if(vo.getDecision().equals(INVALID))
            {
                bpmExM.setFlowStatus("3");

                bpmExMRepository.save(bpmExM);
            }

            return "簽核成功";
        } else {
            return "簽核失敗";
        }
    }

    @Override
    @Transactional
    public String accountingDSCDecision(ApplicationExpensesDecisionVo vo) {

        BpmExMVo bpmExMVo = vo.getBpmExMVo();

        BpmExM bpmExM = bpmExMRepository.findById(bpmExMVo.getExNo()).orElseThrow(() -> new RuntimeException("找不到主檔: EX_NO=" + bpmExMVo.getExNo()));
        if (!bpmExM.getApplyType().equals("7") && !bpmExM.getApplyType().equals("8")) {
            throw new RuntimeException("非對應費用申請單: EX_NO=" + bpmExMVo.getExNo());
        }
        bpmExDWHRepository.deleteByExNo(bpmExMVo.getExNo());
        for (BpmExMPplVo detailVo : bpmExMVo.getBpmExMPplVoList())
        {
            //若有所得稅
            if (!detailVo.getBpmExMDetailWHVoList().isEmpty()) {
                List<BpmExDWH> bpmExDWHList = new ArrayList<>();
                int itemWhIndex = 1;
                for (BpmExMDetailWHVo bpmExMDetailWHVo : detailVo.getBpmExMDetailWHVoList()) {
                    BpmExDWH bpmExDWH = new BpmExDWH();
                    bpmExDWH.setExItemWhNo(String.format("%03d",itemWhIndex));
                    bpmExDWH.setExItemNo(detailVo.getExItemNo());
                    bpmExDWH.setExNo(bpmExMVo.getExNo());
                    bpmExDWH.setBan(bpmExMDetailWHVo.getBan());
                    bpmExDWH.setErrorNote(bpmExMDetailWHVo.getErrorNote());
                    bpmExDWH.setCertificateCategory(bpmExMDetailWHVo.getCertificateCategory());
                    bpmExDWH.setGainerName(bpmExMDetailWHVo.getGainerName());
                    bpmExDWH.setGainerType(bpmExMDetailWHVo.getGainerType());
                    bpmExDWH.setHas183Days(bpmExMDetailWHVo.getHas183Days());
                    bpmExDWH.setCountryCode(bpmExMDetailWHVo.getCountryCode());
                    bpmExDWH.setHasPassport(bpmExMDetailWHVo.getHasPassport());
                    bpmExDWH.setPassportNo(bpmExMDetailWHVo.getPassportNo());
                    bpmExDWH.setHasAddress(bpmExMDetailWHVo.getHasAddress());
                    bpmExDWH.setAddress(bpmExMDetailWHVo.getAddress());
                    bpmExDWH.setContactAddress(bpmExMDetailWHVo.getContactAddress());
                    bpmExDWH.setContactPhone(bpmExMDetailWHVo.getContactPhone());
                    bpmExDWH.setPaymentYear(bpmExMDetailWHVo.getPaymentYear());
                    bpmExDWH.setPaymentMonthSt(bpmExMDetailWHVo.getPaymentMonthSt());
                    bpmExDWH.setPaymentMonthEd(bpmExMDetailWHVo.getPaymentMonthEd());
                    bpmExDWH.setIncomeNote(bpmExMDetailWHVo.getIncomeNote());
                    bpmExDWH.setIncomeType(bpmExMDetailWHVo.getIncomeType());
                    bpmExDWH.setGrossPayment(bpmExMDetailWHVo.getGrossPayment());
                    bpmExDWH.setWithholdingTaxRate(bpmExMDetailWHVo.getWithholdingTaxRate());
                    bpmExDWH.setWithholdingTax(bpmExMDetailWHVo.getWithholdingTax());
                    bpmExDWH.setNhRate(bpmExMDetailWHVo.getNhRate());
                    bpmExDWH.setRevenueCategory(bpmExMDetailWHVo.getRevenueCategory());
                    bpmExDWH.setNhWithHolding(bpmExMDetailWHVo.getNhWithHolding());
                    bpmExDWH.setNetPayment(bpmExMDetailWHVo.getNetPayment());
                    bpmExDWH.setPaymentDate(bpmExMDetailWHVo.getPaymentDate());
                    bpmExDWH.setSoftwareNote(bpmExMDetailWHVo.getSoftwareNote());
                    bpmExDWH.setTaxTreatyCode(bpmExMDetailWHVo.getTaxTreatyCode());
                    bpmExDWH.setTaxCreditFlag(bpmExMDetailWHVo.getTaxCreditFlag());
                    bpmExDWH.setCertificateIssueMethod(bpmExMDetailWHVo.getCertificateIssueMethod());
                    bpmExDWH.setTaxIdentificationNo(bpmExMDetailWHVo.getTaxIdentificationNo());
                    bpmExDWH.setShareColumn1(bpmExMDetailWHVo.getShareColumn1());
                    bpmExDWH.setShareColumn2(bpmExMDetailWHVo.getShareColumn2());
                    bpmExDWH.setShareColumn3(bpmExMDetailWHVo.getShareColumn3());
                    bpmExDWH.setShareColumn4(bpmExMDetailWHVo.getShareColumn4());
                    bpmExDWH.setShareColumn5(bpmExMDetailWHVo.getShareColumn5());
                    bpmExDWH.setRemark(bpmExMDetailWHVo.getRemark());

                    bpmExDWHList.add(bpmExDWH);

                    //更改董監事或研發委員的電話,聯絡地址第1筆都是該所得人,只有董監事會有第2筆
                    if(itemWhIndex == 1)
                    {
                        switch (bpmExM.getApplyType())
                        {
                            //董監事
                            case "7":

                                ncccDirectorSupervisorListRepository.updateFields(bpmExMDetailWHVo.getBan(),bpmExMDetailWHVo.getContactPhone(),bpmExMDetailWHVo.getAddress(),bpmExMDetailWHVo.getContactAddress());

                                break;

                            //研發委員
                            case "8":

                                ncccCommitteeListRepository.updateFields(bpmExMDetailWHVo.getBan(),bpmExMDetailWHVo.getContactPhone(),bpmExMDetailWHVo.getAddress(),bpmExMDetailWHVo.getContactAddress());

                                break;
                        }

                    }

                    itemWhIndex++;
                }

                bpmExDWHRepository.saveAll(bpmExDWHList);
            }
        }

        // region 代扣繳項目

        bpmExMWHRepository.deleteByExNo(bpmExMVo.getExNo());

        List<BpmExMWH> bpmExMWHList = new ArrayList<>();

        int wIndex = 1;
        for (BpmExMWHVo bpmExMWHVo : bpmExMVo.getBpmExMWHVoList()) {
            BpmExMWH bpmExMWH = new BpmExMWH();
            bpmExMWH.setExWhNo(String.format("%03d",wIndex));
            bpmExMWH.setExNo(bpmExMVo.getExNo());
            bpmExMWH.setName(bpmExMWHVo.getName());
            bpmExMWH.setAccounting(bpmExMWHVo.getAccounting());
            bpmExMWH.setAmount(bpmExMWHVo.getAmount());
            bpmExMWH.setAccountingName(bpmExMWHVo.getAccountingName());
            bpmExMWH.setItemWHText(bpmExMWHVo.getItemWHText());
            bpmExMWH.setPayDate(bpmExMWHVo.getPayDate());
            bpmExMWHList.add(bpmExMWH);
            wIndex++;
        }

        bpmExMWHRepository.saveAll(bpmExMWHList);
        // endregion

        boolean flag = flowableService.completeTask(vo);

        if (flag) {

            // 回到建單人,預算退回
            if(flowableService.checkAtInitiatorTask(vo.getProcessId()))
            {
                returnBudgetTranscation(bpmExM.getExNo());
            }

            // 作廢
            if(vo.getDecision().equals(INVALID))
            {
                bpmExM.setFlowStatus("3");

                bpmExMRepository.save(bpmExM);
            }

            return "簽核成功";
        } else {
            return "簽核失敗";
        }
    }

    @Override
    public String setNextAssignee(AssignTasksVo assignTasksVo) {
        flowableService.setNextAssignee(assignTasksVo.getProcessId(), assignTasksVo.getNextAssignee());
        return "指派任務完成";
    }

    // endregion

    // region 過審及拋SAP

    /**
     * 完成費用申請單
     *
     * @return
     */
    public boolean finishEx(String exNo) {
        BpmExM bpmExM = bpmExMRepository.findById(exNo).orElseThrow(() -> new RuntimeException("找不到主檔: EX_NO=" + exNo));
        List<BpmExMD1> bpmExMD1List = bpmExMD1Repository.findByExNo(exNo);

        NcccUserDto user = SecurityUtil.getCurrentUser();
        String sourceFile = "";

        switch (bpmExM.getApplyType()) {
            //一般費用申請
            case "1":
                //公用事業費
            case "4":

                // 所得人資料來源為費用申請單
                sourceFile = "5";

                List<String> PrepayNoList = Arrays.asList((bpmExM.getPrepayNo().split(",")));

                //處理預付單

                BigDecimal payAmount = bpmExM.getPayAmount();

                BigDecimal refundAmount = bpmExM.getRefundAmount();

                for (String prepayNo : PrepayNoList) {
                    BpmPrepaidOrder thisBpmPrepaidOrder = bpmPrepaidOrderRepository.findByExNo(prepayNo);

                    // 尚有金額未付
                    if (payAmount.compareTo(BigDecimal.ZERO) == 1) {
                        BigDecimal thisRemainAmount = thisBpmPrepaidOrder.getTotalAmount().subtract(thisBpmPrepaidOrder.getPayAmount());

                        //金額大於等於剩餘金額
                        if (payAmount.compareTo(thisRemainAmount) != -1) {
                            thisBpmPrepaidOrder.setPayAmount(thisBpmPrepaidOrder.getTotalAmount());

                            payAmount = payAmount.subtract(thisRemainAmount);
                        } else //金額小於剩餘金額
                        {
                            BigDecimal thisPayAmount = thisBpmPrepaidOrder.getPayAmount().add(payAmount);

                            thisBpmPrepaidOrder.setPayAmount(thisPayAmount);

                            payAmount = new BigDecimal(0);

                        }

                    }
                    // 尚有歸還金額未解決
                    if (refundAmount.compareTo(BigDecimal.ZERO) == 1) {
                        BigDecimal thisRemainRefundAmount = thisBpmPrepaidOrder.getTotalAmount().subtract(thisBpmPrepaidOrder.getPayAmount());

                        //此單尚有金額
                        if (thisRemainRefundAmount.compareTo(BigDecimal.ZERO) == 1) {
                            //金額大於等於剩餘金額
                            if (refundAmount.compareTo(thisRemainRefundAmount) != -1) {
                                thisBpmPrepaidOrder.setRefundAmount(thisRemainRefundAmount);

                                refundAmount = refundAmount.subtract(thisRemainRefundAmount);

                            } else //金額小於剩餘金額
                            {
                                BigDecimal thisRefundAmount = thisBpmPrepaidOrder.getRefundAmount().add(refundAmount);

                                thisBpmPrepaidOrder.setRefundAmount(thisRefundAmount);

                                refundAmount = new BigDecimal(0);
                            }

                            DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                            LocalDate refundDate = LocalDate.parse(bpmExM.getRefundDate(), DATEFORMATTER);

                            thisBpmPrepaidOrder.setRefundDate(refundDate);
                        }
                    }

                    bpmPrepaidOrderRepository.save(thisBpmPrepaidOrder);

                }

                //尚有簽核流程重複選擇同一張預付單一起跑的問題

                break;

            //零用金申請
            case "2":

                // 所得人資料來源為費用申請單
                sourceFile = "5";

                List<BpmExPcD1> bpmExPcD1List = new ArrayList<>();

                for (BpmExMD1 bpmExMD1 : bpmExMD1List) {
                    BpmExPcD1 bpmExPcD1 = new BpmExPcD1();
                    bpmExPcD1.setExNo(bpmExMD1.getExNo());
                    bpmExPcD1.setExItemNo(bpmExMD1.getExItemNo());
                    bpmExPcD1.setCertificateDate(bpmExMD1.getCertificateDate());
                    bpmExPcD1.setCertificateCode(bpmExMD1.getCertificateCode());
                    bpmExPcD1.setCertificateType(bpmExMD1.getCertificateType());
                    bpmExPcD1.setUniformNum(bpmExMD1.getUniformNum());
                    bpmExPcD1.setApplyAmount(bpmExMD1.getApplyAmount());
                    bpmExPcD1.setUntaxAmount(bpmExMD1.getUntaxAmount());
                    bpmExPcD1.setTax(bpmExMD1.getTax());
                    bpmExPcD1.setTaxCode(bpmExMD1.getTaxCode());
                    bpmExPcD1.setTaxRate(bpmExMD1.getTaxRate());
                    bpmExPcD1.setAccounting(bpmExMD1.getItemCode());
                    bpmExPcD1.setCostType(bpmExMD1.getItemName());
                    bpmExPcD1.setCostCenter(bpmExMD1.getCostCenter());
                    bpmExPcD1.setDescription(bpmExMD1.getDescription());
                    bpmExPcD1.setHasIncomeTax(bpmExMD1.getHasIncomeTax());
                    bpmExPcD1.setDeduction(bpmExMD1.getDeduction());
                    bpmExPcD1.setRemark(bpmExMD1.getRemark());
                    bpmExPcD1.setModifiedDate(LocalDate.now());
                    bpmExPcD1.setFinish("0");
                    bpmExPcD1List.add(bpmExPcD1);
                }

                bpmExPcD1Repository.saveAll(bpmExPcD1List);

                break;

            //零用金撥補表
            case "3":

                // 所得人資料來源為費用申請單
                sourceFile = "5";

                List<BpmExPcD1> bpmExPcD1UpdateList = new ArrayList<>();

                for (BpmExMD1 bpmExMD1 : bpmExMD1List) {
                    BpmExPcD1 bpmExPcD1 = bpmExPcD1Repository.findByExNoAndExItemNo(bpmExMD1.getCopyExNo(),bpmExMD1.getCopyExItemNo());
                    bpmExPcD1.setFinish("1");
                    bpmExPcD1UpdateList.add(bpmExPcD1);
                }

                bpmExPcD1Repository.saveAll(bpmExPcD1UpdateList);

                break;

            //預付借支
            case "5":

                // 所得人資料來源為費用申請單
                sourceFile = "5";

                BpmPrepaidOrder bpmPrepaidOrder = new BpmPrepaidOrder();

                bpmPrepaidOrder.setExNo(exNo);
                bpmPrepaidOrder.setTotalAmount(bpmExM.getPredictTwd());
                bpmPrepaidOrder.setRefundAmount(new BigDecimal(0));
                bpmPrepaidOrder.setPayAmount(new BigDecimal(0));

                bpmPrepaidOrderRepository.save(bpmPrepaidOrder);

                if (bpmExMD1List.size() > 0 && bpmExMD1List.stream().anyMatch(x -> x.getChtCode() != null && !x.getChtCode().isEmpty())) {
                    List<BpmCHTPayment> bpmCHTPaymentList = new ArrayList<>();

                    for (BpmExMD1 bpmExMD1 : bpmExMD1List) {
                        BpmCHTPayment bpmCHTPayment = new BpmCHTPayment();
                        bpmCHTPayment.setExNo(bpmExMD1.getExNo());
                        bpmCHTPayment.setBan(bpmExMD1.getUniformNum());
                        bpmCHTPayment.setPayType(bpmExMD1.getChtCode());
                        bpmCHTPayment.setZeroTaxAmount(bpmExMD1.getZeroTaxAmount());
                        bpmCHTPayment.setApplyAmount(bpmExMD1.getApplyAmount());
                        bpmCHTPayment.setDutyFreeAmount(bpmExMD1.getDutyFreeAmount());
                        bpmCHTPayment.setTaxAmount(bpmExMD1.getTax());
                        bpmCHTPayment.setUntaxAmount(bpmExMD1.getUntaxAmount());
                        bpmCHTPayment.setPhone(bpmExMD1.getDescription());
                        bpmCHTPayment.setCarrierNumber(bpmExMD1.getCarrierNumber());
                        bpmCHTPayment.setNotificationNumber(bpmExMD1.getNotificationNumber());
                        bpmCHTPaymentList.add(bpmCHTPayment);
                    }

                    bpmCHTPaymentRepository.saveAll(bpmCHTPaymentList);
                }
                break;

            // 沒收卡獎金
            case "6":

                // 所得人資料來源為沒收卡獎金
                sourceFile = "4";

                break;

            // 董監事
            case "7":

                // 所得人資料來源為董監事
                sourceFile = "2";

                break;

            // 研發委員
            case "8":

                // 所得人資料來源為研發委員
                sourceFile = "3";

                break;
        }

        // region 所得人相關處理

        List<NcccIncomeTaxDetail> ncccIncomeTaxDetailList = new ArrayList<>();

        List<BpmExDWH> bpmExDWHList = bpmExDWHRepository.findAllByExNo(exNo);

        for (BpmExDWH bpmExDWH : bpmExDWHList) {
            NcccIncomePerson ncccIncomePerson = ncccIncomePersonRepository.findByIdAndName(bpmExDWH.getBan(), bpmExDWH.getGainerName());

            // 沒有此所得人,新增所得人
            if (ncccIncomePerson == null) {
                NcccIncomePerson newIncomePerson = new NcccIncomePerson();
                newIncomePerson.setId(bpmExDWH.getBan());
                newIncomePerson.setName(bpmExDWH.getGainerName());
                newIncomePerson.setIncomeCategory(bpmExDWH.getGainerType());
                newIncomePerson.setResidence(bpmExDWH.getHasAddress());
                newIncomePerson.setErrorNote(bpmExDWH.getErrorNote());
                newIncomePerson.setPhone1("");
                newIncomePerson.setPhone2("");
                newIncomePerson.setContactPhone(bpmExDWH.getContactPhone());
                newIncomePerson.setMobile("");
                newIncomePerson.setEmail("");
                newIncomePerson.setContactName(bpmExDWH.getGainerName());
                newIncomePerson.setContactEmail("");
                newIncomePerson.setAddress(bpmExDWH.getAddress());
                newIncomePerson.setContactAddress(bpmExDWH.getContactAddress());
                newIncomePerson.setRemark(bpmExDWH.getRemark());
                newIncomePerson.setSourceFile(sourceFile);
                newIncomePerson.setUpdateUser(user.getUserId());
                ncccIncomePerson = ncccIncomePersonRepository.saveAndFlush(newIncomePerson);
            }

            // region 申報資料

            NcccIncomeTaxDetail ncccIncomeTaxDetail = new NcccIncomeTaxDetail();
            ncccIncomeTaxDetail.setPkMId(ncccIncomePerson.getPkId());
            ncccIncomeTaxDetail.setPaymentYear(bpmExDWH.getPaymentYear());
            ncccIncomeTaxDetail.setPaymentMonthSt(bpmExDWH.getPaymentMonthSt());
            ncccIncomeTaxDetail.setPaymentMonthEd(bpmExDWH.getPaymentMonthEd());
            ncccIncomeTaxDetail.setSoftwareNote(bpmExDWH.getSoftwareNote());
            ncccIncomeTaxDetail.setIncomeType(bpmExDWH.getIncomeType());
            ncccIncomeTaxDetail.setIncomeNote(bpmExDWH.getIncomeNote());
            ncccIncomeTaxDetail.setGrossPayment(bpmExDWH.getGrossPayment());
            ncccIncomeTaxDetail.setWithholdingTaxRate(bpmExDWH.getWithholdingTaxRate());
            ncccIncomeTaxDetail.setWithholdingTax(bpmExDWH.getWithholdingTax());
            ncccIncomeTaxDetail.setNhRate(bpmExDWH.getNhRate());
            ncccIncomeTaxDetail.setNhWithHolding(bpmExDWH.getNhWithHolding());
            ncccIncomeTaxDetail.setRevenueCategory(bpmExDWH.getRevenueCategory());
            ncccIncomeTaxDetail.setNetPayment(bpmExDWH.getNetPayment());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (bpmExDWH.getPaymentDate() != null && !bpmExDWH.getPaymentDate().isEmpty()) {
                ncccIncomeTaxDetail.setPaymentDate(LocalDate.parse(bpmExDWH.getPaymentDate(), formatter));
            }
            ncccIncomeTaxDetail.setTaxTreatyCode(bpmExDWH.getTaxTreatyCode());
            ncccIncomeTaxDetail.setTaxCreditFlag(bpmExDWH.getTaxCreditFlag());
            ncccIncomeTaxDetail.setCertificateIssueMethod(bpmExDWH.getCertificateIssueMethod());
            ncccIncomeTaxDetail.setTaxIdentificationNo(bpmExDWH.getTaxIdentificationNo());
            ncccIncomeTaxDetail.setCountryCode(bpmExDWH.getCountryCode());
            ncccIncomeTaxDetail.setHas183Days(bpmExDWH.getHas183Days());
            ncccIncomeTaxDetail.setShareColumn1(bpmExDWH.getShareColumn1());
            ncccIncomeTaxDetail.setShareColumn2(bpmExDWH.getShareColumn2());
            ncccIncomeTaxDetail.setShareColumn3(bpmExDWH.getShareColumn3());
            ncccIncomeTaxDetail.setShareColumn4(bpmExDWH.getShareColumn4());
            ncccIncomeTaxDetail.setShareColumn5(bpmExDWH.getShareColumn5());
            ncccIncomeTaxDetail.setRemark(bpmExDWH.getRemark());
            ncccIncomeTaxDetail.setBtpOrderNo(exNo);
            //sap會計文件號碼(代處理)
            ncccIncomeTaxDetail.setSapDocNo("");
            ncccIncomeTaxDetail.setSourceFile(sourceFile);
            ncccIncomeTaxDetailList.add(ncccIncomeTaxDetail);
            // endregion

        }

        ncccIncomeTaxDetailRepository.saveAll(ncccIncomeTaxDetailList);

        // endregion

        // region 費用申請單狀態修正

        bpmExM.setFlowStatus("2");

        bpmExMRepository.save(bpmExM);

        // endregion

        return true;
    }

    /**
     * 將費用申請單拋轉至RFC
     */
    @Override
    public String toSAP(String exNo) {
        try {
            BpmExM bpmExM = bpmExMRepository.findById(exNo).orElseThrow(() -> new RuntimeException("找不到主檔: EX_NO=" + exNo));
            List<BpmExMD1> bpmExMD1List = bpmExMD1Repository.findByExNo(exNo);
            List<BpmExPsD> bpmExPsDList = bpmExPsDRepository.findByExNo(exNo);

            // SAP等完成與否
            boolean TaskFinish = false;

            //零用金不需要拋轉
            if (bpmExM.getApplyType().equals("2")) {
                TaskFinish = true;
            } else {
                //region 表頭部分

                ZHeader header = new ZHeader();
                // 來源系統
                header.setZSYSTEM("BTP");
                // 公司代碼
                header.setBUKRS("1010");
                // 文件日期
                header.setBLDAT(bpmExM.getPostingDate().replace("-", ""));
                // 過帳日期
                header.setBUDAT(bpmExM.getPostingDate().replace("-", ""));

                /**
                 * 文件類型: 一般費用申請單、零用金申請單、零用金撥補表、公用事業費、預付借支
                 * 1: 一般費用申請單
                 * 2: 零用金申請單
                 * 3: 零用金撥補表
                 * 4: 公用事業費
                 * 5: 預付借支
                 */
                String docType = bpmExM.getApplyType();
                boolean isTransfer = false;
                if (bpmExM.getPayWayMethod() != null) {
                    isTransfer = bpmExM.getPayWayMethod().equals("transfer");
                }

                /**
                 * 零用金撥補表: KR(供應商發票)
                 * 一般費用申請單、差旅費、臨櫃繳款: KR(供應商發票)
                 * 預付單: KS(預付借支)
                 * 轉帳代扣: XX(轉帳代扣文件)
                 */
                if (docType.equals("3")) {
                    docType = "KR"; // 零用金撥補表
                } else if (docType.equals("5")) {
                    docType = "KS"; // 預付借支
                } else if (docType.equals("1") || docType.equals("4") || docType.equals("6") || docType.equals("7") || docType.equals("8")) {
                    if (isTransfer) {
                        docType = "XX"; // 轉帳代扣文件
                    } else {
                        docType = "KR"; // 一般費用申請單、差旅費、臨櫃繳款
                    }
                } else {
                    throw new RuntimeException("未知的費用申請單類型: " + docType);
                }
                header.setBLART(docType);
                // 幣別
                header.setWAERS(bpmExM.getTradeCurrency());
                // 匯率(交易幣別≠TWD，帶入傳票匯率)
                if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                    header.setKURSF(bpmExM.getCurrencyRate().toString());
                }
                // 參考("當有一筆憑證種類為憑證後補時，固定填入: "憑證後補日:"其餘留空)
                if (bpmExMD1List.stream().filter(detail -> detail.getCertificateType() != null && detail.getCertificateType().equals("憑證後補")).count() > 0) {
                    header.setXBLNR("憑證後補日:");
                }
                // 文件表頭內文(公文文號)
                header.setBKTXT(bpmExM.getApprovalNo());
                // 文件表頭的參考碼 1 內部(BTP 的申請單號)
                header.setXREF1_HD(bpmExM.getExNo());

                //endregion

                //region 明細部分
                ArrayList<ZItem> zitemList = new ArrayList<ZItem>();
                zitemList.addAll(getSAPDetails(bpmExM, bpmExMD1List, bpmExPsDList));

                //endregion

                //region GUI處理
                List<TInput> tInputList = buildTInputListByBpmExD(bpmExMD1List);

                //endregion
                Map<String, String> result;

                //檢查傳票
                try
                {
                    result = sapUtil.callZAccfTwguiCheck01(tInputList);

                    if(result.get("E_ERROR") == null || result.get("E_ERROR") == "")
                    {
                        //region 拋轉至SAP
                        ZReturn zreturn = sapUtil.callZcreateAccDocument(header, zitemList);
                        SapBpmExStatus sapBpmExStatus = new SapBpmExStatus();
                        sapBpmExStatus.setExNo(exNo);
                        sapBpmExStatus.setType(zreturn.getTYPE());
                        sapBpmExStatus.setBukrs(zreturn.getBUKRS());
                        sapBpmExStatus.setBelnr(zreturn.getBELNR());
                        sapBpmExStatus.setGjahr(zreturn.getGJAHR());
                        sapBpmExStatus.setMessage(zreturn.getMESSAGE());
                        sapBpmExStatusRepository.save(sapBpmExStatus);

                        if (zreturn.getTYPE().equals("S")) {
                            sapUtil.callZAccfTwguiInsert01(sapBpmExStatus.getBukrs(), sapBpmExStatus.getBelnr(),
                                    sapBpmExStatus.getGjahr(), tInputList);
                            TaskFinish = true;
                            //結束流程
                            DecisionVo decisionVo = new DecisionVo();
                            decisionVo.setProcessId(bpmExM.getTaskId());
                            decisionVo.setDecision(Decision.END);
                            flowableService.completeTask(decisionVo);

                        } else {
                            return ("費用申請單拋轉至SAP失敗" + sapBpmExStatus.getMessage());
                        }
                        //endregion
                    }
                    else
                    {
                        SapBpmExStatus sapBpmExStatus = new SapBpmExStatus();
                        sapBpmExStatus.setExNo(exNo);
                        sapBpmExStatus.setType("E");
                        sapBpmExStatus.setMessage(result.get("E_XBLNR") + result.get("E_MSG"));
                        sapBpmExStatusRepository.save(sapBpmExStatus);
                        return "拋轉失敗，" + sapBpmExStatus.getMessage();
                    }
                }
                catch (Exception e)
                {
                    return ("費用申請單拋轉至SAP失敗" + e);
                }

            }

            //若SAP等確定處理完畢

            if (TaskFinish) {

                // 結束流程
                DecisionVo decisionVo = new DecisionVo();
                decisionVo.setProcessId(bpmExM.getTaskId());
                decisionVo.setDecision(Decision.END);
                flowableService.completeTask(decisionVo);

                finishEx(bpmExM.getExNo());
            }

            return "拋轉成功";

        } catch (Exception e) {
            return ("費用申請單拋轉至SAP失敗" + e);
        }
    }


    /**
     * 取得應付帳款明細
     *
     * @param bpmExM
     * @param bpmExMD1List
     * @return
     */
    private List<ZItem> getSAPDetails(BpmExM bpmExM, List<BpmExMD1> bpmExMD1List, List<BpmExPsD> bpmExPsDList) {
        List<ZItem> zitemList = new ArrayList<ZItem>();

        switch (bpmExM.getApplyType()) {
            case "6":
            case "1":

                //region 貸方

                //若有預付單號,走預付單邏輯
                if (bpmExM.getPrepayNo() != null) {

                    List<String> PrepayNoList = Arrays.asList((bpmExM.getPrepayNo().split(",")));

                    BigDecimal payAmount = bpmExM.getPayAmount();

                    BigDecimal refundAmount = bpmExM.getRefundAmount();

                    //region 實際支付金額會科

                    ZItem payAmountZitem = new ZItem();
                    // 過帳碼
                    payAmountZitem.setBSCHL("50");
                    // 科目
                    payAmountZitem.setNEWKO(bpmExM.getEmpNo());
                    // 總帳科目
                    payAmountZitem.setHKONT("11110306");
                    // 文件幣別金額
                    payAmountZitem.setWRBTR(payAmount.toString());
                    // 本國貨幣金額
                    // 文件非TWD才需填，換算台幣金額
                    // 以傳票匯率*文件幣別金額呈現
                    if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                        payAmountZitem.setDMBTR((payAmount.multiply(bpmExM.getCurrencyRate())).toString());
                    }
                    // 稅碼
                    payAmountZitem.setMWSKZ(null);
                    // 稅基
                    payAmountZitem.setTXBFW(null);
                    // 到期日
                    payAmountZitem.setZFBDT(null);
                    // 成本中心
                    payAmountZitem.setKOSTL(null);
                    // 指派
                    payAmountZitem.setZUONR(PrepayNoList.get(0));
                    // 內文

                    String prepayNoItemText = "";

                    List<BpmExMD1> thisPrepayNoBpmExMD1List = bpmExMD1Repository.findByExNo(PrepayNoList.get(0));

                    if (thisPrepayNoBpmExMD1List.size() > 0) {
                        prepayNoItemText = thisPrepayNoBpmExMD1List.get(0).getItemText();
                    }

                    payAmountZitem.setSGTXT("沖" + prepayNoItemText);
                    // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                    payAmountZitem.setXREF1(null);

                    zitemList.add(payAmountZitem);

                    //endregion

                    //region 返還金額會科

                    ZItem refundAmountZitem = new ZItem();
                    // 過帳碼
                    refundAmountZitem.setBSCHL("40");
                    // 科目
                    refundAmountZitem.setNEWKO("11010203");
                    // 總帳科目
                    refundAmountZitem.setHKONT(null);
                    // 文件幣別金額
                    refundAmountZitem.setWRBTR(refundAmount.toString());
                    // 本國貨幣金額
                    // 文件非TWD才需填，換算台幣金額
                    // 以傳票匯率*文件幣別金額呈現
                    if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                        refundAmountZitem.setDMBTR((refundAmount.multiply(bpmExM.getCurrencyRate())).toString());
                    }
                    // 稅碼
                    refundAmountZitem.setMWSKZ(null);
                    // 稅基
                    refundAmountZitem.setTXBFW(null);
                    // 到期日
                    refundAmountZitem.setZFBDT(null);
                    // 成本中心
                    refundAmountZitem.setKOSTL(null);
                    // 指派
                    refundAmountZitem.setZUONR(PrepayNoList.get(0));
                    // 內文
                    refundAmountZitem.setSGTXT("沖" + prepayNoItemText);
                    // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                    refundAmountZitem.setXREF1(null);

                    zitemList.add(refundAmountZitem);

                    //endregion
                } else {
                    BigDecimal total = bpmExM.getTotal();

                    // region 代扣繳

                    List<BpmExMWH> bpmExMWHList = bpmExMWHRepository.findAllByExNo(bpmExM.getEmpNo());

                    for (BpmExMWH bpmExMWH : bpmExMWHList) {
                        ZItem withHoldingZitem = new ZItem();
                        // 過帳碼
                        withHoldingZitem.setBSCHL("50");
                        // 科目
                        withHoldingZitem.setNEWKO(bpmExMWH.getAccounting());
                        // 總帳科目
                        withHoldingZitem.setHKONT(null);
                        // 文件幣別金額
                        withHoldingZitem.setWRBTR(bpmExMWH.getAmount().toString());
                        // 本國貨幣金額
                        // 文件非TWD才需填，換算台幣金額
                        // 以傳票匯率*文件幣別金額呈現
                        if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                            withHoldingZitem.setDMBTR((bpmExMWH.getAmount().multiply(bpmExM.getCurrencyRate())).toString());
                        }
                        // 稅碼
                        withHoldingZitem.setMWSKZ(null);
                        // 稅基
                        withHoldingZitem.setTXBFW(null);
                        // 到期日
                        withHoldingZitem.setZFBDT(bpmExMWH.getPayDate().replace("-", ""));
                        // 成本中心
                        withHoldingZitem.setKOSTL(null);
                        // 指派
                        withHoldingZitem.setZUONR(null);
                        // 內文
                        withHoldingZitem.setSGTXT(bpmExMWH.getName());
                        // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                        withHoldingZitem.setXREF1(null);

                        zitemList.add(withHoldingZitem);

                        total = total.subtract(bpmExMWH.getAmount());
                    }

                    // endregion

                    if (bpmExM.getPayWayMethod() != null && (bpmExM.getPayWayMethod().equals("transfer") || bpmExM.getPayWayMethod().equals("counter"))) {
                        //region 若為銀行存款項目,代入銀行存款會計

                        ZItem bankAccountZitem = new ZItem();
                        // 過帳碼
                        bankAccountZitem.setBSCHL("40");
                        // 科目
                        bankAccountZitem.setNEWKO(bpmExM.getBankAccount());
                        // 總帳科目
                        bankAccountZitem.setHKONT(null);
                        // 文件幣別金額
                        bankAccountZitem.setWRBTR(total.toString());
                        // 本國貨幣金額
                        // 文件非TWD才需填，換算台幣金額
                        // 以傳票匯率*文件幣別金額呈現
                        if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                            bankAccountZitem.setDMBTR((total.multiply(bpmExM.getCurrencyRate())).toString());
                        }
                        // 稅碼
                        bankAccountZitem.setMWSKZ(null);
                        // 稅基
                        bankAccountZitem.setTXBFW(null);
                        // 到期日
                        bankAccountZitem.setZFBDT(null);
                        // 成本中心
                        bankAccountZitem.setKOSTL(null);
                        // 指派
                        bankAccountZitem.setZUONR(null);
                        // 內文
                        bankAccountZitem.setSGTXT(bpmExMD1List.get(0).getItemText());
                        // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                        bankAccountZitem.setXREF1(null);

                        zitemList.add(bankAccountZitem);

                        //endregion
                    } else {
                        // region 直接帶入受款人

                        ZItem payEmpNoZitem = new ZItem();
                        // 過帳碼
                        payEmpNoZitem.setBSCHL("31");
                        // 科目
                        payEmpNoZitem.setNEWKO(bpmExM.getPayEmpNo());
                        // 總帳科目
                        payEmpNoZitem.setHKONT(null);
                        // 文件幣別金額
                        payEmpNoZitem.setWRBTR(total.toString());
                        // 本國貨幣金額
                        // 文件非TWD才需填，換算台幣金額
                        // 以傳票匯率*文件幣別金額呈現
                        if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                            payEmpNoZitem.setDMBTR((total.multiply(bpmExM.getCurrencyRate())).toString());
                        }
                        // 稅碼
                        payEmpNoZitem.setMWSKZ(null);
                        // 稅基
                        payEmpNoZitem.setTXBFW(null);
                        // 到期日
                        payEmpNoZitem.setZFBDT(bpmExM.getSpecialPayDate().replace("-", ""));
                        // 成本中心
                        payEmpNoZitem.setKOSTL(null);
                        // 指派
                        payEmpNoZitem.setZUONR(null);
                        // 內文
                        payEmpNoZitem.setSGTXT(bpmExMD1List.get(0).getItemText());
                        // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                        payEmpNoZitem.setXREF1(null);

                        zitemList.add(payEmpNoZitem);

                        // endregion
                    }
                }

                //endregion

                //region 借方

                Map<String, BigDecimal> taxCodeTotalList = new HashMap<>();

                Map<String, BigDecimal> taxCodeTaxList = new HashMap<>();

                for (BpmExMD1 bpmExMD1 : bpmExMD1List) {
                    ZItem thisDetailZitem = new ZItem();
                    // 過帳碼
                    thisDetailZitem.setBSCHL("40");
                    // 科目
                    thisDetailZitem.setNEWKO(bpmExMD1.getItemCode().substring(0, bpmExMD1.getItemCode().length() - 2));
                    // 總帳科目
                    thisDetailZitem.setHKONT(null);
                    // 文件幣別金額
                    thisDetailZitem.setWRBTR(bpmExMD1.getUntaxAmount().toString());
                    // 本國貨幣金額
                    // 文件非TWD才需填，換算台幣金額
                    // 以傳票匯率*文件幣別金額呈現
                    if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                        thisDetailZitem.setDMBTR((bpmExMD1.getUntaxAmount().multiply(bpmExM.getCurrencyRate())).toString());
                    }
                    // 稅碼
                    thisDetailZitem.setMWSKZ(bpmExMD1.getTaxCode());
                    // 稅基
                    thisDetailZitem.setTXBFW(null);
                    // 到期日
                    thisDetailZitem.setZFBDT(null);
                    // 成本中心
                    thisDetailZitem.setKOSTL(bpmExMD1.getCostCenter());
                    // 指派
                    thisDetailZitem.setZUONR(null);
                    // 內文
                    thisDetailZitem.setSGTXT(bpmExMD1.getItemText());
                    // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                    thisDetailZitem.setXREF1(null);

                    zitemList.add(thisDetailZitem);

                    //region 進項稅額分類

                    //有稅都變進項稅額
                    if (bpmExMD1.getTax().compareTo(BigDecimal.ZERO) == 1) {
                        if (taxCodeTotalList.containsKey(bpmExMD1.getTaxCode())) {
                            taxCodeTotalList.put(bpmExMD1.getTaxCode(), taxCodeTotalList.get(bpmExMD1.getTaxCode()).add(bpmExMD1.getUntaxAmount()));

                            taxCodeTaxList.put(bpmExMD1.getTaxCode(), taxCodeTaxList.get(bpmExMD1.getTaxCode()).add(bpmExMD1.getTax()));
                        } else {
                            taxCodeTotalList.put(bpmExMD1.getTaxCode(), bpmExMD1.getUntaxAmount());

                            taxCodeTaxList.put(bpmExMD1.getTaxCode(), bpmExMD1.getTax());
                        }
                    }

                    //endregion
                }

                //region 進項稅額

                for (Map.Entry<String, BigDecimal> taxCodeEntry : taxCodeTotalList.entrySet()) {
                    ZItem taxCodeZitem = new ZItem();
                    // 過帳碼
                    taxCodeZitem.setBSCHL("40");
                    // 科目
                    taxCodeZitem.setNEWKO("11110601");
                    // 總帳科目
                    taxCodeZitem.setHKONT(null);
                    // 文件幣別金額
                    taxCodeZitem.setWRBTR(taxCodeTaxList.get(taxCodeEntry.getKey()).toString());
                    // 本國貨幣金額
                    // 文件非TWD才需填，換算台幣金額
                    // 以傳票匯率*文件幣別金額呈現
                    if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                        taxCodeZitem.setDMBTR(taxCodeTaxList.get(taxCodeEntry.getKey()).toString());
                    }
                    // 稅碼
                    taxCodeZitem.setMWSKZ(taxCodeEntry.getKey());
                    // 稅基
                    taxCodeZitem.setTXBFW(taxCodeEntry.getValue().toString());
                    // 到期日
                    taxCodeZitem.setZFBDT(null);
                    // 成本中心
                    taxCodeZitem.setKOSTL(null);
                    // 指派
                    taxCodeZitem.setZUONR(null);
                    // 內文
                    taxCodeZitem.setSGTXT(bpmExMD1List.get(0).getItemText() + "稅額");
                    // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                    taxCodeZitem.setXREF1(null);

                    zitemList.add(taxCodeZitem);
                }

                //endregion

                //endregion

                break;

            case "3":

                //region 總金額

                ZItem zitem = new ZItem();
                // 過帳碼
                zitem.setBSCHL("50");
                // 科目(固定應付費用-員工)
                zitem.setNEWKO("21050502");
                // 總帳科目
                zitem.setHKONT(null);
                // 文件幣別金額
                zitem.setWRBTR(bpmExM.getTotal().toString());
                // 本國貨幣金額
                // 文件非TWD才需填，換算台幣金額
                // 以傳票匯率*文件幣別金額呈現
                if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                    zitem.setDMBTR(bpmExM.getPredictTwd().toString());
                }
                // 稅碼
                zitem.setMWSKZ(null);
                // 稅基
                zitem.setTXBFW(null);
                // 到期日(指定付款日)
                zitem.setZFBDT(bpmExM.getSpecialPayDate().replace("-", ""));
                // 成本中心
                zitem.setKOSTL(null);
                // 指派
                zitem.setZUONR(null);
                // 內文
                zitem.setSGTXT("零用金撥補");
                // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                zitem.setXREF1(null);

                zitemList.add(zitem);

                //endregion

                //region 零用金撥補明細

                Map<String, BigDecimal> taxCodePettyCashTotalList = new HashMap<>();

                Map<String, BigDecimal> taxCodePettyCashTaxList = new HashMap<>();

                for (BpmExMD1 bpmExMD1 : bpmExMD1List) {
                    ZItem thisDetailZitem = new ZItem();
                    // 過帳碼
                    thisDetailZitem.setBSCHL("40");
                    // 科目
                    thisDetailZitem.setNEWKO(bpmExMD1.getItemCode().substring(0, bpmExMD1.getItemCode().length() - 2));
                    // 總帳科目
                    thisDetailZitem.setHKONT(null);
                    // 文件幣別金額
                    thisDetailZitem.setWRBTR(bpmExMD1.getUntaxAmount().toString());
                    // 本國貨幣金額
                    // 文件非TWD才需填，換算台幣金額
                    // 以傳票匯率*文件幣別金額呈現
                    if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                        thisDetailZitem.setDMBTR((bpmExMD1.getUntaxAmount().multiply(bpmExM.getCurrencyRate())).toString());
                    }
                    // 稅碼
                    thisDetailZitem.setMWSKZ(bpmExMD1.getTaxCode());
                    // 稅基
                    thisDetailZitem.setTXBFW(null);
                    // 到期日
                    thisDetailZitem.setZFBDT(null);
                    // 成本中心
                    thisDetailZitem.setKOSTL(bpmExMD1.getCostCenter());
                    // 指派
                    thisDetailZitem.setZUONR(null);
                    // 內文
                    thisDetailZitem.setSGTXT(bpmExMD1.getItemText());
                    // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                    thisDetailZitem.setXREF1(null);

                    zitemList.add(thisDetailZitem);

                    //region 進項稅額分類

                    //有稅都變進項稅額
                    if (bpmExMD1.getTax().compareTo(BigDecimal.ZERO) == 1) {
                        if (taxCodePettyCashTotalList.containsKey(bpmExMD1.getTaxCode())) {
                            taxCodePettyCashTotalList.put(bpmExMD1.getTaxCode(), taxCodePettyCashTotalList.get(bpmExMD1.getTaxCode()).add(bpmExMD1.getUntaxAmount()));

                            taxCodePettyCashTaxList.put(bpmExMD1.getTaxCode(), taxCodePettyCashTaxList.get(bpmExMD1.getTaxCode()).add(bpmExMD1.getTax()));
                        } else {
                            taxCodePettyCashTotalList.put(bpmExMD1.getTaxCode(), bpmExMD1.getUntaxAmount());

                            taxCodePettyCashTaxList.put(bpmExMD1.getTaxCode(), bpmExMD1.getTax());
                        }
                    }

                    //endregion
                }

                //region 進項稅額

                for (Map.Entry<String, BigDecimal> taxCodeEntry : taxCodePettyCashTotalList.entrySet()) {
                    ZItem taxCodeZitem = new ZItem();
                    // 過帳碼
                    taxCodeZitem.setBSCHL("40");
                    // 科目
                    taxCodeZitem.setNEWKO("11110601");
                    // 總帳科目
                    taxCodeZitem.setHKONT(null);
                    // 文件幣別金額
                    taxCodeZitem.setWRBTR(taxCodePettyCashTaxList.get(taxCodeEntry.getKey()).toString());
                    // 本國貨幣金額
                    // 文件非TWD才需填，換算台幣金額
                    // 以傳票匯率*文件幣別金額呈現
                    if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                        taxCodeZitem.setDMBTR(taxCodePettyCashTaxList.get(taxCodeEntry.getKey()).toString());
                    }
                    // 稅碼
                    taxCodeZitem.setMWSKZ(taxCodeEntry.getKey());
                    // 稅基
                    taxCodeZitem.setTXBFW(taxCodeEntry.getValue().toString());
                    // 到期日
                    taxCodeZitem.setZFBDT(null);
                    // 成本中心
                    taxCodeZitem.setKOSTL(null);
                    // 指派
                    taxCodeZitem.setZUONR(null);
                    // 內文
                    taxCodeZitem.setSGTXT(bpmExMD1List.get(0).getItemText() + "稅額");
                    // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                    taxCodeZitem.setXREF1(null);

                    zitemList.add(taxCodeZitem);
                }

                //endregion

                //endregion

                break;

            case "4":

                //region 中華電信借項

                //根據會計科目稅碼成本中心彙整
                List<ChtAccTaxCoCostDto> chtAccTaxCoCostList = new ArrayList<>();

                BigDecimal chtCost = BigDecimal.ZERO;

                BigDecimal taxCost = BigDecimal.ZERO;

                BigDecimal untaxAmount = BigDecimal.ZERO;

                for (BpmExMD1 bpmExMD1 : bpmExMD1List) {
                    //有稅都變進項稅額
                    if (bpmExMD1.getTax().compareTo(BigDecimal.ZERO) == 1) {
                        taxCost = taxCost.add(bpmExMD1.getTax());
                    }

                    // 中心上限金額
                    if (bpmExMD1.getChtCollectAmount().compareTo(BigDecimal.ZERO) == 1) {
                        chtCost = chtCost.add(bpmExMD1.getChtCollectAmount());
                    }

                    untaxAmount = untaxAmount.add(bpmExMD1.getUntaxAmount().add(bpmExMD1.getChtCollectAmount()));

                    Optional<ChtAccTaxCoCostDto> foundCATCCD = chtAccTaxCoCostList.stream()
                            .filter(x -> x.getAccounting().equals(bpmExMD1.getItemCode()) && x.getCostCenter().equals(bpmExMD1.getCostCenter()))
                            .findFirst();

                    if (foundCATCCD.isPresent()) {
                        ChtAccTaxCoCostDto CATCCD = foundCATCCD.get();
                        CATCCD.setUnTaxAmount(CATCCD.getUnTaxAmount().add(bpmExMD1.getUntaxAmount()));
                        CATCCD.setDutyFreeAmount(CATCCD.getDutyFreeAmount().add(bpmExMD1.getDutyFreeAmount()));
                        CATCCD.setZeroTaxAmount(CATCCD.getZeroTaxAmount().add(bpmExMD1.getZeroTaxAmount()));
                    } else {
                        ChtAccTaxCoCostDto newCATCCD = new ChtAccTaxCoCostDto();
                        newCATCCD.setAccounting(bpmExMD1.getItemCode());
                        newCATCCD.setCostCenter(bpmExMD1.getCostCenter());
                        newCATCCD.setZeroTaxAmount(bpmExMD1.getZeroTaxAmount());
                        newCATCCD.setUnTaxAmount(bpmExMD1.getUntaxAmount());
                        newCATCCD.setDutyFreeAmount(bpmExMD1.getDutyFreeAmount());
                        chtAccTaxCoCostList.add(newCATCCD);
                    }
                }

                //region 成本中心

                ZItem chtDebitZitem = new ZItem();
                // 過帳碼
                chtDebitZitem.setBSCHL("40");
                // 科目
                chtDebitZitem.setNEWKO("21050399");
                // 總帳科目
                chtDebitZitem.setHKONT(null);
                // 文件幣別金額
                chtDebitZitem.setWRBTR(chtCost.toString());
                // 本國貨幣金額
                // 文件非TWD才需填，換算台幣金額
                // 以傳票匯率*文件幣別金額呈現
                if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                    chtDebitZitem.setDMBTR(chtCost.toString());
                }
                // 稅碼
                chtDebitZitem.setMWSKZ(null);
                // 稅基
                chtDebitZitem.setTXBFW(null);
                // 到期日 (指定付款日)
                chtDebitZitem.setZFBDT(null);
                // 成本中心
                chtDebitZitem.setKOSTL(null);
                // 指派
                chtDebitZitem.setZUONR(null);
                // 內文
                chtDebitZitem.setSGTXT("沖代收行動電話費員工自付額");
                // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                chtDebitZitem.setXREF1(null);

                zitemList.add(chtDebitZitem);

                //endregion

                // region 進項稅

                ZItem taxCodeZitem = new ZItem();
                // 過帳碼
                taxCodeZitem.setBSCHL("40");
                // 科目
                taxCodeZitem.setNEWKO("11110601");
                // 總帳科目
                taxCodeZitem.setHKONT(null);
                // 文件幣別金額
                taxCodeZitem.setWRBTR(taxCost.toString());
                // 本國貨幣金額
                // 文件非TWD才需填，換算台幣金額
                // 以傳票匯率*文件幣別金額呈現
                if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                    taxCodeZitem.setDMBTR(taxCost.toString());
                }
                // 稅碼
                taxCodeZitem.setMWSKZ("V3");
                // 稅基
                taxCodeZitem.setTXBFW(taxCost.multiply(new BigDecimal(20)).toString());
                // 到期日
                taxCodeZitem.setZFBDT(null);
                // 成本中心
                taxCodeZitem.setKOSTL(null);
                // 指派
                taxCodeZitem.setZUONR(null);
                // 內文
                taxCodeZitem.setSGTXT(bpmExMD1List.get(0).getItemText() + "稅額");
                // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                taxCodeZitem.setXREF1(null);

                zitemList.add(taxCodeZitem);

                // endregion

                // region 會計科目稅碼成本中心

                for (ChtAccTaxCoCostDto CATCCD : chtAccTaxCoCostList) {

                    // region 未稅金額

                    if(CATCCD.getUnTaxAmount().compareTo(BigDecimal.ZERO) > 0)
                    {
                        ZItem thisDetailZitem = new ZItem();
                        // 過帳碼
                        thisDetailZitem.setBSCHL("40");
                        // 科目
                        thisDetailZitem.setNEWKO(CATCCD.getAccounting().substring(0, CATCCD.getAccounting().length() - 2));
                        // 總帳科目
                        thisDetailZitem.setHKONT(null);
                        // 文件幣別金額
                        thisDetailZitem.setWRBTR(CATCCD.getUnTaxAmount().toString());
                        // 本國貨幣金額
                        // 文件非TWD才需填，換算台幣金額
                        // 以傳票匯率*文件幣別金額呈現
                        if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                            thisDetailZitem.setDMBTR((CATCCD.getUnTaxAmount().multiply(bpmExM.getCurrencyRate())).toString());
                        }
                        // 稅碼
                        thisDetailZitem.setMWSKZ("V3");
                        // 稅基
                        thisDetailZitem.setTXBFW(null);
                        // 到期日
                        thisDetailZitem.setZFBDT(null);
                        // 成本中心
                        thisDetailZitem.setKOSTL(CATCCD.getCostCenter());
                        // 指派
                        thisDetailZitem.setZUONR(null);
                        // 內文
                        thisDetailZitem.setSGTXT(bpmExMD1List.get(0).getItemText());
                        // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                        thisDetailZitem.setXREF1(null);

                        zitemList.add(thisDetailZitem);

                    }

                    // endregion

                    // region 零稅金額

                    if(CATCCD.getZeroTaxAmount().compareTo(BigDecimal.ZERO) > 0)
                    {
                        ZItem thisDetailZitem = new ZItem();
                        // 過帳碼
                        thisDetailZitem.setBSCHL("40");
                        // 科目
                        thisDetailZitem.setNEWKO(CATCCD.getAccounting().substring(0, CATCCD.getAccounting().length() - 2));
                        // 總帳科目
                        thisDetailZitem.setHKONT(null);
                        // 文件幣別金額
                        thisDetailZitem.setWRBTR(CATCCD.getZeroTaxAmount().toString());
                        // 本國貨幣金額
                        // 文件非TWD才需填，換算台幣金額
                        // 以傳票匯率*文件幣別金額呈現
                        if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                            thisDetailZitem.setDMBTR((CATCCD.getZeroTaxAmount().multiply(bpmExM.getCurrencyRate())).toString());
                        }
                        // 稅碼
                        thisDetailZitem.setMWSKZ("V0");
                        // 稅基
                        thisDetailZitem.setTXBFW(null);
                        // 到期日
                        thisDetailZitem.setZFBDT(null);
                        // 成本中心
                        thisDetailZitem.setKOSTL(CATCCD.getCostCenter());
                        // 指派
                        thisDetailZitem.setZUONR(null);
                        // 內文
                        thisDetailZitem.setSGTXT(bpmExMD1List.get(0).getItemText());
                        // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                        thisDetailZitem.setXREF1(null);

                        zitemList.add(thisDetailZitem);

                    }

                    // endregion

                    // region 免稅金額

                    if(CATCCD.getDutyFreeAmount().compareTo(BigDecimal.ZERO) > 0)
                    {
                        ZItem thisDetailZitem = new ZItem();
                        // 過帳碼
                        thisDetailZitem.setBSCHL("40");
                        // 科目
                        thisDetailZitem.setNEWKO(CATCCD.getAccounting().substring(0, CATCCD.getAccounting().length() - 2));
                        // 總帳科目
                        thisDetailZitem.setHKONT(null);
                        // 文件幣別金額
                        thisDetailZitem.setWRBTR(CATCCD.getDutyFreeAmount().toString());
                        // 本國貨幣金額
                        // 文件非TWD才需填，換算台幣金額
                        // 以傳票匯率*文件幣別金額呈現
                        if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                            thisDetailZitem.setDMBTR((CATCCD.getDutyFreeAmount().multiply(bpmExM.getCurrencyRate())).toString());
                        }
                        // 稅碼
                        thisDetailZitem.setMWSKZ("V0");
                        // 稅基
                        thisDetailZitem.setTXBFW(null);
                        // 到期日
                        thisDetailZitem.setZFBDT(null);
                        // 成本中心
                        thisDetailZitem.setKOSTL(CATCCD.getCostCenter());
                        // 指派
                        thisDetailZitem.setZUONR(null);
                        // 內文
                        thisDetailZitem.setSGTXT(bpmExMD1List.get(0).getItemText());
                        // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                        thisDetailZitem.setXREF1(null);

                        zitemList.add(thisDetailZitem);

                    }

                    // endregion

                }

                // endregion

                //endregion

                // region 中華電信貸項

                ZItem chtCreditZitem = new ZItem();
                // 過帳碼
                chtCreditZitem.setBSCHL("31");
                // 科目
                chtCreditZitem.setNEWKO("A81691784");
                // 總帳科目
                chtCreditZitem.setHKONT("11110399");
                // 文件幣別金額
                chtCreditZitem.setWRBTR(bpmExM.getTotal().toString());
                // 本國貨幣金額
                // 文件非TWD才需填，換算台幣金額
                // 以傳票匯率*文件幣別金額呈現
                if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                    chtCreditZitem.setDMBTR(bpmExM.getPredictTwd().toString());
                }
                // 稅碼
                chtCreditZitem.setMWSKZ(null);
                // 稅基
                chtCreditZitem.setTXBFW(null);
                // 到期日 (指定付款日)
                chtCreditZitem.setZFBDT(null);
                // 成本中心
                chtCreditZitem.setKOSTL(null);
                // 指派
                List<String> chtPrepayNos = Arrays.asList((bpmExM.getPrepayNo().split(",")));
                chtCreditZitem.setZUONR(chtPrepayNos.get(0));
                // 內文
                BpmExM prepayNoBpmExM = bpmExMRepository.findByExNo(chtPrepayNos.get(0));

                String thisPostDate = "";

                if(prepayNoBpmExM != null)
                {
                    thisPostDate = prepayNoBpmExM.getPostingDate();
                }

                chtCreditZitem.setSGTXT("沖預付" + thisPostDate + "中華電信電話費");
                // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                chtCreditZitem.setXREF1(null);

                zitemList.add(chtCreditZitem);

                //endregion

                break;

            case "5":

                //若為中華電信預付單
                if (bpmExMD1List.stream().anyMatch(x -> x.getChtCode() != null && !x.getChtCode().isEmpty())) {
                    // region 中華電信預付單

                    ZItem debitZitem = new ZItem();
                    // 過帳碼
                    debitZitem.setBSCHL("21");
                    // 科目
                    debitZitem.setNEWKO("A81691784");
                    // 總帳科目
                    debitZitem.setHKONT("11110399");
                    // 文件幣別金額
                    debitZitem.setWRBTR(bpmExM.getTotal().toString());
                    // 本國貨幣金額
                    // 文件非TWD才需填，換算台幣金額
                    // 以傳票匯率*文件幣別金額呈現
                    if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                        debitZitem.setDMBTR(bpmExM.getPredictTwd().toString());
                    }
                    // 稅碼
                    debitZitem.setMWSKZ(null);
                    // 稅基
                    debitZitem.setTXBFW(null);
                    // 到期日
                    debitZitem.setZFBDT(null);
                    // 成本中心
                    debitZitem.setKOSTL(null);
                    // 指派
                    debitZitem.setZUONR(bpmExM.getExNo());
                    // 內文
                    debitZitem.setSGTXT("預付" + bpmExM.getApplyReason());
                    // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                    debitZitem.setXREF1(null);

                    zitemList.add(debitZitem);

                    // endregion

                    // region 中華電信會計科目

                    ZItem creditZitem = new ZItem();
                    // 過帳碼
                    creditZitem.setBSCHL("50");
                    // 科目
                    creditZitem.setNEWKO("11010201");
                    // 總帳科目
                    creditZitem.setHKONT(null);
                    // 文件幣別金額
                    creditZitem.setWRBTR(bpmExM.getTotal().toString());
                    // 本國貨幣金額
                    // 文件非TWD才需填，換算台幣金額
                    // 以傳票匯率*文件幣別金額呈現
                    if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                        creditZitem.setDMBTR(bpmExM.getPredictTwd().toString());
                    }
                    // 稅碼
                    creditZitem.setMWSKZ(null);
                    // 稅基
                    creditZitem.setTXBFW(null);
                    // 到期日 (指定付款日)
                    creditZitem.setZFBDT(null);
                    // 成本中心
                    creditZitem.setKOSTL(null);
                    // 指派
                    creditZitem.setZUONR(bpmExM.getExNo());
                    // 內文
                    creditZitem.setSGTXT("付" + bpmExM.getApplyReason());
                    // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                    creditZitem.setXREF1(null);

                    zitemList.add(creditZitem);

                    // endregion
                } else {
                    //region 預付員工借支

                    ZItem debitZitem = new ZItem();
                    // 過帳碼
                    debitZitem.setBSCHL("21");
                    // 科目
                    debitZitem.setNEWKO(bpmExM.getPayEmpNo());
                    // 總帳科目
                    debitZitem.setHKONT("11110306");
                    // 文件幣別金額
                    debitZitem.setWRBTR(bpmExM.getTotal().toString());
                    // 本國貨幣金額
                    // 文件非TWD才需填，換算台幣金額
                    // 以傳票匯率*文件幣別金額呈現
                    if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                        debitZitem.setDMBTR(bpmExM.getPredictTwd().toString());
                    }
                    // 稅碼
                    debitZitem.setMWSKZ(null);
                    // 稅基
                    debitZitem.setTXBFW(null);
                    // 到期日 (過帳日期次月1號+3個月)
                    LocalDate next3Month1stDayDate = LocalDate.parse(bpmExM.getSpecialPayDate()).plusMonths(3).withDayOfMonth(1);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    debitZitem.setZFBDT(next3Month1stDayDate.format(formatter));
                    // 成本中心
                    debitZitem.setKOSTL(null);
                    // 指派
                    debitZitem.setZUONR(bpmExM.getExNo());
                    // 內文
                    debitZitem.setSGTXT("預付" + bpmExMD1List.get(0).getItemText() + "(" + bpmExM.getApplicant() + ")");
                    // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                    debitZitem.setXREF1(null);

                    zitemList.add(debitZitem);

                    // endregion

                    //region 應付費用員工

                    ZItem creditZitem = new ZItem();
                    // 過帳碼
                    creditZitem.setBSCHL("31");
                    // 科目
                    creditZitem.setNEWKO(bpmExM.getPayEmpNo());
                    // 總帳科目
                    creditZitem.setHKONT(null);
                    // 文件幣別金額
                    creditZitem.setWRBTR(bpmExM.getTotal().toString());
                    // 本國貨幣金額
                    // 文件非TWD才需填，換算台幣金額
                    // 以傳票匯率*文件幣別金額呈現
                    if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                        creditZitem.setDMBTR(bpmExM.getPredictTwd().toString());
                    }
                    // 稅碼
                    creditZitem.setMWSKZ(null);
                    // 稅基
                    creditZitem.setTXBFW(null);
                    // 到期日 (指定付款日)
                    creditZitem.setZFBDT(bpmExM.getSpecialPayDate().replace("-", ""));
                    // 成本中心
                    creditZitem.setKOSTL(null);
                    // 指派
                    creditZitem.setZUONR(bpmExM.getExNo());
                    // 內文
                    creditZitem.setSGTXT("應付" + bpmExMD1List.get(0).getItemText() + "(" + bpmExM.getApplicant() + ")");
                    // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                    creditZitem.setXREF1(null);

                    zitemList.add(creditZitem);

                    // endregion
                }

                break;

            // 董監事
            case "7":

                //region 貸

                // region 代扣繳

                List<BpmExMWH> bpmExMWHList = bpmExMWHRepository.findAllByExNo(bpmExM.getExNo());

                for (BpmExMWH bpmExMWH : bpmExMWHList) {
                    ZItem withHoldingZitem = new ZItem();
                    // 過帳碼
                    withHoldingZitem.setBSCHL("50");
                    // 科目
                    withHoldingZitem.setNEWKO(bpmExMWH.getAccounting());
                    // 總帳科目
                    withHoldingZitem.setHKONT(null);
                    // 文件幣別金額
                    withHoldingZitem.setWRBTR(bpmExMWH.getAmount().toString());
                    // 本國貨幣金額
                    // 文件非TWD才需填，換算台幣金額
                    // 以傳票匯率*文件幣別金額呈現
                    if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                        withHoldingZitem.setDMBTR((bpmExMWH.getAmount().multiply(bpmExM.getCurrencyRate())).toString());
                    }
                    // 稅碼
                    withHoldingZitem.setMWSKZ(null);
                    // 稅基
                    withHoldingZitem.setTXBFW(null);
                    // 到期日
                    withHoldingZitem.setZFBDT(bpmExMWH.getPayDate().replace("-", ""));
                    // 成本中心
                    withHoldingZitem.setKOSTL(null);
                    // 指派
                    withHoldingZitem.setZUONR(null);
                    // 內文
                    withHoldingZitem.setSGTXT(bpmExMWH.getName());
                    // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                    withHoldingZitem.setXREF1(null);

                    zitemList.add(withHoldingZitem);
                }

                // endregion

                // region 所得人淨額

                List<BpmExDWH> bpmExDWHList = bpmExDWHRepository.findAllByExNo(bpmExM.getExNo());

                List<ZmmtSupplier> zmmtSupplierList = zmmtSupplierRepository.findAll();

                for (BpmExDWH bpmExDWH : bpmExDWHList) {
                    // region 直接帶入所得人

                    ZItem gainerZitem = new ZItem();
                    // 過帳碼
                    gainerZitem.setBSCHL("31");
                    //董監事SAP代碼
                    String pplNewKo = "";

                    Optional<ZmmtSupplier> foundZmmtS = zmmtSupplierList.stream().filter(x -> x.getTaxnum() != null && x.getTaxnum().equals(bpmExDWH.getBan())).findFirst();

                    if (foundZmmtS.isPresent()) {
                        ZmmtSupplier zmmtSupplier = foundZmmtS.get();

                        pplNewKo = zmmtSupplier.getPartner();

                    }

                    // 科目
                    gainerZitem.setNEWKO(pplNewKo);
                    // 總帳科目
                    gainerZitem.setHKONT(null);
                    // 文件幣別金額
                    gainerZitem.setWRBTR(bpmExDWH.getNetPayment().toString());
                    // 本國貨幣金額
                    // 文件非TWD才需填，換算台幣金額
                    // 以傳票匯率*文件幣別金額呈現
                    if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                        gainerZitem.setDMBTR((bpmExDWH.getNetPayment().multiply(bpmExM.getCurrencyRate())).toString());
                    }
                    // 稅碼
                    gainerZitem.setMWSKZ(null);
                    // 稅基
                    gainerZitem.setTXBFW(null);
                    // 到期日
                    gainerZitem.setZFBDT(bpmExM.getSpecialPayDate().replace("-", ""));
                    // 成本中心
                    gainerZitem.setKOSTL(null);
                    // 指派
                    gainerZitem.setZUONR(null);
                    // 內文
                    gainerZitem.setSGTXT(bpmExDWH.getGainerName() + "車馬費及出席費");
                    // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                    gainerZitem.setXREF1(null);

                    zitemList.add(gainerZitem);

                    // endregion
                }

                //endregion

                //endregion

                // region 借

                BigDecimal travelFeesTotalAmount = BigDecimal.ZERO;

                BigDecimal attendFeesTotalAmount = BigDecimal.ZERO;

                String costCenter = "";

                String ouCode = "";

                SyncUser syncUser = syncUserRepository.findByHrid(bpmExM.getEmpNo());

                if(syncUser != null)
                {
                    ouCode = syncUser.getOuCode();

                    NcccCostcenterOrgMapping ncccCostcenterOrgMapping = ncccCostcenterOrgMappingRepository.findByHrDepCodeAct(ouCode);

                    if(ncccCostcenterOrgMapping != null)
                    {
                        costCenter = ncccCostcenterOrgMapping.getCostcenter();
                    }
                }

                for (BpmExPsD bpmExPsD : bpmExPsDList) {
                    travelFeesTotalAmount = travelFeesTotalAmount.add(bpmExPsD.getAmount1()).add(bpmExPsD.getAmount3());

                    attendFeesTotalAmount = attendFeesTotalAmount.add(bpmExPsD.getAmount2()).add(bpmExPsD.getAmount4());
                }

                // region 車馬費

                ZItem travelFeesAmountZitem = new ZItem();
                // 過帳碼
                travelFeesAmountZitem.setBSCHL("40");
                // 科目
                travelFeesAmountZitem.setNEWKO("51032601");
                // 總帳科目
                travelFeesAmountZitem.setHKONT(null);
                // 文件幣別金額
                travelFeesAmountZitem.setWRBTR(travelFeesTotalAmount.toString());
                // 本國貨幣金額
                // 文件非TWD才需填，換算台幣金額
                // 以傳票匯率*文件幣別金額呈現
                if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                    travelFeesAmountZitem.setDMBTR(travelFeesTotalAmount.multiply(bpmExM.getCurrencyRate()).toString());
                }
                // 稅碼
                travelFeesAmountZitem.setMWSKZ(null);
                // 稅基
                travelFeesAmountZitem.setTXBFW(null);
                // 到期日
                travelFeesAmountZitem.setZFBDT(null);
                // 成本中心
                travelFeesAmountZitem.setKOSTL(costCenter);
                // 指派
                travelFeesAmountZitem.setZUONR(null);
                // 內文
                travelFeesAmountZitem.setSGTXT("董監事車馬費");
                // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                travelFeesAmountZitem.setXREF1(null);

                zitemList.add(travelFeesAmountZitem);

                // endregion

                // region 出席費

                ZItem attendFeesAmountZitem = new ZItem();
                // 過帳碼
                attendFeesAmountZitem.setBSCHL("40");
                // 科目
                attendFeesAmountZitem.setNEWKO("51032601");
                // 總帳科目
                attendFeesAmountZitem.setHKONT(null);
                // 文件幣別金額
                attendFeesAmountZitem.setWRBTR(attendFeesTotalAmount.toString());
                // 本國貨幣金額
                // 文件非TWD才需填，換算台幣金額
                // 以傳票匯率*文件幣別金額呈現
                if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                    attendFeesAmountZitem.setDMBTR(attendFeesTotalAmount.multiply(bpmExM.getCurrencyRate()).toString());
                }
                // 稅碼
                attendFeesAmountZitem.setMWSKZ(null);
                // 稅基
                attendFeesAmountZitem.setTXBFW(null);
                // 到期日
                attendFeesAmountZitem.setZFBDT(null);
                // 成本中心
                attendFeesAmountZitem.setKOSTL(costCenter);
                // 指派
                attendFeesAmountZitem.setZUONR(null);
                // 內文
                attendFeesAmountZitem.setSGTXT("董監事出席費");
                // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                attendFeesAmountZitem.setXREF1(null);

                zitemList.add(attendFeesAmountZitem);

                // endregion

                // endregion

                break;
            // 研發委員
            case "8":

                //region 貸

                // region 代扣繳

                List<BpmExMWH> thisbpmExMWHList = bpmExMWHRepository.findAllByExNo(bpmExM.getExNo());

                for (BpmExMWH bpmExMWH : thisbpmExMWHList) {
                    ZItem withHoldingZitem = new ZItem();
                    // 過帳碼
                    withHoldingZitem.setBSCHL("50");
                    // 科目
                    withHoldingZitem.setNEWKO(bpmExMWH.getAccounting());
                    // 總帳科目
                    withHoldingZitem.setHKONT(null);
                    // 文件幣別金額
                    withHoldingZitem.setWRBTR(bpmExMWH.getAmount().toString());
                    // 本國貨幣金額
                    // 文件非TWD才需填，換算台幣金額
                    // 以傳票匯率*文件幣別金額呈現
                    if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                        withHoldingZitem.setDMBTR((bpmExMWH.getAmount().multiply(bpmExM.getCurrencyRate())).toString());
                    }
                    // 稅碼
                    withHoldingZitem.setMWSKZ(null);
                    // 稅基
                    withHoldingZitem.setTXBFW(null);
                    // 到期日
                    withHoldingZitem.setZFBDT(bpmExMWH.getPayDate().replace("-", ""));
                    // 成本中心
                    withHoldingZitem.setKOSTL(null);
                    // 指派
                    withHoldingZitem.setZUONR(null);
                    // 內文
                    withHoldingZitem.setSGTXT(bpmExMWH.getName());
                    // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                    withHoldingZitem.setXREF1(null);

                    zitemList.add(withHoldingZitem);
                }


                // endregion

                // region 所得人淨額

                List<BpmExDWH> thisbpmExDWHList = bpmExDWHRepository.findAllByExNo(bpmExM.getExNo());

                List<ZmmtSupplier> zmmtSupplierTList = zmmtSupplierRepository.findAll();

                for (BpmExDWH bpmExDWH : thisbpmExDWHList) {
                    // region 直接帶入所得人

                    ZItem gainerZitem = new ZItem();
                    // 過帳碼
                    gainerZitem.setBSCHL("31");

                    // 研發委員SAP代碼
                    String pplNewKo = "";

                    Optional<ZmmtSupplier> foundZmmtS = zmmtSupplierTList.stream()
                            .filter(x -> x.getTaxnum() != null && x.getTaxnum().equals(bpmExDWH.getBan()))
                            .findFirst();

                    if (foundZmmtS.isPresent()) {
                        ZmmtSupplier zmmtSupplier = foundZmmtS.get();

                        pplNewKo = zmmtSupplier.getPartner();

                    }

                    // 科目(待確認)
                    gainerZitem.setNEWKO(pplNewKo);
                    // 總帳科目(待確認)
                    gainerZitem.setHKONT(null);
                    // 文件幣別金額
                    gainerZitem.setWRBTR(bpmExDWH.getNetPayment().toString());
                    // 本國貨幣金額
                    // 文件非TWD才需填，換算台幣金額
                    // 以傳票匯率*文件幣別金額呈現
                    if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                        gainerZitem.setDMBTR((bpmExDWH.getNetPayment().multiply(bpmExM.getCurrencyRate())).toString());
                    }
                    // 稅碼
                    gainerZitem.setMWSKZ(null);
                    // 稅基
                    gainerZitem.setTXBFW(null);
                    // 到期日
                    gainerZitem.setZFBDT(bpmExM.getSpecialPayDate().replace("-", ""));
                    // 成本中心
                    gainerZitem.setKOSTL(null);
                    // 指派
                    gainerZitem.setZUONR(null);
                    // 內文
                    gainerZitem.setSGTXT(bpmExDWH.getGainerName() + "研究費,出席費及幹事津貼");
                    // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                    gainerZitem.setXREF1(null);

                    zitemList.add(gainerZitem);

                    // endregion
                }

                //endregion

                //endregion

                //region 借

                BigDecimal researchFeesTotalAmount = BigDecimal.ZERO;

                BigDecimal attendFeeTotalAmount = BigDecimal.ZERO;

                BigDecimal allowanceTotalAmount = BigDecimal.ZERO;

                String thisCostCenter = "";

                String thisOuCode = "";

                SyncUser thisSyncUser = syncUserRepository.findByHrid(bpmExM.getEmpNo());

                if(thisSyncUser != null)
                {
                    thisOuCode = thisSyncUser.getOuCode();

                    NcccCostcenterOrgMapping ncccCostcenterOrgMapping = ncccCostcenterOrgMappingRepository.findByHrDepCodeAct(thisOuCode);

                    if(ncccCostcenterOrgMapping != null)
                    {
                        thisCostCenter = ncccCostcenterOrgMapping.getCostcenter();
                    }
                }

                for (BpmExPsD bpmExPsD : bpmExPsDList) {
                    researchFeesTotalAmount = researchFeesTotalAmount.add(bpmExPsD.getAmount1());

                    attendFeeTotalAmount = attendFeeTotalAmount.add(bpmExPsD.getAmount2());

                    allowanceTotalAmount = allowanceTotalAmount.add(bpmExPsD.getAmount3());
                }

                // region 研究費

                ZItem researchFeesAmountZitem = new ZItem();
                // 過帳碼
                researchFeesAmountZitem.setBSCHL("40");
                // 科目
                researchFeesAmountZitem.setNEWKO("51021801");
                // 總帳科目
                researchFeesAmountZitem.setHKONT(null);
                // 文件幣別金額
                researchFeesAmountZitem.setWRBTR(researchFeesTotalAmount.toString());
                // 本國貨幣金額
                // 文件非TWD才需填，換算台幣金額
                // 以傳票匯率*文件幣別金額呈現
                if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                    researchFeesAmountZitem.setDMBTR(researchFeesTotalAmount.multiply(bpmExM.getCurrencyRate()).toString());
                }
                // 稅碼
                researchFeesAmountZitem.setMWSKZ(null);
                // 稅基
                researchFeesAmountZitem.setTXBFW(null);
                // 到期日
                researchFeesAmountZitem.setZFBDT(null);
                // 成本中心
                researchFeesAmountZitem.setKOSTL(thisCostCenter);
                // 指派
                researchFeesAmountZitem.setZUONR(null);
                // 內文
                researchFeesAmountZitem.setSGTXT("研發委員研究費");
                // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                researchFeesAmountZitem.setXREF1(null);

                zitemList.add(researchFeesAmountZitem);

                // endregion

                // region 出席費

                ZItem attendFeeTotalAmountZitem = new ZItem();
                // 過帳碼
                attendFeeTotalAmountZitem.setBSCHL("40");
                // 科目
                attendFeeTotalAmountZitem.setNEWKO("51021802");
                // 總帳科目
                attendFeeTotalAmountZitem.setHKONT(null);
                // 文件幣別金額
                attendFeeTotalAmountZitem.setWRBTR(attendFeeTotalAmount.toString());
                // 本國貨幣金額
                // 文件非TWD才需填，換算台幣金額
                // 以傳票匯率*文件幣別金額呈現
                if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                    attendFeeTotalAmountZitem.setDMBTR(attendFeeTotalAmount.multiply(bpmExM.getCurrencyRate()).toString());
                }
                // 稅碼
                attendFeeTotalAmountZitem.setMWSKZ(null);
                // 稅基
                attendFeeTotalAmountZitem.setTXBFW(null);
                // 到期日
                attendFeeTotalAmountZitem.setZFBDT(null);
                // 成本中心
                attendFeeTotalAmountZitem.setKOSTL(thisCostCenter);
                // 指派
                attendFeeTotalAmountZitem.setZUONR(null);
                // 內文
                attendFeeTotalAmountZitem.setSGTXT("研發委員出席費");
                // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                attendFeeTotalAmountZitem.setXREF1(null);

                zitemList.add(attendFeeTotalAmountZitem);

                // endregion

                // region 幹事津貼

                ZItem allowanceTotalAmountZitem = new ZItem();
                // 過帳碼
                allowanceTotalAmountZitem.setBSCHL("40");
                // 科目
                allowanceTotalAmountZitem.setNEWKO("51021803");
                // 總帳科目
                allowanceTotalAmountZitem.setHKONT(null);
                // 文件幣別金額
                allowanceTotalAmountZitem.setWRBTR(allowanceTotalAmount.toString());
                // 本國貨幣金額
                // 文件非TWD才需填，換算台幣金額
                // 以傳票匯率*文件幣別金額呈現
                if (bpmExM.getTradeCurrency().equals("TWD") == false) {
                    allowanceTotalAmountZitem.setDMBTR(allowanceTotalAmount.multiply(bpmExM.getCurrencyRate()).toString());
                }
                // 稅碼
                allowanceTotalAmountZitem.setMWSKZ(null);
                // 稅基
                allowanceTotalAmountZitem.setTXBFW(null);
                // 到期日
                allowanceTotalAmountZitem.setZFBDT(null);
                // 成本中心
                allowanceTotalAmountZitem.setKOSTL(thisCostCenter);
                // 指派
                allowanceTotalAmountZitem.setZUONR(null);
                // 內文
                allowanceTotalAmountZitem.setSGTXT("研發委員幹事津貼");
                // 參考碼 1 如BTP表頭有維護匯率，將匯率填入
                allowanceTotalAmountZitem.setXREF1(null);

                zitemList.add(allowanceTotalAmountZitem);

                // endregion

                //endregion

                break;

        }

        return zitemList;
    }


    // endregion

    // region 檢查費用申請單類型
    @Override
    public String getApplicationExpensesType(String exNo)
    {
        BpmExM bpmExM = bpmExMRepository.findById(exNo).orElseThrow(() -> new RuntimeException("找不到主檔: EX_NO=" + exNo));

        return bpmExM.getApplyType();
    }

    // endregion

    // region 按鈕功能

    /**
     * 取得預付單號列表
     *
     * @return
     */
    @Override
    public List<BpmPrepaidOrderVo> getPrepaidOrderList(String empNo) {
        List<BpmPrepaidOrderVo> result = new ArrayList<>();

        List<BpmExM> bpmExMList = bpmExMRepository.findAllByPayEmpNo(empNo);

        for (BpmExM bpmExM : bpmExMList) {
            BpmPrepaidOrder bpmPrepaidOrder = bpmPrepaidOrderRepository.findByExNo(bpmExM.getExNo());

            if (bpmPrepaidOrder != null && (bpmPrepaidOrder.getTotalAmount().compareTo(bpmPrepaidOrder.getRefundAmount().add(bpmPrepaidOrder.getPayAmount())) == 1)) {
                BpmPrepaidOrderVo bpmPrepaidOrderVo = new BpmPrepaidOrderVo();

                bpmPrepaidOrderVo.setExNo(bpmExM.getExNo());
                bpmPrepaidOrderVo.setApplyReason(bpmExM.getApplyReason());
                bpmPrepaidOrderVo.setRefundAmount(bpmPrepaidOrder.getRefundAmount());
                bpmPrepaidOrderVo.setPayAmount(bpmPrepaidOrder.getPayAmount());
                bpmPrepaidOrderVo.setTotalAmount(bpmPrepaidOrder.getTotalAmount());
                bpmPrepaidOrderVo.setRefundDate(bpmPrepaidOrder.getRefundDate());
                result.add(bpmPrepaidOrderVo);
            }
        }
        return result;
    }

    /**
     * 取得零用金撥補對應明晰
     *
     * @return
     */
    @Override
    public List<BpmExMDetailVo> getPettyCashClaimDetails() {
        List<BpmExMDetailVo> result = new ArrayList<>();

        List<BpmExPcD1> bpmExPcD1List = bpmExPcD1Repository.findByFinish("0");

        for (BpmExPcD1 bpmExPcD1 : bpmExPcD1List) {

            BpmExMDetailVo bpmExMDetailVo = new BpmExMDetailVo();
            bpmExMDetailVo.setCopyExNo(bpmExPcD1.getExNo());
            bpmExMDetailVo.setCopyExItemNo(bpmExPcD1.getExItemNo());
            bpmExMDetailVo.setCertificateDate(bpmExPcD1.getCertificateDate());
            bpmExMDetailVo.setCertificateCode(bpmExPcD1.getCertificateCode());
            bpmExMDetailVo.setCertificateType(bpmExPcD1.getCertificateType());
            bpmExMDetailVo.setUniformNum(bpmExPcD1.getUniformNum());
            bpmExMDetailVo.setApplyAmount(bpmExPcD1.getApplyAmount());
            bpmExMDetailVo.setUntaxAmount(bpmExPcD1.getUntaxAmount());
            bpmExMDetailVo.setTax(bpmExPcD1.getTax());
            bpmExMDetailVo.setTaxCode(bpmExPcD1.getTaxCode());
            bpmExMDetailVo.setTaxRate(bpmExPcD1.getTaxRate());
            bpmExMDetailVo.setItemCode(bpmExPcD1.getAccounting());
            bpmExMDetailVo.setItemName(bpmExPcD1.getCostType());
            bpmExMDetailVo.setCostCenter(bpmExPcD1.getCostCenter());
            bpmExMDetailVo.setDescription(bpmExPcD1.getDescription());
            bpmExMDetailVo.setDeduction(bpmExPcD1.getDeduction());
            bpmExMDetailVo.setHasIncomeTax(bpmExPcD1.getHasIncomeTax());
            bpmExMDetailVo.setRemark(bpmExPcD1.getRemark());
            bpmExMDetailVo.setChtCode("");
            bpmExMDetailVo.setNotificationNumber("");
            bpmExMDetailVo.setCarrierNumber("");
            bpmExMDetailVo.setMultiShare("N");
            bpmExMDetailVo.setYear("");
            bpmExMDetailVo.setAccounting("");
            bpmExMDetailVo.setOuCode("");
            result.add(bpmExMDetailVo);
        }

        return (result);
    }

    /**
     * 下載範本檔案
     *
     * @return
     */
    @Override
    public byte[] downloadTemplateFile() {
        try {
            ClassPathResource resource = new ClassPathResource(TEMPLATE_PATH);
            try (InputStream in = resource.getInputStream();
                 ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                byte[] data = new byte[4096];
                int nRead;
                while ((nRead = in.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                return buffer.toByteArray();
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    ;

    /**
     * 上傳憑證檔案
     *
     * @param file
     * @return
     */
    @Override
    public List<BpmExMDetailVo> uploadCertificateData(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("上傳的檔案為空或遺失");
        }

        List<BpmExMDetailVo> detailVoList = new ArrayList<>();
        List<NcccReceiptType> ncccReceiptTypeList = ncccReceiptTypeRepository.findAll();
        List<NcccExpenseCategoryNumber> ncccExpenseCategoryNumberList = ncccExpenseCategoryNumberRepository.findAll();
        List<NcccCostCenter> ncccCostCenterList = ncccCostCenterRepository.findAll();

        // 解析 CSV
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] cells = line.split(",");
                BpmExMDetailVo vo = new BpmExMDetailVo();
                // 根據欄位順序設值
                String certificateDate = cells.length > 0 && DateUtil.isValidDate(cells[0]) ? cells[0] : "";
                vo.setCertificateDate(certificateDate);
                vo.setCertificateCode(cells.length > 1 ? cells[1] : "");
                String certificateType = cells.length > 2 ? cells[2] : "";
                String taxCode = "";
                String deduction = "";
                String hasIncomeTax = "";
                BigDecimal taxRate = new BigDecimal(0);

                String finalCertificateType = certificateType;
                Optional<NcccReceiptType> foundNCCCRT = ncccReceiptTypeList.stream()
                        .filter(x -> x.getIntyp().equals(finalCertificateType))
                        .findFirst();

                if (foundNCCCRT.isPresent()) {
                    NcccReceiptType ncccReceiptType = foundNCCCRT.get();
                    certificateType = ncccReceiptType.getIntyp();
                    taxCode = ncccReceiptType.getMwskz();
                    deduction = ncccReceiptType.getZguifg();
                    hasIncomeTax = ncccReceiptType.getIncometax();
                    taxRate = ncccReceiptType.getTaxrate();

                } else {
                    certificateType = "";
                }
                vo.setCertificateType(certificateType);
                vo.setTaxCode(taxCode);
                vo.setDeduction(deduction);
                vo.setHasIncomeTax(hasIncomeTax);
                vo.setTaxRate(taxRate.toString());

                vo.setUniformNum(cells.length > 3 ? cells[3] : "");
                vo.setApplyAmount(cells.length > 4 ? convertStringToBigDecimal(cells[4]) : new BigDecimal("0"));
                vo.setUntaxAmount(cells.length > 5 ? convertStringToBigDecimal(cells[5]) : new BigDecimal("0"));
                vo.setTax(cells.length > 6 ? convertStringToBigDecimal(cells[6]) : new BigDecimal("0"));
                vo.setZeroTaxAmount(cells.length > 7 ? convertStringToBigDecimal(cells[7]) : new BigDecimal("0"));
                vo.setDutyFreeAmount(cells.length > 8 ? convertStringToBigDecimal(cells[8]) : new BigDecimal("0"));

                String itemCode = cells.length > 9 ? cells[9] : "";
                String itemName = "";

                String finalAccounting = itemCode;
                Optional<NcccExpenseCategoryNumber> foundNCCCEXN = ncccExpenseCategoryNumberList.stream()
                        .filter(x -> x.getCategoryNumber().equals(finalAccounting))
                        .findFirst();

                if (foundNCCCEXN.isPresent()) {
                    NcccExpenseCategoryNumber ncccExpenseCategoryNumber = foundNCCCEXN.get();
                    itemCode = ncccExpenseCategoryNumber.getCategoryNumber();
                    itemName = ncccExpenseCategoryNumber.getCategoryName();
                } else {
                    itemCode = "";
                }
                vo.setItemCode(itemCode);
                vo.setItemName(itemName);

                String costCenter = cells.length > 10 ? cells[10] : "";

                String finalCostCenter = costCenter;
                Optional<NcccCostCenter> foundNCCCCC = ncccCostCenterList.stream()
                        .filter(x -> x.getKtext().equals(finalCostCenter))
                        .findFirst();

                if (foundNCCCCC.isPresent()) {
                    NcccCostCenter ncccCostCenter = foundNCCCCC.get();
                    costCenter = ncccCostCenter.getKostl();
                } else {
                    costCenter = "";
                }

                vo.setCostCenter(costCenter);
                vo.setDescription(cells.length > 11 ? cells[11] : "");
                vo.setCarrierNumber("");
                vo.setNotificationNumber("");
                vo.setItemText("");
                vo.setChtCode("");
                detailVoList.add(vo);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return detailVoList;
    }

    /**
     * 上傳 中華電信繳費檔案
     *
     * @param file
     * @return
     */
    @Override
    public List<BpmExMDetailVo> uploadCHTBill(MultipartFile file, String itemText) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("上傳的檔案為空或遺失");
        }

        String fileName = file.getOriginalFilename();

        String chtCode = "";

        List<BpmExMDetailVo> detailVoList = new ArrayList<>();

        try {

            if (fileName != null && (fileName.toLowerCase().endsWith("b.csv") || fileName.toLowerCase().endsWith("b.xlsx"))) {
                chtCode = "1";
            } else if (fileName != null && (fileName.toLowerCase().endsWith("p.csv") || fileName.toLowerCase().endsWith("p.xlsx"))) {
                chtCode = "2";
            } else {
                throw new RuntimeException("不支援的檔案名稱");
            }

            if (fileName != null && fileName.toLowerCase().endsWith(".csv")) {
                // 解析 CSV
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "Big5"))) {
                    String line;
                    int rowIndex = 1;
                    while ((line = reader.readLine()) != null) {
                        String[] cells = line.split(",");
                        BpmExMDetailVo vo = new BpmExMDetailVo();
                        // 根據欄位順序設值
                        vo.setZeroTaxAmount(cells.length > 1 ? convertStringToBigDecimal(cells[1]) : new BigDecimal("0"));
                        vo.setDutyFreeAmount(cells.length > 2 ? convertStringToBigDecimal(cells[2]) : new BigDecimal("0"));
                        vo.setUntaxAmount(cells.length > 0 ? convertStringToBigDecimal(cells[0]) : new BigDecimal("0"));
                        vo.setTax(cells.length > 5 ? convertStringToBigDecimal(cells[5]) : new BigDecimal("0"));
                        vo.setApplyAmount(cells.length > 6 ? convertStringToBigDecimal(cells[6]) : new BigDecimal("0"));
                        vo.setCarrierNumber(cells.length > 7 ? cells[7] : "");
                        vo.setDescription(cells.length > 8 ? cells[8] : "");
                        vo.setUniformNum(cells.length > 10 ? cells[10] : "");
                        vo.setNotificationNumber(cells.length > 13 ? Long.toString(scientificToLong(cells[13])) : "");
                        vo.setItemText(itemText);
                        vo.setTaxRate("0");
                        vo.setChtCode(chtCode);
                        detailVoList.add(vo);
                        rowIndex++;
                    }
                }
            } else if (fileName != null && fileName.toLowerCase().endsWith(".xlsx")) {
                // 解析 Excel
                try (InputStream is = file.getInputStream();
                     Workbook workbook = new XSSFWorkbook(is)) {
                    Sheet sheet = workbook.getSheetAt(0);
                    for (Row row : sheet) {
                        BpmExMDetailVo detailVo = convertCHTBillRowToData(row, row.getRowNum());
                        detailVo.setChtCode(chtCode);
                        detailVo.setItemText(itemText);
                        detailVo.setTaxRate("0");
                        detailVoList.add(detailVo);
                    }
                }
            } else {
                throw new RuntimeException("不支援的檔案格式");
            }
        } catch (Exception e) {
            LOG.error("解析檔案失敗", e);
        }

        List<NcccExpenseCategoryNumber> ncccExpenseCategoryNumberList = ncccExpenseCategoryNumberRepository.findAll();

        //(自動扣款)
        if (chtCode.equals("1")) {
            List<NcccUtilityBudgetAttributionB> ncccUtilityBudgetAttributionBList = ncccUtilityBudgetAttributionBRepository.findAll();

            for (BpmExMDetailVo detailVo : detailVoList) {

                //取得此電話的對應資料
                Optional<NcccUtilityBudgetAttributionB> foundNCCCUBAB = ncccUtilityBudgetAttributionBList.stream()
                        .filter(x -> x.getPhone().equals(detailVo.getDescription()))
                        .findFirst();

                if (foundNCCCUBAB.isPresent()) {
                    NcccUtilityBudgetAttributionB ncccUBAB = foundNCCCUBAB.get();
                    String itemCode = ""; // 品號
                    String itemName = ""; // 品名
                    Optional<NcccExpenseCategoryNumber> foundNCCCEXN = ncccExpenseCategoryNumberList.stream()
                            .filter(x -> x.getAccounting().equals(ncccUBAB.getAccounting()))
                            .findFirst();
                    if (foundNCCCEXN.isPresent()) {
                        NcccExpenseCategoryNumber ncccExpenseCategoryNumber = foundNCCCEXN.get();
                        itemCode = ncccExpenseCategoryNumber.getCategoryNumber();
                        itemName = ncccExpenseCategoryNumber.getCategoryName();
                    }
                    detailVo.setItemCode(itemCode);
                    detailVo.setItemName(itemName);
                    detailVo.setCostCenter(ncccUBAB.getOuCode());
                    BigDecimal untaxAmount = detailVo.getUntaxAmount();
                    BigDecimal limitAmount = BigDecimal.ZERO;
                    if (ncccUBAB.getLimitAmount() != null) {
                        untaxAmount = untaxAmount.subtract(ncccUBAB.getLimitAmount());

                        limitAmount = ncccUBAB.getLimitAmount();
                    }
                    detailVo.setUntaxAmount(untaxAmount);
                    detailVo.setChtCollectAmount(limitAmount);
                }
            }
        } else if (chtCode.equals("2")) //(臨櫃繳款)
             {
            List<NcccUtilityBudgetAttribution> ncccUtilityBudgetAttributionList = ncccUtilityBudgetAttributionRepository.findAll();

            for (BpmExMDetailVo detailVo : detailVoList) {

                //取得此電話的資料
                Optional<NcccUtilityBudgetAttribution> foundNCCCUBA = ncccUtilityBudgetAttributionList.stream()
                        .filter(x -> x.getPhone().equals(detailVo.getDescription()))
                        .findFirst();

                if (foundNCCCUBA.isPresent()) {
                    NcccUtilityBudgetAttribution ncccUBA = foundNCCCUBA.get();
                    String itemCode = ""; // 品號
                    String itemName = ""; // 品名
                    Optional<NcccExpenseCategoryNumber> foundNCCCEXN = ncccExpenseCategoryNumberList.stream()
                            .filter(x -> x.getAccounting().equals(ncccUBA.getAccounting()))
                            .findFirst();
                    if (foundNCCCEXN.isPresent()) {
                        NcccExpenseCategoryNumber ncccExpenseCategoryNumber = foundNCCCEXN.get();
                        itemCode = ncccExpenseCategoryNumber.getCategoryNumber();
                        itemName = ncccExpenseCategoryNumber.getCategoryName();
                    }
                    detailVo.setItemCode(itemCode);
                    detailVo.setItemName(itemName);
                    detailVo.setCostCenter(ncccUBA.getOuCode());
                    BigDecimal untaxAmount = detailVo.getUntaxAmount();
                    BigDecimal limitAmount = BigDecimal.ZERO;
                    if (ncccUBA.getLimitAmount() != null) {
                        untaxAmount = untaxAmount.subtract(ncccUBA.getLimitAmount());

                        limitAmount = ncccUBA.getLimitAmount();
                    }
                    detailVo.setUntaxAmount(untaxAmount);
                    detailVo.setChtCollectAmount(limitAmount);
                }
            }
        }

        return detailVoList;
    }

    /**
     * 檢查是否為中華電信預付單
     *
     * @param exNo
     * @return
     */
    @Override
    public Boolean checkCHTPrepaidOrder(String exNo) {
        return bpmCHTPaymentRepository.existsByExNo(exNo);
    }

    /**
     * 上傳 中華電信銷帳檔案
     *
     * @param file
     * @return
     */
    @Override
    public List<BpmExMDetailVo> uploadCHTWriteOff(MultipartFile file, String exNo) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("上傳的檔案為空或遺失");
        }

        String fileName = file.getOriginalFilename();

        String chtCode = "";

        List<BpmExMDetailVo> detailVoList = new ArrayList<>();

        try {

            if (fileName != null && (fileName.toLowerCase().endsWith("trans.csv") || fileName.toLowerCase().endsWith("_trans.xlsx"))) {
                chtCode = "1";
            } else if (fileName != null && (fileName.toLowerCase().endsWith("_non-trans.csv") || fileName.toLowerCase().endsWith("_non-trans.xlsx"))) {
                chtCode = "2";
            } else {
                throw new RuntimeException("不支援的檔案名稱");
            }

            if (fileName != null && fileName.toLowerCase().endsWith(".csv")) {
                // 解析 CSV
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "Big5"))) {
                    String line;
                    int rowIndex = 1;
                    while ((line = reader.readLine()) != null) {
                        String[] cells = line.split(",");
                        BpmExMDetailVo vo = new BpmExMDetailVo();
                        // 根據欄位順序設值
                        vo.setCertificateCode(cells.length > 0 ? cells[0] : "");
                        vo.setCertificateDate(cells.length > 1 ? DateUtil.changeRocDateToNormalDate(cells[1]) : "");
                        vo.setCarrierNumber(cells.length > 2 ? cells[2] : "");
                        vo.setNotificationNumber(cells.length > 3 ? cells[3] : "");
                        vo.setChtCode(chtCode);
                        detailVoList.add(vo);
                        rowIndex++;
                    }
                }
            } else if (fileName != null && fileName.toLowerCase().endsWith(".xlsx")) {
                // 解析 Excel
                try (InputStream is = file.getInputStream();
                     Workbook workbook = new XSSFWorkbook(is)) {
                    Sheet sheet = workbook.getSheetAt(0);
                    for (Row row : sheet) {
                        BpmExMDetailVo detailVo = convertCHTWriteOffRowToData(row, row.getRowNum());
                        detailVo.setChtCode(chtCode);
                        detailVoList.add(detailVo);
                    }
                }
            } else {
                throw new RuntimeException("不支援的檔案格式");
            }
        } catch (Exception e) {
            LOG.error("解析檔案失敗", e);
        }

        List<BpmCHTPayment> bpmCHTPaymentList = bpmCHTPaymentRepository.findByExNo(exNo);

        List<NcccExpenseCategoryNumber> ncccExpenseCategoryNumberList = ncccExpenseCategoryNumberRepository.findAll();

        //電信發票參數
        String ChtTaxCertificateType = "";

        String ChtTaxTaxRate = "";

        String ChtTaxTaxCode = "";

        String ChtTaxDeduction = "";

        String ChtTaxHasIncomeTax = "";

        NcccReceiptType chtTaxReceiptType = ncccReceiptTypeRepository.findByIntyp("電子發票(5%)");

        if (chtTaxReceiptType != null) {
            ChtTaxCertificateType = chtTaxReceiptType.getIntyp();

            ChtTaxTaxRate = chtTaxReceiptType.getTaxrate().toString();

            ChtTaxTaxCode = chtTaxReceiptType.getMwskz();

            ChtTaxDeduction = chtTaxReceiptType.getZguifg();

            ChtTaxHasIncomeTax = chtTaxReceiptType.getIncometax();
        }

        //零稅電信發票參數
        String ChtZeroTaxCertificateType = "";

        String ChtZeroTaxTaxRate = "";

        String ChtZeroTaxTaxCode = "";

        String ChtZeroTaxDeduction = "";

        String ChtZeroTaxHasIncomeTax = "";

        NcccReceiptType chtZeroTaxReceiptType = ncccReceiptTypeRepository.findByIntyp("電子發票(零.免稅)");

        if (chtZeroTaxReceiptType != null) {
            ChtZeroTaxCertificateType = chtZeroTaxReceiptType.getIntyp();

            ChtZeroTaxTaxRate = chtZeroTaxReceiptType.getTaxrate().toString();

            ChtZeroTaxTaxCode = chtZeroTaxReceiptType.getMwskz();

            ChtZeroTaxDeduction = chtZeroTaxReceiptType.getZguifg();

            ChtZeroTaxHasIncomeTax = chtZeroTaxReceiptType.getIncometax();
        }

        //部門對應表
        List<NcccCostcenterOrgMapping> ncccCostcenterOrgMappingList = ncccCostcenterOrgMappingRepository.findAll();

        //自動扣款
        if (chtCode.equals("1")) {
            List<NcccUtilityBudgetAttributionB> ncccUtilityBudgetAttributionBList = ncccUtilityBudgetAttributionBRepository.findAll();

            for (BpmExMDetailVo detailVo : detailVoList) {
                Optional<BpmCHTPayment> foundBPMCHTPM = bpmCHTPaymentList.stream()
                        .filter(x -> x.getCarrierNumber().equals(detailVo.getCarrierNumber()) && x.getNotificationNumber().equals(detailVo.getNotificationNumber()))
                        .findFirst();

                if (foundBPMCHTPM.isPresent()) {
                    BpmCHTPayment bpmCHTPayment = foundBPMCHTPM.get();

                    detailVo.setZeroTaxAmount(bpmCHTPayment.getZeroTaxAmount());
                    detailVo.setDutyFreeAmount(bpmCHTPayment.getDutyFreeAmount());
                    detailVo.setUntaxAmount(bpmCHTPayment.getUntaxAmount());
                    detailVo.setTax(bpmCHTPayment.getTaxAmount());
                    detailVo.setApplyAmount(bpmCHTPayment.getApplyAmount());
                    detailVo.setDescription(bpmCHTPayment.getPhone());
                    detailVo.setUniformNum(bpmCHTPayment.getBan());

                    if (detailVo.getTax().compareTo(BigDecimal.ZERO) == 1) {
                        detailVo.setCertificateType(ChtTaxCertificateType);
                        detailVo.setTaxRate(ChtTaxTaxRate);
                        detailVo.setTaxCode(ChtTaxTaxCode);
                        detailVo.setDeduction(ChtTaxDeduction);
                        detailVo.setHasIncomeTax(ChtTaxHasIncomeTax);
                    } else {
                        detailVo.setCertificateType(ChtZeroTaxCertificateType);
                        detailVo.setTaxRate(ChtZeroTaxTaxRate);
                        detailVo.setTaxCode(ChtZeroTaxTaxCode);
                        detailVo.setDeduction(ChtZeroTaxDeduction);
                        detailVo.setHasIncomeTax(ChtZeroTaxHasIncomeTax);
                    }

                    Optional<NcccUtilityBudgetAttributionB> foundNCCCUBAB = ncccUtilityBudgetAttributionBList.stream()
                            .filter(x -> x.getPhone().equals(bpmCHTPayment.getPhone()))
                            .findFirst();

                    if (foundNCCCUBAB.isPresent()) {
                        NcccUtilityBudgetAttributionB ncccUBAB = foundNCCCUBAB.get();
                        String itemCode = ""; // 品號
                        String itemName = ""; // 品名
                        String accounting = ""; // 預算科目
                        Optional<NcccExpenseCategoryNumber> foundNCCCEXN = ncccExpenseCategoryNumberList.stream()
                                .filter(x -> x.getAccounting().equals(ncccUBAB.getAccounting()))
                                .findFirst();
                        if (foundNCCCEXN.isPresent()) {
                            NcccExpenseCategoryNumber ncccExpenseCategoryNumber = foundNCCCEXN.get();
                            itemCode = ncccExpenseCategoryNumber.getCategoryNumber();
                            itemName = ncccExpenseCategoryNumber.getCategoryName();
                            accounting = ncccExpenseCategoryNumber.getAccounting();
                        }

                        detailVo.setItemCode(itemCode);
                        detailVo.setItemName(itemName);
                        detailVo.setAccounting(accounting);
                        detailVo.setCostCenter(ncccUBAB.getOuCode());

                        //根據成本中心取得對應預算部門
                        Optional<NcccCostcenterOrgMapping> foundNCCCCOM = ncccCostcenterOrgMappingList.stream()
                                .filter(x -> x.getCostcenter().equals(detailVo.getCostCenter()))
                                .findFirst();

                        foundNCCCCOM.ifPresent(ncccCostcenterOrgMapping -> detailVo.setOuCode(ncccCostcenterOrgMapping.getBudgetOuCode()));

                        BigDecimal limitAmount = BigDecimal.ZERO;
                        //若有平台金額
                        if (ncccUBAB.getLimitAmount() != null && ncccUBAB.getLimitAmount().compareTo(BigDecimal.ZERO) == 1) {
                            limitAmount = ncccUBAB.getLimitAmount();

                        }

                        detailVo.setChtCollectAmount(limitAmount);
                    }
                }
            }

        } else if (chtCode.equals("2")) // 臨櫃
        {
            List<NcccUtilityBudgetAttribution> ncccUtilityBudgetAttributionList = ncccUtilityBudgetAttributionRepository.findAll();

            for (BpmExMDetailVo detailVo : detailVoList) {
                Optional<BpmCHTPayment> foundBPMCHTPM = bpmCHTPaymentList.stream()
                        .filter(x -> x.getCarrierNumber().equals(detailVo.getCarrierNumber()) && x.getNotificationNumber().equals(detailVo.getNotificationNumber()))
                        .findFirst();

                if (foundBPMCHTPM.isPresent()) {
                    BpmCHTPayment bpmCHTPayment = foundBPMCHTPM.get();

                    detailVo.setZeroTaxAmount(bpmCHTPayment.getZeroTaxAmount());
                    detailVo.setDutyFreeAmount(bpmCHTPayment.getDutyFreeAmount());
                    detailVo.setUntaxAmount(bpmCHTPayment.getUntaxAmount());
                    detailVo.setTax(bpmCHTPayment.getTaxAmount());
                    detailVo.setApplyAmount(bpmCHTPayment.getApplyAmount());
                    detailVo.setDescription(bpmCHTPayment.getPhone());
                    detailVo.setUniformNum(bpmCHTPayment.getBan());

                    if (detailVo.getTax().compareTo(BigDecimal.ZERO) == 1) {
                        detailVo.setCertificateType(ChtTaxCertificateType);
                        detailVo.setTaxRate(ChtTaxTaxRate);
                        detailVo.setTaxCode(ChtTaxTaxCode);
                        detailVo.setDeduction(ChtTaxDeduction);
                        detailVo.setHasIncomeTax(ChtTaxHasIncomeTax);
                    } else {
                        detailVo.setCertificateType(ChtZeroTaxCertificateType);
                        detailVo.setTaxRate(ChtZeroTaxTaxRate);
                        detailVo.setTaxCode(ChtZeroTaxTaxCode);
                        detailVo.setDeduction(ChtZeroTaxDeduction);
                        detailVo.setHasIncomeTax(ChtZeroTaxHasIncomeTax);
                    }

                    Optional<NcccUtilityBudgetAttribution> foundNCCCUBA = ncccUtilityBudgetAttributionList.stream()
                            .filter(x -> x.getPhone().equals(bpmCHTPayment.getPhone()))
                            .findFirst();

                    if (foundNCCCUBA.isPresent()) {
                        NcccUtilityBudgetAttribution ncccUBA = foundNCCCUBA.get();
                        String itemCode = ""; // 品號
                        String itemName = ""; // 品名
                        String accounting = ""; // 預算科目
                        Optional<NcccExpenseCategoryNumber> foundNCCCEXN = ncccExpenseCategoryNumberList.stream()
                                .filter(x -> x.getAccounting().equals(ncccUBA.getAccounting()))
                                .findFirst();
                        if (foundNCCCEXN.isPresent()) {
                            NcccExpenseCategoryNumber ncccExpenseCategoryNumber = foundNCCCEXN.get();
                            itemCode = ncccExpenseCategoryNumber.getCategoryNumber();
                            itemName = ncccExpenseCategoryNumber.getCategoryName();
                            accounting = ncccExpenseCategoryNumber.getAccounting();
                        }
                        detailVo.setItemCode(itemCode);
                        detailVo.setItemName(itemName);
                        detailVo.setAccounting(accounting);
                        detailVo.setCostCenter(ncccUBA.getOuCode());

                        //根據成本中心取得對應預算部門
                        Optional<NcccCostcenterOrgMapping> foundNCCCCOM = ncccCostcenterOrgMappingList.stream()
                                .filter(x -> x.getCostcenter().equals(detailVo.getCostCenter()))
                                .findFirst();

                        foundNCCCCOM.ifPresent(ncccCostcenterOrgMapping -> detailVo.setOuCode(ncccCostcenterOrgMapping.getBudgetOuCode()));

                        BigDecimal limitAmount = BigDecimal.ZERO;

                        //若有平台金額
                        if (ncccUBA.getLimitAmount() != null && ncccUBA.getLimitAmount().compareTo(BigDecimal.ZERO) == 1) {
                            limitAmount = ncccUBA.getLimitAmount();
                        }

                        detailVo.setChtCollectAmount(limitAmount);
                    }
                }
            }
        }
        return detailVoList;
    }

    /**
     * 上傳 沒收卡獎金檔案
     *
     * @param fileName
     * @return
     */
    @Override
    public BpmExMDetailVo uploadPickUpCardBouns(String fileName) {

        BigDecimal Amount = new BigDecimal(0);

        List<BpmExMDetailWHVo> detailVoList = new ArrayList<>();

        try {
            if (fileName != null && fileName.toLowerCase().endsWith(".txt"))
            {
                FtpsUtil util = ftpsUtilISD;

                String path = isdFtpPath + fileName;

                // 解析 TXT
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(util.readFileStream(path), "Big5"))) {
                    String line;
                    int rowIndex = 1;
                    while ((line = reader.readLine()) != null) {
                        BpmExMDetailWHVo vo = convertPickUpCardBounsToData(line);
                        Amount = Amount.add(vo.getGrossPayment());
                        detailVoList.add(vo);
                        rowIndex++;
                    }
                }
            } else {
                throw new RuntimeException("檔案" + fileName + "處理失敗:不支援的檔案格式");
            }
        } catch (Exception e) {
            LOG.error("檔案" + fileName + "處理失敗:" + e);

            throw new RuntimeException("檔案" + fileName + "處理失敗:" + e);
        }


        BpmExMDetailVo detailVo = new BpmExMDetailVo();
        detailVo.setApplyAmount(Amount);
        detailVo.setUntaxAmount(Amount);
        detailVo.setCertificateType("沒卡獎金匯入");
        String itemCode = "1111990701";
        String itemName = "";
        String accounting = "11119907";
        NcccExpenseCategoryNumber ncccExpenseCategoryNumber = ncccExpenseCategoryNumberRepository.findByCategoryNumber(itemCode);
        if (ncccExpenseCategoryNumber != null) {
            itemName = ncccExpenseCategoryNumber.getCategoryName();
        }
        detailVo.setItemCode(itemCode);
        detailVo.setItemName(itemName);
        detailVo.setAccounting(accounting);
        detailVo.setCostCenter("1E000");
        NcccCostcenterOrgMapping ncccCostcenterOrgMapping = ncccCostcenterOrgMappingRepository.findByCostcenter("1E000");
        if(ncccCostcenterOrgMapping != null)
        {
            detailVo.setOuCode(ncccCostcenterOrgMapping.getBudgetOuCode());
        }
        detailVo.setDescription("代付ATM沒收卡獎金共" + detailVoList.size() + "筆");
        detailVo.setDeduction("N");
        detailVo.setHasIncomeTax("Y");
        detailVo.setItemText("代付ATM沒收卡獎金共" + detailVoList.size() + "筆");
        detailVo.setBpmExMDetailWHVoList(detailVoList);

        return detailVo;
    }

    // 產生董監事明細
    @Override
    public List<BpmExMPplVo> convertDSPplDetails(List<String> selectedDS, BigDecimal travelFeesAmount, BigDecimal attendFeesAmount) {
        List<BpmExMPplVo> result = new ArrayList<>();

        List<NcccDirectorSupervisorList> data = ncccDirectorSupervisorListRepository.GetByIdList(selectedDS);

        List<NcccIncomeTaxParameter> TaxParameters = ncccIncomeTaxParameterRepository.findAll();

        List<String> dataPartnerList = data.stream().map(NcccDirectorSupervisorList::getPartner).collect(Collectors.toList());

        List<ZmmtSupplier> zmmtSupplierList = zmmtSupplierRepository.GetByPartnerList(dataPartnerList);

        BigDecimal Total = travelFeesAmount.add(attendFeesAmount);

        BigDecimal TaxRate = new BigDecimal(10);

        BigDecimal InsuranceRate = new BigDecimal(2.11);

        Optional<NcccIncomeTaxParameter> foundTP = TaxParameters.stream().filter(o -> !o.getTaxRate().equals("") && !o.getInsuranceRate().equals(""))
                .findFirst();

        if (foundTP.isPresent()) {
            NcccIncomeTaxParameter TaxParameter = foundTP.get();

            TaxRate = convertStringToBigDecimal(TaxParameter.getTaxRate());

            InsuranceRate = convertStringToBigDecimal(TaxParameter.getInsuranceRate());
        }

        for (String selectedId : selectedDS) {
            Optional<NcccDirectorSupervisorList> foundNDSL = data.stream()
                    .filter(x -> x.getId().equals(selectedId))
                    .findFirst();

            if (foundNDSL.isPresent()) {
                NcccDirectorSupervisorList thisData = foundNDSL.get();

                // region 明細

                BpmExMPplVo vo = new BpmExMPplVo();

                BigDecimal ThisTravelFeesAmount = travelFeesAmount;

                BigDecimal ThisAttendFeesAmount = attendFeesAmount;

                BigDecimal ThisCenterTravelFeesAmount = new BigDecimal(0);

                BigDecimal ThisCenterAttendFeesAmount = new BigDecimal(0);

                if (thisData.getIsCivil().equals("Y")) {
                    //大於8500
                    if (travelFeesAmount.compareTo(new BigDecimal(8500)) > 0) {
                        ThisTravelFeesAmount = new BigDecimal(8500);
                        ThisCenterTravelFeesAmount = travelFeesAmount.subtract(new BigDecimal(8500));
                        ThisAttendFeesAmount = new BigDecimal(0);
                        ThisCenterAttendFeesAmount = attendFeesAmount;
                    } else {
                        if (attendFeesAmount.add(travelFeesAmount).compareTo(new BigDecimal(8500)) > 0) {
                            ThisAttendFeesAmount = new BigDecimal(8500).subtract(travelFeesAmount);
                            ThisCenterAttendFeesAmount = attendFeesAmount.subtract(ThisAttendFeesAmount);
                        } else if (attendFeesAmount.compareTo(new BigDecimal(8500)) > 0) {
                            ThisAttendFeesAmount = new BigDecimal(8500);
                            ThisCenterAttendFeesAmount = attendFeesAmount.subtract(ThisAttendFeesAmount);
                        }
                    }
                }

                vo.setPplId(thisData.getId());
                vo.setName(thisData.getName());
                vo.setIsCivil(thisData.getIsCivil());
                vo.setUnit(thisData.getUnit());
                vo.setJobTitle(thisData.getJobTitle());
                vo.setAmount1(ThisTravelFeesAmount);
                vo.setAmount2(ThisAttendFeesAmount);
                vo.setAmount3(ThisCenterTravelFeesAmount);
                vo.setAmount4(ThisCenterAttendFeesAmount);
                vo.setTotalAmount(Total);
                vo.setAccounting1("51032601");
                vo.setAccounting2("51032701");
                vo.setAccounting3("51032601");
                vo.setAccounting4("51032701");
                vo.setYear("");
                vo.setOuCode("");

                // region 扣繳清單

                List<BpmExMDetailWHVo> detailVoList = new ArrayList<>();

                BpmExMDetailWHVo detailVo = new BpmExMDetailWHVo();

                BigDecimal thisTotal = ThisTravelFeesAmount.add(ThisAttendFeesAmount);

                detailVo.setBan(thisData.getId());
                detailVo.setCertificateCategory("0");
                detailVo.setGainerName(thisData.getName());
                detailVo.setHasAddress("Y");
                detailVo.setIncomeType("50");
                detailVo.setAddress(thisData.getAddress());
                detailVo.setContactAddress(thisData.getContactAddress());
                detailVo.setContactPhone(thisData.getPhone1());
                detailVo.setPaymentYear(Integer.toString(LocalDate.now().getYear()));
                detailVo.setPaymentMonthSt("01");
                detailVo.setPaymentMonthEd("12");
                detailVo.setCertificateIssueMethod("1");
                detailVo.setGrossPayment(thisTotal);
                detailVo.setWithholdingTaxRate(TaxRate);
                detailVo.setWithholdingTax(thisTotal.multiply(TaxRate.divide(new BigDecimal(100))).setScale(0, BigDecimal.ROUND_HALF_UP));
                detailVo.setNhRate(InsuranceRate);
                detailVo.setRevenueCategory("62");
                detailVo.setNhWithHolding(thisTotal.multiply(InsuranceRate.divide(new BigDecimal(100))).setScale(0, BigDecimal.ROUND_HALF_UP));
                detailVo.setNetPayment(thisTotal.subtract(detailVo.getWithholdingTax()).subtract(detailVo.getNhWithHolding()));
                detailVoList.add(detailVo);

                if (thisData.getIsCivil().equals("Y"))
                {
                    Optional<ZmmtSupplier> foundZS = zmmtSupplierList.stream()
                            .filter(x -> x.getPartner().equals(thisData.getPartner()))
                            .findFirst();

                    String SupplierName = "";

                    String SupplierBan = "";

                    if(foundZS.isPresent())
                    {
                        ZmmtSupplier zmmtSupplier = foundZS.get();

                        SupplierName = zmmtSupplier.getNameOrg1();

                        SupplierBan = zmmtSupplier.getTaxnum();
                    }

                    BigDecimal thisSupplierTotal = ThisCenterTravelFeesAmount.add(ThisCenterAttendFeesAmount);

                    BpmExMDetailWHVo supplierDetailVo = new BpmExMDetailWHVo();

                    supplierDetailVo.setBan(SupplierBan);
                    supplierDetailVo.setCertificateCategory("1");
                    supplierDetailVo.setGainerName(SupplierName);
                    supplierDetailVo.setHasAddress("Y");
                    supplierDetailVo.setIncomeType("50");
                    supplierDetailVo.setAddress("");
                    supplierDetailVo.setContactAddress("");
                    supplierDetailVo.setContactPhone("");
                    supplierDetailVo.setPaymentYear(Integer.toString(LocalDate.now().getYear()));
                    supplierDetailVo.setPaymentMonthSt("01");
                    supplierDetailVo.setPaymentMonthEd("12");
                    supplierDetailVo.setCertificateIssueMethod("1");
                    supplierDetailVo.setGrossPayment(thisSupplierTotal);
                    supplierDetailVo.setWithholdingTaxRate(TaxRate);
                    supplierDetailVo.setWithholdingTax(thisSupplierTotal.multiply(TaxRate.divide(new BigDecimal(100))).setScale(0, BigDecimal.ROUND_HALF_UP));
                    supplierDetailVo.setNhRate(InsuranceRate);
                    supplierDetailVo.setRevenueCategory("62");
                    supplierDetailVo.setNhWithHolding(thisSupplierTotal.multiply(InsuranceRate.divide(new BigDecimal(100))).setScale(0, BigDecimal.ROUND_HALF_UP));
                    supplierDetailVo.setNetPayment(thisSupplierTotal.subtract(supplierDetailVo.getWithholdingTax()).subtract(supplierDetailVo.getNhWithHolding()));
                    detailVoList.add(supplierDetailVo);
                }

                // endregion

                vo.setBpmExMDetailWHVoList(detailVoList);

                result.add(vo);
                // endregion

            }
        }

        return result;
    }

    // 產生研發委員明細
    @Override
    public List<BpmExMPplVo> convertCPplDetails(List<String> selectedC, BigDecimal researchFeesAmount, BigDecimal attendFeesAmount, BigDecimal allowanceAmount) {
        List<BpmExMPplVo> result = new ArrayList<>();

        List<NcccCommitteeList> data = ncccCommitteeListRepository.findAll();

        List<NcccIncomeTaxParameter> TaxParameters = ncccIncomeTaxParameterRepository.findAll();

        BigDecimal Total = researchFeesAmount.add(attendFeesAmount).add(allowanceAmount);

        BigDecimal TaxRate = new BigDecimal(10);

        BigDecimal InsuranceRate = new BigDecimal(2.11);

        Optional<NcccIncomeTaxParameter> foundTP = TaxParameters.stream().filter(o -> !o.getTaxRate().equals("") && !o.getInsuranceRate().equals(""))
                .findFirst();

        if (foundTP.isPresent()) {
            NcccIncomeTaxParameter TaxParameter = foundTP.get();

            TaxRate = convertStringToBigDecimal(TaxParameter.getTaxRate());

            InsuranceRate = convertStringToBigDecimal(TaxParameter.getInsuranceRate());
        }

        for (String selectedId : selectedC) {
            Optional<NcccCommitteeList> foundNDSL = data.stream()
                    .filter(x -> x.getId().equals(selectedId))
                    .findFirst();

            if (foundNDSL.isPresent()) {
                NcccCommitteeList thisData = foundNDSL.get();

                // region 扣繳清單

                List<BpmExMDetailWHVo> detailVoList = new ArrayList<>();

                BpmExMDetailWHVo detailVo = new BpmExMDetailWHVo();

                detailVo.setBan(thisData.getId());
                detailVo.setCertificateCategory("0");
                detailVo.setGainerName(thisData.getName());
                detailVo.setHasAddress("Y");
                detailVo.setIncomeType("50");
                detailVo.setContactPhone(thisData.getPhone1());
                detailVo.setAddress(thisData.getAddress());
                detailVo.setContactAddress(thisData.getContactAddress());
                detailVo.setPaymentYear(Integer.toString(LocalDate.now().getYear()));
                detailVo.setPaymentMonthSt("01");
                detailVo.setPaymentMonthEd("12");
                detailVo.setCertificateIssueMethod("1");
                detailVo.setGrossPayment(Total);
                detailVo.setWithholdingTaxRate(TaxRate);
                detailVo.setWithholdingTax(Total.multiply(TaxRate.divide(new BigDecimal(100))).setScale(0, BigDecimal.ROUND_HALF_UP));
                detailVo.setNhRate(InsuranceRate);
                detailVo.setRevenueCategory("62");
                detailVo.setNhWithHolding(Total.multiply(InsuranceRate.divide(new BigDecimal(100))).setScale(0, BigDecimal.ROUND_HALF_UP));
                detailVo.setNetPayment(Total.subtract(detailVo.getWithholdingTax()).subtract(detailVo.getNhWithHolding()));
                detailVoList.add(detailVo);

                // endregion

                // region 明細

                BpmExMPplVo vo = new BpmExMPplVo();
                vo.setPplId(thisData.getId());
                vo.setName(thisData.getName());
                vo.setUnit(thisData.getUnit());
                vo.setJobTitle(thisData.getJobTitle());
                vo.setAmount1(researchFeesAmount);
                vo.setAmount2(attendFeesAmount);
                vo.setAmount3(allowanceAmount);
                vo.setTotalAmount(Total);
                vo.setAccounting1("51021801");
                vo.setAccounting2("51021802");
                vo.setAccounting3("51021803");
                vo.setAccounting4("");
                vo.setYear("");
                vo.setOuCode("");
                vo.setBpmExMDetailWHVoList(detailVoList);
                result.add(vo);
                // endregion

            }
        }

        return result;
    }

    // endregion

    // region 憑證黏貼單

    @Override
    public byte[] getVoucherStrickerWord(String exNo) {
        BpmExM bpmExM = bpmExMRepository.findById(exNo).orElseThrow(() -> new RuntimeException("找不到主檔: EX_NO=" + exNo));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // region 費用單類型

        String applyType = "";

        String resourceName = "template/voucherStickerTemplate.docx";

        switch (bpmExM.getApplyType()) {
            case "1":

                applyType = "一般";

                break;

            case "2":

                applyType = "零用金";

                break;

            case "3":

                applyType = "零用金撥補";

                break;

            case "4":

                applyType = "公用事業費";

                break;

            case "5":

                applyType = "預付借支";

                break;

            case "6":

                applyType = "沒收卡獎金";

                break;

            case "7":

                applyType = "董監事";

                resourceName = "template/voucherStickerDSTemplate.docx";

                break;

            case "8":

                applyType = "研發委員";

                resourceName = "template/voucherStickerCTemplate.docx";

                break;

        }

        // endregion

        ClassPathResource resource = new ClassPathResource(resourceName);

        try {

            Map<String, String> replacements = new HashMap<>();
            replacements.put("{apartment}", bpmExM.getDepartment());
            replacements.put("{name}", bpmExM.getApplicant());
            replacements.put("{orderNo}", bpmExM.getExNo());
            replacements.put("{type}", applyType);
            replacements.put("{paidDate}", bpmExM.getPostingDate());

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

            //董監事
            if (resourceName.equals("template/voucherStickerDSTemplate.docx")) {
                List<BpmExPsD> bpmExPsDList = bpmExPsDRepository.findByExNo(exNo);

                BigDecimal totalTravelFeesAmount = new BigDecimal(0);

                BigDecimal totalAttendanceFeeAmount = new BigDecimal(0);

                BigDecimal totalTravelFeesByCenterAmount = new BigDecimal(0);

                BigDecimal totalAttendanceFeeByCenterAmount = new BigDecimal(0);

                BigDecimal totalAmount = new BigDecimal(0);

                int index = 1;

                for (BpmExPsD bpmExPsD : bpmExPsDList) {
                    totalTravelFeesAmount = totalTravelFeesAmount.add(bpmExPsD.getAmount1());

                    totalAttendanceFeeAmount = totalAttendanceFeeAmount.add(bpmExPsD.getAmount2());

                    totalTravelFeesByCenterAmount = totalTravelFeesByCenterAmount.add(bpmExPsD.getAmount3());

                    totalAttendanceFeeByCenterAmount = totalAttendanceFeeByCenterAmount.add(bpmExPsD.getAmount4());

                    totalAmount = totalAmount.add(bpmExPsD.getAmount1().add(bpmExPsD.getAmount2()).add(bpmExPsD.getAmount3()).add(bpmExPsD.getAmount4()));

                    // region 新增明細

                    XWPFTableRow newRow = table.createRow();

                    XWPFTableCell indexCell = newRow.getCell(0);

                    indexCell.setText(String.valueOf(index));

                    XWPFParagraph indexPara = indexCell.getParagraphs().get(0);

                    indexPara.setAlignment(ParagraphAlignment.CENTER);

                    XWPFTableCell nameCell = newRow.getCell(1);

                    nameCell.setText(bpmExPsD.getName());

                    XWPFTableCell jobTitleCell = newRow.getCell(2);

                    jobTitleCell.setText(bpmExPsD.getJobTitle());

                    XWPFTableCell unitCell = newRow.getCell(3);

                    unitCell.setText(bpmExPsD.getUnit());

                    XWPFTableCell travelFeesCell = newRow.getCell(4);

                    travelFeesCell.setText(MoneyUtil.formatTWD(bpmExPsD.getAmount1()));

                    XWPFParagraph travelFeesPara = travelFeesCell.getParagraphs().get(0);

                    travelFeesPara.setAlignment(ParagraphAlignment.RIGHT);

                    XWPFTableCell attendanceFeeCell = newRow.getCell(5);

                    attendanceFeeCell.setText(MoneyUtil.formatTWD(bpmExPsD.getAmount2()));

                    XWPFParagraph attendanceFeePara = attendanceFeeCell.getParagraphs().get(0);

                    attendanceFeePara.setAlignment(ParagraphAlignment.RIGHT);

                    XWPFTableCell travelFeesByCenterCell = newRow.getCell(6);

                    travelFeesByCenterCell.setText(MoneyUtil.formatTWD(bpmExPsD.getAmount3()));

                    XWPFParagraph travelFeesByCenterPara = travelFeesByCenterCell.getParagraphs().get(0);

                    travelFeesByCenterPara.setAlignment(ParagraphAlignment.RIGHT);

                    XWPFTableCell attendanceFeeByCenterCell = newRow.getCell(7);

                    attendanceFeeByCenterCell.setText(MoneyUtil.formatTWD(bpmExPsD.getAmount4()));

                    XWPFParagraph attendanceFeeByCenterPara = attendanceFeeByCenterCell.getParagraphs().get(0);

                    attendanceFeeByCenterPara.setAlignment(ParagraphAlignment.RIGHT);

                    XWPFTableCell totalAmountCell = newRow.getCell(8);

                    totalAmountCell.setText(MoneyUtil.formatTWD(bpmExPsD.getAmount1().add(bpmExPsD.getAmount2()).add(bpmExPsD.getAmount3()).add(bpmExPsD.getAmount4())));

                    XWPFParagraph totalAmountPara = totalAmountCell.getParagraphs().get(0);

                    totalAmountPara.setAlignment(ParagraphAlignment.RIGHT);

                    //設定文字大小
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

                XWPFTableCell totalTravelFeesAmountCell = totalRow.getCell(4);

                totalTravelFeesAmountCell.setText(MoneyUtil.formatTWD(totalTravelFeesAmount));

                XWPFParagraph totalTravelFeesAmountPara = totalTravelFeesAmountCell.getParagraphs().get(0);

                XWPFRun totalTravelFeesAmountRun = totalTravelFeesAmountPara.getRuns().get(0);

                totalTravelFeesAmountRun.setUnderline(UnderlinePatterns.SINGLE);

                XWPFTableCell totalAttendanceFeeAmountCell = totalRow.getCell(5);

                totalAttendanceFeeAmountCell.setText(MoneyUtil.formatTWD(totalAttendanceFeeAmount));

                XWPFParagraph totalAttendanceFeeAmountPara = totalAttendanceFeeAmountCell.getParagraphs().get(0);

                XWPFRun totalAttendanceFeeAmountRun = totalAttendanceFeeAmountPara.getRuns().get(0);

                totalAttendanceFeeAmountRun.setUnderline(UnderlinePatterns.SINGLE);

                XWPFTableCell totalTravelFeesByCenterAmountCell = totalRow.getCell(6);

                totalTravelFeesByCenterAmountCell.setText(MoneyUtil.formatTWD(totalTravelFeesByCenterAmount));

                XWPFParagraph totalTravelFeesByCenterAmountPara = totalTravelFeesByCenterAmountCell.getParagraphs().get(0);

                XWPFRun totalTravelFeesByCenterAmountRun = totalTravelFeesByCenterAmountPara.getRuns().get(0);

                totalTravelFeesByCenterAmountRun.setUnderline(UnderlinePatterns.SINGLE);

                XWPFTableCell totalAttendanceFeeByCenterAmountCell = totalRow.getCell(7);

                totalAttendanceFeeByCenterAmountCell.setText(MoneyUtil.formatTWD(totalAttendanceFeeByCenterAmount));

                XWPFParagraph totalAttendanceFeeByCenterAmountPara = totalAttendanceFeeByCenterAmountCell.getParagraphs().get(0);

                XWPFRun totalAttendanceFeeByCenterAmountRun = totalAttendanceFeeByCenterAmountPara.getRuns().get(0);

                totalAttendanceFeeByCenterAmountRun.setUnderline(UnderlinePatterns.SINGLE);

                XWPFTableCell totalAmountCell = totalRow.getCell(8);

                totalAmountCell.setText(MoneyUtil.formatTWD(totalAmount));

                XWPFParagraph totalAmountPara = totalAmountCell.getParagraphs().get(0);

                XWPFRun totalAmountRun = totalAmountPara.getRuns().get(0);

                totalAmountRun.setUnderline(UnderlinePatterns.SINGLE);

                //設定文字大小
                for (XWPFTableCell cell : totalRow.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        for (XWPFRun run : paragraph.getRuns()) {
                            run.setFontSize(8); // 設定文字大小為 12pt
                        }
                    }
                }

                // endregion
            }
            // 研發委員
            else if (resourceName.equals("template/voucherStickerCTemplate.docx")) {
                List<BpmExPsD> bpmExPsDList = bpmExPsDRepository.findByExNo(exNo);

                BigDecimal totalResearchFeeAmount = new BigDecimal(0);

                BigDecimal totalTravelFeesAmount = new BigDecimal(0);

                BigDecimal totalAllowanceAmount = new BigDecimal(0);

                BigDecimal totalAmount = new BigDecimal(0);

                int index = 1;

                for (BpmExPsD bpmExPsD : bpmExPsDList) {
                    totalResearchFeeAmount = totalResearchFeeAmount.add(bpmExPsD.getAmount1());

                    totalTravelFeesAmount = totalTravelFeesAmount.add(bpmExPsD.getAmount2());

                    totalAllowanceAmount = totalAllowanceAmount.add(bpmExPsD.getAmount3());

                    totalAmount = totalAmount.add(bpmExPsD.getAmount1().add(bpmExPsD.getAmount2()).add(bpmExPsD.getAmount3()));

                    // region 新增明細

                    XWPFTableRow newRow = table.createRow();

                    XWPFTableCell indexCell = newRow.getCell(0);

                    indexCell.setText(String.valueOf(index));

                    XWPFParagraph indexPara = indexCell.getParagraphs().get(0);

                    indexPara.setAlignment(ParagraphAlignment.CENTER);

                    XWPFTableCell nameCell = newRow.getCell(1);

                    nameCell.setText(bpmExPsD.getName());

                    XWPFTableCell jobTitleCell = newRow.getCell(2);

                    jobTitleCell.setText(bpmExPsD.getJobTitle());

                    XWPFTableCell unitCell = newRow.getCell(3);

                    unitCell.setText(bpmExPsD.getUnit());

                    XWPFTableCell researchFeeCell = newRow.getCell(4);

                    researchFeeCell.setText(MoneyUtil.formatTWD(bpmExPsD.getAmount1()));

                    XWPFParagraph researchFeePara = researchFeeCell.getParagraphs().get(0);

                    researchFeePara.setAlignment(ParagraphAlignment.RIGHT);

                    XWPFTableCell travelFeesCell = newRow.getCell(5);

                    travelFeesCell.setText(MoneyUtil.formatTWD(bpmExPsD.getAmount2()));

                    XWPFParagraph travelFeesPara = travelFeesCell.getParagraphs().get(0);

                    travelFeesPara.setAlignment(ParagraphAlignment.RIGHT);

                    XWPFTableCell allowanceCell = newRow.getCell(6);

                    allowanceCell.setText(MoneyUtil.formatTWD(bpmExPsD.getAmount3()));

                    XWPFParagraph allowancePara = allowanceCell.getParagraphs().get(0);

                    allowancePara.setAlignment(ParagraphAlignment.RIGHT);

                    XWPFTableCell totalAmountCell = newRow.getCell(7);

                    totalAmountCell.setText(MoneyUtil.formatTWD(bpmExPsD.getAmount1().add(bpmExPsD.getAmount2()).add(bpmExPsD.getAmount3())));

                    XWPFParagraph totalAmountPara = totalAmountCell.getParagraphs().get(0);

                    totalAmountPara.setAlignment(ParagraphAlignment.RIGHT);

                    //設定文字大小
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

                XWPFTableCell totalResearchFeeAmountCell = totalRow.getCell(4);

                totalResearchFeeAmountCell.setText(MoneyUtil.formatTWD(totalResearchFeeAmount));

                XWPFParagraph totalResearchFeeAmountPara = totalResearchFeeAmountCell.getParagraphs().get(0);

                XWPFRun totalResearchFeeAmountRun = totalResearchFeeAmountPara.getRuns().get(0);

                totalResearchFeeAmountRun.setUnderline(UnderlinePatterns.SINGLE);

                XWPFTableCell totalTravelFeesAmountCell = totalRow.getCell(5);

                totalTravelFeesAmountCell.setText(MoneyUtil.formatTWD(totalTravelFeesAmount));

                XWPFParagraph totalTravelFeesAmountPara = totalTravelFeesAmountCell.getParagraphs().get(0);

                XWPFRun totalTravelFeesAmountRun = totalTravelFeesAmountPara.getRuns().get(0);

                totalTravelFeesAmountRun.setUnderline(UnderlinePatterns.SINGLE);

                XWPFTableCell totalAllowanceAmountCell = totalRow.getCell(6);

                totalAllowanceAmountCell.setText(MoneyUtil.formatTWD(totalAllowanceAmount));

                XWPFParagraph totalAllowanceAmountPara = totalAllowanceAmountCell.getParagraphs().get(0);

                XWPFRun totalAllowanceAmountRun = totalAllowanceAmountPara.getRuns().get(0);

                totalAllowanceAmountRun.setUnderline(UnderlinePatterns.SINGLE);

                XWPFTableCell totalAmountCell = totalRow.getCell(7);

                totalAmountCell.setText(MoneyUtil.formatTWD(totalAmount));

                XWPFParagraph totalAmountPara = totalAmountCell.getParagraphs().get(0);

                XWPFRun totalAmountRun = totalAmountPara.getRuns().get(0);

                totalAmountRun.setUnderline(UnderlinePatterns.SINGLE);

                //設定文字大小
                for (XWPFTableCell cell : totalRow.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        for (XWPFRun run : paragraph.getRuns()) {
                            run.setFontSize(8); // 設定文字大小為 12pt
                        }
                    }
                }

                // endregion
            }
            //其他
            else {
                List<BpmExMD1> bpmExMD1List = bpmExMD1Repository.findByExNo(exNo);

                BigDecimal totalApplyAmount = new BigDecimal(0);

                BigDecimal totalUntaxAmount = new BigDecimal(0);

                BigDecimal totalTaxAmount = new BigDecimal(0);

                BigDecimal totalDutyFreeAmount = new BigDecimal(0);

                BigDecimal totalZeroTaxAmount = new BigDecimal(0);

                int index = 1;

                for (BpmExMD1 bpmExMD1 : bpmExMD1List) {
                    totalApplyAmount = totalApplyAmount.add(bpmExMD1.getApplyAmount());

                    totalUntaxAmount = totalUntaxAmount.add(bpmExMD1.getUntaxAmount()).add(bpmExMD1.getChtCollectAmount());

                    totalTaxAmount = totalTaxAmount.add(bpmExMD1.getTax());

                    totalDutyFreeAmount = totalDutyFreeAmount.add(bpmExMD1.getDutyFreeAmount());

                    totalZeroTaxAmount = totalZeroTaxAmount.add(bpmExMD1.getZeroTaxAmount());

                    // region 新增明細

                    XWPFTableRow newRow = table.createRow();

                    XWPFTableCell indexCell = newRow.getCell(0);

                    indexCell.setText(String.valueOf(index));

                    XWPFParagraph indexPara = indexCell.getParagraphs().get(0);

                    indexPara.setAlignment(ParagraphAlignment.CENTER);

                    XWPFTableCell certificateDateCell = newRow.getCell(1);

                    certificateDateCell.setText(bpmExMD1.getCertificateDate());

                    XWPFTableCell certificateCodeCell = newRow.getCell(2);

                    certificateCodeCell.setText(bpmExMD1.getCertificateCode());

                    XWPFTableCell nameCell = newRow.getCell(3);

                    nameCell.setText(bpmExMD1.getItemName());

                    XWPFTableCell applyAmountCell = newRow.getCell(4);

                    applyAmountCell.setText(MoneyUtil.formatTWD(bpmExMD1.getApplyAmount()));

                    XWPFParagraph applyAmountPara = applyAmountCell.getParagraphs().get(0);

                    applyAmountPara.setAlignment(ParagraphAlignment.RIGHT);

                    XWPFTableCell untaxAmountCell = newRow.getCell(5);

                    untaxAmountCell.setText(MoneyUtil.formatTWD(bpmExMD1.getUntaxAmount().add(bpmExMD1.getChtCollectAmount())));

                    XWPFParagraph untaxAmountPara = untaxAmountCell.getParagraphs().get(0);

                    untaxAmountPara.setAlignment(ParagraphAlignment.RIGHT);

                    XWPFTableCell taxAmountCell = newRow.getCell(6);

                    taxAmountCell.setText(MoneyUtil.formatTWD(bpmExMD1.getTax()));

                    XWPFParagraph taxAmountPara = taxAmountCell.getParagraphs().get(0);

                    taxAmountPara.setAlignment(ParagraphAlignment.RIGHT);

                    XWPFTableCell dutyFreeAmountCell = newRow.getCell(7);

                    dutyFreeAmountCell.setText(MoneyUtil.formatTWD(bpmExMD1.getDutyFreeAmount()));

                    XWPFParagraph dutyFreeAmountPara = dutyFreeAmountCell.getParagraphs().get(0);

                    dutyFreeAmountPara.setAlignment(ParagraphAlignment.RIGHT);

                    XWPFTableCell zeroTaxAmountCell = newRow.getCell(8);

                    zeroTaxAmountCell.setText(MoneyUtil.formatTWD(bpmExMD1.getZeroTaxAmount()));

                    XWPFParagraph zeroTaxAmountPara = zeroTaxAmountCell.getParagraphs().get(0);

                    zeroTaxAmountPara.setAlignment(ParagraphAlignment.RIGHT);

                    //設定文字大小
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

                //設定文字大小
                for (XWPFTableCell cell : totalRow.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        for (XWPFRun run : paragraph.getRuns()) {
                            run.setFontSize(8); // 設定文字大小為 12pt
                        }
                    }
                }

                // endregion
            }

            // endregion


            document.write(outputStream);
            document.close();


        } catch (Exception e) {
        }

        return outputStream.toByteArray();
    }

    private static void replaceTextInParagraph(XWPFParagraph paragraph, Map<String, String> replacements) {
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

    // region 副函式

    private ArrayList<TInput> getTInputList(List<BpmExMD1> bpmExMD1List) {
        List<NcccReceiptType> ncccReceiptTypeList = ncccReceiptTypeRepository.findAll();

        ArrayList<TInput> result = new ArrayList<TInput>();

        for (BpmExMD1 bpmExMD1 : bpmExMD1List) {

            String ZformCode = "";
            String cusType = "";
            Optional<NcccReceiptType> foundReceiptType = ncccReceiptTypeList.stream()
                    .filter(x -> x.getIntyp().equals(bpmExMD1.getCertificateType()))
                    .findFirst();
            if (foundReceiptType.isPresent()) {
                NcccReceiptType receiptType = foundReceiptType.get();
                if (receiptType.getZformCode() != null) {
                    ZformCode = receiptType.getZformCode().toString();
                }
                cusType = receiptType.getCusType();
            }


            TInput tInput = new TInput();
            tInput.setBLDAT(bpmExMD1.getCertificateDate().replace("-", ""));
            tInput.setVATDATE(bpmExMD1.getCertificateDate().replace("-", ""));
            tInput.setXBLNR((bpmExMD1.getCertificateCode()));
            tInput.setZFORM_CODE(ZformCode);
            tInput.setSTCEG(bpmExMD1.getUniformNum());
            tInput.setHWBAS(bpmExMD1.getUntaxAmount().toString());
            tInput.setHWSTE(bpmExMD1.getTax().toString());
            tInput.setTAX_TYPE("1");
            tInput.setCUS_TYPE(cusType);
            tInput.setAM_TYPE("1");
            result.add(tInput);
        }

        return result;
    }

    /**
     * 將 TXT 的行資料轉換為 ApplicationExpensesPickUpCardVo
     *
     * @return
     */
    private static BpmExMDetailWHVo convertPickUpCardBounsToData(String line) {
        BpmExMDetailWHVo detailVo = new BpmExMDetailWHVo();

        // 定義欄位名稱及其長度的映射
        Map<String, Integer> fieldLengths = new LinkedHashMap<>();
        fieldLengths.put("IDNO", 10);
        fieldLengths.put("IDCODE", 1);
        fieldLengths.put("IDTYPE", 1);
        fieldLengths.put("R_MAN", 500);
        fieldLengths.put("R_MANTYPE", 1);
        fieldLengths.put("PASSPORT_FLG", 1);
        fieldLengths.put("PASSPORT", 10);
        fieldLengths.put("ADDRESS_FLG", 1);
        fieldLengths.put("ADDRESS1", 80);
        fieldLengths.put("ADDRESS2", 80);
        fieldLengths.put("TELNO", 20);
        fieldLengths.put("AMT", 11);
        fieldLengths.put("WITHHOLDING_TAXRATE", 5);
        fieldLengths.put("WITHHOLDING_TAX", 9);
        fieldLengths.put("NET_BENEFIT", 11);
        fieldLengths.put("INCOME_YEAR", 4);
        fieldLengths.put("DESCRIPTION", 20);

        Map<String, String> fieldValues = new HashMap<>();
        int curr = 0; // 當前位置

        // 使用迴圈提取每個欄位
        for (Map.Entry<String, Integer> entry : fieldLengths.entrySet()) {
            String fieldName = entry.getKey();
            int byteLength = entry.getValue();
            int actualLength = 0;
            StringBuilder field = new StringBuilder();

            // 確保當前位置不超出字串長度
            while (curr < line.length() && actualLength < byteLength) {
                char ch = line.charAt(curr);
                int charByteLength = String.valueOf(ch).getBytes(Charset.forName("Big5")).length;

                // 中文為 2 bytes，其他為 1 byte
                if (charByteLength == 2) {
                    if (actualLength + 2 <= byteLength) {
                        field.append(ch);
                        actualLength += 2; // 增加 2 bytes
                    } else {
                        break; // 超過限制，不再添加
                    }
                } else {
                    if (actualLength + 1 <= byteLength) {
                        field.append(ch);
                        actualLength += 1; // 增加 1 byte
                    } else {
                        break; // 超過限制，不再添加
                    }
                }
                curr++; // 移動到下一個字元
            }

            fieldValues.put(fieldName, field.toString().trim());
        }

        // 設置欄位值
        detailVo.setBan(fieldValues.get("IDNO"));
        detailVo.setErrorNote(fieldValues.get("IDCODE"));
        detailVo.setCertificateCategory(fieldValues.get("IDTYPE"));
        detailVo.setGainerName(fieldValues.get("R_MAN"));
        detailVo.setGainerType(fieldValues.get("R_MANTYPE"));
        detailVo.setHasPassport(fieldValues.get("PASSPORT_FLG") == "1" ? "Y" : "N");
        detailVo.setPassportNo(fieldValues.get("PASSPORT"));
        detailVo.setHasAddress(fieldValues.get("ADDRESS_FLG") == "1" ? "Y" : "N");
        detailVo.setAddress(fieldValues.get("ADDRESS1"));
        detailVo.setContactAddress(fieldValues.get("ADDRESS2"));
        detailVo.setContactPhone(fieldValues.get("TELNO"));
        detailVo.setGrossPayment(convertStringToBigDecimal(fieldValues.get("AMT")));
        detailVo.setWithholdingTaxRate(convertStringToBigDecimal("0"));
        detailVo.setWithholdingTax(convertStringToBigDecimal("0"));
        detailVo.setNetPayment(convertStringToBigDecimal(fieldValues.get("AMT")));
        detailVo.setPaymentYear(fieldValues.get("INCOME_YEAR"));
        detailVo.setPaymentMonthSt("01");
        detailVo.setPaymentMonthEd("12");
        detailVo.setRemark(fieldValues.get("DESCRIPTION"));
        detailVo.setIncomeType("92");
        detailVo.setShareColumn1("8A");
        detailVo.setRevenueCategory("");
        return detailVo;
    }

    /**
     * 將字串轉換為 BigDecimal，並處理可能的錯誤。
     *
     * @param numberString 要轉換的字串
     * @return 轉換後的 BigDecimal，若轉換失敗則返回 null
     */
    private static BigDecimal convertStringToBigDecimal(String numberString) {
        try {
            return new BigDecimal(numberString);
        } catch (NumberFormatException e) {
            System.out.println("無效的數字格式: " + numberString);
            return null; // 返回 null 表示轉換失敗
        }
    }

    /**
     * 將字串轉換為 Integer，並處理可能的錯誤。
     *
     * @param numberString 要轉換的字串
     * @return 轉換後的 Integer，若轉換失敗則返回 null
     */
    private static Integer convertStringToInteger(String numberString) {
        try {
            return Integer.parseInt(numberString);
        } catch (NumberFormatException e) {
            System.out.println("無效的整數格式: " + numberString);
            return null; // 返回 null 表示轉換失敗
        }
    }

    /**
     * 將 Excel 的行資料轉換為 ApplicationExpensesDetailVo
     *
     * @param row
     * @param rowIndex
     * @return
     */
    private static BpmExMDetailVo convertRowToData(Row row, int rowIndex) {
        String index = String.valueOf(rowIndex);

        BpmExMDetailVo detailVo = new BpmExMDetailVo();

        Cell cell = null;

        int cellNum = 0;

        cell = row.getCell(cellNum);

        cell = row.getCell(cellNum++);

        detailVo.setCertificateDate(handleCellValue(cell));

        cell = row.getCell(cellNum++);

        detailVo.setCertificateCode(handleCellValue(cell));

        cell = row.getCell(cellNum++);

        detailVo.setCertificateType(handleCellValue(cell));

        cell = row.getCell(cellNum++);

        detailVo.setUniformNum(handleCellValue(cell));

        cell = row.getCell(cellNum++);

        detailVo.setApplyAmount(convertStringToBigDecimal(handleCellValue(cell)));

        cell = row.getCell(cellNum++);

        detailVo.setUntaxAmount(convertStringToBigDecimal(handleCellValue(cell)));

        cell = row.getCell(cellNum++);

        detailVo.setTax(convertStringToBigDecimal(handleCellValue(cell)));

        cell = row.getCell(cellNum++);

        detailVo.setItemCode(handleCellValue(cell));

        cell = row.getCell(cellNum++);

        detailVo.setItemName(handleCellValue(cell));

        cell = row.getCell(cellNum++);

        detailVo.setCostCenter(handleCellValue(cell));

        cell = row.getCell(cellNum++);

        detailVo.setDescription(handleCellValue(cell));

        cell = row.getCell(cellNum++);

        detailVo.setItemText(handleCellValue(cell));

        return detailVo;
    }

    /**
     * 將 中華電信繳費Excel 的行資料轉換為 ApplicationExpensesDetailVo
     *
     * @param row
     * @param rowIndex
     * @return
     */
    private static BpmExMDetailVo convertCHTBillRowToData(Row row, int rowIndex) {
        String index = String.valueOf(rowIndex);

        BpmExMDetailVo detailVo = new BpmExMDetailVo();

        Cell cell = null;

        cell = row.getCell(1);

        detailVo.setZeroTaxAmount(convertStringToBigDecimal(handleCellValue(cell)));

        cell = row.getCell(2);

        detailVo.setDutyFreeAmount(convertStringToBigDecimal(handleCellValue(cell)));

        cell = row.getCell(0);

        detailVo.setUntaxAmount(convertStringToBigDecimal(handleCellValue(cell)));

        cell = row.getCell(5);

        detailVo.setTax(convertStringToBigDecimal(handleCellValue(cell)));

        cell = row.getCell(6);

        detailVo.setApplyAmount(convertStringToBigDecimal(handleCellValue(cell)));

        cell = row.getCell(7);

        detailVo.setCarrierNumber(handleCellValue(cell));

        cell = row.getCell(8);

        detailVo.setDescription(handleCellValue(cell));

        cell = row.getCell(10);

        detailVo.setUniformNum(handleCellValue(cell));

        cell = row.getCell(13);

        detailVo.setNotificationNumber(handleCellValue(cell));

        return detailVo;
    }

    /**
     * 將 中華電信銷帳Excel 的行資料轉換為 ApplicationExpensesDetailVo
     *
     * @param row
     * @param rowIndex
     * @return
     */
    private static BpmExMDetailVo convertCHTWriteOffRowToData(Row row, int rowIndex) {
        String index = String.valueOf(rowIndex);

        BpmExMDetailVo detailVo = new BpmExMDetailVo();

        Cell cell = null;

        cell = row.getCell(0);

        detailVo.setCertificateCode(handleCellValue(cell));

        cell = row.getCell(1);

        detailVo.setCertificateDate(DateUtil.changeRocDateToNormalDate(handleCellValue(cell)));

        cell = row.getCell(2);

        detailVo.setCarrierNumber(handleCellValue(cell));

        cell = row.getCell(3);

        detailVo.setNotificationNumber(handleCellValue(cell));

        return detailVo;
    }

    /**
     * 處理單元格的值，返回字符串
     *
     * @param cell
     * @return
     */
    private static String handleCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        DataFormatter formatter = new DataFormatter();
        String str = formatter.formatCellValue(cell);

        return str;

    }

    /**
     * 取得用戶資料集
     */
    public List<SyncUser> findSyncUserAll() {
        return syncUserRepository.findAll();
    }

    public List<SyncOU> findSyncOUAll() {
        return syncOURepository.findAll();
    }

    // 指派
    private Map<String, Object> setAssignee(Map<String, Object> vars, AssigneeRole assigneeRole) {
        vars.put(assigneeRole.getKey(),
                syncOURepository.findByOuCodeWithManager(assigneeRole.getCode()).getHrid());
        return vars;
    }

    private static long scientificToLong(String sci) {
        BigDecimal bd = new BigDecimal(sci);
        return bd.longValueExact(); // throws exception if value has decimals
    }

    private List<TInput> buildTInputList(List<BpmExMDetailVo> bpmExMDetailVoList) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<NcccReceiptType> ncccReceiptTypeList = ncccReceiptTypeRepository.findAll();

        List<TInput> tInputList = new ArrayList<>();
        for (BpmExMDetailVo bpmExMDetailVo : bpmExMDetailVoList) {
            if (bpmExMDetailVo.getTaxCode() == null || GUI_SKIP.contains(bpmExMDetailVo.getTaxCode())) {
                // V0 V9不進GUI
                continue;
            }
            String ZformCode = "";
            String cusType = "";
            Optional<NcccReceiptType> foundReceiptType = ncccReceiptTypeList.stream()
                    .filter(x -> x.getIntyp().equals(bpmExMDetailVo.getCertificateType()))
                    .findFirst();
            if (foundReceiptType.isPresent()) {
                NcccReceiptType receiptType = foundReceiptType.get();
                if (receiptType.getZformCode() != null) {
                    ZformCode = receiptType.getZformCode().toString();
                }
                cusType = receiptType.getCusType();
            }

            TInput tInput = new TInput();
            tInput.setBLDAT(bpmExMDetailVo.getCertificateDate().replace("-", ""));
            tInput.setVATDATE(bpmExMDetailVo.getCertificateDate().replace("-", ""));
            tInput.setXBLNR((bpmExMDetailVo.getCertificateCode()));
            tInput.setZFORM_CODE(ZformCode);
            tInput.setSTCEG(bpmExMDetailVo.getUniformNum());
            tInput.setHWBAS(bpmExMDetailVo.getUntaxAmount().toString());
            tInput.setHWSTE(bpmExMDetailVo.getTax().toString());
            tInput.setTAX_TYPE("1");
            tInput.setCUS_TYPE(cusType);
            tInput.setAM_TYPE("1");
            tInputList.add(tInput);
        }
        return tInputList;
    }

    private List<TInput> buildTInputListByBpmExD(List<BpmExMD1> bpmExMD1List) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<NcccReceiptType> ncccReceiptTypeList = ncccReceiptTypeRepository.findAll();

        List<TInput> tInputList = new ArrayList<>();
        for (BpmExMD1 bpmExMD1 : bpmExMD1List) {
            if (bpmExMD1.getTaxCode() == null || GUI_SKIP.contains(bpmExMD1.getTaxCode())) {
                // V0 V9不進GUI
                continue;
            }
            String ZformCode = "";
            String cusType = "";
            Optional<NcccReceiptType> foundReceiptType = ncccReceiptTypeList.stream()
                    .filter(x -> x.getIntyp().equals(bpmExMD1.getCertificateType()))
                    .findFirst();
            if (foundReceiptType.isPresent()) {
                NcccReceiptType receiptType = foundReceiptType.get();
                if (receiptType.getZformCode() != null) {
                    ZformCode = receiptType.getZformCode().toString();
                }
                cusType = receiptType.getCusType();
            }

            TInput tInput = new TInput();
            tInput.setBLDAT(bpmExMD1.getCertificateDate().replace("-", ""));
            tInput.setVATDATE(bpmExMD1.getCertificateDate().replace("-", ""));
            tInput.setXBLNR((bpmExMD1.getCertificateCode()));
            tInput.setZFORM_CODE(ZformCode);
            tInput.setSTCEG(bpmExMD1.getUniformNum());
            tInput.setHWBAS(bpmExMD1.getUntaxAmount().toString());
            tInput.setHWSTE(bpmExMD1.getTax().toString());
            tInput.setTAX_TYPE("1");
            tInput.setCUS_TYPE(cusType);
            tInput.setAM_TYPE("1");
            tInputList.add(tInput);
        }
        return tInputList;
    }
    // endregion

    // region 預算動用退回

    //退回預算
    private void returnBudgetTranscation(String exNo)
    {
        BpmExM bpmExM = bpmExMRepository.findByExNo(exNo);

        List<BudgetVo.BudgetTranscation> budgetTranscationList =  new ArrayList<>();

        // 董監事及研發委員
        if(bpmExM.getApplyType().equals("7") || bpmExM.getApplyType().equals("8"))
        {
            List<BpmExPsD> bpmExPsDList = bpmExPsDRepository.findByExNo(exNo);

            for(BpmExPsD bpmExPsD : bpmExPsDList)
            {
                // region 預算動用

                if(bpmExPsD.getAccounting1() != null && !bpmExPsD.getAccounting1().isEmpty() && bpmExPsD.getAmount1().compareTo(BigDecimal.ZERO) > 0)
                {
                    BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                    budgetTranscation.setYear(bpmExPsD.getYear());
                    budgetTranscation.setVersion("2");
                    budgetTranscation.setOuCode(bpmExPsD.getOuCode());
                    budgetTranscation.setAccounting(bpmExPsD.getAccounting1());
                    budgetTranscation.setAmount(bpmExPsD.getAmount1().multiply(new BigDecimal(-1)));
                    budgetTranscation.setTranscationSource("費用申請單");
                    budgetTranscation.setTranscationNo(exNo);
                    budgetTranscation.setTranscationDate(LocalDate.now());
                    budgetTranscation.setTranscationType("動用");
                    budgetTranscation.setBpNo(bpmExPsD.getPplId());
                    budgetTranscation.setBpName(bpmExPsD.getName());
                    budgetTranscation.setDocNo("");
                    budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                    budgetTranscation.setCreateDate(LocalDateTime.now());
                    budgetTranscationList.add(budgetTranscation);
                }

                if(bpmExPsD.getAccounting2() != null && !bpmExPsD.getAccounting2().isEmpty() && bpmExPsD.getAmount2().compareTo(BigDecimal.ZERO) > 0)
                {
                    BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                    budgetTranscation.setYear(bpmExPsD.getYear());
                    budgetTranscation.setVersion("2");
                    budgetTranscation.setOuCode(bpmExPsD.getOuCode());
                    budgetTranscation.setAccounting(bpmExPsD.getAccounting2());
                    budgetTranscation.setAmount(bpmExPsD.getAmount2().multiply(new BigDecimal(-1)));
                    budgetTranscation.setTranscationSource("費用申請單");
                    budgetTranscation.setTranscationNo(exNo);
                    budgetTranscation.setTranscationDate(LocalDate.now());
                    budgetTranscation.setTranscationType("動用");
                    budgetTranscation.setBpNo(bpmExPsD.getPplId());
                    budgetTranscation.setBpName(bpmExPsD.getName());
                    budgetTranscation.setDocNo("");
                    budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                    budgetTranscation.setCreateDate(LocalDateTime.now());
                    budgetTranscationList.add(budgetTranscation);
                }

                if(bpmExPsD.getAccounting3() != null && !bpmExPsD.getAccounting3().isEmpty() && bpmExPsD.getAmount3().compareTo(BigDecimal.ZERO) > 0)
                {
                    BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                    budgetTranscation.setYear(bpmExPsD.getYear());
                    budgetTranscation.setVersion("2");
                    budgetTranscation.setOuCode(bpmExPsD.getOuCode());
                    budgetTranscation.setAccounting(bpmExPsD.getAccounting3());
                    budgetTranscation.setAmount(bpmExPsD.getAmount3().multiply(new BigDecimal(-1)));
                    budgetTranscation.setTranscationSource("費用申請單");
                    budgetTranscation.setTranscationNo(exNo);
                    budgetTranscation.setTranscationDate(LocalDate.now());
                    budgetTranscation.setTranscationType("動用");
                    budgetTranscation.setBpNo(bpmExPsD.getPplId());
                    budgetTranscation.setBpName(bpmExPsD.getName());
                    budgetTranscation.setDocNo("");
                    budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                    budgetTranscation.setCreateDate(LocalDateTime.now());
                    budgetTranscationList.add(budgetTranscation);
                }

                if(bpmExPsD.getAccounting4() != null && !bpmExPsD.getAccounting4().isEmpty() && bpmExPsD.getAmount4().compareTo(BigDecimal.ZERO) > 0)
                {
                    BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                    budgetTranscation.setYear(bpmExPsD.getYear());
                    budgetTranscation.setVersion("2");
                    budgetTranscation.setOuCode(bpmExPsD.getOuCode());
                    budgetTranscation.setAccounting(bpmExPsD.getAccounting4());
                    budgetTranscation.setAmount(bpmExPsD.getAmount4().multiply(new BigDecimal(-1)));
                    budgetTranscation.setTranscationSource("費用申請單");
                    budgetTranscation.setTranscationNo(exNo);
                    budgetTranscation.setTranscationDate(LocalDate.now());
                    budgetTranscation.setTranscationType("動用");
                    budgetTranscation.setBpNo(bpmExPsD.getPplId());
                    budgetTranscation.setBpName(bpmExPsD.getName());
                    budgetTranscation.setDocNo("");
                    budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                    budgetTranscation.setCreateDate(LocalDateTime.now());
                    budgetTranscationList.add(budgetTranscation);
                }

                // endregion 預算動用
            }
        }
        else
        {
            // (零用金與預付借支不會動到預算)
            if(!bpmExM.getApplyType().equals("2") && !bpmExM.getApplyType().equals("5"))
            {
                List<BpmExMD1> bpmExMD1List = bpmExMD1Repository.findByExNo(exNo);

                for(BpmExMD1 bpmExMD1 : bpmExMD1List)
                {
                    // region 預算動用
                    // 若沒有多重分攤預算動用 
                    if(bpmExMD1.getMultiShare().equals("N"))
                    {
                        BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                        budgetTranscation.setYear(bpmExMD1.getYear());
                        budgetTranscation.setVersion("2");
                        budgetTranscation.setOuCode(bpmExMD1.getOuCode());
                        budgetTranscation.setAccounting(bpmExMD1.getAccounting());
                        budgetTranscation.setAmount(bpmExMD1.getUntaxAmount().multiply(new BigDecimal(-1)));
                        budgetTranscation.setTranscationSource("費用申請單");
                        budgetTranscation.setTranscationNo(exNo);
                        budgetTranscation.setTranscationDate(LocalDate.now());
                        budgetTranscation.setTranscationType("動用");
                        budgetTranscation.setBpNo(bpmExM.getEmpNo());
                        budgetTranscation.setBpName(bpmExM.getEmpNo());
                        budgetTranscation.setDocNo(bpmExM.getApprovalNo());
                        budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                        budgetTranscation.setCreateDate(LocalDateTime.now());
                        budgetTranscationList.add(budgetTranscation);
                    }
                    else
                    {
                        List<BpmSplitM> bpmSplitMList = bpmSplitMRepository.findByExNoAndExItemNo(exNo,bpmExMD1.getExItemNo());

                        for(BpmSplitM bpmSplitM : bpmSplitMList)
                        {
                            BudgetVo.BudgetTranscation budgetTranscation = new BudgetVo.BudgetTranscation();
                            budgetTranscation.setYear(bpmSplitM.getYear());
                            budgetTranscation.setVersion("2");
                            budgetTranscation.setOuCode(bpmSplitM.getOuCode());
                            budgetTranscation.setAccounting(bpmSplitM.getAccounting());
                            budgetTranscation.setAmount(bpmSplitM.getUntaxAmount().multiply(new BigDecimal(-1)));
                            budgetTranscation.setTranscationSource("費用申請單");
                            budgetTranscation.setTranscationNo(exNo);
                            budgetTranscation.setTranscationDate(LocalDate.now());
                            budgetTranscation.setTranscationType("動用");
                            budgetTranscation.setBpNo(bpmExM.getEmpNo());
                            budgetTranscation.setBpName(bpmExM.getEmpNo());
                            budgetTranscation.setDocNo(bpmExM.getApprovalNo());
                            budgetTranscation.setCreateUser(bpmExM.getCreatedUser());
                            budgetTranscation.setCreateDate(LocalDateTime.now());
                            budgetTranscationList.add(budgetTranscation);
                        }


                    }

                    // endregion
                }
            }
        }

        // region 預算動用寫入

        for(BudgetVo.BudgetTranscation budgetTranscation : budgetTranscationList)
        {
            ncccBudgetService.WriteBudgetTranscation(budgetTranscation);
        }

        // endregion
    }

    // endregion

}
