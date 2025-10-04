package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccCashController;

public interface CashControllerService {

  List<NcccCashController> findAll();

  NcccCashController add(NcccCashController ncccCashController);

  NcccCashController update(NcccCashController ncccCashController);

  String delete(String cashControllerNo);
}
