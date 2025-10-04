package nccc.btp.controller;

import lombok.RequiredArgsConstructor;
import nccc.btp.dto.PreBudgetOperateDto;
import nccc.btp.service.BudgetActualOperationQueryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/budgetActualOperationQuery")
public class BudgetActualOperationQueryController {

  private static final Logger LOG = LoggerFactory.getLogger(BudgetActualOperationQueryController.class);

  private final BudgetActualOperationQueryService service;

  @PostMapping("/search")
  public ResponseEntity<List<PreBudgetOperateDto.Row>> search(@RequestBody PreBudgetOperateDto.SearchReq req) {
    LOG.info("search req => year={}, version={}, ouCodes={}, operateItemCodes={}",
        req.getYear(), req.getVersion(), req.getOuCodes(), req.getOperateItemCodes());
    return ResponseEntity.ok(service.search(req));
  }  

}
