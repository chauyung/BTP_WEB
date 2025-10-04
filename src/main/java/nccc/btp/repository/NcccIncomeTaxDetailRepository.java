package nccc.btp.repository;

import java.util.List;
import nccc.btp.entity.NcccIncomeTaxDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NcccIncomeTaxDetailRepository extends JpaRepository<NcccIncomeTaxDetail, Long> {

    List<NcccIncomeTaxDetail> findBypkMId(long pkMId);

    List<NcccIncomeTaxDetail> findByPaymentYear(String paymentYear);

    int deleteById(long id);
}
