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
import nccc.btp.entity.NcccCostAccount;
import nccc.btp.service.CostAccountService;

@RestController
@RequestMapping("/costAccount")
public class CostAccountController {

  protected static Logger LOG = LoggerFactory.getLogger(CostAccountController.class);

  @Autowired
  private CostAccountService costAccountService;

  @GetMapping("/findAll")
  public ResponseEntity<List<NcccCostAccount>> findAll() {
    return ResponseEntity.ok(costAccountService.findAll());
  }

  @PostMapping("/addNcccCostAccount")
  public ResponseEntity<NcccCostAccount> addNcccCostAccount(
      @RequestBody NcccCostAccount ncccCostAccount) {
    return ResponseEntity.ok(costAccountService.add(ncccCostAccount));
  }

  @PostMapping("/updateNcccCostAccount")
  public ResponseEntity<NcccCostAccount> updateNcccCostAccount(
      @RequestBody NcccCostAccount ncccCostAccount) {
    return ResponseEntity.ok(costAccountService.update(ncccCostAccount));
  }

  @PostMapping("/deleteNcccCostAccount")
  public ResponseEntity<String> deleteNcccCostAccount(
      @RequestBody NcccCostAccount ncccCostAccount) {
    try {
      String message = costAccountService.delete(ncccCostAccount.getId());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
