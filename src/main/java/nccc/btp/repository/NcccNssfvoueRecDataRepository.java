package nccc.btp.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.entity.NcccNssfvoueRecData;
import nccc.btp.entity.NcccNssfvoueRecDataId;

public interface NcccNssfvoueRecDataRepository extends JpaRepository<NcccNssfvoueRecData, NcccNssfvoueRecDataId> {

  @Transactional
  @Modifying
  @Query("DELETE FROM NcccNssfvoueRecData d WHERE d.id.nssfvoueDataBatch = :batch")
  void deleteByDataBatch(@Param("batch") String batch);
  
  @Transactional
  @Modifying
  @Query("DELETE FROM NcccNssfvoueRecData d WHERE d.id.nssfvoueDataBatch = :batch and sourceFile  LIKE CONCAT(:sourceFile, '%')")
  void deleteByDataBatchAndSourceFile(@Param("batch") String batch,@Param("sourceFile") String sourceFile);
  
  List<NcccNssfvoueRecData> findByIdNssfvoueDataBatchIn(Collection<String> batches);
  
  List<NcccNssfvoueRecData> findByIdNssfvoueDataBatch(String nssfvoueDataBatch);
}
