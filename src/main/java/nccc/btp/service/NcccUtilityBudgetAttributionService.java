package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccUtilityBudgetAttribution;

public interface NcccUtilityBudgetAttributionService {

	List<NcccUtilityBudgetAttribution> findAll();

	NcccUtilityBudgetAttribution add(NcccUtilityBudgetAttribution ncccUtilityBudgetAttribution);

	NcccUtilityBudgetAttribution update(NcccUtilityBudgetAttribution ncccUtilityBudgetAttribution);

	String delete(String ncccUtilityBudgetAttribution);
}
