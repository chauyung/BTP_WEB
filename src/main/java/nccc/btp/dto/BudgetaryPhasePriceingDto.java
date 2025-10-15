package nccc.btp.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 預算管理模組 : 預算編列-業務目標及分項計價維護.取得分項計價明細(DTO)
 * ------------------------------------------------------
 * 建立人員: ChauYung
 * 建立日期: 2025-10-08
 */
@SuperBuilder
@Data
public class BudgetaryPhasePriceingDto implements Serializable {
	private static final long serialVersionUID = 3587765304744595382L;
	
	String documentNo;
	String budgetYear;
	String version;
	String applyDate;
	String applyUserCode;
	String applyUserName;
	String departmentCode;
	String departmentName;
	String assignment;
	
	List<BudgetaryPhasePriceingSaveDetailDto> detail;
}
