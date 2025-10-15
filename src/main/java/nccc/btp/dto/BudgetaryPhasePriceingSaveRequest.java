package nccc.btp.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 預算管理模組 : 預算編列-業務目標及分項計價維護.儲存(Request)
 * ------------------------------------------------------
 * 建立人員: ChauYung
 * 建立日期: 2025-10-08
 */
@SuperBuilder
@Data
public class BudgetaryPhasePriceingSaveRequest implements Serializable {
	private static final long serialVersionUID = -2411849066448426750L;
}
