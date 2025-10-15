package nccc.btp.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nccc.btp.dto.BudgetaryApportionmentCheckApprovalRequest;
import nccc.btp.dto.BudgetaryApportionmentCheckExecutRequest;
import nccc.btp.dto.BudgetaryApportionmentCheckExecutedRequest;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.repository.NcccBudgetApportionRepository;
import nccc.btp.service.BudgetaryApportionmentService;
import nccc.btp.util.SecurityUtil;

/**
 * 預算管理模組 : 預算編列-預算分攤展算批次作業(Service)
 * ------------------------------------------------------
 * 建立人員: ChauYung(Team)
 * 建立日期: 2025-10-08
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BudgetaryApportionmentServiceImpl implements BudgetaryApportionmentService{
	
	// 預算編列-預算分攤展算表
	private final NcccBudgetApportionRepository ncccBudgetApportionRepository;	
	

	/**
	 * 檢查 預算編列複核狀態
	 * @param req - 預算管理模組 : 預算編列-預算分攤展算批次作業.檢查預算編列複核狀態(Request)
	 * @return - 是/否 通過預算編列複核狀態
	 */
	@Override
	public Boolean checkApproval(BudgetaryApportionmentCheckApprovalRequest req) {
		
		// STEP_01: 取得預算編列(未核覆)資料量
		Integer dataCount = ncccBudgetApportionRepository.checkApproval(req.getYear(), req.getVersion());
		
		// STEP_02: 如果資料存在, 顯示錯誤訊息
		return (dataCount > 0) ? Boolean.FALSE: Boolean.TRUE;
	}

	/**
	 * 檢查 是否已執行過分攤
	 * @param req - 預算管理模組 : 預算編列-預算分攤展算批次作業.檢查是否已執行過分攤(Request)
	 * @return - 是/否 已執行過分攤
	 */
	@Override
	public Boolean checkExecuted(BudgetaryApportionmentCheckExecutedRequest req) {
		
		// STEP_01: 檢查是否重複執行
		Integer dataCount = ncccBudgetApportionRepository.fetchCount(req.getYear(), req.getVersion());
		
		// STEP_02: 如果資料存在, 顯示錯誤訊息
		return (dataCount > 0) ? Boolean.FALSE: Boolean.TRUE;
	}

	/**
	 * 執行 分攤
	 * @param req - 預算管理模組 : 預算編列-預算分攤展算批次作業.執行分攤(Request)
	 * @return - 執行成功/已有資料存在(需清除)
	 */
	@Override
	public Boolean checkExecut(BudgetaryApportionmentCheckExecutRequest req) {
		
		// STEP_01: 依據前甝設定(是否強制清除)進行查核, 或直接清除
		if (Boolean.TRUE.equals(req.getClearOld())) {
			ncccBudgetApportionRepository.deleteBy(req.getYear(), req.getVersion());
		} else {
			Integer dataCount = ncccBudgetApportionRepository.fetchCount(req.getYear(), req.getVersion());
			if (dataCount > 0) {
				return Boolean.FALSE;
			}
		}
		
		// STEP_02: 執行 分攤
		this.execute(req);
		return Boolean.TRUE;
	}
	
	/**
	 * 執行 分攤
	 * @param req - 預算管理模組 : 預算編列-預算分攤展算批次作業.執行分攤(Request)
	 */
	private void execute(BudgetaryApportionmentCheckExecutRequest req) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		
		// STEP_01: 執行 資料備妥(分攤前)
		ncccBudgetApportionRepository.checkExecut01(req.getYear(), req.getVersion(), user.getUserId());
		
		// STEP_02: 執行 預算攤分(分攤後.無需分攤)
		ncccBudgetApportionRepository.checkExecut02(req.getYear(), req.getVersion());
		
		// STEP_03: 執行 預算攤分(分攤後.部門分攤_依表4拾5入到仟位)
	    ncccBudgetApportionRepository.checkExecut03(req.getYear(), req.getVersion());
	    
	    // STEP_04: 執行 預算攤分(分攤後.移除過渡資料)
	    ncccBudgetApportionRepository.checkExecut00(req.getYear(), req.getVersion(), "DEP");
	    
	    // STEP_05: 執行 預算攤分(分攤後.部門分攤_總數差值列計於最大筆中)
	    ncccBudgetApportionRepository.checkExecut05(req.getYear(), req.getVersion());
	    
	    // STEP_06: 執行 預算攤分(分攤後.項目分攤_人事費_依表4拾5入到仟位)
	    ncccBudgetApportionRepository.checkExecut06(req.getYear(), req.getVersion());
	    
	    // STEP_07: 執行 預算攤分(分攤後.項目分攤_業務費_依表4拾5入到仟位)
	    ncccBudgetApportionRepository.checkExecut07(req.getYear(), req.getVersion());
	    
	    // STEP_08: 執行 預算攤分(分攤後.移除過渡資料)
	    ncccBudgetApportionRepository.checkExecut00(req.getYear(), req.getVersion(), "DEP_FINAL");
	    
	    // STEP_09: 執行 預算攤分(分攤後.項目分攤_總數差值列計於最大筆中)
	    ncccBudgetApportionRepository.checkExecut09(req.getYear(), req.getVersion());
	    
	    // STEP_10: 執行 預算攤分(調整備註)
	    ncccBudgetApportionRepository.checkExecut10(req.getYear(), req.getVersion());
	}
}
