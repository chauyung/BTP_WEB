package nccc.btp.controller;

import nccc.btp.vo.IncomePersonVo;
import nccc.btp.vo.IncomeTaxDetailVo;
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
import nccc.btp.entity.NcccIncomePerson;
import nccc.btp.entity.NcccIncomeTaxDetail;
import nccc.btp.entity.NcccIncomeTaxParameter;
import nccc.btp.service.NcccIncomePersonService;
import nccc.btp.service.NcccIncomeTaxCategoryService;
import nccc.btp.vo.NcccIncomePersonVo;

import java.util.List;

@RestController
@RequestMapping("/ncccIncomePerson")
public class NcccIncomePersonController {

  protected static Logger LOG = LoggerFactory.getLogger(NcccIncomePersonController.class);

  @Autowired
  private NcccIncomePersonService ncccIncomePersonService;

  @Autowired
  private NcccIncomeTaxCategoryService ncccIncomeTaxCategoryService;

  @GetMapping("/init")
  public ResponseEntity<NcccIncomePersonVo> init() {
    NcccIncomePersonVo ncccIncomePersonVo = new NcccIncomePersonVo();
    ncccIncomePersonVo
            .setCertificateCategoryList(ncccIncomeTaxCategoryService.findCertificateCategory());
    ncccIncomePersonVo.setErrorNoteList(ncccIncomeTaxCategoryService.findErrorNote());
    ncccIncomePersonVo.setIncomeCategoryList(ncccIncomeTaxCategoryService.findIncomeCategory());
    ncccIncomePersonVo.setIncomeNoteList(ncccIncomeTaxCategoryService.findIncomeNote());
    ncccIncomePersonVo.setSoftwareNoteList(ncccIncomeTaxCategoryService.findSoftwareNote());
    ncccIncomePersonVo.setChargeCodeList(ncccIncomeTaxCategoryService.findChargeCode());
    ncccIncomePersonVo.setOtherIncomeList(ncccIncomeTaxCategoryService.findOtherIncome());
    ncccIncomePersonVo.setBusinessSectorList(ncccIncomeTaxCategoryService.findBusinessSector());
    ncccIncomePersonVo.setCountryCodeList(ncccIncomeTaxCategoryService.findCountryCode());
    return ResponseEntity.ok(ncccIncomePersonVo);
  }

  @GetMapping("/findAll")
  public ResponseEntity<List<IncomePersonVo>> findAll() {
    return ResponseEntity.ok(ncccIncomePersonService.findAll());
  }

  @GetMapping("/getIncomeTaxParameter")
  public ResponseEntity<NcccIncomeTaxParameter> getIncomeTaxParameter() {
    return ResponseEntity.ok(ncccIncomePersonService.getIncomeTaxParameter());
  }

  @GetMapping("/getIncomeTaxDetailListByPaymentYear")
  public ResponseEntity<List<IncomeTaxDetailVo>> getIncomeTaxDetailListByPaymentYear(String paymentYear) {
    return ResponseEntity.ok(ncccIncomePersonService.getIncomeTaxDetailListByPaymentYear(paymentYear));
  }

  @GetMapping("/getIncomeTaxDetailList")
  public ResponseEntity<List<IncomeTaxDetailVo>> getIncomeTaxDetailList(long pkId) {
    return ResponseEntity.ok(ncccIncomePersonService.getIncomeTaxDetailList(pkId));
  }

  @PostMapping("/addNcccIncomePerson")
  public ResponseEntity<NcccIncomePerson> addNcccIncomePerson(
      @RequestBody NcccIncomePerson ncccIncomePerson) {
    return ResponseEntity.ok(ncccIncomePersonService.add(ncccIncomePerson));
  }

  @PostMapping("/updateNcccIncomePerson")
  public ResponseEntity<NcccIncomePerson> updateNcccIncomePerson(
      @RequestBody NcccIncomePerson ncccIncomePerson) {
    return ResponseEntity.ok(ncccIncomePersonService.update(ncccIncomePerson));
  }

  @PostMapping("/deleteNcccIncomePerson")
  public ResponseEntity<String> deleteNcccIncomePerson(
      @RequestBody NcccIncomePerson ncccIncomePerson) {
    try {
      String message = ncccIncomePersonService.delete(ncccIncomePerson.getPkId());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PostMapping("/addNcccIncomeTaxDetail")
  public ResponseEntity<NcccIncomeTaxDetail> addNcccIncomeTaxDetail(
          @RequestBody IncomeTaxDetailVo vo) {
    return ResponseEntity.ok(ncccIncomePersonService.addTaxDetail(vo));
  }

  @PostMapping("/updateNcccIncomeTaxDetail")
  public ResponseEntity<NcccIncomeTaxDetail> updateNcccIncomeTaxDetail(
          @RequestBody IncomeTaxDetailVo vo) {
    return ResponseEntity.ok(ncccIncomePersonService.updateTaxDetail(vo));
  }

  @PostMapping("/deleteNcccIncomeTaxDetail")
  public ResponseEntity<String> deleteNcccIncomeTaxDetail(
          @RequestBody IncomeTaxDetailVo vo) {
    try {
      String message = ncccIncomePersonService.deleteTaxDetail(vo.getId());
      return ResponseEntity.ok(message); // 200 OK
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  // region 所得申報資料產生



  // endregion
}
