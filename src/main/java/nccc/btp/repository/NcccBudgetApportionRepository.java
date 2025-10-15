package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.NcccBudgetApportion;

/**
 * 預算編列-預算分攤展算表(Repository)
 * ------------------------------------------------------
 * 建立人員: ChauYung(Team)
 * 建立日期: 2025-10-08
 */
public interface NcccBudgetApportionRepository extends JpaRepository<NcccBudgetApportion, NcccBudgetApportion.ConfigId>{
	
	/**
	 * 取得 預算編列(未核覆)資料量
	 * @param year - 預算年度
	 * @param version - 版次
	 * @return - 預算編列(未核覆)資料量
	 */
	@Query(value = ""
			+ "SELECT COUNT(1) \n"
			+ "  FROM NCCC_PRE_BUDGET_M \n"
			+ " WHERE 1 = 1 \n"
			+ "   AND YEAR = :year \n"
			+ "   AND VERSION = :version \n"
			+ "   AND ASSIGNMENT <> 2", 
			nativeQuery = true)
	int checkApproval(@Param("year") String year, @Param("version") String version);
	
	/**
	 * 移除 指定條件(預算年度, 版次)資料
	 * @param year - 預算年度
	 * @param version - 版次
	 */
	@Modifying
    @Query(value = ""
    		    + "DELETE FROM NCCC_BUDGET_APPORTION \n"
    			+ " WHERE 1 = 1 \n"
    			+ "   AND YYYY = :year \n"
    			+ "   AND VERSION = :version \n",
			nativeQuery = true)
	void deleteBy(@Param("year") String year, @Param("version") String version);
	
	/**
	 * 取得 指定條件(預算年度, 版次)資料量
	 * @param year - 預算年度
	 * @param version - 版次
	 * @return - 指定條件(預算年度, 版次)資料量
	 */
	@Query(value = ""
			+ "SELECT COUNT(1) \n"
			+ "  FROM NCCC_BUDGET_APPORTION \n"
			+ " WHERE 1 = 1 \n"
			+ "   AND YYYY = :year \n"
			+ "   AND VERSION = :version \n",
			nativeQuery = true)
	int fetchCount(@Param("year") String year, @Param("version") String version);
		
