package nccc.btp.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import nccc.btp.entity.NcccPreBudgetD;

public interface NcccPreBudgetDRepository extends JpaRepository<NcccPreBudgetD, NcccPreBudgetD.ConfigId> {

    /**
     * 根據主檔ID查詢所有明細資料
     * @param budgetNo 主檔ID
     * @return 明細資料列表
     */
    @Query("SELECT d FROM NcccPreBudgetD d WHERE d.budgetNo = :budgetNo")
    List<NcccPreBudgetD> findByBudgetNo(@Param("budgetNo") String budgetNo);

    /**
     * 刪除指定主檔的所有明細資料
     * @param budgetNo 主檔ID
     */
    @Query("DELETE FROM NcccPreBudgetD d WHERE d.budgetNo = :budgetNo")
    @Modifying
    void deleteByBudgetNo(@Param("budgetNo") String budgetNo);
}
