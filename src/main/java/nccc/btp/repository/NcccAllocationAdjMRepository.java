package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.NcccAllocationAdjM;

public interface NcccAllocationAdjMRepository extends JpaRepository<NcccAllocationAdjM, String> {

    /**
     * 取當月（yyMM）最大的 4 碼流水號
     */
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(b.allAdjNo, 7, 4) AS int)), 0) FROM NcccAllocationAdjM b WHERE SUBSTRING(b.allAdjNo, 3, 4) = :prefix")
    Integer findMaxSerialByPrefix(@Param("prefix") String prefix);


    @Query("SELECT a FROM NcccAllocationAdjM a WHERE a.allAdjNo = :allAdjNo")
    NcccAllocationAdjM findByAllocationAdjNo(@Param("allAdjNo") String allAdjNo);

    @Query("SELECT a FROM NcccAllocationAdjM a WHERE a.taskID = :taskId")
    NcccAllocationAdjM findByTaskId(@Param("taskId") String taskId);
    
}
