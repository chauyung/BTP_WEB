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
import nccc.btp.entity.NcccAccountingList;
import nccc.btp.entity.NcccNssfvoueHeaderBatch;
import nccc.btp.entity.NcccNssfvoueRecData;
import nccc.btp.service.ReceivablesManagementService;
import nccc.btp.util.CsvUtil;
import nccc.btp.vo.CsvData;
import nccc.btp.vo.ReceivablesManagementQueryVo;
import nccc.btp.vo.ReceivablesManagementVo;

@RestController
@RequestMapping("/receivablesManagement")
public class ReceivablesManagementController {

  protected static Logger LOG = LoggerFactory.getLogger(ReceivablesManagementController.class);

  @Autowired
  private ReceivablesManagementService receivablesManagementService;

  // 取得底稿批號清單
  @GetMapping("/getNssfvoueHeaderBatch")
  public ResponseEntity<List<NcccNssfvoueHeaderBatch>> getNssfvoueHeaderBatch() {
    return ResponseEntity.ok(receivablesManagementService.getNssfvoueHeaderBatch());
  }

  // 取得科目名稱清單
  @GetMapping("/getNcccAccountingList")
  public ResponseEntity<List<NcccAccountingList>> getNcccAccountingList() {
    return ResponseEntity.ok(receivablesManagementService.getNcccAccountingList());
  }

  @PostMapping("/query")
  public ResponseEntity<List<ReceivablesManagementVo>> query(
      @RequestBody ReceivablesManagementQueryVo receivablesManagementQueryVo) {
    return ResponseEntity.ok(receivablesManagementService.query(receivablesManagementQueryVo));
  }

  @PostMapping("/add")
  public ResponseEntity<List<ReceivablesManagementVo>> add(
      @RequestBody ReceivablesManagementVo receivablesManagementVo) {
    return ResponseEntity.ok(receivablesManagementService.add(receivablesManagementVo));
  }

  @PostMapping("/edit")
  public ResponseEntity<List<ReceivablesManagementVo>> edit(
      @RequestBody ReceivablesManagementVo receivablesManagementVo) {
    return ResponseEntity.ok(receivablesManagementService.edit(receivablesManagementVo));
  }

  @PostMapping("/delete")
  public ResponseEntity<String> delete(@RequestBody List<String> nssfvoueHeaderBatchList) {
    try {
      String message = receivablesManagementService.delete(nssfvoueHeaderBatchList);
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PostMapping("/toSAP")
  public ResponseEntity<String> toSAP(
      @RequestBody List<ReceivablesManagementVo> receivablesManagementVoList) {
    return ResponseEntity.ok(receivablesManagementService.toSAP(receivablesManagementVoList));
  }

  @PostMapping("/searchSubpoena")
  public ResponseEntity<String> searchSubpoena(
      @RequestBody ReceivablesManagementQueryVo receivablesManagementQueryVo) {
    String msg = "";
    try {
      msg = receivablesManagementService.importFromFtp(receivablesManagementQueryVo.getFileName());
      return ResponseEntity.ok(msg); // 200 OK
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  // 確認核可
  @PostMapping("/confirmation")
  public ResponseEntity<String> confirmation(@RequestBody List<String> nssfvoueHeaderBatchList) {
    return ResponseEntity.ok(receivablesManagementService.confirmation(nssfvoueHeaderBatchList));
  }

  // 確認退回
  @PostMapping("/confirmReturn")
  public ResponseEntity<String> confirmReturn(@RequestBody List<String> nssfvoueHeaderBatchList) {
    return ResponseEntity.ok(receivablesManagementService.confirmReturn(nssfvoueHeaderBatchList));
  }

  // 覆核核可
  @PostMapping("/reviewAndApproval")
  public ResponseEntity<String> reviewAndApproval(
      @RequestBody List<String> nssfvoueHeaderBatchList) {
    return ResponseEntity
        .ok(receivablesManagementService.reviewAndApproval(nssfvoueHeaderBatchList));
  }

  // 覆核退回
  @PostMapping("/reviewAndReturn")
  public ResponseEntity<String> reviewAndReturn(@RequestBody List<String> nssfvoueHeaderBatchList) {
    return ResponseEntity.ok(receivablesManagementService.reviewAndReturn(nssfvoueHeaderBatchList));
  }

  @PostMapping("/findDetailsByBatch")
  public ResponseEntity<List<NcccNssfvoueRecData>> findDetailsByBatch(
      @RequestBody ReceivablesManagementQueryVo receivablesManagementQueryVo) {
    return ResponseEntity.ok(receivablesManagementService
        .findDetailsByBatch(receivablesManagementQueryVo.getNssfvoueHeaderBatch()));
  }

  @PostMapping(value = "/exportHeaders", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = "text/csv; charset=BIG5")
  public ResponseEntity<byte[]> exportHeaders(@RequestBody CsvData payload) {
    return buildCsvResponse(payload);
  }

  @PostMapping(value = "exportDetails", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = "text/csv; charset=BIG5")
  public ResponseEntity<byte[]> exportDetails(@RequestBody CsvData payload) {
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
