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
import nccc.btp.entity.NcccHtcMobile;
import nccc.btp.service.NcccHtcMobileService;

@RestController
@RequestMapping("/ncccHtcMobile")
public class NcccHtcMobileController {

	protected static Logger LOG = LoggerFactory.getLogger(NcccHtcMobileController.class);

	@Autowired
	private NcccHtcMobileService ncccHtcMobileService;

	@GetMapping("/findAll")
	public ResponseEntity<List<NcccHtcMobile>> findAll() {
		return ResponseEntity.ok(ncccHtcMobileService.findAll());
	}

	@PostMapping("/addNcccHtcMobile")
	public ResponseEntity<NcccHtcMobile> addNcccHtcMobile(@RequestBody NcccHtcMobile ncccHtcMobile) {
		return ResponseEntity.ok(ncccHtcMobileService.add(ncccHtcMobile));
	}

	@PostMapping("/updateNcccHtcMobile")
	public ResponseEntity<NcccHtcMobile> updateNcccHtcMobile(@RequestBody NcccHtcMobile ncccHtcMobile) {
		return ResponseEntity.ok(ncccHtcMobileService.update(ncccHtcMobile));
	}

	@PostMapping("/deleteNcccHtcMobile")
	public ResponseEntity<String> deleteNcccHtcMobile(@RequestBody NcccHtcMobile ncccHtcMobile) {
		try {
			String message = ncccHtcMobileService.delete(ncccHtcMobile.getNotificationNumber());
			return ResponseEntity.ok(message); // 200 OK
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
