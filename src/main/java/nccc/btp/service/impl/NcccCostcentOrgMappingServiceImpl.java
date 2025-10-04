package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccCostcentOrgMapping;
import nccc.btp.repository.NcccCostcentOrgMappingRepository;
import nccc.btp.service.NcccCostcentOrgMappingService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class NcccCostcentOrgMappingServiceImpl implements NcccCostcentOrgMappingService {

	protected static Logger LOG = LoggerFactory.getLogger(NcccCostcentOrgMappingServiceImpl.class);

	@Autowired
	private NcccCostcentOrgMappingRepository ncccCostcentOrgMappingRepository;

	@Override
	public List<NcccCostcentOrgMapping> findAll() {
		return ncccCostcentOrgMappingRepository.findAll(Sort.by(Sort.Direction.ASC, "hrDepCodeAct"));
	}

	@Override
	public NcccCostcentOrgMapping add(NcccCostcentOrgMapping ncccCostcentOrgMapping) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccCostcentOrgMapping.setUpdateUser(user.getUserId());
		return ncccCostcentOrgMappingRepository.save(ncccCostcentOrgMapping);
	}

	@Override
	public NcccCostcentOrgMapping update(NcccCostcentOrgMapping ncccCostcentOrgMapping) {
		if (!ncccCostcentOrgMappingRepository.existsById(ncccCostcentOrgMapping.getHrDepCodeAct())) {
			throw new RuntimeException(ncccCostcentOrgMapping.getHrDepCodeAct() + " not found");
		}
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccCostcentOrgMapping.setUpdateUser(user.getUserId());
		return ncccCostcentOrgMappingRepository.save(ncccCostcentOrgMapping);
	}

	@Override
	public String delete(String hrDepCodeAct) {
		if (!ncccCostcentOrgMappingRepository.existsById(hrDepCodeAct)) {
			throw new RuntimeException(hrDepCodeAct + " not found");
		}
		ncccCostcentOrgMappingRepository.deleteById(hrDepCodeAct);
		return "deleted successfully";
	}
}
