package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccTaxCode;
import nccc.btp.repository.NcccTaxCodeRepository;
import nccc.btp.service.TaxCodeService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class TaxCodeServiceImpl implements TaxCodeService {

	protected static Logger LOG = LoggerFactory.getLogger(TaxCodeServiceImpl.class);

	@Autowired
	private NcccTaxCodeRepository ncccTaxCodeRepository;

	@Override
	public List<NcccTaxCode> findAll() {
		return ncccTaxCodeRepository.findAll(Sort.by(Sort.Direction.ASC, "taxCode"));
	}

	@Override
	public NcccTaxCode add(NcccTaxCode ncccTaxCode) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccTaxCode.setUpdateUser(user.getUserId());
		return ncccTaxCodeRepository.save(ncccTaxCode);
	}

	@Override
	public NcccTaxCode update(NcccTaxCode ncccTaxCode) {
		if (!ncccTaxCodeRepository.existsById(ncccTaxCode.getTaxCode())) {
			throw new RuntimeException(ncccTaxCode.getTaxCode() + " not found");
		}
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccTaxCode.setUpdateUser(user.getUserId());
		return ncccTaxCodeRepository.save(ncccTaxCode);
	}

	@Override
	public String delete(String taxCode) {
		if (!ncccTaxCodeRepository.existsById(taxCode)) {
			throw new RuntimeException(taxCode + " not found");
		}
		ncccTaxCodeRepository.deleteById(taxCode);
		return "deleted successfully";
	}
}
