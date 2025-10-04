package nccc.btp.repository;

import java.util.Collection;
import java.util.List;

import nccc.btp.entity.NcccBudgetM;
import nccc.btp.vo.BudgetVo;
import nccc.btp.vo.BudgetVo.BudgetDetailItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.domain.Specification;

public interface NcccBudgetMRepository extends JpaRepository<NcccBudgetM, NcccBudgetM.ConfigId> {

    /**
     * 查詢組織指定年度和版本的預算項目
     * @param year 年度
     * @param version 版本
     * @param ouCode 部門
     * @return 預算主檔列表
     */
    @Query(value = "SELECT" +
            "    m.year AS YEAR," +
            "    m.version AS VERSION," +
            "    m.oucode AS OU_CODE," +
            "    ou.OU_NAME AS OU_NAME," +
            "    m.accounting AS ACC_CODE," +
            "    ac.ESSAY AS ACC_NAME," +
            "    m.ORIGINAL_BUDGET AS OriginalBudget," +
            "    m.RESERVE_BUDGET AS ReserveBudget," +
            "    m.ALLOC_INCREASE_AMT AS AllocIncreaseAmt," +
            "    m.ALLOC_REDUSE_AMT AS AllocReduseAmt," +
            "    m.OCCUPY_AMT AS OccupyAmt," +
            "    m.USE_AMT AS UseAmt," +
            "    m.CONSUME_AMT AS ConsumeAmt" +
            "    FROM nccc_budget_m m" +
            "    LEFT JOIN SYNC_OU ou ON m.oucode = ou.OU_CODE" +
            "    LEFT JOIN NCCC_ACCOUNTING_LIST ac ON m.accounting = ac.SUBJECT WHERE m.year=:year AND m.version=:version AND m.oucode=:ouCode",nativeQuery = true)
    List<BudgetDetailItem> GetItemsByYearAndVersionAndOuCode(@Param("year") String year,@Param("version") String version,@Param("ouCode") String ouCode);

    NcccBudgetM findByYearAndVersionAndOuCodeAndAccounting(@Param("year") String year,@Param("version") String version,@Param("ouCode") String ouCode,@Param("accounting") String accounting);

    /**
     * 查詢指定年度和版本的預算項目
     * @param year 年度
     * @param version 版本
     * @return 預算主檔列表
     */
    @Query(value = "SELECT" +
            "    m.year AS YEAR," +
            "    m.version AS VERSION," +
            "    m.oucode AS OU_CODE," +
            "    ou.OU_NAME AS OU_NAME," +
            "    m.accounting AS ACC_CODE," +
            "    ac.ESSAY AS ACC_NAME," +
            "    m.ORIGINAL_BUDGET AS BUD_AMOUNT" +
            "    FROM nccc_budget_m m" +
            "    LEFT JOIN SYNC_OU ou ON m.oucode = ou.OU_CODE" +
            "    LEFT JOIN NCCC_ACCOUNTING_LIST ac ON m.accounting = ac.SUBJECT WHERE year=:year AND version=:version",nativeQuery = true)
    List<BudgetVo.BudgetItem> GetItemsByYearAndVersion(@Param("year") String year,@Param("version") String version);

    /**
     * 查詢採購單
     */
    @Query(value = "SELECT "+
            "M.PO_NO AS purchaseOrderNo,"+
            "PR.PR_NO AS prNo,"+
            "M.DEPARTMENT AS DEPTCODE,"+
            "OU.OU_NAME AS DEPTNAME,"+
            "M.applicant AS applicant,"+
            "PD1.ACCOUNTING AS accountingCode,"+
            "AC.ESSAY AS accountingName,"+
            "PD1.PRICE AS PURCHASEAMOUNT,"+
            "PRD1.PRICE AS requestAmount,"+
            "PD1.REMARK AS PURCHASEPURPOSE,"+
            "M.DOC_NO AS PURCHASEDOCNO,"+
            "PR.DOC_NO AS RequestDocNo,"+
            "NVL((NVL(BM.ORIGINAL_BUDGET,0) + NVL(BM.RESERVE_BUDGET,0) + NVL(BM.ALLOC_INCREASE_AMT,0) - NVL(BM.ALLOC_REDUSE_AMT,0) - NVL(BM.OCCUPY_AMT,0) - NVL(BM.USE_AMT,0) - NVL(BM.CONSUME_AMT,0)),0) AS budgetBalance" +
            " FROM BPM_PO_M_D1 PD1"+
            " INNER JOIN BPM_PO_M M ON PD1.PO_NO = M.PO_NO"+
            " LEFT JOIN NCCC_BUDGET_M BM ON PD1.ACCOUNTING = BM.ACCOUNTING AND BM.OUCODE = PD1.OUCODE AND BM.year=:year"+
            " LEFT JOIN SYNC_OU OU ON OU.OU_CODE = PD1.OUCODE"+
            " LEFT JOIN BPM_PR_M PR ON PR.PO_NO = M.PO_NO "+
            " LEFT JOIN BPM_PR_M_D1 PRD1 ON PRD1.PR_NO = PR.PR_NO AND PRD1.ACCOUNTING = PD1.ACCOUNTING AND PRD1.OUCODE = PD1.OUCODE AND PRD1.PR_ITEM_NO = PD1.PO_ITEM_NO AND PRD1.year=:year "+
            " LEFT JOIN NCCC_ACCOUNTING_LIST AC ON AC.SUBJECT = PD1.ACCOUNTING"+
            " WHERE PD1.YEAR = :year", nativeQuery = true)
    List<BudgetVo.BudgetPurchaseOrderData> GetPurchaseOrder(@Param("year") String year);

