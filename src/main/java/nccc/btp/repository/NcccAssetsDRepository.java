// ===================== Repository: NCCC_ASSETS_D =====================
package nccc.btp.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.NcccAssetsD;

public interface NcccAssetsDRepository extends JpaRepository<NcccAssetsD, Long> {

    interface OperItemRatio {
        String getOperateItemCode();     // 作業項目代號
        String getOperateItem();         // 作業項目名稱(由 NCCC_OPERATE_ITEMS 連接)
        BigDecimal getOperateRatio();    // 百分比
    }

    @Query(value =
        "SELECT d.OPERATE_ITEM_CODE AS operateItemCode, " +
        "       NVL(oi.OPERATE_ITEM, d.OPERATE_ITEM_CODE) AS operateItem, " +
        "       NVL(d.OPERATE_RATIO, 0) AS operateRatio " +
        "  FROM NCCC_ASSETS_D d " +
        "  LEFT JOIN NCCC_OPERATE_ITEMS oi ON oi.OPERATE_ITEM_CODE = d.OPERATE_ITEM_CODE " +
        " WHERE d.ASSETS_CODE = :assetsCode",
        nativeQuery = true)
    List<OperItemRatio> findRatiosByAssets(@Param("assetsCode") String assetsCode);
    
    interface OperItemAmt {
        String getOperateItemCode();
        String getOperateItem();
        java.math.BigDecimal getOperateAmt();
    }

    @Query(value =
        "SELECT OPERATE_ITEM_CODE AS operateItemCode, " +
        "       OPERATE_ITEM      AS operateItem, " +
        "       NVL(OPERATE_AMT,0) AS operateAmt " +
        "  FROM NCCC_ASSETS_D " +
        " WHERE ASSETS_CODE = :assetsCode " +
        "   AND NVL(OPERATE_AMT,0) <> 0",
        nativeQuery = true)
    List<OperItemAmt> findAmtsByAssets(@Param("assetsCode") String assetsCode);
}
