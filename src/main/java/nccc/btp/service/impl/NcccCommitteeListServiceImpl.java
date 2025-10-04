package nccc.btp.service.impl;

import nccc.btp.dto.PayMethodSelectOption;
import nccc.btp.entity.NcccPayMethod;
import nccc.btp.entity.NcccCommitteeList;
import nccc.btp.entity.NcccCommitteeGroup;
import nccc.btp.entity.NcccFinancialInstitution;
import nccc.btp.repository.NcccCommitteeListRepository;
import nccc.btp.repository.NcccPayMethodRepository;
import nccc.btp.repository.NcccFinancialInstitutionRepository;
import nccc.btp.repository.NcccCommitteeGroupRepository;
import nccc.btp.service.NcccCommitteeListService;
import nccc.btp.vo.CommitteeVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NcccCommitteeListServiceImpl implements NcccCommitteeListService {

    protected static Logger LOG = LoggerFactory.getLogger(NcccCommitteeListServiceImpl.class);

    @Autowired
    private NcccCommitteeListRepository  ncccCommitteeListRepository;

    @Autowired
    private NcccPayMethodRepository ncccPayMethodRepository;

    @Autowired
    private NcccFinancialInstitutionRepository ncccFinancialInstitutionRepository;

    @Autowired
    private NcccCommitteeGroupRepository ncccCommitteeGroupRepository;

    @Override
    public List<CommitteeVo> findAll() {

        List<CommitteeVo> Result = new ArrayList<>();

        List<NcccCommitteeList> ncccCommitteeLists = ncccCommitteeListRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        for(NcccCommitteeList data :  ncccCommitteeLists)
        {
            CommitteeVo committeeVo = new CommitteeVo();

            committeeVo.setId(data.getId());
            committeeVo.setJobTitle(data.getJobTitle());
            committeeVo.setPayMethod(data.getPayMethod());
            committeeVo.setName(data.getName());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String SDate = "";
            if(data.getSDate() != null)
            {
                SDate =  data.getSDate().format(formatter);
            }
            String EDate = "";
            if(data.getEDate() != null)
            {
                EDate =  data.getEDate().format(formatter);
            }
            committeeVo.setSDate(SDate);
            committeeVo.setEDate(EDate);
            committeeVo.setPhone1(data.getPhone1());
            committeeVo.setFinancialInstitutionCode(data.getFinancialInstitutionCode());
            committeeVo.setUnit(data.getUnit());
            committeeVo.setEmail(data.getEmail());
            committeeVo.setPosition(data.getPosition());
            committeeVo.setFax(data.getFax());
            committeeVo.setSort(data.getSort());
            committeeVo.setAddress(data.getAddress());
            committeeVo.setContactAddress(data.getContactAddress());
            committeeVo.setGroupName(data.getGroupName());
            committeeVo.setResearchProject(data.getResearchProject());
            committeeVo.setConvener(data.getConvener());
            committeeVo.setRemark(data.getRemark());
            Result.add(committeeVo);

        }

        return Result;
    }

    @Override
    public NcccCommitteeList add(CommitteeVo vo) {

        NcccCommitteeList data = new NcccCommitteeList();

        data.setId(vo.getId());
        data.setJobTitle(vo.getJobTitle());
        data.setPayMethod(vo.getPayMethod());
        data.setName(vo.getName());
        data.setPhone1(vo.getPhone1());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(vo.getSDate() != null && !vo.getSDate().isEmpty())
        {
            data.setSDate(LocalDate.parse(vo.getSDate(), formatter));
        }

        if(vo.getEDate() != null && !vo.getEDate().isEmpty())
        {
            data.setEDate(LocalDate.parse(vo.getEDate(), formatter));
        }
        data.setFinancialInstitutionCode(vo.getFinancialInstitutionCode());
        data.setUnit(vo.getUnit());
        data.setEmail(vo.getEmail());
        data.setPosition(vo.getPosition());
        data.setFax(vo.getFax());
        data.setSort(vo.getSort());
        data.setAddress(vo.getAddress());
        data.setContactAddress(vo.getContactAddress());
        data.setGroupName(vo.getGroupName());
        data.setResearchProject(vo.getResearchProject());
        data.setConvener(vo.getConvener());
        data.setRemark(vo.getRemark());
        return ncccCommitteeListRepository.save(data);
    }

    @Override
    public NcccCommitteeList update(CommitteeVo vo) {
        if (!ncccCommitteeListRepository.existsById(vo.getId())) {
            throw new RuntimeException(vo.getId() + " not found");
        }

        NcccCommitteeList data = ncccCommitteeListRepository.findById(vo.getId()).get();

        data.setId(vo.getId());
        data.setJobTitle(vo.getJobTitle());
        data.setPayMethod(vo.getPayMethod());
        data.setName(vo.getName());
        data.setPhone1(vo.getPhone1());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(vo.getSDate() != null && !vo.getSDate().isEmpty())
        {
            data.setSDate(LocalDate.parse(vo.getSDate(), formatter));
        }

        if(vo.getEDate() != null && !vo.getEDate().isEmpty())
        {
            data.setEDate(LocalDate.parse(vo.getEDate(), formatter));
        }
        data.setFinancialInstitutionCode(vo.getFinancialInstitutionCode());
        data.setUnit(vo.getUnit());
        data.setEmail(vo.getEmail());
        data.setPosition(vo.getPosition());
        data.setFax(vo.getFax());
        data.setSort(vo.getSort());
        data.setAddress(vo.getAddress());
        data.setContactAddress(vo.getContactAddress());
        data.setGroupName(vo.getGroupName());
        data.setResearchProject(vo.getResearchProject());
        data.setConvener(vo.getConvener());
        data.setRemark(vo.getRemark());
        return ncccCommitteeListRepository.save(data);
    }

    @Override
    public String delete(String id) {
        if (!ncccCommitteeListRepository.existsById(id)) {
            throw new RuntimeException(id + " not found");
        }
        ncccCommitteeListRepository.deleteById(id);
        return id + " deleted successfully";
    }


    public List<PayMethodSelectOption> getPayMethodSelectOptions() {

        List<PayMethodSelectOption> ResultDatas = new ArrayList<PayMethodSelectOption>();

        List<NcccPayMethod> Source = ncccPayMethodRepository.findAll();

        for (final NcccPayMethod Data : Source) {

            PayMethodSelectOption WarteData = new PayMethodSelectOption(Data.getZlsch(), Data.getText1());

            ResultDatas.add(WarteData);

        }

        return ResultDatas;
    }

    //金融機構代碼
    public List<NcccFinancialInstitution> getNcccFinancialInstitutionList()
    {
        return ncccFinancialInstitutionRepository.findAll();
    }

    //研發委員組別
    public List<NcccCommitteeGroup> getNcccCommitteeGroupList()
    {
        return ncccCommitteeGroupRepository.findAll();
    }

}
