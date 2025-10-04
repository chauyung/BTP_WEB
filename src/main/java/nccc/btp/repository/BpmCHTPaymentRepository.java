package nccc.btp.repository;

import java.util.List;
import nccc.btp.entity.BpmCHTPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BpmCHTPaymentRepository  extends JpaRepository<BpmCHTPayment, Long> {

    BpmCHTPayment findByExNoAndPhone(String exNo, String phone);

    List<BpmCHTPayment> findByExNo(String exNo);

    boolean existsByExNo(String exNo);
}
