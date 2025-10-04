package nccc.btp.service;

import nccc.btp.vo.AssignTasksVo;
import nccc.btp.vo.BudgetVo;
import nccc.btp.vo.DecisionVo;
import org.springframework.web.multipart.MultipartFile;

import nccc.btp.dto.BudgetAllocationDto;
import nccc.btp.dto.BudgetApportionmentRuleDTO;
import nccc.btp.dto.BudgetExpenseDto;
import nccc.btp.dto.BudgetGetExistingRuleInfo;
import nccc.btp.dto.BudgetGetRuleListDto;
import nccc.btp.dto.BudgetRequestDto;
import nccc.btp.dto.BudgetRequestDto.DecisionInfo;
import nccc.btp.dto.BudgetRequestDto.DownloadBudgetRuleFile;
import nccc.btp.dto.BudgetRequestDto.QueryBudgetExpense;
import nccc.btp.dto.BudgetRequestDto.QueryBudgetPurchaseOrder;
import nccc.btp.dto.BudgetRequestDto.QueryBudgetRequestOrder;
import nccc.btp.dto.BudgetRequestDto.QueryBudgetReserve;
import nccc.btp.dto.BudgetRequestDto.QueryBudgetTransaction;
import nccc.btp.dto.BudgetRequestDto.YearVersion;
import nccc.btp.dto.BudgetReserveDto;
import nccc.btp.dto.BudgetResponse;
import nccc.btp.dto.BudgetResponseibilityAllocationDto.QueryBudgetAllocation;
import nccc.btp.dto.BudgetResponseibilityAllocationDto.QueryBudgetAllocationSAPData;
import nccc.btp.dto.BudgetResponseibilityAllocationDto.ReqDetailByPoNo;
import nccc.btp.dto.BudgetResponseibilityAllocationDto.allocationData;
import nccc.btp.dto.BudgetResponseibilityAllocationDto.transferAllocationToSAPReq;
import nccc.btp.dto.BudgetRuleDTO;

import java.util.List;

public interface NcccBudgetService {

    /**
     * 取得預算會計科目
     * @return
     */
    BudgetResponse getBudgetAccount();

    /**
     * 取得作業項目
     * @return
     */
    BudgetResponse getOperateItems();

    /**
     * 取得預算來源部門
     * @return
     */
    BudgetResponse getSourceDepartment();

    /**
     * 取得部門與經辦
     * @return
     */
    BudgetResponse getOuManager();

    /**
     * 查詢預算版次
     * @param year
     * @return
     */
    BudgetResponse getBudgetVersion(String year);

    /**
     * 查詢預算當前版次
     * @param year
     * @return
     */
    BudgetResponse getBudgetCurrentVersion(String year);

    /**
     * 建立預算版次
     * @param model
     * @return
     */
    BudgetResponse createBudgetYear(BudgetRequestDto.YearVersion model);

    /**
     * 複製預算版次
     * @param model
     * @return
     */
    BudgetResponse copyBudgetYear(BudgetRequestDto.CopyYear model);

    /**
     * 取得明細資料
     * @param model
     * @return
     */
    BudgetResponse getExistingRuleData(BudgetGetExistingRuleInfo model);

    /**
     * 查詢請購單
     * @param model
     * @return
     */
    BudgetResponse getRequestOrder(QueryBudgetRequestOrder model);

    /**
     * 取得採購單
     * @param model
     * @return
     */
    BudgetResponse getPurchaseOrder(QueryBudgetPurchaseOrder model);

    /**
     * 取得最近一筆明細資料
     * @param model
     * @return
     */
    BudgetResponse getLastExistingRuleData(BudgetGetExistingRuleInfo model);

    /**
     * 儲存部門分攤規則資料
     * @param model
     * @return
     */
    BudgetResponse saveApportionmentRule(BudgetApportionmentRuleDTO model);

    /**
     * 取得預算編列規則列表
     * @param model
     * @return
     */
    BudgetResponse getBudgetRuleList(BudgetGetRuleListDto.BudgetGetRuleSearch model);

    /**
     * 上傳預算編列規則檔案
     * @param file
     * @param year
     * @return
     */
    BudgetResponse uploadBudgetRuleFile(MultipartFile file, String year,String department);

    /**
     * 下載預算規則檔案
     * @param model
     * @return
     */
    BudgetResponse downloadBudgetRuleFile(DownloadBudgetRuleFile model);

    /**
     * 上傳折舊預算檔案
     * @param file
     * @param year
     * @return
     */
    BudgetResponse uploadBudgetRuleDepreciationFile(MultipartFile file, String year, String version);

    /**
     * 合併SAP折舊預算檔案
     * @param file
     * @param year
     * @return
     */
    BudgetResponse uploadBudgetRuleSAPDepreciationFile(MultipartFile file, String year);

    /**
     * 儲存預算編列規則資料
     * @param model
     * @return
     */
    BudgetResponse saveBudgetRule(BudgetRuleDTO model);

    /**
     * 取得預算編列規則
     * @param model
     * @return
     */
    BudgetResponse getBudgetRule(BudgetRequestDto.GetRuleDto model);

    /**
     * 刪除預算編列規則
     * @param model
     * @return
     */
    BudgetResponse deleteBudgetRule(BudgetRequestDto.GetRuleDto model);

    /**
     * 預算開帳
     * @param model
     * @return
     */
    BudgetResponse openBudget(BudgetRequestDto.YearVersion model);

    /**
     * 查詢是否已經開帳
     * @return
     */
    BudgetResponse checkBudgetOpen(String year);

