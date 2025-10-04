package nccc.btp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import nccc.btp.vo.AssignTasksVo;
import nccc.btp.vo.BudgetVo;
import nccc.btp.vo.DecisionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.var;
import nccc.btp.dto.BudgetAllocationDto;
import nccc.btp.dto.BudgetApportionmentRuleDTO;
import nccc.btp.dto.BudgetExpenseDto;
import nccc.btp.dto.BudgetGetRuleListDto;
import nccc.btp.dto.BudgetRequestDto;
import nccc.btp.dto.BudgetReserveDto;
import nccc.btp.dto.BudgetGetExistingRuleInfo;
import nccc.btp.dto.BudgetResponse;
import nccc.btp.dto.BudgetResponseibilityAllocationDto;
import nccc.btp.dto.BudgetRuleDTO;
import nccc.btp.service.NcccBudgetService;

/**
 * 預算Controller
 */
@RestController
@RequestMapping("/budget")
public class BudgetController {
	protected static Logger LOG = LoggerFactory.getLogger(BudgetController.class);

	@Autowired
	private NcccBudgetService ncccBudgetService;

	/**
	 * 取得預算來源部門
	 */
	@GetMapping("/getSourceDepartment")
	public ResponseEntity<BudgetResponse> getSourceDepartment() {
		return ResponseEntity.ok(ncccBudgetService.getSourceDepartment());
	}

	/**
	 * 取得全部部門
	 */
	@GetMapping("/getAllSourceDepartment")
	public ResponseEntity<BudgetResponse> getAllSourceDepartment() {
		return ResponseEntity.ok(ncccBudgetService.getAllSourceDepartment());
	}

	/**
	 * 取得預算會計科目
	 *
	 * @return
	 */
	@GetMapping("/getBudgetAccount")
	public ResponseEntity<BudgetResponse> getBudgetAccount() {
		return ResponseEntity.ok(ncccBudgetService.getBudgetAccount());
	}

	/**
	 * 取得作業項目
	 *
	 * @return
	 */
	@GetMapping("/getOperateItems")
	public ResponseEntity<BudgetResponse> getOperateItems() {
		return ResponseEntity.ok(ncccBudgetService.getOperateItems());
	}

	/**
	 * 取得部門與經辦
	 */
	@GetMapping("/getOuManager")
	public ResponseEntity<BudgetResponse> getOuManager() {
		return ResponseEntity.ok(ncccBudgetService.getOuManager());
	}

	/**
	 * 取得預算部門底下經辦
	 * @return
	 */
	@GetMapping("/getBudgetOuUnderAccount")
	public ResponseEntity<BudgetResponse> getBudgetOuUnderAccount() {
		return ResponseEntity.ok(ncccBudgetService.getBudgetOuUnderAccount());
	}

	/**
	 * 取得請購單資料
	 * @param model
	 * @return
	 */
	@PostMapping("/getRequestOrder")
	public ResponseEntity<BudgetResponse> getRequestOrder(@RequestBody BudgetRequestDto.QueryBudgetRequestOrder model) {
		return ResponseEntity.ok(ncccBudgetService.getRequestOrder(model));
	}


	/**
	 * 取得採購單資料
	 * @return
	 */
	@PostMapping("/getPurchaseOrder")
	public ResponseEntity<BudgetResponse> getPurchaseOrder(@RequestBody BudgetRequestDto.QueryBudgetPurchaseOrder model) {
		return ResponseEntity.ok(ncccBudgetService.getPurchaseOrder(model));
	}

	/**
	 * 取得預算調撥採購單資料
	 * @return
	 */
	@PostMapping("/getBudgetManagementPurchaseOrder")
	public ResponseEntity<BudgetResponse> getBudgetManagementPurchaseOrder(@RequestBody BudgetRequestDto.QueryBudgetPurchaseOrder model) {
		return ResponseEntity.ok(ncccBudgetService.getBudgetManagementPurchaseOrder(model));
	}

	/**
	 * 查詢預算版次
	 *
	 * @return
	 **/
	@GetMapping("/getBudgetVersion")
	public ResponseEntity<BudgetResponse> getBudgetVersion(String year) {
		return ResponseEntity.ok(ncccBudgetService.getBudgetVersion(year));
	}

	/**
	 * 查詢當前預算版次
	 *
	 * @return
	 **/
	@GetMapping("/getBudgetCurrentVersion")
	public ResponseEntity<BudgetResponse> getBudgetCurrentVersion(String year) {
		return ResponseEntity.ok(ncccBudgetService.getBudgetCurrentVersion(year));
	}


