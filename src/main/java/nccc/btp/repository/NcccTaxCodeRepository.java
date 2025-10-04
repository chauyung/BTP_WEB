package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.NcccTaxCode;

public interface NcccTaxCodeRepository extends JpaRepository<NcccTaxCode, String> {


}
