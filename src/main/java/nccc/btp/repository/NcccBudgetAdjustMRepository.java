package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.NcccBudgetAdjustM;

public interface NcccBudgetAdjustMRepository extends JpaRepository<NcccBudgetAdjustM, String> {

    /**
     * 取當月（yyMM）最大的 4 碼流水號
     */
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(b.adjNo, 7, 4) AS int)), 0) FROM NcccBudgetAdjustM b WHERE SUBSTRING(b.adjNo, 3, 4) = :prefix")
    Integer findMaxSerialByPrefix(@Param("prefix") String prefix);


    /**
     * 查詢指定年度和版本的預算調撥主檔
     * @param adjNo
     * @param year
     * @param version
     * @return
     */
    @Query("SELECT n FROM NcccBudgetAdjustM n WHERE n.year=:year AND n.version=:version AND n.adjNo=:adjNo")
    NcccBudgetAdjustM findByAdjNoAndYearAndVersion(@Param("adjNo") String adjNo,@Param("year") String year,@Param("version") String version);

    /**
     * 查詢指定流水號的預算調撥主檔
     * @param adjNo
     * @return
     */
    @Query("SELECT n FROM NcccBudgetAdjustM n WHERE n.adjNo=:adjNo")
    NcccBudgetAdjustM findByAdjNo(@Param("adjNo") String adjNo);

    /**
     * 查詢指定任務號的預算調撥主檔
     * @param taskID
     * @return
     */
    @Query("SELECT n FROM NcccBudgetAdjustM n WHERE n.taskID=:taskID")
    NcccBudgetAdjustM findByTaskID(@Param("taskID") String taskID);

}
