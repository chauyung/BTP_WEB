package nccc.btp.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nccc.btp.dto.BudgetaryApportionmentCheckApprovalRequest;
import nccc.btp.dto.BudgetaryApportionmentCheckDto;
import nccc.btp.dto.BudgetaryApportionmentCheckExecutRequest;
import nccc.btp.dto.BudgetaryApportionmentCheckExecutedRequest;
import nccc.btp.response.ApiResponse;
import nccc.btp.service.BudgetaryApportionmentService;

/**
 * 預算管理模組 : 預算編列-預算分攤展算批次作業(Controller)
 * ------------------------------------------------------
 * 建立人員: ChauYung(Team)
 * 建立日期: 2025-10-08
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/budgetaryApportionment")
public class BudgetaryApportionmentController {
	
	// 預算管理模組 : 預算編列-預算分攤展算批次作業(Service)
	private final BudgetaryApportionmentService budgetaryApportionmentService;
	

	/**
	 * 檢查 預算編列複核狀態
	 * @param req - 預算管理模組 : 預算編列-預算分攤展算批次作業.檢查預算編列複核狀態(Request)
	 * @return - 預算管理模組 : 預算編列-預算分攤展算批次作業.檢查預算編列複核狀態(Response)
	 */
	@PostMapping("/checkApproval")
	public ApiResponse<BudgetaryApportionmentCheckDto> checkApproval(BudgetaryApportionmentCheckApprovalRequest req) {

		ApiResponse<BudgetaryApportionmentCheckDto> response = 
				ApiResponse.success((BudgetaryApportionmentCheckDto)req);
		try {
			// 檢查 預算編列複核狀態
			Boolean reponseValue = budgetaryApportionmentService.checkApproval(req);
			
			if (Boolean.TRUE.equals(reponseValue)) {
				// 複核狀態已完成(OK)
				response.setStatus("OK");
				response.setMSG("預算編列複核狀態已完成");
			} else {
				// 複核狀態未完成(FAIL)
				response.setStatus("FAIL");
				response.setMSG("預算編列有未覆核資料, 不能執行分攤展算");
			}
		} catch(Exception ex) {
			// 發生例外狀況
			response.setStatus("EXCEPTION");
			response.setMSG(ex.getMessage());
		}
		return response;
	}
	
	/**
	 * 檢查 是否已執行過分攤
	 * @param req - 預算管理模組 : 預算編列-預算分攤展算批次作業.檢查是否已執行過分攤(Request)
	 * @return - 預算管理模組 : 預算編列-預算分攤展算批次作業.檢查是否已執行過分攤(Response)
	 */
	@PostMapping("/checkExecuted")
	public ApiResponse<BudgetaryApportionmentCheckDto> checkExecuted(BudgetaryApportionmentCheckExecutedRequest req) {

		ApiResponse<BudgetaryApportionmentCheckDto> response = 
				ApiResponse.success((BudgetaryApportionmentCheckDto)req);
		try {
			// 檢查 是否已執行過分攤
			Boolean reponseValue = budgetaryApportionmentService.checkExecuted(req);
			
			if (Boolean.TRUE.equals(reponseValue)) {
				// 未執行過(clear)
				response.setStatus("OK");
				response.setMSG("未執行過");
			} else {
				// 已執行過(duplicate)
				response.setStatus("FAIL");
				response.setMSG("該年月已經執行預算分攤展算批次作業, 按確定清除該年月資料, 按取消結束程式");
			}
		} catch (Exception ex) {
			// 發生例外狀況
			response.setStatus("EXCEPTION");
			response.setMSG(ex.getMessage());
		}
		return response;
	}
	
	/**
	 * 執行 分攤
	 * @param req - 預算管理模組 : 預算編列-預算分攤展算批次作業.執行分攤(Request)
	 * @return - 預算管理模組 : 預算編列-預算分攤展算批次作業.執行分攤(Response)
	 */
	@PostMapping("/checkExecut")
	public ApiResponse<BudgetaryApportionmentCheckDto> checkExecut(BudgetaryApportionmentCheckExecutRequest req) {
		
		ApiResponse<BudgetaryApportionmentCheckDto> response = 
				ApiResponse.success((BudgetaryApportionmentCheckDto)req);
		try {
			// 執行 分攤
			Boolean reponseValue = budgetaryApportionmentService.checkExecut(req);
						
			if (Boolean.TRUE.equals(reponseValue)) {
				// 分攤展算執行成功(success)
				response.setStatus("OK");
				response.setMSG("分攤展算執行成功");
			} else {
				// 已有資料存在(need-clear)
				response.setStatus("FAIL");
				response.setMSG("已有資料存在(需清除)");
			}
		} catch(Exception ex) {
			// 發生例外狀況
			response.setStatus("EXCEPTION");
			response.setMSG(ex.getMessage());
		}
		return response;
	}
}
