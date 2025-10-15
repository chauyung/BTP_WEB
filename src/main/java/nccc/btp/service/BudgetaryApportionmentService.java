package nccc.btp.service;

import nccc.btp.dto.BudgetaryApportionmentCheckApprovalRequest;
import nccc.btp.dto.BudgetaryApportionmentCheckExecutRequest;
import nccc.btp.dto.BudgetaryApportionmentCheckExecutedRequest;

/**
 * 預算管理模組 : 預算編列-預算分攤展算批次作業(Service)
 * ------------------------------------------------------
 * 建立人員: ChauYung(Team)
 * 建立日期: 2025-10-08
 */
public interface BudgetaryApportionmentService {
	
	/**
	 * 檢查 預算編列複核狀態
	 * @param req - 預算管理模組 : 預算編列-預算分攤展算批次作業.檢查預算編列複核狀態(Request)
	 * @return - 是/否 通過預算編列複核狀態
	 */
	public Boolean checkApproval(BudgetaryApportionmentCheckApprovalRequest req);
	
	
	/**
	 * 檢查 是否已執行過分攤
	 * @param req - 預算管理模組 : 預算編列-預算分攤展算批次作業.檢查是否已執行過分攤(Request)
	 * @return - 是/否 已執行過分攤
	 */
	public Boolean checkExecuted(BudgetaryApportionmentCheckExecutedRequest req);
	
	
	/**
	 * 執行 分攤
	 * @param req - 預算管理模組 : 預算編列-預算分攤展算批次作業.執行分攤(Request)
	 * @return - 執行成功/已有資料存在(需清除)
	 */
	public Boolean checkExecut(BudgetaryApportionmentCheckExecutRequest req);
}
