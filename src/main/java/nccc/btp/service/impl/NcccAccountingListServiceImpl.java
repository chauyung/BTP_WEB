package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccAccountingList;
import nccc.btp.repository.NcccAccountingListRepository;
import nccc.btp.service.NcccAccountingListService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class NcccAccountingListServiceImpl implements NcccAccountingListService {

  protected static Logger LOG = LoggerFactory.getLogger(NcccAccountingListServiceImpl.class);

  @Autowired
  private NcccAccountingListRepository ncccAccountingListRepository;

  @Override
  public List<NcccAccountingList> findAll() {
    return ncccAccountingListRepository.findAll(Sort.by(Sort.Direction.ASC, "subject"));
  }

  @Override
  public NcccAccountingList add(NcccAccountingList ncccAccountingList) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccAccountingList.setUpdateUser(user.getUserId());
    return ncccAccountingListRepository.save(ncccAccountingList);
  }

  @Override
  public NcccAccountingList update(NcccAccountingList ncccAccountingList) {
    if (!ncccAccountingListRepository.existsById(ncccAccountingList.getSubject())) {
      throw new RuntimeException(ncccAccountingList.getSubject() + " not found");
    }
	NcccUserDto user = SecurityUtil.getCurrentUser();
	ncccAccountingList.setUpdateUser(user.getUserId());
    return ncccAccountingListRepository.save(ncccAccountingList);
  }

  @Override
  public String delete(String subject) {
    if (!ncccAccountingListRepository.existsById(subject)) {
      throw new RuntimeException(subject + " not found");
    }
    ncccAccountingListRepository.deleteById(subject);
    return "deleted successfully";
  }
}
