package nccc.btp.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * 預算管理模組 : 預算編列-業務目標及分項計價維護.刪除(Request)
 * ------------------------------------------------------
 * 建立人員: ChauYung
 * 建立日期: 2025-10-08
 */
@Builder
@Data
public class BudgetaryPhasePriceingDeleteRequest implements Serializable {
	private static final long serialVersionUID = -7379278500717945148L;

	List<String> documentNo;
}
