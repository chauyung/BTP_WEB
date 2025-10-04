package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import nccc.btp.entity.LogFunctionStats;
import nccc.btp.entity.LogFunctionStatsId;

@Repository
public interface LogFunctionStatsRepository
    extends JpaRepository<LogFunctionStats, LogFunctionStatsId> {

}