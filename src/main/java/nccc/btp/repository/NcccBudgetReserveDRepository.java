package nccc.btp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.NcccBudgetAdjustD;
import nccc.btp.entity.NcccBudgetReserveD;

public interface NcccBudgetReserveDRepository extends JpaRepository<NcccBudgetReserveD, NcccBudgetReserveD.ConfigId> {

    @Modifying
    @Query("delete from NcccBudgetReserveD where rsvNo = :rsvNo")
    void deleteByRsvNo(@Param("rsvNo") String rsvNo);

    List<NcccBudgetReserveD> findByRsvNo(@Param("rsvNo") String rsvNo);
    
}
