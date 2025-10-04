package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccCashController;
import nccc.btp.repository.NcccCashControllerRepository;
import nccc.btp.service.CashControllerService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class CashControllerServiceImpl implements CashControllerService {

	protected static Logger LOG = LoggerFactory.getLogger(CashControllerServiceImpl.class);

	@Autowired
	private NcccCashControllerRepository ncccCashControllerRepository;

	@Override
	public List<NcccCashController> findAll() {
		return ncccCashControllerRepository.findAll(Sort.by(Sort.Direction.ASC, "cashControllerNo"));
	}

	@Override
	public NcccCashController add(NcccCashController ncccCashController) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccCashController.setUpdateUser(user.getUserId());
		return ncccCashControllerRepository.save(ncccCashController);
	}

	@Override
	public NcccCashController update(NcccCashController ncccCashController) {
		if (!ncccCashControllerRepository.existsById(ncccCashController.getCashControllerNo())) {
			throw new RuntimeException(ncccCashController.getCashControllerNo() + " not found");
		}
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccCashController.setUpdateUser(user.getUserId());
		return ncccCashControllerRepository.save(ncccCashController);
	}

	@Override
	public String delete(String cashControllerNo) {
		if (!ncccCashControllerRepository.existsById(cashControllerNo)) {
			throw new RuntimeException(cashControllerNo + " not found");
		}
		ncccCashControllerRepository.deleteById(cashControllerNo);
		return "deleted successfully";
	}
}
