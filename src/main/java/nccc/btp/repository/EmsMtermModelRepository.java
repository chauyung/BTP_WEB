package nccc.btp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import nccc.btp.entity.EmsMtermModel;

/**
 * 報表管理：端末機機型資料檔(Repository)
 * ------------------------------------------------------
 * 建立人員: ChauYung(Team)
 * 建立日期: 2025-10-08
 */
public interface EmsMtermModelRepository extends JpaRepository<EmsMtermModel, String> {

	/**
	 * 報表管理：非預算 報表-端末機帳列數報表(SQL)
	 */
	interface EmsTerminalCompareRow {
		String getData01();     // [機型名稱]
		String getKey01();      // [機型名稱]
		String getKey02();      // [機型名稱]
		String getData02();     // [機型名稱]
	}
	
	/**
	 * 報表管理：非預算 報表-端末機帳列數報表(SQL)
	 * @return
	 */
	@Query(value = ""
			+ "SELECT T1.MODEL_DESC     AS DATA01,  -- [機型名稱] \n"
			+ "       T1.NCCC_WEALTH_NO AS KEY01,   -- [行管部財編] \n"
			+ "       T1.MODEL_NO       AS KEY02,   -- [設備代號] \n"
			+ "       T1.DEL_FLAG       AS DATA01   -- [資產狀態] \n"
			+ "  FROM EMS_MTERM_MODEL T1 \n"
			+ " WHERE 1 = 1 \n"
			+ " ORDER BY T1.T1.MODEL_DESC ASC \n"
			, nativeQuery = true)
	List<EmsTerminalCompareRow> fetchNonBudgetaryTerminalCompare();
}
