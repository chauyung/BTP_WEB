package nccc.btp.controller;

import lombok.RequiredArgsConstructor;
import nccc.btp.dto.BudgetActualDeptOperationQueryRequest;
import nccc.btp.entity.NcccBudgetActual;
import nccc.btp.service.BudgetActualDeptOperationQueryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/budgetActualDeptOperationQuery")
public class BudgetActualDeptOperationQueryController {

  @Autowired
  private BudgetActualDeptOperationQueryService service;

  @PostMapping("/search")
  public ResponseEntity<?> search(@RequestBody BudgetActualDeptOperationQueryRequest req) {
	    List<NcccBudgetActual> data = service.search(req);
	    return ResponseEntity.ok(Collections.singletonMap("data", data));
	}
}
