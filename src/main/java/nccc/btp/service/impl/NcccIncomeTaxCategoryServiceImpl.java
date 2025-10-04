package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.entity.NcccIncomeTaxCategory;
import nccc.btp.repository.NcccIncomeTaxCategoryRepository;
import nccc.btp.service.NcccIncomeTaxCategoryService;

@Service
@Transactional
public class NcccIncomeTaxCategoryServiceImpl implements NcccIncomeTaxCategoryService {

  protected static Logger LOG = LoggerFactory.getLogger(NcccIncomeTaxCategoryServiceImpl.class);

  @Autowired
  private NcccIncomeTaxCategoryRepository ncccIncomeTaxCategoryRepository;

  @Override
  public List<NcccIncomeTaxCategory> findCertificateCategory() {
    return ncccIncomeTaxCategoryRepository.findByIdTaxCategory("8.證號別",Sort.by(Sort.Direction.ASC, "id.taxCode"));
  }

  @Override
  public List<NcccIncomeTaxCategory> findErrorNote() {
    return ncccIncomeTaxCategoryRepository.findByIdTaxCategory("4.錯誤註記",Sort.by(Sort.Direction.ASC, "id.taxCode"));
  }

  @Override
  public List<NcccIncomeTaxCategory> findIncomeCategory() {
    return ncccIncomeTaxCategoryRepository.findByIdTaxCategory("2.所得格式",Sort.by(Sort.Direction.ASC, "id.taxCode"));
  }

  @Override
  public List<NcccIncomeTaxCategory> findIncomeNote() {
    return ncccIncomeTaxCategoryRepository.findByIdTaxCategory("1.所得註記",Sort.by(Sort.Direction.ASC, "id.taxCode"));
  }

  @Override
  public List<NcccIncomeTaxCategory> findSoftwareNote() {
    return ncccIncomeTaxCategoryRepository.findByIdTaxCategory("3.軟體註記",Sort.by(Sort.Direction.ASC, "id.taxCode"));
  }

  @Override
  public List<NcccIncomeTaxCategory> findChargeCode() {
    return ncccIncomeTaxCategoryRepository.findByIdTaxCategory("7.費用代號",Sort.by(Sort.Direction.ASC, "id.taxCode"));
  }

  @Override
  public List<NcccIncomeTaxCategory> findOtherIncome() {
    return ncccIncomeTaxCategoryRepository.findByIdTaxCategory("5.其他所得",Sort.by(Sort.Direction.ASC, "id.taxCode"));
  }

  @Override
  public List<NcccIncomeTaxCategory> findBusinessSector() {
    return ncccIncomeTaxCategoryRepository.findByIdTaxCategory("6.執行業務所得",Sort.by(Sort.Direction.ASC, "id.taxCode"));
  }

  @Override
  public List<NcccIncomeTaxCategory> findCountryCode() {
    return ncccIncomeTaxCategoryRepository.findByIdTaxCategory("9.國家代碼",Sort.by(Sort.Direction.ASC, "id.taxCode"));
  }

}
