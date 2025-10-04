package nccc.btp.repository;

import java.util.Collection;
import java.util.List;

import nccc.btp.entity.NcccBudgetM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BudgetSummaryRepository extends JpaRepository<NcccBudgetM, Long> {

    @Query(value =
        "SELECT m.OUCODE AS ou, m.ACCOUNTING AS acc8, " +
        "SUM(m.ORIGINAL_BUDGET) AS original, " +
        "SUM(m.RESERVE_BUDGET) AS reserve, " +
        "SUM(m.ALLOC_INCREASE_AMT) AS allocIn, " +
        "SUM(m.ALLOC_REDUSE_AMT) AS allocOut, " +
        "SUM(m.OCCUPY_AMT) AS occupy, " +
        "SUM(m.USE_AMT) AS useAmt, " +
        "SUM(m.CONSUME_AMT) AS consume " +
        "FROM NCCC_BUDGET_M m " +
        "WHERE m.YEAR IN (:years) " +
        "AND m.VERSION = (SELECT MAX(m2.VERSION) FROM NCCC_BUDGET_M m2 WHERE m2.YEAR = m.YEAR) " +
        "AND (:accF IS NULL OR m.ACCOUNTING >= :accF) " +
        "AND (:accT IS NULL OR m.ACCOUNTING <= :accT) " +
        "AND ((:hasOu) = 0 OR m.OUCODE IN (:ou)) " +
        "GROUP BY m.OUCODE, m.ACCOUNTING",
        nativeQuery = true)
    List<BudgetLatestAgg> aggregateLatest(@Param("years") Collection<String> years,
                                          @Param("ou") Collection<String> ou,
                                          @Param("hasOu") int hasOu,
                                          @Param("accF") String accFrom,
                                          @Param("accT") String accTo);

    @Query(value =
        "SELECT a.OUCODE AS ou, a.ACCOUNTING AS acc8, SUM(a.AMOUNT) AS posted " +
        "FROM NCCC_BUDGET_ACTUAL a " +
        "WHERE a.YYMM BETWEEN :f AND :t " +
        "AND a.APPROATION = 'BEFORE' " +
        "AND (:accF IS NULL OR a.ACCOUNTING >= :accF) " +
        "AND (:accT IS NULL OR a.ACCOUNTING <= :accT) " +
        "AND ((:hasOu) = 0 OR a.OUCODE IN (:ou)) " +
        "GROUP BY a.OUCODE, a.ACCOUNTING",
        nativeQuery = true)
    List<ActualPostedAgg> aggregatePosted(@Param("f") String from,
                                          @Param("t") String to,
                                          @Param("ou") Collection<String> ou,
                                          @Param("hasOu") int hasOu,
                                          @Param("accF") String accFrom,
                                          @Param("accT") String accTo);

    @Query("select n.accountingLevelCode, n.accountingLevelName from NcccAccountingLevel n")
    List<Object[]> loadAccNames();


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

    interface ActualPostedAgg {
        String getOu();
        String getAcc8();
        java.math.BigDecimal getPosted();
    }
}
