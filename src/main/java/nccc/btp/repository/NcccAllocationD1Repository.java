package nccc.btp.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import nccc.btp.entity.NcccAllocationD1;

public interface NcccAllocationD1Repository extends JpaRepository<NcccAllocationD1, NcccAllocationD1.ConfigId> {

    @Query("SELECT a FROM NcccAllocationD1 a WHERE a.poNo = :poNo AND a.poItemNo = :poItemNo")
    List<NcccAllocationD1> findByPoNoAndPoItemNo(@Param("poNo") String poNo,@Param("poItemNo") String poItemNo);

    /**
     * 刪除指定條件的所有明細資料
     * @param poNo 採購單編號
     * @param poItemNo 採購單項次
     */
    @Query("DELETE FROM NcccAllocationD1 d WHERE d.poNo = :poNo AND d.poItemNo = :poItemNo")
    @Modifying
    void deleteByPoNoAndPoItemNo(@Param("poNo") String poNo,@Param("poItemNo") String poItemNo);
    
    interface OperItemRatio {
        String getOperateItemCode();
        String getOperateItem();
        BigDecimal getOperateRatio();
    }

    @Query("select d1.operateItemCode as operateItemCode," +
           "       d1.operateItem     as operateItem," +
           "       d1.operateRatio    as operateRatio " +
           "  from NcccAllocationD1 d1 " +
           " where d1.poNo = :poNo and d1.poItemNo = :poItemNo " +
           " order by d1.seqNo1")
    List<OperItemRatio> findRatiosByPo(@Param("poNo") String poNo,
                                       @Param("poItemNo") String poItemNo);
    
    
}
