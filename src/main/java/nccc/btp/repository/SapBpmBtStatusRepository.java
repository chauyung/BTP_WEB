package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.SapBpmBtStatus;

public interface SapBpmBtStatusRepository extends JpaRepository<SapBpmBtStatus, String> {
  
}
