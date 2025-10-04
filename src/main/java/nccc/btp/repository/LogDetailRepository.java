package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import nccc.btp.entity.LogDetail;
import nccc.btp.entity.LogDetailId;

@Repository
public interface LogDetailRepository extends JpaRepository<LogDetail, LogDetailId> {

}