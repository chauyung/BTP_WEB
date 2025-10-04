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

import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.Zguit0010;
import nccc.btp.service.Zguit0010Service;
import nccc.btp.util.SecurityUtil;

@RestController
@RequestMapping("/zguit0010")
public class Zguit0010Controller {

	protected static Logger LOG = LoggerFactory.getLogger(Zguit0010Controller.class);

	@Autowired
	private Zguit0010Service zguit0010Service;

	@GetMapping("/findAll")
	public ResponseEntity<List<Zguit0010>> findAll() {
		return ResponseEntity.ok(zguit0010Service.findAll());
	}

	@PostMapping("/addZguit0010")
	public ResponseEntity<Zguit0010> addZguit0010(@RequestBody Zguit0010 zguit0010) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		zguit0010.setUpdateUser(user.getUserId());
		return ResponseEntity.ok(zguit0010Service.add(zguit0010));
	}

	@PostMapping("/updateZguit0010")
	public ResponseEntity<Zguit0010> updateZguit0010(@RequestBody Zguit0010 zguit0010) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		zguit0010.setUpdateUser(user.getUserId());
		return ResponseEntity.ok(zguit0010Service.update(zguit0010));
	}

	@PostMapping("/deleteZguit0010")
	public ResponseEntity<String> deleteZguit0010(@RequestBody Zguit0010 zguit0010) {
		try {
			String message = zguit0010Service.delete(zguit0010.getVersSeq());
			return ResponseEntity.ok(message); // 200 OK
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
