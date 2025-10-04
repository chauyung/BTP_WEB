package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccNssfvoueHeaderBatch;

public interface NcccNssfvoueHeaderBatchService {

	List<NcccNssfvoueHeaderBatch> findAll();

	NcccNssfvoueHeaderBatch add(NcccNssfvoueHeaderBatch ncccNssfvoueHeaderBatch);

	NcccNssfvoueHeaderBatch update(NcccNssfvoueHeaderBatch ncccNssfvoueHeaderBatch);

	String delete(String nssfvoueHeaderBatch);
}