	/**
	 * 建立預算版次
	 *
	 * @param model
	 * @return
	 **/
	@PostMapping("/createBudgetYear")
	public ResponseEntity<BudgetResponse> createBudgetYear(@RequestBody BudgetRequestDto.YearVersion model) {
		return ResponseEntity.ok(ncccBudgetService.createBudgetYear(model));
	}

	/**
	 * 複製預算版次
	 *
	 * @param model
	 * @return
	 **/
	@PostMapping("/copyBudgetYear")
	public ResponseEntity<BudgetResponse> copyBudgetYear(@RequestBody BudgetRequestDto.CopyYear model) {
		return ResponseEntity.ok(ncccBudgetService.copyBudgetYear(model));
	}

	/**
	 * 取得明細資料
	 */
	@PostMapping("/getExistingRuleData")
	public ResponseEntity<BudgetResponse> getExistingRuleData(@RequestBody BudgetGetExistingRuleInfo model) {
		return ResponseEntity.ok(ncccBudgetService.getExistingRuleData(model));
	}

	/**
	 * 取得最近一筆明細資料
	 */
	@PostMapping("/getLastExistingRuleData")
	public ResponseEntity<BudgetResponse> getLastExistingRuleData(@RequestBody BudgetGetExistingRuleInfo model) {
		return ResponseEntity.ok(ncccBudgetService.getLastExistingRuleData(model));
	}

	/**
	 * 儲存部門分攤規則資料
	 */
	@PostMapping("/saveApportionmentRule")
	public ResponseEntity<BudgetResponse> saveApportionmentRule(@RequestBody BudgetApportionmentRuleDTO model) {
		return ResponseEntity.ok(ncccBudgetService.saveApportionmentRule(model));
	}

	/**
	 * 取得預算編列列表
	 *
	 * @param model
	 * @return
	 */
	@PostMapping("/getBudgetRuleList")
	public ResponseEntity<BudgetResponse> getBudgetRuleList(
			@RequestBody BudgetGetRuleListDto.BudgetGetRuleSearch model) {
		return ResponseEntity.ok(ncccBudgetService.getBudgetRuleList(model));
	}

	/**
	 * 取得預算編列規則資料
	 */
	@PostMapping("/getBudgetRule")
	public ResponseEntity<BudgetResponse> getBudgetRule(@RequestBody BudgetRequestDto.GetRuleDto model) {
		return ResponseEntity.ok(ncccBudgetService.getBudgetRule(model));
	}



	/**
	 * 上傳預算編列檔案
	 * @param model
	 * @return
	 */
	@PostMapping("/uploadBudgetRuleFile")
	public ResponseEntity<BudgetResponse> uploadBudgetRuleFile(@RequestPart(value = "file") MultipartFile file,@RequestPart(value = "year") String year,@RequestPart(value = "department") String department) {
		return ResponseEntity.ok(ncccBudgetService.uploadBudgetRuleFile(file,year,department));
	}

	/**
	 * 下載預算編列檔案
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@PostMapping("/downloadBudgetRuleFile")
	public ResponseEntity<Object> downloadBudgetRuleFile(@RequestBody BudgetRequestDto.DownloadBudgetRuleFile model) throws UnsupportedEncodingException {
		var resp = ncccBudgetService.downloadBudgetRuleFile(model);
		if (resp.getSuccess()) {
			// 取出串流
			String fileName = "預算編列檔案_" + model.getYear() + "_" + model.getDepartment() + "_" + model.getVersion() + ".xlsx";

			// 下載
			// 設定響應標頭
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+URLEncoder.encode(fileName, "utf-8")+"\"");
			headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
			return ResponseEntity.ok()
					.headers(headers)
					.body((byte[]) resp.getData());
		}

		// 若下載不成功，返回相應的響應
		return ResponseEntity.ok(resp);
	}

	/**
	 * 上傳折舊預算檔案
	 * @param model
	 * @return
	 */
	@PostMapping("/uploadBudgetRuleDepreciationFile")
	public ResponseEntity<BudgetResponse> uploadBudgetRuleDepreciationFile(@RequestPart(value = "file") MultipartFile file,@RequestPart(value = "year") String year,@RequestPart(value = "version") String version) {
		return ResponseEntity.ok(ncccBudgetService.uploadBudgetRuleDepreciationFile(file,year,version));
	}

