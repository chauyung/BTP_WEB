package nccc.btp.repository;

import nccc.btp.entity.BpmExPcD1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BpmExPcD1Repository extends JpaRepository<BpmExPcD1, Long> {

    @Query("SELECT n FROM BpmExPcD1 n WHERE n.finish=:finish")
    List<BpmExPcD1> findByFinish(@Param("finish") String finish);

    BpmExPcD1 findByExNoAndExItemNo(@Param("exNo") String exNo, @Param("exItemNo") String exItemNo);
}
