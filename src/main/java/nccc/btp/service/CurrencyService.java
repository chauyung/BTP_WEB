package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccCurrency;

public interface CurrencyService {

  List<NcccCurrency> findAll();

  NcccCurrency add(NcccCurrency ncccCurrency);

  NcccCurrency update(NcccCurrency ncccCurrency);

  String delete(String waers);
}
