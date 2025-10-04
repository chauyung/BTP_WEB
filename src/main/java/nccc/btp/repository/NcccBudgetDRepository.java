package nccc.btp.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import nccc.btp.entity.NcccBudgetD;

public interface NcccBudgetDRepository extends JpaRepository<NcccBudgetD, NcccBudgetD.ConfigId> {

  interface BudgetItem {
    String getOperateItemCode();
    String getOperateItem();
    BigDecimal getFinalTotal();     // 對應 d.OPERATE_AMT
    BigDecimal getOperateRatio();
  }

  @Query(value =
      "SELECT d.OPERATE_ITEM_CODE         AS operateItemCode, " +
      "       NVL(d.OPERATE_AMT, 0)       AS finalTotal, " +
      "       NVL(d.OPERATE_RATIO, 0)     AS operateRatio, " +
      "       oi.OPERATE_ITEM             AS operateItem " +
      "  FROM NCCC_BUDGET_D d " +
      "  LEFT JOIN NCCC_OPERATE_ITEMS oi " +
      "    ON oi.OPERATE_ITEM_CODE = d.OPERATE_ITEM_CODE " +
      " WHERE d.YEAR = :year " +
      "   AND d.VERSION = :version " +
      "   AND d.OUCODE = :oucode " +
      "   AND d.ACCOUNTING = :accounting",
      nativeQuery = true)
  List<BudgetItem> findBudgetItems(@Param("year") int year,
                                   @Param("version") String version,
                                   @Param("oucode") String oucode,
                                   @Param("accounting") String accounting);

  // 來源預算部門金額 Σ OPERATE_AMT（Oracle 空字串安全不需特判）
  @Query(value =
      "SELECT NVL(SUM(d.OPERATE_AMT), 0) " +
      "  FROM NCCC_BUDGET_D d " +
      " WHERE d.YEAR = :year " +
      "   AND d.VERSION = :version " +
      "   AND d.OUCODE = :oucode " +
      "   AND d.ACCOUNTING = :accounting",
      nativeQuery = true)
  BigDecimal sumFinalTotal(@Param("year") String year,
                           @Param("version") String version,
                           @Param("oucode") String oucode,
                           @Param("accounting") String accounting);
}
