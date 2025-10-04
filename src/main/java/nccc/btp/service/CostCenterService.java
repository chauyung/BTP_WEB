package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccCostCenter;

public interface CostCenterService {

  List<NcccCostCenter> findAll();

  NcccCostCenter add(NcccCostCenter ncccCostCenter);

  NcccCostCenter update(NcccCostCenter ncccCostCenter);

  String delete(String kostl);
}
