package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccExpenseCategoryNumber;

public interface NcccExpenseCategoryNumberService {

	List<NcccExpenseCategoryNumber> findAll();

	NcccExpenseCategoryNumber add(NcccExpenseCategoryNumber ncccExpenseCategoryNumber);

	NcccExpenseCategoryNumber update(NcccExpenseCategoryNumber ncccExpenseCategoryNumber);

	String delete(String categoryNumber);
}
