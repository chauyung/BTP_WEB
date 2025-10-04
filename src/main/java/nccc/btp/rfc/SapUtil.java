package nccc.btp.rfc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

@Component
public class SapUtil {

  protected static Logger LOG = LoggerFactory.getLogger(SapUtil.class);

  private final RFCManager rfcManager;

  public SapUtil(RFCManager rfcManager) {
    this.rfcManager = rfcManager;
  }

  public JCoFunction getFunction(JCoDestination destination, String functionName) {
    try {
      ensureStateful(destination);
      JCoFunction function = destination.getRepository().getFunction(functionName);
      if (function == null) {
        LOG.error("Function not found: " + functionName);
        throw new RuntimeException("Function not found: " + functionName);
      }
      return function;
    } catch (JCoException e) {
      LOG.error(e.getMessage());
      throw new RuntimeException("取得 function 發生錯誤: " + functionName, e);
    }
  }

  public ZReturn callZcreateAccDocument(ZHeader zheader, List<ZItem> zitem) throws Exception {
    LOG.info("@@@@@@@@@@@ 進callrfc");
    ZReturn zreturn = new ZReturn();
    JCoDestination destination = null;
    try {
      destination = rfcManager.getDestination();
      ensureStateful(destination);
      JCoFunction function = getFunction(destination, "ZCREATE_ACC_DOCUMENT");
      JCoStructure header = function.getImportParameterList().getStructure("ZHEADER");
      LOG.info("@@@@@@@@@@@ 處理完連線 塞資料");
      header.setValue("ZSYSTEM", zheader.getZSYSTEM());
      header.setValue("BUKRS", zheader.getBUKRS());
      header.setValue("BLDAT", zheader.getBLDAT());
      header.setValue("BUDAT", zheader.getBUDAT());
      header.setValue("BLART", zheader.getBLART());
      header.setValue("WAERS", zheader.getWAERS());
      header.setValue("KURSF", zheader.getKURSF());
      header.setValue("XBLNR", zheader.getXBLNR());
      header.setValue("BKTXT", zheader.getBKTXT());
      header.setValue("XREF1_HD", zheader.getXREF1_HD());
      LOG.info("ZHeader: " + zheader.toString());
      JCoParameterList importParam = function.getImportParameterList();
      importParam.setValue("ZHEADER", header);
      JCoParameterList tableParamList = function.getTableParameterList();
      JCoTable itemTable = tableParamList.getTable("ZITEM");
      for (ZItem item : zitem) {
        itemTable.appendRow();
        itemTable.setValue("BSCHL", item.getBSCHL());
        itemTable.setValue("NEWKO", item.getNEWKO());
        itemTable.setValue("HKONT", item.getHKONT());
        itemTable.setValue("WRBTR", item.getWRBTR());
        itemTable.setValue("DMBTR", item.getDMBTR());
        itemTable.setValue("MWSKZ", item.getMWSKZ());
        itemTable.setValue("TXBFW", item.getTXBFW());
        itemTable.setValue("ZFBDT", item.getZFBDT());
        itemTable.setValue("KOSTL", item.getKOSTL());
        itemTable.setValue("ZUONR", item.getZUONR());
        itemTable.setValue("SGTXT", item.getSGTXT());
        itemTable.setValue("XREF1", item.getXREF1());
        itemTable.setValue("XREF2", item.getXREF2());
        itemTable.setValue("XREF3", item.getXREF3());
        LOG.info("ZItem: " + item.toString());
      }
      LOG.info("@@@@@@@@@@@ 執行rfc");
      function.execute(destination);
      JCoParameterList exportParam = function.getExportParameterList();
      JCoStructure zreturnObj = exportParam.getStructure("ZRETURN");
      zreturn.setTYPE(zreturnObj.getString("TYPE"));
      zreturn.setBUKRS(zreturnObj.getString("BUKRS"));
      zreturn.setBELNR(zreturnObj.getString("BELNR"));
      zreturn.setGJAHR(zreturnObj.getString("GJAHR"));
      zreturn.setMESSAGE(zreturnObj.getString("MESSAGE"));
    } catch (Exception e) {
      LOG.error(e.getMessage());
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      releaseContext(destination);
    }
    return zreturn;
  }

