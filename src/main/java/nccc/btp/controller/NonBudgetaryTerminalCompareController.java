package nccc.btp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nccc.btp.service.NonBudgetaryTerminalCompareService;

/**
 * 報表管理：非預算 報表-端末機差異比較表(Controller)
 * ------------------------------------------------------
 * 建立人員: ChauYung(Team)
 * 建立日期: 2025-10-08
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/nonBudgetaryterminalCompare")
public class NonBudgetaryTerminalCompareController {
	
	// 報表管理：非預算 報表-端末機帳列數報表(Service)
	private final NonBudgetaryTerminalCompareService service;

	
	/**
	 * 產檔(Excel)
	 * @return API-Response
	 */
	@PostMapping("/export")
	public ResponseEntity<byte[]> export() {
		Optional<byte[]> opt = service.exportExcel();
		if (!opt.isPresent()) {
			return ResponseEntity.noContent().build();
		}

		String fileName = "report.xlsx";
		try {
		    fileName = URLEncoder.encode("端末機差異比較表" + ".xlsx", "UTF-8");
		} catch (UnsupportedEncodingException e) {
		    // 如果發生錯誤，就給個預設檔名（理論上 UTF-8 一定支援）
		}
		byte[] bytes = opt.get();
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + fileName)
				.contentType(org.springframework.http.MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.contentLength(bytes.length)
				.body(bytes);
	}
}
