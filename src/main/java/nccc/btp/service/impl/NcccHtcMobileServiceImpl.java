package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccHtcMobile;
import nccc.btp.repository.NcccHtcMobileRepository;
import nccc.btp.service.NcccHtcMobileService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class NcccHtcMobileServiceImpl implements NcccHtcMobileService {

	protected static Logger LOG = LoggerFactory.getLogger(NcccHtcMobileServiceImpl.class);

	@Autowired
	private NcccHtcMobileRepository nccccHtcMobileRepository;

	@Override
	public List<NcccHtcMobile> findAll() {
		return nccccHtcMobileRepository.findAll(Sort.by(Sort.Direction.ASC, "notificationNumber"));
	}

	@Override
	public NcccHtcMobile add(NcccHtcMobile nccccHtcMobile) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		nccccHtcMobile.setUpdateUser(user.getUserId());
		return nccccHtcMobileRepository.save(nccccHtcMobile);
	}

	@Override
	public NcccHtcMobile update(NcccHtcMobile nccccHtcMobile) {
		if (!nccccHtcMobileRepository.existsById(nccccHtcMobile.getNotificationNumber())) {
			throw new RuntimeException(nccccHtcMobile.getNotificationNumber() + " not found");
		}
		NcccUserDto user = SecurityUtil.getCurrentUser();
		nccccHtcMobile.setUpdateUser(user.getUserId());
		return nccccHtcMobileRepository.save(nccccHtcMobile);
	}

	@Override
	public String delete(String notificationNumber) {
		if (!nccccHtcMobileRepository.existsById(notificationNumber)) {
			throw new RuntimeException(notificationNumber + " not found");
		}
		nccccHtcMobileRepository.deleteById(notificationNumber);
		return "deleted successfully";
	}
}
