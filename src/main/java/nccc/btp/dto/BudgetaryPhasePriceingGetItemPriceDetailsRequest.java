package nccc.btp.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 預算管理模組 : 預算編列-業務目標及分項計價維護.取得分項計價明細(Request)
 * ------------------------------------------------------
 * 建立人員: ChauYung
 * 建立日期: 2025-10-08
 */
@SuperBuilder
@Data
public class BudgetaryPhasePriceingGetItemPriceDetailsRequest implements Serializable {
	private static final long serialVersionUID = -6192440656549665818L;

	String documentNo;
}
