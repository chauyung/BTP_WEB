package nccc.btp.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.NcccIncomeTaxCategory;
import nccc.btp.entity.NcccIncomeTaxCategoryId;

public interface NcccIncomeTaxCategoryRepository
    extends JpaRepository<NcccIncomeTaxCategory, NcccIncomeTaxCategoryId> {

  List<NcccIncomeTaxCategory> findByIdTaxCategory(String taxCategoory, Sort sort);
}
