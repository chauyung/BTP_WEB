package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccUtilityBudgetAttributionB;

public interface NcccUtilityBudgetAttributionBService {

    List<NcccUtilityBudgetAttributionB> findAll();

    NcccUtilityBudgetAttributionB add(NcccUtilityBudgetAttributionB ncccUtilityBudgetAttributionB);

    NcccUtilityBudgetAttributionB update(NcccUtilityBudgetAttributionB ncccUtilityBudgetAttributionB);

    String delete(String ncccUtilityBudgetAttributionB);
}
