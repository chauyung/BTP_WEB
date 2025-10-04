package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccAccountingList;

public interface NcccAccountingListService {

	List<NcccAccountingList> findAll();

	NcccAccountingList add(NcccAccountingList ncccAccountingList);

	NcccAccountingList update(NcccAccountingList ncccAccountingList);

	String delete(String subject);
}