  public ZReturn callZretireAsset(ZInfo zinfo) throws Exception {
    ZReturn zreturn = new ZReturn();
    JCoDestination destination = null;
    try {
      destination = rfcManager.getDestination();
      ensureStateful(destination);
      JCoFunction function = getFunction(destination, "ZRETIRE_ASSET");
      JCoStructure info = function.getImportParameterList().getStructure("ZINFO");
      LOG.info("@@@@@@@@@@@ 處理完連線 塞資料");
      info.setValue("ZSYSTEM", zinfo.getZSYSTEM());
      info.setValue("ZEDCNO", zinfo.getZEDCNO());
      info.setValue("BUKRS", zinfo.getBUKRS());
      info.setValue("ANLN1", zinfo.getANLN1());
      info.setValue("ANLN2", zinfo.getANLN2());
      info.setValue("BLDAT", zinfo.getBLDAT());
      info.setValue("BUDAT", zinfo.getBUDAT());
      info.setValue("BZDAT", zinfo.getBZDAT());
      info.setValue("SGTXT", zinfo.getSGTXT());
      info.setValue("MENGE", zinfo.getMENGE());
      info.setValue("XAALT", zinfo.getXAALT());
      info.setValue("XANEU", zinfo.getXANEU());
      LOG.info("Zinfo: " + zinfo.toString());
      LOG.info("@@@@@@@@@@@ 執行rfc");
      function.execute(destination);
      JCoParameterList exportParam = function.getExportParameterList();
      JCoStructure zreturnObj = exportParam.getStructure("ZRETURN");
      zreturn.setTYPE(zreturnObj.getString("TYPE"));
      zreturn.setBUKRS(zreturnObj.getString("BUKRS"));
      zreturn.setBELNR(zreturnObj.getString("BELNR"));
      zreturn.setGJAHR(zreturnObj.getString("GJAHR"));
      zreturn.setMESSAGE(zreturnObj.getString("MESSAGE"));
    } catch (Exception e) {
      LOG.error(e.getMessage());
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      releaseContext(destination);
    }
    return zreturn;
  }

  public Map<String, String> callZAccfTwguiCheck01(List<TInput> tInputList) throws Exception {
    Map<String, String> result = new HashMap<>();
    JCoDestination destination = null;
    try {
      destination = rfcManager.getDestination();
      ensureStateful(destination);
      JCoFunction function = getFunction(destination, "Z_ACCF_TWGUI_CHECK_01");
      if (function == null) {
        throw new RuntimeException("Function not found");
      }
      LOG.info("@@@@@@@@@@@ 處理完連線 塞資料");
      JCoParameterList tableParamList = function.getTableParameterList();
      JCoTable itemTable = tableParamList.getTable("T_INPUT");
      for (TInput tInput : tInputList) {
        itemTable.appendRow();
        itemTable.setValue("BLDAT", tInput.getBLDAT());
        itemTable.setValue("VATDATE", tInput.getVATDATE());
        itemTable.setValue("XBLNR", tInput.getXBLNR());
        itemTable.setValue("ZFORM_CODE", tInput.getZFORM_CODE());
        itemTable.setValue("STCEG", tInput.getSTCEG());
        itemTable.setValue("HWBAS", tInput.getHWBAS());
        itemTable.setValue("HWSTE", tInput.getHWSTE());
        itemTable.setValue("TAX_TYPE", tInput.getTAX_TYPE());
        itemTable.setValue("CUS_TYPE", tInput.getCUS_TYPE());
        itemTable.setValue("AM_TYPE", tInput.getAM_TYPE());
        itemTable.setValue("BUPLA", "1010");
        itemTable.setValue("VATCODE", "1010");
        itemTable.setValue("BUKRS", "1010");
        LOG.info("ZInvoice: " + tInput.toString());
      }
      LOG.info("@@@@@@@@@@@ 執行rfc");
      function.execute(destination);
      JCoParameterList exportParam = function.getExportParameterList();
      result.put("E_ERROR", exportParam.getString("E_ERROR"));
      result.put("E_XBLNR", exportParam.getString("E_XBLNR"));
      result.put("E_MSG", exportParam.getString("E_MSG"));
    } catch (Exception e) {
      LOG.error(e.getMessage());
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      releaseContext(destination);
    }
    return result;
  }