	/**
	 * 執行 資料備妥(分攤前)
	 * @param year - 預算年度
	 * @param version - 版次
	 * @param userId - 操作人員代碼
	 */
	@Modifying
	@Query(value = ""
			+ " INSERT INTO NCCC_BUDGET_APPORTION( \n"
			+ "     YYYY, VERSION, APPROATION, OUCODE, OUNAME, ACCOUNTING, SUBJECT, OPERATE_ITEM_CODE, OPERATE_ITEM, AMOUNT, \n"
			+ "     CREATE_USER, CREATE_DATE, UPDATE_USER, UPDATE_DATE \n"
			+ " ) SELECT SCP.YYYY, SCP.VERSION, 'BEFORE' AS APPROATION, \n"
			+ "          SCP.OUCODE, SCP.OUNAME, SCP.ACCOUNTING, SCP.SUBJECT, \n"
			+ "          SCP.OPERATE_ITEM_CODE, SCP.OPERATE_ITEM, SCP.AMOUNT, \n"
			+ "          :userId AS CREATE_USER, \n"
			+ "          CURRENT_TIMESTAMP AS CREATE_DATE, \n"
			+ "          :userId AS UPDATE_USER, \n"
			+ "          CURRENT_TIMESTAMP AS UPDATE_DATE \n"
			+ "     FROM (SELECT T1.YEAR               AS YYYY,  \n"
			+ "                  T1.VERSION            AS VERSION,  \n"
			+ "                  T1.OUCODE             AS OUCODE,  \n"
			+ "                  X1.OU_NAME            AS OUNAME, \n"
			+ "                  T2.ACCOUNTING         AS ACCOUNTING, \n"
			+ "                  X2.ESSAY              AS SUBJECT, \n"
			+ "                  T3.OPERATE_ITEM_CODE  AS OPERATE_ITEM_CODE, \n"
			+ "                  X3.OPERATE_ITEM       AS OPERATE_ITEM, \n"
			+ "                  SUM(T3.OPERATE_AMT)   AS AMOUNT \n"
			+ "             FROM NCCC_PRE_BUDGET_M T1 \n"
			+ "                  INNER JOIN NCCC_PRE_BUDGET_D T2 \n"
			+ "                          ON T2.BUDGET_NO = T1.BUDGET_NO \n"
			+ "                  INNER JOIN NCCC_PRE_BUDGET_D1 T3 \n"
			+ "                          ON T3.BUDGET_NO = T2.BUDGET_NO \n"
			+ "                         AND T3.SEQ_NO = T2.SEQ_NO \n"
			+ "                  LEFT OUTER JOIN SYNC_OU X1 \n"
			+ "                          ON X1.OU_CODE = T1.OUCODE \n"
			+ "                  LEFT OUTER JOIN NCCC_ACCOUNTING_LIST X2 \n"
			+ "                          ON X2.SUBJECT = T2.ACCOUNTING \n"
			+ "                  LEFT OUTER JOIN NCCC_OPERATE_ITEMS X3 \n"
			+ "                          ON X3.OPERATE_ITEM_CODE = T3.OPERATE_ITEM_CODE \n"
			+ "            WHERE 1 = 1 \n"
			+ "              AND T1.YEAR = :year \n"
			+ "              AND T1.VERSION = :version \n"
			+ "            GROUP BY T1.YEAR, T1.VERSION, T1.OUCODE, X1.OU_NAME, \n"
			+ "                     T2.ACCOUNTING, X2.ESSAY, T3.OPERATE_ITEM_CODE, X3.OPERATE_ITEM) SCP \n"
			+ "    WHERE 1 = 1 \n"
			, nativeQuery = true)
	void checkExecut01(@Param("year") String year, @Param("version") String version, @Param("userId") String userId);
	
	/**
	 * 執行 預算攤分(分攤後.無需分攤)
	 * @param year - 預算年度
	 * @param version - 版次
	 */
	@Modifying
	@Query(value = ""
			+ " INSERT INTO NCCC_BUDGET_APPORTION( \n"
			+ "     YYYY, VERSION, APPROATION, OUCODE, OUNAME, ACCOUNTING, SUBJECT, OPERATE_ITEM_CODE, OPERATE_ITEM, AMOUNT, \n"
			+ "     REMARK, CREATE_USER, CREATE_DATE, UPDATE_USER, UPDATE_DATE \n"
			+ " ) SELECT T1.YYYY               AS YYYY, \n"
			+ "          T1.VERSION            AS VERSION, \n"
			+ "          (CASE WHEN T2.BELONG_OU_CODE IS NULL \n"
			+ "                THEN 'AFTER' \n"
			+ "                ELSE 'DEP' \n"
			+ "            END)                AS APPROATION, \n"
			+ "          T1.OUCODE             AS OUCODE, \n"
			+ "          T1.OUNAME             AS OUNAME, \n"
			+ "          T1.ACCOUNTING         AS ACCOUNTING, \n"
			+ "          T1.SUBJECT            AS SUBJECT, \n"
			+ "          T1.OPERATE_ITEM_CODE  AS OPERATE_ITEM_CODE, \n"
			+ "          T1.OPERATE_ITEM       AS OPERATE_ITEM, \n"
			+ "          T1.AMOUNT             AS AMOUNT, \n"
			+ "          (CASE WHEN T2.BELONG_OU_CODE IS NULL \n"
			+ "                THEN '無分攤' \n"
			+ "                ELSE '需分攤' \n"
			+ "            END)                AS REMARK, \n"
			+ "          T1.CREATE_USER        AS CREATE_USER, \n"
			+ "          T1.CREATE_DATE        AS CREATE_DATE, \n"
			+ "          T1.UPDATE_USER        AS UPDATE_USER, \n"
			+ "          T1.UPDATE_DATE        AS UPDATE_DATE \n"
			+ "     FROM NCCC_BUDGET_APPORTION T1 \n"
			+ "          LEFT OUTER JOIN NCCC_APPORTIONMENT_RULE_M T2 \n"
			+ "                  ON T2.YEAR = T1.YYYY \n"
			+ "                 AND T2.ACCOUNTING = T1.ACCOUNTING \n"
			+ "                 AND T2.MONTH = '00' \n"
			+ "    WHERE 1 = 1 \n"
			+ "      AND T1.YYYY = :year \n"
			+ "      AND T1.VERSION = :version \n"
			, nativeQuery = true)
	void checkExecut02(@Param("year") String year, @Param("version") String version);
	
