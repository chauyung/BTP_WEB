package nccc.btp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import nccc.btp.service.AcceptanceFormService;
import nccc.btp.vo.AcceptanceFormVo;

@RestController
@RequestMapping("/acceptanceForm")
public class AcceptanceFormController {

  @Autowired
  AcceptanceFormService acceptanceFormService;

  // 初始化
  @GetMapping("/init")
  public ResponseEntity<AcceptanceFormVo> init(@RequestParam("revNo") String revNo) {
    return ResponseEntity.ok(acceptanceFormService.init(revNo));

  }
}
