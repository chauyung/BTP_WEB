package nccc.btp.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.BpmRevMD;

public interface BpmRevMDDRepository extends JpaRepository<BpmRevMD, String> {
	interface DesignatedItem {
	    String getOperateItemCode();
	    String getOperateItem();
	    java.math.BigDecimal getTotal();
	  }

	  @Query(value =
	    "SELECT x.OPERATE_ITEM_CODE AS operateItemCode, " +
	    "       x.OPERATE_ITEM      AS operateItem, " +
	    "       NVL(x.TOTAL,0)      AS total " +
	    "  FROM BPM_REV_M_D_D x " +
	    " WHERE x.REV_NO      = :revNo " +
	    "   AND x.PO_ITEM_NO  = :poItemNo " +
	    "   AND x.REV_ITEM_NO = :revItemNo " +
	    "   AND NVL(x.TOTAL,0) <> 0",
	    nativeQuery = true)
	  List<DesignatedItem> findItems(@Param("revNo") String revNo,
	                                 @Param("poItemNo") String poItemNo,
	                                 @Param("revItemNo") String revItemNo);
}