	/**
	 * 執行 預算攤分(分攤後.部門分攤_依表4拾5入到仟位)
	 * @param year - 預算年度
	 * @param version - 版次
	 */
	@Modifying
	@Query(value = ""
			+ " INSERT INTO NCCC_BUDGET_APPORTION( \n"
			+ "     YYYY, VERSION, APPROATION, OUCODE, OUNAME, ACCOUNTING, SUBJECT, OPERATE_ITEM_CODE, OPERATE_ITEM, AMOUNT, \n"
			+ "     REMARK, CREATE_USER, CREATE_DATE, UPDATE_USER, UPDATE_DATE \n"
            + " ) SELECT T1.YYYY                                             AS YYYY, \n"
            + "          T1.VERSION                                          AS VERSION, \n"
            + "          'DEP_SPLIT'                                         AS APPROATION, \n"
            + "          T3.OU_CODE                                          AS OUCODE, \n"
            + "          T3.OU_NAME                                          AS OUNAME, \n"
            + "          T3.ACCOUNTING                                       AS ACCOUNTING, \n"
            + "          T2.SUBJECT                                          AS SUBJECT, \n"
            + "          T1.OPERATE_ITEM_CODE                                AS OPERATE_ITEM_CODE, \n"
            + "          T1.OPERATE_ITEM                                     AS OPERATE_ITEM, \n"
            + "          ROUND(T1.AMOUNT * (T3.UNIT_QTY / T4.GROUP_QTY), -4) AS AMOUNT, \n"
            + "          '有分攤,依部門'                                        AS REMARK, \n"
            + "          T1.CREATE_USER                                      AS CREATE_USER, \n"
            + "          T1.CREATE_DATE                                      AS CREATE_DATE, \n"
            + "          T1.UPDATE_USER                                      AS UPDATE_USER, \n"
            + "          T1.UPDATE_DATE                                      AS UPDATE_DATE  \n"
            + "     FROM NCCC_BUDGET_APPORTION T1 \n"
            + "          INNER JOIN NCCC_APPORTIONMENT_RULE_M T2 \n"
            + "                  ON T2.YEAR = T1.YYYY \n"
            + "                 AND T2.ACCOUNTING = T1.ACCOUNTING \n"
            + "          INNER JOIN NCCC_APPORTIONMENT_RULE_D T3 \n"
            + "                  ON T3.YEAR = T2.YEAR \n"
            + "                 AND T3.ACCOUNTING = T2.ACCOUNTING \n"
            + "                 AND T3.MONTH = T2.MONTH \n"
            + "          INNER JOIN ( SELECT X1.ACCOUNTING, \n"
            + "                              X1.BELONG_OU_CODE, \n"
            + "                              SUM(X2.UNIT_QTY) AS GROUP_QTY \n"
            + "                         FROM NCCC_APPORTIONMENT_RULE_M X1 \n"
            + "                              INNER JOIN NCCC_APPORTIONMENT_RULE_D X2 \n"
            + "                                      ON X2.YEAR = X1.YEAR \n"
            + "                                     AND X2.ACCOUNTING = X1.ACCOUNTING \n"
            + "                                     AND X2.MONTH = X1.MONTH \n"
            + "                        WHERE 1 = 1 \n"
            + "                          AND X1.YEAR = :year \n"
            + "                          AND X1.MONTH = '00' \n"
            + "                        GROUP BY X1.ACCOUNTING, X1.BELONG_OU_CODE ) T4 \n"
            + "                  ON T4.ACCOUNTING = T3.ACCOUNTING \n"
            + "                 AND T4.BELONG_OU_CODE = T2.BELONG_OU_CODE \n"
            + "    WHERE 1 = 1  \n"
            + "      AND T1.YYYY = :year  \n"
            + "      AND T1.VERSION = :version  \n"
            + "      AND T1.APPROATION = 'DEP' \n"
            + "      AND T2.MONTH = '00' \n"
			, nativeQuery = true)
	void checkExecut03(@Param("year") String year, @Param("version") String version);
	
