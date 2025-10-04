package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import nccc.btp.entity.NcccCostAccount;

public interface NcccCostAccountRepository extends JpaRepository<NcccCostAccount, Long> {

  @Query("SELECT COALESCE(MAX(n.id), 0) FROM NcccCostAccount n")
  Long getMaxId();
}
