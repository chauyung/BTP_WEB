package nccc.btp.service.impl;

import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccUtilityBudgetAttributionB;
import nccc.btp.repository.NcccUtilityBudgetAttributionBRepository;
import nccc.btp.service.NcccUtilityBudgetAttributionBService;
import nccc.btp.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NcccUtilityBudgetAttributionBServiceImpl implements NcccUtilityBudgetAttributionBService {

    protected static Logger LOG = LoggerFactory.getLogger(NcccUtilityBudgetAttributionBServiceImpl.class);

    @Autowired
    private NcccUtilityBudgetAttributionBRepository ncccUtilityBudgetAttributionBRepository;

    @Override
    public List<NcccUtilityBudgetAttributionB> findAll() {
        return ncccUtilityBudgetAttributionBRepository.findAll(Sort.by(Sort.Direction.ASC, "phone"));
    }

    @Override
    public NcccUtilityBudgetAttributionB add(NcccUtilityBudgetAttributionB ncccUtilityBudgetAttributionB) {
        NcccUserDto user = SecurityUtil.getCurrentUser();
        ncccUtilityBudgetAttributionB.setUpdateUser(user.getUserId());
        return ncccUtilityBudgetAttributionBRepository.save(ncccUtilityBudgetAttributionB);
    }

    @Override
    public NcccUtilityBudgetAttributionB update(NcccUtilityBudgetAttributionB ncccUtilityBudgetAttributionB) {
        if (!ncccUtilityBudgetAttributionBRepository.existsById(ncccUtilityBudgetAttributionB.getPhone())) {
            throw new RuntimeException(ncccUtilityBudgetAttributionB.getPhone() + " not found");
        }
        NcccUserDto user = SecurityUtil.getCurrentUser();
        ncccUtilityBudgetAttributionB.setUpdateUser(user.getUserId());
        return ncccUtilityBudgetAttributionBRepository.save(ncccUtilityBudgetAttributionB);
    }

    @Override
    public String delete(String phone) {
        if (!ncccUtilityBudgetAttributionBRepository.existsById(phone)) {
            throw new RuntimeException(phone + " not found");
        }
        ncccUtilityBudgetAttributionBRepository.deleteById(phone);
        return phone + " deleted successfully";
    }
}
