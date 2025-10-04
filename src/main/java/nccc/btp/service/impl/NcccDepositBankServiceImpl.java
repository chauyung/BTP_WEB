package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccDepositBank;
import nccc.btp.repository.NcccDepositBankRepository;
import nccc.btp.service.NcccDepositBankService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class NcccDepositBankServiceImpl implements NcccDepositBankService {

	protected static Logger LOG = LoggerFactory.getLogger(NcccDepositBankServiceImpl.class);

	@Autowired
	private NcccDepositBankRepository ncccDepositBankRepository;

	@Override
	public List<NcccDepositBank> findAll() {
		return ncccDepositBankRepository.findAll(Sort.by(Sort.Direction.ASC, "account"));
	}

	@Override
	public NcccDepositBank add(NcccDepositBank ncccDepositBank) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccDepositBank.setUpdateUser(user.getUserId());
		return ncccDepositBankRepository.save(ncccDepositBank);
	}

	@Override
	public NcccDepositBank update(NcccDepositBank ncccDepositBank) {
		if (!ncccDepositBankRepository.existsById(ncccDepositBank.getAccount())) {
			throw new RuntimeException(ncccDepositBank.getAccount() + " not found");
		}
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccDepositBank.setUpdateUser(user.getUserId());
		return ncccDepositBankRepository.save(ncccDepositBank);
	}

	@Override
	public String delete(String account) {
		if (!ncccDepositBankRepository.existsById(account)) {
			throw new RuntimeException(account + " not found");
		}
		ncccDepositBankRepository.deleteById(account);
		return "deleted successfully";
	}
}
