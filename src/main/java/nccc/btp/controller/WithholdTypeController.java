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
import nccc.btp.entity.NcccWithholdType;
import nccc.btp.service.WithholdTypeService;

@RestController
@RequestMapping("/withholdType")
public class WithholdTypeController {

  protected static Logger LOG = LoggerFactory.getLogger(WithholdTypeController.class);

  @Autowired
  private WithholdTypeService withholdTypeService;

  @GetMapping("/findAll")
  public ResponseEntity<List<NcccWithholdType>> findAll() {
    return ResponseEntity.ok(withholdTypeService.findAll());
  }

  @PostMapping("/addNcccWithholdType")
  public ResponseEntity<NcccWithholdType> addNcccWithholdType(
      @RequestBody NcccWithholdType ncccWithholdType) {
    return ResponseEntity.ok(withholdTypeService.add(ncccWithholdType));
  }

  @PostMapping("/updateNcccWithholdType")
  public ResponseEntity<NcccWithholdType> updateNcccWithholdType(
      @RequestBody NcccWithholdType ncccWithholdType) {
    return ResponseEntity.ok(withholdTypeService.update(ncccWithholdType));
  }

  @PostMapping("/deleteNcccWithholdType")
  public ResponseEntity<String> deleteNcccWithholdType(
      @RequestBody NcccWithholdType ncccWithholdType) {
    try {
      String message = withholdTypeService.delete(ncccWithholdType.getId());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
