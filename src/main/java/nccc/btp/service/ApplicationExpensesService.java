package nccc.btp.service;

import nccc.btp.entity.SyncOU;
import nccc.btp.entity.SyncUser;
import nccc.btp.vo.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

public interface ApplicationExpensesService {

  ApplicationExpensesVo init();

  //董監事Init
  DSApplicationExpensesVo initDS();

  // 研發委員Init
  CApplicationExpensesVo initC();

  String startProcess(BpmExMVo vo, List<MultipartFile> files);

  String startDSCProcess(BpmExMVo vo, List<MultipartFile> files);

  ApplicationExpensesVo query(String exNo);

  DSApplicationExpensesVo queryDS(String exNo);

  CApplicationExpensesVo queryC(String exNo);

  String decision(DecisionVo vo);

  String accountingDecision(ApplicationExpensesDecisionVo vo);

  String accountingDSCDecision(ApplicationExpensesDecisionVo vo);

  String setNextAssignee(AssignTasksVo assignTasksVo);

  String update(BpmExMVo vo, List<MultipartFile> files);

  String updateDSC(BpmExMVo vo, List<MultipartFile> files);

  /**
   * 取得預付單號列表
   * @return
   */
  List<BpmPrepaidOrderVo> getPrepaidOrderList(String empNo);

  /**
   * 取得零用金撥補對應明晰
   * @return
   */
  List<BpmExMDetailVo> getPettyCashClaimDetails();

    /**
     * 上傳 中華電信繳費檔案
     * @return
     */
  List<BpmExMDetailVo> uploadCHTBill(MultipartFile file,String itemText);

  /**
   * 檢查是否為中華電信預付單
   * @param exNo
   * @return
   */
  Boolean checkCHTPrepaidOrder(String exNo);

  /**
   * 上傳 中華電信銷帳檔案
   * @param file
   * @return
   */
  List<BpmExMDetailVo> uploadCHTWriteOff(MultipartFile file,String exNo);

  /**
   * 上傳 沒收卡獎金檔案
   * @param fileName
   * @return
   */
  BpmExMDetailVo uploadPickUpCardBouns(String fileName);

  /**
   * 下載範本檔案
   * @return
   */
  byte[] downloadTemplateFile();

  /**
   * 上傳憑證檔案
   * @param file
   * @return
   */
  List<BpmExMDetailVo>  uploadCertificateData(MultipartFile file);

  /**
   * 拋轉RFC
   * @return
   */
  String toSAP(String exNo);

  // 產生董監事明細
  List<BpmExMPplVo> convertDSPplDetails(List<String> selectedDS , BigDecimal travelFeesAmount , BigDecimal attendFeesAmount);

  // 產生研發委員明細
  List<BpmExMPplVo> convertCPplDetails(List<String> selectedC ,BigDecimal researchFeesAmount , BigDecimal attendFeesAmount , BigDecimal allowanceAmount);

  /**
   * 取得用戶資料集
   */
  List<SyncUser> findSyncUserAll();

  /**
   * 取得用戶部門資料集
   * @return
   */
  List<SyncOU> findSyncOUAll();

  byte[] getVoucherStrickerWord(String exNo);

  String getApplicationExpensesType(String exNo);
}
