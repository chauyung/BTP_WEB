package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccTaxCode;

public interface TaxCodeService {

  List<NcccTaxCode> findAll();

  NcccTaxCode add(NcccTaxCode ncccTaxCode);

  NcccTaxCode update(NcccTaxCode ncccTaxCode);

  String delete(String taxCode);
}
