package nccc.btp.repository;

import nccc.btp.entity.BpmPrepaidOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BpmPrepaidOrderRepository extends JpaRepository<BpmPrepaidOrder, Long> {

    BpmPrepaidOrder findByExNo(String exNo);
}
