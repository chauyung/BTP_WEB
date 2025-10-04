package nccc.btp.repository;

import java.util.List;

import nccc.btp.dto.BudgetGetRuleListDto;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import nccc.btp.entity.NcccPreBudgetM;
import nccc.btp.vo.BudgetVo;

public interface NcccPreBudgetMRepository extends JpaRepository<NcccPreBudgetM, String> {

    /**
     * 取當月（yyMM）最大的 4 碼流水號
     */
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(b.budgetNo, 7, 4) AS int)), 0) FROM NcccPreBudgetM b WHERE SUBSTRING(b.budgetNo, 3, 4) = :prefix")
    Integer findMaxSerialByPrefix(@Param("prefix") String prefix);
    
    /**
     * 查詢指定年度和版本的預算編號主檔
     * @param budgetNo 預算編號
     * @param year 年度
     * @param version 版本
     * @return 預算主檔
     */
    @Query("SELECT n FROM NcccPreBudgetM n WHERE n.year=:year AND n.version=:version AND n.budgetNo=:budgetNo")
    NcccPreBudgetM findByBudgetNoAndYearAndVersion(@Param("budgetNo") String budgetNo, @Param("year") String year, @Param("version") String version);

    /**
     * 查詢指定年度和版本的預算編號主檔
     * @param year 年度
     * @param version 版本
     * @return 預算主檔列表
     */
    @Query("SELECT n FROM NcccPreBudgetM n WHERE n.year=:year AND n.version=:version")
    List<NcccPreBudgetM> findByYearAndVersion( @Param("year") String year,@Param("version") String version);
      
    /**
     * 查詢指定年度和版本的預算項目
     * @param year 年度
     * @param version 版本
     * @return 預算主檔列表
     */
    @Query(value = "SELECT" +
                "    d.budget_no AS BUDGET_NO," +
                "    d.SEQ_NO AS SEQ_NO," +
                "    m.year AS YEAR," +
                "    m.version AS VERSION," +
                "    m.oucode AS OU_CODE," +
                "    ou.OU_NAME AS OU_NAME," +
                "    d.accounting AS ACC_CODE," +
                "    ac.ESSAY AS ACC_NAME," +
                "    d.BUD_AMOUNT AS BUD_AMOUNT," +
                "    d.REMARK AS REMARK" +
                "    FROM nccc_pre_budget_m m" +
                "    INNER JOIN nccc_pre_budget_d d ON m.budget_no = d.budget_no" +
                "    LEFT JOIN SYNC_OU ou ON m.oucode = ou.OU_CODE" +
                "    LEFT JOIN NCCC_ACCOUNTING_LIST ac ON d.accounting = ac.SUBJECT WHERE year=:year AND version=:version",nativeQuery = true)
    List<BudgetVo.BudgetItem> GetItemsByYearAndVersion(@Param("year") String year,@Param("version") String version);

    /**
     * 查詢指定部門的年度和版本的預算項目
     * @param year 年度
     * @param version 版本
     * @return 預算主檔列表
     */
    @Query(value = "SELECT" +
                "    d.budget_no AS BUDGET_NO," +
                "    d.SEQ_NO AS SEQ_NO," +
                "    m.year AS YEAR," +
                "    m.version AS VERSION," +
                "    m.oucode AS OU_CODE," +
                "    ou.OU_NAME AS OU_NAME," +
                "    d.accounting AS ACC_CODE," +
                "    ac.ESSAY AS ACC_NAME," +
                "    d.BUD_AMOUNT AS BUD_AMOUNT," +
                "    d.REMARK AS REMARK" +
                "    FROM nccc_pre_budget_m m" +
                "    INNER JOIN nccc_pre_budget_d d ON m.budget_no = d.budget_no" +
                "    LEFT JOIN SYNC_OU ou ON m.oucode = ou.OU_CODE" +
                "    LEFT JOIN NCCC_ACCOUNTING_LIST ac ON d.accounting = ac.SUBJECT WHERE year=:year AND version=:version AND m.oucode=:ouCode",nativeQuery = true)
    List<BudgetVo.BudgetItem> GetItemsByYearAndVersionAndOuCode(@Param("year") String year,@Param("version") String version,@Param("ouCode") String ouCode);

    /**
     * 查詢指定年度和版本的作業項目
     * @param year 年度
     * @param version 版本
     * @return 預算主檔列表
     */
    @Query(value = "SELECT" +
                "    m.year AS YEAR," +
                "    m.version AS VERSION," +
                "    m.oucode AS OU_CODE," +
                "    ou.OU_NAME AS OU_NAME," +
                "    d.accounting AS ACC_CODE," +
                "    ac.ESSAY AS ACC_NAME," +
                "    d.BUD_AMOUNT AS BUD_AMOUNT," +
                "    d1.OPERATE_ITEM AS OperateItem," +
                "    d1.OPERATE_ITEM_CODE AS OperateItemCode," +
                "    d1.OPERATE_AMT AS OperateAmt," +
                "    d1.OPERATE_RATIO AS OperateRatio" +
                "    FROM nccc_pre_budget_m m" +
                "    INNER JOIN nccc_pre_budget_d d ON m.budget_no = d.budget_no" +
                "    INNER JOIN nccc_pre_budget_d1 d1 ON d.budget_no = d1.budget_no AND d.seq_No = d1.seq_No" +
                "    LEFT JOIN SYNC_OU ou ON m.oucode = ou.OU_CODE" +
                "    LEFT JOIN NCCC_ACCOUNTING_LIST ac ON d.accounting = ac.SUBJECT WHERE year=:year AND version=:version",nativeQuery = true)
    List<BudgetVo.BudgetOperateItem> GetOperateItemsByYearAndVersion(@Param("year") String year,@Param("version") String version);

    @Query("SELECT n FROM NcccPreBudgetM n WHERE n.budgetNo IN :batches")
    List<NcccPreBudgetM>  GetByBatchBudgetNoList(@Param("batches") List<String> batches);

    /**
     * 取得對應固定資產資料
     */
    @Query(value = "SELECT" +
            "    a.ASSETS_CODE AS assetsCode," +
            "    a.ASSETS_NAME AS assetsName," +
            "    a.ASSETS_TYPE AS assetsType," +
            "    a.OUCODE AS ouCode," +
            "    ac.ACCUMULATED_DEPRECIATION_ACCOUNT AS accounting," +
            "    al.ESSAY AS accountingName" +
            "    FROM NCCC_ASSETS a" +
            "    LEFT JOIN ASSET_CATEGORY ac ON SUBSTR(a.ASSETS_TYPE, -5) = ac.ASSET_CATEGORY" +
            "    LEFT JOIN NCCC_ACCOUNTING_LIST al ON al.SUBJECT = ac.ACCUMULATED_DEPRECIATION_ACCOUNT WHERE a.ASSETS_CODE IN :batches",nativeQuery = true)
    List<BudgetVo.BudgetSAPDepreciationAssetAccountingOuCodeData> GetSAPDepreciationAssetAboutData(@Param("batches") List<String> batches);

    /**
     * 取得對應固定資產作業項目資料
     */
    @Query(value = "SELECT" +
            "    d.ASSETS_CODE AS assetsCode," +
            "    d.OPERATE_ITEM_CODE AS operateItemCode," +
            "    d.OPERATE_ITEM AS operateItem," +
            "    d.OPERATE_RATIO AS operateRatio" +
            "    FROM NCCC_ASSETS_D d WHERE d.ASSETS_CODE IN :batches",nativeQuery = true)
    List<BudgetVo.BudgetSAPDepreciationAssetOperateData> GetSAPDepreciationAssetOperateData(@Param("batches") List<String> batches);


}
