package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccWithholdType;

public interface WithholdTypeService {

  List<NcccWithholdType> findAll();

  NcccWithholdType add(NcccWithholdType ncccWithholdType);

  NcccWithholdType update(NcccWithholdType ncccWithholdType);

  String delete(Long id);
}
