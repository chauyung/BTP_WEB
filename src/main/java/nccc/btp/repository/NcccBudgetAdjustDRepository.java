package nccc.btp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.NcccBudgetAdjustD;

public interface NcccBudgetAdjustDRepository extends JpaRepository<NcccBudgetAdjustD, NcccBudgetAdjustD.ConfigId> {

    @Modifying
    @Query("DELETE FROM NcccBudgetAdjustD WHERE adjNo = :adjNo")
    void deleteByAdjNo(@Param("adjNo") String adjNo);

    @Query("SELECT n FROM NcccBudgetAdjustD n WHERE n.adjNo = :adjNo")
    List<NcccBudgetAdjustD> findByAdjNo(@Param("adjNo") String adjNo);
    
}
