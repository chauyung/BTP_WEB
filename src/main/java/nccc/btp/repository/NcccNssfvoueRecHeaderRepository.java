package nccc.btp.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.entity.NcccNssfvoueRecHeader;

public interface NcccNssfvoueRecHeaderRepository extends
    JpaRepository<NcccNssfvoueRecHeader, String>, JpaSpecificationExecutor<NcccNssfvoueRecHeader> {

  @Modifying
  @Transactional
  @Query("UPDATE NcccNssfvoueRecHeader n SET n.assignment  = :assignment, n.assignUser  = :assignUser, n.assignDate  = :assignDate WHERE n.nssfvoueHeaderBatch IN :batches")
  int assignByHeaderBatchIn(@Param("batches") List<String> batches,
      @Param("assignment") String assignment, @Param("assignUser") String assignUser,
      @Param("assignDate") LocalDate assignDate);

  @Modifying
  @Transactional
  @Query("UPDATE NcccNssfvoueRecHeader n SET n.assignment  = :assignment, n.reviewUser  = :reviewUser, n.reviewDate  = :reviewDate WHERE n.nssfvoueHeaderBatch IN :batches")
  int reviewByHeaderBatchIn(@Param("batches") List<String> batches,
      @Param("assignment") String assignment, @Param("reviewUser") String reviewUser,
      @Param("reviewDate") LocalDate reviewDate);

  @Modifying
  @Transactional
  @Query("UPDATE NcccNssfvoueRecHeader n SET n.assignment  = '0', n.assignUser  = null, n.assignDate  = null WHERE n.nssfvoueHeaderBatch IN :batches")
  int returnByHeaderBatchIn(@Param("batches") List<String> batches);

  @Modifying
  @Transactional
  @Query("UPDATE NcccNssfvoueRecHeader n SET n.assignment  = '1', n.reviewUser  = null, n.reviewDate  = null WHERE n.nssfvoueHeaderBatch IN :batches")
  int reviewAndReturnByHeaderBatchIn(@Param("batches") List<String> batches);

  @Modifying
  @Transactional
  @Query("UPDATE NcccNssfvoueRecHeader n SET n.assignment = :assignment WHERE n.nssfvoueHeaderBatch = :batch")
  int updateAssignmentByBatch(@Param("batch") String batch, @Param("assignment") String assignment);

  @Query("SELECT n.assignment FROM NcccNssfvoueRecHeader n WHERE n.nssfvoueHeaderBatch IN :batches")
  List<String> findAssignmentsByHeaderBatchIn(@Param("batches") List<String> batches);
  
  @Query("SELECT n FROM NcccNssfvoueRecHeader n WHERE n.nssfvoueHeaderBatch IN :batches")
  List<NcccNssfvoueRecHeader> findByHeaderBatchIn(@Param("batches") List<String> batches);
}
