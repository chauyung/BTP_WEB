package nccc.btp.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import nccc.btp.entity.SapNssfvoueRecStatus;

public interface SapNssfvoueRecStatusRepository
    extends JpaRepository<SapNssfvoueRecStatus, String> {

  @Query("SELECT s.belnr FROM SapNssfvoueRecStatus s WHERE s.nssfvoueHeaderBatch = :nssfvoueHeaderBatch")
  String findBelnrByNssfvoueHeaderBatch(@Param("nssfvoueHeaderBatch") String nssfvoueHeaderBatch);

  List<SapNssfvoueRecStatus> findByNssfvoueHeaderBatchIn(Collection<String> batches);

  List<SapNssfvoueRecStatus> findByTypeAndNssfvoueHeaderBatchIn(String type,
      Collection<String> batches);
}
