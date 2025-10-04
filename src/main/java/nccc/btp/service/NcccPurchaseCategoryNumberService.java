package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccPurchaseCategoryNumber;

public interface NcccPurchaseCategoryNumberService {

	List<NcccPurchaseCategoryNumber> findAll();

	NcccPurchaseCategoryNumber add(NcccPurchaseCategoryNumber ncccPurchaseCategoryNumber);

	NcccPurchaseCategoryNumber update(NcccPurchaseCategoryNumber ncccPurchaseCategoryNumber);

	String delete(String categoryNumber);
}
