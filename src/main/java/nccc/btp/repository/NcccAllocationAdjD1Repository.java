package nccc.btp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.NcccAllocationAdjD1;

public interface NcccAllocationAdjD1Repository extends JpaRepository<NcccAllocationAdjD1, NcccAllocationAdjD1.ConfigId> {


    @Modifying
    @Query("DELETE FROM NcccAllocationAdjD1 d WHERE d.allAdjNo = :allAdjNo")
    void deleteByAllAdjNo(@Param("allAdjNo") String allAdjNo);

    @Query("SELECT d FROM NcccAllocationAdjD1 d WHERE d.allAdjNo = :allAdjNo AND d.seqNo = :seqNo")
    List<NcccAllocationAdjD1> getByAllAdjNoAndSeqNo(@Param("allAdjNo") String allAdjNo,@Param("seqNo") String seqNo);
    
}