    /**
     * 查詢預算調撥採購單
     */
    @Query(value = "SELECT "+
            "M.PO_NO AS purchaseOrderNo,"+
            "M.applicant AS applicant,"+
            "M.EMP_NO AS empNo,"+
            "M.REMARK AS purchasePurpose,"+
            "PD1.OUCODE AS deptCode,"+
            "OU.OU_NAME AS deptName,"+
            "PD1.ACCOUNTING AS accountingCode,"+
            "AC.ESSAY AS accountingName,"+
            "PD1.BP_PRICE AS purchaseAmount,"+
            "NVL((NVL(BM.ORIGINAL_BUDGET,0) + NVL(BM.RESERVE_BUDGET,0) + NVL(BM.ALLOC_INCREASE_AMT,0) - NVL(BM.ALLOC_REDUSE_AMT,0) - NVL(BM.OCCUPY_AMT,0) - NVL(BM.USE_AMT,0) - NVL(BM.CONSUME_AMT,0)),0) AS budgetBalance" +
            " FROM BPM_PO_M_D1 PD1"+
            " LEFT JOIN BPM_PO_M M ON PD1.PO_NO = M.PO_NO"+
            " LEFT JOIN NCCC_BUDGET_M BM ON PD1.ACCOUNTING = BM.ACCOUNTING AND BM.OUCODE = PD1.OUCODE AND BM.year=:year"+
            " LEFT JOIN SYNC_OU OU ON OU.OU_CODE = PD1.OUCODE"+
            " LEFT JOIN NCCC_ACCOUNTING_LIST AC ON AC.SUBJECT = PD1.ACCOUNTING"+
            " WHERE PD1.YEAR = :year", nativeQuery = true)
    List<BudgetVo.BudgetManagementPurchaseOrderIncludeDetailData> GetBudgetManagementPurchaseOrder(@Param("year") String year);

    /**
     * 查詢對應預算資料
     */
    @Query(value = "SELECT "+
            "BM.YEAR AS year,"+
            "BM.VERSION AS version,"+
            "BM.OUCODE AS deptCode,"+
            "OU.OU_NAME AS deptName,"+
            "BM.ACCOUNTING AS accountingCode,"+
            "AC.ESSAY AS accountingName,"+
            "NVL(BM.ORIGINAL_BUDGET,0) AS originalBudget,"+
            "NVL(BM.RESERVE_BUDGET,0) AS reserveBudget,"+
            "NVL(BM.ALLOC_INCREASE_AMT,0) AS allocIncreaseAmt,"+
            "NVL(BM.ALLOC_REDUSE_AMT,0) AS allocReduseAmt,"+
            "NVL(BM.OCCUPY_AMT,0) AS occupyAmt,"+
            "NVL(BM.USE_AMT,0) AS useAmt,"+
            "NVL(BM.CONSUME_AMT,0) AS consumeAmt,"+
            "NVL((NVL(BM.ORIGINAL_BUDGET,0) + NVL(BM.RESERVE_BUDGET,0) + NVL(BM.ALLOC_INCREASE_AMT,0) - NVL(BM.ALLOC_REDUSE_AMT,0)),0) AS availableBudget," +
            "NVL((NVL(BM.ORIGINAL_BUDGET,0) + NVL(BM.RESERVE_BUDGET,0) + NVL(BM.ALLOC_INCREASE_AMT,0) - NVL(BM.ALLOC_REDUSE_AMT,0) - NVL(BM.OCCUPY_AMT,0) - NVL(BM.USE_AMT,0) - NVL(BM.CONSUME_AMT,0)),0) AS budgetBalance" +
            " FROM NCCC_BUDGET_M BM"+
            " LEFT JOIN SYNC_OU OU ON OU.OU_CODE = BM.OUCODE"+
            " LEFT JOIN NCCC_ACCOUNTING_LIST AC ON AC.SUBJECT = BM.ACCOUNTING"+
            " WHERE BM.YEAR = :year AND BM.ACCOUNTING = :accounting AND BM.VERSION = 2 AND BM.OUCODE = :ouCode", nativeQuery = true)
    BudgetVo.BudgetBalanceData GetBudgetBalanceByCondition(@Param("year") String year,@Param("accounting") String accounting,@Param("ouCode") String ouCode);

