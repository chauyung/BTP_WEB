package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccIncomeTaxCategory;

public interface NcccIncomeTaxCategoryService {

  List<NcccIncomeTaxCategory> findCertificateCategory();
  
  List<NcccIncomeTaxCategory> findErrorNote();

  List<NcccIncomeTaxCategory> findIncomeCategory();

  List<NcccIncomeTaxCategory> findIncomeNote();

  List<NcccIncomeTaxCategory> findSoftwareNote();

  List<NcccIncomeTaxCategory> findChargeCode();

  List<NcccIncomeTaxCategory> findOtherIncome();

  List<NcccIncomeTaxCategory> findBusinessSector();

  List<NcccIncomeTaxCategory> findCountryCode();
}
