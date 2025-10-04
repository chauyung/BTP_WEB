package nccc.btp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.NcccNssfvoueInvoice;

public interface NcccNssfvoueInvoiceRepository extends JpaRepository<NcccNssfvoueInvoice, String> {

  List<NcccNssfvoueInvoice> findByXa001(String nssfvoueHeaderBatch);
}
