package nccc.btp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.NcccBudgetReserveM;
import nccc.btp.vo.BudgetVo.OpenBudgetReserveData;
import nccc.btp.vo.BudgetVo.OpenBudgetReserveView;

public interface NcccBudgetReserveMRepository extends JpaRepository<NcccBudgetReserveM, String> {

    /**
     * 取當月（yyMM）最大的 4 碼流水號
     */
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(b.rsvNo, 7, 4) AS int)), 0) FROM NcccBudgetReserveM b WHERE SUBSTRING(b.rsvNo, 3, 4) = :prefix")
    Integer findMaxSerialByPrefix(@Param("prefix") String prefix);

    /**
     * 查詢指定年度和版本的預算保留主檔
     * @param rsvNo
     * @param year
     * @param version
     * @return
     */
    @Query("SELECT m FROM NcccBudgetReserveM m WHERE m.year=:year AND m.version=:version AND m.rsvNo=:rsvNo")
    NcccBudgetReserveM findByRsvNoAndYearAndVersion(@Param("rsvNo") String rsvNo,@Param("year") String year,@Param("version") String version);

    /**
     * 查詢指定流水號的預算保留主檔
     * @param rsvNo
     * @return
     */
    @Query("SELECT m FROM NcccBudgetReserveM m WHERE m.rsvNo = :rsvNo")
    NcccBudgetReserveM findByRsvNo(@Param("rsvNo") String rsvNo);

    /**
     * 查詢開帳已結案,採購單號等於空白的預算保留資料
     * @return
     */
    @Query(value="SELECT M.YEAR,M.VERSION,M.RSV_NO,M.APPLY_DATE,M.APPLY_OUCODE,M.APPLY_USER,"+
            "       D.ACCOUNTING,D.DOC_NO,D.PO_NO,D.PO_AMT,D.PO_REMARK,D.RESERVER_AMT,D.RES_REASON" +
            " FROM NCCC_BUDGET_RESERVE_M M inner join NCCC_BUDGET_RESERVE_D D on M.RSV_NO = D.RSV_NO" +
            " WHERE M.FLOW_STATUS='2' AND D.PO_NO IS NULL AND M.year=:year AND M.version=:version",nativeQuery = true)
    List<OpenBudgetReserveView> findOpenBudgetReserveData(@Param("year") String year,@Param("version") String version);

    /**
     * 查詢指定任務號的預算保留主檔
     * @param taskId
     * @return
     */
    @Query("SELECT m FROM NcccBudgetReserveM m WHERE m.taskId = :taskId")
    NcccBudgetReserveM findByTaskId(@Param("taskId") String taskId);

}
