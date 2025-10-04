package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccUtilityBudgetAttribution;
import nccc.btp.repository.NcccUtilityBudgetAttributionRepository;
import nccc.btp.service.NcccUtilityBudgetAttributionService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class NcccUtilityBudgetAttributionServiceImpl implements NcccUtilityBudgetAttributionService {

	protected static Logger LOG = LoggerFactory.getLogger(NcccUtilityBudgetAttributionServiceImpl.class);

	@Autowired
	private NcccUtilityBudgetAttributionRepository ncccUtilityBudgetAttributionRepository;

	@Override
	public List<NcccUtilityBudgetAttribution> findAll() {
		return ncccUtilityBudgetAttributionRepository.findAll(Sort.by(Sort.Direction.ASC, "phone"));
	}

	@Override
	public NcccUtilityBudgetAttribution add(NcccUtilityBudgetAttribution ncccUtilityBudgetAttribution) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccUtilityBudgetAttribution.setUpdateUser(user.getUserId());
		return ncccUtilityBudgetAttributionRepository.save(ncccUtilityBudgetAttribution);
	}

	@Override
	public NcccUtilityBudgetAttribution update(NcccUtilityBudgetAttribution ncccUtilityBudgetAttribution) {
		if (!ncccUtilityBudgetAttributionRepository.existsById(ncccUtilityBudgetAttribution.getPhone())) {
			throw new RuntimeException(ncccUtilityBudgetAttribution.getPhone() + " not found");
		}
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccUtilityBudgetAttribution.setUpdateUser(user.getUserId());
		return ncccUtilityBudgetAttributionRepository.save(ncccUtilityBudgetAttribution);
	}

	@Override
	public String delete(String phone) {
		if (!ncccUtilityBudgetAttributionRepository.existsById(phone)) {
			throw new RuntimeException(phone + " not found");
		}
		ncccUtilityBudgetAttributionRepository.deleteById(phone);
		return "deleted successfully";
	}
}
