package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccCostGroup;
import nccc.btp.repository.NcccCostGroupRepository;
import nccc.btp.service.CostGroupService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class CostGroupServiceImpl implements CostGroupService {

	protected static Logger LOG = LoggerFactory.getLogger(CostGroupServiceImpl.class);

	@Autowired
	private NcccCostGroupRepository ncccCostGroupRepository;

	@Override
	public List<NcccCostGroup> findAll() {
		return ncccCostGroupRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@Override
	public NcccCostGroup add(NcccCostGroup ncccCostGroup) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccCostGroup.setUpdateUser(user.getUserId());
		ncccCostGroup.setId(ncccCostGroupRepository.getMaxId() + 1);
		return ncccCostGroupRepository.save(ncccCostGroup);
	}

	@Override
	public NcccCostGroup update(NcccCostGroup ncccCostGroup) {
		if (!ncccCostGroupRepository.existsById(ncccCostGroup.getId())) {
			throw new RuntimeException(ncccCostGroup.getId() + " not found");
		}
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccCostGroup.setUpdateUser(user.getUserId());
		return ncccCostGroupRepository.save(ncccCostGroup);
	}

	@Override
	public String delete(Long id) {
		if (!ncccCostGroupRepository.existsById(id)) {
			throw new RuntimeException(id + " not found");
		}
		ncccCostGroupRepository.deleteById(id);
		 return "deleted successfully";
	}

}
