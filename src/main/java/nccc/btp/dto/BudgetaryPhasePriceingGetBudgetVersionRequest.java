package nccc.btp.dto;

import java.io.Serializable;

import lombok.experimental.SuperBuilder;

/**
 * 預算管理模組 : 預算編列-業務目標及分項計價維護.取得該年度版次狀態(Request)
 * ------------------------------------------------------
 * 建立人員: ChauYung
 * 建立日期: 2025-10-08
 */
@SuperBuilder
public class BudgetaryPhasePriceingGetBudgetVersionRequest extends BudgetaryPhasePriceingGetBudgetVersionDto implements Serializable {
	private static final long serialVersionUID = 1503508648108393964L;
}