	/**
	 * 執行 預算攤分(分攤後.移除過渡資料)
	 * @param year - 預算年度
	 * @param version - 版次
	 * @param approation - 資料類別
	 */
	@Modifying
	@Query(value = ""
			+ "DELETE FROM NCCC_BUDGET_APPORTION \n"
			+ " WHERE 1 = 1 \n"
			+ "   AND T1.YYYY = :year \n"
			+ "   AND T1.VERSION = :version \n"
			+ "   AND T1.APPROATION = :approation \n"
			, nativeQuery = true)
	void checkExecut00(@Param("year") String year, @Param("version") String version, @Param("approation") String approation);
	
	/**
	 * 執行 預算攤分(分攤後.部門分攤_總數差值列計於最大筆中)
	 * @param year - 預算年度
	 * @param version - 版次
	 * @param approation - 分攤前:BEFORE/分攤後:AFTER 註記
	 */
	@Modifying
	@Query(value = ""
            + "BEGIN \n"
            + "    FOR C IN ( \n"
            + "        SELECT TRG.UPDATE_RID            AS UPDATE_RID, \n"
            + "               TRG.AMOUNT + SCP.DIFF_AMT AS FINAL_AMOUNT \n"
            + "          FROM ( SELECT T1.YYYY                   AS YYYY, \n"
            + "                        T1.VERSION                AS VERSION, \n"
            + "                        T1.ACCOUNTING             AS ACCOUNTING, \n"
            + "                        T2.AMOUNT - T1.TOTAL_AMT  AS DIFF_AMT \n"
            + "                   FROM ( SELECT X1.YYYY        AS YYYY, \n"
            + "                                 X1.VERSION     AS VERSION, \n"
            + "                                 X1.ACCOUNTING  AS ACCOUNTING, \n"
            + "                                 SUM(X1.AMOUNT) AS TOTAL_AMT \n"
            + "                            FROM NCCC_BUDGET_APPORTION X1 \n"
            + "                           WHERE 1 = 1 \n"
            + "                             AND X1.YYYY = :year \n"
            + "                             AND X1.VERSION = :version \n"
            + "                             AND X1.APPROATION = 'DEP_SPLIT' \n"
            + "                           GROUP BY X1.ACCOUNTING ) T1 \n"
            + "                        INNER JOIN NCCC_BUDGET_APPORTION T2 \n"
            + "                                ON T2.YYYY = T1.YYYY \n"
            + "                               AND T2.VERSION = T1.VERSION \n"
            + "                               AND T2.ACCOUNTING = T1.ACCOUNTING \n"
            + "                    AND T2.APPROATION = 'BEFORE' \n"
            + "                    AND T2.AMOUNT <> T1.TOTAL_AMT ) SCP \n"
            + "              INNER JOIN ( SELECT X1.YYYY                              AS YYYY, \n"
            + "                                  X1.VERSION                           AS VERSION, \n"
            + "                                  X1.ACCOUNTING                        AS ACCOUNTING, \n"
            + "                                  X1.AMOUNT                            AS AMOUNT, \n"
            + "                                  X1.ROWID                             AS UPDATE_RID, \n"
            + "                                  ROW_NUMBER() OVER \n"
            + "                                         (PARTITION BY X1.ACCOUNTING \n"
            + "                                              ORDER BY X1.AMOUNT DESC) AS SN  \n"
            + "                             FROM NCCC_BUDGET_APPORTION X1 \n"
            + "                            WHERE 1 = 1 \n"
            + "                              AND X1.YYYY = :year \n"
            + "                              AND X1.VERSION = :version \n"
            + "                              AND X1.APPROATION = 'DEP_SPLIT' ) TRG \n"
            + "                      ON TRG.YYYY = SCP.YYYY \n"
            + "                     AND TRG.VERSION = SCP.VERSION \n"
            + "                     AND TRG.ACCOUNTING = SCP.ACCOUNTING \n"
            + "        WHERE 1 = 1 \n"
            + "          AND TRG.SN = 1 \n"
            + "    ) LOOP \n"
            + "        UPDATE NCCC_BUDGET_APPORTION \n"
            + "           SET AMOUNT = C.FINAL_AMOUNT \n"
            + "         WHERE 1 = 1 \n"
            + "           AND ROWID = C.UPDATE_RID; \n"
            + "    END LOOP; \n"
            + "     \n"
            + "    UPDATE NCCC_BUDGET_APPORTION \n"
            + "       SET APPROATION = 'DEP_FINAL' \n"
            + "     WHERE 1 = 1 \n"
            + "       AND YYYY = :year \n"
            + "       AND VERSION = :version \n"
            + "       AND APPROATION = 'DEP_SPLIT'; \n"
            + "END; \n"
			, nativeQuery = true)
	void checkExecut05(@Param("year") String year, @Param("version") String version);
	
