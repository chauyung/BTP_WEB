package nccc.btp.controller;

import lombok.RequiredArgsConstructor;
import nccc.btp.dto.BudgetActualAllocationBatchRequest;
import nccc.btp.service.BudgetActualAllocationBatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/budgetActualAllocationBatch")
@RequiredArgsConstructor
@Validated
public class BudgetActualAllocationBatchController {

    private final BudgetActualAllocationBatchService service;

    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> run(@Valid @RequestBody BudgetActualAllocationBatchRequest req) {
        Map<String, Object> body = new HashMap<String, Object>();
        try {
            int rows = service.run(req);
            body.put("ok", Boolean.TRUE);
            body.put("message", "執行完成，共寫入 " + rows + " 筆");
            body.put("budgetYm", req.getBudgetYm());
            body.put("version", req.getVersion());
            body.put("totalInserted", Integer.valueOf(rows));
            return ResponseEntity.ok(body);
        } catch (IllegalStateException e) {
            body.clear();
            body.put("ok", Boolean.FALSE);
            body.put("code", "EXISTS");
            body.put("message", "該年月已經執行報表批次作業，按「確定」清除該年月資料，按「取消」結束程式");
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            body.clear();
            body.put("ok", Boolean.FALSE);
            body.put("message", e.getMessage());
            return ResponseEntity.ok(body);
        }
    }
}
