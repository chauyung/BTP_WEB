package nccc.btp.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nccc.btp.entity.NcccUtilityBudgetAttribution;
import nccc.btp.service.NcccUtilityBudgetAttributionService;

@RestController
@RequestMapping("/ncccUtilityBudgetAttribution")
public class NcccUtilityBudgetAttributionController {

	protected static Logger LOG = LoggerFactory.getLogger(NcccUtilityBudgetAttributionController.class);

	@Autowired
	private NcccUtilityBudgetAttributionService ncccUtilityBudgetAttributionService;

	@GetMapping("/findAll")
	public ResponseEntity<List<NcccUtilityBudgetAttribution>> findAll() {
		return ResponseEntity.ok(ncccUtilityBudgetAttributionService.findAll());
	}

	@PostMapping("/addNcccUtilityBudgetAttribution")
	public ResponseEntity<NcccUtilityBudgetAttribution> addNcccHtcMobile(@RequestBody NcccUtilityBudgetAttribution ncccUtilityBudgetAttribution) {
		return ResponseEntity.ok(ncccUtilityBudgetAttributionService.add(ncccUtilityBudgetAttribution));
	}

	@PostMapping("/updateNcccUtilityBudgetAttribution")
	public ResponseEntity<NcccUtilityBudgetAttribution> updateNcccHtcMobile(@RequestBody NcccUtilityBudgetAttribution ncccUtilityBudgetAttribution) {
		return ResponseEntity.ok(ncccUtilityBudgetAttributionService.update(ncccUtilityBudgetAttribution));
	}

	@PostMapping("/deleteNcccUtilityBudgetAttribution")
	public ResponseEntity<String> deleteNcccHtcMobile(@RequestBody NcccUtilityBudgetAttribution ncccUtilityBudgetAttribution) {
		try {
			String message = ncccUtilityBudgetAttributionService.delete(ncccUtilityBudgetAttribution.getOuCode());
			return ResponseEntity.ok(message); // 200 OK
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
