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
import nccc.btp.entity.NcccOperateItems;
import nccc.btp.service.NcccOperateItemsService;

@RestController
@RequestMapping("/ncccOperateItems")
public class NcccOperateItemsController {

  protected static Logger LOG = LoggerFactory.getLogger(NcccOperateItemsController.class);

  @Autowired
  private NcccOperateItemsService ncccOperateItemsService;

  @GetMapping("/findAll")
  public ResponseEntity<List<NcccOperateItems>> findAll() {
    return ResponseEntity.ok(ncccOperateItemsService.findAll());
  }

  @PostMapping("/addNcccOperateItems")
  public ResponseEntity<NcccOperateItems> addNcccNcccOperateItems(@RequestBody NcccOperateItems ncccOperateItems) {
    return ResponseEntity.ok(ncccOperateItemsService.add(ncccOperateItems));
  }

  @PostMapping("/updateNcccOperateItems")
  public ResponseEntity<NcccOperateItems> updateNcccNcccOperateItems(@RequestBody NcccOperateItems ncccOperateItems) {
    return ResponseEntity.ok(ncccOperateItemsService.update(ncccOperateItems));
  }

  @PostMapping("/deleteNcccOperateItems")
  public ResponseEntity<String> deleteNcccNcccOperateItems(@RequestBody NcccOperateItems ncccOperateItems) {
    try {
      String message = ncccOperateItemsService.delete(ncccOperateItems.getOperateItemCode());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
