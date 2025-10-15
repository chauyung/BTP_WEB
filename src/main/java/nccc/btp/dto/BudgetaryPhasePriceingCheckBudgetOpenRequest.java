package nccc.btp.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 預算管理模組 : 預算編列-業務目標及分項計價維護.檢查是否已開帳(Request)
 * ------------------------------------------------------
 * 建立人員: ChauYung
 * 建立日期: 2025-10-08
 */
@Builder
@Data
public class BudgetaryPhasePriceingCheckBudgetOpenRequest implements Serializable {
	private static final long serialVersionUID = 4320375568617825787L;

	String year;
}
