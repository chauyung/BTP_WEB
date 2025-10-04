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
import nccc.btp.entity.NcccReceiptType;
import nccc.btp.service.ReceiptTypeService;

@RestController
@RequestMapping("/receiptType")
public class ReceiptTypeController {

  protected static Logger LOG = LoggerFactory.getLogger(ReceiptTypeController.class);

  @Autowired
  private ReceiptTypeService receiptTypeService;

  @GetMapping("/findAll")
  public ResponseEntity<List<NcccReceiptType>> findAll() {
    return ResponseEntity.ok(receiptTypeService.findAll());
  }

  @PostMapping("/addNcccReceiptType")
  public ResponseEntity<NcccReceiptType> addNcccReceiptType(
      @RequestBody NcccReceiptType ncccReceiptType) {
    return ResponseEntity.ok(receiptTypeService.add(ncccReceiptType));
  }

  @PostMapping("/updateNcccReceiptType")
  public ResponseEntity<NcccReceiptType> updateNcccReceiptType(
      @RequestBody NcccReceiptType ncccReceiptType) {
    return ResponseEntity.ok(receiptTypeService.update(ncccReceiptType));
  }

  @PostMapping("/deleteNcccReceiptType")
  public ResponseEntity<String> deleteNcccReceiptType(
      @RequestBody NcccReceiptType ncccReceiptType) {
    try {
      String message = receiptTypeService.delete(ncccReceiptType.getId());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
