package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccCurrency;
import nccc.btp.repository.NcccCurrencyRepository;
import nccc.btp.service.CurrencyService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class CurrencyServiceImpl implements CurrencyService {

	protected static Logger LOG = LoggerFactory.getLogger(CurrencyServiceImpl.class);

	@Autowired
	private NcccCurrencyRepository ncccCurrencyRepository;

	@Override
	public List<NcccCurrency> findAll() {
		return ncccCurrencyRepository.findAll(Sort.by(Sort.Direction.ASC, "waers"));
	}

	@Override
	public NcccCurrency add(NcccCurrency ncccCurrency) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccCurrency.setUpdateUser(user.getUserId());
		return ncccCurrencyRepository.save(ncccCurrency);
	}

	@Override
	public NcccCurrency update(NcccCurrency ncccCurrency) {
		if (!ncccCurrencyRepository.existsById(ncccCurrency.getWaers())) {
			throw new RuntimeException(ncccCurrency.getWaers() + " not found");
		}
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccCurrency.setUpdateUser(user.getUserId());
		return ncccCurrencyRepository.save(ncccCurrency);
	}

	@Override
	public String delete(String waers) {
		if (!ncccCurrencyRepository.existsById(waers)) {
			throw new RuntimeException(waers + " not found");
		}
		ncccCurrencyRepository.deleteById(waers);
		return "deleted successfully";
	}
}
