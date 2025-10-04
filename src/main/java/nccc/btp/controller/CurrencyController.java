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
import nccc.btp.entity.NcccCurrency;
import nccc.btp.service.CurrencyService;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

  protected static Logger LOG = LoggerFactory.getLogger(CurrencyController.class);

  @Autowired
  private CurrencyService currencyService;

  @GetMapping("/findAll")
  public ResponseEntity<List<NcccCurrency>> findAll() {
    return ResponseEntity.ok(currencyService.findAll());
  }

  @PostMapping("/addNcccCurrency")
  public ResponseEntity<NcccCurrency> addNcccCurrency(@RequestBody NcccCurrency ncccCurrency) {
    return ResponseEntity.ok(currencyService.add(ncccCurrency));
  }

  @PostMapping("/updateNcccCurrency")
  public ResponseEntity<NcccCurrency> updateNcccCurrency(@RequestBody NcccCurrency ncccCurrency) {
    return ResponseEntity.ok(currencyService.update(ncccCurrency));
  }

  @PostMapping("/deleteNcccCurrency")
  public ResponseEntity<String> deleteNcccCurrency(@RequestBody NcccCurrency ncccCurrency) {
    try {
      String message = currencyService.delete(ncccCurrency.getWaers());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
