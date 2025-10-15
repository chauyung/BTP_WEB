package nccc.btp.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

/**
 * 預算管理模組 : 預算編列-業務目標及分項計價維護.查詢(Request)
 * ------------------------------------------------------
 * 建立人員: ChauYung
 * 建立日期: 2025-10-08
 */
@Builder
@Data
public class BudgetaryPhasePriceingSaveDetailDto implements Serializable {
	private static final long serialVersionUID = 2348636185440234694L;

	String itemCode;
	
	String itemName;
	
	String budgetDepartmentCode;
	
	String budgetDepartmentName;
	
	BigDecimal amount;
	
	String remark;
}
