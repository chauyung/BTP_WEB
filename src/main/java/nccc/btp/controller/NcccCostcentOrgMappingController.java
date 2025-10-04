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
import nccc.btp.entity.NcccCostcentOrgMapping;
import nccc.btp.service.NcccCostcentOrgMappingService;

@RestController
@RequestMapping("/ncccCostcentOrgMapping")
public class NcccCostcentOrgMappingController {

	protected static Logger LOG = LoggerFactory.getLogger(NcccCostcentOrgMappingController.class);

	@Autowired
	private NcccCostcentOrgMappingService ncccCostcentOrgMappinggMappingService;

	@GetMapping("/findAll")
	public ResponseEntity<List<NcccCostcentOrgMapping>> findAll() {
		return ResponseEntity.ok(ncccCostcentOrgMappinggMappingService.findAll());
	}

	@PostMapping("/addNcccCostcentOrgMapping")
	public ResponseEntity<NcccCostcentOrgMapping> addNcccCostcentOrgMapping(@RequestBody NcccCostcentOrgMapping ncccCostcentOrgMapping) {
		return ResponseEntity.ok(ncccCostcentOrgMappinggMappingService.add(ncccCostcentOrgMapping));
	}

	@PostMapping("/updateNcccCostcentOrgMapping")
	public ResponseEntity<NcccCostcentOrgMapping> updaNcccCostcentOrgMapping(@RequestBody NcccCostcentOrgMapping ncccCostcentOrgMapping) {
		return ResponseEntity.ok(ncccCostcentOrgMappinggMappingService.update(ncccCostcentOrgMapping));
	}

	@PostMapping("/deleteNcccCostcentOrgMapping")
	public ResponseEntity<String> deleteNcccCostcentOrgMapping(@RequestBody NcccCostcentOrgMapping ncccCostcentOrgMapping) {
		try {
			String message = ncccCostcentOrgMappinggMappingService.delete(ncccCostcentOrgMapping.getHrDepCodeAct());
			return ResponseEntity.ok(message); // 200 OK
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
