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
import nccc.btp.entity.NcccTaxCode;
import nccc.btp.service.TaxCodeService;

@RestController
@RequestMapping("/taxCode")
public class TaxCodeController {

  protected static Logger LOG = LoggerFactory.getLogger(TaxCodeController.class);

  @Autowired
  private TaxCodeService taxCodeService;

  @GetMapping("/findAll")
  public ResponseEntity<List<NcccTaxCode>> findAll() {
    return ResponseEntity.ok(taxCodeService.findAll());
  }

  @PostMapping("/addNcccTaxCode")
  public ResponseEntity<NcccTaxCode> addNcccTaxCode(@RequestBody NcccTaxCode ncccTaxCode) {
    return ResponseEntity.ok(taxCodeService.add(ncccTaxCode));
  }

  @PostMapping("/updateNcccTaxCode")
  public ResponseEntity<NcccTaxCode> updateNcccTaxCode(@RequestBody NcccTaxCode ncccTaxCode) {
    return ResponseEntity.ok(taxCodeService.update(ncccTaxCode));
  }

  @PostMapping("/deleteNcccTaxCode")
  public ResponseEntity<String> deleteNcccTaxCode(@RequestBody NcccTaxCode ncccTaxCode) {
    try {
      String message = taxCodeService.delete(ncccTaxCode.getTaxCode());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
