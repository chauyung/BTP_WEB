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
import nccc.btp.entity.ZmmtSupplier;
import nccc.btp.service.ZmmtSupplierService;

@RestController
@RequestMapping("/zmmtSupplier")
public class ZmmtSupplierController {

  protected static Logger LOG = LoggerFactory.getLogger(ZmmtSupplierController.class);

  @Autowired
  private ZmmtSupplierService zmmtSupplierService;

  @GetMapping("/findAll")
  public ResponseEntity<List<ZmmtSupplier>> findAll() {
    return ResponseEntity.ok(zmmtSupplierService.findAll());
  }

  @PostMapping("/addZmmtSupplier")
  public ResponseEntity<ZmmtSupplier> addZmmtSupplier(@RequestBody ZmmtSupplier zmmtSupplier) {
    return ResponseEntity.ok(zmmtSupplierService.add(zmmtSupplier));
  }

  @PostMapping("/updateZmmtSupplier")
  public ResponseEntity<ZmmtSupplier> updateZmmtSupplier(@RequestBody ZmmtSupplier zmmtSupplier) {
    return ResponseEntity.ok(zmmtSupplierService.update(zmmtSupplier));
  }

  @PostMapping("/deleteZmmtSupplier")
  public ResponseEntity<String> deleteZmmtSupplier(@RequestBody ZmmtSupplier zmmtSupplier) {
    try {
      String message = zmmtSupplierService.delete(zmmtSupplier.getPartner());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

}
