package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccPurchaseCategoryNumber;
import nccc.btp.repository.NcccPurchaseCategoryNumberRepository;
import nccc.btp.service.NcccPurchaseCategoryNumberService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class NcccPurchaseCategoryNumberServiceImpl implements NcccPurchaseCategoryNumberService {

	protected static Logger LOG = LoggerFactory.getLogger(NcccPurchaseCategoryNumberServiceImpl.class);

	@Autowired
	private NcccPurchaseCategoryNumberRepository ncccPurchaseCategoryNumberRepository;

	@Override
	public List<NcccPurchaseCategoryNumber> findAll() {
		return ncccPurchaseCategoryNumberRepository.findAll(Sort.by(Sort.Direction.ASC, "categoryNumber"));
	}

	@Override
	public NcccPurchaseCategoryNumber add(NcccPurchaseCategoryNumber ncccPurchaseCategoryNumber) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccPurchaseCategoryNumber.setUpdateUser(user.getUserId());
		return ncccPurchaseCategoryNumberRepository.save(ncccPurchaseCategoryNumber);
	}

	@Override
	public NcccPurchaseCategoryNumber update(NcccPurchaseCategoryNumber ncccPurchaseCategoryNumber) {
		if (!ncccPurchaseCategoryNumberRepository.existsById(ncccPurchaseCategoryNumber.getCategoryNumber())) {
			throw new RuntimeException(ncccPurchaseCategoryNumber.getCategoryNumber() + " not found");
		}
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccPurchaseCategoryNumber.setUpdateUser(user.getUserId());
		return ncccPurchaseCategoryNumberRepository.save(ncccPurchaseCategoryNumber);
	}

	@Override
	public String delete(String categoryNumber) {
		if (!ncccPurchaseCategoryNumberRepository.existsById(categoryNumber)) {
			throw new RuntimeException(categoryNumber + " not found");
		}
		ncccPurchaseCategoryNumberRepository.deleteById(categoryNumber);
		return "deleted successfully";
	}
}