    /**
     * 預算關帳
     * @param model
     * @return
     */
    BudgetResponse closeBudget(BudgetRequestDto.YearVersion model);

    /*
     * 預算編列確認
     */
    BudgetResponse Confirm(List<String> batchList);

    /*
     * 預算編列確認退回
     */
    BudgetResponse ConfirmReturn(List<String> batchList);

    /*
     * 預算編列覆核
     */
    BudgetResponse Approve(List<String> batchList);

    /*
     * 預算編列覆核退回
     */
    BudgetResponse ApproveReturn(List<String> batchList);

    /**
     * 取得預算主檔的預算項目
     */
    BudgetResponse getBudgetItem(BudgetRequestDto.GetBudgetItemByOuCode model);

    /**
     * 取得預算編列的預算項目
     * @param model
     * @return
     */
    BudgetResponse getPreBudgetItem(YearVersion model);

    /**
     * 取得部門剩餘預算
     * @param model
     * @return
     */
    BudgetResponse getBudgetRemainByOuCode(BudgetRequestDto.GetBudgetRemainByOuCode model);

    /**
     * 儲存預算調撥
     * @param model
     * @return
     */
    BudgetResponse saveBudgetAllocation(BudgetAllocationDto model);

    /**
     * 取得預算調撥
     * @param model
     * @return
     */
    BudgetResponse getBudgetAllocation(BudgetRequestDto.GetBudgetAllocation model);

    /**
     * 預算調撥動作判斷
     * @param vo
     * @return
     */
    BudgetResponse decisionBudgetAllocation(DecisionVo vo);

    /**
     * 查詢預算
     * @param model
     * @return
     */
    BudgetResponse queryBudget(BudgetRequestDto.QueryBudget model);

    /**
     * 查詢預算交易明細
     * @param model
     * @return
     */
    BudgetResponse queryBudgetTransaction(QueryBudgetTransaction model);

    /**
     * 儲存預算保留
     * @param model
     * @return
     */
    BudgetResponse saveBudgetReserve(BudgetReserveDto model);

    /**
     * 查詢預算保留
     * @param model
     * @return
     */
    BudgetResponse queryBudgetReserve(QueryBudgetReserve model);

    /**
     * 預算保留動作判斷
     * @param vo
     * @return
     */
    BudgetResponse decisionBudgetReserve(DecisionVo vo);

    /**
     * 列印預算保留
     * @param model
     * @return
     */
    BudgetResponse printBudgetReserve(BudgetRequestDto.QueryBudgetReserve model);

    /**
     * 預算保留指派成本經辦
     *
     * @param assignTasksVo model
     */
    String setBudgetReserveNextAssignee(AssignTasksVo assignTasksVo);

    /**
     * 提列費用儲存
     * @param model
     * @return
     */
    BudgetResponse saveBudgetExpense(BudgetExpenseDto model);

    /**
     * 查詢提列費用
     * @param model
     * @return
     */
    BudgetResponse queryBudgetExpense(QueryBudgetExpense model);

    /**
     * 提列費用動作判斷
     * @param vo
     * @return
     */
    BudgetResponse decisionBudgetExpense(DecisionVo vo);

    /**
     * TODO 指派成本經辦
     *
     * @param assignTasksVo
     * @return
     */
    String setBudgetExpenseNextAssignee(AssignTasksVo assignTasksVo);

    /**
     * 權責分攤_取得採購單資料
     * @return
     */
    BudgetResponse getAllocationPOData();

    /**
     * 權責分攤_取得詳細資料
     * @param model
     * @return
     */
    BudgetResponse getAllocationDetails(ReqDetailByPoNo model);

    /**
     * 權責分攤儲存
     * @param model
     * @return
     */
    BudgetResponse saveResponseibilityAllocation(allocationData model);

    /**
     * 權責分攤取得
     * @param model
     * @return
     */
    BudgetResponse getResponsibilityAllocation(QueryBudgetAllocation model);

    /**
     * 權責分攤_動作判斷
     * @param vo
     * @return
     */
    BudgetResponse decisionResponsibilityAllocation(DecisionVo vo);

    /**
     * 權責分攤查詢SAP拋轉資料
     * @param model
     * @return
     */
    BudgetResponse queryResponsibilityAllocationSAPData(QueryBudgetAllocationSAPData model);

    /**
     * 權責分攤SAP拋轉
     * @param model
     * @return
     */
    BudgetResponse transferAllocationToSAP(transferAllocationToSAPReq model);

    /**
     * 查詢預算調撥採購單
     * @param model
     * @return
     */
    BudgetResponse getBudgetManagementPurchaseOrder(QueryBudgetPurchaseOrder model);

    /**
     * 取得預算部門底下經辦
     * @return
     */
    BudgetResponse getBudgetOuUnderAccount();

    /**
     * 寫預算交易明細紀錄
     *
     * @param transcation 模型
     */
    void WriteBudgetTranscation(BudgetVo.BudgetTranscation transcation);

    /**
     * 查詢預算餘額
     * @param model 查詢模型
     */
    BudgetResponse getBudgetBalance(BudgetVo.SearchBudgetBalanceData model);

    /**
     * 取得對應預算部門
     * @param ouCode
     * @return
     */
    BudgetResponse getBudgetOuCodeByOuCode(String ouCode);

    /**
     * 取得部門
     *
     * @return
     */
    BudgetResponse getAllSourceDepartment();

    /**
     * 查詢預算餘額
     * @param model 查詢模型
     * @return
     */
    BudgetResponse getBudgetBalanceList(BudgetVo.SearchBudgetBalanceListData model);

}
