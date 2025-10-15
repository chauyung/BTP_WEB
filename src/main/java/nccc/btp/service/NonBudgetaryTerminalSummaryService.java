package nccc.btp.service;

import java.util.Optional;

import nccc.btp.dto.NonBudgetaryTerminalSummaryRequest;

/**
 * 報表管理：非預算 報表-端末機帳列數報表(Service)
 * ------------------------------------------------------
 * 建立人員: ChauYung(Team)
 * 建立日期: 2025-10-08
 */
public interface NonBudgetaryTerminalSummaryService {

	/**
	 * 產檔(Excel)
	 * @param req - API-Request
	 * @return - Excel(Byte)
	 */
	public Optional<byte[]> exportExcel(NonBudgetaryTerminalSummaryRequest req);
}
