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
import nccc.btp.entity.NcccExpenseCategoryNumber;
import nccc.btp.service.NcccExpenseCategoryNumberService;

@RestController
@RequestMapping("/ncccExpenseCategoryNumber")
public class NcccExpenseCategoryNumberController {

  protected static Logger LOG = LoggerFactory.getLogger(NcccExpenseCategoryNumberController.class);

  @Autowired
  private NcccExpenseCategoryNumberService ncccExpenseCategoryNumberService;

  @GetMapping("/findAll")
  public ResponseEntity<List<NcccExpenseCategoryNumber>> findAll() {
    return ResponseEntity.ok(ncccExpenseCategoryNumberService.findAll());
  }

  @PostMapping("/addNcccExpenseCategoryNumber")
  public ResponseEntity<NcccExpenseCategoryNumber> addNcccTaxCode(@RequestBody NcccExpenseCategoryNumber ncccExpenseCategoryNumber) {
    return ResponseEntity.ok(ncccExpenseCategoryNumberService.add(ncccExpenseCategoryNumber));
  }

  @PostMapping("/updateNcccExpenseCategoryNumber")
  public ResponseEntity<NcccExpenseCategoryNumber> updateNcccPurchaseCategoryNumber(@RequestBody NcccExpenseCategoryNumber ncccExpenseCategoryNumber) {
    return ResponseEntity.ok(ncccExpenseCategoryNumberService.update(ncccExpenseCategoryNumber));
  }

  @PostMapping("/deleteNcccExpenseCategoryNumber")
  public ResponseEntity<String> deleteNcccPurchaseCategoryNumber(@RequestBody NcccExpenseCategoryNumber ncccExpenseCategoryNumber) {
    try {
      String message = ncccExpenseCategoryNumberService.delete(ncccExpenseCategoryNumber.getCategoryNumber());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