	/**
	 * 合併SAP折舊預算檔案
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@PostMapping("/uploadBudgetRuleSAPDepreciationFile")
	public ResponseEntity<Object> uploadBudgetRuleSAPDepreciationFile(@RequestPart(value = "file") MultipartFile file,@RequestPart(value = "year") String year) throws UnsupportedEncodingException {
		var resp = ncccBudgetService.uploadBudgetRuleSAPDepreciationFile(file,year);
		if (resp.getSuccess()) {
			// 取出串流
			String fileName = "折舊預算檔案_" + year  + ".xlsx";

			// 下載
			// 設定響應標頭
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+URLEncoder.encode(fileName, "utf-8")+"\"");
			headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
			return ResponseEntity.ok()
					.headers(headers)
					.body((byte[]) resp.getData());
		}

		// 若下載不成功，返回相應的響應
		return ResponseEntity.ok(resp);
	}

	/**
	 * 新增預算編列
	 *
	 * @param model
	 * @return
	 */
	@PostMapping("/saveBudgetRule")
	public ResponseEntity<BudgetResponse> saveBudgetRule(@RequestBody BudgetRuleDTO model) {
		return ResponseEntity.ok(ncccBudgetService.saveBudgetRule(model));
	}

	/**
	 * 刪除預算編列規則
	 *
	 * @param model
	 * @return
	 */
	@PostMapping("/deleteBudgetRule")
	public ResponseEntity<BudgetResponse> deleteBudgetRule(@RequestBody BudgetRequestDto.GetRuleDto model) {
		return ResponseEntity.ok(ncccBudgetService.deleteBudgetRule(model));
	}

	/**
	 * 預算開帳
	 */
	@PostMapping("/openBudget")
	public ResponseEntity<BudgetResponse> openBudget(@RequestBody BudgetRequestDto.YearVersion model) {
		return ResponseEntity.ok(ncccBudgetService.openBudget(model));
	}

	/**
	 * 查詢是否已經開帳
	 * @param model
	 * @return
	 */
	@GetMapping("/checkBudgetOpen")
	public ResponseEntity<BudgetResponse> checkBudgetOpen(String year) {
		return ResponseEntity.ok(ncccBudgetService.checkBudgetOpen(year));
	}

	/**
	 * 預算關帳
	 */
	@PostMapping("/closeBudget")
	public ResponseEntity<BudgetResponse> closeBudget(@RequestBody BudgetRequestDto.YearVersion model) {
		return ResponseEntity.ok(ncccBudgetService.closeBudget(model));
	}

	/*
	 * 預算編列確認
	 */
	@PostMapping("/Confirm")
	public ResponseEntity<BudgetResponse> Confirm(@RequestBody List<String> batchList) {

		return ResponseEntity.ok(ncccBudgetService.Confirm(batchList));

	}

	/*
	 * 預算編列確認退回
	 */
	@PostMapping("/ConfirmReturn")
	public ResponseEntity<BudgetResponse> ConfirmReturn(@RequestBody List<String> batchList) {

		return ResponseEntity.ok(ncccBudgetService.ConfirmReturn(batchList));

	}

	/*
	 * 預算編列複核
	 */
	@PostMapping("/Approve")
	public ResponseEntity<BudgetResponse> Approve(@RequestBody List<String> batchList) {

		return ResponseEntity.ok(ncccBudgetService.Approve(batchList));

	}

	/*
	 * 預算編列複核退回
	 */
	@PostMapping("/ApproveReturn")
	public ResponseEntity<BudgetResponse> ApproveReturn(@RequestBody List<String> batchList) {

		return ResponseEntity.ok(ncccBudgetService.ApproveReturn(batchList));

	}

	/**
	 * 取得預算編列的預算項目
	 */
	@PostMapping("/getPreBudgetItem")
	public ResponseEntity<BudgetResponse> getPreBudgetItem(@RequestBody BudgetRequestDto.YearVersion model) {
		return ResponseEntity.ok(ncccBudgetService.getPreBudgetItem(model));
	}

	/**
	 * 取得預算主檔的預算項目
	 */
	@PostMapping("/getBudgetItem")
	public ResponseEntity<BudgetResponse> getBudgetItem(@RequestBody BudgetRequestDto.GetBudgetItemByOuCode model) {
		return ResponseEntity.ok(ncccBudgetService.getBudgetItem(model));
	}


