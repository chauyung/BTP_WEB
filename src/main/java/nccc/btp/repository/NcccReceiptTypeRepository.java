package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.NcccReceiptType;

public interface NcccReceiptTypeRepository extends JpaRepository<NcccReceiptType, Long> {

    NcccReceiptType findByIntyp(String intyp);
}
