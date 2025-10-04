package nccc.btp.repository;

import nccc.btp.entity.NcccBudgetAdjustM;
import nccc.btp.dto.TransferRowTuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface NcccBudgetAdjustQueryRepository
        extends JpaRepository<NcccBudgetAdjustM, String>, JpaSpecificationExecutor<NcccBudgetAdjustM> {
		
	@Query("select new nccc.btp.dto.TransferRowTuple(" +
		       " m.year, m.version, m.adjNo, d.outOuCode, d.inOuCode, m.applyDate," +
		       " d.outAccounting, d.inAccounting, d.outBalance, d.adjustAmt) " +
		       "from NcccBudgetAdjustD d join d.master m " +
		       "where (m.flowStatus is null or m.flowStatus <> '2') " +
		       "and (:year is null or m.year = :year) " +
		       "and (:ver  is null or m.version = :ver) " +
		       "and (:ds   is null or m.applyDate >= :ds) " +
		       "and (:de   is null or m.applyDate <= :de) " +
		       "and ( :depts is null or d.outOuCode in :depts or d.inOuCode in :depts ) " +
		       "and ( (:biStart is null and :biEnd is null) " +
		       "   or (:biStart is not null and :biEnd is not null and " +
		       "       (d.outAccounting between :biStart and :biEnd or d.inAccounting between :biStart and :biEnd)) " +
		       "   or (:biStart is not null and :biEnd is null and (d.outAccounting >= :biStart or d.inAccounting >= :biStart)) " +
		       "   or (:biStart is null and :biEnd is not null and (d.outAccounting <= :biEnd or d.inAccounting <= :biEnd)) ) " +
		       "and (:adjNo is null or m.adjNo = :adjNo) " +
		       "order by d.outOuCode, m.applyDate, m.adjNo, d.outAccounting")
		List<TransferRowTuple> search(@Param("year") String year,
		                              @Param("ver") String ver,
		                              @Param("ds") LocalDate ds,
		                              @Param("de") LocalDate de,
		                              @Param("depts") Collection<String> depts,
		                              @Param("biStart") String biStart,
		                              @Param("biEnd") String biEnd,
		                              @Param("adjNo") String adjNo);
}