	/**
	 * 取得部門剩餘預算
	 */
	@PostMapping("/getBudgetRemainByOuCode")
	public ResponseEntity<BudgetResponse> getBudgetRemainByOuCode(@RequestBody BudgetRequestDto.GetBudgetRemainByOuCode model) {
		return ResponseEntity.ok(ncccBudgetService.getBudgetRemainByOuCode(model));
	}

	/**
	 * 預算調撥儲存
	 */
	@PostMapping("/saveBudgetAllocation")
	public ResponseEntity<BudgetResponse> saveBudgetAllocation(@RequestBody BudgetAllocationDto model) {
		return ResponseEntity.ok(ncccBudgetService.saveBudgetAllocation(model));
	}

	/**
	 * 預算調撥查詢
	 */
	@PostMapping("/getBudgetAllocation")
	public ResponseEntity<BudgetResponse> getBudgetAllocation(@RequestBody BudgetRequestDto.GetBudgetAllocation model) {
		return ResponseEntity.ok(ncccBudgetService.getBudgetAllocation(model));
	}

	/**
	 * 預算調撥_核決判斷
	 */
	@PostMapping("/decisionBudgetAllocation")
	public ResponseEntity<BudgetResponse> decisionBudgetAllocation(@RequestBody DecisionVo model) {
		return ResponseEntity.ok(ncccBudgetService.decisionBudgetAllocation(model));
	}

	/**
	 * 預算查詢
	 */
	@PostMapping("/queryBudget")
	public ResponseEntity<BudgetResponse> queryBudget(@RequestBody BudgetRequestDto.QueryBudget model) {
		return ResponseEntity.ok(ncccBudgetService.queryBudget(model));
	}

	/**
	 * 預算交易明細查詢
	 */
	@PostMapping("/queryBudgetTransaction")
	public ResponseEntity<BudgetResponse> queryBudgetTransaction(@RequestBody BudgetRequestDto.QueryBudgetTransaction model) {
		return ResponseEntity.ok(ncccBudgetService.queryBudgetTransaction(model));
	}

	/**
	 * 預算保留儲存
	 */
	@PostMapping("/saveBudgetReserve")
	public ResponseEntity<BudgetResponse> saveBudgetReserve(@RequestBody BudgetReserveDto model) {
		return ResponseEntity.ok(ncccBudgetService.saveBudgetReserve(model));
	}

	/**
	 * 預算保留查詢
	 */
	@PostMapping("/queryBudgetReserve")
	public ResponseEntity<BudgetResponse> queryBudgetReserve(@RequestBody BudgetRequestDto.QueryBudgetReserve model) {
		return ResponseEntity.ok(ncccBudgetService.queryBudgetReserve(model));
	}

	/**
	 * 預算保留_核決判斷
	 */
	@PostMapping("/decisionBudgetReserve")
	public ResponseEntity<BudgetResponse> decisionBudgetReserve(@RequestBody DecisionVo model) {
		return ResponseEntity.ok(ncccBudgetService.decisionBudgetReserve(model));
	}

	/**
	 * 預算保留列印
	 */
	@PostMapping("/printBudgetReserve")
	public ResponseEntity<Object> printBudgetReserve(@RequestBody BudgetRequestDto.QueryBudgetReserve model) {
		return ResponseEntity.ok(ncccBudgetService.printBudgetReserve(model).data);
	}

	/**
	 * 預算保留_指派人員
	 */
	@PostMapping("/setBudgetReserveNextAssignee")
	public ResponseEntity<String> setBudgetReserveNextAssignee(@RequestBody AssignTasksVo assignTasksVo) {
		return ResponseEntity.ok(ncccBudgetService.setBudgetReserveNextAssignee(assignTasksVo));
	}

	/**
	 * 提列費用_儲存
	 */
	@PostMapping("/saveBudgetExpense")
	public ResponseEntity<BudgetResponse> saveBudgetExpense(@RequestBody BudgetExpenseDto model) {
		return ResponseEntity.ok(ncccBudgetService.saveBudgetExpense(model));
	}

	/**
	 * 提列費用_查詢
	 */
	@PostMapping("/queryBudgetExpense")
	public ResponseEntity<BudgetResponse> queryBudgetExpense(@RequestBody BudgetRequestDto.QueryBudgetExpense model) {
		return ResponseEntity.ok(ncccBudgetService.queryBudgetExpense(model));
	}

