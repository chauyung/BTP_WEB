package nccc.btp.repository;

import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import nccc.btp.entity.NcccAllocationAdjD;

public interface NcccAllocationAdjDRepository extends JpaRepository<NcccAllocationAdjD, NcccAllocationAdjD.ConfigId> {

    @Modifying
    @Query("DELETE FROM NcccAllocationAdjD d WHERE d.allAdjNo = :allAdjNo")
    void deleteByAllAdjNo(@Param("allAdjNo") String allAdjNo);

    @Query("SELECT d FROM NcccAllocationAdjD d WHERE d.allAdjNo = :allAdjNo")
    List<NcccAllocationAdjD> getByAllAdjNo(@Param("allAdjNo") String no);

    @Query("SELECT d FROM NcccAllocationAdjD d WHERE d.allAdjNo = :allAdjNo AND d.seqNo = :seqNo")
    NcccAllocationAdjD findByAllAdjNoAndSeqNo(@Param("allAdjNo") String allAdjNo,@Param("seqNo") String seqNo);
    
}
