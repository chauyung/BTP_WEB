package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccOperateItems;

public interface NcccOperateItemsService {

  List<NcccOperateItems> findAll();

  NcccOperateItems add(NcccOperateItems ncccOperateItems);

  NcccOperateItems update(NcccOperateItems ncccOperateItems);

  String delete(String operateItemCode);
}
