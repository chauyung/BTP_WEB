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
import nccc.btp.entity.NcccAssetsManager;
import nccc.btp.service.AssetsManagerService;

@RestController
@RequestMapping("/assetsManager")
public class AssetsManagerController {

  protected static Logger LOG = LoggerFactory.getLogger(AssetsManagerController.class);

  @Autowired
  private AssetsManagerService assetsManagerService;

  @GetMapping("/findAll")
  public ResponseEntity<List<NcccAssetsManager>> findAll() {
    return ResponseEntity.ok(assetsManagerService.findAll());
  }

  @PostMapping("/addNcccAssetsManager")
  public ResponseEntity<NcccAssetsManager> addNcccAssetsManager(
      @RequestBody NcccAssetsManager ncccAssetsManager) {
    return ResponseEntity.ok(assetsManagerService.add(ncccAssetsManager));
  }

  @PostMapping("/updateNcccAssetsManager")
  public ResponseEntity<NcccAssetsManager> updateNcccAssetsManager(
      @RequestBody NcccAssetsManager ncccAssetsManager) {
    return ResponseEntity.ok(assetsManagerService.update(ncccAssetsManager));
  }

  @PostMapping("/deleteNcccAssetsManager")
  public ResponseEntity<String> deleteNcccAssetsManager(
      @RequestBody NcccAssetsManager ncccAssetsManager) {
    try {
      String message = assetsManagerService.delete(ncccAssetsManager.getAssetsCode());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
