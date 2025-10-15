package nccc.btp.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import nccc.btp.entity.NcccEmsMgm;

public interface NcccEmsMgmRepository extends JpaRepository<NcccEmsMgm, String>, JpaSpecificationExecutor<NcccEmsMgm> {

	/**
	 * 報表管理：非預算 報表-端末機帳列數報表(View)
	 */
	interface TerminalSummaryRow {
		String getSn();         // [項次]
		String getData01();     // [機型]
		BigDecimal getSum01();  // [當月出售]
		BigDecimal getSum02();  // [當月報廢]
		BigDecimal getSum03();  // [當月減損]
		BigDecimal getSum04();  // [當月新增]
		BigDecimal getSum05();  // [列帳]
		BigDecimal getSum06();  // [列管]
		BigDecimal getSum07();  // [使用中合計]
	}
	
	/**
	 * 報表管理：非預算 報表-端末機帳列數報表(SQL)
	 */
	interface BtpTerminalCompareRow {
		String getData01();     // [機型名稱]
		String getKey01();      // [機型名稱]
		String getKey02();      // [機型名稱]
		String getData02();     // [機型名稱]
	}

	/**
	 * 報表管理：非預算 報表-端末機帳列數報表(SQL)
	 * @param yyyymm
	 * @return
	 */
	@Query(value = ""
			+ "SELECT ROW_NUMBER() OVER \n"
			+ "              (PARTITION BY 1 \n"
			+ "                   ORDER BY NVL(X1.DATA_01, X2.DATA_01) ASC) AS SN, \n"
			+ "       NVL(X1.DATA_01, X2.DATA_01)           AS DATA01, \n"
			+ "       NVL(X2.SUM_01, 0)                     AS SUM01,  \n"
			+ "       NVL(X2.SUM_02, 0)                     AS SUM02,  \n"
			+ "       NVL(X3.SUM_03, 0)                     AS SUM03,  \n"
			+ "       NVL(X2.SUM_04, 0)                     AS SUM04,  \n"
			+ "       NVL(X1.SUM_05, 0)                     AS SUM05,  \n"
			+ "       NVL(X1.SUM_06, 0)                     AS SUM06,  \n"
			+ "       NVL(X1.SUM_05, 0) + NVL(X1.SUM_06, 0) AS SUM07   \n"
			+ "  FROM ( SELECT T1.MODEL_NO                                     AS DATA_01, -- [機型] \n"
			+ "                SUM(CASE WHEN T1.STATUS = '' THEN 1 ELSE 0 END) AS SUM_05,  -- [列帳] \n"
			+ "                SUM(CASE WHEN T1.STATUS = '' THEN 1 ELSE 0 END) AS SUM_06   -- [列管] \n"
			+ "           FROM NcccEmsMgm T1 \n"
			+ "          WHERE 1 = 1 \n"
			+ "            AND T1.CREATE_DATE <  ADD_MONTHS(TO_DATE(:yyyymm||'01', 'YYYYMMDD'), 1) \n"
			+ "          GROUP BY T1.MODEL_NO ) X1 \n"
			+ "       FULL JOIN ( SELECT T1.MODEL_NO                                      AS DATA_01, -- [機型] \n"
			+ "                          SUM(CASE WHEN T1.STATUS = '4' THEN 1 ELSE 0 END) AS SUM_01,  -- [當月出售] \n"
			+ "                          SUM(CASE WHEN T1.STATUS = '3' THEN 1 ELSE 0 END) AS SUM_02,  -- [當月報廢] \n"
			+ "                          SUM(CASE WHEN T1.STATUS = '2' THEN 1 ELSE 0 END) AS SUM_04   -- [當月新增] \n"
			+ "                     FROM NCCC_EMS_MGM T1 \n"
			+ "                    WHERE 1 = 1 \n"
			+ "                      AND T1.UPDATE_DATE >= TO_DATE(:yyyymm||'01', 'YYYYMMDD') \n"
			+ "                      AND T1.UPDATE_DATE <  ADD_MONTHS(TO_DATE(:yyyymm||'01', 'YYYYMMDD'), 1) \n"
			+ "                    GROUP BY T1.MODEL_NO ) X2 \n"
			+ "              ON X2.DATA_01 = X1.DATA_01 \n"
			+ "       FULL JOIN ( SELECT T1.MODEL_NO                                      AS DATA_01, -- [機型] \n"
			+ "                          COUNT(1)                                         AS SUM_03   -- [當月減損] \n"
			+ "                     FROM NCCC_EMS_MGM T1 \n"
			+ "                    WHERE 1 = 1 \n"
			+ "                      AND T1.IMPAIRMENT_DATE >= TO_DATE(:yyyymm||'01', 'YYYYMMDD') \n"
			+ "                      AND T1.IMPAIRMENT_DATE <  ADD_MONTHS(TO_DATE(:yyyymm||'01', 'YYYYMMDD'), 1) \n"
			+ "                    GROUP BY T1.MODEL_NO ) X3 \n"
			+ "              ON X3.DATA_01 = X1.DATA_01 \n"
			+ " WHERE 1 = 1 \n"
			, nativeQuery = true)
	List<TerminalSummaryRow> fetchNonBudgetaryTerminalSummary(@Param("yyyymm") String yyyymm);
	
