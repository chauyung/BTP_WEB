package nccc.btp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.NcccBudgetAdjustD1;

public interface NcccBudgetAdjustD1Repository extends JpaRepository<NcccBudgetAdjustD1, NcccBudgetAdjustD1.ConfigId> {

    @Modifying
    @Query("DELETE FROM NcccBudgetAdjustD1 WHERE adjNo = :adjNo")
    void deleteByAdjNo(@Param("adjNo") String adjNo);

    List<NcccBudgetAdjustD1> findByAdjNoAndSeqNo(String adjNo, String seqNo);
    
}
