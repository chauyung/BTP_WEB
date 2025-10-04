package nccc.btp.repository;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.NcccProvisionExpenseD1;


public interface NcccProvisionExpenseD1Repository extends JpaRepository<NcccProvisionExpenseD1, NcccProvisionExpenseD1.ConfigId> {

    /**
     * 刪除預算提列明細
     * @param provisionNo
     */
    @Modifying
    @Query("delete from NcccProvisionExpenseD1 where provisionNo = :provisionNo")
    void deleteByProvisionNo(@Param("provisionNo") String provisionNo);

    /**
     * 查詢預算提列明細
     * @param provisionNo
     * @param seqNo
     * @return
     */
    @Query("SELECT n FROM NcccProvisionExpenseD1 n WHERE n.provisionNo=:provisionNo AND n.seqNo=:seqNo")
    List<NcccProvisionExpenseD1> findByProvisionNo(@Param("provisionNo") String provisionNo,@Param("seqNo") String seqNo);
    
    // 金額版（before_provision 用）
    interface OperItemAmt {
        String getOperateItemCode();
        String getOperateItem();
        BigDecimal getOperateAmt();
    }

    @Query(value =
        "SELECT d1.OPERATE_ITEM_CODE AS operateItemCode, " +
        "       NVL(d1.OPERATE_ITEM, oi.OPERATE_ITEM) AS operateItem, " +
        "       NVL(d1.OPERATE_AMT, 0) AS operateAmt " +
        "  FROM NCCC_PROVISION_EXPENSE_D1 d1 " +
        "  LEFT JOIN NCCC_OPERATE_ITEMS oi " +
        "    ON oi.OPERATE_ITEM_CODE = d1.OPERATE_ITEM_CODE " +
        " WHERE d1.PROVISION_NO = :provisionNo " +
        "   AND d1.SEQ_NO       = :seqNo " +
        " ORDER BY d1.OPERATE_ITEM_CODE",
        nativeQuery = true)
    List<OperItemAmt> findItems(@Param("provisionNo") String provisionNo,
                                @Param("seqNo") String seqNo);

    // 比例版（after_provision 用）
    public interface D1Row {
        String getOperateItemCode();
        String getOperateItem();
        BigDecimal getOperateRatio(); // 百分比或小數
    }
    @Query(value =
        "SELECT d1.OPERATE_ITEM_CODE AS operateItemCode, " +
        "       oi.OPERATE_ITEM AS operateItem, " +
        "       d1.OPERATE_RATIO AS operateRatio " +
        "  FROM NCCC_PROVISION_EXPENSE_D1 d1 " +
        "  LEFT JOIN NCCC_OPERATE_ITEMS oi ON oi.OPERATE_ITEM_CODE=d1.OPERATE_ITEM_CODE " +
        " WHERE d1.PROVISION_NO=:no AND d1.SEQ_NO=:seq",
        nativeQuery = true)
    List<D1Row> findItemsRatio(@Param("no") String provisionNo, @Param("seq") String seqNo);
    
}
