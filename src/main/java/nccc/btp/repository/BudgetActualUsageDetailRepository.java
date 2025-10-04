package nccc.btp.repository;

import nccc.btp.entity.NcccBudgetM;
import nccc.btp.vo.BudgetActualUsageDetailVo.TxnRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BudgetActualUsageDetailRepository extends JpaRepository<NcccBudgetM, NcccBudgetM.ConfigId> {

	@Query("select new nccc.btp.vo.BudgetActualUsageDetailVo$TxnRow(" +
		       " m.year, m.version, m.ouCode, m.accounting, " +
		       " coalesce(cast(al.accountLong as string), coalesce(al.essay, '')), " +
		       " (coalesce(m.originalBudget,0) + coalesce(m.reserveBudget,0)), " +
		       " coalesce(m.reserveBudget,0), " +
		       " coalesce(m.allocIncreaseAmt,0), coalesce(m.allocReduseAmt,0), " +
		       " coalesce(m.occupyAmt,0), coalesce(m.useAmt,0), " +
		       " t.transcationDate, t.transcationNo, t.transcationType, t.amount, " +
		       " t.docNo, t.transcationSource, t.bpNo, t.bpName) " +
		       "from nccc.btp.entity.NcccBudgetM m " +
		       "join nccc.btp.entity.NcccAccountingList al on al.subject = m.accounting " +   // << 這裡改成 subject
		       "left join nccc.btp.entity.NcccBudgetTranscation t on " +
		       "  t.year = m.year and t.version = m.version and " +
		       "  t.ouCode = m.ouCode and t.accounting = m.accounting and " +
		       "  t.transcationDate between :startDate and :endDate " +
		       "where m.year = :year and m.version = :version " +
		       "  and (:deptEmpty = true or m.ouCode in :depts) " +
		       "  and (:accFrom is null or m.accounting >= :accFrom) " +
		       "  and (:accTo   is null or m.accounting <= :accTo) " +
		       "order by m.ouCode, m.accounting, t.transcationDate, t.transcationNo")
	List<TxnRow> findTxnRows(
	        @Param("year") String year,
	        @Param("version") String version,
	        @Param("deptEmpty") boolean deptEmpty,
	        @Param("depts") java.util.Collection<String> depts,
	        @Param("accFrom") String accFrom,
	        @Param("accTo") String accTo,
	        @Param("startDate") java.time.LocalDate startDate,
	        @Param("endDate") java.time.LocalDate endDate
	);
}
