package nccc.btp.repository;

import nccc.btp.entity.NcccProvisionExpenseM;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NcccProvisionExpenseMRepository extends JpaRepository<NcccProvisionExpenseM, String> {
    
    /**
     * 取當月（yyMM）最大的 4 碼流水號
     */
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(b.provisionNo, 7, 4) AS int)), 0) FROM NcccProvisionExpenseM b WHERE SUBSTRING(b.provisionNo, 3, 4) = :prefix")
    Integer findMaxSerialByPrefix(@Param("prefix") String prefix);

    /**
     * 查詢指定年度和版本的預算提列主檔
     * @param provisionNo
     * @param year
     * @param version
     * @return
     */
    NcccProvisionExpenseM findByProvisionNoAndYearAndVersion(@Param("provisionNo") String provisionNo,@Param("year") String year,@Param("version") String version);

    /**
     * 查詢預算提列主檔
     * @param provisionNo
     * @return
     */
    @Query("SELECT n FROM NcccProvisionExpenseM n WHERE n.provisionNo=:provisionNo")
    NcccProvisionExpenseM findByProvisionNo(@Param("provisionNo") String provisionNo);

    /**
     * 查詢預算提列主檔
     * @param taskId
     * @return
     */
    @Query("SELECT n FROM NcccProvisionExpenseM n WHERE n.taskID=:taskId")
    NcccProvisionExpenseM findByTaskId(@Param("taskId") String taskId);
    
    interface HeaderRow {
        String getProvisionNo();
        String getSeqNo();
        String getOuCode();
        String getAccounting();
        BigDecimal getAmount();
        String getAllocationMethod();
        String getYear();              
    }

    @Query(value =
    	    "SELECT m.PROVISION_NO AS provisionNo, " +
    	    "       d.SEQ_NO       AS seqNo, " +
    	    "       d.OUCODE       AS ouCode, " +
    	    "       m.ACCOUNTING   AS accounting, " +
    	    "       d.AMOUNT       AS amount, " +
    	    "       NVL(TRIM(d.ALLOCATION_METHOD),'2') AS allocationMethod, " +
    	    "       m.YEAR         AS year " +
    	    "  FROM NCCC_PROVISION_EXPENSE_M m " +
    	    "  JOIN NCCC_PROVISION_EXPENSE_D d ON d.PROVISION_NO = m.PROVISION_NO " +
    	    " WHERE m.FLOW_STATUS = '2' " +
    	    "   AND m.YEAR  = :yy " +
    	    "   AND m.MONTH = :mm " +
    	    " ORDER BY m.PROVISION_NO, d.SEQ_NO",
    	    nativeQuery = true)
    	List<HeaderRow> findHeaders(@Param("yy") String year, @Param("mm") String month);

    
}
