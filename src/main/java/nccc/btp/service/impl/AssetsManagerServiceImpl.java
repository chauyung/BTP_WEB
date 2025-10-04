package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccAssetsManager;
import nccc.btp.repository.NcccAssetsManagerRepository;
import nccc.btp.service.AssetsManagerService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class AssetsManagerServiceImpl implements AssetsManagerService {

  protected static Logger LOG = LoggerFactory.getLogger(AssetsManagerServiceImpl.class);

  @Autowired
  private NcccAssetsManagerRepository ncccAssetsManagerRepository;

  @Override
  public List<NcccAssetsManager> findAll() {
    return ncccAssetsManagerRepository.findAll(Sort.by(Sort.Direction.ASC, "assetsCode"));
  }

  @Override
  public NcccAssetsManager add(NcccAssetsManager ncccAssetsManager) {
    NcccUserDto user = SecurityUtil.getCurrentUser();
    ncccAssetsManager.setUpdateUser(user.getUserId());
    return ncccAssetsManagerRepository.save(ncccAssetsManager);
  }

  @Override
  public NcccAssetsManager update(NcccAssetsManager ncccAssetsManager) {
    if (!ncccAssetsManagerRepository.existsById(ncccAssetsManager.getAssetsCode())) {
      throw new RuntimeException(ncccAssetsManager.getAssetsCode() + " not found");
    }
    NcccUserDto user = SecurityUtil.getCurrentUser();
    ncccAssetsManager.setUpdateUser(user.getUserId());
    return ncccAssetsManagerRepository.save(ncccAssetsManager);
  }

  @Override
  public String delete(String id) {
    if (!ncccAssetsManagerRepository.existsById(id)) {
      throw new RuntimeException(id + " not found");
    }
    ncccAssetsManagerRepository.deleteById(id);
    return "deleted successfully";
  }
}
