package nccc.btp.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.AcceptanceGeneralAcceptance;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.BpmRevM;
import nccc.btp.entity.BpmRevMD;
import nccc.btp.repository.BpmRevMDRRepository;
import nccc.btp.repository.BpmRevMDRepository;
import nccc.btp.repository.BpmRevMRepository;
import nccc.btp.service.AcceptanceFormService;
import nccc.btp.util.SecurityUtil;
import nccc.btp.vo.AcceptanceFormVo;

/*
 * 驗收單服務
 */
@Service
@Transactional
public class AcceptanceFormServiceImpl implements AcceptanceFormService {

  protected static Logger LOG = LoggerFactory.getLogger(AcceptanceFormServiceImpl.class);

  @Autowired
  private BpmRevMRepository bpmRevMRepository;

  @Autowired
  private BpmRevMDRRepository bpmRevMDRRepository;

  @Autowired
  private BpmRevMDRepository bpmRevMDRepository;

  // 初始化
  @Override
  public AcceptanceFormVo init(String revNo) {
    NcccUserDto user = SecurityUtil.getCurrentUser();
    AcceptanceFormVo vo = new AcceptanceFormVo();
    Optional<BpmRevM> bpmRevM = bpmRevMRepository.findById(revNo);
    if (!bpmRevM.isPresent()) {
      throw new RuntimeException("bpmRevM not found. revNo=" + revNo);
    }
    vo.setRevNo(bpmRevM.get().getRevNo());
    vo.setHrid(user.getHrid());
    vo.setApplicant(bpmRevM.get().getApplicant());
    vo.setDepartment(bpmRevM.get().getDepartment());
    vo.setPrNo(bpmRevM.get().getPrNo());
    vo.setPoNo(bpmRevM.get().getPoNo());
    vo.setBargDocNo(bpmRevM.get().getBargDocNo());
    vo.setRemark(bpmRevM.get().getRemark());
    List<BpmRevMD> bpmRevMDList = bpmRevMDRepository.findByRevNo(revNo);
    for (BpmRevMD md : bpmRevMDList) {
      AcceptanceGeneralAcceptance item = new AcceptanceGeneralAcceptance();
      item.setQty(md.getQty());
      vo.getGeneralAcceptanceList().add(item);
    }
    return vo;
  }

}
