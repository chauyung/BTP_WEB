package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccCostAccount;

public interface CostAccountService {

  List<NcccCostAccount> findAll();

  NcccCostAccount add(NcccCostAccount ncccCostAccount);

  NcccCostAccount update(NcccCostAccount ncccCostAccount);

  String delete(Long id);
}
