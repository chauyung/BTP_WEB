package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccCostCenter;
import nccc.btp.repository.NcccCostCenterRepository;
import nccc.btp.service.CostCenterService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class CostCenterServiceImpl implements CostCenterService {

	protected static Logger LOG = LoggerFactory.getLogger(CostCenterServiceImpl.class);

	@Autowired
	private NcccCostCenterRepository ncccCostCenterRepository;

	@Override
	public List<NcccCostCenter> findAll() {
		return ncccCostCenterRepository.findAll(Sort.by(Sort.Direction.ASC, "kostl"));
	}

	@Override
	public NcccCostCenter add(NcccCostCenter ncccCostCenter) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccCostCenter.setUpdateUser(user.getUserId());
		return ncccCostCenterRepository.save(ncccCostCenter);
	}

	@Override
	public NcccCostCenter update(NcccCostCenter ncccCostCenter) {
		if (!ncccCostCenterRepository.existsById(ncccCostCenter.getKostl())) {
			throw new RuntimeException(ncccCostCenter.getKostl() + " not found");
		}
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccCostCenter.setUpdateUser(user.getUserId());
		return ncccCostCenterRepository.save(ncccCostCenter);
	}

	@Override
	public String delete(String kostl) {
		if (!ncccCostCenterRepository.existsById(kostl)) {
			throw new RuntimeException(kostl + " not found");
		}
		ncccCostCenterRepository.deleteById(kostl);
		return "deleted successfully";
	}
}
