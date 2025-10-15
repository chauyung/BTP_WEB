package nccc.btp.dto;

import java.io.Serializable;

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
public class BudgetaryPhasePriceingQueryDto implements Serializable {
	private static final long serialVersionUID = 1518031538054693219L;

	private String documentNo;
	
	private String budgetYear;
	
	private String version;
	
	private String applyDate;
	
	private String applyUserCode;
	
	private String applyUserName;
	
	private String departmentCode;
	
	private String departmentName;
	
	private String assignment;
}
