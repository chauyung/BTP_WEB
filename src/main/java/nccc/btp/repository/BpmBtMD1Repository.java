package nccc.btp.repository;

import java.util.Collection;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import nccc.btp.entity.BpmBtMD1;
import nccc.btp.entity.BpmBtMD1Id;

public interface BpmBtMD1Repository extends JpaRepository<BpmBtMD1, BpmBtMD1Id> {

  List<BpmBtMD1> findByBtNo(String btNo);

  int deleteByBtNo(String btNo);

  @Modifying
  @Query("UPDATE BpmBtMD1 d SET d.costCenter = :costCenter,d.deduction = :deduction,d.remark = :remark WHERE d.btNo = :btNo AND d.btItemNo = :btItemNo")
  int updateFields(@Param("btNo") String btNo, @Param("btItemNo") String btItemNo,
      @Param("costCenter") String costCenter, @Param("deduction") String deduction,
      @Param("remark") String remark);

  @Query("select distinct d.certificateCode from BpmBtMD1 d join BpmBtM m on m.btNo = d.btNo where d.certificateCode in :codes and m.flowStatus <> '3' ")
  List<String> findConflictedCertificateCodes(@Param("codes") Collection<String> codes);
  
  interface TravelRow {
      String getBtNo();
      Integer getYear();
      String getOuCode();
      String getAccounting();
      BigDecimal getUnTaxAmount();
  }

  @Query(value =
      "SELECT m.BT_NO                AS btNo, " +
      "       d.YEAR                 AS year, " +
      "       d.OUCODE               AS ouCode, " +
      "       d.ACCOUNTING           AS accounting, " +
      "       NVL(d.UNTAX_AMOUNT, 0) AS unTaxAmount " +
      "  FROM BPM_BT_M m " +
      "  JOIN BPM_BT_M_D1 d ON d.BT_NO = m.BT_NO " +
      " WHERE m.FLOW_STATUS = '2' " +
      "   AND TO_CHAR(m.POSTING_DATE, 'YYYYMM') = :yymm",
      nativeQuery = true)
  List<TravelRow> findTravelRows(@Param("yymm") String yyyymm);
}
