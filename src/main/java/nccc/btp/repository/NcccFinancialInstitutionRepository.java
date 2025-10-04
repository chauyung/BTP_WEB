package nccc.btp.repository;

import nccc.btp.entity.NcccFinancialInstitution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NcccFinancialInstitutionRepository extends JpaRepository<NcccFinancialInstitution, String> {
}
