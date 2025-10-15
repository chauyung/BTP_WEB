package nccc.btp.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 預算管理模組 : 預算編列-預算分攤展算批次作業.執行分攤(Request)
 * ------------------------------------------------------
 * 建立人員: ChauYung
 * 建立日期: 2025-10-08
 */
@Builder
@Data
@EqualsAndHashCode(callSuper=true)
public class BudgetaryApportionmentCheckExecutRequest extends BudgetaryApportionmentCheckDto implements Serializable {
	private static final long serialVersionUID = 3504173660538197557L;
	
	/**
	 * 是否 清掉舊有資料(預設:FALSE)
	 */
	@Builder.Default
	private Boolean clearOld = false;
}