	/**
	 * 執行 預算攤分(分攤後.項目分攤_人事費_依表4拾5入到仟位)
	 * @param year - 預算年度
	 * @param version - 版次
	 */
	@Modifying
	@Query(value = ""
            + "INSERT INTO NCCC_BUDGET_APPORTION( \n"
            + "    YYYY, VERSION, APPROATION, OUCODE, OUNAME, ACCOUNTING, SUBJECT, OPERATE_ITEM_CODE, OPERATE_ITEM, AMOUNT, \n"
            + "    REMARK, CREATE_USER, CREATE_DATE, UPDATE_USER, UPDATE_DATE \n"
            + ") SELECT T1.YYYY                                             AS YYYY, \n"
            + "         T1.VERSION                                          AS VERSION, \n"
            + "         'AFTER'                                             AS APPROATION, \n"
            + "         T3.OU_CODE                                          AS OUCODE, \n"
            + "         T3.OU_NAME                                          AS OUNAME, \n"
            + "         T3.ACCOUNTING                                       AS ACCOUNTING, \n"
            + "         T2.SUBJECT                                          AS SUBJECT, \n"
            + "         T4.OPERATE_ITEM_CODE                                AS OPERATE_ITEM_CODE, \n"
            + "         T4.OPERATE_ITEM                                     AS OPERATE_ITEM, \n"
            + "         (CASE WHEN T2.OPERATION_TYPE = 1 THEN  \n"
            + "                   ROUND(T1.AMOUNT * T4.OPERATE_QTY_RATIO, -4) \n"
            + "               WHEN T2.OPERATION_TYPE = 2 THEN \n"
            + "                   ROUND(T1.AMOUNT * T4.OPERATE_AMT_RATIO, -4) \n"
            + "           END)                                              AS AMOUNT, \n"
            + "         (CASE WHEN T2.OPERATION_TYPE = 1 THEN  \n"
            + "                   '有分攤,人事費.用人數' \n"
            + "               WHEN T2.OPERATION_TYPE = 2 THEN \n"
            + "                   '有分攤,人事費.用金額' \n"
            + "           END)                                              AS REMARK, \n"
            + "         T1.CREATE_USER                                      AS CREATE_USER, \n"
            + "         T1.CREATE_DATE                                      AS CREATE_DATE, \n"
            + "         T1.UPDATE_USER                                      AS UPDATE_USER, \n"
            + "         T1.UPDATE_DATE                                      AS UPDATE_DATE  \n"
            + "    FROM NCCC_BUDGET_APPORTION T1 \n"
            + "         INNER JOIN NCCC_APPORTIONMENT_RULE_M T2 \n"
            + "                 ON T2.YEAR = T1.YYYY \n"
            + "                AND T2.ACCOUNTING = T1.ACCOUNTING \n"
            + "         INNER JOIN NCCC_APPORTIONMENT_RULE_D T3 \n"
            + "                 ON T3.YEAR = T2.YEAR \n"
            + "                AND T3.ACCOUNTING = T2.ACCOUNTING \n"
            + "                AND T3.MONTH = T2.MONTH \n"
            + "         INNER JOIN NCCC_OUCODE_ACCOUNTING_OPERATE_D T4 \n"
            + "                 ON T4.YEAR = T3.YEAR \n"
            + "                AND T4.ACCOUNTING = T3.ACCOUNTING \n"
            + "                AND T4.MONTH = T3.MONTH \n"
            + "                AND T4.OUCODE = T3.OU_CODE \n"
            + "   WHERE 1 = 1  \n"
            + "     AND T1.YYYY = :year  \n"
            + "     AND T1.VERSION = :version  \n"
            + "     AND T1.APPROATION = 'DEP_FINAL' \n"
            + "     AND T2.MONTH = '00' \n"
            + "     AND INSTR(T2.ACCOUNTING, '5101') = 1 \n"
			, nativeQuery = true)
	void checkExecut06(@Param("year") String year, @Param("version") String version);
	
