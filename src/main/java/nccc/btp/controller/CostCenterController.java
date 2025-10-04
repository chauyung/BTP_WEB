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
import nccc.btp.entity.NcccCostCenter;
import nccc.btp.service.CostCenterService;

@RestController
@RequestMapping("/costCenter")
public class CostCenterController {

  protected static Logger LOG = LoggerFactory.getLogger(CostCenterController.class);

  @Autowired
  private CostCenterService costCenterService;

  @GetMapping("/findAll")
  public ResponseEntity<List<NcccCostCenter>> findAll() {
    return ResponseEntity.ok(costCenterService.findAll());
  }

  @PostMapping("/addNcccCostCenter")
  public ResponseEntity<NcccCostCenter> addNcccCostCenter(
      @RequestBody NcccCostCenter ncccCostCenter) {
    return ResponseEntity.ok(costCenterService.add(ncccCostCenter));
  }

  @PostMapping("/updateNcccCostCenter")
  public ResponseEntity<NcccCostCenter> updateNcccCostCenter(
      @RequestBody NcccCostCenter ncccCostCenter) {
    return ResponseEntity.ok(costCenterService.update(ncccCostCenter));
  }

  @PostMapping("/deleteNcccCostCenter")
  public ResponseEntity<String> deleteNcccCostCenter(@RequestBody NcccCostCenter ncccCostCenter) {
    try {
      String message = costCenterService.delete(ncccCostCenter.getKostl());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
