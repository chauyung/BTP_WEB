package nccc.btp.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nccc.btp.entity.SyncOU;
import nccc.btp.entity.SyncUser;
import nccc.btp.repository.SyncOURepository;
import nccc.btp.repository.SyncUserRepository;
import nccc.btp.service.OrganizationTreeService;

@Service
public class OrganizationTreeServiceImpl implements OrganizationTreeService {

  @Autowired
  SyncOURepository syncOURepository;
  @Autowired
  SyncUserRepository syncUserRepository;

  @Override
  public List<SyncOU> getOrganizationTree() {
    return syncOURepository.findAll();
  }

  @Override
  public List<SyncUser> getUsersByOuCode(String ouCode) {
    return syncUserRepository.findByOuCode(ouCode);
  }
}
