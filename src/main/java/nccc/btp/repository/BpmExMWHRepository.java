package nccc.btp.repository;

import nccc.btp.entity.BpmExMWH;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BpmExMWHRepository extends JpaRepository<BpmExMWH,Long>{

    List<BpmExMWH> findAllByExNo(String exNo);

    int deleteByExNo(String exNo);

    @Modifying
    @Query("UPDATE BpmExMWH b SET b.payDate = :payDate,b.itemWHText = :itemWHText WHERE b.exNo = :exNo  AND b.id = :id")
    int updateFields(@Param("exNo") String exNo, @Param("id") Long id, @Param("itemWHText") String itemWHText, @Param("payDate") String payDate);
}
