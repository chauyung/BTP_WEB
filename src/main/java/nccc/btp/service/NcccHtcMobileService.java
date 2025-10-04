package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccHtcMobile;

public interface NcccHtcMobileService {

	List<NcccHtcMobile> findAll();

	NcccHtcMobile add(NcccHtcMobile ncccHtcMobile);

	NcccHtcMobile update(NcccHtcMobile ncccHtcMobile);

	String delete(String notificationNumber);
}
