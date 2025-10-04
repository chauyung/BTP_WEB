package nccc.btp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import nccc.btp.entity.BpmRevMD;

/** 驗收單明細倉儲 */
public interface BpmRevMDRepository extends JpaRepository<BpmRevMD, Long>  {

    /** 依單號取明細 */
    List<BpmRevMD> findByRevNo(String revNo);

    interface AcceptanceRow {
        String getRevNo();
        String getPoItemNo();
        String getRevItemNo();
        String getOuCode();
        String getAccounting();
        java.math.BigDecimal getTotal();
        String getIsFixedAsset();
        String getFixedAssetNo();
        String getAllocationMethod();
        String getIsResponsibility();
        Integer getYear(); // or Number
      }

    // 分攤前：驗收
    @Query(value =
        "SELECT m.REV_NO                              AS revNo, " +
        "       TO_CHAR(d.PO_ITEM_NO)                 AS poItemNo, " +
        "       TO_CHAR(d.REV_ITEM_NO)                AS revItemNo, " +
        "       d.OUCODE                              AS ouCode, " +
        "       d.ACCOUNTING                          AS accounting, " +
        "       NVL(d.TOTAL,0)                        AS total, " +
        "       NVL(d.IS_FIXED_ASSET,'0')             AS isFixedAsset, " +
        "       d.FIXED_ASSET_NO                      AS fixedAssetNo, " +
        "       NVL(d.ALLOCATION_METHOD,'2')          AS allocationMethod, " +
        "       NVL(TRIM(d.IS_RESPONSIBILITY),'N')    AS isResponsibility, " +
        "       d.YEAR                                AS year " +
        "  FROM BPM_REV_M m " +
        "  JOIN BPM_REV_M_D d ON d.REV_NO = m.REV_NO " +
        " WHERE m.FLOW_STATUS = '2' " +
        "   AND TO_CHAR(m.POSTING_DATE,'YYYYMM') = :yymm " +
        "   AND NVL(TRIM(d.IS_RESPONSIBILITY),'N') <> 'Y' " +
        "   AND SUBSTR(NVL(d.ACCOUNTING,''),1,2) = '51' " +
        "   AND NVL(d.TOTAL,0) <> 0",
        nativeQuery = true)
    List<AcceptanceRow> findAcceptanceRows(@Param("yymm") String yymm);

    // 分攤後：驗收（同投影介面）
    @Query(value =
        "SELECT m.REV_NO                              AS revNo, " +
        "       TO_CHAR(d.PO_ITEM_NO)                 AS poItemNo, " +
        "       TO_CHAR(d.REV_ITEM_NO)                AS revItemNo, " +
        "       d.OUCODE                              AS ouCode, " +
        "       d.ACCOUNTING                          AS accounting, " +
        "       NVL(d.TOTAL,0)                        AS total, " +
        "       NVL(d.IS_FIXED_ASSET,'0')             AS isFixedAsset, " +
        "       d.FIXED_ASSET_NO                      AS fixedAssetNo, " +
        "       NVL(d.ALLOCATION_METHOD,'2')          AS allocationMethod, " +
        "       NVL(TRIM(d.IS_RESPONSIBILITY),'N')    AS isResponsibility, " +
        "       d.YEAR                                AS year " +
        "  FROM BPM_REV_M m " +
        "  JOIN BPM_REV_M_D d ON d.REV_NO = m.REV_NO " +
        " WHERE m.FLOW_STATUS = '2' " +
        "   AND TO_CHAR(m.POSTING_DATE,'YYYYMM') = :yymm " +
        "   AND NVL(TRIM(d.IS_RESPONSIBILITY),'N') <> 'Y' " +
        "   AND SUBSTR(NVL(d.ACCOUNTING,''),1,2) = '51' " +
        "   AND NVL(d.TOTAL,0) <> 0",
        nativeQuery = true)
    List<AcceptanceRow> findAcceptanceRowsAfter(@Param("yymm") String yymm);
}