  public ZReturn callZAccfTwguiInsert01(String bukrs, String belnr, String gjahr,
      List<TInput> tInputList) throws Exception {
    ZReturn zreturn = new ZReturn();
    JCoDestination destination = null;
    try {
      destination = rfcManager.getDestination();
      ensureStateful(destination);
      JCoFunction function = getFunction(destination, "Z_ACCF_TWGUI_INSERT_01");
      if (function == null) {
        throw new RuntimeException("Function not found");
      }
      LOG.info("@@@@@@@@@@@ 處理完連線 塞資料");
      JCoParameterList input = function.getImportParameterList();
      input.setValue("I_BUKRS", bukrs);
      input.setValue("I_BELNR", belnr);
      input.setValue("I_GJAHR", gjahr);
      JCoParameterList tableParamList = function.getTableParameterList();
      JCoTable itemTable = tableParamList.getTable("T_INPUT");
      for (TInput tInput : tInputList) {
        itemTable.appendRow();
        itemTable.setValue("BLDAT", tInput.getBLDAT());
        itemTable.setValue("VATDATE", tInput.getVATDATE());
        itemTable.setValue("XBLNR", tInput.getXBLNR());
        itemTable.setValue("ZFORM_CODE", tInput.getZFORM_CODE());
        itemTable.setValue("STCEG", tInput.getSTCEG());
        itemTable.setValue("HWBAS", tInput.getHWBAS());
        itemTable.setValue("HWSTE", tInput.getHWSTE());
        itemTable.setValue("TAX_TYPE", tInput.getTAX_TYPE());
        itemTable.setValue("CUS_TYPE", tInput.getCUS_TYPE());
        itemTable.setValue("AM_TYPE", tInput.getAM_TYPE());
        LOG.info("ZInvoice: " + tInput.toString());
      }
      LOG.info("@@@@@@@@@@@ 執行rfc");
      function.execute(destination);
    } catch (Exception e) {
      LOG.error(e.getMessage());
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      releaseContext(destination);
    }
    return zreturn;
  }

  public Map<String, String> callZAccfTwguiCheck11(List<T202> t202List) throws Exception {
    Map<String, String> result = new HashMap<>();
    JCoDestination destination = null;
    try {
      destination = rfcManager.getDestination();
      ensureStateful(destination);
      JCoFunction function = getFunction(destination, "Z_ACCF_TWGUI_CHECK_11");
      if (function == null) {
        throw new RuntimeException("Function not found");
      }
      LOG.info("@@@@@@@@@@@ 處理完連線 塞資料");
      JCoParameterList input = function.getImportParameterList();
      input.setValue("I_BUKRS", "1010");
      input.setValue("I_BUPLA", "1010");
      JCoParameterList tableParamList = function.getTableParameterList();
      JCoTable t202Table = tableParamList.getTable("T_202");
      for (T202 t202 : t202List) {
        t202Table.appendRow();
        t202Table.setValue("XC003", t202.getXC003());
        t202Table.setValue("XC011", t202.getXC011());
        t202Table.setValue("XC012", t202.getXC012());
        t202Table.setValue("XD003", t202.getXD003());
        t202Table.setValue("XD004", t202.getXD004());
        t202Table.setValue("XD005", t202.getXD005());
        t202Table.setValue("XD006", t202.getXD006());
        t202Table.setValue("XD007", t202.getXD007());
        t202Table.setValue("XD008", t202.getXD008());
        t202Table.setValue("XD009", t202.getXD009());
        t202Table.setValue("XD011", t202.getXD011());
        t202Table.setValue("XD012", t202.getXD012());
        t202Table.setValue("TA007", t202.getTA007());
        t202Table.setValue("TA008", t202.getTA008());
        t202Table.setValue("TA015", t202.getTA015());
        t202Table.setValue("TA016", t202.getTA016());
        t202Table.setValue("TA201", t202.getTA201());
        t202Table.setValue("TA204", t202.getTA204());
        t202Table.setValue("TA205", t202.getTA205());
        t202Table.setValue("XA001", t202.getXA001());
        LOG.info("T202Table: " + t202.toString());
      }
      LOG.info("@@@@@@@@@@@ 執行rfc");
      function.execute(destination);
      JCoParameterList exportParam = function.getExportParameterList();
      result.put("E_ERROR", exportParam.getString("E_ERROR"));
      result.put("E_XBLNR", exportParam.getString("E_XBLNR"));
      result.put("E_MSG", exportParam.getString("E_MSG"));
    } catch (Exception e) {
      LOG.error(e.getMessage());
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      releaseContext(destination);
    }
    return result;
  }

