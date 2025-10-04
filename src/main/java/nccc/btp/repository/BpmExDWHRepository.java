package nccc.btp.repository;

import nccc.btp.entity.BpmExDWH;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BpmExDWHRepository extends JpaRepository<BpmExDWH, Long> {

    List<BpmExDWH> findAllByExNo(String exNo);

    List<BpmExDWH> findAllByExNoAndExItemNo(String exNo, String exItemNo);

    int deleteByExNo(String exNo);
}
