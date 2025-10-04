package nccc.btp.repository;

import nccc.btp.entity.BpmSplitM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface BpmSplitMRepository extends JpaRepository<BpmSplitM, Long> {

    // 常用 finder
    List<BpmSplitM> findByExNo(String exNo);

    @Query("SELECT n FROM BpmSplitM n WHERE n.exNo=:exNo AND n.exItemNo=:exItemNo")
    List<BpmSplitM> findByExNoAndExItemNo(@Param("exNo") String exNo,@Param("exItemNo") String exItemNo);

    @Modifying
    @Query("DELETE FROM BpmSplitM n WHERE n.exNo=:exNo AND n.exItemNo=:exItemNo")
    void deleteByExNoAndExItemNo(@Param("exNo") String exNo,@Param("exItemNo") String exItemNo);

    @Modifying
    @Query("DELETE FROM BpmSplitM n WHERE n.exNo=:exNo")
    void deleteByExNo(@Param("exNo") String exNo);

    // 若需要依科目/金額過濾
    //List<BpmSplitM> findByExNoAndAccountingStartingWithAndUnTaxAmountNot(String exNo, String accountingPrefix,BigDecimal unTaxAmount);

    interface SplitBatchRow {
        String getExNo();

        String getMId();

        Integer getYear();

        String getOuCode();

        String getAccounting();

        BigDecimal getUnTaxAmount();

        String getAllocationMethod();
    }

    // 批次作業用的 native 查詢（修正成 s.ID_M 連 d.ID）
    @Query(value = "SELECT " + "  s.EX_NO              AS exNo, " + "  s.EX_ITEM_NO               AS mId, "
            + "  TO_NUMBER(TO_CHAR(m.POSTING_DATE,'YYYY')) AS year, "
            + "  TRIM(NVL(s.OUCODE, d.COST_CENTER))        AS ouCode, " + "  s.ACCOUNTING         AS accounting, "
            + "  s.UNTAX_AMOUNT       AS unTaxAmount, " + "  NVL(TRIM(s.ALLOCATION_METHOD),'2') AS allocationMethod "
            + "FROM BPM_EX_M m " + "JOIN BPM_EX_M_D1 d ON d.EX_NO = m.EX_NO "
            + "JOIN BPM_SPLIT_M s ON s.EX_NO = d.EX_NO AND s.EX_ITEM_NO = d.EX_ITEM_NO " + "WHERE NVL(TRIM(m.FLOW_STATUS),'') = '2' "
            + "  AND TO_CHAR(m.POSTING_DATE,'YYYYMM') = :yyyymm " + "  AND NVL(TRIM(d.MULTI_SHARE),'N') = 'Y' "
            + "  AND SUBSTR(NVL(s.ACCOUNTING,''),1,2) = '51' " + "  AND NVL(s.UNTAX_AMOUNT,0) <> 0", nativeQuery = true)
    List<SplitBatchRow> findRowsForBatch(@Param("yyyymm") String yyyymm);

    interface SplitRow {
        String getExNo();

        String getMId();

        Integer getYear();

        String getOuCode();

        String getAccounting();

        BigDecimal getUnTaxAmount();

        String getAllocationMethod();
    }

    // 多重分攤資料（正確連結 s.ID_M = d.ID）
    @Query(value = "SELECT " + "  s.EX_NO AS exNo, " + "  s.EX_ITEM_NO  AS mId, "
            + "  TO_NUMBER(TO_CHAR(m.POSTING_DATE,'YYYY')) AS year, "
            + "  TRIM(NVL(s.OUCODE, d.COST_CENTER)) AS ouCode, " + "  s.ACCOUNTING   AS accounting, "
            + "  s.UNTAX_AMOUNT AS unTaxAmount, " + "  NVL(TRIM(s.ALLOCATION_METHOD),'2') AS allocationMethod "
            + "FROM BPM_EX_M m " + "JOIN BPM_EX_M_D1 d ON d.EX_NO = m.EX_NO "
            + "JOIN BPM_SPLIT_M s ON s.EX_NO = d.EX_NO AND s.EX_ITEM_NO = d.EX_ITEM_NO " + "WHERE NVL(TRIM(m.FLOW_STATUS),'') = '2' "
            + "  AND TO_CHAR(m.POSTING_DATE,'YYYYMM') = :yyyymm " + "  AND NVL(TRIM(d.MULTI_SHARE),'N') = 'Y' "
            + "  AND SUBSTR(NVL(s.ACCOUNTING,''),1,2) = '51' " + "  AND NVL(s.UNTAX_AMOUNT,0) <> 0", nativeQuery = true)
    List<SplitRow> findSplitRows(@Param("yyyymm") String yyyymm);

}
