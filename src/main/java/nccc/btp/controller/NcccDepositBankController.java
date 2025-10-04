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
import nccc.btp.entity.NcccDepositBank;
import nccc.btp.service.NcccDepositBankService;

@RestController
@RequestMapping("/ncccDepositBank")
public class NcccDepositBankController {

  protected static Logger LOG = LoggerFactory.getLogger(NcccDepositBankController.class);

  @Autowired
  private NcccDepositBankService ncccDepositBankService;

  @GetMapping("/findAll")
  public ResponseEntity<List<NcccDepositBank>> findAll() {
    return ResponseEntity.ok(ncccDepositBankService.findAll());
  }

  @PostMapping("/addNcccDepositBank")
  public ResponseEntity<NcccDepositBank> addNcccTaxCode(@RequestBody NcccDepositBank ncccDepositBank) {
    return ResponseEntity.ok(ncccDepositBankService.add(ncccDepositBank));
  }

  @PostMapping("/updateNcccDepositBank")
  public ResponseEntity<NcccDepositBank> updateNcccTaxCode(@RequestBody NcccDepositBank ncccDepositBank) {
    return ResponseEntity.ok(ncccDepositBankService.update(ncccDepositBank));
  }

  @PostMapping("/deleteNcccDepositBank")
  public ResponseEntity<String> deleteNcccTaxCode(@RequestBody NcccDepositBank ncccDepositBank) {
    try {
      String message = ncccDepositBankService.delete(ncccDepositBank.getAccount());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
