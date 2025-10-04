package nccc.btp.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import nccc.btp.dto.CategoryOptionDto;
import nccc.btp.dto.PurchaseRequestDto;
import nccc.btp.entity.NcccCostCenter;
import nccc.btp.service.PurchaseRequistionService;

@RestController
@RequestMapping("/purchaseRequistion")
public class PurchaseRequistionController {
  protected static Logger LOG = LoggerFactory.getLogger(PurchaseRequistionController.class);
  
  @Autowired
  private PurchaseRequistionService purchaseRequistionService;


  @PostMapping(path = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> submit(@RequestPart("purchaseRequest") PurchaseRequestDto dto,
      @RequestPart(name = "attachment", required = false) List<MultipartFile> attachment,
      @RequestPart(name = "quotationFiles", required = false) List<MultipartFile> quotationFiles) {
    try {
      purchaseRequistionService.createAndStartProcess(dto, attachment, quotationFiles);
      return ResponseEntity.ok().body("已接收 " + dto.getDetails().size() + " 筆明細，"
          + (attachment != null ? "有附件，" : "無附件，") + (quotationFiles != null ? "有詢價單" : "無詢價單"));
    } catch (Exception e) {
      return ResponseEntity.status(500).body("處理失敗：" + e.getMessage());
    }
  }
  
  @GetMapping("/costCenters")
  public ResponseEntity<List<NcccCostCenter>> getCostCenters() {
    List<NcccCostCenter> centers = purchaseRequistionService.getCostCenters();
    return ResponseEntity.ok(centers);
  }

  @GetMapping("/categoryOptions")
  public List<CategoryOptionDto> categoryOptions() {
      return purchaseRequistionService.listCategoryOptions();
  }

  @GetMapping("/query")
  public ResponseEntity<PurchaseRequestDto> query(@RequestParam("prNo") String prNo) {
    return ResponseEntity.ok(purchaseRequistionService.query(prNo));
  }
}
