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
import nccc.btp.entity.NcccPurchaseCategoryNumber;
import nccc.btp.service.NcccPurchaseCategoryNumberService;

@RestController
@RequestMapping("/ncccPurchaseCategoryNumber")
public class NcccPurchaseCategoryNumberController {

  protected static Logger LOG = LoggerFactory.getLogger(NcccPurchaseCategoryNumberController.class);

  @Autowired
  private NcccPurchaseCategoryNumberService ncccPurchaseCategoryNumberService;

  @GetMapping("/findAll")
  public ResponseEntity<List<NcccPurchaseCategoryNumber>> findAll() {
    return ResponseEntity.ok(ncccPurchaseCategoryNumberService.findAll());
  }

  @PostMapping("/addNcccPurchaseCategoryNumber")
  public ResponseEntity<NcccPurchaseCategoryNumber> addNcccNcccPurchaseCategoryNumber(@RequestBody NcccPurchaseCategoryNumber ncccPurchaseCategoryNumber) {
    return ResponseEntity.ok(ncccPurchaseCategoryNumberService.add(ncccPurchaseCategoryNumber));
  }

  @PostMapping("/updateNcccPurchaseCategoryNumber")
  public ResponseEntity<NcccPurchaseCategoryNumber> updateNcccPurchaseCategoryNumber(@RequestBody NcccPurchaseCategoryNumber ncccPurchaseCategoryNumber) {
    return ResponseEntity.ok(ncccPurchaseCategoryNumberService.update(ncccPurchaseCategoryNumber));
  }

  @PostMapping("/deleteNcccPurchaseCategoryNumber")
  public ResponseEntity<String> deleteNcccPurchaseCategoryNumber(@RequestBody NcccPurchaseCategoryNumber ncccPurchaseCategoryNumber) {
    try {
      String message = ncccPurchaseCategoryNumberService.delete(ncccPurchaseCategoryNumber.getCategoryNumber());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
