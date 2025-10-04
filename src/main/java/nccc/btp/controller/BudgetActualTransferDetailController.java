package nccc.btp.controller;

import lombok.RequiredArgsConstructor;
import nccc.btp.dto.BudgetActualTransferDetailRequest;
import nccc.btp.service.BudgetActualTransferDetailService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budgetActualTransferDetail")
@RequiredArgsConstructor
public class BudgetActualTransferDetailController {

	@Autowired
    private BudgetActualTransferDetailService service;

    @PostMapping("/export")
    public ResponseEntity<byte[]> export(@RequestBody BudgetActualTransferDetailRequest req) {
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
