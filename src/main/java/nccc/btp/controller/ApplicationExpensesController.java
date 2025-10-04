package nccc.btp.controller;

import nccc.btp.dto.BudgetResponse;
import nccc.btp.entity.*;
import nccc.btp.service.*;
import nccc.btp.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import nccc.btp.dto.PayMethodSelectOption;
import nccc.btp.dto.SupplierInfo;
import nccc.btp.dto.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 費用申請單Controller
 */
@RestController
@RequestMapping("/applicationExpenses")
public class ApplicationExpensesController {

  protected static Logger LOG = LoggerFactory.getLogger(ApplicationExpensesController.class);

  @Autowired
  private ApplicationExpensesService applicationExpensesService;

  @Autowired
  private PayMethodService payMethodService;

  @Autowired
  private ZmmtSupplierService zmmtSupplierService;

  @Autowired
  private NcccDepositBankService ncccDepositBankService;

  @Autowired
  private NcccIncomePersonService ncccIncomePersonService;

  /**
   * 費用申請單初始化
   */
  @GetMapping("/init")
  public ApplicationExpensesVo init() {
    return applicationExpensesService.init();
  }

  /**
   * 董監事費用申請單初始化
   */
  @GetMapping("/initDS")
  public DSApplicationExpensesVo initDS() {
    return applicationExpensesService.initDS();
  }

  /**
   * 研發委員費用申請單初始化
   */
  @GetMapping("/initC")
  public CApplicationExpensesVo initC() {
    return applicationExpensesService.initC();
  }

