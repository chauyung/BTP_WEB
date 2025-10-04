package nccc.btp.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import nccc.btp.dto.PayMethodSelectOption;
import nccc.btp.entity.NcccDirectorSupervisorList;
import nccc.btp.entity.NcccPayMethod;
import nccc.btp.vo.DirectorSupervisorVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import nccc.btp.service.NcccDirectorSupervisorListService;
import nccc.btp.repository.NcccDirectorSupervisorListRepository;
import nccc.btp.repository.NcccPayMethodRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NcccDirectorSupervisorListServiceImpl implements NcccDirectorSupervisorListService {

    protected static Logger LOG = LoggerFactory.getLogger(NcccDirectorSupervisorListServiceImpl.class);

    @Autowired
    private NcccDirectorSupervisorListRepository  ncccDirectorSupervisorListRepository;

    @Autowired
    private NcccPayMethodRepository  ncccPayMethodRepository;

    @Override
    public List<DirectorSupervisorVo> findAll() {

        List<DirectorSupervisorVo> Result = new ArrayList<>();

        List<NcccDirectorSupervisorList>  ncccDirectorSupervisorLists = ncccDirectorSupervisorListRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        for(NcccDirectorSupervisorList data :  ncccDirectorSupervisorLists)
        {
            DirectorSupervisorVo  directorSupervisorVo = new DirectorSupervisorVo();

            directorSupervisorVo.setId(data.getId());
            directorSupervisorVo.setJobTitle(data.getJobTitle());
            directorSupervisorVo.setName(data.getName());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String Birthday = "";
            if(data.getBirthday() != null)
            {
                Birthday =  data.getBirthday().format(formatter);
            }
            directorSupervisorVo.setBirthday(Birthday);
            directorSupervisorVo.setUnit(data.getUnit());
            directorSupervisorVo.setUnitJobTitle(data.getUnitJobtitle());
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
            directorSupervisorVo.setSDate(SDate);
            directorSupervisorVo.setEDate(EDate);
            directorSupervisorVo.setPhone1(data.getPhone1());
            directorSupervisorVo.setPhone2(data.getPhone2());
            directorSupervisorVo.setMobile(data.getMobile());
            directorSupervisorVo.setContactName(data.getContactName());
            directorSupervisorVo.setContactPhone(data.getContactPhone());
            directorSupervisorVo.setPayMethod(data.getPayMethod());
            directorSupervisorVo.setAddress(data.getAddress());
            directorSupervisorVo.setContactAddress(data.getContactAddress());
            directorSupervisorVo.setRemark1(data.getRemark1());
            directorSupervisorVo.setRemark2(data.getRemark2());
            directorSupervisorVo.setSort(data.getSort());
            directorSupervisorVo.setIsCivil(data.getIsCivil());

            Result.add(directorSupervisorVo);

        }

        return Result;
    }

    @Override
    public NcccDirectorSupervisorList add(DirectorSupervisorVo vo) {

        NcccDirectorSupervisorList data = new NcccDirectorSupervisorList();

        data.setId(vo.getId());
        data.setJobTitle(vo.getJobTitle());
        data.setName(vo.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(vo.getBirthday() != null && !vo.getBirthday().isEmpty())
        {

            data.setBirthday(LocalDate.parse(vo.getBirthday(), formatter));
        }
        data.setUnit(vo.getUnit());
        data.setUnitJobtitle(vo.getUnitJobTitle());
        if(vo.getSDate() != null && !vo.getSDate().isEmpty())
        {
            data.setSDate(LocalDate.parse(vo.getSDate(), formatter));
        }

        if(vo.getEDate() != null && !vo.getEDate().isEmpty())
        {
            data.setEDate(LocalDate.parse(vo.getEDate(), formatter));
        }
        data.setPhone1(vo.getPhone1());
        data.setPhone2(vo.getPhone2());
        data.setMobile(vo.getMobile());
        data.setContactName(vo.getContactName());
        data.setContactPhone(vo.getContactPhone());
        data.setPayMethod(vo.getPayMethod());
        data.setAddress(vo.getAddress());
        data.setContactAddress(vo.getContactAddress());
        data.setRemark1(vo.getRemark1());
        data.setRemark2(vo.getRemark2());
        data.setSort(vo.getSort());
        data.setIsCivil(vo.getIsCivil());


        return ncccDirectorSupervisorListRepository.save(data);
    }

    @Override
    public NcccDirectorSupervisorList update(DirectorSupervisorVo vo) {
        if (!ncccDirectorSupervisorListRepository.existsById(vo.getId())) {
            throw new RuntimeException(vo.getId() + " not found");
        }

        NcccDirectorSupervisorList data = ncccDirectorSupervisorListRepository.findById(vo.getId()).get();

        data.setJobTitle(vo.getJobTitle());
        data.setName(vo.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(vo.getBirthday() != null && !vo.getBirthday().isEmpty())
        {

            data.setBirthday(LocalDate.parse(vo.getBirthday(), formatter));
        }
        data.setUnit(vo.getUnit());
        data.setUnitJobtitle(vo.getUnitJobTitle());
        if(vo.getSDate() != null && !vo.getSDate().isEmpty())
        {
            data.setSDate(LocalDate.parse(vo.getSDate(), formatter));
        }

        if(vo.getEDate() != null && !vo.getEDate().isEmpty())
        {
            data.setEDate(LocalDate.parse(vo.getEDate(), formatter));
        }
        data.setPhone1(vo.getPhone1());
        data.setPhone2(vo.getPhone2());
        data.setMobile(vo.getMobile());
        data.setContactName(vo.getContactName());
        data.setContactPhone(vo.getContactPhone());
        data.setPayMethod(vo.getPayMethod());
        data.setAddress(vo.getAddress());
        data.setContactAddress(vo.getContactAddress());
        data.setRemark1(vo.getRemark1());
        data.setRemark2(vo.getRemark2());
        data.setSort(vo.getSort());
        data.setIsCivil(vo.getIsCivil());

        return ncccDirectorSupervisorListRepository.save(data);
    }

    @Override
    public String delete(String id) {
        if (!ncccDirectorSupervisorListRepository.existsById(id)) {
            throw new RuntimeException(id + " not found");
        }
        ncccDirectorSupervisorListRepository.deleteById(id);
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

}
