package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.NcccExpenseCategoryNumber;

public interface NcccExpenseCategoryNumberRepository extends JpaRepository<NcccExpenseCategoryNumber, String> {

    NcccExpenseCategoryNumber findByAccounting(String accounting);

    NcccExpenseCategoryNumber findByCategoryNumber(String categoryNumber);
}
