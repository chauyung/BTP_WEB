package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccAssets;

public interface NcccAssetsService {

	List<NcccAssets> findAll();

	NcccAssets add(NcccAssets ncccAssets);

	NcccAssets update(NcccAssets ncccAssets);

	String delete(String description);
}
