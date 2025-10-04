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
import nccc.btp.entity.NcccCostGroup;
import nccc.btp.service.CostGroupService;

@RestController
@RequestMapping("/costGroup")
public class CostGroupController {

  protected static Logger LOG = LoggerFactory.getLogger(CostGroupController.class);

  @Autowired
  private CostGroupService costGroupService;

  @GetMapping("/findAll")
  public ResponseEntity<List<NcccCostGroup>> findAll() {
    return ResponseEntity.ok(costGroupService.findAll());
  }

  @PostMapping("/addNcccCostGroup")
  public ResponseEntity<NcccCostGroup> addNcccCostGroup(@RequestBody NcccCostGroup ncccCostGroup) {
    return ResponseEntity.ok(costGroupService.add(ncccCostGroup));
  }

  @PostMapping("/updateNcccCostGroup")
  public ResponseEntity<NcccCostGroup> updateNcccCostGroup(
      @RequestBody NcccCostGroup ncccCostGroup) {
    return ResponseEntity.ok(costGroupService.update(ncccCostGroup));
  }

  @PostMapping("/deleteNcccCostGroup")
  public ResponseEntity<String> deleteNcccCostGroup(@RequestBody NcccCostGroup ncccCostGroup) {
    try {
      String message = costGroupService.delete(ncccCostGroup.getId());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