    /**
     * 查詢請購單
     */
    @Query(value = "SELECT "+
            "M.PR_NO As RequestOrder,"+
            "M.APPLY_DATE AS RequestDate,"+
            "D1.ACCOUNTING AS AccountingCode,"+
            "AC.ESSAY AS AccountingName,"+
            "M.DOC_NO AS RequestDocNo,"+
            "D1.PRICE AS Amount"+
            " FROM BPM_PR_M_D1 D1"+
            " LEFT JOIN BPM_PR_M M ON D1.PR_NO = M.PR_NO"+
            " LEFT JOIN NCCC_ACCOUNTING_LIST AC ON AC.SUBJECT = D1.ACCOUNTING"+
            " WHERE D1.YEAR = :year", nativeQuery = true)
    List<BudgetVo.BudgetRequestOrderData> GetRequestOrder(@Param("year") String year);

    /**
     * 查詢指定年度和版本的預算項目
     * @param sourceYear
     * @param sourceVersion
     * @return
     */
    @Query("SELECT n FROM NcccBudgetM n WHERE n.year=:year AND n.version=:version")
    List<NcccBudgetM> findByYearAndVersion(@Param("year") String sourceYear,@Param("version") String sourceVersion);


    /**
     * 預算主檔：每年最新版的彙總（依 OU + 8 碼科目）
     */
    @Query(value =
            "SELECT m.OUCODE AS ou, m.ACCOUNTING AS acc8, " +
                    "       SUM(m.ORIGINAL_BUDGET)    AS original, " +
                    "       SUM(m.RESERVE_BUDGET)     AS reserve, " +
                    "       SUM(m.ALLOC_INCREASE_AMT) AS allocIn, " +
                    "       SUM(m.ALLOC_REDUSE_AMT)   AS allocOut, " +
                    "       SUM(m.OCCUPY_AMT)         AS occupy, " +
                    "       SUM(m.USE_AMT)            AS useAmt, " +
                    "       SUM(m.CONSUME_AMT)        AS consume " +
                    "FROM NCCC_BUDGET_M m " +
                    "WHERE m.YEAR IN (:years) " +
                    "  AND m.VERSION = (SELECT MAX(m2.VERSION) FROM NCCC_BUDGET_M m2 WHERE m2.YEAR = m.YEAR) " +
                    "  AND (:accF IS NULL OR m.ACCOUNTING >= :accF) " +
                    "  AND (:accT IS NULL OR m.ACCOUNTING <= :accT) " +
                    "  AND ((:hasOu) = 0 OR m.OUCODE IN (:ou)) " +
                    "GROUP BY m.OUCODE, m.ACCOUNTING",
            nativeQuery = true)
    List<BudgetLatestAgg> aggregateLatest(@Param("years") Collection<String> years,
                                          @Param("ou") Collection<String> ou,
                                          @Param("hasOu") int hasOu,
                                          @Param("accF") String accFrom,
                                          @Param("accT") String accTo);

    /**
     * 內嵌投影介面：對應 aggregateLatest 的欄位別名
     */
    interface BudgetLatestAgg {
        String getOu();
        String getAcc8();
        java.math.BigDecimal getOriginal();
        java.math.BigDecimal getReserve();
        java.math.BigDecimal getAllocIn();
        java.math.BigDecimal getAllocOut();
        java.math.BigDecimal getOccupy();
        java.math.BigDecimal getUseAmt();
        java.math.BigDecimal getConsume();
    }

    List<NcccBudgetM> findAll(Specification<NcccBudgetM> spec);
}
