package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccAssets;
import nccc.btp.repository.NcccAssetsRepository;
import nccc.btp.service.NcccAssetsService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class NcccAssetsServiceImpl implements NcccAssetsService {

	protected static Logger LOG = LoggerFactory.getLogger(NcccAssetsServiceImpl.class);

	@Autowired
	private NcccAssetsRepository ncccAssetsRepository;

	@Override
	public List<NcccAssets> findAll() {
		return ncccAssetsRepository.findAll(Sort.by(Sort.Direction.ASC, "description"));
	}

	@Override
	public NcccAssets add(NcccAssets ncccAssets) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccAssets.setUpdateUser(user.getUserId());
		return ncccAssetsRepository.save(ncccAssets);
	}

	@Override
	public NcccAssets update(NcccAssets ncccAssets) {
		if (!ncccAssetsRepository.existsById(ncccAssets.getOperateItemCode())) {
			throw new RuntimeException(ncccAssets.getOperateItemCode() + " not found");
		}
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccAssets.setUpdateUser(user.getUserId());
		return ncccAssetsRepository.save(ncccAssets);
	}

	@Override
	public String delete(String operateItemCode) {
		if (!ncccAssetsRepository.existsById(operateItemCode)) {
			throw new RuntimeException(operateItemCode + " not found");
		}
		ncccAssetsRepository.deleteById(operateItemCode);
		return "deleted successfully";
	}
}
