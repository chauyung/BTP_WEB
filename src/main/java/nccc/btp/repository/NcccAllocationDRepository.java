package nccc.btp.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import nccc.btp.vo.BudgetVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.NcccAllocationD;

public interface NcccAllocationDRepository extends JpaRepository<NcccAllocationD, NcccAllocationD.ConfigId> {

    @Query("SELECT d FROM NcccAllocationD d WHERE d.poNo = :poNo AND d.poItemNo = :poItemNo")
    List<NcccAllocationD> findByPoNoAndPoItemNo(@Param("poNo") String poNo,@Param("poItemNo") String poItemNo);

    @Query("SELECT d FROM NcccAllocationD d WHERE d.poNo = :poNo AND d.poItemNo = :poItemNo AND d.plan = :plan")
    NcccAllocationD findByPoNoAndPoItemNoAndPlan(@Param("poNo") String poNo,@Param("poItemNo") String poItemNo,@Param("plan") String plan);
    
    /**
     * 刪除指定條件的所有明細資料
     * @param poNo 採購單編號
     * @param poItemNo 採購單項次
     */
    @Query("DELETE FROM NcccAllocationD d WHERE d.poNo = :poNo AND d.poItemNo = :poItemNo")
    @Modifying
    void deleteByPoNoAndPoItemNo(@Param("poNo") String poNo,@Param("poItemNo") String poItemNo);

    @Query(value =
		  "SELECT  M.ACCOUNTING, M.SUBJECT, M.ITEM_TEXT, M.OUCODE, M.OUNAME, " +
		  "        D.PO_NO, D.PO_ITEM_NO, D.PLAN, D.YEAR, D.MONTH, D.FINAL_TOTAL, D.SAP_DOC_NO, D.POST_DATE " +
		  "FROM NCCC_ALLOCATION_M M " +
		  "JOIN NCCC_ALLOCATION_D D " +
		  "  ON TRIM(D.PO_NO)=TRIM(M.PO_NO) " +
		  " AND LPAD(TRIM(D.PO_ITEM_NO),5,'0')=LPAD(TRIM(M.PO_ITEM_NO),5,'0') " +
		  "WHERE D.SAP_DOC_NO IS NULL " +
		  "  AND D.YEAR = :year AND D.MONTH = :month " +
		  "  AND TRUNC(D.POST_DATE) = :postDate",
		  nativeQuery = true)
	List<BudgetVo.NcccAllocationSAPData> getSAPData(@Param("year") String year,
	                                                @Param("month") String month,
	                                                @Param("postDate") LocalDate postDate);
    interface AccrualRow {
    	String getPoNo();
    	String getPoItemNo();
    	String getPlan();
    	Timestamp getPostDate();
    	String getSapDocNo();
    	BigDecimal getFinalTotal();
    	String getOuCode();
    	String getOuName();
    	String getAccounting();
    	String getSubject();
    }

    @Query(value =
		  "SELECT " +
		  "  d.PO_NO                  		poNo, " +
		  "  d.PO_ITEM_NO 					poItemNo, " +
		  "  d.PLAN                         plan, " +
		  "  CAST(d.POST_DATE AS TIMESTAMP) postDate, " +
		  "  d.SAP_DOC_NO            		sapDocNo, " +
		  "  d.FINAL_TOTAL                  finalTotal, " +
		  "  m.OUCODE                       ouCode, " +
		  "  m.OUNAME                       ouName, " +
		  "  m.ACCOUNTING                   accounting, " +
		  "  m.SUBJECT                      subject " +
		  "FROM NCCC_ALLOCATION_D d " +
		  "JOIN ( " +
		  "  SELECT * FROM ( " +
		  "    SELECT mm.PO_NO PO_NO, " +
		  "           mm.PO_ITEM_NO PO_ITEM_NO, " +
		  "           mm.OUCODE, mm.OUNAME, mm.ACCOUNTING, mm.SUBJECT, " +
		  "           ROW_NUMBER() OVER ( " +
		  "             PARTITION BY mm.PO_NO, mm.PO_ITEM_NO" +
		  "             ORDER BY mm.CREATE_DATE DESC NULLS LAST " +
		  "           ) rn " +
		  "    FROM NCCC_ALLOCATION_M mm " +
		  "  ) WHERE rn = 1 " +
		  ") m ON m.PO_NO = TRIM(d.PO_NO) " +
		  "    AND m.PO_ITEM_NO = d.PO_ITEM_NO " +
		  "WHERE TO_CHAR(d.POST_DATE,'YYYYMM') = :ym " +
		  "  AND TRIM(d.SAP_DOC_NO) IS NOT NULL",
		  nativeQuery = true)
	List<AccrualRow> findRowsByYm(@Param("ym") String yyyymm);
}
