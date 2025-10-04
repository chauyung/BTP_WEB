package nccc.btp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nccc.btp.dto.BudgetActualUsageDetailRequest;
import nccc.btp.service.BudgetActualUsageDetailService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/budgetActualUsageDetail")
@RequiredArgsConstructor
public class BudgetActualUsageDetailController {

	@Autowired
    private BudgetActualUsageDetailService service;

    @PostMapping("/export")
    public ResponseEntity<byte[]> export(@RequestBody BudgetActualUsageDetailRequest req) {
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
