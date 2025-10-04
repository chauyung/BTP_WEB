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
import nccc.btp.entity.NcccPayMethod;
import nccc.btp.service.PayMethodService;

@RestController
@RequestMapping("/payMethod")
public class PayMethodController {

  protected static Logger LOG = LoggerFactory.getLogger(PayMethodController.class);

  @Autowired
  private PayMethodService payMethodService;

  @GetMapping("/findAll")
  public ResponseEntity<List<NcccPayMethod>> findAll() {
    return ResponseEntity.ok(payMethodService.findAll());
  }

  @PostMapping("/addNcccPayMethod")
  public ResponseEntity<NcccPayMethod> addNcccPayMethod(@RequestBody NcccPayMethod ncccPayMethod) {
    return ResponseEntity.ok(payMethodService.add(ncccPayMethod));
  }

  @PostMapping("/updateNcccPayMethod")
  public ResponseEntity<NcccPayMethod> updateNcccPayMethod(
      @RequestBody NcccPayMethod ncccPayMethod) {
    return ResponseEntity.ok(payMethodService.update(ncccPayMethod));
  }

  @PostMapping("/deleteNcccPayMethod")
  public ResponseEntity<String> deleteNcccPayMethod(@RequestBody NcccPayMethod ncccPayMethod) {
    try {
      String message = payMethodService.delete(ncccPayMethod.getZlsch());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

}
