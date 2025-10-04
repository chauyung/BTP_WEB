package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import nccc.btp.entity.BpmExM;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BpmExMRepository extends JpaRepository<BpmExM, String> {

    BpmExM findByExNo(String exNo);

    List<BpmExM> findAllByEmpNo(String empNo);

    List<BpmExM> findAllByPayEmpNo(String payEmpNo);

    BpmExM findByTaskId(String taskId);

    /**
     * 取當月（yyMM）最大的 4 碼流水號
     */
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(b.exNo, 7, 4) AS int)), 0) FROM BpmExM b WHERE SUBSTRING(b.exNo, 3, 4) = :prefix")
    Integer findMaxSerialByPrefix(@Param("prefix") String prefix);

    @Modifying
    @Query("UPDATE BpmExM b SET b.currencyRate = :currencyRate,b.postingDate = :postingDate,b.total = :total WHERE b.exNo = :exNo")
    int updateFields(@Param("exNo") String exNo, @Param("currencyRate") BigDecimal currencyRate, @Param("postingDate") String postingDate,
                     @Param("total") BigDecimal total);
    
    @Query(value =
	    "SELECT m.EX_NO, d1.ID AS D1_ID, d1.YEAR, d1.OUCODE, d1.ACCOUNTING, d1.UNTAX_AMOUNT, " +
	    "       m.POSTING_DATE, d1.MULTI_SHARE " +
	    "FROM BPM_EX_M m JOIN BPM_EX_M_D1 d1 ON d1.EX_NO = m.EX_NO " +
	    "WHERE m.FLOW_STATUS = 2 " +
	    "  AND TO_CHAR(m.POSTING_DATE,'YYYYMM') = :yyyymm " +
	    "  AND NVL(LENGTH(TRIM(d1.ACCOUNTING)),0) > 0 " +
	    "  AND SUBSTR(d1.ACCOUNTING,1,2) = '51'",
	    nativeQuery = true)
	  List<Object[]> findExpenseD1ByMonth(@Param("yyyymm") String yyyymm);

}
