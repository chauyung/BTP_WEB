package nccc.btp.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import nccc.btp.entity.NcccPreBudgetD1;

public interface NcccPreBudgetD1Repository extends JpaRepository<NcccPreBudgetD1, NcccPreBudgetD1.ConfigId> {

    /**
     * 根據主檔ID查詢所有明細資料
     * @param budgetNo 主檔ID
     * @return 明細資料列表
     */
    @Query("SELECT d FROM NcccPreBudgetD1 d WHERE d.budgetNo = :budgetNo")
    List<NcccPreBudgetD1> findByBudgetNo(@Param("budgetNo") String budgetNo);

    /**
     * 刪除指定主檔的所有明細資料
     * @param budgetNo 主檔ID
     */
    @Query("DELETE FROM NcccPreBudgetD1 d WHERE d.budgetNo = :budgetNo")
    @Modifying
    void deleteByBudgetNo(@Param("budgetNo") String budgetNo);

    /**
     * 根據主檔ID查詢所有明細資料
     * @param budget_NO
     * @param seq_NO
     * @return
     */
    @Query("SELECT d FROM NcccPreBudgetD1 d WHERE d.budgetNo = :budgetNo AND d.seqNo = :seqNo")
    List<NcccPreBudgetD1> findByBudgetNoAndSeqNo(@Param("budgetNo") String budgetNo, @Param("seqNo") String seqNo);

    /**
     * 取得對應作業項目
     * @param budgetNoList
     * @return
     */
    @Query("SELECT DISTINCT d.operateItem FROM NcccPreBudgetD1 d WHERE d.budgetNo IN (:budgetNoList) ORDER BY d.operateItem")
    List<String> getOperateItemByBudgetNoList(@Param("budgetNoList") List<String> budgetNoList);

}
