package nccc.btp.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.SapPendingRemittanceStatus;

public interface SapPendingRemittanceStatusRepository extends JpaRepository<SapPendingRemittanceStatus, Long> {
  
  List<SapPendingRemittanceStatus> findByIdIn(Collection<Long> ids);
}
