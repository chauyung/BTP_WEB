package nccc.btp.service;

import java.util.List;

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

/**
 * 預算管理模組 : 預算編列-業務目標及分項計價維護(Service)
 * ------------------------------------------------------
 * 建立人員: ChauYung(Team)
 * 建立日期: 2025-10-08
 */
public interface BudgetaryPhasePriceingService {

	/**
	 * 查詢
	 * @param req
	 * @return
	 */
	List<BudgetaryPhasePriceingQueryDto> query(BudgetaryPhasePriceingQueryRequest req);
	
	/**
	 * 刪除
	 * @param req
	 */
	void delete(BudgetaryPhasePriceingDeleteRequest req);
	
	/**
	 * 儲存
	 * @param req
	 */
	void save(BudgetaryPhasePriceingSaveRequest req);
	
	/**
	 * LOV:補助清單選取(單選)
	 * @return
	 */
	List<BudgetaryPhasePriceingGetItemPriceDto> getItemPriceList();
	
	/**
	 * [取得該年度版次狀態]
	 * @param req
	 * @return
	 */
	BudgetaryPhasePriceingGetBudgetVersionDto getBudgetVersion(BudgetaryPhasePriceingGetBudgetVersionRequest req);
	
	/**
	 * [檢查是否已開帳]
	 * @param req
	 * @return
	 */
	BudgetaryPhasePriceingCheckBudgetOpenDto checkBudgetOpen(BudgetaryPhasePriceingCheckBudgetOpenRequest req);
	
	/**
	 * [取得分項計價明細]
	 * @param req
	 * @return
	 */
	BudgetaryPhasePriceingDto getItemPriceDetails(BudgetaryPhasePriceingGetItemPriceDetailsRequest req);
}
