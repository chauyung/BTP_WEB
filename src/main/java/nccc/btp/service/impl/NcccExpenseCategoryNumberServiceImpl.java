package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccExpenseCategoryNumber;
import nccc.btp.repository.NcccExpenseCategoryNumberRepository;
import nccc.btp.service.NcccExpenseCategoryNumberService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class NcccExpenseCategoryNumberServiceImpl implements NcccExpenseCategoryNumberService {

  protected static Logger LOG = LoggerFactory.getLogger(NcccExpenseCategoryNumberServiceImpl.class);

  @Autowired
  private NcccExpenseCategoryNumberRepository ncccExpenseCategoryNumberRepository;

  @Override
  public List<NcccExpenseCategoryNumber> findAll() {
    return ncccExpenseCategoryNumberRepository.findAll(Sort.by(Sort.Direction.ASC, "categoryNumber"));
  }

  @Override
  public NcccExpenseCategoryNumber add(NcccExpenseCategoryNumber ncccExpenseCategoryNumber) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccExpenseCategoryNumber.setUpdateUser(user.getUserId());
    return ncccExpenseCategoryNumberRepository.save(ncccExpenseCategoryNumber);
  }

  @Override
  public NcccExpenseCategoryNumber update(NcccExpenseCategoryNumber ncccExpenseCategoryNumber) {
    if (!ncccExpenseCategoryNumberRepository.existsById(ncccExpenseCategoryNumber.getCategoryNumber())) {
      throw new RuntimeException(ncccExpenseCategoryNumber.getCategoryNumber() + " not found");
    }
	NcccUserDto user = SecurityUtil.getCurrentUser();
	ncccExpenseCategoryNumber.setUpdateUser(user.getUserId());
    return ncccExpenseCategoryNumberRepository.save(ncccExpenseCategoryNumber);
  }

  @Override
  public String delete(String categoryNumber) {
    if (!ncccExpenseCategoryNumberRepository.existsById(categoryNumber)) {
      throw new RuntimeException(categoryNumber + " not found");
    }
    ncccExpenseCategoryNumberRepository.deleteById(categoryNumber);
    return "deleted successfully";
  }
}
