package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import nccc.btp.entity.NcccPurchaseCategoryNumber;

@Repository
public interface NcccPurchaseCategoryNumberRepository extends JpaRepository<NcccPurchaseCategoryNumber, String> {

  NcccPurchaseCategoryNumber findByCategoryNumber(String itemCode);


}
