package nccc.btp.repository;

import nccc.btp.dto.TransferRowTuple;
import nccc.btp.entity.NcccBudgetActual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface BudgetActualRepository extends JpaRepository<NcccBudgetActual, Long>,
        JpaSpecificationExecutor<NcccBudgetActual> {
	
	@Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from NcccBudgetActual a where a.id.yymm=:ym and a.id.version=:ver")
    int deleteByYmVer(@Param("ym") String yymm, @Param("ver") String version);
	
	@Query("select count(a) from NcccBudgetActual a where a.id.yymm=:ym and a.id.version=:ver")
	long countByYmVer(@Param("ym") String yymm, @Param("ver") String version);
	
    interface ActualPostedAgg {
        String getOu();
        String getAcc8();
        java.math.BigDecimal getPosted();
    }
   
    @Query("select a from NcccBudgetActual a " +
	       "where trim(a.id.yymm) = :yymm " +
	       "  and trim(a.id.version) = :ver " +
	       "  and (:appr is null or upper(trim(a.id.approation)) = :appr) " +
	       "  and (:ouEmpty = 1 or upper(trim(a.id.ouCode)) in :ouList) " +
	       "  and (:accFrom is null or function('LPAD', trim(a.id.accounting), 8, '0') >= :accFrom) " +
	       "  and (:accTo   is null or function('LPAD', trim(a.id.accounting), 8, '0') <= :accTo) " +
	       "order by upper(trim(a.id.ouCode)), function('LPAD', trim(a.id.accounting), 8, '0')")
	List<NcccBudgetActual> searchBudgetActual(
	        @Param("yymm") String yymm,
	        @Param("ver") String ver,
	        @Param("appr") String appr,              // 只會 AFTER / BEFORE
	        @Param("ouList") Collection<String> ouList,
	        @Param("ouEmpty") Integer ouEmpty,
	        @Param("accFrom") String accFrom,
	        @Param("accTo") String accTo
	);
 
    @Query("select a from NcccBudgetActual a " +
            "where (:yymm is null or trim(a.id.yymm) like concat(:yymm, '%')) " +
            "  and (:ver        is null or trim(a.id.version) = :ver) " +
            "  and (:ouEmpty   = 1 or upper(trim(a.id.ouCode)) in :ouCodes) " +
            "  and (:opEmpty   = 1 or upper(trim(a.id.operateItemCode)) in :oprs) " +
            "  and (:apprEmpty  = 1 or upper(trim(a.id.approation)) in :apprs) " +
            "order by trim(a.id.version), trim(a.id.yymm), trim(a.id.ouCode), trim(a.id.accounting)")
     List<NcccBudgetActual> searchByOperationQuery(
             @Param("yymm") String yymm,
             @Param("ver") String ver,
             @Param("ouCodes") List<String> ouCodes,
             @Param("ouEmpty") Integer ouEmpty,
             @Param("oprs") List<String> operateItemCodes,
             @Param("opEmpty") Integer opEmpty,
             @Param("apprs") Collection<String> apprs,
             @Param("apprEmpty") Integer apprEmpty
     );

    @Query("select " +
    	       "  upper(trim(a.id.ouCode)) as ou, " +
    	       "  substring(function('LPAD', trim(a.id.accounting), 8, '0'), 1, 8) as acc8, " +
    	       "  sum(coalesce(a.amount, 0)) as posted " +
    	       "from NcccBudgetActual a " +
    	       "where a.id.yymm between :startYm and :endYm " +
    	       "  and a.id.approation = 'AFTER' " +
    	       "  and ( :hasOu = 0 or upper(trim(a.id.ouCode)) in :ouFilter ) " +
    	       "  and ( :accFrom is null or substring(function('LPAD', trim(a.id.accounting), 8, '0'), 1, 8) >= :accFrom ) " +
    	       "  and ( :accTo   is null or substring(function('LPAD', trim(a.id.accounting), 8, '0'), 1, 8) <= :accTo   ) " +
    	       "group by upper(trim(a.id.ouCode)), substring(function('LPAD', trim(a.id.accounting), 8, '0'), 1, 8)")
	List<ActualPostedAgg> aggregatePosted(@Param("startYm") String startYm,
	                                      @Param("endYm") String endYm,
	                                      @Param("ouFilter") List<String> ouFilter,
	                                      @Param("hasOu") int hasOu,
	                                      @Param("accFrom") String accFrom,
	                                      @Param("accTo") String accTo);
    
    @Query("select new nccc.btp.dto.TransferRowTuple(" +
            " m.year, m.version, m.adjNo, d.outOuCode, d.inOuCode, m.applyDate, d.outAccounting, d.inAccounting, d.outBalance, d.adjustAmt" +
            ") " +
            " from NcccBudgetAdjustM m join m.details d " +
            " where (m.flowStatus is null or m.flowStatus <> '2') " +
            " and (:year is null or m.year = :year) " +
            " and (:ver is null or m.version = :ver) " +
            " and (:ds is null or m.applyDate >= :ds) " +
            " and (:de is null or m.applyDate <= :de) " +
            " and ( :depts is null or d.outOuCode in :depts or d.inOuCode in :depts ) " +
            " and ( " +
            "   (:biStart is null and :biEnd is null) " +
            "   or (:biStart is not null and :biEnd is not null and (d.outAccounting between :biStart and :biEnd or d.inAccounting between :biStart and :biEnd)) " +
            "   or (:biStart is not null and :biEnd is null and (d.outAccounting >= :biStart or d.inAccounting >= :biStart)) " +
            "   or (:biStart is null and :biEnd is not null and (d.outAccounting <= :biEnd or d.inAccounting <= :biEnd)) " +
            " ) " +
            " and (:adjNo is null or m.adjNo = :adjNo) " +
            " order by d.outOuCode, m.applyDate, m.adjNo, d.outAccounting")
    List<TransferRowTuple> search(@Param("year") String year,
                                  @Param("ver") String version,
                                  @Param("ds") LocalDate dateStart,
                                  @Param("de") LocalDate dateEnd,
                                  @Param("depts") Collection<String> departments,
                                  @Param("biStart") String biStart,
                                  @Param("biEnd") String biEnd,
                                  @Param("adjNo") String adjNo);
    
    @Query("select " +
            " a.id.yymm as yymm, a.id.version as version, a.id.approation as approation, " +
            " a.id.ouCode as ouCode, a.ouName as ouName, a.id.accounting as accounting, " +
            " a.subject as subject, a.id.operateItemCode as operateItemCode, a.operateItem as operateItem, " +
            " a.amount as amount " +
            "from NcccBudgetActual a " +
            "where length(a.id.accounting) = 8 " +
            "  and trim(a.id.yymm) between :s and :e " +
            "  and (:deptsEmpty = 1 or upper(trim(a.id.ouCode)) in :depts) " +
            "  and (:acf is null or a.id.accounting >= :acf) " +
            "  and (:act is null or a.id.accounting <= :act) " +
            "  and (:ap is null or a.id.approation = :ap) " +
            "order by a.id.ouCode, a.id.operateItemCode, a.id.accounting")
     List<DetailRowView> findDeptDetailRows(@Param("s") String startYm,
                                            @Param("e") String endYm,
                                            @Param("deptsEmpty") int deptsEmpty,
                                            @Param("depts") Collection<String> deptCodesUpper,
                                            @Param("acf") String accountFrom,
                                            @Param("act") String accountTo,
                                            @Param("ap") String approationOrNull);
    
    @Query("SELECT a FROM NcccBudgetActual a WHERE a.id.yymm BETWEEN :startYm AND :endYm " +
    	       "AND LENGTH(a.id.accounting) = 8 " +
    	       "AND (:deptsEmpty = 1 or upper(trim(a.id.ouCode)) in :depts) " +
    	       "AND (:accountFrom IS NULL OR a.id.accounting >= :accountFrom) " +
    	       "AND (:accountTo IS NULL OR a.id.accounting <= :accountTo) " +
    	       "AND (:ap is null or a.id.approation = :ap) ")
	List<NcccBudgetActual> queryActuals(@Param("startYm") String startYm,
	                                    @Param("endYm") String endYm,
	                                    @Param("deptsEmpty") int deptsEmpty,
	                                    @Param("depts") Collection<String> deptCodesUpper,
	                                    @Param("accountFrom") String accountFrom,
	                                    @Param("accountTo") String accountTo,
	                                    @Param("ap") String approationOrNull);
    
    interface Zfit0006Row {
        String getGjahr();   // 4 碼年
        String getPoper();   // 3 碼期(001~016)
        String getAnln1();   // 資產號
        String getHkont();   // 科目
        java.math.BigDecimal getWrbtr(); // 未稅金額
    }

    @Query(value =
        "SELECT t.GJAHR AS gjahr, t.POPER AS poper, t.ANLN1 AS anln1, " +
        "       t.HKONT AS hkont, t.WRBTR AS wrbtr " +
        "  FROM ZFIT0006 t " +
        " WHERE t.GJAHR = :yy " +
        "   AND SUBSTR(t.POPER, -2) = :mm",
        nativeQuery = true)
    List<Zfit0006Row> findZfit0006(@Param("yy") String year, @Param("mm") String month);

}
