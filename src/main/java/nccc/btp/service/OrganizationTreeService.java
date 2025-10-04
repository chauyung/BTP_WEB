package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.SyncOU;
import nccc.btp.entity.SyncUser;

public interface OrganizationTreeService {

  List<SyncOU> getOrganizationTree();

  List<SyncUser> getUsersByOuCode(String ouCode);
}