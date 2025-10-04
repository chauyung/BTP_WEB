package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccCostGroup;

public interface CostGroupService {

  List<NcccCostGroup> findAll();

  NcccCostGroup add(NcccCostGroup ncccCostGroup);

  NcccCostGroup update(NcccCostGroup ncccCostGroup);

  String delete(Long id);

}
