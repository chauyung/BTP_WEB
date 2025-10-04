package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.NcccCurrency;

public interface NcccCurrencyRepository extends JpaRepository<NcccCurrency, String> {


}
