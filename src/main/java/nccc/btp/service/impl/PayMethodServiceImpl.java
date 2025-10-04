package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccPayMethod;
import nccc.btp.repository.NcccPayMethodRepository;
import nccc.btp.service.PayMethodService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class PayMethodServiceImpl implements PayMethodService {

	protected static Logger LOG = LoggerFactory.getLogger(PayMethodServiceImpl.class);

	@Autowired
	private NcccPayMethodRepository ncccPayMethodRepository;

	@Override
	public List<NcccPayMethod> findAll() {
		return ncccPayMethodRepository.findAll(Sort.by(Sort.Direction.ASC, "zlsch"));
	}

	@Override
	public NcccPayMethod add(NcccPayMethod ncccPayMethod) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccPayMethod.setUpdateUser(user.getUserId());
		return ncccPayMethodRepository.save(ncccPayMethod);
	}

	@Override
	public NcccPayMethod update(NcccPayMethod ncccPayMethod) {
		if (!ncccPayMethodRepository.existsById(ncccPayMethod.getZlsch())) {
			throw new RuntimeException(ncccPayMethod.getZlsch() + " not found");
		}
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccPayMethod.setUpdateUser(user.getUserId());
		return ncccPayMethodRepository.save(ncccPayMethod);
	}

	@Override
	public String delete(String zlsch) {
		if (!ncccPayMethodRepository.existsById(zlsch)) {
			throw new RuntimeException(zlsch + " not found");
		}
		ncccPayMethodRepository.deleteById(zlsch);
		return "deleted successfully";
	}
}
