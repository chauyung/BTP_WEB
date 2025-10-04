package nccc.btp.repository;

import nccc.btp.entity.BpmTaskItem;
import nccc.btp.vo.BpmExMDetailSplitTaskItemVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BpmTaskItemRepository extends JpaRepository<BpmTaskItem, Long> {

    @Query("SELECT n.operateItemCode as code, oi.operateItem as name, n.description as description,n.remark as remark, n.itemText as itemText, n.untaxAmount as amount , n.operateRatio as ratio FROM BpmTaskItem n LEFT JOIN NcccOperateItems oi ON oi.operateItemCode = n.operateItemCode WHERE n.exNo=:exNo AND n.exItemNo=:exItemNo AND n.exItemSplitNo=:exItemSplitNo")
    List<BpmExMDetailSplitTaskItem> findByExNoAndExItemNoAndExItemSplitNo(@Param("exNo") String exNo,@Param("exItemNo") String exItemNo,@Param("exItemSplitNo") String exItemSplitNo);

    @Modifying
    @Query("DELETE FROM BpmTaskItem n WHERE n.exNo=:exNo AND n.exItemNo=:exItemNo")
    void deleteByExNoAndExItemNo(@Param("exNo") String exNo,@Param("exItemNo") String exItemNo);

    @Modifying
    @Query("DELETE FROM BpmTaskItem n WHERE n.exNo=:exNo")
    void deleteByExNo(@Param("exNo") String exNo);

    interface BpmExMDetailSplitTaskItem
    {
        String getCode();

        String getName();

        BigDecimal getAmount();

        BigDecimal getRatio();

        String getDescription();

        String getRemark();

        String getItemText();
    }

    interface TaskItemRow {
        String getOperateItemCode();
        String getOperateItem();
        BigDecimal getUnTaxAmount();
    }

    @Query(value =
            "SELECT t.OPERATE_ITEM_CODE AS operateItemCode, " +
                    "       NVL(t.ITEM_TEXT, t.OPERATE_ITEM_CODE) AS operateItem, " +
                    "       NVL(t.UNTAX_AMOUNT,0) AS unTaxAmount " +
                    "  FROM BPM_TASK_ITEM t " +
                    " WHERE t.EX_NO = :exNo " +
                    "   AND t.EX_ITEM_NO  = :mId " +
                    "   AND NVL(t.UNTAX_AMOUNT,0) <> 0",
            nativeQuery = true)
    List<TaskItemRow> findItems(@Param("exNo") String exNo, @Param("mId") String mId);

}
