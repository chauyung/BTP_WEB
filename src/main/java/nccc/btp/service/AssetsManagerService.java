package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccAssetsManager;

public interface AssetsManagerService {

  List<NcccAssetsManager> findAll();

  NcccAssetsManager add(NcccAssetsManager ncccAssetsManager);

  NcccAssetsManager update(NcccAssetsManager ncccAssetsManager);

  String delete(String id);
}
