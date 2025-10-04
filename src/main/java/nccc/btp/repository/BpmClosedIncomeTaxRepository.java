package nccc.btp.repository;

import nccc.btp.entity.BpmClosedIncomeTax;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BpmClosedIncomeTaxRepository extends JpaRepository<BpmClosedIncomeTax, Long> {

    boolean existsByPaymentYear(String paymentYear);

    List<BpmClosedIncomeTax>  findByPaymentYear(String paymentYear);

    int deleteByPaymentYear(String paymentYear);
}
