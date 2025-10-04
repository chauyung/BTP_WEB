package nccc.btp.controller;

import nccc.btp.service.BudgetActualAssetRetentionService;
import nccc.btp.dto.BudgetActualAssetRetentionRequest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budgetActualAssetRetention")
public class BudgetActualAssetRetentionController {

	@Autowired
    private BudgetActualAssetRetentionService service;

    @PostMapping("/export")
    public ResponseEntity<byte[]> export(@RequestBody BudgetActualAssetRetentionRequest req) {
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
