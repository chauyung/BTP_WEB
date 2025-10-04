package nccc.btp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.NcccAllocationAdjD2;
import nccc.btp.vo.BudgetVo.NcccAllocationSAPData;

public interface NcccAllocationAdjD2Repository extends JpaRepository<NcccAllocationAdjD2, NcccAllocationAdjD2.ConfigId> {

    @Modifying
    @Query("DELETE FROM NcccAllocationAdjD2 d WHERE d.allAdjNo = :allAdjNo")
    void deleteByAllAdjNo(@Param("allAdjNo") String allAdjNo);

    @Query("SELECT d FROM NcccAllocationAdjD2 d WHERE d.allAdjNo = :allAdjNo AND d.seqNo = :seqNo")
    List<NcccAllocationAdjD2> getByAllAdjNoAndSeqNo(@Param("allAdjNo") String allAdjNo,@Param("seqNo") String seqNo);

//    @Query(value = "SELECT  M.ALL_ADJ_NO," +
//                "    D.SEQ_NO, D.ACCOUNTING, D.SUBJECT, D.ITEM_TEXT, D.OUCODE, D.OUNAME, D.PO_NO," +
//                "    D2.PLAN, D2.YEAR, D2.MONTH, D2.FINAL_TOTAL, D2.SAP_DOC_NO, D2.POST_DATE" +
//                " FROM NCCC_ALLOCATION_ADJ_M M " +
//                " INNER JOIN NCCC_ALLOCATION_ADJ_D D on D.ALL_ADJ_NO = M.ALL_ADJ_NO" +
//                " INNER JOIN NCCC_ALLOCATION_ADJ_D2 D2 on D2.ALL_ADJ_NO = D.ALL_ADJ_NO AND D2.SEQ_NO = D.SEQ_NO" +
//                " WHERE D2.SAP_DOC_NO IS NULL AND D2.YEAR = :year AND D2.MONTH = :month AND D2.POST_DATE = :postDate", nativeQuery = true)
//    List<NcccAllocationSAPData> getSAPData(@Param("year") String year ,@Param("month") String month,@Param("postDate") LocalDate POST_DATE);

    @Query("SELECT d FROM NcccAllocationAdjD2 d WHERE d.allAdjNo = :allAdjNo AND d.seqNo = :seqNo")
    NcccAllocationAdjD2 findByAllAdjNoAndSeqNo(@Param("allAdjNo") String allAdjNo,@Param("seqNo") String seqNo);
}
