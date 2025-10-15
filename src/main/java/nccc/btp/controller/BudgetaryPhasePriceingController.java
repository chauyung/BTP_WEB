package nccc.btp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nccc.btp.dto.BudgetaryPhasePriceingCheckBudgetOpenDto;
import nccc.btp.dto.BudgetaryPhasePriceingCheckBudgetOpenRequest;
import nccc.btp.dto.BudgetaryPhasePriceingDeleteRequest;
import nccc.btp.dto.BudgetaryPhasePriceingDto;
import nccc.btp.dto.BudgetaryPhasePriceingGetBudgetVersionDto;
import nccc.btp.dto.BudgetaryPhasePriceingGetBudgetVersionRequest;
import nccc.btp.dto.BudgetaryPhasePriceingGetItemPriceDetailsRequest;
import nccc.btp.dto.BudgetaryPhasePriceingGetItemPriceDto;
import nccc.btp.dto.BudgetaryPhasePriceingQueryDto;
import nccc.btp.dto.BudgetaryPhasePriceingQueryRequest;
import nccc.btp.dto.BudgetaryPhasePriceingSaveRequest;
import nccc.btp.response.ApiResponse;
import nccc.btp.service.BudgetaryPhasePriceingService;

/**
 * 預算管理模組 : 預算編列-業務目標及分項計價維護(Controller)
 * ------------------------------------------------------
 * 建立人員: ChauYung(Team)
 * 建立日期: 2025-10-08
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/budgetaryPhasePriceing")
public class BudgetaryPhasePriceingController {

	// 預算管理模組 : 預算編列-業務目標及分項計價維護(Service)
	private final BudgetaryPhasePriceingService budgetaryPhasePriceingService;
	
	/**
	 * 查詢
	 * @param req
	 * @return
	 */
	@PostMapping("/query")
	public ApiResponse<List<BudgetaryPhasePriceingQueryDto>> query(BudgetaryPhasePriceingQueryRequest req) {
		
		ApiResponse<List<BudgetaryPhasePriceingQueryDto>> response = 
				ApiResponse.success(null);
		try {
			// 查詢
			List<BudgetaryPhasePriceingQueryDto> resData = budgetaryPhasePriceingService.query(req);
			
			response.setStatus("OK");
			response.setMSG("查詢成功");
			response.setData(resData);
		} catch(Exception ex) {
			// 發生例外狀況
			response.setStatus("EXCEPTION");
			response.setMSG(ex.getMessage());
		}
		return response;
	}
	
	/**
	 * 刪除
	 * @param req
	 * @return
	 */
	@PostMapping("/delete")
	public ApiResponse<Object> delete(BudgetaryPhasePriceingDeleteRequest req) {
		
		ApiResponse<Object> response = 
				ApiResponse.success(null);
		try {
			// 刪除
			budgetaryPhasePriceingService.delete(req);
			
			response.setStatus("OK");
			response.setMSG("刪除成功");
		} catch(Exception ex) {
			// 發生例外狀況
			response.setStatus("EXCEPTION");
			response.setMSG(ex.getMessage());
		}
		return response;
	}
	
	/**
	 * 儲存
	 * @param req
	 * @return
	 */
	@PostMapping("/save")
	public ApiResponse<Object> save(BudgetaryPhasePriceingSaveRequest req) {
		
		ApiResponse<Object> response = 
				ApiResponse.success(null);
		try {
			// 儲存
			budgetaryPhasePriceingService.save(req);
			
			response.setStatus("OK");
			response.setMSG("儲存成功");
		} catch(Exception ex) {
			// 發生例外狀況
			response.setStatus("EXCEPTION");
			response.setMSG(ex.getMessage());
		}
		return response;
	}
	
	/**
	 * [查詢分項計價清單]: LOV:補助清單選取(單選)
	 * @param req
	 * @return
	 */
	@PostMapping("/getItemPriceList")
	public ApiResponse<List<BudgetaryPhasePriceingGetItemPriceDto>> getItemPriceList() {
		
		ApiResponse<List<BudgetaryPhasePriceingGetItemPriceDto>> response = 
				ApiResponse.success(null);
		try {
			// LOV:補助清單選取(單選)
			List<BudgetaryPhasePriceingGetItemPriceDto> resData = 
					budgetaryPhasePriceingService.getItemPriceList();
			
			response.setStatus("OK");
			response.setMSG("查詢成功");
			response.setData(resData);
		} catch(Exception ex) {
			// 發生例外狀況
			response.setStatus("EXCEPTION");
			response.setMSG(ex.getMessage());
		}
		return response;
	}
	
	/**
	 * [取得該年度版次狀態]
	 * @param req
	 * @return
	 */
	@PostMapping("/getBudgetVersion")
	public ApiResponse<BudgetaryPhasePriceingGetBudgetVersionDto> checkAppgetBudgetVersionroval(BudgetaryPhasePriceingGetBudgetVersionRequest req) {
		
		ApiResponse<BudgetaryPhasePriceingGetBudgetVersionDto> response = 
				ApiResponse.success(null);
		try {
			// [取得該年度版次狀態]
			BudgetaryPhasePriceingGetBudgetVersionDto resData = 
					budgetaryPhasePriceingService.getBudgetVersion(req);
			
			response.setStatus("OK");
			response.setMSG("查詢成功");
			response.setData(resData);
		} catch(Exception ex) {
			// 發生例外狀況
			response.setStatus("EXCEPTION");
			response.setMSG(ex.getMessage());
		}
		return response;
	}
	
	/**
	 * [檢查是否已開帳]
	 * @param req
	 * @return
	 */
	@PostMapping("/checkBudgetOpen")
	public ApiResponse<BudgetaryPhasePriceingCheckBudgetOpenDto> checkBudgetOpen(BudgetaryPhasePriceingCheckBudgetOpenRequest req) {
		
		ApiResponse<BudgetaryPhasePriceingCheckBudgetOpenDto> response = 
				ApiResponse.success(null);
		try {
			// [檢查是否已開帳]
			BudgetaryPhasePriceingCheckBudgetOpenDto resData = 
					budgetaryPhasePriceingService.checkBudgetOpen(req);
			
			response.setStatus("OK");
			response.setMSG("查詢成功");
			response.setData(resData);
		} catch(Exception ex) {
			// 發生例外狀況
			response.setStatus("EXCEPTION");
			response.setMSG(ex.getMessage());
		}
		return response;
	}
	
	/**
	 * [取得分項計價明細]
	 * @param req
	 * @return
	 */
	@PostMapping("/getItemPriceDetails")
	public ApiResponse<BudgetaryPhasePriceingDto> getItemPriceDetails(BudgetaryPhasePriceingGetItemPriceDetailsRequest req) {
		
		ApiResponse<BudgetaryPhasePriceingDto> response = 
				ApiResponse.success(null);
		try {
			// 查詢
			BudgetaryPhasePriceingDto resData = 
					budgetaryPhasePriceingService.getItemPriceDetails(req);
			
			response.setStatus("OK");
			response.setMSG("查詢成功");
			response.setData(resData);
		} catch(Exception ex) {
			// 發生例外狀況
			response.setStatus("EXCEPTION");
			response.setMSG(ex.getMessage());
		}
		return response;
	}
}
