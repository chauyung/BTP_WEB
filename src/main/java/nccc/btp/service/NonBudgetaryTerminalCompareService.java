package nccc.btp.service;

import java.util.Optional;

/**
 * 報表管理：非預算 報表-端末機差異比較表(Service)
 * ------------------------------------------------------
 * 建立人員: ChauYung(Team)
 * 建立日期: 2025-10-08
 */
public interface NonBudgetaryTerminalCompareService {
	
	/**
	 * 產檔(Excel)
	 * @return - Excel(Byte)
	 */
	public Optional<byte[]> exportExcel();
}
