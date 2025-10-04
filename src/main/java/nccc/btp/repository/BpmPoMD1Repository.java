package nccc.btp.repository;

import nccc.btp.entity.BpmPoMD1;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface BpmPoMD1Repository extends JpaRepository<BpmPoMD1, Integer> {
	interface PoAgg {
        String getPoNo();
        String getOuCode();
        String getAccounting();
        BigDecimal getBpTotalSum();
        BigDecimal getBpTaxSum();
    }

    @Query("select p.poNo as poNo, p.ouCode as ouCode, p.accounting as accounting, " +
           "       sum(coalesce(p.bpTotal,0)) as bpTotalSum, " +
           "       sum(coalesce(p.bpTax,0))   as bpTaxSum " +
           "from BpmPoMD1 p " +
           "where p.poNo in :poNos " +
           "group by p.poNo, p.ouCode, p.accounting")
    List<PoAgg> findPoAgg(@Param("poNos") Set<String> poNos);
}