  /**
   * 費用申請單建立
   */
  @PostMapping("/startProcess")
  public ResponseEntity<String> startProcess(@RequestPart(value = "vo") BpmExMVo vo,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {
    return ResponseEntity.ok(applicationExpensesService.startProcess(vo, files));
  }

  /**
   * 董監事及研發委員費用申請單建立
   */
  @PostMapping("/startDSCProcess")
  public ResponseEntity<String> startDSCProcess(@RequestPart(value = "vo") BpmExMVo vo,
                                             @RequestPart(value = "files", required = false) List<MultipartFile> files) {
    return ResponseEntity.ok(applicationExpensesService.startDSCProcess(vo, files));
  }

  /**
   * 取得費用申請單資料
   */
  @GetMapping("/query")
  public ResponseEntity<ApplicationExpensesVo> query(@RequestParam("exNo") String exNo) {
    return ResponseEntity.ok(applicationExpensesService.query(exNo));
  }

  /**
   * 取得董監事費用申請單資料
   */
  @GetMapping("/queryDS")
  public ResponseEntity<DSApplicationExpensesVo> queryDS(@RequestParam("exNo") String exNo) {
    return ResponseEntity.ok(applicationExpensesService.queryDS(exNo));
  }

  /**
   * 取得研發委員費用申請單資料
   */
  @GetMapping("/queryC")
  public ResponseEntity<CApplicationExpensesVo> queryC(@RequestParam("exNo") String exNo) {
    return ResponseEntity.ok(applicationExpensesService.queryC(exNo));
  }

  /**
   * 費用申請單編輯
   */
  @PostMapping("/update")
  public ResponseEntity<String> update(@RequestPart("vo") BpmExMVo vo,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {
    return ResponseEntity.ok(applicationExpensesService.update(vo, files));
  }

  /**
   * 董監事及研發委員費用申請單編輯
   */
  @PostMapping("/updateDSC")
  public ResponseEntity<String> updateDSC(@RequestPart("vo") BpmExMVo vo,
                                       @RequestPart(value = "files", required = false) List<MultipartFile> files) {
    return ResponseEntity.ok(applicationExpensesService.updateDSC(vo, files));
  }

  /**
   * 費用申請單拋轉sap
   */
  @PostMapping("/toSAP")
  public ResponseEntity<String> toSAP(@RequestBody BpmExMVo vo) {
    return ResponseEntity.ok(applicationExpensesService.toSAP(vo.getExNo()));
  }

  /**
   * 上傳 中華電信繳費檔案
   * @param file
   * @return
   */
  @PostMapping("/uploadCHTBill")
  public ResponseEntity<List<BpmExMDetailVo>> uploadCHTBill(@RequestPart(value = "file") MultipartFile file,@RequestPart(value = "itemText") String itemText)  {
    return ResponseEntity.ok(applicationExpensesService.uploadCHTBill(file,itemText));
  }

  /**
   * 檢查預付單號是否為中華電信預付單
   */
  @GetMapping("/checkCHTPrepaidOrder")
  public ResponseEntity<Boolean> checkCHTPrepaidOrder(@RequestParam("exNo") String exNo) {
    return ResponseEntity.ok(applicationExpensesService.checkCHTPrepaidOrder(exNo));
  }

  /**
   * 上傳 中華電信銷帳檔案
   * @param file
   * @return
   */
  @PostMapping("/uploadCHTWriteOff")
  public ResponseEntity<List<BpmExMDetailVo>> uploadCHTWriteOff(@RequestPart(value = "file") MultipartFile file ,@RequestPart(value = "exNo") String exNo ){
    return ResponseEntity.ok(applicationExpensesService.uploadCHTWriteOff(file,exNo));
  }

    /**
   * 上傳 沒收卡獎金檔案
   * @param fileName
   * @return
   */
  @PostMapping("/uploadPickUpCardBouns")
  public ResponseEntity<BudgetResponse> uploadPickUpCardBouns(@RequestPart(value = "fileName") String fileName) {
    BudgetResponse resp = new BudgetResponse();

    try {
      resp.success = true;
      resp.setData(applicationExpensesService.uploadPickUpCardBouns(fileName));
    }catch (Exception e){
      resp.success = false;
      resp.msg = e.getMessage();
    }
    return ResponseEntity.ok(resp);

  }

  /**
   * 下載範本檔案
   * @return
   */
  @GetMapping("/downloadTemplateFile")
  public ResponseEntity<byte[]> downloadTemplateFile() {
    byte[] data = applicationExpensesService.downloadTemplateFile();
    if (data == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(applicationExpensesService.downloadTemplateFile());
  }

  /**
   * 上傳憑證檔案
   * @param file
   * @return
   */
  @PostMapping("/uploadCertificateData")
  public ResponseEntity<List<BpmExMDetailVo>> uploadCertificateData(@RequestPart(value = "file") MultipartFile file) {
    return ResponseEntity.ok(applicationExpensesService.uploadCertificateData(file));
  }

  /**
   * 產生董監事明細
   */
  @PostMapping("/convertDSPplDetails")
  public ResponseEntity<List<BpmExMPplVo>> convertDSPplDetails(@RequestPart(value = "selectedDS") List<String> selectedDS,@RequestPart(value = "travelFeesAmount") BigDecimal travelFeesAmount,@RequestPart(value = "attendFeesAmount") BigDecimal attendFeesAmount)  {
    return ResponseEntity.ok(applicationExpensesService.convertDSPplDetails(selectedDS,travelFeesAmount,attendFeesAmount));
  }

  /**
   * 產生研發委員明細
   */
  @PostMapping("/convertCPplDetails")
  public ResponseEntity<List<BpmExMPplVo>> convertCPplDetails(@RequestPart(value = "selectedC") List<String> selectedC,@RequestPart(value = "researchFeesAmount") BigDecimal researchFeesAmount,@RequestPart(value = "attendFeesAmount") BigDecimal attendFeesAmount, @RequestPart(value = "allowanceAmount")BigDecimal allowanceAmount)  {
    return ResponseEntity.ok(applicationExpensesService.convertCPplDetails(selectedC,researchFeesAmount,attendFeesAmount,allowanceAmount));
  }

  /**
   * 取得付款方式選項
   */
  @GetMapping("/getPayMethodOptions")
  public ResponseEntity<List<PayMethodSelectOption>> getPayMethodOptions() {

    List<PayMethodSelectOption> ResultDatas = new ArrayList<PayMethodSelectOption>();

    List<NcccPayMethod> Source = payMethodService.findAll();

    for (final NcccPayMethod Data : Source) {

      PayMethodSelectOption WarteData = new PayMethodSelectOption(Data.getZlsch(), Data.getText1());

      ResultDatas.add(WarteData);

    }

    return ResponseEntity.ok(ResultDatas);

  }

  /**
   * 取得員工資料集
   */
  @GetMapping("/getSyncUserDatas")
  public ResponseEntity<List<UserInfo>> getSyncUserDatas() {

    List<UserInfo> ResultDatas = new ArrayList<UserInfo>();

    List<SyncUser> Source = applicationExpensesService.findSyncUserAll();

    List<SyncOU> OUSource = applicationExpensesService.findSyncOUAll();

    for (final SyncUser Data : Source) {

      if (Data.getDisabled().equals("0")) {

        String CostCenterName = "";

        Optional<SyncOU> foundSOU = OUSource.stream()
                .filter(x -> x.getOuCode().equals( Data.getOuCode())).findFirst();

        if(foundSOU.isPresent())
        {
          CostCenterName = foundSOU.get().getOuName();
        }

        UserInfo WriteData = new UserInfo(Data.getHrid(), Data.getDisplayName(), Data.getCostCenter(),CostCenterName);

        ResultDatas.add(WriteData);

      }

    }

    return ResponseEntity.ok(ResultDatas);

  }

  /**
   * 取得供應商資料集
   */
  @GetMapping("/getSupplierDatas")
  public ResponseEntity<List<SupplierInfo>> getSupplierDatas() {

    List<SupplierInfo> ResultDatas = new ArrayList<SupplierInfo>();

    List<ZmmtSupplier> Source = zmmtSupplierService.findAll();

    for (final ZmmtSupplier Data : Source) {

      SupplierInfo WarteData = new SupplierInfo(Data.getLand1(), Data.getPartner(), Data.getNameOrg1(), Data.getTaxnum());

      ResultDatas.add(WarteData);

    }

    return ResponseEntity.ok(ResultDatas);

  }

  /**
   * 取得銀行存款資料集
   */
  @GetMapping("/getDepositBankDatas")
  public ResponseEntity<List<NcccDepositBank>> getDepositBankDatas() {

    List<NcccDepositBank> ResultDatas = ncccDepositBankService.findAll();

    return ResponseEntity.ok(ResultDatas);

  }

  /**
   * 取得預付單號選單
   */
  @GetMapping("/getPrepaidOrderList")
  public ResponseEntity<List<BpmPrepaidOrderVo>> getPrepaidOrderList(@RequestParam("empNo") String empNo) {

    return ResponseEntity.ok(applicationExpensesService.getPrepaidOrderList(empNo));

  }

  /**
   * 取得零用金撥補資料
   * @return
   */
  @GetMapping("/getPettyCashClaimDetails")
  public ResponseEntity<List<BpmExMDetailVo>> getPettyCashClaimDetails() {
    return ResponseEntity.ok(applicationExpensesService.getPettyCashClaimDetails());
  }

  /**
   * 費用申請單簽核動作判斷
   */
  @PostMapping("/decision")
  public ResponseEntity<String> decision(@RequestBody DecisionVo vo) {
    return ResponseEntity.ok(applicationExpensesService.decision(vo));
  }

  /**
   * 費用申請單簽核動作判斷(清會部用)
   */
  @PostMapping("/accountingDecision")
  public ResponseEntity<String> accountingDecision(@RequestBody ApplicationExpensesDecisionVo vo) {
    return ResponseEntity.ok(applicationExpensesService.accountingDecision(vo));
  }

  /**
   * 董監事及研發委員費用申請單簽核動作判斷(清會部用)
   */
  @PostMapping("/accountingDSCDecision")
  public ResponseEntity<String> accountingDSCDecision(@RequestBody ApplicationExpensesDecisionVo vo) {
    return ResponseEntity.ok(applicationExpensesService.accountingDSCDecision(vo));
  }

  /**
   * 簽核流程指派人員
   */
  @PostMapping("/setNextAssignee")
  public ResponseEntity<String> setNextAssignee(@RequestBody AssignTasksVo assignTasksVo) {
    return ResponseEntity.ok(applicationExpensesService.setNextAssignee(assignTasksVo));
  }

  /**
   * 取得預付單號選單
   */
  @GetMapping("/getIncomePersonByIdAndName")
  public ResponseEntity<IncomePersonVo> getIncomePersonByIdAndName(@RequestParam("id") String id,@RequestParam("name") String name) {

    return ResponseEntity.ok(ncccIncomePersonService.GetIncomePersonByIdAndName(id,name));

  }

  /**
   * 下載憑證貼黏單
   * @return
   */
  @GetMapping("/getVoucherStrickerWord")
  public ResponseEntity<byte[]> getVoucherStrickerWord(String exNo) {
    byte[] data = applicationExpensesService.getVoucherStrickerWord(exNo);
    if (data == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(applicationExpensesService.getVoucherStrickerWord(exNo));
  }

  /**
   * 檢查費用申請單類型
   * @param exNo
   * @return
   */
  @GetMapping("/getApplicationExpensesType")
  public ResponseEntity<String> getApplicationExpensesType(@RequestParam("exNo") String exNo) {
    return ResponseEntity.ok(applicationExpensesService.getApplicationExpensesType(exNo));
  }

}
