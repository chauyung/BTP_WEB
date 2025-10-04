package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.ZmmtSupplier;
import nccc.btp.repository.ZmmtSupplierRepository;
import nccc.btp.service.ZmmtSupplierService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class ZmmtSupplierServiceImpl implements ZmmtSupplierService {

	protected static Logger LOG = LoggerFactory.getLogger(ZmmtSupplierServiceImpl.class);

	@Autowired
	private ZmmtSupplierRepository zmmtSupplierRepository;

	@Override
	public List<ZmmtSupplier> findAll() {
		return zmmtSupplierRepository.findAll(Sort.by(Sort.Direction.ASC, "partner"));
	}

	@Override
	public ZmmtSupplier add(ZmmtSupplier zmmtSupplier) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		zmmtSupplier.setUpdateUser(user.getUserId());
		return zmmtSupplierRepository.save(zmmtSupplier);
	}

	@Override
  public ZmmtSupplier update(ZmmtSupplier zmmtSupplier) {
    if (!zmmtSupplierRepository.existsById(zmmtSupplier.getPartner())) {
      throw new RuntimeException(zmmtSupplier.getPartner() + " not found");
    }
	NcccUserDto user = SecurityUtil.getCurrentUser();
    zmmtSupplier.setUpdateUser(user.getUserId());
    return zmmtSupplierRepository.save(zmmtSupplier);
  }

	@Override
	public String delete(String partner) {
		if (!zmmtSupplierRepository.existsById(partner)) {
			throw new RuntimeException(partner + " not found");
		}
		zmmtSupplierRepository.deleteById(partner);
		return "deleted successfully";
	}
}
