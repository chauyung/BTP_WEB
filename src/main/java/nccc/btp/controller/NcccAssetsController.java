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
import nccc.btp.entity.NcccAssets;
import nccc.btp.service.NcccAssetsService;

@RestController
@RequestMapping("/ncccAssets")
public class NcccAssetsController {

	protected static Logger LOG = LoggerFactory.getLogger(NcccAssetsController.class);

	@Autowired
	private NcccAssetsService ncccAssetsService;

	@GetMapping("/findAll")
	public ResponseEntity<List<NcccAssets>> findAll() {
		return ResponseEntity.ok(ncccAssetsService.findAll());
	}

	@PostMapping("/addNcccAssets")
	public ResponseEntity<NcccAssets> addNcccAssets(@RequestBody NcccAssets ncccAssets) {
		return ResponseEntity.ok(ncccAssetsService.add(ncccAssets));
	}

	@PostMapping("/updateNcccAssets")
	public ResponseEntity<NcccAssets> updateNcccAssets(@RequestBody NcccAssets ncccAssets) {
		return ResponseEntity.ok(ncccAssetsService.update(ncccAssets));
	}

	@PostMapping("/deleteNcccAssets")
	public ResponseEntity<String> deleteNcccAssets(@RequestBody NcccAssets ncccAssets) {
		try {
			String message = ncccAssetsService.delete(ncccAssets.getOperateItemCode());
			return ResponseEntity.ok(message); // 200 OK
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
