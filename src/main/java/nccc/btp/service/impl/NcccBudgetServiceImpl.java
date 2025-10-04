package nccc.btp.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import nccc.btp.dto.*;
import nccc.btp.entity.*;
import nccc.btp.enums.AssigneeRole;
import nccc.btp.enums.ProcessDefinitionKey;
import nccc.btp.enums.Decision;
import nccc.btp.enums.Mode;
import nccc.btp.repository.*;
import nccc.btp.rfc.SapUtil;
import nccc.btp.rfc.ZHeader;
import nccc.btp.rfc.ZItem;
import nccc.btp.rfc.ZReturn;
import nccc.btp.service.FlowableService;
import nccc.btp.util.MoneyUtil;
import nccc.btp.vo.AssignTasksVo;
import nccc.btp.vo.DecisionVo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.var;
import nccc.btp.dto.BudgetRequestDto.DecisionInfo;
import nccc.btp.dto.BudgetRequestDto.DownloadBudgetRuleFile;
import nccc.btp.dto.BudgetRequestDto.QueryBudgetExpense;
import nccc.btp.dto.BudgetRequestDto.QueryBudgetPurchaseOrder;
import nccc.btp.dto.BudgetRequestDto.QueryBudgetRequestOrder;
import nccc.btp.dto.BudgetRequestDto.QueryBudgetTransaction;
import nccc.btp.dto.BudgetResponseibilityAllocationDto.QueryBudgetAllocationSAPData;
import nccc.btp.dto.BudgetResponseibilityAllocationDto.ReqDetailByPoNo;
import nccc.btp.dto.BudgetResponseibilityAllocationDto.transferAllocationToSAPReq;
import nccc.btp.dto.BudgetRuleDTO.BudgetDetail;
import nccc.btp.service.NcccBudgetService;
import nccc.btp.util.SecurityUtil;
import nccc.btp.vo.BudgetVo;
import nccc.btp.vo.BudgetVo.NcccAllocationSAPData;
import nccc.btp.vo.BudgetVo.OpenBudgetReserveData;
import nccc.btp.vo.BudgetVo.OpenBudgetReserveView;
import nccc.btp.vo.SyncOUVo;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

@Service
@Transactional
public class NcccBudgetServiceImpl implements NcccBudgetService {

    protected static Logger LOG = LoggerFactory.getLogger(NcccBudgetServiceImpl.class);

    @Autowired
    private FlowableService flowableService;

    @Autowired
    private SyncUserRepository syncUserRepository;

    @Autowired
    private SyncOURepository syncOURepository;

    /**
     * 會計科目
     */
    @Autowired
    private NcccAccountingListRepository ncccAccountingListRepository;

    /**
     * 預算作業項目
     */
    @Autowired
    private NcccOperateItemsRepository ncccOperateItemsRepository;

    /**
     * 預算版本
     */
    @Autowired
    private NcccBudgetVersionRepository ncccBudgetVersionRepository;

    /**
     * 預算分攤規則主檔
     */
    @Autowired
    private NcccApportionmentRuleMRepository ncccApportionmentRuleMRepository;

    /**
     * 預算分攤規則明細
     */
    @Autowired
    private NcccApportionmentRuleDRepository ncccApportionmentRuleDRepository;

    /**
     * 預算編列_預算主檔
     */
    @Autowired
    private NcccPreBudgetMRepository ncccPreBudgetMRepository;

    /**
     * 預算編列_明細
     */
    @Autowired
    private NcccPreBudgetDRepository ncccPreBudgetDRepository;

    /**
     * 預算編列_作業項目明細
     */
    @Autowired
    private NcccPreBudgetD1Repository ncccPreBudgetD1Repository;

    /**
     * 預算主檔
     */
    @Autowired
    private NcccBudgetMRepository ncccBudgetMRepository;

    /**
     * 預算主檔_明細
     */
    @Autowired
    private NcccBudgetDRepository ncccBudgetDRepository;

    /**
     * 預算主檔_交易明細
     */
    @Autowired
    private NcccBudgetTranscationRepository ncccBudgetTranscationRepository;

    /**
     * 預算調撥_主檔
     */
    @Autowired
    private NcccBudgetAdjustMRepository ncccBudgetAdjustMRepository;

    /**
     * 預算調撥_明細
     */
    @Autowired
    private NcccBudgetAdjustDRepository ncccBudgetAdjustDRepository;

    /**
     * 預算調撥_作業項目明細
     */
    @Autowired
    private NcccBudgetAdjustD1Repository ncccBudgetAdjustD1Repository;

    /**
     * 預算保留_主檔
     */
    @Autowired
    private NcccBudgetReserveMRepository ncccBudgetReserveMRepository;

    /**
     * 預算保留_明細
     */
    @Autowired
    private NcccBudgetReserveDRepository ncccBudgetReserveDRepository;

    /**
     * 預算提列費用_主檔
     */
    @Autowired
    private NcccProvisionExpenseMRepository ncccProvisionExpenseMRepository;

    /**
     * 預算提列費用_明細
     */
    @Autowired
    private NcccProvisionExpenseDRepository ncccProvisionExpenseDRepository;

    /**
     * 預算提列費用_作業項目明細
     */
    @Autowired
    private NcccProvisionExpenseD1Repository ncccProvisionExpenseD1Repository;

    /**
     * 權責分攤_主檔
     */
    @Autowired
    private NcccAllocationMRepository ncccAllocationMRepository;

    /**
     * 權責分攤_明細
     */
    @Autowired
    private NcccAllocationDRepository ncccAllocationDRepository;

    /**
     * 權責分攤_作業項目明細檔
     */
    @Autowired
    private NcccAllocationD1Repository ncccAllocationD1Repository;

    /**
     * 權責分攤_調整主檔
     */
    @Autowired
    private NcccAllocationAdjMRepository ncccAllocationAdjMRepository;

    /**
     * 權責分攤_調整明細
     */
    @Autowired
    private NcccAllocationAdjDRepository ncccAllocationAdjDRepository;

    /**
     * 權責分攤_作業項目明細
     */
    @Autowired
    private NcccAllocationAdjD1Repository ncccAllocationAdjD1Repository;

    /**
     * 權責分攤_期數明細檔
     */
    @Autowired
    private NcccAllocationAdjD2Repository ncccAllocationAdjD2Repository;

    /**
     * 任務查詢
     */
    @Autowired
    private BpmNcccMissionAssignerRepository bpmNcccMissionAssignerRepository;

    /**
     * 保留簽的成本經辦
     */
    @Autowired
    private NcccReserverControllerRepository ncccReserverControllerRepository;

    /**
     * 部門對應sap欄位
     */
    @Autowired
    private NcccCostcenterOrgMappingRepository ncccCostcenterOrgMappingRepository;

    @Autowired
    private SapUtil sapUtil;

    /**
     * 取得部門
     *
     * @return
     */
    @Override
    public BudgetResponse getAllSourceDepartment() {

        // 取得預算來源部門邏輯
        // 讀取 SyncOU 表中 預算部門
        List<SyncOU> budgetSourceDepartments = syncOURepository.findAll();

        // 產生所需的結構
        List<Map<String, String>> allDepartments = budgetSourceDepartments.stream()
                .map(department -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("code", department.getOuCode());
                    map.put("name", department.getOuName());
                    return map;
                })
                .collect(Collectors.toList());

