package nccc.btp.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 報表管理：非預算 報表-端末機帳列數報表(API-Request)
 * ------------------------------------------------------
 * 建立人員: ChauYung
 * 建立日期: 2025-10-08
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NonBudgetaryTerminalSummaryRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 年月(YYYYMM)
	 */
	private String yearMonth;
}
