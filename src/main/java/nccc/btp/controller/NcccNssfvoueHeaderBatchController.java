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
import nccc.btp.entity.NcccNssfvoueHeaderBatch;
import nccc.btp.service.NcccNssfvoueHeaderBatchService;

@RestController
@RequestMapping("/ncccNssfvoueHeaderBatch")
public class NcccNssfvoueHeaderBatchController {

	protected static Logger LOG = LoggerFactory.getLogger(NcccNssfvoueHeaderBatchController.class);

	@Autowired
	private NcccNssfvoueHeaderBatchService ncccNssfvoueHeaderBatchervice;

	@GetMapping("/findAll")
	public ResponseEntity<List<NcccNssfvoueHeaderBatch>> findAll() {
		return ResponseEntity.ok(ncccNssfvoueHeaderBatchervice.findAll());
	}

	@PostMapping("/addNcccNssfvoueHeaderBatch")
	public ResponseEntity<NcccNssfvoueHeaderBatch> addZguit0010(@RequestBody NcccNssfvoueHeaderBatch ncccNssfvoueHeaderBatch) {
		return ResponseEntity.ok(ncccNssfvoueHeaderBatchervice.add(ncccNssfvoueHeaderBatch));
	}

	@PostMapping("/updateNcccNssfvoueHeaderBatch")
	public ResponseEntity<NcccNssfvoueHeaderBatch> updateZguit0010(@RequestBody NcccNssfvoueHeaderBatch ncccNssfvoueHeaderBatch) {
		return ResponseEntity.ok(ncccNssfvoueHeaderBatchervice.update(ncccNssfvoueHeaderBatch));
	}

	@PostMapping("/deleteNcccNssfvoueHeaderBatch")
	public ResponseEntity<String> deleteNcccNssfvoueHeaderBatch(@RequestBody NcccNssfvoueHeaderBatch ncccNssfvoueHeaderBatch) {
		try {
			String message = ncccNssfvoueHeaderBatchervice.delete(ncccNssfvoueHeaderBatch.getNssfvoueHeaderBatch());
			return ResponseEntity.ok(message); // 200 OK
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
