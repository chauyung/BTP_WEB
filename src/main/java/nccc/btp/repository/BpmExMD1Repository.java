package nccc.btp.repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.BpmExMD1;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BpmExMD1Repository extends JpaRepository<BpmExMD1, Long> {

    List<BpmExMD1> findByExNo(String exNo);

    int deleteByExNo(String exNo);

    @Modifying
    @Query("UPDATE BpmExMD1 b SET b.itemText = :itemText,b.remark = :remark WHERE b.exNo = :exNo  AND b.exItemNo = :exItemNo")
    int updateFields(@Param("exNo") String exNo, @Param("exItemNo") String exItemNo,  @Param("itemText") String itemText, @Param("remark") String remark);

    @Query("select distinct d.certificateCode from BpmExMD1 d join BpmExM m on m.exNo = d.exNo where d.certificateCode in :codes and m.flowStatus <> '3' ")
    List<String> findConflictedCertificateCodes(@Param("codes") Collection<String> codes);

    @Query("select distinct d.certificateCode from BpmExMD1 d join BpmExM m on m.exNo = d.exNo where d.certificateCode in :codes and m.flowStatus <> '3' and m.exNo = :exNo ")
    List<String> findConflictedCertificateCodesWithoutExNo(@Param("exNo") String exNo,@Param("codes") Collection<String> codes);

    interface NonMultiRow {
        String getExNo();
        String getId();          // 若為 NUMBER，對應 BigDecimal；若為字串請改 String
        Integer getYear();
        String getOuCode();
        String getAccounting();
        BigDecimal getUnTaxAmount();
    }

    @Query(value =
            "SELECT d.EX_NO       AS exNo, " +
                    "       d.EX_ITEM_NO         AS id, " +
                    "       TO_NUMBER(TO_CHAR(m.POSTING_DATE,'YYYY')) AS year, " +
                    "       TRIM(d.COST_CENTER) AS ouCode, " +
                    "       TRIM(d.ACCOUNTING)  AS accounting, " +
                    "       d.UNTAX_AMOUNT      AS unTaxAmount " +
                    "  FROM BPM_EX_M m " +
                    "  JOIN BPM_EX_M_D1 d ON d.EX_NO = m.EX_NO " +
                    " WHERE NVL(TRIM(m.FLOW_STATUS),'') = '2' " +
                    "   AND TO_CHAR(m.POSTING_DATE,'YYYYMM') = :yymm " +
                    "   AND SUBSTR(NVL(d.ACCOUNTING,''),1,2) = '51' " +
                    "   AND NVL(TRIM(d.MULTI_SHARE),'N') <> 'Y' " +
                    "   AND NVL(d.UNTAX_AMOUNT,0) <> 0",
            nativeQuery = true)
    List<NonMultiRow> findNonMultiRows(@Param("yymm") String yymm);
}
