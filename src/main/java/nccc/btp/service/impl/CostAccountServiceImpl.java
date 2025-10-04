package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccCostAccount;
import nccc.btp.repository.NcccCostAccountRepository;
import nccc.btp.service.CostAccountService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class CostAccountServiceImpl implements CostAccountService {

	protected static Logger LOG = LoggerFactory.getLogger(CostAccountServiceImpl.class);

	@Autowired
	private NcccCostAccountRepository ncccCostAccountRepository;

	@Override
	public List<NcccCostAccount> findAll() {
		return ncccCostAccountRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@Override
	public NcccCostAccount add(NcccCostAccount ncccCostAccount) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccCostAccount.setUpdateUser(user.getUserId());
		ncccCostAccount.setId(ncccCostAccountRepository.getMaxId() + 1);
		return ncccCostAccountRepository.save(ncccCostAccount);
	}

	@Override
	public NcccCostAccount update(NcccCostAccount ncccCostAccount) {
		if (!ncccCostAccountRepository.existsById(ncccCostAccount.getId())) {
			throw new RuntimeException(ncccCostAccount.getId() + " not found");
		}
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccCostAccount.setUpdateUser(user.getUserId());
		return ncccCostAccountRepository.save(ncccCostAccount);
	}

	@Override
	public String delete(Long id) {
		if (!ncccCostAccountRepository.existsById(id)) {
			throw new RuntimeException(id + " not found");
		}
		ncccCostAccountRepository.deleteById(id);
		return "deleted successfully";
	}
}
