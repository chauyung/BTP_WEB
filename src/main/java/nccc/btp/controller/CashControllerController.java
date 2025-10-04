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
import nccc.btp.entity.NcccCashController;
import nccc.btp.service.CashControllerService;

@RestController
@RequestMapping("/cashController")
public class CashControllerController {

  protected static Logger LOG = LoggerFactory.getLogger(CashControllerController.class);

  @Autowired
  private CashControllerService cashControllerService;

  @GetMapping("/findAll")
  public ResponseEntity<List<NcccCashController>> findAll() {
    return ResponseEntity.ok(cashControllerService.findAll());
  }

  @PostMapping("/addNcccCashController")
  public ResponseEntity<NcccCashController> addNcccCashController(
      @RequestBody NcccCashController ncccCashController) {
    return ResponseEntity.ok(cashControllerService.add(ncccCashController));
  }

  @PostMapping("/updateNcccCashController")
  public ResponseEntity<NcccCashController> updateNcccCashController(
      @RequestBody NcccCashController ncccCashController) {
    return ResponseEntity.ok(cashControllerService.update(ncccCashController));
  }

  @PostMapping("/deleteNcccCashController")
  public ResponseEntity<String> deleteNcccCashController(
      @RequestBody NcccCashController ncccCashController) {
    try {
      String message = cashControllerService.delete(ncccCashController.getCashControllerNo());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
