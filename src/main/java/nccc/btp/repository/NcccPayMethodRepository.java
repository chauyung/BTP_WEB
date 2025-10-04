package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.NcccPayMethod;

public interface NcccPayMethodRepository extends JpaRepository<NcccPayMethod, String> {


}
