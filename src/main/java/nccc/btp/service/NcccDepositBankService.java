package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccDepositBank;

public interface NcccDepositBankService {

  List<NcccDepositBank> findAll();

  NcccDepositBank add(NcccDepositBank ncccDepositBank);

  NcccDepositBank update(NcccDepositBank ncccDepositBank);

  String delete(String account);
}
