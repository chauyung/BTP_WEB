package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccPayMethod;

public interface PayMethodService {

  List<NcccPayMethod> findAll();

  NcccPayMethod add(NcccPayMethod ncccPayMethod);

  NcccPayMethod update(NcccPayMethod ncccPayMethod);

  String delete(String zlsch);
}
