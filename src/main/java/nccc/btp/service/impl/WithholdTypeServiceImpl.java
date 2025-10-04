package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccWithholdType;
import nccc.btp.repository.NcccWithholdTypeRepository;
import nccc.btp.service.WithholdTypeService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class WithholdTypeServiceImpl implements WithholdTypeService {

	protected static Logger LOG = LoggerFactory.getLogger(WithholdTypeServiceImpl.class);

	@Autowired
	private NcccWithholdTypeRepository ncccWithholdTypeRepository;

	@Override
	public List<NcccWithholdType> findAll() {
		return ncccWithholdTypeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@Override
	public NcccWithholdType add(NcccWithholdType ncccWithholdType) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccWithholdType.setUpdateUser(user.getUserId());
		return ncccWithholdTypeRepository.save(ncccWithholdType);
	}

	@Override
	public NcccWithholdType update(NcccWithholdType ncccWithholdType) {
		if (!ncccWithholdTypeRepository.existsById(ncccWithholdType.getId())) {
			throw new RuntimeException(ncccWithholdType.getId() + " not found");
		}
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccWithholdType.setUpdateUser(user.getUserId());
		return ncccWithholdTypeRepository.save(ncccWithholdType);
	}

	@Override
	public String delete(Long id) {
		if (!ncccWithholdTypeRepository.existsById(id)) {
			throw new RuntimeException(id + " not found");
		}
		ncccWithholdTypeRepository.deleteById(id);
		return "deleted successfully";
	}
}
