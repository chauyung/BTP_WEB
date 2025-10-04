package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccCostcentOrgMapping;

public interface NcccCostcentOrgMappingService {

	List<NcccCostcentOrgMapping> findAll();

	NcccCostcentOrgMapping add(NcccCostcentOrgMapping ncccCostcentOrgMapping);

	NcccCostcentOrgMapping update(NcccCostcentOrgMapping ncccCostcentOrgMapping);

	String delete(String hrDepCodeAct);
}
