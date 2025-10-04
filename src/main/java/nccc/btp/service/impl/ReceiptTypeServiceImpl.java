package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccReceiptType;
import nccc.btp.repository.NcccReceiptTypeRepository;
import nccc.btp.service.ReceiptTypeService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class ReceiptTypeServiceImpl implements ReceiptTypeService {

  protected static Logger LOG = LoggerFactory.getLogger(ReceiptTypeServiceImpl.class);

  @Autowired
  private NcccReceiptTypeRepository ncccReceiptTypeRepository;

  @Override
  public List<NcccReceiptType> findAll() {
    return ncccReceiptTypeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
  }

  @Override
  public NcccReceiptType add(NcccReceiptType ncccReceiptType) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
    ncccReceiptType.setUpdateUser(user.getUserId());
    return ncccReceiptTypeRepository.save(ncccReceiptType);
  }

  @Override
  public NcccReceiptType update(NcccReceiptType ncccReceiptType) {
    if (!ncccReceiptTypeRepository.existsById(ncccReceiptType.getId())) {
      throw new RuntimeException(ncccReceiptType.getId() + " not found");
    }
	NcccUserDto user = SecurityUtil.getCurrentUser();
    ncccReceiptType.setUpdateUser(user.getUserId());
    return ncccReceiptTypeRepository.save(ncccReceiptType);
  }

  @Override
  public String delete(Long id) {
    if (!ncccReceiptTypeRepository.existsById(id)) {
      throw new RuntimeException(id + " not found");
    }
    ncccReceiptTypeRepository.deleteById(id);
    return "deleted successfully";
  }
}