	/**
	 * 報表管理：非預算 報表-端末機帳列數報表(SQL)
	 * @return
	 */
	@Query(value = ""
			+ "SELECT T1.MODEL_NAME     AS DATA01,  -- [機型名稱] \n"
			+ "       T1.NCCC_WEALTH_NO AS KEY01,   -- [行管部財編] \n"
			+ "       T1.EQ_TYPE        AS KEY02,   -- [設備代號] \n"
			+ "       T1.STATUS         AS DATA02   -- [資產狀態] \n"
			+ "  FROM NCCC_EMS_MGM T1 \n"
			+ " WHERE 1 = 1 \n"
			+ " ORDER BY T1.T1.MODEL_NAME ASC \n"
			, nativeQuery = true)
	List<BtpTerminalCompareRow> fetchNonBudgetaryTerminalCompare();

	@Override
	@EntityGraph(attributePaths = "sapEmsRecStatus")
	List<NcccEmsMgm> findAll(Specification<NcccEmsMgm> spec);

	boolean existsByWealthNoAndNcccWealthNo(String wealthNo, String ncccWealthNo);

	boolean existsByWealthNoAndNcccWealthNoAndStatus(String wealthNo, String ncccWealthNo, String status);

	@Modifying
	@Query("UPDATE NcccEmsMgm e SET e.status = '3',e.postingDate = :postingDate ,e.docNo = :docNo,e.description = :description WHERE e.wealthNo = :wealthNo AND e.ncccWealthNo = :ncccWealthNo")
	int updateStatusToScrap(@Param("wealthNo") String wealthNo, @Param("ncccWealthNo") String ncccWealthNo,
			@Param("postingDate") LocalDate postingDate, @Param("docNo") String docNo,
			@Param("description") String description);

	@Modifying
	@Transactional
	@Query("UPDATE NcccEmsMgm n SET n.status = :status, n.postingDate = :postingDate ,n.docNo = :docNo,n.description = :description,n.listed = :listed,n.impairmentDate = :impairmentDate , n.updateDate = CURRENT_DATE , n.updateUser = :updateUser WHERE n.wealthNo = :wealthNo")
	int updateStatusByWealthNo(@Param("wealthNo") String wealthNo, @Param("status") String status,
			@Param("postingDate") LocalDate postingDate, @Param("docNo") String docNo,
			@Param("description") String description, @Param("listed") String listed,
			@Param("impairmentDate") LocalDate impairmentDate, @Param("updateUser") String updateUser);

	@Modifying
	@Query("UPDATE NcccEmsMgm e SET e.listed = 'Y' WHERE e.wealthNo = :wealthNo AND e.ncccWealthNo = :ncccWealthNo")
	int updateListed(@Param("wealthNo") String wealthNo, @Param("ncccWealthNo") String ncccWealthNo);

	@Modifying
	@Transactional
	@Query("UPDATE NcccEmsMgm e SET e.impairmentDate = :impairmentDate WHERE e.wealthNo = :wealthNo AND e.ncccWealthNo = :ncccWealthNo")
	int updateImpairmentDate(@Param("impairmentDate") LocalDate impairmentDate, @Param("wealthNo") String wealthNo,
			@Param("ncccWealthNo") String ncccWealthNo);
}
