package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.NcccCostcenterOrgMapping;

public interface NcccCostcenterOrgMappingRepository extends JpaRepository<NcccCostcenterOrgMapping, String> {

    NcccCostcenterOrgMapping findByDepNameAct(String depNameAct);

    NcccCostcenterOrgMapping findByCostcenter(String costcenter);


    NcccCostcenterOrgMapping findByHrDepCodeAct(String hrDepCodeAct);
}
