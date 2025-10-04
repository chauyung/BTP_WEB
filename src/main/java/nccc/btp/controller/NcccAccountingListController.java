package nccc.btp.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nccc.btp.entity.NcccAccountingList;
import nccc.btp.service.NcccAccountingListService;

@RestController
@RequestMapping("/ncccAccountingList")
public class NcccAccountingListController {

  protected static Logger LOG = LoggerFactory.getLogger(NcccAccountingListController.class);

  @Autowired
  private NcccAccountingListService ncccaccountinglistService;

  @GetMapping("/findAll")
  public ResponseEntity<List<NcccAccountingList>> findAll() {
    return ResponseEntity.ok(ncccaccountinglistService.findAll());
  }

  @PostMapping("/addNcccAccountingList")
  public ResponseEntity<NcccAccountingList> addNcccAccountingList(@RequestBody NcccAccountingList ncccAccountingList) {
    return ResponseEntity.ok(ncccaccountinglistService.add(ncccAccountingList));
  }

  @PostMapping("/updateNcccAccountingList")
  public ResponseEntity<NcccAccountingList> updateNcccAccountingList(@RequestBody NcccAccountingList ncccAccountingList) {
    return ResponseEntity.ok(ncccaccountinglistService.update(ncccAccountingList));
  }

  @PostMapping("/deleteNcccAccountingList")
  public ResponseEntity<String> deleteNcccAccountingList(@RequestBody NcccAccountingList ncccAccountingList) {
    try {
      String message = ncccaccountinglistService.delete(ncccAccountingList.getSubject());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