	/**
	 * 執行 預算攤分(分攤後.項目分攤_業務費_依表4拾5入到仟位)
	 * @param year - 預算年度
	 * @param version - 版次
	 */
	@Modifying
	@Query(value = ""
            + "INSERT INTO NCCC_BUDGET_APPORTION( \n"
            + "    YYYY, VERSION, APPROATION, OUCODE, OUNAME, ACCOUNTING, SUBJECT, OPERATE_ITEM_CODE, OPERATE_ITEM, AMOUNT, \n"
            + "    REMARK, CREATE_USER, CREATE_DATE, UPDATE_USER, UPDATE_DATE \n"
            + ") SELECT T1.YYYY                                             AS YYYY, \n"
            + "         T1.VERSION                                          AS VERSION, \n"
            + "         'AFTER'                                             AS APPROATION, \n"
            + "         T3.OUCODE                                           AS OUCODE, \n"
            + "         X1.OU_NAME                                          AS OUNAME, \n"
            + "         T3.ACCOUNTING                                       AS ACCOUNTING, \n"
            + "         T2.SUBJECT                                          AS SUBJECT, \n"
            + "         T4.OPERATE_ITEM_CODE                                AS OPERATE_ITEM_CODE, \n"
            + "         T4.OPERATE_ITEM                                     AS OPERATE_ITEM, \n"
            + "         ROUND(T1.AMOUNT * T4.OPERATE_RATIO, -4)             AS AMOUNT, \n"
            + "         '有分攤,業務費.用金額'                                 AS REMARK, \n"
            + "         T1.CREATE_USER                                      AS CREATE_USER, \n"
            + "         T1.CREATE_DATE                                      AS CREATE_DATE, \n"
            + "         T1.UPDATE_USER                                      AS UPDATE_USER, \n"
            + "         T1.UPDATE_DATE                                      AS UPDATE_DATE  \n"
            + "    FROM NCCC_BUDGET_APPORTION T1 \n"
            + "         INNER JOIN NCCC_APPORTIONMENT_RULE_M T2 \n"
            + "                 ON T2.YEAR = T1.YYYY \n"
            + "                AND T2.ACCOUNTING = T1.ACCOUNTING \n"
            + "         INNER JOIN NCCC_PRE_BUDGET_D T3 \n"
            + "                 ON T3.BUDGET_NO = T2.BUDGET_NO \n"
            + "                AND T3.ACCOUNTING = T2.ACCOUNTING \n"
            + "         INNER JOIN NCCC_PRE_BUDGET_D1 T4 \n"
            + "                 ON T4.BUDGET_NO = T3.BUDGET_NO \n"
            + "                AND T4.ACCOUNTING = T3.ACCOUNTING \n"
            + "                AND T4.SEQ_NO = T3.SEQ_NO \n"
            + "         LEFT OUTER JOIN SYNC_OU X1 \n"
            + "                 ON X1.OUCODE = T3.OUCODE \n"
            + "   WHERE 1 = 1  \n"
            + "     AND T1.YYYY = :year  \n"
            + "     AND T1.VERSION = :version  \n"
            + "     AND T1.APPROATION = 'DEP_FINAL' \n"
            + "     AND T2.MONTH = '00' \n"
            + "     AND INSTR(T2.ACCOUNTING, '5101') <> 1 \n"
			, nativeQuery = true)
	void checkExecut07(@Param("year") String year, @Param("version") String version);
	
