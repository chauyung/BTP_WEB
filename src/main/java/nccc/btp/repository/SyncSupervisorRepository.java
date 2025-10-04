package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.SyncSupervisor;
import nccc.btp.entity.SyncSupervisorId;

public interface SyncSupervisorRepository extends JpaRepository<SyncSupervisor, SyncSupervisorId> {

}