	/**
	 * 提列費用_核決判斷
	 */
	@PostMapping("/decisionBudgetExpense")
	public ResponseEntity<BudgetResponse> decisionBudgetExpense(@RequestBody DecisionVo model) {
		return ResponseEntity.ok(ncccBudgetService.decisionBudgetExpense(model));
	}

	/**
	 * 提列費用_指派人員
	 */
	@PostMapping("/setBudgetExpenseNextAssignee")
	public ResponseEntity<String> setBudgetExpenseNextAssignee(@RequestBody AssignTasksVo assignTasksVo) {
		return ResponseEntity.ok(ncccBudgetService.setBudgetExpenseNextAssignee(assignTasksVo));
	}

	/**
	 * 權責分攤_取得採購單資料
	 */
	@GetMapping("/getAllocationPOData")
	public ResponseEntity<BudgetResponse> getAllocationPOData() {
		return ResponseEntity.ok(ncccBudgetService.getAllocationPOData());
	}

	/**
	 * 權責分攤_取得詳細資料
	 */
	@PostMapping("/getAllocationDetails")
	public ResponseEntity<BudgetResponse> getAllocationDetails(@RequestBody BudgetResponseibilityAllocationDto.ReqDetailByPoNo model) {
		return ResponseEntity.ok(ncccBudgetService.getAllocationDetails(model));
	}

	/**
	 * 權責分攤_儲存資料
	 */
	@PostMapping("/saveResponsibilityAllocation")
	public ResponseEntity<BudgetResponse> saveResponsibilityAllocation(@RequestBody BudgetResponseibilityAllocationDto.allocationData model) {
		return ResponseEntity.ok(ncccBudgetService.saveResponseibilityAllocation(model));
	}

	/**
	 * 權責分攤_取得資料
	 * @param model
	 * @return
	 */
	@PostMapping("/getResponsibilityAllocation")
	public ResponseEntity<BudgetResponse> getResponsibilityAllocation(@RequestBody BudgetResponseibilityAllocationDto.QueryBudgetAllocation model) {
		return ResponseEntity.ok(ncccBudgetService.getResponsibilityAllocation(model));
	}

	/**
	 * 權責分攤_核決判斷
	 */
	@PostMapping("/decisionResponsibilityAllocation")
	public ResponseEntity<BudgetResponse> decisionResponsibilityAllocation(@RequestBody DecisionVo model) {
		return ResponseEntity.ok(ncccBudgetService.decisionResponsibilityAllocation(model));
	}

	/**
	 * 權責分攤_查詢SAP拋轉資料
	 */
	@PostMapping("/queryResponsibilityAllocationSAPData")
	public ResponseEntity<BudgetResponse> queryResponsibilityAllocationSAPData(@RequestBody BudgetResponseibilityAllocationDto.QueryBudgetAllocationSAPData model) {
		return ResponseEntity.ok(ncccBudgetService.queryResponsibilityAllocationSAPData(model));
	}

	/**
	 * 權責分攤_SAP拋轉資料
	 */
	@PostMapping("/transferAllocationToSAP")
	public ResponseEntity<BudgetResponse> transferAllocationToSAP(@RequestBody BudgetResponseibilityAllocationDto.transferAllocationToSAPReq model) {
		return ResponseEntity.ok(ncccBudgetService.transferAllocationToSAP(model));
	}

	/**
	 * 查詢預算餘額
	 */
	@PostMapping("/getBudgetBalance")
	public ResponseEntity<BudgetResponse> getBudgetBalance(@RequestBody BudgetVo.SearchBudgetBalanceData model) {
		return ResponseEntity.ok(ncccBudgetService.getBudgetBalance(model));
	}

	/**
	 * 取得對應預算部門
	 *
	 * @return
	 **/
	@GetMapping("/getBudgetOuCodeByOuCode")
	public ResponseEntity<BudgetResponse> getBudgetOuCodeByOuCode(String ouCode) {
		return ResponseEntity.ok(ncccBudgetService.getBudgetOuCodeByOuCode(ouCode));
	}

	/**
	 * 查詢多筆預算餘額
	 */
	@PostMapping("/getBudgetBalanceList")
	public ResponseEntity<BudgetResponse> getBudgetBalanceList(@RequestBody BudgetVo.SearchBudgetBalanceListData model) {
		return ResponseEntity.ok(ncccBudgetService.getBudgetBalanceList(model));
	}
}