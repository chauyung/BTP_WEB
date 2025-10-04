package nccc.btp.controller;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nccc.btp.entity.NcccDepositBank;
import nccc.btp.entity.NcccPendingRemittance;
import nccc.btp.service.PendingRemittanceService;
import nccc.btp.util.CsvUtil;
import nccc.btp.vo.CsvData;
import nccc.btp.vo.PendingRemittanceQueryVo;
import nccc.btp.vo.PendingRemittanceVo;

@RestController
@RequestMapping("/pendingRemittance")
public class PendingRemittanceController {

  protected static Logger LOG = LoggerFactory.getLogger(PendingRemittanceController.class);

  @Autowired
  private PendingRemittanceService pendingRemittanceService;

  @GetMapping("/getBankNo")
  public ResponseEntity<List<NcccDepositBank>> getBankNo() {
    return ResponseEntity.ok(pendingRemittanceService.getBankNo());
  }

  @PostMapping("/searchNcccPendingRemittance")
  public ResponseEntity<List<PendingRemittanceVo>> searchNcccPendingRemittance(
      @RequestBody PendingRemittanceQueryVo pendingRemittanceQueryVo) {
    return ResponseEntity.ok(pendingRemittanceService.search(pendingRemittanceQueryVo));
  }

  @PostMapping("/addNcccPendingRemittance")
  public ResponseEntity<NcccPendingRemittance> addNcccPendingRemittance(
      @RequestBody NcccPendingRemittance ncccPendingRemittance) {
    return ResponseEntity.ok(pendingRemittanceService.add(ncccPendingRemittance));
  }

  @PostMapping("/updateNcccPendingRemittance")
  public ResponseEntity<NcccPendingRemittance> updateNcccPendingRemittance(
      @RequestBody NcccPendingRemittance ncccPendingRemittance) {
    return ResponseEntity.ok(pendingRemittanceService.update(ncccPendingRemittance));
  }

  @PostMapping("/deleteNcccPendingRemittance")
  public ResponseEntity<String> deleteNcccPendingRemittance(
      @RequestBody NcccPendingRemittance ncccPendingRemittance) {
    try {
      String message = pendingRemittanceService.delete(ncccPendingRemittance.getId());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PostMapping("/checkout")
  public ResponseEntity<String> checkout(
      @RequestBody PendingRemittanceQueryVo pendingRemittanceQueryVo) {
    String message = pendingRemittanceService.checkout(pendingRemittanceQueryVo);
    return ResponseEntity.ok(message);
  }

  @PostMapping("/toSAP")
  public ResponseEntity<List<PendingRemittanceVo>> toSAP(
      @RequestBody List<NcccPendingRemittance> list) {
    return ResponseEntity.ok(pendingRemittanceService.toSAP(list));
  }

  @PostMapping(value = "exportCsv", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = "text/csv; charset=BIG5")
  public ResponseEntity<byte[]> exportCsv(@RequestBody CsvData payload) {
    return buildCsvResponse(payload);
  }

  // ===== 共用：把 Map 形式的 headers 轉成 labels/keys，產 CSV 後回應 =====
  private ResponseEntity<byte[]> buildCsvResponse(CsvData payload) {
    if (payload == null || payload.getRows() == null || payload.getRows().isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    if (payload.getHeaders() == null || payload.getHeaders().isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    // 依 headers 決定欄位順序；要求每個 header map 有 key="key" 與 key="label"
    List<Map<String, String>> hs = payload.getHeaders();
    String[] labels = new String[hs.size()];
    String[] keys = new String[hs.size()];

    for (int i = 0; i < hs.size(); i++) {
      Map<String, String> h = hs.get(i);
      String label = h.get("label");
      String key = h.get("key");
      if (label == null || key == null) {
        return ResponseEntity.badRequest().build();
      }
      labels[i] = label;
      keys[i] = key;
    }

    // 產 CSV
    byte[] body = CsvUtil.toCsvBytes(labels, keys, payload.getRows());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("text", "csv", Charset.forName("Big5")));
    headers.setContentLength(body.length);

    return new ResponseEntity<>(body, headers, HttpStatus.OK);
  }
}
