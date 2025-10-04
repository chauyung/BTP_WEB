package nccc.btp.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.entity.NcccEmsMgm;

public interface NcccEmsMgmRepository
    extends JpaRepository<NcccEmsMgm, String>, JpaSpecificationExecutor<NcccEmsMgm> {

  @EntityGraph(attributePaths = "sapEmsRecStatus")
  List<NcccEmsMgm> findAll(Specification<NcccEmsMgm> spec);

  boolean existsByWealthNoAndNcccWealthNo(String wealthNo, String ncccWealthNo);

  boolean existsByWealthNoAndNcccWealthNoAndStatus(String wealthNo, String ncccWealthNo,
      String status);

  @Modifying
  @Query("UPDATE NcccEmsMgm e SET e.status = '3',e.postingDate = :postingDate ,e.docNo = :docNo,e.description = :description WHERE e.wealthNo = :wealthNo AND e.ncccWealthNo = :ncccWealthNo")
  int updateStatusToScrap(@Param("wealthNo") String wealthNo,
      @Param("ncccWealthNo") String ncccWealthNo, @Param("postingDate") LocalDate postingDate,
      @Param("docNo") String docNo, @Param("description") String description);

  @Modifying
  @Transactional
  @Query("UPDATE NcccEmsMgm n SET n.status = :status, n.postingDate = :postingDate ,n.docNo = :docNo,n.description = :description,n.listed = :listed,n.impairmentDate = :impairmentDate , n.updateDate = CURRENT_DATE , n.updateUser = :updateUser WHERE n.wealthNo = :wealthNo")
  int updateStatusByWealthNo(@Param("wealthNo") String wealthNo, @Param("status") String status,
      @Param("postingDate") LocalDate postingDate, @Param("docNo") String docNo,
      @Param("description") String description, @Param("listed") String listed,
      @Param("impairmentDate") LocalDate impairmentDate, @Param("updateUser") String updateUser);

  @Modifying
  @Query("UPDATE NcccEmsMgm e SET e.listed = 'Y' WHERE e.wealthNo = :wealthNo AND e.ncccWealthNo = :ncccWealthNo")
  int updateListed(@Param("wealthNo") String wealthNo, @Param("ncccWealthNo") String ncccWealthNo);

  @Modifying
  @Transactional
  @Query("UPDATE NcccEmsMgm e SET e.impairmentDate = :impairmentDate WHERE e.wealthNo = :wealthNo AND e.ncccWealthNo = :ncccWealthNo")
  int updateImpairmentDate(@Param("impairmentDate") LocalDate impairmentDate,
      @Param("wealthNo") String wealthNo, @Param("ncccWealthNo") String ncccWealthNo);
}
