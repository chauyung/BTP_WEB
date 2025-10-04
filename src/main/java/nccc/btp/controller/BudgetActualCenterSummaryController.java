package nccc.btp.controller;

import nccc.btp.dto.BudgetActualCenterSummaryRequest;
import nccc.btp.service.BudgetActualCenterSummaryService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budgetActualCenterSummary")
public class BudgetActualCenterSummaryController {

	@Autowired
	private BudgetActualCenterSummaryService service;
    
    @PostMapping("/export")
    public ResponseEntity<byte[]> exportExcel(@RequestBody BudgetActualCenterSummaryRequest req) {
    	Optional<byte[]> opt = service.exportExcelMaybe(req);
        if (!opt.isPresent()) {
            return ResponseEntity.noContent().build(); // 204
        }
        byte[] bytes = opt.get();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"transfer-detail.xlsx\"")
            .contentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .contentLength(bytes.length)
            .body(bytes);
    }
}
