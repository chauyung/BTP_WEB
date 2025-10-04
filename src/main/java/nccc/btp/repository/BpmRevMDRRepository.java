package nccc.btp.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import nccc.btp.entity.BpmRevMDR;

/**
 * 驗收單紀錄倉儲
 */
public interface BpmRevMDRRepository extends JpaRepository<BpmRevMDR, Long> {

  /*
   * 取得驗收單紀錄資料集
   */
  List<BpmRevMDR> findByRevNo(String RevNo);

  /*
   * 取得驗收單紀錄資料
   */
  List<BpmRevMDR> findByRevNoAndHandleIdentificationKey(String RevNo, String HandleIdentificationKey);

  interface AcceptanceRow {
      String getRevNo();
      Integer getId();
      Integer getYear();
      String getOuCode();
      String getAccounting();
      BigDecimal getTotal();
      String getIsFixedAsset();
      String getAllocationMethod();
      String getAssetsCode();
  }

  @Query(value =
      "SELECT d.REV_NO             AS revNo, " +
      "       d.ID                 AS id, " +
      "       d.YEAR               AS year, " +
      "       d.OUCODE             AS ouCode, " +
      "       d.ACCOUNTING         AS accounting, " +
      "       NVL(d.TOTAL,0)       AS total, " +
      "       NVL(d.IS_FIXED_ASSET,'0') AS isFixedAsset, " +
      "       NVL(d.ALLOCATION_METHOD,'2') AS allocationMethod, " +
      "       d.ASSETS_CODE        AS assetsCode " +
      "  FROM BPM_REV_M m " +
      "  JOIN BPM_REV_M_D d ON d.REV_NO = m.REV_NO " +
      " WHERE m.FLOW_STATUS = '2' " +
      "   AND TO_CHAR(m.POSTING_DATE,'YYYYMM') = :yymm " +
      "   AND SUBSTR(d.ACCOUNTING,1,2) = '51'",
      nativeQuery = true)
  List<AcceptanceRow> findAcceptanceRows(@Param("yymm") String yymm);
}
