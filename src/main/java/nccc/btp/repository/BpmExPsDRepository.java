package nccc.btp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.BpmExPsD;

public interface BpmExPsDRepository extends JpaRepository<BpmExPsD, Long> {

    List<BpmExPsD> findByExNo(String exNo);

    int deleteByExNo(String exNo);
}
