package nccc.btp.repository;

import java.util.List;
import nccc.btp.entity.NcccApportionmentRuleM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NcccApportionmentRuleMRepository
        extends JpaRepository<NcccApportionmentRuleM, NcccApportionmentRuleM.ConfigId> {

    /** 依 年+月 取當月規則主檔 */
    @Query(value =
        "SELECT * FROM NCCC_APPORTIONMENT_RULE_M " +
        " WHERE YEAR = :year AND MONTH = :month",
        nativeQuery = true)
    List<NcccApportionmentRuleM> findByYearAndMonth(@Param("year") String year,
                                                    @Param("month") String month);

    /**
     * 根據年度、分攤代號、會計科目、預算來源部門查詢資料
     */
    @Query("SELECT m FROM NcccApportionmentRuleM m WHERE m.year = :year AND m.month = :month AND m.accounting = :accounting")
    NcccApportionmentRuleM findByYearAndMonthAndAccounting(
           @Param("year") String year,
           @Param("month") String month,
           @Param("accounting") String accounting);
    /** 依 年+月+會科 取單筆（優先當月；服務層需自行回退 '00' 月） */
    @Query(value =
        "SELECT * FROM NCCC_APPORTIONMENT_RULE_M " +
        " WHERE TRIM(YEAR)=:yy AND TRIM(MONTH)=:mm AND TRIM(ACCOUNTING)=:acc " +
        "   AND ROWNUM = 1",
        nativeQuery = true)
    NcccApportionmentRuleM findByYMA(@Param("yy") String year,
                                     @Param("mm") String month,
                                     @Param("acc") String accounting);

    /** 依 年+會科 取最新一筆（UPDATE_DATE→CREATE_DATE） */
    @Query(value =
        "SELECT * FROM (" +
        "  SELECT m.* FROM NCCC_APPORTIONMENT_RULE_M m " +
        "   WHERE m.YEAR = :year AND m.ACCOUNTING = :accounting " +
        "   ORDER BY m.UPDATE_DATE DESC NULLS LAST, m.CREATE_DATE DESC NULLS LAST" +
        ") WHERE ROWNUM = 1",
        nativeQuery = true)
    NcccApportionmentRuleM findLatestByYearAndAccounting(@Param("year") String year,
                                                         @Param("accounting") String accounting);

    /** 只取 OPERATION_TYPE 投影，用於快速判斷（1=人數, 2=金額） */
    interface RuleM { String getOperationType(); }

    @Query(value =
        "SELECT m.OPERATION_TYPE AS operationType " +
        "  FROM NCCC_APPORTIONMENT_RULE_M m " +
        " WHERE TRIM(m.YEAR)=:yy AND TRIM(m.MONTH)=:mm AND TRIM(m.ACCOUNTING)=:acc " +
        " FETCH FIRST 1 ROWS ONLY",
        nativeQuery = true)
    RuleM findRuleM(@Param("yy") String year,
                    @Param("mm") String month,
                    @Param("acc") String accounting);
    
    // after_rules 用：限定 ACTUAL_QTY_FLAG='Y'
    List<NcccApportionmentRuleM> findByYearAndMonthAndActualQtyFlag(String year, String month, String actualQtyFlag);

    
}
