package nccc.btp.controller;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import nccc.btp.service.TravelExpensesService;
import nccc.btp.vo.AssignTasksVo;
import nccc.btp.vo.DecisionVo;
import nccc.btp.vo.TravelExpensesAccountingDecisionVo;
import nccc.btp.vo.TravelExpensesVo;

@RestController
@RequestMapping("/travelExpenses")
public class TravelExpensesController {

  protected static Logger LOG = LoggerFactory.getLogger(TravelExpensesController.class);

  @Autowired
  private TravelExpensesService travelExpensesService;

  @GetMapping("/init")
  public ResponseEntity<TravelExpensesVo> init() {
    return ResponseEntity.ok(travelExpensesService.init());
  }

  @PostMapping("/startProcess")
  public ResponseEntity<String> startProcess(@RequestPart("vo") TravelExpensesVo vo,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {
    return ResponseEntity.ok(travelExpensesService.startProcess(vo, files));
  }

  @GetMapping("/query")
  public ResponseEntity<TravelExpensesVo> query(@RequestParam("btNo") String btNo) {
    return ResponseEntity.ok(travelExpensesService.query(btNo));
  }

  @PostMapping("/decision")
  public ResponseEntity<String> decision(@RequestBody DecisionVo vo) {
    return ResponseEntity.ok(travelExpensesService.decision(vo));
  }
  
  @PostMapping("/accountingDecision")
  public ResponseEntity<String> accountingDecision(@RequestBody TravelExpensesAccountingDecisionVo vo) {
    return ResponseEntity.ok(travelExpensesService.accountingDecision(vo));
  }

  @PostMapping("/update")
  public ResponseEntity<String> update(@RequestPart("vo") TravelExpensesVo vo,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {
    return ResponseEntity.ok(travelExpensesService.update(vo, files));
  }

  @PostMapping("/toSAP")
  public ResponseEntity<String> toSAP(@RequestBody TravelExpensesVo vo) {
    return ResponseEntity.ok(travelExpensesService.toSAP(vo));
  }

  @PostMapping("/setNextAssignee")
  public ResponseEntity<String> setNextAssignee(@RequestBody AssignTasksVo assignTasksVo) {
    return ResponseEntity.ok(travelExpensesService.setNextAssignee(assignTasksVo));
  }

  @GetMapping("/downloadCsvSample")
  public ResponseEntity<Resource> downloadCsvSample() {
    Resource resource;
    try {
      resource = travelExpensesService.getTravelExpensesCsvSample();
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment");
      headers.setContentType(MediaType.parseMediaType("text/csv; charset=Big5"));
      return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * 下載憑證貼黏單
   * @return
   */
  @GetMapping("/getVoucherStrickerWord")
  public ResponseEntity<byte[]> getVoucherStrickerWord(String btNo) {
    byte[] data = travelExpensesService.getVoucherStrickerWord(btNo);
    if (data == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(travelExpensesService.getVoucherStrickerWord(btNo));
  }
}