  public ZReturn callZAccfTwguiInsert11(String bukrs, String belnr, String gjahr,
      List<T202> t202List) throws Exception {
    ZReturn zreturn = new ZReturn();
    JCoDestination destination = null;
    try {
      destination = rfcManager.getDestination();
      ensureStateful(destination);
      JCoFunction function = getFunction(destination, "Z_ACCF_TWGUI_INSERT_11");
      if (function == null) {
        throw new RuntimeException("Function not found");
      }
      LOG.info("@@@@@@@@@@@ 處理完連線 塞資料");
      JCoParameterList input = function.getImportParameterList();
      input.setValue("I_BUKRS", bukrs);
      input.setValue("I_BELNR", belnr);
      input.setValue("I_GJAHR", gjahr);
      JCoParameterList tableParamList = function.getTableParameterList();
      JCoTable t202Table = tableParamList.getTable("T_202");
      for (T202 t202 : t202List) {
        t202Table.appendRow();
        t202Table.setValue("XC003", t202.getXC003());
        t202Table.setValue("XC011", t202.getXC011());
        t202Table.setValue("XC012", t202.getXC012());
        t202Table.setValue("XD003", t202.getXD003());
        t202Table.setValue("XD004", t202.getXD004());
        t202Table.setValue("XD005", t202.getXD005());
        t202Table.setValue("XD006", t202.getXD006());
        t202Table.setValue("XD007", t202.getXD007());
        t202Table.setValue("XD008", t202.getXD008());
        t202Table.setValue("XD009", t202.getXD009());
        t202Table.setValue("XD011", t202.getXD011());
        t202Table.setValue("XD012", t202.getXD012());
        t202Table.setValue("TA007", t202.getTA007());
        t202Table.setValue("TA008", t202.getTA008());
        t202Table.setValue("TA015", t202.getTA015());
        t202Table.setValue("TA016", t202.getTA016());
        t202Table.setValue("TA201", t202.getTA201());
        t202Table.setValue("TA204", t202.getTA204());
        t202Table.setValue("TA205", t202.getTA205());
        t202Table.setValue("XA001", t202.getXA001());
        LOG.info("T202Table: " + t202.toString());
      }
      LOG.info("@@@@@@@@@@@ 執行rfc");
      function.execute(destination);
    } catch (Exception e) {
      LOG.error(e.getMessage());
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      releaseContext(destination);
    }
    return zreturn;
  }


  private void ensureStateful(JCoDestination dest) {
    if (!JCoContext.isStateful(dest)) {
      JCoContext.begin(dest);
    }
  }

  private void releaseContext(JCoDestination dest) {
    try {
      if (JCoContext.isStateful(dest)) {
        JCoContext.end(dest);
      }
    } catch (JCoException e) {
      LOG.error("Error ending JCo context", e.getMessage(), e);
    }
  }

}
