package nccc.btp.controller;

import java.util.List;

import nccc.btp.service.BudgetActualMaintenanceService;
import nccc.btp.vo.BudgetActualVo;
import nccc.btp.dto.OperateItemsSaveRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/budgetActualMaintenance")
public class BudgetActualMaintenanceController {

  @Autowired
  private BudgetActualMaintenanceService service;

  @PostMapping("/search")
  public ResponseEntity<List<BudgetActualVo>> search(@RequestBody BudgetActualVo query) {
    return ResponseEntity.ok(service.search(query));
  }
  
  @PostMapping("/operateItemsSave")
  public ResponseEntity<List<BudgetActualVo>> saveOperateItems(@RequestBody OperateItemsSaveRequest req) {
    return ResponseEntity.ok(service.saveOperateItems(req));
  }
}