	/**
	 * 執行 預算攤分(分攤後.移除過渡資料)
	 * @param year - 預算年度
	 * @param version - 版次
	 */
	@Modifying
	@Query(value = ""
			+ "DELETE FROM NCCC_BUDGET_APPORTION \n"
			+ " WHERE 1 = 1 \n"
			+ "   AND T1.YYYY = :year \n"
			+ "   AND T1.VERSION = :version \n"
			+ "   AND T1.APPROATION = 'DEP_FINAL' \n"
			, nativeQuery = true)
	void checkExecut08(@Param("year") String year, @Param("version") String version);
	
	/**
	 * 執行 預算攤分(分攤後.項目分攤_總數差值列計於最大筆中)
	 * @param year - 預算年度
	 * @param version - 版次
	 */
	@Modifying
	@Query(value = ""
            + "BEGIN \n"
            + "    FOR C IN ( \n"
            + "        SELECT TRG.UPDATE_RID            AS UPDATE_RID, \n"
            + "               TRG.AMOUNT + SCP.DIFF_AMT AS FINAL_AMOUNT \n"
            + "          FROM ( SELECT T1.YYYY                   AS YYYY, \n"
            + "                        T1.VERSION                AS VERSION, \n"
            + "                        T1.ACCOUNTING             AS ACCOUNTING, \n"
            + "                        T2.AMOUNT - T1.TOTAL_AMT  AS DIFF_AMT \n"
            + "                   FROM ( SELECT X1.YYYY        AS YYYY, \n"
            + "                                 X1.VERSION     AS VERSION, \n"
            + "                                 X1.ACCOUNTING  AS ACCOUNTING, \n"
            + "                                 SUM(X1.AMOUNT) AS TOTAL_AMT \n"
            + "                            FROM NCCC_BUDGET_APPORTION X1 \n"
            + "                           WHERE 1 = 1 \n"
            + "                             AND X1.YYYY = :year \n"
            + "                             AND X1.VERSION = :version \n"
            + "                             AND X1.APPROATION = 'AFTER' \n"
            + "                           GROUP BY X1.ACCOUNTING ) T1 \n"
            + "                        INNER JOIN NCCC_BUDGET_APPORTION T2 \n"
            + "                                ON T2.YYYY = T1.YYYY \n"
            + "                               AND T2.VERSION = T1.VERSION \n"
            + "                               AND T2.ACCOUNTING = T1.ACCOUNTING \n"
            + "                    AND T2.APPROATION = 'BEFORE' \n"
            + "                    AND T2.AMOUNT <> T1.TOTAL_AMT ) SCP \n"
            + "              INNER JOIN ( SELECT X1.YYYY                              AS YYYY, \n"
            + "                                  X1.VERSION                           AS VERSION, \n"
            + "                                  X1.ACCOUNTING                        AS ACCOUNTING, \n"
            + "                                  X1.AMOUNT                            AS AMOUNT, \n"
            + "                                  X1.ROWID                             AS UPDATE_RID, \n"
            + "                                  ROW_NUMBER() OVER \n"
            + "                                         (PARTITION BY X1.ACCOUNTING \n"
            + "                                              ORDER BY X1.AMOUNT DESC) AS SN \n"
            + "                             FROM NCCC_BUDGET_APPORTION X1 \n"
            + "                            WHERE 1 = 1 \n"
            + "                              AND X1.YYYY = :year \n"
            + "                              AND X1.VERSION = :version \n"
            + "                              AND X1.APPROATION = 'AFTER' ) TRG \n"
            + "                      ON TRG.YYYY = SCP.YYYY \n"
            + "                     AND TRG.VERSION = SCP.VERSION \n"
            + "                     AND TRG.ACCOUNTING = SCP.ACCOUNTING \n"
            + "        WHERE 1 = 1 \n"
            + "          AND TRG.SN = 1 \n"
            + "    ) LOOP \n"
            + "        UPDATE NCCC_BUDGET_APPORTION \n"
            + "           SET AMOUNT = C.FINAL_AMOUNT \n"
            + "         WHERE 1 = 1 \n"
            + "           AND ROWID = C.UPDATE_RID; \n"
            + "    END LOOP; \n"
            + "END; \n"
			, nativeQuery = true)
	void checkExecut09(@Param("year") String year, @Param("version") String versionn);
	
