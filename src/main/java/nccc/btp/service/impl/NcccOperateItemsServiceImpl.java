package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccOperateItems;
import nccc.btp.repository.NcccOperateItemsRepository;
import nccc.btp.service.NcccOperateItemsService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class NcccOperateItemsServiceImpl implements NcccOperateItemsService {

  protected static Logger LOG = LoggerFactory.getLogger(NcccOperateItemsServiceImpl.class);

  @Autowired
  private NcccOperateItemsRepository ncccOperateItemsRepository;

  @Override
  public List<NcccOperateItems> findAll() {
    return ncccOperateItemsRepository.findAll(Sort.by(Sort.Direction.ASC, "operateItemCode"));
  }

  @Override
  public NcccOperateItems add(NcccOperateItems ncccOperateItems) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
	  ncccOperateItems.setUpdateUser(user.getUserId());
    return ncccOperateItemsRepository.save(ncccOperateItems);
  }

  @Override
  public NcccOperateItems update(NcccOperateItems ncccOperateItems) {
    if (!ncccOperateItemsRepository.existsById(ncccOperateItems.getOperateItemCode())) {
      throw new RuntimeException(ncccOperateItems.getOperateItemCode() + " not found");
    }
	NcccUserDto user = SecurityUtil.getCurrentUser();
    ncccOperateItems.setUpdateUser(user.getUserId());
    return ncccOperateItemsRepository.save(ncccOperateItems);
  }

  @Override
  public String delete(String operateItemCode) {
    if (!ncccOperateItemsRepository.existsById(operateItemCode)) {
      throw new RuntimeException(operateItemCode + " not found");
    }
    ncccOperateItemsRepository.deleteById(operateItemCode);
    return "deleted successfully";
  }
}
