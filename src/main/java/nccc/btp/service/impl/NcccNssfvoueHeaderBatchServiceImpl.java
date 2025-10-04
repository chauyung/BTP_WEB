package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccNssfvoueHeaderBatch;
import nccc.btp.repository.NcccNssfvoueHeaderBatchRepository;
import nccc.btp.service.NcccNssfvoueHeaderBatchService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class NcccNssfvoueHeaderBatchServiceImpl implements NcccNssfvoueHeaderBatchService {

	protected static Logger LOG = LoggerFactory.getLogger(NcccNssfvoueHeaderBatchServiceImpl.class);

	@Autowired
	private NcccNssfvoueHeaderBatchRepository ncccNssfvoueHeaderBatchRepository;

	@Override
	public List<NcccNssfvoueHeaderBatch> findAll() {
		return ncccNssfvoueHeaderBatchRepository.findAll(Sort.by(Sort.Direction.ASC, "nssfvoueHeaderBatch"));
	}

	@Override
	public NcccNssfvoueHeaderBatch add(NcccNssfvoueHeaderBatch ncccNssfvoueHeaderBatch) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccNssfvoueHeaderBatch.setUpdateUser(user.getUserId());
		return ncccNssfvoueHeaderBatchRepository.save(ncccNssfvoueHeaderBatch);
	}

	@Override
	public NcccNssfvoueHeaderBatch update(NcccNssfvoueHeaderBatch ncccNssfvoueHeaderBatch) {
		if (!ncccNssfvoueHeaderBatchRepository.existsById(ncccNssfvoueHeaderBatch.getNssfvoueHeaderBatch())) {
			throw new RuntimeException(ncccNssfvoueHeaderBatch.getNssfvoueHeaderBatch() + " not found");
		}
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccNssfvoueHeaderBatch.setUpdateUser(user.getUserId());
		return ncccNssfvoueHeaderBatchRepository.save(ncccNssfvoueHeaderBatch);
	}

	@Override
	public String delete(String nssfvoueHeaderBatch) {
		if (!ncccNssfvoueHeaderBatchRepository.existsById(nssfvoueHeaderBatch)) {
			throw new RuntimeException(nssfvoueHeaderBatch + " not found");
		}
		ncccNssfvoueHeaderBatchRepository.deleteById(nssfvoueHeaderBatch);
		return "deleted successfully";
	}
}
