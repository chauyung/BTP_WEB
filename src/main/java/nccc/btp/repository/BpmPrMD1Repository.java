package nccc.btp.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import nccc.btp.entity.BpmPrMD1;

public interface BpmPrMD1Repository extends JpaRepository<BpmPrMD1, Integer> {
	interface PrAgg {
        String getPrNo();
        String getOuCode();
        String getAccounting();
        BigDecimal getTotalSum();
        BigDecimal getTaxSum();
    }

    @Query("select p.prNo as prNo, p.ouCode as ouCode, p.accounting as accounting, " +
           "       sum(coalesce(p.total,0)) as totalSum, " +
           "       sum(coalesce(p.tax,0))   as taxSum " +
           "from BpmPrMD1 p " +
           "where p.prNo in :prNos " +
           "group by p.prNo, p.ouCode, p.accounting")
    List<PrAgg> findPrAgg(@Param("prNos") Set<String> prNos);

    List<BpmPrMD1> findByPrNo(String prNo);
}
