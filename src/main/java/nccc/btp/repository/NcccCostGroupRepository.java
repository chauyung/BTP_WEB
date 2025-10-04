package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import nccc.btp.entity.NcccCostGroup;

public interface NcccCostGroupRepository extends JpaRepository<NcccCostGroup, Long> {

  @Query("SELECT COALESCE(MAX(n.id), 0) FROM NcccCostGroup n")
  Long getMaxId();

}
