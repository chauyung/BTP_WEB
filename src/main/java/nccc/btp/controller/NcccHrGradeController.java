package nccc.btp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nccc.btp.dto.NcccHrGradeDto;
import nccc.btp.service.NcccHrGradeService;

@RestController
@RequestMapping("/ncccHrGrade")
public class NcccHrGradeController {
	
	protected static Logger LOG = LoggerFactory.getLogger(NcccHrGradeController.class);
	
	@Autowired
	private NcccHrGradeService ncccHrGradeService;
	
	@GetMapping("/findAll")
	public ResponseEntity<List<NcccHrGradeDto>> findAll() {
		return ResponseEntity.ok(ncccHrGradeService.findAll());
	}
	

}
