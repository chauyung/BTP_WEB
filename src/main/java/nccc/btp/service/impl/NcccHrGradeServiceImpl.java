package nccc.btp.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccHrGradeDto;
import nccc.btp.entity.NcccHrGradeM;
import nccc.btp.repository.NcccHrGradeRepository;
import nccc.btp.service.NcccHrGradeService;

@Service
@Transactional
public class NcccHrGradeServiceImpl implements NcccHrGradeService {
	
	protected static Logger LOG = LoggerFactory.getLogger(NcccHrGradeServiceImpl.class);
	
	@Autowired
	private NcccHrGradeRepository ncccHrGradeRepository;

	@Override
	public List<NcccHrGradeDto> findAll() {
		
		List<NcccHrGradeDto> resultList = new ArrayList<>();
		List<NcccHrGradeM> ncccHrGradeMList = ncccHrGradeRepository.findAll();
		DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		for (NcccHrGradeM data : ncccHrGradeMList) {
			
			NcccHrGradeDto dto = new NcccHrGradeDto();
			
			dto.setYear(data.getYear());
			dto.setVersion(data.getVersion());
			dto.setAccounting(data.getAccounting());
			dto.setSubject(data.getSubject());
			dto.setCreateUser(data.getCreateUser());
			dto.setCreateDate(data.getCreateDate().format(dtFormatter));
			dto.setUpdateUser(data.getUpdateUser());
			dto.setUpdateDate(data.getUpdateDate().format(dtFormatter));
			resultList.add(dto);
			
		}
		
		return resultList;
	}

	@Override
	public List<NcccHrGradeDto> findByYearAndVersionAndAccounting(
			String year, String version, String accounting) {
		return null;
	}

}