	/**
	 * 執行 預算攤分(調整備註)
	 * @param year - 預算年度
	 * @param version - 版次
	 */
	@Modifying
	@Query(value = ""
            + "BEGIN \n"
            + "    FOR C IN ( \n"
            + "        SELECT SCP.UPD_RID, SCP.REMARK, SCP.SN \n"
            + "          FROM (SELECT T1.ROWID                             AS UPD_RID, \n"
            + "                       T2.REMARK                            AS REMARK, \n"
            + "                       ROW_NUMBER() OVER  \n"
            + "                              (PARTITION BY T1.ROWID \n"
            + "                                   ORDER BY T2.AMOUNT DESC) AS SN \n"
            + "                  FROM NCCC_BUDGET_APPORTION T1 \n"
            + "                       INNER JOIN NCCC_BUDGET_APPORTION T2 \n"
            + "                               ON T2.YYYY = T1.YYYY \n"
            + "                              AND T2.VERSION = T1.VERSION \n"
            + "                              AND T2.ACCOUNTING = T1.ACCOUNTING \n"
            + "                 WHERE 1 = 1 \n"
            + "                   AND T1.YYYY = :year \n"
            + "                   AND T1.VERSION = :version \n"
            + "                   AND T1.APPROATION = 'BEFORE' \n"
            + "                   AND T2.APPROATION = 'AFTER' ) SCP \n"
            + "         WHERE 1 = 1 \n"
            + "           AND SCP.SN = 1 \n"
            + "     ) LOOP \n"
            + "         UPDATE NCCC_BUDGET_APPORTION X1 \n"
            + "            SET X1.REMARK = C.REMARK \n"
            + "          WHERE 1 = 1 \n"
            + "            AND X1.ROWID = C.UPD_RID; \n"
            + "     END LOOP; \n"
            + "END; \n"
			, nativeQuery = true)
	void checkExecut10(@Param("year") String year, @Param("version") String versionn);
}
