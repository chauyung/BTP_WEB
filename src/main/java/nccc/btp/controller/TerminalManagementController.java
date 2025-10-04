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
import org.springframework.web.bind.annotation.RestController;
import nccc.btp.entity.NcccEmsMgm;
import nccc.btp.service.TerminalManagementService;
import nccc.btp.vo.BulkEmsUpdateVo;
import nccc.btp.vo.TerminalManagementVo;

@RestController
@RequestMapping("/terminalManagement")
public class TerminalManagementController {

  protected static Logger LOG = LoggerFactory.getLogger(TerminalManagementController.class);

  @Autowired
  private TerminalManagementService terminalManagementService;

  @GetMapping("/search")
  public ResponseEntity<List<TerminalManagementVo>> search(
      @RequestParam(value = "ncccWealthNo", required = false) String ncccWealthNo,
      @RequestParam(value = "wealthNo", required = false) String wealthNo) {
    return ResponseEntity.ok(terminalManagementService.search(ncccWealthNo, wealthNo));
  }

  @PostMapping("/update")
  public ResponseEntity<String> update(@RequestBody List<NcccEmsMgm> ncccEmsMgmList) {
    return ResponseEntity.ok(terminalManagementService.update(ncccEmsMgmList));
  }

  @PostMapping("/toSAP")
  public ResponseEntity<String> toSAP(@RequestBody List<NcccEmsMgm> ncccEmsMgmList) {
    return ResponseEntity.ok(terminalManagementService.toSAP(ncccEmsMgmList));
  }

  @GetMapping("/downloadCsvSample")
  public ResponseEntity<Resource> downloadCsvSample() {
    Resource resource;
    try {
      resource = terminalManagementService.getTerminalManagementCsvSample();
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment");
      headers.setContentType(MediaType.parseMediaType("text/csv; charset=Big5"));
      return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/ledgerToScrap")
  public ResponseEntity<String> ledgerToScrap(@RequestBody List<BulkEmsUpdateVo> voList) {
    return ResponseEntity.ok(terminalManagementService.ledgerToScrap(voList));
  }

  @PostMapping("/ledgerToListed")
  public ResponseEntity<String> ledgerToListed(@RequestBody List<BulkEmsUpdateVo> voList) {
    return ResponseEntity.ok(terminalManagementService.ledgerToListed(voList));
  }

  @PostMapping("/listedImpair")
  public ResponseEntity<String> listedImpair(@RequestBody List<BulkEmsUpdateVo> voList) {
    return ResponseEntity.ok(terminalManagementService.listedImpair(voList));
  }

}
