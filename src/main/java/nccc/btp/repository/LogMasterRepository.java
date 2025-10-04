package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import nccc.btp.entity.LogMaster;

@Repository
public interface LogMasterRepository extends JpaRepository<LogMaster, String> {

}
