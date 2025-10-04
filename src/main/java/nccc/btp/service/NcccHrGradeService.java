package nccc.btp.service;

import java.util.List;

import nccc.btp.dto.NcccHrGradeDto;

public interface NcccHrGradeService {
	
	List<NcccHrGradeDto> findAll();
	
	List<NcccHrGradeDto> findByYearAndVersionAndAccounting(String year, String version,
			String accounting);

}
