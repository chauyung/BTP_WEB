package nccc.btp.repository;

import nccc.btp.entity.NcccProvisionExpenseD;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NcccProvisionExpenseDRepository extends JpaRepository<NcccProvisionExpenseD, NcccProvisionExpenseD.ConfigId> {

    /**
     * 刪除預算提列明細
     * @param provisionNo
     */
    @Modifying
    @Query("delete from NcccProvisionExpenseD where provisionNo = :provisionNo")
    void deleteByProvisionNo(@Param("provisionNo") String provisionNo);

    /**
     * 查詢預算提列明細
     * @param provisionNo
     * @return
     */
    @Query("SELECT n FROM NcccProvisionExpenseD n WHERE n.provisionNo=:provisionNo")
    List<NcccProvisionExpenseD> findByProvisionNo(@Param("provisionNo") String provisionNo);
    
}
