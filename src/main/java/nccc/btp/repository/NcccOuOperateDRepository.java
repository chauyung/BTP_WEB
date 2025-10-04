package nccc.btp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nccc.btp.entity.NcccOuOperateD;

/**
 * 5101 專用：查 NCCC_OUCODE_ACCOUNTING_OPERATE_D（含 ACCOUNTING）
 */
@Repository
public interface NcccOuOperateDRepository extends JpaRepository<NcccOuOperateD, Long> {

    interface WeightedRow {
        String getOperateItemCode();
        String getOperateItem();
        java.math.BigDecimal getWeight();
    }
    interface OperQtyRatioRow {
        String getOperateItemCode();
        String getOperateItem();
        java.math.BigDecimal getOperateQtyRatio(); // 百分比
    }
    interface OperAmtRatioRow {
        String getOperateItemCode();
        String getOperateItem();
        java.math.BigDecimal getOperateAmtRatio(); // 百分比
    }
    interface OperQtyRow extends WeightedRow {}
    interface OperAmtRow extends WeightedRow {}

    // 5101 + OPERATION_TYPE=1：比例（人數）
    @Query(value =
        "SELECT d.OPERATE_ITEM_CODE AS operateItemCode, " +
        "       NVL(oi.OPERATE_ITEM, d.OPERATE_ITEM_CODE) AS operateItem, " +
        "       NVL(d.OPERATE_QTY_RATIO, 0) AS operateQtyRatio " +
        "  FROM NCCC_OUCODE_ACCOUNTING_OPERATE_D d " +
        "  LEFT JOIN NCCC_OPERATE_ITEMS oi ON oi.OPERATE_ITEM_CODE = d.OPERATE_ITEM_CODE " +
        " WHERE d.YEAR = :yy AND d.VERSION = :ver AND d.OUCODE = :ou AND d.ACCOUNTING = :acc " +
        " ORDER BY d.OPERATE_ITEM_CODE",
        nativeQuery = true)
    List<OperQtyRatioRow> findItemsQtyRatio(@Param("yy") String year,
                                            @Param("ver") String version,
                                            @Param("ou") String oucode,
                                            @Param("acc") String accounting);

    // 5101 + OPERATION_TYPE=2：比例（金額）
    @Query(value =
        "SELECT d.OPERATE_ITEM_CODE AS operateItemCode, " +
        "       NVL(oi.OPERATE_ITEM, d.OPERATE_ITEM_CODE) AS operateItem, " +
        "       NVL(d.OPERATE_AMT_RATIO, 0) AS operateAmtRatio " +
        "  FROM NCCC_OUCODE_ACCOUNTING_OPERATE_D d " +
        "  LEFT JOIN NCCC_OPERATE_ITEMS oi ON oi.OPERATE_ITEM_CODE = d.OPERATE_ITEM_CODE " +
        " WHERE d.YEAR = :yy AND d.VERSION = :ver AND d.OUCODE = :ou AND d.ACCOUNTING = :acc " +
        " ORDER BY d.OPERATE_ITEM_CODE",
        nativeQuery = true)
    List<OperAmtRatioRow> findItemsAmtRatio(@Param("yy") String year,
                                            @Param("ver") String version,
                                            @Param("ou") String oucode,
                                            @Param("acc") String accounting);

    // 比例不存在 → 權重（人數）
    @Query(value =
        "SELECT d.OPERATE_ITEM_CODE AS operateItemCode, " +
        "       NVL(oi.OPERATE_ITEM, d.OPERATE_ITEM_CODE) AS operateItem, " +
        "       NVL(d.OPERATE_QTY, 0) AS operateQty, " +
        "       NVL(d.OPERATE_QTY, 0) AS weight " +
        "  FROM NCCC_OUCODE_ACCOUNTING_OPERATE_D d " +
        "  LEFT JOIN NCCC_OPERATE_ITEMS oi ON oi.OPERATE_ITEM_CODE = d.OPERATE_ITEM_CODE " +
        " WHERE d.YEAR = :yy AND d.VERSION = :ver AND d.OUCODE = :ou AND d.ACCOUNTING = :acc " +
        " ORDER BY d.OPERATE_ITEM_CODE",
        nativeQuery = true)
    List<OperQtyRow> findItems(@Param("yy") String year,
                               @Param("ver") String version,
                               @Param("ou") String oucode,
                               @Param("acc") String accounting);

    // 比例不存在 → 權重（金額）
    @Query(value =
        "SELECT d.OPERATE_ITEM_CODE AS operateItemCode, " +
        "       NVL(oi.OPERATE_ITEM, d.OPERATE_ITEM_CODE) AS operateItem, " +
        "       NVL(d.OPERATE_AMT, 0) AS operateAmt, " +
        "       NVL(d.OPERATE_AMT, 0) AS weight " +
        "  FROM NCCC_OUCODE_ACCOUNTING_OPERATE_D d " +
        "  LEFT JOIN NCCC_OPERATE_ITEMS oi ON oi.OPERATE_ITEM_CODE = d.OPERATE_ITEM_CODE " +
        " WHERE d.YEAR = :yy AND d.VERSION = :ver AND d.OUCODE = :ou AND d.ACCOUNTING = :acc " +
        " ORDER BY d.OPERATE_ITEM_CODE",
        nativeQuery = true)
    List<OperAmtRow> findItemsAmt(@Param("yy") String year,
                                  @Param("ver") String version,
                                  @Param("ou") String oucode,
                                  @Param("acc") String accounting);
}