        return new BudgetResponse(true, "取得預算來源部門成功", allDepartments);
    }

    /**
     * 取得預算來源部門
     *
     * @return
     */
    @Override
    public BudgetResponse getSourceDepartment() {

        // 取得預算來源部門邏輯
        // 讀取 SyncOU 表中 預算部門
        List<SyncOU> budgetSourceDepartments = syncOURepository.findBudgetOU();

        // 產生所需的結構
        List<Map<String, String>> allDepartments = budgetSourceDepartments.stream()
                .map(department -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("code", department.getOuCode());
                    map.put("name", department.getOuName());
                    return map;
                })
                .collect(Collectors.toList());

        return new BudgetResponse(true, "取得預算來源部門成功", allDepartments);
    }

    /**
     * 取得部門與經辦
     *
     * @return
     */
    @Override
    public BudgetResponse getOuManager() {

        // 取得部門與經辦
        List<SyncOUVo.OuManager> ouMangers = syncOURepository.getAllByMgrAccount();

        return new BudgetResponse(true, "取得部門與經辦成功", ouMangers);
    }

    /**
     * 取得預算部門底下經辦
     * @return
     */
    @Override
    public BudgetResponse getBudgetOuUnderAccount(){

        List<SyncOUVo.BudgetOuManager> allManagers = new ArrayList<>();

        List<String> ouCodeList = syncOURepository.findBudgetOUCodeList();

        for(String ouCode : ouCodeList)
        {
            allManagers.addAll(syncOURepository.getTargetOuUnderAccount(ouCode));
        }

        return new BudgetResponse(true, "取得部門與經辦成功", allManagers);
    }

    /**
     * 取得作業項目
     *
     * @return
     */
    @Override
    public BudgetResponse getOperateItems() {
        // 取得作業項目
        List<NcccOperateItems> operateItems = ncccOperateItemsRepository.findAll();

        // 產生所需的結構
        List<Map<String, String>> allItems = operateItems.stream()
                .map(item -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("code", item.getOperateItemCode());
                    map.put("name", item.getOperateItem());
                    return map;
                })
                .collect(Collectors.toList());

        return new BudgetResponse(true, "取得作業項目成功", allItems);
    }

    /**
     * 取得預算會計科目
     *
     * @return
     */
    @Override
    public BudgetResponse getBudgetAccount() {
        // 取得預算會計科目
        List<NcccAccountingList> budgetAccounts = ncccAccountingListRepository.findByBudget();

        // 產生所需的結構
        List<Map<String, String>> allAccounts = budgetAccounts.stream()
                .map(account -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("code", account.getSubject());
                    map.put("name", account.getEssay());
                    return map;
                })
                .collect(Collectors.toList());

        return new BudgetResponse(true, "取得預算會計科目成功", allAccounts);
    }

    /**
     * 查詢請購單
     * @param model
     * @return
     */
    @Override
    public BudgetResponse getRequestOrder(QueryBudgetRequestOrder model){
        // 查詢請購單
        List<BudgetVo.BudgetRequestOrderData> roData = ncccBudgetMRepository.GetRequestOrder(model.getYear());
        return new BudgetResponse(true, "取得請購單成功", roData);
    }

    /**
     * 查詢採購單
     * @param model
     * @return
     */
    @Override
    public BudgetResponse getPurchaseOrder(QueryBudgetPurchaseOrder model){
        // 查詢採購單
        List<BudgetVo.BudgetPurchaseOrderData> poData = ncccBudgetMRepository.GetPurchaseOrder(model.getYear());
        return new BudgetResponse(true, "取得採購單成功", poData);
    }

    /**
     * 查詢預算調撥採購單
     * @param model
     * @return
     */
    @Override
    public BudgetResponse getBudgetManagementPurchaseOrder(QueryBudgetPurchaseOrder model)
    {
        List<BudgetVo.BudgetManagementPurchaseOrderIncludeDetailData> poData = ncccBudgetMRepository.GetBudgetManagementPurchaseOrder(model.getYear());
        return new BudgetResponse(true, "取得採購單成功", poData);
    }

    // region 預算版次

    /**
     * 查詢預算版次
     *
     * @param year
     * @return
     */
    @Override
    public BudgetResponse getBudgetVersion(String year) {

        BudgetGetYearInfo budgetGetYearInfo = new BudgetGetYearInfo();

        // 查詢所有預算版次
        List<NcccBudgetVersion> budgetVersions = ncccBudgetVersionRepository.findAllByYear(year);
        if (budgetVersions.isEmpty()) {
            budgetGetYearInfo.version1Exists = false;
            budgetGetYearInfo.version2Exists = false;
        }

        // 檢查版次1是否存在
        budgetGetYearInfo.version1Exists = budgetVersions.stream()
                .anyMatch(version -> version.getVersion().equals("1"));

        // 檢查版次2是否存在
        budgetGetYearInfo.version2Exists = budgetVersions.stream()
                .anyMatch(version -> version.getVersion().equals("2"));

        // 回傳結果
        BudgetResponse budgetResponse = new BudgetResponse(true, "查詢預算版次成功", budgetGetYearInfo);
        return budgetResponse;
    }

    /**
     * 查詢預算當前版次
     *
     * @param year
     * @return
     */
    @Override
    public BudgetResponse getBudgetCurrentVersion(String year) {

        BudgetResponse getVersion = getBudgetVersion(year);
        BudgetGetYearInfo budgetGetYearInfo = (BudgetGetYearInfo) getVersion.data;

        String currentVersion = budgetGetYearInfo.version2Exists ? "2" : "1";

        // 回傳結果
        BudgetResponse budgetResponse = new BudgetResponse(true, "查詢預算當前版次成功", currentVersion);
        return budgetResponse;
    }

    /**
     * 建立預算版次
     *
     * @param model
     * @return
     */
    @Override
    public BudgetResponse createBudgetYear(BudgetRequestDto.YearVersion model) {

        // 先檢查版次是否存在
        if (ncccBudgetVersionRepository.existsByYearAndVersion(model.getYear(), model.getVersion())) {
            return new BudgetResponse(false, "已經有該預算年度,預算版次");
        }

        NcccUserDto user = SecurityUtil.getCurrentUser();

        // 建立新的預算版次
        NcccBudgetVersion budgetVersion = new NcccBudgetVersion();
        budgetVersion.setYear(model.getYear());
        budgetVersion.setVersion(model.getVersion());
        budgetVersion.setCreateDate(LocalDate.now());
        budgetVersion.setCreateUser(user.getUserId());

        ncccBudgetVersionRepository.save(budgetVersion);
        return new BudgetResponse(true, "預算年度,版次建立成功");
    }

    /**
     * 複製預算版次
     *
     * @param model
     * @return
     */
    @Override
    public BudgetResponse copyBudgetYear(BudgetRequestDto.CopyYear model) {

        // 先檢查來源版次是否存在
        if (!ncccBudgetVersionRepository
                .existsByYearAndVersion(model.getSourceYear(), model.getSourceVersion())) {
            return new BudgetResponse(false, "沒有可複預算年度,版次資料");
        }

        // 檢查目標版次是否已存在
        if (ncccBudgetVersionRepository
                .existsByYearAndVersion(model.getTargetYear(), model.getTargetVersion())) {
            return new BudgetResponse(false, "已經有該預算年度,版次");
        }

        NcccUserDto user = SecurityUtil.getCurrentUser();

        // 複製預算版次
        NcccBudgetVersion targetVersion = new NcccBudgetVersion();
        targetVersion.setYear(model.getTargetYear());
        targetVersion.setVersion(model.getTargetVersion());
        targetVersion.setCreateDate(LocalDate.now());
        targetVersion.setCreateUser(user.getUserId());

        ncccBudgetVersionRepository.save(targetVersion);

        /**
         *
         * 複製其他東西 ex.人事費 預算編列
         * 請自行發揮O_<
         */

        //查詢版本1的預算編列複製到版本2
        List<NcccPreBudgetM> preBudgetMs = ncccPreBudgetMRepository.findByYearAndVersion(model.getSourceYear(), model.getSourceVersion());
        List<NcccPreBudgetM> newPreBudgetMs = new ArrayList<>();
        List<NcccPreBudgetD> newPreBudgetDs = new ArrayList<>();
        List<NcccPreBudgetD1> newPreBudgetD1s = new ArrayList<>();

        //新單號
        String prefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));
        Integer nextSerial = ncccPreBudgetMRepository.findMaxSerialByPrefix(prefix) + 1;
        for (NcccPreBudgetM preBudgetM : preBudgetMs) {

            NcccPreBudgetM  newPreBudgetM = new NcccPreBudgetM();

            String serialStr = String.format("%04d", nextSerial);
            String budgetNo = "BU" + prefix + serialStr;
            newPreBudgetM.setBudgetNo(budgetNo);
            newPreBudgetM.setYear(model.getSourceYear());
            newPreBudgetM.setVersion(model.getTargetVersion());
            newPreBudgetM.setApplyOuCode(preBudgetM.getApplyOuCode());
            newPreBudgetM.setApplyUser(preBudgetM.getApplyUser());
            newPreBudgetM.setApplyDate(preBudgetM.getApplyDate());
            newPreBudgetM.setCreateDate(LocalDate.now());
            newPreBudgetM.setCreateUser(preBudgetM.getCreateUser());
            newPreBudgetM.setAssignment("1");
            newPreBudgetMs.add(newPreBudgetM);

            List<NcccPreBudgetD> preBudgetDs = ncccPreBudgetDRepository.findByBudgetNo(preBudgetM.getBudgetNo());

            for(NcccPreBudgetD  preBudgetD : preBudgetDs)
            {
                NcccPreBudgetD newPreBudgetD = new NcccPreBudgetD();
                newPreBudgetD.setBudgetNo(budgetNo);
                newPreBudgetD.setSeqNo(preBudgetD.getSeqNo());
                newPreBudgetD.setAccounting(preBudgetD.getAccounting());
                newPreBudgetD.setBudAmount(preBudgetD.getBudAmount());
                newPreBudgetD.setRemark(preBudgetD.getRemark());
                newPreBudgetD.setCreateDate(LocalDate.now());
                newPreBudgetD.setCreateUser(preBudgetD.getCreateUser());
                newPreBudgetD.setUpdateDate(LocalDate.now());
                newPreBudgetD.setUpdateUser(preBudgetD.getUpdateUser());
                newPreBudgetDs.add(newPreBudgetD);
            }

            List<NcccPreBudgetD1> preBudgetD1s = ncccPreBudgetD1Repository.findByBudgetNo(preBudgetM.getBudgetNo());

            for(NcccPreBudgetD1 preBudgetD1 :preBudgetD1s)
            {
                NcccPreBudgetD1 newPreBudgetD1 = new NcccPreBudgetD1();
                newPreBudgetD1.setBudgetNo(budgetNo);
                newPreBudgetD1.setSeqNo(preBudgetD1.getSeqNo());
                newPreBudgetD1.setSeqNo1(preBudgetD1.getSeqNo1());
                newPreBudgetD1.setOperateItemCode(preBudgetD1.getOperateItemCode());
                newPreBudgetD1.setOperateItem(preBudgetD1.getOperateItem());
                newPreBudgetD1.setOperateAmt(preBudgetD1.getOperateAmt());
                newPreBudgetD1.setOperateRatio(preBudgetD1.getOperateRatio());
                newPreBudgetD1.setCreateDate(LocalDate.now());
                newPreBudgetD1.setCreateUser(preBudgetD1.getCreateUser());
                newPreBudgetD1.setUpdateDate(LocalDate.now());
                newPreBudgetD1.setUpdateUser(preBudgetD1.getUpdateUser());
                newPreBudgetD1s.add(newPreBudgetD1);
            }

            nextSerial++;
        }
        ncccPreBudgetMRepository.saveAll(newPreBudgetMs);
        ncccPreBudgetDRepository.saveAll(newPreBudgetDs);
        ncccPreBudgetD1Repository.saveAll(newPreBudgetD1s);

        return new BudgetResponse(true, "預算年度,版次複製成功");

    }
    // endregion 預算版次

    // region 部門分攤

    /**
     * 取得部門分攤明細資料
     *
     * @param model
     * @return
     */
    @Override
    public BudgetResponse getExistingRuleData(BudgetGetExistingRuleInfo model) {

        // 輸出模型
        BudgetApportionmentRuleDTO result = new BudgetApportionmentRuleDTO();

        // 取得預算來源部門邏輯
        // 讀取 SyncOU 表中的所有部門
        List<SyncOU> budgetSourceDepartments = syncOURepository.findBudgetOU();

        // 產生明細資料
        result.setDetails(budgetSourceDepartments.stream()
                .map(department -> {
                    BudgetApportionmentRuleDTO.DetailDTO detail = new BudgetApportionmentRuleDTO.DetailDTO();
                    detail.setOuCode(department.getOuCode());
                    detail.setOuName(department.getOuName());
                    detail.setUnitQty(BigDecimal.ZERO); // 預設單位數量為0
                    detail.setRemark(""); // 預設備註為空
                    return detail;
                })
                .collect(Collectors.toList()));

        // 根據 year, ruleId, accounting, belongOuCode 查詢主檔資料
        NcccApportionmentRuleM ruleM = ncccApportionmentRuleMRepository
                .findByYearAndMonthAndAccounting(model.year,model.month, model.accounting);
        if (ruleM != null) {
            // 將主檔資料填入結果模型
            result.getMain().setYear(ruleM.getYear());
            result.getMain().setDescription(ruleM.getDescription());
            result.getMain().setMonth(ruleM.getMonth());
            result.getMain().setAccounting(ruleM.getAccounting());
            result.getMain().setSubject(ruleM.getSubject());
            result.getMain().setBelongOuCode(ruleM.getBelongOuCode());
            result.getMain().setBelongOuName(ruleM.getBelongOuName());
            result.getMain().setActualQtyFlag(ruleM.getActualQtyFlag().equals("Y"));
            result.getMain().setOperationType(ruleM.getOperationType());
            // 如果有主檔資料，則查詢對應的明細資料
            List<NcccApportionmentRuleD> ruleD = ncccApportionmentRuleDRepository.findByYearAndMonthAndAccounting(ruleM.getYear(), ruleM.getMonth(), ruleM.getAccounting());
            if (ruleD.isEmpty() == false) {
                // 以allDepartments為主，將明細資料更新到對應的部門
                for (NcccApportionmentRuleD detail : ruleD) {
                    for (BudgetApportionmentRuleDTO.DetailDTO department : result.getDetails()) {
                        if (department.getOuCode().equals(detail.getOuCode())) {
                            department.setUnitQty(detail.getUnitQty());
                            department.setRemark(detail.getRemark());
                        }
                    }
                }
            }
        }

        return new BudgetResponse(true, "取得明細資料成功", result);
    }

    /**
     * 取得最近一筆明細資料
     *
     * @param model
     * @return
     */
    @Override
    public BudgetResponse getLastExistingRuleData(BudgetGetExistingRuleInfo model) {

        // 輸出模型
        BudgetApportionmentRuleDTO result = new BudgetApportionmentRuleDTO();

        String year = model.getYear();

        if(model.getMonth().equals("00"))
        {
            year = String.valueOf(Integer.parseInt(year)-1);
        }

        // 根據 year, 查詢最近一筆主檔資料
        NcccApportionmentRuleM ruleM = ncccApportionmentRuleMRepository
                .findLatestByYearAndAccounting(year, model.accounting);

        if (ruleM == null) {
            return new BudgetResponse(false, "沒有可複製的上一筆資料");
        } else {

            // 取得預算來源部門邏輯
            // 讀取 SyncOU 表中的所有部門
            List<SyncOU> budgetSourceDepartments = syncOURepository.findBudgetOU();

            // 產生明細資料
            result.setDetails(budgetSourceDepartments.stream()
                    .map(department -> {
                        BudgetApportionmentRuleDTO.DetailDTO detail = new BudgetApportionmentRuleDTO.DetailDTO();
                        detail.setOuCode(department.getOuCode());
                        detail.setOuName(department.getOuName());
                        detail.setUnitQty(BigDecimal.ZERO); // 預設單位數量為0
                        detail.setRemark(""); // 預設備註為空
                        return detail;
                    })
                    .collect(Collectors.toList()));

            // 將主檔資料填入結果模型
            result.getMain().setYear(ruleM.getYear());
            result.getMain().setDescription(ruleM.getDescription());
            result.getMain().setMonth(ruleM.getMonth());
            result.getMain().setAccounting(ruleM.getAccounting());
            result.getMain().setSubject(ruleM.getSubject());
            result.getMain().setBelongOuCode(ruleM.getBelongOuCode());
            result.getMain().setBelongOuName(ruleM.getBelongOuName());
            result.getMain().setActualQtyFlag(ruleM.getActualQtyFlag().equals("Y"));
            result.getMain().setOperationType(ruleM.getOperationType());
            // 如果有主檔資料，則查詢對應的明細資料
            List<NcccApportionmentRuleD> ruleD = ncccApportionmentRuleDRepository.findByYearAndMonthAndAccounting(ruleM.getYear(), ruleM.getMonth(),ruleM.getAccounting());
            if (ruleD.isEmpty() == false) {
                // 以allDepartments為主，將明細資料更新到對應的部門
                for (NcccApportionmentRuleD detail : ruleD) {
                    for (BudgetApportionmentRuleDTO.DetailDTO department : result.getDetails()) {
                        if (department.getOuCode().equals(detail.getOuCode())) {
                            department.setUnitQty(detail.getUnitQty());
                            department.setRemark(detail.getRemark());
                        }
                    }
                }
            }
        }

        return new BudgetResponse(true, "取得明細資料成功", result);
    }

    /**
     * 儲存部門分攤規則資料
     *
     * @param model
     * @return
     */
    @Transactional
    @Override
    public BudgetResponse saveApportionmentRule(BudgetApportionmentRuleDTO model) {
        try {
            NcccUserDto user = SecurityUtil.getCurrentUser();

            // 檢查是否有主檔資料
            NcccApportionmentRuleM ruleM = ncccApportionmentRuleMRepository
                    .findByYearAndMonthAndAccounting(
                            model.getMain().getYear(),
                            model.getMain().getMonth(),
                            model.getMain().getAccounting());

            if (ruleM == null) {
                // 如果沒有主檔資料，則建立新的主檔
                ruleM = new NcccApportionmentRuleM();
                ruleM.setYear(model.getMain().getYear());
                ruleM.setAccounting(model.getMain().getAccounting());
                ruleM.setBelongOuCode(model.getMain().getBelongOuCode());
                ruleM.setBelongOuName(model.getMain().getBelongOuName());
                ruleM.setCreateDate(LocalDate.now());
                ruleM.setCreateUser(user.getUserId());
            }

            // 更新主檔資料
            ruleM.setSubject(model.getMain().getSubject());
            ruleM.setMonth(model.getMain().getMonth());
            ruleM.setDescription(model.getMain().getDescription());
            ruleM.setActualQtyFlag(model.getMain().isActualQtyFlag()?"Y":"N");
            ruleM.setOperationType(model.getMain().getOperationType());
            ruleM.setUpdateDate(LocalDate.now());
            ruleM.setUpdateUser(user.getUserId());
            ncccApportionmentRuleMRepository.save(ruleM);

            // 清除舊的明細資料
            ncccApportionmentRuleDRepository.deleteByYearAndMonthAndAccounting(ruleM.getYear(), ruleM.getMonth(),ruleM.getAccounting());

            // 儲存明細資料
            List<NcccApportionmentRuleD> details = new ArrayList<>();
            for (BudgetApportionmentRuleDTO.DetailDTO detail : model.getDetails()) {
                NcccApportionmentRuleD ruleD = new NcccApportionmentRuleD();
                ruleD.setYear(ruleM.getYear());
                ruleD.setMonth(ruleM.getMonth());
                ruleD.setAccounting(ruleM.getAccounting());
                ruleD.setOuCode(detail.getOuCode());
                ruleD.setOuName(detail.getOuName());
                ruleD.setUnitQty(detail.getUnitQty());
                ruleD.setRemark(detail.getRemark());
                ruleD.setCreateDate(LocalDate.now());
                ruleD.setCreateUser(user.getUserId());
                ruleD.setUpdateDate(LocalDate.now());
                ruleD.setUpdateUser(user.getUserId());
                details.add(ruleD);
            }

            ncccApportionmentRuleDRepository.saveAll(details);

            return new BudgetResponse(true, "部門分攤規則資料儲存成功");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
            LOG.error("儲存部門分攤規則資料失敗", e);
            return new BudgetResponse(false, "儲存部門分攤規則資料失敗: " + e.getMessage());
        }
    }
    // endregion 部門分攤

    // region 預算編列

    /**
     * 取得預算編列列表
     *
     * @param model
     * @return
     */
    @Override
    public BudgetResponse getBudgetRuleList(BudgetGetRuleListDto.BudgetGetRuleSearch model) {
        try {
            // 輸出模型
            BudgetGetRuleListDto responseDto = new BudgetGetRuleListDto();

            // 檢查輸入參數
            if (model == null) {
                return new BudgetResponse(false, "查詢條件不能為空");
            }

            // 查詢預算編列
            List<NcccPreBudgetM> budgetMList = ncccPreBudgetMRepository.findByYearAndVersion(model.year, model.version);

            // 根據日期範圍過濾
            if (model.dateFrom != null) {
                budgetMList = budgetMList.stream()
                        .filter(budgetM -> {
                            LocalDate applyDate = budgetM.getApplyDate();

                            return applyDate.isAfter(model.dateFrom) || applyDate.isEqual(model.dateFrom);
                        })
                        .collect(Collectors.toList());
            }
            if (model.dateTo != null) {
                budgetMList = budgetMList.stream()
                        .filter(budgetM -> {
                            LocalDate applyDate = budgetM.getApplyDate();
                            return applyDate.isBefore(model.dateTo) || applyDate.isEqual(model.dateTo);
                        })
                        .collect(Collectors.toList());
            }
            // 根據部門代碼過濾
            if (model.department != null && !model.department.isEmpty()) {
                budgetMList = budgetMList.stream()
                        .filter(budgetM -> budgetM.getApplyOuCode().equals(model.department))
                        .collect(Collectors.toList());
            }

            List<BudgetGetRuleListDto.BudgetGetRuleListData> datas =  new ArrayList<>();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


            for (NcccPreBudgetM budgetM : budgetMList) {

                BudgetGetRuleListDto.BudgetGetRuleListData budgetMDto =  new BudgetGetRuleListDto.BudgetGetRuleListData();

                budgetMDto.budgetNo = budgetM.getBudgetNo();
                budgetMDto.applyDate = budgetM.getApplyDate().format(formatter);
                budgetMDto.applyUser = budgetM.getApplyUser();
                SyncUser user = syncUserRepository.findByAccount(budgetM.getApplyUser());
                if (user != null) {
                    budgetMDto.applyUserName = user.getDisplayName();
                }
                budgetMDto.departmentCode = budgetM.getApplyOuCode();
                SyncOU ou = syncOURepository.findByOuCode(budgetM.getApplyOuCode());
                if (ou != null) {
                    budgetMDto.departmentName = ou.getOuName();
                }
                budgetMDto.budgetDepartmentCode = budgetM.getOuCode();
                SyncOU budgetOu = syncOURepository.findByOuCode(budgetM.getOuCode());
                if (budgetOu != null)
                {
                    budgetMDto.budgetDepartmentName = budgetOu.getOuName();
                }

                if(budgetM.getAssignDate() != null)
                {
                    budgetMDto.assignDate =  budgetM.getAssignDate().format(formatter);
                    SyncUser assignUser = syncUserRepository.findByAccount(budgetM.getAssignUser());
                    if (assignUser != null)
                    {
                        budgetMDto.assignUserName = assignUser.getDisplayName();
                    }
                }

                if(budgetM.getReviewDate() != null)
                {
                    budgetMDto.reviewDate =  budgetM.getReviewDate().format(formatter);
                    SyncUser reviewUser = syncUserRepository.findByAccount(budgetM.getReviewUser());
                    if (reviewUser != null)
                    {
                        budgetMDto.reviewUserName = reviewUser.getDisplayName();
                    }
                }

                budgetMDto.assignment = budgetM.getAssignment();

                datas.add(budgetMDto);

            }

            responseDto.data = datas;
            return new BudgetResponse(true, "取得預算編列列表成功", responseDto.data);
        } catch (Exception e) {
            LOG.error("取得預算編列列表失敗", e);
            return new BudgetResponse(false, "取得預算編列列表失敗: " + e.getMessage());
        }
    }

    /**
     * 上傳預算編列檔案
     * @param file
     * @param year
     * @param department
     * @return
     */
    @Override
    public BudgetResponse uploadBudgetRuleFile(MultipartFile file, String year,String department){
        try{
            List<BudgetDetail> dto = new ArrayList<>();

            // 檢查檔案是否為空
            if (file.isEmpty()) {
                return new BudgetResponse(false, "上傳檔案不能為空");
            }
            // 檢查檔案類型
            String contentType = file.getContentType();
            if (!"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
                return new BudgetResponse(false, "上傳檔案格式不正確，請上傳 Excel 檔案");
            }

            // 解析 Excel 檔案
            try (InputStream inputStream = file.getInputStream()) {

                Workbook workbook = new XSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0); // 讀取第一個工作表

                // 讀取表頭 (第4行) 以獲取動態欄位的數量
                Row headerRow = sheet.getRow(3); // 第4行 (索引為3)
                int dynamicFieldStartIndex = 5; // F欄的索引
                int totalColumns = headerRow.getPhysicalNumberOfCells(); // 總列數
                int dynamicFieldEndIndex = totalColumns - 2; // 扣掉O欄和P欄

                // 讀取年度 (A2)
                Row rowA2 = sheet.getRow(1);
                if (rowA2 == null || rowA2.getCell(0) == null) {
                    return new BudgetResponse(false, "年度資料不存在或格式不正確");
                }
                String yearData = getCellValueAsString(rowA2.getCell(0));
                if (!yearData.equals(year)) {
                    return new BudgetResponse(false, "年度資料不正確");
                }

                // 讀取資料 (從第5行開始)
                for (int i = 4; i <= sheet.getLastRowNum(); i++) { // 從第5行 (索引為4)
                    Row dataRow = sheet.getRow(i);
                    if (dataRow != null) {
                        // 假設每行的結構與 BudgetDetail 類的屬性一一對應
                        String depart = getCellValueAsString(dataRow.getCell(0));
                        String budgetCode = getCellValueAsString(dataRow.getCell(1));
                        //String budgetName = dataRow.getCell(2).getStringCellValue();
                        String description = getCellValueAsString(dataRow.getCell(3));
                        double amount = dataRow.getCell(4).getNumericCellValue();

                        // 檢查預算項目代碼是否存在
                        var budgetItem = ncccAccountingListRepository.findBySubject(budgetCode);
                        if (budgetItem == null) {
                            throw new Exception("第" + i + "行預算項目代碼 " + budgetCode + " 不存在");
                        }
                        // 檢查部門代碼是否存在
                        if (department.equals(depart) == false) {
                            throw new Exception("第" + i + "行部門代碼 " + depart + " 不正確");
                        }

                        // 讀取動態欄位名稱和值
                        Map<String, Double> dynamicFields = new HashMap<>();
                        for (int j = dynamicFieldStartIndex; j < dynamicFieldEndIndex; j++) { // 使用 < 而不是 <=
                            Cell cell = dataRow.getCell(j);
                            String fieldName = headerRow.getCell(j).getStringCellValue(); // 獲取欄位名稱
                            double fieldValue = (cell != null && cell.getCellType() == CellType.NUMERIC) ?
                                    cell.getNumericCellValue() : 0.0; // 如果是空白或非數字，預設為 0
                            dynamicFields.put(fieldName, fieldValue); // 將欄位名稱和值放入 Map
                        }
                        // 將資料加入到回應 DTO 中
                        var detail = new BudgetDetail();
                        detail.setBudgetItemCode(budgetItem.getSubject());
                        detail.setBudgetItemName(budgetItem.getEssay());
                        detail.setRemark(description);
                        detail.setBudgetAmount(BigDecimal.valueOf(amount));
                        //填入作業項目
                        List<BudgetRuleDTO.OperationItem> operationItems = new ArrayList<>();
                        for(var field : dynamicFields.entrySet()) {
                            String fieldName = field.getKey();
                            double fieldValue = field.getValue();

                            //如果值是 0 跳過
                            if (fieldValue == 0) {
                                continue;
                            }

                            var opItem = new BudgetRuleDTO.OperationItem();

                            var operateItem = ncccOperateItemsRepository.findByOperateItemName(fieldName);
                            if (operateItem == null) {
                                throw new Exception("第" + i + "行作業項目代碼 " + fieldName + " 不存在");
                            }

                            opItem.setCode(operateItem.getOperateItemCode());
                            opItem.setName(operateItem.getOperateItem());
                            // 金額為預算金額乘於Ratio 千位之後的數字四捨五入
                            // 這裡假設 Ratio 是百分比形式，需轉換為小數形式
                            opItem.setAmount(roundToNearestThousand((int) (amount * fieldValue / 100)));
                            // Ratio 直接使用 fieldValue
                            opItem.setRatio(BigDecimal.valueOf(fieldValue));
                            operationItems.add(opItem);
                        }
                        detail.setOperationItems(operationItems);

                        // operationItems內的RATIO加總起來必須等於100%
                        BigDecimal totalRatio = operationItems.stream()
                                .map(BudgetRuleDTO.OperationItem::getRatio)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        if (totalRatio.compareTo(BigDecimal.valueOf(100.00)) != 0) {
                            throw new Exception("第" + i + "行作業項目加總為 " + totalRatio + "%,不等於100%");
                        }

                        // 計算差異數=預算金額-作業項目加總金額
                        // 如果差異數不等於0,找出作業項目金額最大的明細-差異數
                        BigDecimal totalAmount = operationItems.stream()
                                .map(BudgetRuleDTO.OperationItem::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal difference = detail.getBudgetAmount().subtract(totalAmount);
                        if (difference.compareTo(BigDecimal.ZERO) != 0) {
                            var maxItem = operationItems.stream()
                                    .max(Comparator.comparing(BudgetRuleDTO.OperationItem::getAmount))
                                    .orElse(null);
                            if (maxItem != null) {
                                //如果difference是正值
                                //則從金額最大的作業項目中加上差異數
                                if (difference.compareTo(BigDecimal.ZERO) > 0) {
                                    maxItem.setAmount(maxItem.getAmount().add(difference));
                                }
                                //如果difference是負值
                                //則從金額最大的作業項目中減去差異數
                                else {
                                    maxItem.setAmount(maxItem.getAmount().subtract(difference));
                                }
                            }
                        }

                        // 將 detail 加入到 DTO 中
                        dto.add(detail);
                    }
                }
            }
            return new BudgetResponse(true, "上傳預算編列檔案成功", dto);

        }catch (Exception e) {
            LOG.error("上傳預算編列檔案失敗", e);
            return new BudgetResponse(false, "上傳預算編列檔案失敗: " + e.getMessage());
        }
    }

    /**
     * 將數值四捨五入到最接近的千位數
     * @param value
     * @return
     */
    private static BigDecimal roundToNearestThousand(int value) {
        // 將數值轉換為 BigDecimal
        BigDecimal bdValue = BigDecimal.valueOf(value);

        // 除以 1000，進行四捨五入，然後乘以 1000
        return bdValue.divide(BigDecimal.valueOf(1000), 0, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(1000));
    }

    /**
     * 取得儲存格的字串值
     * @param cell
     * @return
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return ""; // 返回空字串
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) Math.floor(cell.getNumericCellValue()));
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula(); // 處理公式的情況
            default:
                return ""; // 其他類型返回空字串
        }
    }

    /**
     * 下載預算編列
     */
    @Override
    public BudgetResponse downloadBudgetRuleFile(DownloadBudgetRuleFile model) {
        try {
            // 查詢資料集
            List<BudgetVo.BudgetItem> budgetMList = new ArrayList<>();

            if(model.getDepartment() != null && !model.getDepartment().equals(""))
            {
                budgetMList = ncccPreBudgetMRepository
                        .GetItemsByYearAndVersionAndOuCode(model.getYear(), model.getVersion(),model.getDepartment());
            }
            else
            {
                budgetMList = ncccPreBudgetMRepository
                        .GetItemsByYearAndVersion(model.getYear(), model.getVersion());
            }

            if (budgetMList.isEmpty()) {
                return new BudgetResponse(false, "沒有可下載的預算編列資料");
            }

            //取得作業項目
            var operateItems = ncccOperateItemsRepository.findAll();
            //開始動態填入欄位索引
            int cellIndex = 5;
            //編列索引
            Map<String, Integer> operateIndex = new HashMap<>();

            if(model.getIncludeZeroAmount())
            {
                for(var item : operateItems)
                {
                    operateIndex.put(item.getOperateItem(), cellIndex++);
                }
            }
            else
            {
                List<String> budgetNoList = budgetMList.stream().map(BudgetVo.BudgetItem::getBUDGET_NO).collect(Collectors.toList());

                List<String> OperateItemList = ncccPreBudgetD1Repository.getOperateItemByBudgetNoList(budgetNoList);

                for(var item : operateItems)
                {
                    if(OperateItemList.contains(item.getOperateItem()))
                    {
                        operateIndex.put(item.getOperateItem(), cellIndex++);
                    }
                }
            }

            // 準備 Excel 模板
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("預算編列");

            // 設定年度
            Row yearRow = sheet.createRow(0);
            yearRow.createCell(0).setCellValue("預算年度");
            Row yearDataRow = sheet.createRow(1);
            yearDataRow.createCell(0).setCellValue(model.getYear()); // 當前年份

            // 設定表頭
            Row headerRow = sheet.createRow(3);
            headerRow.createCell(0).setCellValue("部門");
            headerRow.createCell(1).setCellValue("預算科目代碼");
            headerRow.createCell(2).setCellValue("預算科目名稱");
            headerRow.createCell(3).setCellValue("摘要說明");
            headerRow.createCell(4).setCellValue("金額");

            for (var item : operateIndex.keySet()) {
                headerRow.createCell(operateIndex.get(item)).setCellValue(item);
            }

            headerRow.createCell(cellIndex++).setCellValue("合計");
            headerRow.createCell(cellIndex++).setCellValue("預算說明");

            // 填入資料
            int rowIndex = 4;
            for (var budgetM : budgetMList) {

                //取得作業項目
                var budgetOperateItems = ncccPreBudgetD1Repository.findByBudgetNoAndSeqNo(budgetM.getBUDGET_NO(), budgetM.getSEQ_NO());

                Row dataRow = sheet.createRow(rowIndex);

                dataRow.createCell(0).setCellValue(budgetM.getOU_CODE());
                dataRow.createCell(1).setCellValue(budgetM.getACC_CODE());
                dataRow.createCell(2).setCellValue(budgetM.getACC_NAME());
                dataRow.createCell(3).setCellValue(budgetM.getREMARK());
                dataRow.createCell(4).setCellValue(budgetM.getBUD_AMOUNT().doubleValue());

                int itemIndex = 5;

                BigDecimal total =  BigDecimal.ZERO;

                for(var item : operateIndex.keySet())
                {
                    var foundBudgetOperateItem = budgetOperateItems.stream().filter(o -> o.getOperateItem().equals(item)).findFirst();

                    if(foundBudgetOperateItem.isPresent())
                    {
                        var budgetOperateItem =  foundBudgetOperateItem.get();

                        total = total.add(budgetOperateItem.getOperateRatio());

                        dataRow.createCell(itemIndex).setCellValue(budgetOperateItem.getOperateRatio().doubleValue());
                    }
                    else
                    {
                        dataRow.createCell(itemIndex).setCellValue(new BigDecimal(0).doubleValue());
                    }

                    itemIndex++;
                }

                dataRow.createCell(itemIndex).setCellValue(total.doubleValue());

                rowIndex++;
            }

            //回傳串流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            //回傳串流
            return new BudgetResponse(true, "下載預算編列成功", outputStream.toByteArray());
        } catch (Exception e) {
            LOG.error("下載預算編列失敗", e);
            return new BudgetResponse(false, "下載預算編列失敗: " + e.getMessage());
        }
    }


    /**
     * 上傳折舊預算檔案
     * @param file
     * @param year
     * @return
     */
    @Override
    public BudgetResponse uploadBudgetRuleDepreciationFile(MultipartFile file, String year, String version) {
        try{
            // 檢查檔案是否為空
            if (file.isEmpty()) {
                return new BudgetResponse(false, "上傳檔案不能為空");
            }
            // 檢查檔案類型
            String contentType = file.getContentType();
            if (!"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
                return new BudgetResponse(false, "上傳檔案格式不正確，請上傳 Excel 檔案");
            }

            // 建立BudgetRuleDTO資料集
            Map<String, BudgetRuleDTO> budgetRuleByOuCode = new HashMap<>();

            // 解析 Excel 檔案
            try (InputStream inputStream = file.getInputStream()) {

                Workbook workbook = new XSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0); // 讀取第一個工作表

                // 讀取表頭 (第4行) 以獲取動態欄位的數量
                Row headerRow = sheet.getRow(3); // 第4行 (索引為3)
                int dynamicFieldStartIndex = 4; // F欄的索引
                int totalColumns = headerRow.getPhysicalNumberOfCells(); // 總列數
                int dynamicFieldEndIndex = totalColumns; // 總列數

                // 讀取年度 (A2)
                Row rowA2 = sheet.getRow(1);
                if (rowA2 == null || rowA2.getCell(0) == null) {
                    return new BudgetResponse(false, "年度資料不存在或格式不正確");
                }
                String yearData = getCellValueAsString(rowA2.getCell(0));
                if (!yearData.equals(year)) {
                    return new BudgetResponse(false, "年度資料不正確");
                }

                // 讀取資料 (從第5行開始)
                for (int i = 4; i <= sheet.getLastRowNum(); i++) { // 從第5行 (索引為4)
                    Row dataRow = sheet.getRow(i);
                    if (dataRow != null) {
                        // 假設每行的結構與 BudgetDetail 類的屬性一一對應
                        String depart = getCellValueAsString(dataRow.getCell(0));
                        String budgetCode = getCellValueAsString(dataRow.getCell(1));
                        //String budgetName = dataRow.getCell(2).getStringCellValue();
                        String description = getCellValueAsString(dataRow.getCell(4));
                        double amount = dataRow.getCell(3).getNumericCellValue();

                        if(depart.equals(""))
                        {
                            break;
                        }

                        // 檢查預算項目代碼是否存在
                        var budgetItem = ncccAccountingListRepository.findBySubject(budgetCode);
                        if (budgetItem == null) {
                            throw new Exception("第" + i + "行預算項目代碼 " + budgetCode + " 不存在");
                        }
                        // 檢查部門代碼是否存在
                        var department = syncOURepository.findByOuCode(depart);
                        if (department == null) {
                            throw new Exception("第" + i + "行部門代碼 " + depart + " 不正確");
                        }

                        // 讀取動態欄位名稱和值
                        Map<String, Double> dynamicFields = new HashMap<>();
                        for (int j = dynamicFieldStartIndex; j < dynamicFieldEndIndex; j++) { // 使用 < 而不是 <=
                            Cell cell = dataRow.getCell(j);
                            String fieldName = headerRow.getCell(j).getStringCellValue(); // 獲取欄位名稱
                            double fieldValue = (cell != null && cell.getCellType() == CellType.NUMERIC) ?
                                    cell.getNumericCellValue() : 0.0; // 如果是空白或非數字，預設為 0
                            dynamicFields.put(fieldName, fieldValue); // 將欄位名稱和值放入 Map
                        }

                        // 檢查OUcode是否存在budgetRuleByOuCode
                        if(!budgetRuleByOuCode.containsKey(depart)) {

                            // 將資料加入到回應 DTO 中

                            BudgetRuleDTO rule = new BudgetRuleDTO();
                            rule.setBudgetYear(year);
                            rule.setVersion(version);
                            rule.setDepartmentCode(SecurityUtil.getCurrentUser().getOuCode());
                            rule.setDepartmentName(SecurityUtil.getCurrentUser().getDeptName());
                            rule.setEmployeeCode(SecurityUtil.getCurrentUser().getUserId());
                            rule.setEmployeeName(SecurityUtil.getCurrentUser().getUserName());
                            rule.setBudgetDepartment(depart);
                            rule.setAllocationDate(LocalDate.now());
                            rule.setBudgetDetails(new ArrayList<>());
                            budgetRuleByOuCode.put(depart, rule);
                        }

                        // 將資料加入到回應 DTO 中
                        var detail = new BudgetDetail();

                        //填入作業項目
                        List<BudgetRuleDTO.OperationItem> operationItems =  new ArrayList<>();
                        // 新增預算科目
                        boolean isNew = true;

                        // 若該部門已經有對應預算科目,將金額加總
                        if(budgetRuleByOuCode.get(depart).getBudgetDetails().stream().anyMatch(o -> o.getBudgetItemCode().equals(budgetCode)))
                        {
                            isNew = false;
                            detail = budgetRuleByOuCode.get(depart).getBudgetDetails().stream().filter(o -> o.getBudgetItemCode().equals(budgetCode)).findFirst().get();
                            detail.setBudgetAmount(detail.getBudgetAmount().add(roundToNearestThousand((int) (amount))));
                            operationItems = detail.getOperationItems();
                        }
                        else
                        {
                            detail.setBudgetItemCode(budgetItem.getSubject());
                            detail.setBudgetItemName(budgetItem.getEssay());
                            detail.setRemark("資產折舊或攤銷");
                            detail.setBudgetAmount(roundToNearestThousand((int) (amount)));
                        }

                        BigDecimal totalRatio = new BigDecimal(0);

                        for(var field : dynamicFields.entrySet()) {
                            String fieldName = field.getKey();
                            double fieldValue = field.getValue();

                            //如果值是 0 跳過
                            if (fieldValue == 0) {
                                continue;
                            }

                            var opItem = new BudgetRuleDTO.OperationItem();

                            var operateItem = ncccOperateItemsRepository.findByOperateItemName(fieldName);
                            if (operateItem == null) {
                                throw new Exception("第" + i + "行作業項目代碼 " + fieldName + " 不存在");
                            }

                            totalRatio = totalRatio.add(BigDecimal.valueOf(fieldValue));

                            // 若該作業項目已產生,增加金額
                            if(operationItems.stream().anyMatch(o ->o.getCode().equals(operateItem.getOperateItemCode())))
                            {
                                opItem = operationItems.stream().filter(o -> o.getCode().equals(operateItem.getOperateItemCode())).findFirst().get();

                                opItem.setAmount(opItem.getAmount().add(roundToNearestThousand((int) (amount * fieldValue / 100))));
                            }
                            else
                            {
                                opItem.setCode(operateItem.getOperateItemCode());
                                opItem.setName(operateItem.getOperateItem());
                                // 金額為預算金額乘於Ratio 千位之後的數字四捨五入
                                // 這裡假設 Ratio 是百分比形式，需轉換為小數形式
                                opItem.setAmount(roundToNearestThousand((int) (amount * fieldValue / 100)));

                                operationItems.add(opItem);
                            }

                        }

                        // 同一行內的RATIO加總起來必須等於100%
                        if (totalRatio.compareTo(BigDecimal.valueOf(100.00)) != 0) {
                            throw new Exception("第" + i + "行作業項目加總為 " + totalRatio + "%,不等於100%");
                        }

                        for(BudgetRuleDTO.OperationItem opItem : operationItems)
                        {
                            // 對比現在的金額產生新ratio
                            opItem.setRatio(opItem.getAmount().divide(detail.getBudgetAmount(),4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                        }

                        detail.setOperationItems(operationItems);

                        // 計算差異數=預算金額-作業項目加總金額
                        // 如果差異數不等於0,找出作業項目金額最大的明細-差異數
                        BigDecimal totalAmount = operationItems.stream()
                                .map(BudgetRuleDTO.OperationItem::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal difference = detail.getBudgetAmount().subtract(totalAmount);
                        if (difference.compareTo(BigDecimal.ZERO) != 0) {
                            var maxItem = operationItems.stream()
                                    .max(Comparator.comparing(BudgetRuleDTO.OperationItem::getAmount))
                                    .orElse(null);
                            if (maxItem != null) {
                                maxItem.setAmount(maxItem.getAmount().add(difference));
                            }
                        }



                        // 計算新的比例
                        BigDecimal finalTotalRatio = operationItems.stream()
                                .map(BudgetRuleDTO.OperationItem::getRatio)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal differenceRatio = new BigDecimal(100).subtract(finalTotalRatio);
                        if (differenceRatio.compareTo(BigDecimal.ZERO) != 0)
                        {
                            var maxItem = operationItems.stream()
                                    .max(Comparator.comparing(BudgetRuleDTO.OperationItem::getAmount))
                                    .orElse(null);
                            if (maxItem != null) {
                                maxItem.setRatio(maxItem.getRatio().add(differenceRatio));
                            }
                        }

                        // 若是新預算科目,新增
                        if(isNew)
                        {
                            // 將 detail 加入到 budgetRuleByOuCode 中
                            budgetRuleByOuCode.get(depart).getBudgetDetails().add(detail);
                        }
                    }
                }
                String prefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));
                Integer count = ncccPreBudgetMRepository.findMaxSerialByPrefix(prefix) + 1;
                // 寫入資料庫
                for (var rule : budgetRuleByOuCode.values()) {

                    rule.setCount(count);

                    saveBudgetRule(rule);

                    count++;
                }
            }
            return new BudgetResponse(true, "上傳折舊預算檔案成功", null);

        }catch (Exception e) {
            LOG.error("上傳折舊預算檔案失敗", e);
            return new BudgetResponse(false, "上傳折舊預算檔案失敗: " + e.getMessage());
        }
    }

    /**
     * 合併SAP折舊預算檔案
     * @param file
     * @param year
     * @return
     */
    @Override
    public BudgetResponse uploadBudgetRuleSAPDepreciationFile(MultipartFile file, String year) {
        try{
            // 檢查檔案是否為空
            if (file.isEmpty()) {
                return new BudgetResponse(false, "上傳檔案不能為空");
            }
            // 檢查檔案類型
            String contentType = file.getContentType();
            if (!"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
                return new BudgetResponse(false, "上傳檔案格式不正確，請上傳 Excel 檔案");
            }

            // 建立固定資產讀取資料集
            Map<String, BigDecimal> assetExcelData = new HashMap<>();

            // 解析 Excel 檔案
            try (InputStream inputStream = file.getInputStream()) {

                Workbook workbook = new XSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0); // 讀取第一個工作表

                // 讀取資料
                for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 從第1行
                    Row dataRow = sheet.getRow(i);
                    if (dataRow != null)
                    {
                        // 讀取物件
                        String thisObject = getCellValueAsString(dataRow.getCell(1));

                        if(thisObject.isEmpty())
                        {
                            continue;
                        }

                        double amount = dataRow.getCell(14).getNumericCellValue();

                        //跳過金額為0的明細
                        if(amount == 0)
                        {
                            continue;
                        }

                        String thisAssetCode = thisObject.split("/")[0];

                        BigDecimal thisAmount = new BigDecimal(amount).abs();

                        assetExcelData.put(thisAssetCode, thisAmount);

                    }
                }
            }

            List<String> assetCodeList = new ArrayList<>(assetExcelData.keySet());
            //
            List<BudgetVo.BudgetSAPDepreciationAssetAccountingOuCodeData> assetAboutDataList = ncccPreBudgetMRepository.GetSAPDepreciationAssetAboutData(assetCodeList);

            List<BudgetVo.BudgetSAPDepreciationAssetOperateData> assetOperateDataList = ncccPreBudgetMRepository.GetSAPDepreciationAssetOperateData(assetCodeList);

            Map<String,String> operateItems = assetOperateDataList.stream().collect(Collectors.toMap(BudgetVo.BudgetSAPDepreciationAssetOperateData::getOperateItemCode, BudgetVo.BudgetSAPDepreciationAssetOperateData::getOperateItem,(v1, v2) -> v1));

            // 準備 Excel 模板
            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("折舊預算");

            // 設定年度
            Row yearRow = sheet.createRow(0);
            yearRow.createCell(0).setCellValue("預算年度");
            Row yearDataRow = sheet.createRow(1);
            yearDataRow.createCell(0).setCellValue(year); // 當前年份

            // 設定表頭
            Row headerRow = sheet.createRow(3);
            headerRow.createCell(0).setCellValue("部門");
            headerRow.createCell(1).setCellValue("預算科目代碼");
            headerRow.createCell(2).setCellValue("預算科目名稱");
            headerRow.createCell(3).setCellValue("金額");
            headerRow.createCell(4).setCellValue("摘要說明");

            int operateIndex = 5;

            for(var item : operateItems.keySet())
            {
                headerRow.createCell(operateIndex).setCellValue(operateItems.get(item));

                operateIndex++;
            }

            // 填入資料
            int rowIndex = 5;
            for(BudgetVo.BudgetSAPDepreciationAssetAccountingOuCodeData assetAboutData : assetAboutDataList)
            {
                Row dataRow = sheet.createRow(rowIndex);
                dataRow.createCell(0).setCellValue(assetAboutData.getOuCode());
                dataRow.createCell(1).setCellValue(assetAboutData.getAccounting());
                dataRow.createCell(2).setCellValue(assetAboutData.getAccountingName());
                dataRow.createCell(3).setCellValue(assetExcelData.get(assetAboutData.getAssetsCode()).doubleValue());
                dataRow.createCell(4).setCellValue(assetAboutData.getAssetsCode()+","+assetAboutData.getAssetsName());


                int itemIndex = 5;

                for(var item : operateItems.keySet())
                {
                    var foundAssetOperateItem = assetOperateDataList.stream().filter(o -> o.getOperateItemCode().equals(item) && o.getAssetsCode().equals(assetAboutData.getAssetsCode())).findFirst();

                    if(foundAssetOperateItem.isPresent())
                    {
                        var assetOperateItem =  foundAssetOperateItem.get();

                        dataRow.createCell(itemIndex).setCellValue(assetOperateItem.getOperateRatio().doubleValue());
                    }
                    else
                    {
                        dataRow.createCell(itemIndex).setCellValue(new BigDecimal(0).doubleValue());
                    }

                    itemIndex++;
                }

                rowIndex++;

            }

            //回傳串流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            return new BudgetResponse(true, "上傳折舊預算檔案成功", outputStream.toByteArray());

        }catch (Exception e) {
            LOG.error("上傳折舊預算檔案失敗", e);
            return new BudgetResponse(false, "上傳折舊預算檔案失敗: " + e.getMessage());
        }
    }

    /**
     * 儲存預算編列資料
     *
     * @param model
     * @return
     */
    @Transactional
    @Override
    public BudgetResponse saveBudgetRule(BudgetRuleDTO model) {
        try {
            NcccUserDto user = SecurityUtil.getCurrentUser();

            // 檢查是否有主檔資料
            NcccBudgetVersion budgetVersion = ncccBudgetVersionRepository
                    .findByYearAndVersion(model.getBudgetYear(), model.getVersion());

            if (budgetVersion == null) {
                return new BudgetResponse(false, "預算版次不存在");
            }

            NcccPreBudgetM budgetM = null;
            if (model.getDocumentNumber() == null || model.getDocumentNumber().isEmpty()) {
                // 代表新單
                Integer nextSerial = 1;

                String prefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));

                if(model.getCount() > 0)
                {
                    nextSerial = model.getCount();
                }
                else
                {
                    nextSerial = ncccPreBudgetMRepository.findMaxSerialByPrefix(prefix) + 1;
                }
                String serialStr = String.format("%04d", nextSerial);
                String exNo = "BU" + prefix + serialStr;

                budgetM = new NcccPreBudgetM();
                budgetM.setBudgetNo(exNo);
                budgetM.setYear(model.getBudgetYear());
                budgetM.setVersion(model.getVersion());
                budgetM.setCreateDate(LocalDate.now());
                budgetM.setCreateUser(user.getUserId());
            } else {
                // 代表編輯單
                // 查詢主檔單號是否存在
                budgetM = ncccPreBudgetMRepository.findByBudgetNoAndYearAndVersion(model.getDocumentNumber(),
                        model.getBudgetYear(), model.getVersion());
                if (budgetM == null) {
                    throw new IllegalArgumentException("預算編列資料不存在，請確認單號、年度和版次是否正確");
                }
                else if(!budgetM.getAssignment().equals("0"))
                {
                    throw new IllegalArgumentException("預算編列資料非可編輯狀態");
                }

                // 清除舊的明細資料
                ncccPreBudgetDRepository.deleteByBudgetNo(budgetM.getBudgetNo());
                ncccPreBudgetD1Repository.deleteByBudgetNo(budgetM.getBudgetNo());
            }

            // 更新預算主檔資料
            budgetM.setApplyDate(model.getAllocationDate());
            budgetM.setApplyUser(model.getEmployeeCode());
            budgetM.setApplyOuCode(model.getDepartmentCode());
            budgetM.setOuCode(model.getBudgetDepartment());
            budgetM.setUpdateDate(LocalDate.now());
            budgetM.setUpdateUser(user.getUserId());
            budgetM.setAssignment("0");
            ncccPreBudgetMRepository.save(budgetM);



            // 儲存明細資料
            List<NcccPreBudgetD> details = new ArrayList<>();
            List<NcccPreBudgetD1> d1etails = new ArrayList<>();

            int dseqNo = 1; // 預設序號從1開始
            for (BudgetRuleDTO.BudgetDetail detail : model.getBudgetDetails()) {
                NcccPreBudgetD budgetD = new NcccPreBudgetD();
                budgetD.setBudgetNo(budgetM.getBudgetNo());
                budgetD.setSeqNo(String.format("%03d", dseqNo));
                budgetD.setAccounting(detail.getBudgetItemCode());
                budgetD.setBudAmount(detail.getBudgetAmount());
                budgetD.setRemark(detail.getRemark());
                budgetD.setCreateDate(LocalDate.now());
                budgetD.setCreateUser(user.getUserId());
                budgetD.setUpdateDate(LocalDate.now());
                budgetD.setUpdateUser(user.getUserId());
                details.add(budgetD);

                // 儲存明細1資料
                int d1seqNo = 1; // 明細1序號從1開始
                for (BudgetRuleDTO.OperationItem detail1 : detail.getOperationItems()) {
                    NcccPreBudgetD1 budgetD1 = new NcccPreBudgetD1();
                    budgetD1.setBudgetNo(budgetM.getBudgetNo());
                    budgetD1.setSeqNo(String.format("%03d", dseqNo));
                    budgetD1.setSeqNo1(String.format("%03d", d1seqNo));
                    budgetD1.setOperateItemCode(detail1.getCode());
                    budgetD1.setOperateItem(detail1.getName());
                    budgetD1.setOperateAmt(detail1.getAmount());
                    budgetD1.setOperateRatio(detail1.getRatio());
                    budgetD1.setCreateDate(LocalDate.now());
                    budgetD1.setCreateUser(user.getUserId());
                    budgetD1.setUpdateDate(LocalDate.now());
                    budgetD1.setUpdateUser(user.getUserId());

                    d1etails.add(budgetD1);
                    d1seqNo++; // 明細1序號遞增
                }

                dseqNo++; // 序號遞增
            }

            ncccPreBudgetDRepository.saveAll(details);
            ncccPreBudgetD1Repository.saveAll(d1etails);

            return new BudgetResponse(true, "預算編列資料儲存成功");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
            LOG.error("儲存預算編列資料失敗", e);
            return new BudgetResponse(false, "儲存預算編列資料失敗: " + e.getMessage());
        }
    }

    /**
     * 刪除預算編列
     *
     * @param model
     * @return
     */
    @Override
    public BudgetResponse deleteBudgetRule(BudgetRequestDto.GetRuleDto model) {
        try {
            // 根據預算編號、年度和版次查詢預算主檔
            NcccPreBudgetM budgetM = ncccPreBudgetMRepository.findByBudgetNoAndYearAndVersion(model.getDocumentNumber(),
                    model.getBudgetYear(), model.getVersion());
            if (budgetM == null) {
                return new BudgetResponse(false, "沒有找到對應的預算編列資料");
            }
            else if(!budgetM.getAssignment().equals("0")){
                return new BudgetResponse(false, "預算編列資料非可刪除狀態");
            }

            // 刪除預算主檔
            ncccPreBudgetMRepository.delete(budgetM);

            // 刪除相關的預算明細資料
            ncccPreBudgetDRepository.deleteByBudgetNo(budgetM.getBudgetNo());
            ncccPreBudgetD1Repository.deleteByBudgetNo(budgetM.getBudgetNo());

            return new BudgetResponse(true, "刪除預算編列成功");
        } catch (Exception e) {
            LOG.error("刪除預算編列失敗", e);
            return new BudgetResponse(false, "刪除預算編列失敗: " + e.getMessage());
        }
    }

    /**
     * 取得預算編列資料
     */
    @Override
    public BudgetResponse getBudgetRule(BudgetRequestDto.GetRuleDto model) {
        try {
            // 根據預算年度和版次查詢預算主檔
            NcccPreBudgetM budgetM = ncccPreBudgetMRepository.findByBudgetNoAndYearAndVersion(model.getDocumentNumber(),
                    model.getBudgetYear(), model.getVersion());
            if (budgetM == null) {
                return new BudgetResponse(false, "沒有找到對應的預算編列資料");
            }

            // 查詢預算明細資料
            List<NcccPreBudgetD> budgetDetails = ncccPreBudgetDRepository.findByBudgetNo(budgetM.getBudgetNo());
            List<NcccPreBudgetD1> budgetDetails1 = ncccPreBudgetD1Repository.findByBudgetNo(budgetM.getBudgetNo());

            // 將查詢結果轉換為 DTO
            BudgetRuleDTO result = new BudgetRuleDTO();
            result.setDocumentNumber(budgetM.getBudgetNo());
            result.setBudgetYear(budgetM.getYear());
            result.setVersion(budgetM.getVersion());
            result.setAllocationDate(budgetM.getApplyDate());
            result.setEmployeeCode(budgetM.getApplyUser());
            SyncUser user = syncUserRepository.findByAccount(budgetM.getApplyUser());
            if (user != null) {
                result.setEmployeeName(user.getDisplayName());
            }
            result.setDepartmentCode(budgetM.getApplyOuCode());
            SyncOU ou = syncOURepository.findByOuCode(budgetM.getApplyOuCode());
            if (ou != null) {
                result.setDepartmentName(ou.getOuName());
            }
            result.setBudgetDepartment(budgetM.getOuCode());
            result.setAssignment(budgetM.getAssignment());
            // 填充預算明細
            ArrayList<BudgetRuleDTO.BudgetDetail> budgetDetailsList = new ArrayList<>();
            for (NcccPreBudgetD detail : budgetDetails) {
                BudgetRuleDTO.BudgetDetail budgetDetail = new BudgetRuleDTO.BudgetDetail();
                budgetDetail.setBudgetItemCode(detail.getAccounting());
                NcccAccountingList accounting = ncccAccountingListRepository.findBySubject(detail.getAccounting());
                if (accounting != null) {
                    budgetDetail.setBudgetItemName(accounting.getEssay());
                }
                budgetDetail.setSeqNo(detail.getSeqNo());
                budgetDetail.setUploadDate(detail.getUpdateDate());
                budgetDetail.setUploaderName(detail.getUpdateUser());
                budgetDetail.setBudgetAmount(detail.getBudAmount());
                budgetDetail.setRemark(detail.getRemark());

                // 查找對應的作業項目明細
                List<BudgetRuleDTO.OperationItem> operationItems = budgetDetails1.stream()
                        .filter(d1 -> d1.getSeqNo().equals(detail.getSeqNo()))
                        .map(d1 -> {
                            BudgetRuleDTO.OperationItem item = new BudgetRuleDTO.OperationItem();
                            item.setCode(d1.getOperateItemCode());
                            item.setName(d1.getOperateItem());
                            item.setAmount(d1.getOperateAmt());
                            item.setRatio(d1.getOperateRatio());
                            return item;
                        })
                        .collect(Collectors.toList());

                budgetDetail.setOperationItems(operationItems);
                budgetDetailsList.add(budgetDetail);
            }
            result.setBudgetDetails(budgetDetailsList);

            return new BudgetResponse(true, "取得預算編列資料成功", result);
        } catch (Exception e) {
            LOG.error("取得預算編列資料失敗", e);
            return new BudgetResponse(false, "取得預算編列資料失敗: " + e.getMessage());
        }
    }

    /*
     * 預算編列_確認
     */
    @Override
    public BudgetResponse Confirm(List<String> batchList) {

        try {

            LocalDate HandleTimestamp = LocalDate.now();

            NcccUserDto user = SecurityUtil.getCurrentUser();

            List<NcccPreBudgetM> budgetMList = ncccPreBudgetMRepository.GetByBatchBudgetNoList(batchList);

            // region 驗證

            List<String> ErrorList = new ArrayList<>();

            if(budgetMList.isEmpty())
            {
                ErrorList.add("未找到任何預算編列單");
            }
            else
            {
                for (NcccPreBudgetM budgetM : budgetMList)
                {
                    List<String> ThisErrorList =  new ArrayList<>();

                    if(!budgetM.getAssignment().equals("0"))
                    {
                        ThisErrorList.add("非可確認狀態");
                    }

                    if(!ThisErrorList.isEmpty())
                    {
                        ErrorList.add("預算編列單號"+ budgetM.getBudgetNo() + ":"+ String.join(",", ThisErrorList));
                    }
                }
            }

            if (!ErrorList.isEmpty()) {

                throw new IllegalArgumentException(String.join(",", ErrorList));

            }

            // endregion

            List<NcccPreBudgetM> UpdateBudgetDatas = new ArrayList<NcccPreBudgetM>();

            for (NcccPreBudgetM budgetM : budgetMList) {

                budgetM.setAssignDate(HandleTimestamp);

                budgetM.setAssignUser(user.getUserId());

                budgetM.setUpdateDate(HandleTimestamp);

                budgetM.setUpdateUser(user.getUserId());

                budgetM.setAssignment("1");

                UpdateBudgetDatas.add(budgetM);

            }

            ncccPreBudgetMRepository.saveAll(UpdateBudgetDatas);

            return new BudgetResponse(true, "確認預算編列單儲存成功");

        } catch (Exception e) {

            LOG.error("確認預算編列單資料失敗", e);

            return new BudgetResponse(false, "確認預算編列單資料失敗: " + e.getMessage());

        }

    }

    /*
     * 預算編列_確認退回
     */
    @Override
    public BudgetResponse ConfirmReturn(List<String> batchList) {

        try {

            LocalDate HandleTimestamp = LocalDate.now();

            NcccUserDto user = SecurityUtil.getCurrentUser();

            List<NcccPreBudgetM> budgetMList = ncccPreBudgetMRepository.GetByBatchBudgetNoList(batchList);

            // region 驗證

            List<String> ErrorList = new ArrayList<>();

            if(budgetMList.isEmpty())
            {
                ErrorList.add("未找到任何預算編列單");
            }
            else
            {
                for (NcccPreBudgetM budgetM : budgetMList)
                {
                    List<String> ThisErrorList =  new ArrayList<>();

                    if(!budgetM.getAssignment().equals("1"))
                    {
                        ThisErrorList.add("非可確認退回狀態");
                    }

                    if(!ThisErrorList.isEmpty())
                    {
                        ErrorList.add("預算編列單號"+ budgetM.getBudgetNo() + ":"+ String.join(",", ThisErrorList));
                    }
                }
            }

            if (!ErrorList.isEmpty()) {

                throw new IllegalArgumentException(String.join(",", ErrorList));

            }

            // endregion

            List<NcccPreBudgetM> UpdateBudgetDatas = new ArrayList<NcccPreBudgetM>();

            for (NcccPreBudgetM budgetM : budgetMList) {

                budgetM.setAssignDate(null);

                budgetM.setAssignUser(null);

                budgetM.setUpdateDate(HandleTimestamp);

                budgetM.setUpdateUser(user.getUserId());

                budgetM.setAssignment("0");

                UpdateBudgetDatas.add(budgetM);

            }

            ncccPreBudgetMRepository.saveAll(UpdateBudgetDatas);

            return new BudgetResponse(true, "確認退回預算編列單儲存成功");

        } catch (Exception e) {

            LOG.error("確認退回預算編列單資料失敗", e);

            return new BudgetResponse(false, "確認退回預算編列單資料失敗: " + e.getMessage());

        }

    }

    /*
     * 預算編列_覆核
     */
    @Override
    public BudgetResponse Approve(List<String> batchList) {

        try {

            LocalDate HandleTimestamp = LocalDate.now();

            NcccUserDto user = SecurityUtil.getCurrentUser();

            List<NcccPreBudgetM> budgetMList = ncccPreBudgetMRepository.GetByBatchBudgetNoList(batchList);

            // region 驗證

            List<String> ErrorList = new ArrayList<>();

            if(budgetMList.isEmpty())
            {
                ErrorList.add("未找到任何預算編列單");
            }
            else
            {
                for (NcccPreBudgetM budgetM : budgetMList)
                {
                    List<String> ThisErrorList =  new ArrayList<>();

                    if(!budgetM.getAssignment().equals("1"))
                    {
                        ThisErrorList.add("非可覆核狀態");
                    }

                    if(!budgetM.getAssignUser().equals(user.getUserId()))
                    {
                        ThisErrorList.add("不可與確認者相同");
                    }

                    if(!ThisErrorList.isEmpty())
                    {
                        ErrorList.add("預算編列單號"+ budgetM.getBudgetNo() + ":"+ String.join(",", ThisErrorList));
                    }
                }
            }

            if (!ErrorList.isEmpty()) {

                throw new IllegalArgumentException(String.join(",", ErrorList));

            }

            // endregion

            List<NcccPreBudgetM> UpdateBudgetDatas = new ArrayList<NcccPreBudgetM>();

            for (NcccPreBudgetM budgetM : budgetMList) {

                budgetM.setReviewDate(HandleTimestamp);

                budgetM.setReviewUser(user.getUserId());

                budgetM.setUpdateDate(HandleTimestamp);

                budgetM.setUpdateUser(user.getUserId());

                budgetM.setAssignment("2");

                UpdateBudgetDatas.add(budgetM);

            }

            ncccPreBudgetMRepository.saveAll(UpdateBudgetDatas);

            return new BudgetResponse(true, "覆核預算編列單儲存成功");

        } catch (Exception e) {

            LOG.error("覆核預算編列單資料失敗", e);

            return new BudgetResponse(false, "覆核預算編列單資料失敗: " + e.getMessage());

        }

    }

    /*
     * 預算編列_覆核退回
     */
    @Override
    public BudgetResponse ApproveReturn(List<String> batchList) {

        try {

            LocalDate HandleTimestamp = LocalDate.now();

            NcccUserDto user = SecurityUtil.getCurrentUser();

            List<NcccPreBudgetM> budgetMList = ncccPreBudgetMRepository.GetByBatchBudgetNoList(batchList);

            // region 驗證

            List<String> ErrorList = new ArrayList<>();

            if(budgetMList.isEmpty())
            {
                ErrorList.add("未找到任何預算編列單");
            }
            else
            {
                for (NcccPreBudgetM budgetM : budgetMList)
                {
                    List<String> ThisErrorList =  new ArrayList<>();

                    if(!budgetM.getAssignment().equals("2"))
                    {
                        ThisErrorList.add("非可覆核退回狀態");
                    }

                    if(!ThisErrorList.isEmpty())
                    {
                        ErrorList.add("預算編列單號"+ budgetM.getBudgetNo() + ":"+ String.join(",", ThisErrorList));
                    }
                }
            }

            if (!ErrorList.isEmpty()) {

                throw new IllegalArgumentException(String.join(",", ErrorList));

            }

            // endregion

            List<NcccPreBudgetM> UpdateBudgetDatas = new ArrayList<NcccPreBudgetM>();

            for (NcccPreBudgetM budgetM : budgetMList) {

                budgetM.setReviewDate(null);

                budgetM.setReviewUser(null);

                budgetM.setUpdateDate(HandleTimestamp);

                budgetM.setUpdateUser(user.getUserId());

                budgetM.setAssignment("1");

                UpdateBudgetDatas.add(budgetM);

            }

            ncccPreBudgetMRepository.saveAll(UpdateBudgetDatas);

            return new BudgetResponse(true, "覆核退回預算編列單儲存成功");

        } catch (Exception e) {

            LOG.error("覆核退回預算編列單資料失敗", e);

            return new BudgetResponse(false, "覆核退回預算編列單資料失敗: " + e.getMessage());

        }

    }

    // endregion 預算編列

    // region 預算開帳

    /**
     * 預算開帳
     *
     * @param model
     * @return
     */
    @Transactional
    @Override
    public BudgetResponse openBudget(BudgetRequestDto.YearVersion model) {
        try {
            NcccBudgetVersion budgetVersion = ncccBudgetVersionRepository.findByYearAndVersion(model.getYear(),
                    model.getVersion());
            if (budgetVersion == null) {
                return new BudgetResponse(false, "預算版次不存在");
            }

            // 檢查是否已經開帳
            if (budgetVersion.getOpenDate() != null) {
                return new BudgetResponse(false, "預算已經開帳");
            }

            // 設定開帳日期和使用者
            budgetVersion.setOpenDate(LocalDate.now());
            budgetVersion.setOpenUser(SecurityUtil.getCurrentUser().getUserId());
            ncccBudgetVersionRepository.save(budgetVersion);

            // 把預算編列複製到預算主表
            List<NcccBudgetM> pendingNcccBudgetMs = new ArrayList<NcccBudgetM>();
            List<BudgetVo.BudgetItem> budgetMs = ncccPreBudgetMRepository.GetItemsByYearAndVersion(model.getYear(),
                    model.getVersion());
            for (BudgetVo.BudgetItem budgetM : budgetMs) {
                NcccBudgetM budget = new NcccBudgetM();
                budget.setYear(model.getYear());
                budget.setVersion(model.getVersion());
                budget.setOuCode(budgetM.getOU_CODE());
                budget.setAccounting(budgetM.getACC_CODE());
                budget.setOriginalBudget(budgetM.getBUD_AMOUNT());
                budget.setCreateDate(LocalDate.now());
                budget.setCreateUser(SecurityUtil.getCurrentUser().getUserId());
                budget.setUpdateDate(LocalDate.now());
                budget.setUpdateUser(SecurityUtil.getCurrentUser().getUserId());
                pendingNcccBudgetMs.add(budget);
            }
            ncccBudgetMRepository.saveAll(pendingNcccBudgetMs);

            // 把預算編列作業項目複製到預算作業項目
            List<NcccBudgetD> pendingNcccBudgetDs = new ArrayList<NcccBudgetD>();
            List<BudgetVo.BudgetOperateItem> budgetDs = ncccPreBudgetMRepository
                    .GetOperateItemsByYearAndVersion(model.getYear(), model.getVersion());
            for (BudgetVo.BudgetOperateItem budgetD : budgetDs) {
                NcccBudgetD budget = new NcccBudgetD();
                budget.setYear(model.getYear());
                budget.setVersion(model.getVersion());
                budget.setOuCode(budgetD.getOU_CODE());
                budget.setAccounting(budgetD.getACC_CODE());
                budget.setOperateItemCode(budgetD.getOperateItemCode());
                budget.setOperateItem(budgetD.getOperateItem());
                budget.setOperateAmt(budgetD.getOperateAmt());
                budget.setOperateRatio(budgetD.getOperateRatio());
                budget.setCreateDate(LocalDate.now());
                budget.setCreateUser(SecurityUtil.getCurrentUser().getUserId());
                budget.setUpdateDate(LocalDate.now());
                budget.setUpdateUser(SecurityUtil.getCurrentUser().getUserId());
                pendingNcccBudgetDs.add(budget);
            }
            ncccBudgetDRepository.saveAll(pendingNcccBudgetDs);

            // 預算保留
            // 讀保留預算檔判斷已結案,採購單號等於空白, 加總相同預算部門,預算科目代號,保留預算金額
            // 如果資料存在 更新保留預算加上加總的保留預算金額 新增交易明細檔
            // 如果資料不存在 新增1筆預算檔,更新保留預算等於加總的保留預算金額 新增交易明細檔
            List<OpenBudgetReserveView> resviews = ncccBudgetReserveMRepository.findOpenBudgetReserveData(model.getYear(), model.getVersion());

            List<OpenBudgetReserveData> reserveDatas = new ArrayList<OpenBudgetReserveData>();
            // 加總相同預算部門、預算科目代號
            for(int j=0 ; j< resviews.size(); j++) {
                var resview = resviews.get(j);
                OpenBudgetReserveData d = reserveDatas.stream().filter(o -> o.getAPPLY_OUCODE().equals(resview.getAPPLY_OUCODE()))
                        .filter(o -> o.getACCOUNTING().equals(resview.getACCOUNTING())).findFirst().orElse(null);
                if(d == null){
                    OpenBudgetReserveData openBudgetReserveData = new OpenBudgetReserveData();
                    openBudgetReserveData.setAPPLY_OUCODE(resview.getAPPLY_OUCODE());
                    openBudgetReserveData.setACCOUNTING(resview.getACCOUNTING());
                    openBudgetReserveData.setRESERVER_AMT(resview.getRESERVER_AMT());
                    reserveDatas.add(openBudgetReserveData);
                }else{
                    d.setRESERVER_AMT(d.getRESERVER_AMT().add(resview.getRESERVER_AMT()));
                }
            }

            for (OpenBudgetReserveData resData : reserveDatas) {
                // 寫預算主檔<保留預算>
                NcccBudgetM budgetOut = ncccBudgetMRepository.findByYearAndVersionAndOuCodeAndAccounting(
                        model.getYear(),
                        model.getVersion(), resData.getAPPLY_OUCODE(), resData.getACCOUNTING());
                if (budgetOut == null) {
                    budgetOut = new NcccBudgetM();
                    budgetOut.setYear(model.getYear());
                    budgetOut.setVersion(model.getVersion());
                    budgetOut.setOuCode(resData.getAPPLY_OUCODE());
                    budgetOut.setAccounting(resData.getACCOUNTING());
                    budgetOut.setCreateDate(LocalDate.now());
                    budgetOut.setCreateUser(SecurityUtil.getCurrentUser().getUserId());
                    budgetOut.setUpdateDate(LocalDate.now());
                    budgetOut.setUpdateUser(SecurityUtil.getCurrentUser().getUserId());
                    ncccBudgetMRepository.save(budgetOut);
                }

                BudgetVo.BudgetTranscation transOut = new BudgetVo.BudgetTranscation();
                transOut.setYear(model.getYear());
                transOut.setVersion(model.getVersion());
                transOut.setOuCode(resData.getAPPLY_OUCODE());
                transOut.setAccounting(resData.getACCOUNTING());
                transOut.setTranscationSource("保留預算");
                transOut.setTranscationType("保留預算");
                transOut.setTranscationDate(LocalDate.now());
                transOut.setAmount(resData.getRESERVER_AMT());
                transOut.setCreateDate(LocalDateTime.now());
                transOut.setCreateUser(SecurityUtil.getCurrentUser().getUserId());
                transOut.setBpName("");
                transOut.setBpNo("");
                transOut.setDocNo("");
                WriteBudgetTranscation(transOut);
            }

            /**
             * 聽說還有一大堆開帳的邏輯
             * 請自己實現^ㄦ^
             */

            return new BudgetResponse(true, "預算開帳成功");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
            LOG.error("開帳失敗", e);
            return new BudgetResponse(false, "開帳失敗: " + e.getMessage());
        }
    }

    /**
     * 查詢是否已經開帳
     *
     * @param year
     * @return
     */
    @Override
    public BudgetResponse checkBudgetOpen(String year) {
        NcccBudgetVersion budgetVersion = ncccBudgetVersionRepository.findByYearAndVersion(year, "2");
        if (budgetVersion == null) {
            return new BudgetResponse(false, "預算版次不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("isOpen", false);

        // 檢查是否已經開帳
        if (budgetVersion.getOpenDate() != null) {
            result.put("isOpen", true);
            return new BudgetResponse(true, "該預算年度已經開帳", result);
        }

        return new BudgetResponse(false, "該預算年度未開帳", result);
    }

    // endregion 預算開帳

    // region 預算關帳

    /**
     * 關帳預算
     *
     * @param model
     * @return
     */
    @Override
    public BudgetResponse closeBudget(BudgetRequestDto.YearVersion model) {
        try {
            NcccBudgetVersion budgetVersion = ncccBudgetVersionRepository.findByYearAndVersion(model.getYear(),
                    model.getVersion());
            if (budgetVersion == null) {
                return new BudgetResponse(false, "預算版次不存在");
            }

            // 檢查是否已經關帳
            if (budgetVersion.getCloseDate() != null) {
                return new BudgetResponse(false, "預算已經關帳");
            }

            // 設定關帳日期和使用者
            budgetVersion.setCloseDate(LocalDate.now());
            budgetVersion.setCloseUser(SecurityUtil.getCurrentUser().getUserId());
            ncccBudgetVersionRepository.save(budgetVersion);

            /**
             * 聽說還有一大堆關帳的邏輯
             * 請自己實現^ㄦ^
             */

            return new BudgetResponse(true, "預算關帳成功");
        } catch (Exception e) {
            LOG.error("開帳失敗", e);
            return new BudgetResponse(false, "開帳失敗: " + e.getMessage());
        }
    }

    // endregion 預算關帳

    /**
     * 取得預算編列的預算項目
     */
    @Override
    public BudgetResponse getPreBudgetItem(BudgetRequestDto.YearVersion model) {
        try {
            NcccBudgetVersion budgetVersion = ncccBudgetVersionRepository.findByYearAndVersion(model.getYear(),
                    model.getVersion());
            if (budgetVersion == null) {
                return new BudgetResponse(false, "預算版次不存在");
            }
            List<BudgetVo.BudgetItem> budgetItems = ncccPreBudgetMRepository.GetItemsByYearAndVersion(model.getYear(),
                    model.getVersion());

            // 轉換成DTO
            List<BudgetGetItemDto> budgetGetItemDtos = new ArrayList<>();

            for (BudgetVo.BudgetItem budgetItem : budgetItems) {
                BudgetGetItemDto budgetGetItemDto = new BudgetGetItemDto();
                budgetGetItemDto.setCode(budgetItem.getACC_CODE());
                budgetGetItemDto.setName(budgetItem.getACC_NAME());
                budgetGetItemDto.setDeptCode(budgetItem.getOU_CODE());
                budgetGetItemDto.setDeptName(budgetItem.getOU_NAME());
                budgetGetItemDtos.add(budgetGetItemDto);
            }

            return new BudgetResponse(true, "取得預算項目成功", budgetGetItemDtos);
        } catch (Exception e) {
            LOG.error("取得預算項目失敗", e);
            return new BudgetResponse(false, "取得預算項目失敗: " + e.getMessage());
        }
    }

    /**
     * 取得預算主檔的預算項目
     */
    @Override
    public BudgetResponse getBudgetItem(BudgetRequestDto.GetBudgetItemByOuCode model) {
        try {
            NcccBudgetVersion budgetVersion = ncccBudgetVersionRepository.findByYearAndVersion(model.getYear(),
                    model.getVersion());
            if (budgetVersion == null) {
                return new BudgetResponse(false, "預算版次不存在");
            }
            List<BudgetVo.BudgetDetailItem> budgetItems = ncccBudgetMRepository
                    .GetItemsByYearAndVersionAndOuCode(model.getYear(), model.getVersion(), model.getOuCode());

            // 轉換成DTO
            List<BudgetGetItemDto> budgetGetItemDtos = new ArrayList<>();

            for (BudgetVo.BudgetDetailItem budgetItem : budgetItems) {
                BudgetGetItemDto budgetGetItemDto = new BudgetGetItemDto();
                budgetGetItemDto.setCode(budgetItem.getACC_CODE());
                budgetGetItemDto.setName(budgetItem.getACC_NAME());
                budgetGetItemDto.setDeptCode(budgetItem.getOU_CODE());
                budgetGetItemDto.setDeptName(budgetItem.getOU_NAME());
                budgetGetItemDtos.add(budgetGetItemDto);
            }

            return new BudgetResponse(true, "取得預算項目成功", budgetGetItemDtos);
        } catch (Exception e) {
            LOG.error("取得預算項目失敗", e);
            return new BudgetResponse(false, "取得預算項目失敗: " + e.getMessage());
        }
    }

    /**
     * 取得部門預算
     */
    @Override
    public BudgetResponse getBudgetRemainByOuCode(BudgetRequestDto.GetBudgetRemainByOuCode model) {
        try {
            NcccBudgetVersion budgetVersion = ncccBudgetVersionRepository.findByYearAndVersion(model.getYear(),
                    model.getVersion());
            if (budgetVersion == null) {
                return new BudgetResponse(false, "預算版次不存在");
            }

            // 取得預算項目
            List<BudgetVo.BudgetDetailItem> budgetItems = ncccBudgetMRepository
                    .GetItemsByYearAndVersionAndOuCode(model.getYear(), model.getVersion(), model.getOuCode());

            // 轉換成DTO
            List<BudgetGetDeptAmtDto> budgetDetailDtos = new ArrayList<>();
            for (BudgetVo.BudgetDetailItem item : budgetItems) {
                BudgetGetDeptAmtDto budgetGetDeptAmtDto = new BudgetGetDeptAmtDto();
                budgetGetDeptAmtDto.year = model.getYear();
                budgetGetDeptAmtDto.departmentCode = item.getOU_CODE();
                budgetGetDeptAmtDto.departmentName = item.getOU_NAME();
                budgetGetDeptAmtDto.budgetItemCode = item.getACC_CODE();
                budgetGetDeptAmtDto.budgetItemName = item.getACC_NAME();

                // 預算餘額=原始預算+保留預算+勻入-勻出-占用-動用
                BigDecimal remainAmt = item.getOriginalBudget() == null ? BigDecimal.ZERO : item.getOriginalBudget();
                remainAmt = remainAmt.add(item.getReserveBudget() == null ? BigDecimal.ZERO : item.getReserveBudget())
                        .add(item.getAllocIncreaseAmt() == null ? BigDecimal.ZERO : item.getAllocIncreaseAmt())
                        .subtract(item.getAllocReduseAmt() == null ? BigDecimal.ZERO : item.getAllocReduseAmt())
                        .subtract(item.getOccupyAmt() == null ? BigDecimal.ZERO : item.getOccupyAmt())
                        .subtract(item.getUseAmt() == null ? BigDecimal.ZERO : item.getUseAmt());

                budgetGetDeptAmtDto.remainAmount = remainAmt;
                budgetDetailDtos.add(budgetGetDeptAmtDto);
            }

            return new BudgetResponse(true, "取得部門剩餘預算成功", budgetDetailDtos);
        } catch (Exception e) {
            LOG.error("取得部門剩餘預算失敗", e);
            return new BudgetResponse(false, "取得部門剩餘預算失敗: " + e.getMessage());
        }
    }

    // region 預算調撥

    /**
     * 儲存預算調撥
     */
    @Transactional
    @Override
    public BudgetResponse saveBudgetAllocation(BudgetAllocationDto model) {
        try {
            NcccUserDto user = SecurityUtil.getCurrentUser();

            NcccBudgetVersion budgetVersion = ncccBudgetVersionRepository.findByYearAndVersion(model.getBudgetYear(),
                    model.getVersion());
            if (budgetVersion == null) {
                return new BudgetResponse(false, "預算版次不存在");
            }
            // 檢查是否已經開帳
            if (budgetVersion.getOpenDate() == null) {
                return new BudgetResponse(false, "預算尚未開帳");
            }
            // 檢查是否已經關帳
            if (budgetVersion.getCloseDate() != null) {
                return new BudgetResponse(false, "預算已經關帳");
            }

            NcccBudgetAdjustM budgetAdjustM = null;

            String processId = "";

            if (model.getTransferNo() == null || model.getTransferNo().isEmpty()) {
                // 代表新單
                String prefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));
                Integer nextSerial = ncccBudgetAdjustMRepository.findMaxSerialByPrefix(prefix) + 1;
                String serialStr = String.format("%04d", nextSerial);
                String exNo = "AD" + prefix + serialStr;

                budgetAdjustM = new NcccBudgetAdjustM();
                budgetAdjustM.setAdjNo(exNo);
                budgetAdjustM.setYear(model.getBudgetYear());
                budgetAdjustM.setVersion(model.getVersion());
                budgetAdjustM.setCreateDate(LocalDateTime.now());
                budgetAdjustM.setCreateUser(user.getUserId());

                // region 審核人員塞入

                String processKey = ProcessDefinitionKey.BUDGET_BUDGETALLOCATION.getKey();

                Map<String, Object> vars = new HashMap<>();

                vars.put(AssigneeRole.INITIATOR.getKey(), user.getHrid());

                List<String> approvers = new ArrayList<>();

                Optional<SyncUser> foundMgrUser = syncUserRepository.findManagerUserByOuCode(user.getOuCode());

                if(foundMgrUser.isPresent())
                {
                    SyncUser mgrUser = foundMgrUser.get();

                    approvers.add(mgrUser.getHrid());
                }


                for (BudgetAllocationDto.TransferDetail detail : model.getTransferDetails())
                {
                    //勻出經辦
                    approvers.add(detail.getOutDeptUser());

                    //勻出單位主管
                    Optional<SyncUser> foundOutDeptMgrUser = syncUserRepository.findManagerUserByOuCode(detail.getOutDeptCode());

                    if(foundOutDeptMgrUser.isPresent())
                    {
                        SyncUser OutDeptMgrUser = foundOutDeptMgrUser.get();

                        approvers.add(OutDeptMgrUser.getHrid());
                    }

                    //勻入經辦(可選採購單同申請人,故直接拿申請人)
                    approvers.add(user.getHrid());

                    //勻入單位主管
                    if(foundMgrUser.isPresent())
                    {
                        SyncUser mgrUser = foundMgrUser.get();

                        approvers.add(mgrUser.getHrid());
                    }

                }

                vars.put(AssigneeRole.APPROVERS.getKey(), approvers);
                vars.put(AssigneeRole.CURRENT_INDEX.getKey(), 0);
                vars.put(AssigneeRole.CURRENT_APPROVER.getKey(), approvers.get(0));

                processId = flowableService.startProcess(processKey, exNo, vars);

                budgetAdjustM.setTaskID(processId);

                // endregion


            } else {
                // 代表編輯單
                // 查詢主檔單號是否存在
                budgetAdjustM = ncccBudgetAdjustMRepository.findByAdjNoAndYearAndVersion(model.getTransferNo(),
                        model.getBudgetYear(), model.getVersion());
                if (budgetAdjustM == null) {
                    throw new IllegalArgumentException("預算調撥規則資料不存在，請確認單號、年度和版次是否正確");
                }

                processId = budgetAdjustM.getTaskID();
            }

            budgetAdjustM.setApplyDate(model.getApplyDate());
            budgetAdjustM.setApplyUser(model.getEmployeeCode());
            budgetAdjustM.setApplyOuCode(model.getDeptCode());
            budgetAdjustM.setPoNo(model.getPurchaseOrderNo());
            budgetAdjustM.setPoRemark(model.getPurchasePurpose());
            budgetAdjustM.setRemark(model.getTransferRemark());
            budgetAdjustM.setFlowStatus("1");
            budgetAdjustM.setUpdateDate(LocalDateTime.now());
            budgetAdjustM.setUpdateUser(user.getUserId());
            // 儲存主檔
            ncccBudgetAdjustMRepository.save(budgetAdjustM);

            // 清除舊的明細資料
            ncccBudgetAdjustDRepository.deleteByAdjNo(budgetAdjustM.getAdjNo());
            ncccBudgetAdjustD1Repository.deleteByAdjNo(budgetAdjustM.getAdjNo());

            // 儲存明細資料
            List<NcccBudgetAdjustD> details = new ArrayList<>();
            List<NcccBudgetAdjustD1> d1etails = new ArrayList<>();

            int dseqNo = 1; // 預設序號從1開始
            for (BudgetAllocationDto.TransferDetail detail : model.getTransferDetails()) {
                NcccBudgetAdjustD budgetD = new NcccBudgetAdjustD();
                budgetD.setAdjNo(budgetAdjustM.getAdjNo());
                budgetD.setSeqNo(String.format("%03d", dseqNo));
                budgetD.setOutOuCode(detail.getOutDeptCode());
                budgetD.setOutOuCodeUser(detail.getOutDeptUser());
                budgetD.setOutAccounting(detail.getOutBudgetItemCode());
                budgetD.setOutBalance(detail.getOutBudgetBalance());
                budgetD.setInOuCode(detail.getInDeptCode());
                budgetD.setInOuCodeUser(detail.getInDeptUser());
                budgetD.setInAccounting(detail.getInBudgetItemCode());
                budgetD.setInBalance(detail.getInBudgetBalance());
                budgetD.setAdjustAmt(detail.getAmount());
                budgetD.setCreateDate(LocalDate.now());
                budgetD.setCreateUser(user.getUserId());
                budgetD.setUpdateDate(LocalDate.now());
                budgetD.setUpdateUser(user.getUserId());
                details.add(budgetD);

                // 儲存明細1資料
                int d1seqNo = 1; // 明細1序號從1開始
                for (BudgetAllocationDto.OperationItem detail1 : detail.getOperationItems()) {
                    NcccBudgetAdjustD1 budgetD1 = new NcccBudgetAdjustD1();
                    budgetD1.setAdjNo(budgetAdjustM.getAdjNo());
                    budgetD1.setSeqNo(String.format("%03d", dseqNo));
                    budgetD1.setSeqNo1(String.format("%03d", d1seqNo));
                    budgetD1.setOperateItemCode(detail1.getCode());
                    budgetD1.setOperateItem(detail1.getName());
                    budgetD1.setOperateAmt(detail1.getAmount());
                    budgetD1.setOperateRatio(detail1.getRatio());
                    budgetD1.setCreateDate(LocalDate.now());
                    budgetD1.setCreateUser(user.getUserId());
                    budgetD1.setUpdateDate(LocalDate.now());
                    budgetD1.setUpdateUser(user.getUserId());

                    d1etails.add(budgetD1);
                    d1seqNo++; // 明細1序號遞增
                }

                dseqNo++; // 序號遞增

                // 寫預算主檔<勻出>
                BudgetVo.BudgetTranscation transIn = new BudgetVo.BudgetTranscation();
                transIn.setYear(budgetAdjustM.getYear());
                transIn.setVersion(budgetAdjustM.getVersion());
                transIn.setOuCode(detail.getOutDeptCode());
                transIn.setAccounting(detail.getOutBudgetItemCode());
                transIn.setTranscationSource("預算調撥");
                transIn.setTranscationType("勻出");
                transIn.setTranscationDate(budgetAdjustM.getApplyDate());
                transIn.setTranscationNo(budgetAdjustM.getAdjNo());
                transIn.setAmount(detail.getAmount());
                transIn.setCreateDate(LocalDateTime.now());
                transIn.setCreateUser(budgetAdjustM.getCreateUser());
                transIn.setBpName("");
                transIn.setBpNo("");
                transIn.setDocNo("");
                WriteBudgetTranscation(transIn);

                // 寫預算主檔<勻入>
                BudgetVo.BudgetTranscation transOut = new BudgetVo.BudgetTranscation();
                transOut.setYear(budgetAdjustM.getYear());
                transOut.setVersion(budgetAdjustM.getVersion());
                transOut.setOuCode(detail.getInDeptCode());
                transOut.setAccounting(detail.getInBudgetItemCode());
                transOut.setTranscationSource("預算調撥");
                transOut.setTranscationType("勻入");
                transOut.setTranscationDate(budgetAdjustM.getApplyDate());
                transOut.setTranscationNo(budgetAdjustM.getAdjNo());
                transOut.setAmount(detail.getAmount());
                transOut.setCreateDate(LocalDateTime.now());
                transOut.setCreateUser(budgetAdjustM.getCreateUser());
                transOut.setBpName("");
                transOut.setBpNo("");
                transOut.setDocNo("");
                WriteBudgetTranscation(transOut);
            }

            ncccBudgetAdjustDRepository.saveAll(details);
            ncccBudgetAdjustD1Repository.saveAll(d1etails);

            DecisionVo decisionVo = new DecisionVo();
            decisionVo.setProcessId(processId);
            decisionVo.setDecision(Decision.SUBMIT);
            flowableService.completeTask(decisionVo);

            return new BudgetResponse(true, "ok", null);

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
            LOG.error("儲存預算調撥資料失敗", e);
            return new BudgetResponse(false, "儲存預算調撥資料失敗: " + e.getMessage());
        }
    }

    /**
     * 取得預算調撥單資料
     *
     * @param model
     * @return
     */
    @Override
    public BudgetResponse getBudgetAllocation(BudgetRequestDto.GetBudgetAllocation model) {
        try {
            BudgetAllocationDto dto = new BudgetAllocationDto();

            // 取得主檔資料
            NcccBudgetAdjustM budgetAdjustM = ncccBudgetAdjustMRepository.findByAdjNo(model.getAdjNo());
            if (budgetAdjustM == null) {
                return new BudgetResponse(false, "找不到預算調撥單資料");
            }

            // 將資料填入DTO
            dto.setTransferNo(budgetAdjustM.getAdjNo());
            dto.setTaskId(budgetAdjustM.getTaskID());
            dto.setBudgetYear(budgetAdjustM.getYear());
            dto.setVersion(budgetAdjustM.getVersion());
            dto.setApplyDate(budgetAdjustM.getApplyDate());
            dto.setEmployeeCode(budgetAdjustM.getApplyUser());
            dto.setDeptCode(budgetAdjustM.getApplyOuCode());
            dto.setPurchaseOrderNo(budgetAdjustM.getPoNo());
            dto.setPurchasePurpose(budgetAdjustM.getPoRemark());
            dto.setTransferRemark(budgetAdjustM.getRemark());
            SyncUser user = syncUserRepository.findByHrid(budgetAdjustM.getApplyUser());
            if (user != null) {
                dto.setEmployeeName(user.getDisplayName());
            }
            SyncOU ou = syncOURepository.findByOuCode(budgetAdjustM.getApplyOuCode());
            if (ou != null) {
                dto.setDeptName(ou.getOuName());
            }

            // 取得明細資料
            dto.setTransferDetails(new ArrayList<BudgetAllocationDto.TransferDetail>());
            List<NcccBudgetAdjustD> details = ncccBudgetAdjustDRepository.findByAdjNo(model.getAdjNo());
            for (NcccBudgetAdjustD detail : details) {
                BudgetAllocationDto.TransferDetail transferDetail = new BudgetAllocationDto.TransferDetail();
                transferDetail.setItemNo(detail.getSeqNo());
                transferDetail.setOutDeptCode(detail.getOutOuCode());
                transferDetail.setOutDeptUser(detail.getOutOuCodeUser());
                transferDetail.setOutBudgetItemCode(detail.getOutAccounting());
                transferDetail.setOutBudgetBalance(detail.getOutBalance());
                transferDetail.setInDeptCode(detail.getInOuCode());
                transferDetail.setInDeptUser(detail.getInOuCodeUser());
                transferDetail.setInBudgetItemCode(detail.getInAccounting());
                transferDetail.setInBudgetBalance(detail.getInBalance());
                transferDetail.setAmount(detail.getAdjustAmt());

                SyncOU outOU = syncOURepository.findByOuCode(detail.getOutOuCode());
                if (outOU != null) {
                    transferDetail.setOutDeptName(outOU.getOuName());
                }
                SyncOU inOU = syncOURepository.findByOuCode(detail.getInOuCode());
                if (inOU != null) {
                    transferDetail.setInDeptName(inOU.getOuName());
                }
                NcccAccountingList outAccounting = ncccAccountingListRepository
                        .findBySubject(detail.getOutAccounting());
                if (outAccounting != null) {
                    transferDetail.setOutBudgetItemName(outAccounting.getEssay());
                }
                NcccAccountingList inAccounting = ncccAccountingListRepository.findBySubject(detail.getInAccounting());
                if (inAccounting != null) {
                    transferDetail.setInBudgetItemName(inAccounting.getEssay());
                }

                // 載入作業項目
                List<BudgetAllocationDto.OperationItem> operationItems = new ArrayList<>();
                List<NcccBudgetAdjustD1> d1etails = ncccBudgetAdjustD1Repository.findByAdjNoAndSeqNo(detail.getAdjNo(),
                        detail.getSeqNo());
                for (NcccBudgetAdjustD1 d1etail : d1etails) {
                    BudgetAllocationDto.OperationItem operationItem = new BudgetAllocationDto.OperationItem();
                    operationItem.setCode(d1etail.getOperateItemCode());
                    operationItem.setName(d1etail.getOperateItem());
                    operationItem.setAmount(d1etail.getOperateAmt());
                    operationItem.setRatio(d1etail.getOperateRatio());
                    operationItems.add(operationItem);
                }
                transferDetail.setOperationItems(operationItems);
                dto.getTransferDetails().add(transferDetail);
            }

            dto.setTaskHistoryList(flowableService.getTaskHistory(budgetAdjustM.getTaskID()));
            if (flowableService.checkAtInitiatorTask(budgetAdjustM.getTaskID())) {
                dto.setMode(Mode.EDITMODE);
            }
            else
            {
                dto.setMode(Mode.VIEWMODE);
            }

            dto.setCanBackToPrevious(true);

            return new BudgetResponse(true, "ok", dto);
        } catch (Exception e) {
            LOG.error("取得預算調撥單資料失敗", e);
            return new BudgetResponse(false, "取得預算調撥單資料失敗: " + e.getMessage());
        }
    }

    /**
     * 預算調撥動作判斷
     * @param vo
     * @return
     */
    @Override
    public BudgetResponse decisionBudgetAllocation(DecisionVo vo) {

        try
        {
            boolean flag = flowableService.completeTask(vo);

            if (flag) {

                switch(vo.getDecision())
                {
                    case APPROVE:

                        if(flowableService.isFinish(vo.getProcessId()))
                        {
                            return approveAllocation(vo.getProcessId());
                        }
                        else
                        {
                            return new BudgetResponse(true, "ok");
                        }

                    case INVALID:

                        return voidAllocation(vo.getProcessId());

                    case BACK:
                    case RETURN:

                        if(flowableService.checkAtInitiatorTask(vo.getProcessId()))
                        {
                            return returnAllocation(vo.getProcessId());
                        }
                        else
                        {
                            return new BudgetResponse(true, "ok");
                        }

                    default:

                        return new BudgetResponse(true, "ok");

                }

            } else {
                return new BudgetResponse(false, "簽核預算調撥失敗");
            }

        }
        catch (Exception e)
        {
            LOG.error("核可預算調撥失敗", e);
            return new BudgetResponse(false, "簽核預算調撥失敗: " + e.getMessage());
        }


    }

    /**
     * 預算調撥_作廢
     *
     * @param taskId
     * @return
     */
    public BudgetResponse voidAllocation(String taskId) {
        try {
            // 取得主檔資料
            NcccBudgetAdjustM budgetAdjustM = ncccBudgetAdjustMRepository.findByTaskID(taskId);
            if (budgetAdjustM == null) {
                return new BudgetResponse(false, "找不到預算調撥單資料");
            }
            NcccUserDto user = SecurityUtil.getCurrentUser();
            budgetAdjustM.setFlowStatus("3");
            budgetAdjustM.setUpdateDate(LocalDateTime.now());
            budgetAdjustM.setUpdateUser(user.getUserName());
            ncccBudgetAdjustMRepository.save(budgetAdjustM);

            return new BudgetResponse(true, "ok");
        } catch (Exception e) {
            LOG.error("作廢預算調撥單失敗", e);
            return new BudgetResponse(false, "作廢預算調撥單失敗: " + e.getMessage());
        }
    }

    /**
     * 預算調撥_核可
     *
     * @param taskId
     * @return
     */
    public BudgetResponse approveAllocation(String taskId) {
        try {
            // 取得主檔資料
            NcccBudgetAdjustM budgetAdjustM = ncccBudgetAdjustMRepository.findByAdjNo(taskId);
            if (budgetAdjustM == null) {
                return new BudgetResponse(false, "找不到預算調撥單資料");
            }
            NcccUserDto user = SecurityUtil.getCurrentUser();
            budgetAdjustM.setFlowStatus("2");
            budgetAdjustM.setUpdateDate(LocalDateTime.now());
            budgetAdjustM.setUpdateUser(user.getUserName());
            ncccBudgetAdjustMRepository.save(budgetAdjustM);

            return new BudgetResponse(true, "ok");
        } catch (Exception e) {
            LOG.error("核可預算調撥單失敗", e);
            return new BudgetResponse(false, "核可預算調撥單失敗: " + e.getMessage());
        }
    }

    /**
     * 預算調撥_退回
     *
     * @param taskId
     * @return
     */
    @Transactional
    public BudgetResponse returnAllocation(String taskId) {
        try {
            // 取得主檔資料
            NcccBudgetAdjustM budgetAdjustM = ncccBudgetAdjustMRepository.findByTaskID(taskId);
            if (budgetAdjustM == null) {
                return new BudgetResponse(false, "找不到預算調撥單資料");
            }

            // 退回給申請人時
            if(true){
                // 取得明細資料
                List<NcccBudgetAdjustD> details = ncccBudgetAdjustDRepository.findByAdjNo(budgetAdjustM.getAdjNo());
                for (NcccBudgetAdjustD detail : details) {

                    // 寫預算主檔<勻出>
                    BudgetVo.BudgetTranscation transOut = new BudgetVo.BudgetTranscation();
                    transOut.setYear(budgetAdjustM.getYear());
                    transOut.setVersion(budgetAdjustM.getVersion());
                    transOut.setOuCode(detail.getOutOuCode());
                    transOut.setAccounting(detail.getOutAccounting());
                    transOut.setTranscationSource("預算調撥");
                    transOut.setTranscationType("勻出");
                    transOut.setTranscationDate(budgetAdjustM.getApplyDate());
                    transOut.setTranscationNo(budgetAdjustM.getAdjNo());
                    transOut.setAmount(detail.getAdjustAmt().multiply(BigDecimal.valueOf(-1)));
                    transOut.setCreateDate(LocalDateTime.now());
                    transOut.setCreateUser(budgetAdjustM.getCreateUser());
                    transOut.setBpName("");
                    transOut.setBpNo("");
                    transOut.setDocNo("");
                    WriteBudgetTranscation(transOut);

                    // 寫預算主檔<勻入>
                    BudgetVo.BudgetTranscation transIn = new BudgetVo.BudgetTranscation();
                    transIn.setYear(budgetAdjustM.getYear());
                    transIn.setVersion(budgetAdjustM.getVersion());
                    transIn.setOuCode(detail.getInOuCode());
                    transIn.setAccounting(detail.getInAccounting());
                    transIn.setTranscationSource("預算調撥");
                    transIn.setTranscationType("勻入");
                    transIn.setTranscationDate(budgetAdjustM.getApplyDate());
                    transIn.setTranscationNo(budgetAdjustM.getAdjNo());
                    transIn.setAmount(detail.getAdjustAmt().multiply(BigDecimal.valueOf(-1)));
                    transIn.setCreateDate(LocalDateTime.now());
                    transIn.setCreateUser(budgetAdjustM.getCreateUser());
                    transIn.setBpName("");
                    transIn.setBpNo("");
                    transIn.setDocNo("");
                    WriteBudgetTranscation(transIn);
                }
            }

            return new BudgetResponse(true, "ok");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
            LOG.error("退回預算調撥單失敗", e);
            return new BudgetResponse(false, "退回預算調撥單失敗: " + e.getMessage());
        }
    }

    // endregion 預算調撥

    // region 預算查詢

    /**
     * 查詢預算
     */
    @Override
    public BudgetResponse queryBudget(BudgetRequestDto.QueryBudget model) {
        try {
            List<BudgetQueryDto.BudgetItem> dto = new ArrayList<BudgetQueryDto.BudgetItem>();

            Specification<NcccBudgetM> spec = (root, query, cb) -> {
                query.distinct(true);
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.greaterThanOrEqualTo(root.get("year"), model.getStartYear()));

                predicates.add(cb.lessThanOrEqualTo(root.get("year"), model.getEndYear()));

                predicates.add(cb.equal(root.get("version"), model.getVersion()));

                if (model.getOuCode() != null && model.getOuCode().size() > 0) {
                    predicates.add(root.get("ouCode").in(model.getOuCode()));
                }

                if (model.getBudgetItemCode() != null && model.getBudgetItemCode().size() > 0) {
                    predicates.add(root.get("accounting").in(model.getBudgetItemCode()));
                }

                query.orderBy(cb.asc(root.get("year")));

                return cb.and(predicates.toArray(new Predicate[0]));
            };

            // 查詢預算主檔
            List<NcccBudgetM> budgetM = ncccBudgetMRepository.findAll(spec);

            // 轉換成DTO
            for (NcccBudgetM budget : budgetM) {
                BudgetQueryDto.BudgetItem item = new BudgetQueryDto.BudgetItem();
                item.setYear(budget.getYear());
                item.setVersion(budget.getVersion());
                item.setDepartmentCode(budget.getOuCode());
                item.setBudgetItemCode(budget.getAccounting());
                NcccAccountingList outAccounting = ncccAccountingListRepository.findBySubject(budget.getAccounting());
                if (outAccounting != null) {
                    item.setBudgetItemName(outAccounting.getEssay());
                }
                item.setOriginalAmount(
                        budget.getOriginalBudget() == null ? BigDecimal.ZERO : budget.getOriginalBudget());
                item.setReservedAmount(budget.getReserveBudget() == null ? BigDecimal.ZERO : budget.getReserveBudget());
                item.setAllocIncreaseAmt(
                        budget.getAllocIncreaseAmt() == null ? BigDecimal.ZERO : budget.getAllocIncreaseAmt());
                item.setAllocreduseAmt(
                        budget.getAllocReduseAmt() == null ? BigDecimal.ZERO : budget.getAllocReduseAmt());
                BigDecimal availableAmt = item.getOriginalAmount().subtract(item.getReservedAmount())
                        .add(item.getAllocIncreaseAmt()).subtract(item.getAllocreduseAmt());
                item.setAvailableAmount(availableAmt);
                item.setOccupiedAmount(budget.getOccupyAmt() == null ? BigDecimal.ZERO : budget.getOccupyAmt());
                item.setUsedAmount(budget.getUseAmt() == null ? BigDecimal.ZERO : budget.getUseAmt());
                BigDecimal remainAmt = availableAmt.subtract(item.getOccupiedAmount()).subtract(item.getUsedAmount());
                item.setRemainingAmount(remainAmt);

                dto.add(item);
            }
            return new BudgetResponse(true, "ok", dto);
        } catch (Exception e) {
            LOG.error("查詢預算資料失敗", e);
            return new BudgetResponse(false, "查詢預算資料失敗: " + e.getMessage());
        }
    }

    /**
     * 查詢預算交易明細
     */
    @Override
    public BudgetResponse queryBudgetTransaction(QueryBudgetTransaction model) {
        try {
            List<BudgetQueryDto.BudgetTransactionItem> dto = new ArrayList<BudgetQueryDto.BudgetTransactionItem>();

            Specification<NcccBudgetTranscation> spec = (root, query, cb) -> {
                query.distinct(true);
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal(root.get("year"), model.getYear()));

                predicates.add(cb.equal(root.get("version"), model.getVersion()));

                predicates.add(cb.equal(root.get("ouCode"), model.getOuCode()));

                predicates.add(cb.equal(root.get("accounting"), model.getBudgetItemCode()));

                if (model.getSourceTypes() != null && model.getSourceTypes().size() > 0) {
                    predicates.add(root.get("transcationSource").in(model.getSourceTypes()));
                }

                //排除耗用
                predicates.add(cb.notEqual(root.get("transcationType"),"耗用"));

                if (model.getTransactionTypes() != null && model.getTransactionTypes().size() > 0) {
                    predicates.add(root.get("transcationType").in(model.getTransactionTypes()));
                }

                query.orderBy(cb.asc(root.get("transcationDate")));

                return cb.and(predicates.toArray(new Predicate[0]));
            };

            // 查詢預算交易明細
            List<NcccBudgetTranscation> budgetD = ncccBudgetTranscationRepository.findAll(spec);

            // 轉換成DTO
            for (NcccBudgetTranscation budget : budgetD) {
                BudgetQueryDto.BudgetTransactionItem detail = new BudgetQueryDto.BudgetTransactionItem();
                detail.setTransactionDate(budget.getTranscationDate());
                detail.setSourceName(budget.getTranscationSource());
                detail.setSourceNumber(budget.getTranscationNo());
                detail.setTransactionType(budget.getTranscationType());
                detail.setBpNo(budget.getBpNo());
                detail.setDocNo(budget.getDocNo());
                detail.setBpName(budget.getBpName());
                detail.setAmount(budget.getAmount());
                detail.setRemark("");

                SyncUser user = syncUserRepository.findByAccount(budget.getCreateUser());
                if (user != null) {
                    detail.setUserName(user.getDisplayName());
                }

                detail.setCreateDate(budget.getCreateDate());
                dto.add(detail);
            }

            // 依照日期反序排列
            dto.sort((a, b) -> b.getCreateDate().compareTo(a.getCreateDate()));

            return new BudgetResponse(true, "ok", dto);

        } catch (Exception e) {
            LOG.error("查詢預算明細資料失敗", e);
            return new BudgetResponse(false, "查詢預算明細資料失敗: " + e.getMessage());
        }
    }

    // endregion 預算查詢

    // region 預算保留

    /**
     * 儲存預算保留
     */
    @Transactional
    @Override
    public BudgetResponse saveBudgetReserve(BudgetReserveDto model) {
        try {
            NcccUserDto user = SecurityUtil.getCurrentUser();

            NcccBudgetVersion budgetVersion = ncccBudgetVersionRepository.findByYearAndVersion(model.getBudgetYear(),
                    model.getVersion());
            if (budgetVersion == null) {
                return new BudgetResponse(false, "預算版次不存在");
            }
            // 檢查是否已經開帳
            if (budgetVersion.getOpenDate() == null) {
                return new BudgetResponse(false, "預算尚未開帳");
            }
            // 檢查是否已經關帳
            if (budgetVersion.getCloseDate() != null) {
                return new BudgetResponse(false, "預算已經關帳");
            }

            NcccBudgetReserveM reserveM = null;

            String processId = "";
            if (model.getReserveNo() == null || model.getReserveNo().isEmpty()) {
                // 代表新單
                String prefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));
                Integer nextSerial = ncccBudgetReserveMRepository.findMaxSerialByPrefix(prefix) + 1;
                String serialStr = String.format("%04d", nextSerial);
                String exNo = "RS" + prefix + serialStr;

                reserveM = new NcccBudgetReserveM();
                reserveM.setRsvNo(exNo);
                reserveM.setYear(model.getBudgetYear());
                reserveM.setVersion(model.getVersion());
                reserveM.setCreateDate(LocalDateTime.now());
                reserveM.setCreateUser(user.getUserId());

                // region 審核人員塞入

                String processKey = ProcessDefinitionKey.BUDGET_BUDGETRETENTION.getKey();

                Map<String, Object> vars = new HashMap<>();

                vars.put(AssigneeRole.INITIATOR.getKey(), user.getHrid());

                List<String> approvers = new ArrayList<>();

                Optional<SyncUser> foundMgrUser = syncUserRepository.findManagerUserByOuCode(user.getOuCode());

                if(foundMgrUser.isPresent())
                {
                    SyncUser mgrUser = foundMgrUser.get();

                    approvers.add(mgrUser.getHrid());
                }

                vars.put(AssigneeRole.APPROVERS.getKey(), approvers);
                vars.put(AssigneeRole.CURRENT_INDEX.getKey(), 0);
                vars.put(AssigneeRole.CURRENT_APPROVER.getKey(), approvers.get(0));

                // 分派任務(成本經辦)
                vars.put(AssigneeRole.ASSIGN_COST_CLERK.getKey(), bpmNcccMissionAssignerRepository
                        .findByOuCode(AssigneeRole.COST_CLERK.getCode()).getHrid());

                // 成本經辦
                vars.put(AssigneeRole.COST_CLERK.getKey(), null);

                // 成本科長
                setAssignee(vars, AssigneeRole.COST_SECTION_CHIEF);

                // 保留簽的成本經辦
                NcccReserverController reserveController = ncccReserverControllerRepository.findAll().get(0);

                // 保留簽的成本經辦
                vars.put(AssigneeRole.RETENTION_COST_CLERK.getKey(),reserveController.getReserverControllerNo());

                processId = flowableService.startProcess(processKey, exNo, vars);

                reserveM.setTaskId(processId);

                // endregion
            } else {
                // 代表編輯單
                // 查詢主檔單號是否存在
                reserveM = ncccBudgetReserveMRepository.findByRsvNoAndYearAndVersion(model.getReserveNo(),
                        model.getBudgetYear(), model.getVersion());
                if (reserveM == null) {
                    throw new IllegalArgumentException("預算保留資料不存在，請確認單號、年度和版次是否正確");
                }

                processId = reserveM.getTaskId();
            }

            reserveM.setApplyDate(model.getApplyDate());
            reserveM.setApplyUser(model.getEmployeeCode());
            reserveM.setApplyOuCode(model.getDeptCode());
            reserveM.setRemark(model.getRemark());
            reserveM.setFlowStatus("1");
            reserveM.setUpdateUser(user.getUserName());
            reserveM.setUpdateDate(LocalDateTime.now());
            ncccBudgetReserveMRepository.save(reserveM);

            // 清除舊的明細資料
            ncccBudgetReserveDRepository.deleteByRsvNo(reserveM.getRsvNo());

            // 儲存預算保留明細資料
            List<NcccBudgetReserveD> reserveDList = new ArrayList<>();
            int dseqNo = 1; // 明細1序號從1開始
            for (BudgetReserveDto.BudgetReserveDetail detail : model.getDetails()) {
                NcccBudgetReserveD reserveD = new NcccBudgetReserveD();
                reserveD.setRsvNo(reserveM.getRsvNo());
                reserveD.setSeqNo(String.format("%03d", dseqNo));
                reserveD.setPoNo(detail.getPurchaseOrderNo());
                reserveD.setPrNo(detail.getRequestOrderNo());
                reserveD.setPoAmt(detail.getPurchaseAmount());
                reserveD.setPrAmt(detail.getRequestAmount());
                reserveD.setPoRemark(detail.getPurchaseRemark());
                reserveD.setPoDocNo(detail.getPurchaseDocNo());
                reserveD.setPrDocNo(detail.getRequestDocNo());
                reserveD.setResReason(detail.getReserveReason());
                reserveD.setReserverAmt(detail.getReserveAmount());
                reserveD.setAccounting(detail.getBudgetItemCode());
                reserveD.setCreateDate(LocalDateTime.now());
                reserveD.setCreateUser(user.getUserId());
                reserveD.setUpdateUser(user.getUserName());
                reserveD.setUpdateDate(LocalDateTime.now());
                dseqNo++;
                reserveDList.add(reserveD);
            }
            ncccBudgetReserveDRepository.saveAll(reserveDList);

            DecisionVo decisionVo = new DecisionVo();
            decisionVo.setProcessId(processId);
            decisionVo.setDecision(Decision.SUBMIT);
            flowableService.completeTask(decisionVo);

            return new BudgetResponse(true, "ok", null);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
            LOG.error("儲存預算保留失敗", e);
            return new BudgetResponse(false, "儲存預算保留失敗: " + e.getMessage());
        }
    }

    /**
     * 查詢預算保留
     *
     * @param model
     * @return
     */
    @Override
    public BudgetResponse queryBudgetReserve(BudgetRequestDto.QueryBudgetReserve model) {
        try {
            BudgetReserveDto dto = new BudgetReserveDto();

            // 取得主檔資料
            NcccBudgetReserveM budgetReserveM = ncccBudgetReserveMRepository.findByRsvNo(model.getRsvNo());
            if (budgetReserveM == null) {
                return new BudgetResponse(false, "找不到預算保留資料");
            }

            // 將資料填入DTO
            dto.setReserveNo(budgetReserveM.getRsvNo());
            dto.setTaskId(budgetReserveM.getTaskId());
            dto.setBudgetYear(budgetReserveM.getYear());
            dto.setVersion(budgetReserveM.getVersion());
            dto.setApplyDate(budgetReserveM.getApplyDate());
            dto.setEmployeeCode(budgetReserveM.getApplyUser());
            dto.setDeptCode(budgetReserveM.getApplyOuCode());
            dto.setRemark(budgetReserveM.getRemark());
            dto.setStatus("SUBMITTED");
            SyncUser user = syncUserRepository.findByAccount(budgetReserveM.getApplyUser());
            if (user != null) {
                dto.setEmployeeName(user.getDisplayName());
            }
            SyncOU ou = syncOURepository.findByOuCode(budgetReserveM.getApplyOuCode());
            if (ou != null) {
                dto.setDeptName(ou.getOuName());
            }

            // 取得明細資料
            dto.setDetails(new ArrayList<BudgetReserveDto.BudgetReserveDetail>());
            List<NcccBudgetReserveD> details = ncccBudgetReserveDRepository.findByRsvNo(model.getRsvNo());
            for (NcccBudgetReserveD detail : details) {
                BudgetReserveDto.BudgetReserveDetail reserveDetail = new BudgetReserveDto.BudgetReserveDetail();

                reserveDetail.setPurchaseDocNo(detail.getPoDocNo());
                reserveDetail.setPurchaseOrderNo(detail.getPoNo());
                reserveDetail.setPurchaseAmount(detail.getPoAmt());
                reserveDetail.setRequestDocNo(detail.getPrDocNo());
                reserveDetail.setRequestAmount(detail.getPrAmt());
                reserveDetail.setReserveAmount(detail.getReserverAmt());
                reserveDetail.setRequestOrderNo(detail.getPrNo());
                reserveDetail.setPurchaseRemark(detail.getPoRemark());
                reserveDetail.setReserveReason(detail.getResReason());
                reserveDetail.setBudgetItemCode(detail.getAccounting());
                NcccAccountingList accounting = ncccAccountingListRepository.findBySubject(detail.getAccounting());
                if (accounting != null) {
                    reserveDetail.setBudgetItemName(accounting.getEssay());
                }
                dto.getDetails().add(reserveDetail);
            }

            dto.setTaskHistoryList(flowableService.getTaskHistory(budgetReserveM.getTaskId()));
            if (flowableService.checkAtInitiatorTask(budgetReserveM.getTaskId())) {
                dto.setMode(Mode.EDITMODE);
            }
            else
            {
                dto.setMode(Mode.VIEWMODE);

                dto.setCanBackToPrevious(true);
            }

            boolean isSetNextAssignee = flowableService.isSetNextAssignee(budgetReserveM.getTaskId());
            dto.setSetNextAssignee(flowableService.isSetNextAssignee(budgetReserveM.getTaskId()));

            if (isSetNextAssignee) {
                String nextDepId = flowableService.getNextDepId(budgetReserveM.getTaskId());
                SyncOU syncOU = syncOURepository.findByOuCode(nextDepId);
                List<SyncUser> syncUserList = syncUserRepository
                        .findByOuCodeAndAccountNotAndDisabled(syncOU.getOuCode(), syncOU.getMgrAccount(), "0");
                dto.setNextAssigneeList(syncUserList);
            }

            dto.setRetentionCostClerk(flowableService.isRetentionCostClerk(budgetReserveM.getTaskId()));

            return new BudgetResponse(true, "ok", dto);
        } catch (Exception e) {
            LOG.error("取得預算保留單資料失敗", e);
            return new BudgetResponse(false, "取得預算保留單資料失敗: " + e.getMessage());
        }
    }

    /**
     * 列印預算保留
     * @param model
     * @return
     */
    @Override
    public BudgetResponse printBudgetReserve(BudgetRequestDto.QueryBudgetReserve model)
    {
        try
        {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // 取得主檔資料
            NcccBudgetReserveM budgetReserveM = ncccBudgetReserveMRepository.findByRsvNo(model.getRsvNo());
            if (budgetReserveM == null) {
                return new BudgetResponse(false, "找不到預算保留資料");
            }

            String resourceName = "template/budgetRetentionTemplate.docx";

            ClassPathResource resource = new ClassPathResource(resourceName);

            // region 主表

            Map<String, String> replacements = new HashMap<>();
            String Dept = "";
            SyncOU syncOU = syncOURepository.findByOuCode(budgetReserveM.getApplyOuCode());

            if(syncOU != null)
            {
                Dept = syncOU.getOuName();
            }

            replacements.put("{dept}", Dept);
            replacements.put("{year}", budgetReserveM.getYear());
            replacements.put("{remark}", budgetReserveM.getRemark()!= null ?budgetReserveM.getRemark(): "");

            InputStream templateStream = resource.getInputStream();

            XWPFDocument document = new XWPFDocument(templateStream);

            // 替換段落中的文字
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                replaceTextInParagraph(paragraph, replacements);
            }

            // endregion

            // region 明細

            List<NcccBudgetReserveD> budgetReserveDList = ncccBudgetReserveDRepository.findByRsvNo(model.getRsvNo());

            List<XWPFTable> tables = document.getTables();

            XWPFTable table = tables.get(0);

            BigDecimal total = new  BigDecimal(0);

            List<NcccAccountingList> accountingListList = ncccAccountingListRepository.findAll();

            for(NcccBudgetReserveD budgetReserveD : budgetReserveDList.stream().sorted(Comparator.comparing(NcccBudgetReserveD::getSeqNo)).collect(Collectors.toList()))
            {
                total = total.add(budgetReserveD.getReserverAmt());

                XWPFTableRow newRow = table.createRow();

                XWPFTableCell indexCell = newRow.getCell(0);

                indexCell.setText(String.valueOf(budgetReserveD.getSeqNo()));

                XWPFParagraph indexPara = indexCell.getParagraphs().get(0);

                indexPara.setAlignment(ParagraphAlignment.CENTER);

                XWPFTableCell prNoCell = newRow.getCell(1);

                prNoCell.setText(budgetReserveD.getPrNo());

                XWPFTableCell poNoCell = newRow.getCell(2);

                poNoCell.setText(budgetReserveD.getPoNo());

                XWPFTableCell accountingCell = newRow.getCell(3);

                accountingCell.setText(budgetReserveD.getAccounting());

                XWPFTableCell accountNameCell = newRow.getCell(4);

                String accountName = "";

                Optional<NcccAccountingList> foundAccountingList = accountingListList.stream()
                        .filter(x -> x.getSubject().equals(budgetReserveD.getAccounting()))
                        .findFirst();

                if(foundAccountingList.isPresent())
                {
                    NcccAccountingList thisAccountingList = foundAccountingList.get();

                    accountName = thisAccountingList.getEssay();
                }

                accountNameCell.setText(accountName);

                XWPFTableCell reserverAmtCell = newRow.getCell(5);

                reserverAmtCell.setText(MoneyUtil.formatTWD(budgetReserveD.getReserverAmt()));

                XWPFParagraph reserverAmtPara = reserverAmtCell.getParagraphs().get(0);

                reserverAmtPara.setAlignment(ParagraphAlignment.RIGHT);

                XWPFTableCell poRemarkCell = newRow.getCell(6);

                poRemarkCell.setText(budgetReserveD.getPoRemark());

                XWPFTableCell resReasonCell = newRow.getCell(7);

                resReasonCell.setText(budgetReserveD.getResReason());

                XWPFTableCell prDocNoCell = newRow.getCell(8);

                prDocNoCell.setText(budgetReserveD.getPrDocNo());

                XWPFTableCell poDocNoCell = newRow.getCell(9);

                poDocNoCell.setText(budgetReserveD.getPoDocNo());

                //設定文字大小
                for (XWPFTableCell cell : newRow.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        for (XWPFRun run : paragraph.getRuns()) {
                            run.setFontSize(6); // 設定文字大小為 6pt
                        }
                    }
                }
            }

            XWPFTableRow newRow = table.createRow();


            XWPFTableCell totalNameCell = newRow.getCell(4);

            totalNameCell.setText("合 計");

            XWPFTableCell totalCell = newRow.getCell(5);

            totalCell.setText(MoneyUtil.formatTWD(total));

            XWPFParagraph totalPara = totalCell.getParagraphs().get(0);

            totalPara.setAlignment(ParagraphAlignment.RIGHT);

            //設定文字大小
            for (XWPFTableCell cell : newRow.getTableCells()) {
                for (XWPFParagraph paragraph : cell.getParagraphs()) {
                    for (XWPFRun run : paragraph.getRuns()) {
                        run.setFontSize(6); // 設定文字大小為 6pt
                    }
                }
            }

            // endregion

            document.write(outputStream);
            document.close();

            return new BudgetResponse(true, "ok",outputStream.toByteArray());
        }
        catch (Exception e)
        {
            LOG.error("列印預算保留單資料失敗", e);
            return new BudgetResponse(false, "列印預算保留單資料失敗: " + e.getMessage());
        }
    }

    /**
     * 預算保留動作判斷
     * @param vo
     * @return
     */
    @Override
    public BudgetResponse decisionBudgetReserve(DecisionVo vo) {

        try
        {
            boolean flag = flowableService.completeTask(vo);

            if (flag) {

                switch(vo.getDecision())
                {
                    case APPROVE:

                        if(flowableService.isFinish(vo.getProcessId()))
                        {
                            return approveBudgetReserve(vo.getProcessId());
                        }
                        else
                        {
                            return new BudgetResponse(true, "ok");
                        }

                    case INVALID:

                        return voidBudgetReserve(vo.getProcessId());

                    case BACK:
                    case RETURN:

                        if(flowableService.checkAtInitiatorTask(vo.getProcessId()))
                        {
                            return returnBudgetReserve(vo.getProcessId());
                        }
                        else
                        {
                            return new BudgetResponse(true, "ok");
                        }


                    default:

                        return new BudgetResponse(true, "ok");

                }

            } else {
                return new BudgetResponse(false, "簽核預算保留失敗");
            }

        }
        catch (Exception e)
        {
            LOG.error("核可預算保留失敗", e);
            return new BudgetResponse(false, "簽核預算保留失敗: " + e.getMessage());
        }


    }

    /**
     * 預算保留指派成本經辦
     *
     * @param assignTasksVo model
     */
    @Override
    public String setBudgetReserveNextAssignee(AssignTasksVo assignTasksVo) {
        flowableService.setNextAssignee(assignTasksVo.getProcessId(), assignTasksVo.getNextAssignee());
        return "指派任務完成";
    }

    /**
     * 預算保留退回
     *
     * @param taskId
     * @return
     */
    public BudgetResponse returnBudgetReserve(String taskId) {
        try {
            // 取得主檔資料
            NcccBudgetReserveM budgetReserveM = ncccBudgetReserveMRepository.findByTaskId(taskId);
            if (budgetReserveM == null) {
                return new BudgetResponse(false, "找不到預算保留單資料");
            }

            return new BudgetResponse(true, "ok");
        } catch (Exception e) {
            LOG.error("退回預算保留單失敗", e);
            return new BudgetResponse(false, "退回預算保留單失敗: " + e.getMessage());
        }
    }

    /**
     * 預算保留核可
     *
     * @param taskId
     * @return
     */
    public BudgetResponse approveBudgetReserve(String taskId) {
        try {
            // 取得主檔資料
            NcccBudgetReserveM budgetReserveM = ncccBudgetReserveMRepository.findByTaskId(taskId);
            if (budgetReserveM == null) {
                return new BudgetResponse(false, "找不到預算保留單資料");
            }

            NcccUserDto user = SecurityUtil.getCurrentUser();
            budgetReserveM.setFlowStatus("2");
            budgetReserveM.setUpdateDate(LocalDateTime.now());
            budgetReserveM.setUpdateUser(user.getUserName());
            ncccBudgetReserveMRepository.save(budgetReserveM);

            return new BudgetResponse(true, "ok");
        } catch (Exception e) {
            LOG.error("核可預算保留單失敗", e);
            return new BudgetResponse(false, "核可預算保留單失敗: " + e.getMessage());
        }
    }

    /**
     * 預算保留作廢
     *
     * @param taskId
     * @return
     */
    public BudgetResponse voidBudgetReserve(String taskId) {
        try {
            // 取得主檔資料
            NcccBudgetReserveM budgetReserveM = ncccBudgetReserveMRepository.findByTaskId(taskId);
            if (budgetReserveM == null) {
                return new BudgetResponse(false, "找不到預算保留單資料");
            }

            NcccUserDto user = SecurityUtil.getCurrentUser();
            budgetReserveM.setFlowStatus("3");
            budgetReserveM.setUpdateDate(LocalDateTime.now());
            budgetReserveM.setUpdateUser(user.getUserName());
            ncccBudgetReserveMRepository.save(budgetReserveM);

            return new BudgetResponse(true, "ok");
        } catch (Exception e) {
            LOG.error("作廢預算保留單失敗", e);
            return new BudgetResponse(false, "作廢預算保留單失敗: " + e.getMessage());
        }
    }

    // endregion 預算保留

    // region 費用提列

    /**
     * 提列費用儲存
     *
     * @param model
     * @return
     */
    @Transactional
    @Override
    public BudgetResponse saveBudgetExpense(BudgetExpenseDto model) {
        try {
            NcccUserDto user = SecurityUtil.getCurrentUser();

            NcccBudgetVersion budgetVersion = ncccBudgetVersionRepository.findByYearAndVersion(model.getBudgetYear(),
                    model.getVersion());
            if (budgetVersion == null) {
                return new BudgetResponse(false, "預算版次不存在");
            }
            // 檢查是否已經開帳
            if (budgetVersion.getOpenDate() == null) {
                return new BudgetResponse(false, "預算尚未開帳");
            }
            // 檢查是否已經關帳
            if (budgetVersion.getCloseDate() != null) {
                return new BudgetResponse(false, "預算已經關帳");
            }

            NcccProvisionExpenseM ExpenseM = null;

            String processId = "";
            if (model.getProvisionNo() == null || model.getProvisionNo().isEmpty()) {
                // 代表新單
                String prefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));
                Integer nextSerial = ncccProvisionExpenseMRepository.findMaxSerialByPrefix(prefix) + 1;
                String serialStr = String.format("%04d", nextSerial);
                String exNo = "AP" + prefix + serialStr;

                ExpenseM = new NcccProvisionExpenseM();
                ExpenseM.setProvisionNo(exNo);
                ExpenseM.setYear(model.getBudgetYear());
                ExpenseM.setVersion(model.getVersion());
                ExpenseM.setCreateDate(LocalDateTime.now());
                ExpenseM.setCreateUser(user.getUserId());

                // region 審核人員塞入

                String processKey = ProcessDefinitionKey.BUDGET_BUDGETMANAGEMENT.getKey();

                Map<String, Object> vars = new HashMap<>();

                vars.put(AssigneeRole.INITIATOR.getKey(), user.getHrid());

                List<String> approvers = new ArrayList<>();

                Optional<SyncUser> foundMgrUser = syncUserRepository.findManagerUserByOuCode(user.getOuCode());

                if(foundMgrUser.isPresent())
                {
                    SyncUser mgrUser = foundMgrUser.get();

                    approvers.add(mgrUser.getHrid());
                }

                vars.put(AssigneeRole.APPROVERS.getKey(), approvers);
                vars.put(AssigneeRole.CURRENT_INDEX.getKey(), 0);
                vars.put(AssigneeRole.CURRENT_APPROVER.getKey(), approvers.get(0));

                // 分派任務(成本經辦)
                vars.put(AssigneeRole.ASSIGN_COST_CLERK.getKey(), bpmNcccMissionAssignerRepository
                        .findByOuCode(AssigneeRole.COST_CLERK.getCode()).getHrid());

                // 成本經辦
                vars.put(AssigneeRole.COST_CLERK.getKey(), null);

                // 成本科長
                setAssignee(vars, AssigneeRole.COST_SECTION_CHIEF);

                processId = flowableService.startProcess(processKey, exNo, vars);

                ExpenseM.setTaskID(processId);

                // endregion


            } else {
                // 代表編輯單
                // 查詢主檔單號是否存在
                ExpenseM = ncccProvisionExpenseMRepository.findByProvisionNoAndYearAndVersion(model.getProvisionNo(),
                        model.getBudgetYear(), model.getVersion());
                if (ExpenseM == null) {
                    throw new IllegalArgumentException("費用提列資料不存在，請確認單號、年度和版次是否正確");
                }

                processId = ExpenseM.getTaskID();
            }

            ExpenseM.setMonth(model.getMonth());
            ExpenseM.setTotalAmount(model.getTotalAmount());
            ExpenseM.setApplyDate(model.getApplyDate());
            ExpenseM.setApplyUser(model.getEmployeeCode());
            ExpenseM.setApplyOuCode(model.getDeptCode());
            ExpenseM.setAccounting(model.getBudgetItemCode());
            ExpenseM.setPurchaseNo(model.getPurchaseOrderNo());
            ExpenseM.setPostingDate(model.getPostingDate());
            ExpenseM.setDocNo(model.getSapDocumentNo());
            ExpenseM.setUpdateUser(user.getUserName());
            ExpenseM.setUpdateDate(LocalDateTime.now());
            ExpenseM.setFlowStatus("1");
            ncccProvisionExpenseMRepository.save(ExpenseM);

            // 清除舊的明細資料
            ncccProvisionExpenseDRepository.deleteByProvisionNo(ExpenseM.getProvisionNo());
            ncccProvisionExpenseD1Repository.deleteByProvisionNo(ExpenseM.getProvisionNo());

            // 儲存費用提列明細資料
            List<NcccProvisionExpenseD> provisionDList = new ArrayList<>();
            List<NcccProvisionExpenseD1> provisionD1List = new ArrayList<>();

            int dseqNo = 1; // 明細1序號從1開始
            for (BudgetExpenseDto.BudgetExpenseDetail detail : model.getTransferDetails()) {
                NcccProvisionExpenseD provisionD = new NcccProvisionExpenseD();
                provisionD.setProvisionNo(ExpenseM.getProvisionNo());
                provisionD.setSeqNo(String.format("%03d", dseqNo));
                provisionD.setOuCode(detail.getBudgetDeptCode());
                provisionD.setAllocateMethod(detail.getAllocateMethod());
                provisionD.setAmount(detail.getProvisionAmount());
                provisionD.setRemark(detail.getSummary());
                provisionD.setCreateDate(LocalDateTime.now());
                provisionD.setCreateUser(user.getUserId());
                provisionD.setUpdateUser(user.getUserName());
                provisionD.setUpdateDate(LocalDateTime.now());
                provisionDList.add(provisionD);

                // 儲存明細1資料
                int d1seqNo = 1; // 明細1序號從1開始
                for (BudgetExpenseDto.BudgetExpenseOperationItem detail1 : detail.getOperationItems()) {
                    NcccProvisionExpenseD1 provisionD1 = new NcccProvisionExpenseD1();
                    provisionD1.setProvisionNo(ExpenseM.getProvisionNo());
                    provisionD1.setSeqNo(String.format("%03d", dseqNo));
                    provisionD1.setSeqNo1(String.format("%03d", d1seqNo));
                    provisionD1.setOperateItemCode(detail1.getCode());
                    provisionD1.setOperateItem(detail1.getName());
                    provisionD1.setOperateAmt(detail1.getAmount());
                    provisionD1.setOperateRatio(detail1.getRatio());
                    provisionD1.setCreateDate(LocalDate.now());
                    provisionD1.setCreateUser(user.getUserId());
                    provisionD1.setUpdateDate(LocalDate.now());
                    provisionD1.setUpdateUser(user.getUserId());

                    provisionD1List.add(provisionD1);
                    d1seqNo++; // 明細1序號遞增
                }

                dseqNo++;

                // 寫預算主檔<動用>
                BudgetVo.BudgetTranscation transOut = new BudgetVo.BudgetTranscation();
                transOut.setYear(ExpenseM.getYear());
                transOut.setVersion(ExpenseM.getVersion());
                transOut.setOuCode(detail.getBudgetDeptCode());
                transOut.setAccounting(ExpenseM.getAccounting());
                transOut.setTranscationSource("提列費用");
                transOut.setTranscationType("動用");
                transOut.setTranscationDate(ExpenseM.getPostingDate());
                transOut.setTranscationNo(ExpenseM.getProvisionNo());
                transOut.setAmount(detail.getProvisionAmount());
                transOut.setCreateDate(LocalDateTime.now());
                transOut.setCreateUser(ExpenseM.getCreateUser());
                transOut.setBpName("");
                transOut.setBpNo("");
                transOut.setDocNo("");
                WriteBudgetTranscation(transOut);

            }
            ncccProvisionExpenseDRepository.saveAll(provisionDList);
            ncccProvisionExpenseD1Repository.saveAll(provisionD1List);

            DecisionVo decisionVo = new DecisionVo();
            decisionVo.setProcessId(processId);
            decisionVo.setDecision(Decision.SUBMIT);
            flowableService.completeTask(decisionVo);

            return new BudgetResponse(true, "ok", null);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
            LOG.error("儲存提列費用失敗", e);
            return new BudgetResponse(false, "儲存提列費用失敗: " + e.getMessage());
        }
    }

    /**
     * 查詢費用提列
     * @param model
     * @return
     */
    @Override
    public BudgetResponse queryBudgetExpense(QueryBudgetExpense model) {

        try {
            BudgetExpenseDto dto = new BudgetExpenseDto();

            // 取得主檔資料
            NcccProvisionExpenseM provisionExpenseM = ncccProvisionExpenseMRepository.findByProvisionNo(model.getProvisionNo());
            if (provisionExpenseM == null) {
                return new BudgetResponse(false, "找不到費用提列單資料");
            }

            // 將資料填入DTO
            dto.setProvisionNo(provisionExpenseM.getProvisionNo());
            dto.setTaskId(provisionExpenseM.getTaskID());
            dto.setBudgetYear(provisionExpenseM.getYear());
            dto.setVersion(provisionExpenseM.getVersion());
            dto.setApplyDate(provisionExpenseM.getApplyDate());
            dto.setEmployeeCode(provisionExpenseM.getApplyUser());
            dto.setDeptCode(provisionExpenseM.getApplyOuCode());
            dto.setBudgetItemCode(provisionExpenseM.getAccounting());
            dto.setPurchaseOrderNo(provisionExpenseM.getPurchaseNo());
            dto.setPostingDate(provisionExpenseM.getPostingDate());
            dto.setSapDocumentNo(provisionExpenseM.getDocNo());
            dto.setMonth(provisionExpenseM.getMonth());
            dto.setTotalAmount(provisionExpenseM.getTotalAmount());
            SyncUser user = syncUserRepository.findByAccount(provisionExpenseM.getApplyUser());
            if (user != null) {
                dto.setEmployeeName(user.getDisplayName());
            }
            SyncOU ou = syncOURepository.findByOuCode(provisionExpenseM.getApplyOuCode());
            if (ou != null) {
                dto.setDeptName(ou.getOuName());
            }
            NcccAccountingList accounting = ncccAccountingListRepository.findBySubject(provisionExpenseM.getAccounting());
            if (accounting != null) {
                dto.setBudgetItemName(accounting.getEssay());
            }

            // 取得明細資料
            dto.setTransferDetails(new ArrayList<BudgetExpenseDto.BudgetExpenseDetail>());
            List<NcccProvisionExpenseD> details = ncccProvisionExpenseDRepository.findByProvisionNo(model.getProvisionNo());
            for (NcccProvisionExpenseD detail : details) {
                BudgetExpenseDto.BudgetExpenseDetail transferDetail = new BudgetExpenseDto.BudgetExpenseDetail();
                transferDetail.setBudgetDeptCode(detail.getOuCode());
                transferDetail.setAllocateMethod(detail.getAllocateMethod());
                transferDetail.setProvisionAmount(detail.getAmount());
                transferDetail.setSummary(detail.getRemark());
                SyncOU outOU = syncOURepository.findByOuCode(detail.getOuCode());
                if (outOU != null) {
                    transferDetail.setBudgetDeptName(outOU.getOuName());
                }

                // 載入作業項目
                List<BudgetExpenseDto.BudgetExpenseOperationItem> operationItems = new ArrayList<>();
                List<NcccProvisionExpenseD1> dDetails = ncccProvisionExpenseD1Repository.findByProvisionNo(model.getProvisionNo(), detail.getSeqNo());
                for (NcccProvisionExpenseD1 dDetail : dDetails) {
                    BudgetExpenseDto.BudgetExpenseOperationItem operationItem = new BudgetExpenseDto.BudgetExpenseOperationItem();
                    operationItem.setCode(dDetail.getOperateItemCode());
                    operationItem.setName(dDetail.getOperateItem());
                    operationItem.setAmount(dDetail.getOperateAmt());
                    operationItem.setRatio(dDetail.getOperateRatio());
                    operationItems.add(operationItem);
                }
                transferDetail.setOperationItems(operationItems);
                dto.getTransferDetails().add(transferDetail);
            }

            dto.setTaskHistoryList(flowableService.getTaskHistory(provisionExpenseM.getTaskID()));
            if (flowableService.checkAtInitiatorTask(provisionExpenseM.getTaskID())) {
                dto.setMode(Mode.EDITMODE);
            }
            else
            {
                dto.setMode(Mode.VIEWMODE);
            }

            boolean isSetNextAssignee = flowableService.isSetNextAssignee(provisionExpenseM.getTaskID());
            dto.setSetNextAssignee(flowableService.isSetNextAssignee(provisionExpenseM.getTaskID()));

            if (isSetNextAssignee) {
                String nextDepId = flowableService.getNextDepId(provisionExpenseM.getTaskID());
                SyncOU syncOU = syncOURepository.findByOuCode(nextDepId);
                List<SyncUser> syncUserList = syncUserRepository
                        .findByOuCodeAndAccountNotAndDisabled(syncOU.getOuCode(), syncOU.getMgrAccount(), "0");
                dto.setNextAssigneeList(syncUserList);
            }
            if (flowableService.canReturnToInitiator(provisionExpenseM.getTaskID())) {
                dto.setCanReturnToInitiator(true);
            } else {
                dto.setCanBackToPrevious(true);
            }

            return new BudgetResponse(true, "ok", dto);
        } catch (Exception e) {
            LOG.error("取得費用提列單資料失敗", e);
            return new BudgetResponse(false, "取得費用提列單資料失敗: " + e.getMessage());
        }
    }


    /**
     * 提列費用動作判斷
     * @param vo
     * @return
     */
    @Override
    public BudgetResponse decisionBudgetExpense(DecisionVo vo) {

        try
        {
            boolean flag = flowableService.completeTask(vo);

            if (flag) {

                switch(vo.getDecision())
                {
                    case APPROVE:

                        if(flowableService.isFinish(vo.getProcessId()))
                        {
                            return approveBudgetExpense(vo.getProcessId());
                        }
                        else
                        {
                            return new BudgetResponse(true, "ok");
                        }

                    case INVALID:

                        return voidBudgetExpense(vo.getProcessId());

                    case BACK:
                    case RETURN:

                        if(flowableService.checkAtInitiatorTask(vo.getProcessId()))
                        {
                            return returnBudgetExpense(vo.getProcessId());
                        }
                        else
                        {
                            return new BudgetResponse(true, "ok");
                        }

                    default:

                        return new BudgetResponse(true, "ok");

                }

            } else {
                return new BudgetResponse(false, "簽核提列費用失敗");
            }

        }
        catch (Exception e)
        {
            LOG.error("核可提列費用失敗", e);
            return new BudgetResponse(false, "簽核提列費用失敗: " + e.getMessage());
        }


    }

    /**
     * TODO 指派成本經辦
     *
     * @param assignTasksVo
     * @return
     */
    @Override
    public String setBudgetExpenseNextAssignee(AssignTasksVo assignTasksVo) {
        flowableService.setNextAssignee(assignTasksVo.getProcessId(), assignTasksVo.getNextAssignee());
        return "指派任務完成";
    }

    /**
     * 提列費用核可
     * @param taskId
     * @return
     */
    public BudgetResponse approveBudgetExpense(String taskId){
        try {
            // 取得主檔資料
            NcccProvisionExpenseM provisionExpenseM = ncccProvisionExpenseMRepository.findByTaskId(taskId);
            if (provisionExpenseM == null) {
                return new BudgetResponse(false, "找不到提列費用資料");
            }
            NcccUserDto user = SecurityUtil.getCurrentUser();
            provisionExpenseM.setFlowStatus("2");
            provisionExpenseM.setUpdateDate(LocalDateTime.now());
            provisionExpenseM.setUpdateUser(user.getUserName());
            ncccProvisionExpenseMRepository.save(provisionExpenseM);

            return new BudgetResponse(true, "ok");
        } catch (Exception e) {
            LOG.error("核可提列費用失敗", e);
            return new BudgetResponse(false, "核可提列費用失敗: " + e.getMessage());
        }
    }

    /**
     * 提列費用作廢
     * @param taskId
     * @return
     */
    public BudgetResponse voidBudgetExpense(String taskId){
        try {
            // 取得主檔資料
            NcccProvisionExpenseM provisionExpenseM = ncccProvisionExpenseMRepository.findByTaskId(taskId);
            if (provisionExpenseM == null) {
                return new BudgetResponse(false, "找不到提列費用資料");
            }
            NcccUserDto user = SecurityUtil.getCurrentUser();
            provisionExpenseM.setFlowStatus("3");
            provisionExpenseM.setUpdateDate(LocalDateTime.now());
            provisionExpenseM.setUpdateUser(user.getUserName());
            ncccProvisionExpenseMRepository.save(provisionExpenseM);

            return new BudgetResponse(true, "ok");
        } catch (Exception e) {
            LOG.error("作廢提列費用失敗", e);
            return new BudgetResponse(false, "作廢提列費用失敗: " + e.getMessage());
        }
    }

    /**
     * 提列費用退回
     * @param taskId
     * @return
     */
    @Transactional
    public BudgetResponse returnBudgetExpense(String taskId){
        try {
            // 取得主檔資料
            NcccProvisionExpenseM provisionExpenseM = ncccProvisionExpenseMRepository.findByTaskId(taskId);
            if (provisionExpenseM == null) {
                return new BudgetResponse(false, "找不到提列費用資料");
            }

            // 退回給申請人時
            if(true){
                // 取得明細資料
                List<NcccProvisionExpenseD> details = ncccProvisionExpenseDRepository.findByProvisionNo(provisionExpenseM.getProvisionNo());
                for (NcccProvisionExpenseD detail : details) {

                    // 寫預算主檔<動用>
                    BudgetVo.BudgetTranscation transOut = new BudgetVo.BudgetTranscation();
                    transOut.setYear(provisionExpenseM.getYear());
                    transOut.setVersion(provisionExpenseM.getVersion());
                    transOut.setOuCode(detail.getOuCode());
                    transOut.setAccounting(provisionExpenseM.getAccounting());
                    transOut.setTranscationSource("提列費用");
                    transOut.setTranscationType("動用");
                    transOut.setTranscationDate(provisionExpenseM.getPostingDate());
                    transOut.setTranscationNo(provisionExpenseM.getProvisionNo());
                    transOut.setAmount(detail.getAmount().multiply(BigDecimal.valueOf(-1)));
                    transOut.setCreateDate(LocalDateTime.now());
                    transOut.setCreateUser(provisionExpenseM.getCreateUser());
                    transOut.setBpName("");
                    transOut.setBpNo("");
                    transOut.setDocNo("");
                    WriteBudgetTranscation(transOut);
                }
            }

            return new BudgetResponse(true, "ok");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
            LOG.error("退回提列費用失敗", e);
            return new BudgetResponse(false, "退回提列費用失敗: " + e.getMessage());
        }
    }

    // endregion 費用提列

    // region 通用api

    /**
     * 查詢預算餘額
     * @param model 查詢模型
     * @return
     */
    public BudgetResponse getBudgetBalance(BudgetVo.SearchBudgetBalanceData model) {
      try {
        String ouCode;
        SyncOU syncOU = syncOURepository.findByOuCode(model.getOuCode());
        if (syncOU == null) {
          return new BudgetResponse(false, "部門代碼錯誤");
        }
        int level;
        try {
          level = Integer.parseInt(String.valueOf(syncOU.getOuLevel()).trim());
        } catch (NumberFormatException e) {
          return new BudgetResponse(false, "無效的 ouLevel: " + syncOU.getOuLevel());
        }
        if (level < 30) {
          ouCode = "1200";
        } else if (level == 30) {
          ouCode = syncOU.getOuCode();
        } else {
          // level > 30，往上找
          ouCode = null;
          String parentCode = syncOU.getParentOUCode();

          while (parentCode != null && !parentCode.isEmpty()) {
            SyncOU parent = syncOURepository.findByOuCode(parentCode);
            if (parent == null) {
              return new BudgetResponse(false, "找不到父層 OU: " + parentCode);
            }

            int parentLevel;
            try {
              parentLevel = Integer.parseInt(String.valueOf(parent.getOuLevel()).trim());
            } catch (NumberFormatException e) {
              return new BudgetResponse(false, "無效的父層 ouLevel: " + parent.getOuLevel());
            }

            if (parentLevel < 30) {
              // 理論上不會發生；依規則 <30 就固定回 1200
              ouCode = "1200";
              break;
            } else if (parentLevel == 30) {
              ouCode = parent.getOuCode();
              break;
            } else {
              // 繼續往上
              parentCode = parent.getParentOUCode();
            }
          }

          if (ouCode == null) {
            return new BudgetResponse(false, "往上找不到 level=30 的 OU");
          }
        }
        BudgetVo.BudgetBalanceData Result = ncccBudgetMRepository
            .GetBudgetBalanceByCondition(model.getYear(), model.getAccounting(), ouCode);

        if (Result == null) {
          return new BudgetResponse(false, "查無資料");
        }

        return new BudgetResponse(true, "成功", Result);
      } catch (Exception e) {
        return new BudgetResponse(false, "連線異常");
      }
    }

    /**
     * 查詢預算餘額
     * @param model 查詢模型
     * @return
     */
    @Override
    public BudgetResponse getBudgetBalanceList(BudgetVo.SearchBudgetBalanceListData model) {
        try {
            List<BudgetVo.BudgetBalanceData> result = new ArrayList<>();
            for(BudgetVo.SearchBudgetBalanceData searchData : model.getSearchList())
            {
                BudgetVo.BudgetBalanceData thisData = ncccBudgetMRepository
                        .GetBudgetBalanceByCondition(searchData.getYear(), searchData.getAccounting(), searchData.getOuCode());

                if (thisData != null) {
                    result.add(thisData);
                }
            }

            return new BudgetResponse(true, "成功", result);
        } catch (Exception e) {
            return new BudgetResponse(false, "連線異常");
        }
    }



    /**
     * 寫預算交易明細紀錄
     *
     * @param transcation 模型
     */
    @Override
    public void WriteBudgetTranscation(BudgetVo.BudgetTranscation transcation) {

        //查詢對應預算
        NcccBudgetM budget = ncccBudgetMRepository.findByYearAndVersionAndOuCodeAndAccounting(
                transcation.getYear(),
                transcation.getVersion(), transcation.getOuCode(), transcation.getAccounting());
        if (budget == null) {
            throw new IllegalArgumentException("預算主檔不存在，請確認部門、預算項目是否正確");
        }

        // 根據異動類型對對應預算欄位作增減
        switch (transcation.getTranscationType())
        {
            case "動用":

                budget.setUseAmt((budget.getUseAmt() == null ? BigDecimal.ZERO : budget.getUseAmt()).add(transcation.getAmount()));

                break;

            case "占用":

                budget.setOccupyAmt((budget.getOccupyAmt() == null ? BigDecimal.ZERO : budget.getOccupyAmt()).add(transcation.getAmount()));

                break;

            case "耗用":

                budget.setConsumeAmt((budget.getConsumeAmt() == null ? BigDecimal.ZERO : budget.getConsumeAmt()).add(transcation.getAmount()));

                break;

            case "勻入":

                budget.setAllocIncreaseAmt((budget.getAllocIncreaseAmt() == null ? BigDecimal.ZERO : budget.getAllocIncreaseAmt()).add(transcation.getAmount()));

                break;

            case "勻出":

                budget.setAllocReduseAmt((budget.getAllocReduseAmt() == null ? BigDecimal.ZERO : budget.getAllocReduseAmt()).add(transcation.getAmount()));

                break;

            case "保留預算":

                budget.setReserveBudget((budget.getReserveBudget() == null ? BigDecimal.ZERO : budget.getReserveBudget()).add(transcation.getAmount()));

                break;
        }

        ncccBudgetMRepository.save(budget);

        // 寫預算紀錄檔
        NcccBudgetTranscation budgetTranscation = new NcccBudgetTranscation();
        budgetTranscation.setYear(transcation.getYear());
        budgetTranscation.setVersion(transcation.getVersion());
        budgetTranscation.setOuCode(transcation.getOuCode());
        budgetTranscation.setAccounting(transcation.getAccounting());
        budgetTranscation.setTranscationSource(transcation.getTranscationSource());
        budgetTranscation.setTranscationType(transcation.getTranscationType());
        budgetTranscation.setTranscationDate(transcation.getTranscationDate());
        budgetTranscation.setTranscationNo(transcation.getTranscationNo());
        budgetTranscation.setAmount(transcation.getAmount());
        budgetTranscation.setBpNo(transcation.getBpNo());
        budgetTranscation.setBpName(transcation.getBpName());
        budgetTranscation.setDocNo(transcation.getDocNo());
        budgetTranscation.setCreateDate(transcation.getCreateDate());
        budgetTranscation.setCreateUser(transcation.getCreateUser());
        ncccBudgetTranscationRepository.save(budgetTranscation);
    }

    /**
     * 取得對應預算部門
     * @param ouCode
     * @return
     */
    public BudgetResponse getBudgetOuCodeByOuCode(String ouCode)
    {
        String result = "";

        NcccCostcenterOrgMapping costcenterOrgMapping = ncccCostcenterOrgMappingRepository.findByHrDepCodeAct(ouCode);

        if(costcenterOrgMapping != null)
        {
            result = costcenterOrgMapping.getBudgetOuCode();
        }

        return new BudgetResponse(true, "成功", result);
    }

    // endregion

    // region 權責分攤

    /**
     * 權責分攤_取得採購單資料
     * @return
     */
    @Override
    public BudgetResponse getAllocationPOData() {
        try {
            List<BudgetResponseibilityAllocationDto.POData> dto = new ArrayList<>();

            // 查詢採購單資料
            List<NcccAllocationM> allocationM = ncccAllocationMRepository.GetPoList();
            for (NcccAllocationM allocation : allocationM) {
                BudgetResponseibilityAllocationDto.POData item = new BudgetResponseibilityAllocationDto.POData();
                item.setPurchaseOrderNo(allocation.getPoNo());
                item.setBudgetDeptCode(allocation.getOuCode());
                item.setBudgetDeptName(allocation.getOuName());
                item.setBudgetAccountCode(allocation.getAccounting());
                item.setBudgetAccountName(allocation.getSubject());
                item.setUntaxAmount(allocation.getTotal());
                item.setRemainingAmount(allocation.getCalcelTotal());
                item.setAllocatableAmount(allocation.getTotalRemain());
                dto.add(item);
            }

            return new BudgetResponse(true, "ok", dto);
        } catch (Exception e) {
            LOG.error("取得採購單資料失敗", e);
            return new BudgetResponse(false, "取得採購單資料失敗: " + e.getMessage());
        }
    }

    /**
     * 權責分攤_取得明細資料
     * @return
     */
    @Override
    public BudgetResponse getAllocationDetails(ReqDetailByPoNo model) {
        try {
            List<BudgetResponseibilityAllocationDto.DetailData> dto = new ArrayList<>();

            // 查詢明細資料
            for (var poNo : model.purchaseOrderNo) {

                List<NcccAllocationM> allocationM = ncccAllocationMRepository.GetAllByPoNo(poNo);
                if (allocationM.isEmpty()) {
                    return new BudgetResponse(false, "找不到採購單資料: " + poNo);
                }

                for (NcccAllocationM allocation : allocationM) {
                    BudgetResponseibilityAllocationDto.DetailData item = new BudgetResponseibilityAllocationDto.DetailData();
                    item.setPurchaseOrderNo(allocation.getPoNo());
                    item.setPurchaseItemNo(allocation.getPoItemNo());
                    item.setSupplierNo(allocation.getBpNo());
                    item.setSupplierName(allocation.getBpName());
                    item.setBudgetDeptCode(allocation.getOuCode());
                    item.setBudgetDeptName(allocation.getOuName());
                    item.setBudgetAccountCode(allocation.getAccounting());
                    item.setBudgetAccountName(allocation.getSubject());
                    item.setItemText(allocation.getItemText());
                    item.setUntaxAmount(allocation.getTotal());
                    item.setRemainingAmount(allocation.getCalcelTotal());
                    item.setAllocatableAmount(allocation.getTotalRemain());

                    //載入分攤項目
                    List<NcccAllocationD> allocationD = ncccAllocationDRepository.findByPoNoAndPoItemNo(
                            allocation.getPoNo(), allocation.getPoItemNo());
                    List<BudgetResponseibilityAllocationDto.allocationSubData> subItems = new ArrayList<>();
                    for (NcccAllocationD allocationD1 : allocationD) {
                        BudgetResponseibilityAllocationDto.allocationSubData subItem = new BudgetResponseibilityAllocationDto.allocationSubData();
                        subItem.setPlanNo(allocationD1.getPlan());
                        subItem.setYear(allocationD1.getYear());
                        subItem.setMonth(allocationD1.getMonth());
                        subItem.setAdjustAmount(allocationD1.getAdjustTotal());
                        subItem.setAllocationAmount(allocationD1.getAllocationTotal());
                        subItem.setFinalAmount(allocationD1.getAdjustTotal().add(allocationD1.getAllocationTotal()));
                        subItem.setSapDocNo(allocationD1.getSapDocNo());
                        subItem.setPostDate(allocationD1.getPostDate());
                        subItems.add(subItem);
                    }
                    item.setAllocationSubData(subItems);

                    //載入作業項目
                    List<NcccAllocationD1> allocationD1 = ncccAllocationD1Repository.findByPoNoAndPoItemNo(
                            allocation.getPoNo(), allocation.getPoItemNo());
                    List<BudgetResponseibilityAllocationDto.operationItems> operationItems = new ArrayList<>();
                    for (NcccAllocationD1 allocationD11 : allocationD1) {
                        BudgetResponseibilityAllocationDto.operationItems operationItem = new BudgetResponseibilityAllocationDto.operationItems();
                        operationItem.setCode(allocationD11.getOperateItemCode());
                        operationItem.setName(allocationD11.getOperateItem());
                        operationItem.setAmount(allocationD11.getOperateAmt());
                        operationItem.setRatio(allocationD11.getOperateRatio());
                        operationItems.add(operationItem);
                    }
                    item.setOperationItems(operationItems);

                    dto.add(item);
                }

            }

            return new BudgetResponse(true, "ok", dto);
        } catch (Exception e) {
            LOG.error("取得明細資料失敗", e);
            return new BudgetResponse(false, "取得明細資料失敗: " + e.getMessage());
        }
    }

    /**
     * 權責分攤_儲存分攤資料
     * @param model
     * @return
     */
    @Transactional
    @Override
    public BudgetResponse saveResponseibilityAllocation(BudgetResponseibilityAllocationDto.allocationData model) {
        try {
            NcccUserDto user = SecurityUtil.getCurrentUser();
            if (model.getDetails() == null || model.getDetails().isEmpty()) {
                return new BudgetResponse(false, "請至少選擇一筆分攤明細");
            }

            // 主檔
            NcccAllocationAdjM allocationAdjM = null;

            String processId = "";

            if (model.getAllocationNo() == null || model.getAllocationNo().isEmpty()) {
                // 代表新單
                String prefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));
                Integer nextSerial = ncccAllocationAdjMRepository.findMaxSerialByPrefix(prefix) + 1;
                String serialStr = String.format("%04d", nextSerial);
                String exNo = "AL" + prefix + serialStr;

                allocationAdjM = new NcccAllocationAdjM();
                allocationAdjM.setAllAdjNo(exNo);
                allocationAdjM.setCreateUser(user.getUserId());
                allocationAdjM.setCreateDate(LocalDateTime.now());

                // region 審核人員塞入

                String processKey = ProcessDefinitionKey.BUDGET_ALLOCATIONADJ.getKey();

                Map<String, Object> vars = new HashMap<>();

                vars.put(AssigneeRole.INITIATOR.getKey(), user.getHrid());

                List<String> approvers = new ArrayList<>();

                Optional<SyncUser> foundMgrUser = syncUserRepository.findManagerUserByOuCode(user.getOuCode());

                if(foundMgrUser.isPresent())
                {
                    SyncUser mgrUser = foundMgrUser.get();

                    approvers.add(mgrUser.getHrid());
                }

                vars.put(AssigneeRole.APPROVERS.getKey(), approvers);
                vars.put(AssigneeRole.CURRENT_INDEX.getKey(), 0);
                vars.put(AssigneeRole.CURRENT_APPROVER.getKey(), approvers.get(0));

                processId = flowableService.startProcess(processKey, exNo, vars);

                allocationAdjM.setTaskID(processId);

                // endregion
            } else {
                // 代表編輯單
                allocationAdjM = ncccAllocationAdjMRepository.findByAllocationAdjNo(model.getAllocationNo());
                if (allocationAdjM == null) {
                    return new BudgetResponse(false, "找不到分攤資料");
                }

                processId = allocationAdjM.getTaskID();
            }

            allocationAdjM.setApplyDate(model.getAllocationDate());
            allocationAdjM.setApplyUser(model.getEmployeeCode());
            allocationAdjM.setApplyOuCode(model.getDeptCode());
            allocationAdjM.setFlowStatus("1");

            // 清除舊的明細資料
            ncccAllocationAdjDRepository.deleteByAllAdjNo(allocationAdjM.getAllAdjNo());
            ncccAllocationAdjD1Repository.deleteByAllAdjNo(allocationAdjM.getAllAdjNo());
            ncccAllocationAdjD2Repository.deleteByAllAdjNo(allocationAdjM.getAllAdjNo());

            List<NcccAllocationAdjD> allocationAdjDList = new ArrayList<>();
            List<NcccAllocationAdjD1> allocationAdjD1List = new ArrayList<>();
            List<NcccAllocationAdjD2> allocationAdjD2List = new ArrayList<>();

            int dseqNo = 1;
            for(var detail : model.getDetails()) {
                NcccAllocationAdjD allocationAdjD = new NcccAllocationAdjD();
                allocationAdjD.setAllAdjNo(allocationAdjM.getAllAdjNo());
                allocationAdjD.setSeqNo(String.format("%03d", dseqNo));
                allocationAdjD.setPoNo(detail.getPurchaseOrderNo());
                allocationAdjD.setPoItemNo(detail.getPurchaseItemNo());
                allocationAdjD.setOuCode(detail.getBudgetDeptCode());
                allocationAdjD.setOuName(detail.getBudgetDeptName());
                allocationAdjD.setAccounting(detail.getBudgetAccountCode());
                allocationAdjD.setSubject(detail.getBudgetAccountName());
                allocationAdjD.setBpNo(detail.getSupplierNo());
                allocationAdjD.setBpName(detail.getSupplierName());
                allocationAdjD.setTotal(detail.getUntaxAmount());
                allocationAdjD.setTotalRemain(detail.getAllocatableAmount());
                allocationAdjD.setCalcelTotal(detail.getRemainingAmount());
                allocationAdjD.setItemText(detail.getItemText());
                allocationAdjD.setCreateDate(LocalDateTime.now());
                allocationAdjD.setCreateUser(user.getUserId());
                allocationAdjDList.add(allocationAdjD);

                // 儲存分攤作業項目明細檔
                int d1seqNo = 1; // 明細1序號從1開始
                for (BudgetResponseibilityAllocationDto.operationItems operationItem : detail.getOperationItems()) {
                    NcccAllocationAdjD1 allocationAdjD1 = new NcccAllocationAdjD1();
                    allocationAdjD1.setAllAdjNo(allocationAdjM.getAllAdjNo());
                    allocationAdjD1.setSeqNo(String.format("%03d", dseqNo));
                    allocationAdjD1.setSeqNo1(String.format("%03d", d1seqNo));
                    allocationAdjD1.setOperateItemCode(operationItem.getCode());
                    allocationAdjD1.setOperateItem(operationItem.getName());
                    allocationAdjD1.setOperateAmt(operationItem.getAmount());
                    allocationAdjD1.setOperateRatio(operationItem.getRatio());
                    allocationAdjD1.setCreateDate(LocalDateTime.now());
                    allocationAdjD1.setCreateUser(user.getUserId());
                    allocationAdjD1.setUpdateDate(LocalDateTime.now());
                    allocationAdjD1.setUpdateUser(user.getUserId());;
                    allocationAdjD1List.add(allocationAdjD1);
                    d1seqNo++;
                }

                // 儲存分攤項目明細檔
                for (BudgetResponseibilityAllocationDto.allocationSubData subItem : detail.getAllocationSubData()) {
                    NcccAllocationAdjD2 allocationAdjD2 = new NcccAllocationAdjD2();
                    allocationAdjD2.setAllAdjNo(allocationAdjM.getAllAdjNo());
                    allocationAdjD2.setSeqNo(String.format("%03d", dseqNo));
                    allocationAdjD2.setPlan(subItem.getPlanNo());
                    allocationAdjD2.setYear(subItem.getYear());
                    allocationAdjD2.setMonth(subItem.getMonth());
                    allocationAdjD2.setAdjustTotal(subItem.getAdjustAmount());
                    allocationAdjD2.setAllocationTotal(subItem.getAllocationAmount());
                    allocationAdjD2.setFinalTotal(subItem.getFinalAmount());
                    allocationAdjD2.setSapDocNo(subItem.getSapDocNo());
                    allocationAdjD2.setPostDate(subItem.getPostDate());
                    allocationAdjD2.setCreateDate(LocalDateTime.now());
                    allocationAdjD2.setCreateUser(user.getUserId());
                    allocationAdjD2.setUpdateDate(LocalDateTime.now());
                    allocationAdjD2.setUpdateUser(user.getUserId());
                    allocationAdjD2List.add(allocationAdjD2);

                    //若調整金額不等於0
                    if(subItem.getAdjustAmount().compareTo(BigDecimal.ZERO) != 0)
                    {
                        //查詢對應預算
                        NcccBudgetM budget = ncccBudgetMRepository.findByYearAndVersionAndOuCodeAndAccounting(
                                subItem.getYear(),"2", detail.getBudgetDeptCode(), detail.getBudgetAccountCode());

                        if(budget != null)
                        {
                            // 寫預算主檔<動用>
                            BudgetVo.BudgetTranscation transOut = new BudgetVo.BudgetTranscation();
                            transOut.setYear(subItem.getYear());
                            transOut.setVersion("2");
                            transOut.setOuCode(detail.getBudgetDeptCode());
                            transOut.setAccounting(detail.getBudgetAccountCode());
                            transOut.setTranscationSource("權責分攤");
                            transOut.setTranscationType("動用");
                            transOut.setTranscationDate(LocalDateTime.now().toLocalDate());
                            transOut.setTranscationNo(allocationAdjM.getAllAdjNo());
                            transOut.setAmount(subItem.getAdjustAmount());
                            transOut.setCreateDate(LocalDateTime.now());
                            transOut.setCreateUser(allocationAdjM.getCreateUser());
                            transOut.setBpName("");
                            transOut.setBpNo("");
                            transOut.setDocNo("");
                            WriteBudgetTranscation(transOut);
                        }
                    }
                }
            }
            ncccAllocationAdjMRepository.save(allocationAdjM);
            ncccAllocationAdjDRepository.saveAll(allocationAdjDList);
            ncccAllocationAdjD1Repository.saveAll(allocationAdjD1List);
            ncccAllocationAdjD2Repository.saveAll(allocationAdjD2List);

            DecisionVo decisionVo = new DecisionVo();
            decisionVo.setProcessId(processId);
            decisionVo.setDecision(Decision.SUBMIT);
            flowableService.completeTask(decisionVo);

            return new BudgetResponse(true, "ok");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
            LOG.error("儲存分攤資料失敗", e);
            return new BudgetResponse(false, "儲存分攤資料失敗: " + e.getMessage());
        }
    }

    /**
     * 權責分攤_查詢分攤資料
     * @param model
     * @return
     */
    @Override
    public BudgetResponse getResponsibilityAllocation(BudgetResponseibilityAllocationDto.QueryBudgetAllocation model) {
        try {
            BudgetResponseibilityAllocationDto.allocationData dto = new BudgetResponseibilityAllocationDto.allocationData();

            // 取得主檔資料
            NcccAllocationAdjM allocationAdjM = ncccAllocationAdjMRepository.findByAllocationAdjNo(model.getAllocationNo());
            if (allocationAdjM == null) {
                return new BudgetResponse(false, "找不到分攤資料");
            }
            dto.setTaskId(allocationAdjM.getTaskID());
            dto.setAllocationNo(allocationAdjM.getAllAdjNo());
            dto.setAllocationDate(allocationAdjM.getApplyDate());
            dto.setEmployeeCode(allocationAdjM.getApplyUser());
            SyncUser user = syncUserRepository.findByAccount(allocationAdjM.getApplyUser());
            if (user != null) {
                dto.setEmployeeName(user.getAccount());
            }
            dto.setDeptCode(allocationAdjM.getApplyOuCode());
            SyncOU ou = syncOURepository.findByOuCode(allocationAdjM.getApplyOuCode());
            if (ou != null) {
                dto.setDeptName(ou.getOuName());
            }
            dto.setPurchaseOrderNo("");

            // 取得明細資料
            List<NcccAllocationAdjD> details = ncccAllocationAdjDRepository.getByAllAdjNo(model.getAllocationNo());
            List<BudgetResponseibilityAllocationDto.DetailData> detailList = new ArrayList<>();
            for (NcccAllocationAdjD detail : details) {

                BudgetResponseibilityAllocationDto.DetailData detailData = new BudgetResponseibilityAllocationDto.DetailData();
                detailData.setPurchaseOrderNo(detail.getPoNo());
                detailData.setPurchaseItemNo(detail.getPoItemNo());
                detailData.setSupplierNo(detail.getBpNo());
                detailData.setSupplierName(detail.getBpName());
                detailData.setBudgetDeptCode(detail.getOuCode());
                detailData.setBudgetDeptName(detail.getOuName());
                detailData.setBudgetAccountCode(detail.getAccounting());
                detailData.setBudgetAccountName(detail.getSubject());
                detailData.setItemText(detail.getItemText());
                detailData.setUntaxAmount(detail.getTotal());
                detailData.setRemainingAmount(detail.getCalcelTotal());
                detailData.setAllocatableAmount(detail.getTotalRemain());
                detailData.setAllocationSubData(new ArrayList<>());
                detailData.setOperationItems(new ArrayList<>());

                List<NcccAllocationAdjD1> operationDatas = ncccAllocationAdjD1Repository.getByAllAdjNoAndSeqNo(detail.getAllAdjNo(), detail.getSeqNo());
                for (NcccAllocationAdjD1 operationItem : operationDatas) {
                    BudgetResponseibilityAllocationDto.operationItems operationData = new BudgetResponseibilityAllocationDto.operationItems();
                    operationData.setCode(operationItem.getOperateItemCode());
                    operationData.setName(operationItem.getOperateItem());
                    operationData.setAmount(operationItem.getOperateAmt());
                    operationData.setRatio(operationItem.getOperateRatio());
                    detailData.getOperationItems().add(operationData);
                }
                List<NcccAllocationAdjD2> subItems = ncccAllocationAdjD2Repository.getByAllAdjNoAndSeqNo(
                        detail.getAllAdjNo(), detail.getSeqNo());
                for (NcccAllocationAdjD2 subItem : subItems) {
                    BudgetResponseibilityAllocationDto.allocationSubData data = new BudgetResponseibilityAllocationDto.allocationSubData();
                    data.setPlanNo(subItem.getPlan());
                    data.setYear(subItem.getYear());
                    data.setMonth(subItem.getMonth());
                    data.setAdjustAmount(subItem.getAdjustTotal());
                    data.setAllocationAmount(subItem.getAllocationTotal());
                    data.setFinalAmount(subItem.getFinalTotal());
                    data.setSapDocNo(subItem.getSapDocNo());
                    data.setPostDate(subItem.getPostDate());
                    detailData.getAllocationSubData().add(data);
                }

                detailList.add(detailData);
            }
            dto.setDetails(detailList);

            dto.setTaskHistoryList(flowableService.getTaskHistory(allocationAdjM.getTaskID()));
            if (flowableService.checkAtInitiatorTask(allocationAdjM.getTaskID())) {
                dto.setMode(Mode.EDITMODE);
            }
            else
            {
                dto.setMode(Mode.VIEWMODE);

                dto.setCanBackToPrevious(true);
            }
            return new BudgetResponse(true, "ok", dto);
        }
        catch (Exception e) {
            LOG.error("查詢分攤資料失敗", e);
            return new BudgetResponse(false, "查詢分攤資料失敗: " + e.getMessage());
        }
    }

    /**
     * 權責分攤_動作判斷
     * @param vo
     * @return
     */

    public BudgetResponse decisionResponsibilityAllocation(DecisionVo vo)
    {
        try{
            boolean flag = flowableService.completeTask(vo);

            if (flag)
            {
                switch (vo.getDecision())
                {
                    case APPROVE:

                        if(flowableService.isFinish(vo.getProcessId()))
                        {
                            return approveResponseibilityAllocation(vo.getProcessId());
                        }
                        else
                        {
                            return new BudgetResponse(true, "ok");
                        }

                    case INVALID:

                        return voidResponseibilityAllocation(vo.getProcessId());

                    case BACK:
                    case RETURN:

                        if(flowableService.checkAtInitiatorTask(vo.getProcessId()))
                        {
                            return returnResponseibilityAllocation(vo.getProcessId());
                        }
                        else
                        {
                            return new BudgetResponse(true, "ok");
                        }

                    default:

                        return new BudgetResponse(true, "ok");
                }
            }
            else
            {
                return new BudgetResponse(false, "簽核權責分攤失敗");
            }
        }
        catch (Exception e)
        {
            LOG.error("簽核權責分攤失敗", e);
            return new BudgetResponse(false, "簽核權責分攤失敗: " + e.getMessage());
        }
    }

    /**
     * 權責分攤_核可分攤資料
     * @param taskId
     * @return
     */
    public BudgetResponse approveResponseibilityAllocation(String taskId) {
        try {
            // 取得主檔資料
            NcccAllocationAdjM allocationAdjM = ncccAllocationAdjMRepository.findByTaskId(taskId);
            if (allocationAdjM == null) {
                return new BudgetResponse(false, "找不到分攤資料");
            }
            allocationAdjM.setFlowStatus("2");
            ncccAllocationAdjMRepository.save(allocationAdjM);
            //調整單明細
            List<NcccAllocationAdjD> allocationAdjDList = ncccAllocationAdjDRepository.getByAllAdjNo(allocationAdjM.getAllAdjNo());

            for(NcccAllocationAdjD allocationAdjD :allocationAdjDList)
            {
                // region 期數

                //刪除舊有明細
                ncccAllocationDRepository.deleteByPoNoAndPoItemNo(allocationAdjD.getPoNo(),allocationAdjD.getPoItemNo());

                List<NcccAllocationD> allocationDList = new ArrayList<>();

                List<NcccAllocationAdjD2> allocationAdjD2List = ncccAllocationAdjD2Repository.getByAllAdjNoAndSeqNo(allocationAdjD.getAllAdjNo(),allocationAdjD.getSeqNo());

                for(NcccAllocationAdjD2 allocationAdjD2 : allocationAdjD2List)
                {
                    NcccAllocationD allocationD = new NcccAllocationD();

                    allocationD.setYear(allocationAdjD2.getYear());
                    allocationD.setPoNo(allocationAdjD.getPoNo());
                    allocationD.setPoItemNo(allocationAdjD.getPoItemNo());
                    allocationD.setPlan(allocationAdjD2.getPlan());
                    allocationD.setMonth(allocationAdjD2.getMonth());
                    allocationD.setAllocationTotal(allocationAdjD2.getFinalTotal());
                    allocationD.setAdjustTotal(new BigDecimal(0));
                    allocationD.setFinalTotal(allocationAdjD2.getFinalTotal());
                    allocationD.setSapDocNo(allocationAdjD2.getSapDocNo());
                    allocationD.setPostDate(allocationAdjD2.getPostDate());
                    allocationD.setCreateUser(allocationAdjD2.getCreateUser());
                    allocationD.setCreateDate(LocalDate.now());
                    allocationDList.add(allocationD);
                }

                ncccAllocationDRepository.saveAll(allocationDList);

                // endregion

                // region 作業項目

                ncccAllocationD1Repository.deleteByPoNoAndPoItemNo(allocationAdjD.getPoNo(),allocationAdjD.getPoItemNo());

                List<NcccAllocationD1> allocationD1List = new ArrayList<>();

                List<NcccAllocationAdjD1> allocationAdjD1List = ncccAllocationAdjD1Repository.getByAllAdjNoAndSeqNo(allocationAdjD.getAllAdjNo(),allocationAdjD.getSeqNo());

                for(NcccAllocationAdjD1 allocationAdjD1 : allocationAdjD1List)
                {
                    NcccAllocationD1 allocationD1 = new NcccAllocationD1();
                    allocationD1.setPoNo(allocationAdjD.getPoNo());
                    allocationD1.setPoItemNo(allocationAdjD.getPoItemNo());
                    allocationD1.setSeqNo1(allocationAdjD1.getSeqNo1());
                    allocationD1.setOperateItem(allocationAdjD1.getOperateItem());
                    allocationD1.setOperateItemCode(allocationAdjD1.getOperateItemCode());
                    allocationD1.setOperateAmt(allocationAdjD1.getOperateAmt());
                    allocationD1.setOperateRatio(allocationAdjD1.getOperateRatio());
                    allocationD1.setCreateUser(allocationAdjD1.getCreateUser());
                    allocationD1.setCreateDate(LocalDate.now());
                    allocationD1List.add(allocationD1);
                }

                ncccAllocationD1Repository.saveAll(allocationD1List);

                // endregion

            }

            return new BudgetResponse(true, "ok");
        }
        catch (Exception e) {
            LOG.error("核可分攤資料失敗", e);
            return new BudgetResponse(false, "核可分攤資料失敗: " + e.getMessage());
        }
    }

    /**
     * 權責分攤_作廢分攤資料
     * @param taskId
     */
    public BudgetResponse voidResponseibilityAllocation(String taskId) {
        try {
            // 取得主檔資料
            NcccAllocationAdjM allocationAdjM = ncccAllocationAdjMRepository.findByTaskId(taskId);
            if (allocationAdjM == null) {
                return new BudgetResponse(false, "找不到分攤資料");
            }
            allocationAdjM.setFlowStatus("3");
            ncccAllocationAdjMRepository.save(allocationAdjM);

            return new BudgetResponse(true, "ok");
        }
        catch (Exception e) {
            LOG.error("作廢分攤資料失敗", e);
            return new BudgetResponse(false, "作廢分攤資料失敗: " + e.getMessage());
        }
    }

    /**
     * 權責分攤_退回分攤資料
     */
    @Transactional
    public BudgetResponse returnResponseibilityAllocation(String taskId) {
        try {
            // 取得主檔資料
            NcccAllocationAdjM allocationAdjM = ncccAllocationAdjMRepository.findByTaskId(taskId);
            if (allocationAdjM == null) {
                return new BudgetResponse(false, "找不到分攤資料");
            }

            // 退回給申請人時
            if(true){
                // 取得明細資料
                List<NcccAllocationAdjD> details = ncccAllocationAdjDRepository.getByAllAdjNo(allocationAdjM.getAllAdjNo());
                for (NcccAllocationAdjD detail : details) {

                    // 取得權責分攤資料
                    List<NcccAllocationAdjD2> subItems = ncccAllocationAdjD2Repository.getByAllAdjNoAndSeqNo(
                            detail.getAllAdjNo(), detail.getSeqNo());
                    if (subItems.size() == 0) {
                        throw new IllegalArgumentException("找不到權責分攤資料");
                    }

                    for(var subItem : subItems){

                        //若調整金額不等於0
                        if(subItem.getAdjustTotal().compareTo(BigDecimal.ZERO) != 0)
                        {
                            //查詢對應預算
                            NcccBudgetM budget = ncccBudgetMRepository.findByYearAndVersionAndOuCodeAndAccounting(
                                    subItem.getYear(),"2", detail.getOuCode(), detail.getAccounting());

                            if(budget != null)
                            {
                                // 寫預算主檔<動用>
                                BudgetVo.BudgetTranscation transOut = new BudgetVo.BudgetTranscation();
                                transOut.setYear(subItem.getYear());
                                transOut.setVersion("2");
                                transOut.setOuCode(detail.getOuCode());
                                transOut.setAccounting(detail.getAccounting());
                                transOut.setTranscationSource("權責分攤");
                                transOut.setTranscationType("動用");
                                transOut.setTranscationDate(LocalDateTime.now().toLocalDate());
                                transOut.setTranscationNo(allocationAdjM.getAllAdjNo());
                                transOut.setAmount(subItem.getAdjustTotal().multiply(BigDecimal.valueOf(-1)));
                                transOut.setCreateDate(LocalDateTime.now());
                                transOut.setCreateUser(allocationAdjM.getCreateUser());
                                transOut.setBpName("");
                                transOut.setBpNo("");
                                transOut.setDocNo("");
                                WriteBudgetTranscation(transOut);
                            }
                        }
                    }
                }
            }

            return new BudgetResponse(true, "ok");
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
            LOG.error("退回分攤資料失敗", e);
            return new BudgetResponse(false, "退回分攤資料失敗: " + e.getMessage());
        }
    }

    /**
     * 權責分攤_查詢SAP拋轉資料
     */
    @Override
    public BudgetResponse queryResponsibilityAllocationSAPData(QueryBudgetAllocationSAPData model) {
        try {
            List<BudgetResponseibilityAllocationDto.SAPallocationSubData> dto = new ArrayList<>();

            // 查詢SAP拋轉資料
            List<NcccAllocationSAPData> sapDatas = ncccAllocationDRepository.getSAPData(model.getYear(),model.getMonth(), model.getPostDate());

            for (NcccAllocationSAPData item : sapDatas) {
                BudgetResponseibilityAllocationDto.SAPallocationSubData sapData = new BudgetResponseibilityAllocationDto.SAPallocationSubData();
                sapData.setPlanNo(item.getPLAN());
                sapData.setPoItemNo(item.getPO_ITEM_NO());
                sapData.setPurchaseOrderNo(item.getPO_NO());
                sapData.setBudgetDeptCode(item.getOUCODE());
                sapData.setBudgetDeptName(item.getOUNAME());
                sapData.setBudgetAccountCode(item.getACCOUNTING());
                sapData.setBudgetAccountName(item.getSUBJECT());
                sapData.setItemText(item.getITEM_TEXT());
                sapData.setYear(item.getYEAR());
                sapData.setMonth(item.getMONTH());
                sapData.setFinalAmount(item.getFINAL_TOTAL());
                dto.add(sapData);
            }

            return new BudgetResponse(true, "ok", dto);
        }
        catch (Exception e) {
            LOG.error("查詢SAP拋轉資料失敗", e);
            return new BudgetResponse(false, "查詢SAP拋轉資料失敗: " + e.getMessage());
        }
    }


    /**
     * 權責分攤SAP拋轉
     */

    @Transactional
    @Override
    public BudgetResponse transferAllocationToSAP(transferAllocationToSAPReq model) {
        try {
            // 取得主檔資料
            for (var item : model.getItems()) {
                NcccAllocationD allocationD = ncccAllocationDRepository.findByPoNoAndPoItemNoAndPlan(item.getPoNo(), item.getPoItemNo(),item.getPlanNo());
                if (allocationD == null) {
                    throw new IllegalArgumentException("找不到分攤資料");
                }

                NcccAllocationM allocationM = ncccAllocationMRepository.findByPoNoAndPoItemNo(item.getPoNo(), item.getPoItemNo());

                if(allocationM == null)
                {
                    throw new IllegalArgumentException("找不到分攤主檔資料");
                }

                // 檢查是否已經拋轉過
                if (allocationD.getSapDocNo() != null && !allocationD.getSapDocNo().isEmpty()) {
                    return new BudgetResponse(false, "該分攤資料已經拋轉過，請勿重複操作");
                }


                // region 拋轉sap

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

                ZHeader header = new ZHeader();
                // 來源系統
                header.setZSYSTEM("BTP");
                // 公司代碼
                header.setBUKRS("1010");
                // 文件日期
                header.setBLDAT(allocationD.getPostDate().format(formatter));
                // 過帳日期
                header.setBUDAT(allocationD.getPostDate().format(formatter));
                // 文件類型
                header.setBLART("KT");
                // 幣別
                header.setWAERS("TWD");
                // 參考
                header.setXBLNR("");
                // 文件表頭內文
                header.setBKTXT("");
                // 文件表頭的參考碼
                header.setXREF1_HD(allocationD.getPoNo());

                ArrayList<ZItem> zitemList = new ArrayList<ZItem>();

                // 品號品名
                ZItem item1 = new ZItem();
                // 過帳碼
                item1.setBSCHL("40");
                // 科目
                item1.setNEWKO(allocationM.getAccounting());
                // 文件幣別金額
                item1.setWRBTR(allocationD.getFinalTotal().toString());
                // 稅碼
                item1.setMWSKZ("");
                // 稅基
                item1.setTXBFW("");
                // 到期日
                item1.setZFBDT("");

                String CostCenter = "";

                NcccCostcenterOrgMapping ncccCostcenterOrgMapping = ncccCostcenterOrgMappingRepository.findByDepNameAct(allocationM.getOuCode());

                if(ncccCostcenterOrgMapping != null)
                {
                    CostCenter = ncccCostcenterOrgMapping.getCostcenter();
                }

                // 成本中心
                item1.setKOSTL(CostCenter);
                // 內文
                item1.setSGTXT("");

                zitemList.add(item1);

                ZItem item2 = new ZItem();
                // 過帳碼
                item2.setBSCHL("50");
                // 科目
                item2.setNEWKO("11110398");
                // 文件幣別金額
                item2.setWRBTR(allocationD.getFinalTotal().toString());
                // 稅碼
                item2.setMWSKZ("");
                // 稅基
                item2.setTXBFW("");
                // 到期日
                item2.setZFBDT("");
                // 成本中心
                item2.setKOSTL("");
                // 內文
                item2.setSGTXT("");

                zitemList.add(item2);

                ZReturn zreturn = sapUtil.callZcreateAccDocument(header, zitemList);

                if (zreturn.getTYPE().equals("S"))
                {
                    allocationD.setSapDocNo(zreturn.getBELNR());

                    ncccAllocationDRepository.save(allocationD);
                }

                // endregion

            }
            return new BudgetResponse(true, "ok");
        }
        catch (Exception e) {
            LOG.error("權責分攤SAP拋轉失敗", e);
            return new BudgetResponse(false, "權責分攤SAP拋轉失敗: " + e.getMessage());
        }
    }


    // endregion 權責分攤

    private Map<String, Object> setAssignee(Map<String, Object> vars, AssigneeRole assigneeRole) {
        vars.put(assigneeRole.getKey(),
                syncOURepository.findByOuCodeWithManager(assigneeRole.getCode()).getHrid());
        return vars;
    }

    // region word處理

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
}
