package nccc.btp.aplog.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.repackaged.org.apache.commons.collections4.MapUtils;
import lombok.extern.log4j.Log4j2;
import nccc.btp.aplog.filter.MultiReadHttpServletRequest;
import nccc.btp.aplog.mask.DefaultApLogMask;
import nccc.btp.aplog.mask.IApLogMask;
import nccc.btp.aplog.model.SysFunCombinedDTO;
import nccc.btp.repository.SysFunCombinedRepository;
import nccc.btp.util.FileUtil;

@Log4j2
@Service
public class ApLogService {

  private final SysFunCombinedRepository sysFunCombinedRepository; // 更改為新的儲存庫

  private Map<String, List<String>> commonParaNameMapping = null;

  @Autowired
  public ApLogService(SysFunCombinedRepository sysFunCombinedRepository) { // 建構子已更新
    this.sysFunCombinedRepository = sysFunCombinedRepository;
  }

  /**
   * - 取得 FunId 資料for ApLog : 轉換成以 funId 為 Map Key - URI, MENU_NAME, FUN_NAME, FUN_ID,
   * AP_LOG_ACCESS_TYPE, - (List<String>)URI_LIST, (List<String>)AP_LOG_ACCESS_TYPE_LIST
   * 
   * @return Map<funId, Map<String, Object>>
   */
  public Map<String, Map<String, Object>> getFunIdForApLog() {
    List<SysFunCombinedDTO> apLogList = sysFunCombinedRepository.findAllCombinedData(); // 更改為使用新的儲存庫方法
    // log.debug("{}", apLogList);
    Map<String, Map<String, Object>> funIdsMap = new HashMap<String, Map<String, Object>>();
    String lastFunId = null;
    List<String> uriArrayList = null;
    List<String> accessTypeArrayList = null;
    List<String> logCountArrayList = null;

    Map<String, Object> funIdMap = null;

    for (SysFunCombinedDTO apLogEntry : apLogList) { // 迭代 SysFunUri 物件
      // ------------------------------------------
      // -- 轉換成以 funId 為 Map Key

      String funId = apLogEntry.getLdapFunId(); // 直接從 SysFunUri 存取屬性
      if (funId.equals(lastFunId)) {
        // ------------------------------------------------------
        // -- 將 uri 整理進同一個 Map
        uriArrayList.add(apLogEntry.getUri());
        accessTypeArrayList
            .add(apLogEntry.getApLogAccessType() != null ? apLogEntry.getApLogAccessType() : "Q");
        logCountArrayList
            .add(apLogEntry.getApLogCount() != null ? apLogEntry.getApLogCount() : "N");

      } else {
        uriArrayList = new LinkedList<String>();
        accessTypeArrayList = new LinkedList<String>();
        logCountArrayList = new LinkedList<String>();

        funIdMap = new HashMap<String, Object>();
        // 手動將 SysFunUri 的屬性複製到 Map<String, Object>
        funIdMap.put("URI", apLogEntry.getUri());

        funIdMap.put("MENU_NAME", apLogEntry.getMenuNameFromMenu());
        funIdMap.put("FUN_NAME", apLogEntry.getFunName());
        funIdMap.put("LDAP_FUN_ID", apLogEntry.getLdapFunId());
        funIdMap.put("AP_LOG_ACCESS_TYPE", apLogEntry.getApLogAccessType());
        funIdMap.put("AP_LOG_COUNT", apLogEntry.getApLogCount());

        uriArrayList.add(apLogEntry.getUri());
        accessTypeArrayList
            .add(apLogEntry.getApLogAccessType() != null ? apLogEntry.getApLogAccessType() : "Q");
        logCountArrayList
            .add(apLogEntry.getApLogCount() != null ? apLogEntry.getApLogCount() : "N");

        funIdMap.put("URI_LIST", uriArrayList);
        funIdMap.put("AP_LOG_ACCESS_TYPE_LIST", accessTypeArrayList);
        funIdMap.put("AP_LOG_COUNT_LIST", logCountArrayList);

        funIdsMap.put(funId, funIdMap);
        lastFunId = funId;
      }
    }
    return funIdsMap;
  }

  /**
   * - 讀取json檔案
   * 
   * @param jsonFileName
   * @return
   */
  public Map<String, Map<String, List<String>>> loadJsonFileNameMapping(String jsonFileName) {
    InputStream in = null;
    try {
      in = ApLogService.class.getClassLoader().getResourceAsStream(jsonFileName);
      StringBuffer out = new StringBuffer();
      byte[] b = new byte[4096];
      for (int n; (n = in.read(b)) != -1;) {
        out.append(new String(b, 0, n));
      }

      ObjectMapper objectMapper = new ObjectMapper();
      Map<String, Map<String, List<String>>> jsonMap =
          objectMapper.readValue(com.google.json.JsonSanitizer.sanitize(out.toString()), Map.class);

      return jsonMap;
    } catch (Exception x) {
      return null;
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        in = null;
      }
    }
  }

  /**
   * - 取得對應的中文名稱或遮碼方式
   * 
   * @param paraNameMapping
   * @param funId
   * @param name
   * @param idx : 0 : 取得對應的中文名稱 , 1 : 取得該遮碼方式
   * @return
   */
  public String getReqNameSetting(Map<String, Map<String, List<String>>> paraNameMapping,
      String funId, String name, int idx) {
    String retValue = name;
    String maskMethod = null;
    // -------------------------------------------------
    // -- 先讀取 Common 再根據各 funId 取值
    if (this.getCommonParaNameMapping() != null) {
      List<String> l = this.getCommonParaNameMapping().get(name);
      if (l != null && l.size() > idx) {
        retValue = l.get(idx);
        if (idx == 1) {
          // 共通類的遮碼方式
          maskMethod = retValue;
        }
      }
    }


    Map<String, List<String>> nameMaping =
        (Map<String, List<String>>) MapUtils.getMap(paraNameMapping, funId);
    if (nameMaping != null) {
      // ex: "decodeMima":["密碼","proj.nccc.aplog.mask.PxsswordMask"]
      List<String> l = nameMaping.get(name);
      if (l != null && l.size() > idx) {
        return l.get(idx);
      }
      if (idx == 1) {
        // 未設定遮碼方式
        return maskMethod;
      }
    } else if (idx == 1) {
      // 未設定遮碼方式
      return maskMethod;
    }
    return retValue;
  }



  /**
   * - 從 request 取得 json String
   * 
   * @param request
   * @return
   */
  public String getJsonParam(HttpServletRequest request) {
    String jsonParam = "";
    ServletInputStream inputStream = null;
    try {
      int contentLength = request.getContentLength();
      if (!(contentLength < 0)) {
        byte[] buffer = new byte[contentLength];
        inputStream = request.getInputStream();
        for (int i = 0; i < contentLength;) {
          int len = inputStream.read(buffer, i, contentLength);
          if (len == -1) {
            break;
          }
          i += len;
        }
        jsonParam = new String(buffer, "utf-8");
      }
    } catch (IOException e) {
      log.error("parset to json fail {}", e);
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          log.error("parset to json error {}", e);
        }
      }
    }
    return jsonParam;
  }


  /**
   * - 處理固定採用 POST parameterMap (非 JSON Body)
   *
   * @param ldapPrefix
   * @param queryIds
   * @param lInfo
   * @param aplogProps
   */
  public void buildQueryParameter(String ldapPrefix, String queryIds, LdapInfo lInfo,
      Properties aplogProps) {
    String[] queryIdArrays = queryIds.split(",");
    lInfo.setQueryIds(queryIdArrays);

    for (String queryId : queryIdArrays) {
      String qureyPrefix = ldapPrefix + "query." + queryId + ".";
      Map<String, String> paraMap = new HashMap<>();
      String paraIds = aplogProps.getProperty(qureyPrefix + "paraId", "");
      String[] paraIdArrays = paraIds.split(",");
      String paraNames = aplogProps.getProperty(qureyPrefix + "paraName", "");
      String[] paraNameArrays = paraNames.split(",");
      for (int i = 0; i < paraIdArrays.length; i++) {
        paraMap.put(paraIdArrays[i], paraNameArrays[i]);
      }
      lInfo.getQueryParas().put(queryId, paraMap);

      String actionParas = aplogProps.getProperty(qureyPrefix + "action.para", "");
      String[] actionParaArrays = !actionParas.equals("") ? actionParas.split(",") : null;

      String actionValues = aplogProps.getProperty(qureyPrefix + "action.value", "");
      String[] actionValueArrays = !actionValues.equals("") ? actionValues.split(",") : null;

      if (actionParaArrays != null) {
        Map<String, String> actionParaMap = new HashMap<>();
        for (int i = 0; i < actionParaArrays.length; i++) {
          actionParaMap.put(actionParaArrays[i], actionValueArrays[i]);
        }
        lInfo.getActionParas().put(queryId, actionParaMap);
      }

      String paraMasks = aplogProps.getProperty(qureyPrefix + "mask", "");
      String[] paraMasksArrays = paraMasks.split(",");
      String paraMaskTypes = aplogProps.getProperty(qureyPrefix + "maskType", "");
      String[] paraMaskTypeArrays = paraMaskTypes.split(",");
      Map<String, IApLogMask> maskMap = new HashMap<>();
      for (int i = 0; i < paraMasksArrays.length; i++) {
        IApLogMask mask = null;
        if (paraMaskTypeArrays.length > i && paraMaskTypeArrays[i] != null
            && !paraMaskTypeArrays[i].equals("")) {
          try {
            Class<?> c = Class.forName(paraMaskTypeArrays[i]);
            mask = (IApLogMask) c.newInstance();
          } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            mask = new DefaultApLogMask();
          }
        } else {
          mask = new DefaultApLogMask();
        }
        maskMap.put(paraMasksArrays[i], mask);
      }
      lInfo.getQueryMasks().put(queryId, maskMap);

      Map<String, Map<String, String>> mappingMap = new HashMap<>();
      for (String paraId : paraIdArrays) {
        String mappingPrefix = qureyPrefix + paraId + ".mapping.";
        String mappingNames = aplogProps.getProperty(mappingPrefix + "name", "");
        String[] mappingNameArrays = mappingNames.split(",");
        String mappingValues = aplogProps.getProperty(mappingPrefix + "value", "");
        String[] mappingValueArrays = mappingValues.split(",");
        Map<String, String> mapping = new HashMap<>();
        int i = 0;
        for (String mappingValue : mappingValueArrays) {
          mapping.put(mappingValue, mappingNameArrays[i]);
          i++;
        }
        mappingMap.put(paraId, mapping);
      }
      lInfo.getQueryParaMapping().put(queryId, mappingMap);
    }
  }


  /**
   * - 取得輸入的設定
   * 
   * @param hRequest
   * @param queryParas
   * @param maskMap
   * @param mappingMap
   * @param url
   * @return
   */
  public LdapInfo getQueryInputSetting(HttpServletRequest hRequest, String url,
      Map<String, LdapInfo> ldapInfos) {
    boolean isMatched = false;
    LdapInfo ldapInfo = null;
    AntPathMatcher matcher = new AntPathMatcher();
    for (Entry<String, LdapInfo> enrty : ldapInfos.entrySet()) {
      for (int i = 0; i < enrty.getValue().getUrl().length; i++) {
        String pattern = enrty.getValue().getUrl()[i];
        String qureryId = null;
        Map<String, String> actionParaMap = null;
        if (enrty.getValue().getQueryIds() != null) {
          qureryId = enrty.getValue().getQueryIds()[i];
          actionParaMap = enrty.getValue().getActionParas().get(qureryId);
        }

        if (matcher.match(pattern, url)) {
          log.debug("matcher id:" + qureryId);
          boolean isBreak = true;
          if (actionParaMap != null) {
            for (Entry<String, String> actionPara : actionParaMap.entrySet()) {
              String paraName = actionPara.getKey();
              String paraValue = actionPara.getValue();
              log.debug("paraName:" + paraName);
              log.debug("paraValue:" + paraValue);
              String sValue = hRequest.getSession().getAttribute(paraName) != null
                  ? hRequest.getSession().getAttribute(paraName).toString()
                  : null;
              String pValue =
                  hRequest.getParameter(paraName) != null ? hRequest.getParameter(paraName)
                      : sValue;
              if (StringUtils.isBlank(pValue) || !pValue.equalsIgnoreCase(paraValue)) {
                isBreak = false;
              }
            }
            if (isBreak) {
              ldapInfo = enrty.getValue();
              ldapInfo.setLdapInfoQueryParas(enrty.getValue().getQueryParas().get(qureryId));
              ldapInfo.setMaskMap(enrty.getValue().getQueryMasks().get(qureryId));
              ldapInfo.setLdapInfoMappingMap(enrty.getValue().getQueryParaMapping().get(qureryId));

              ldapInfo.setAccessType(this.getArrayUtil(enrty.getValue().getAccType(), i));
              ldapInfo.setFuctionCount(this.getArrayUtil(enrty.getValue().getNeedCount(), i));

              isMatched = true;
              break;
            }
          } else {
            ldapInfo = enrty.getValue();
            ldapInfo.setLdapInfoQueryParas(enrty.getValue().getQueryParas().get(qureryId));
            ldapInfo.setMaskMap(enrty.getValue().getQueryMasks().get(qureryId));
            ldapInfo.setLdapInfoMappingMap(enrty.getValue().getQueryParaMapping().get(qureryId));

            ldapInfo.setAccessType(this.getArrayUtil(enrty.getValue().getAccType(), i));
            ldapInfo.setFuctionCount(this.getArrayUtil(enrty.getValue().getNeedCount(), i));

            isMatched = true;
            break;
          }

        }
      }
      if (isMatched) {
        break;
      }
    }
    return ldapInfo;
  }



  /**
   * - 取得及分析該 request 傳入的參數含 post parameter 及 application/json
   * 
   * @param hRequest
   * @param queryParas
   * @param maskMap
   * @param mappingMap
   * @param ldapInfo
   * @return
   */
  public String getQueryInput(HttpServletRequest hRequest, LdapInfo ldapInfo,
      Map<String, Object> jsonBody, Map<String, Map<String, List<String>>> paraNameMapping) {

    Map<String, String> queryParas = ldapInfo.getLdapInfoQueryParas();
    StringBuffer sb = new StringBuffer();
    Enumeration<String> enumeration = hRequest.getParameterNames();
    while (enumeration.hasMoreElements()) {
      // -----------------------------------------------
      // -- form post/get
      String paraName = enumeration.nextElement();
      log.debug("debug:paraName:" + paraName);
      if (queryParas != null && queryParas.containsKey(paraName)) {
        String requestPara = hRequest.getParameter(paraName);
        // log.debug("debug before mapping:requestPara:" + requestPara);
        if (ldapInfo.getLdapInfoMappingMap() != null
            && ldapInfo.getLdapInfoMappingMap().containsKey(paraName)) {
          Map<String, String> mapping = ldapInfo.getLdapInfoMappingMap().get(paraName);
          if (mapping.containsKey(requestPara)) {
            requestPara = mapping.get(requestPara);
          }
        }
        // log.debug("debug after mapping:requestPara:" + requestPara);
        // log.debug("debug before mask:requestPara:" + requestPara);
        if (ldapInfo.getMaskMap() != null && ldapInfo.getMaskMap().containsKey(paraName)) {
          requestPara = ldapInfo.getMaskMap().get(paraName).mask(requestPara);
        }
        // log.debug("debug after mask:requestPara:" + requestPara);
        sb.append("\"").append(queryParas.get(paraName)).append("\":\"").append(requestPara)
            .append("\"/");
      } else {
        String requestPara = hRequest.getParameter(paraName);
        sb.append("\"").append(paraName).append("\":\"").append(requestPara).append("\"/");
      }
      String contentType = hRequest.getContentType();
      if (contentType != null && contentType.contains("multipart/form-data")) {
        // -- For MULTIPART_FORM_DATA
        MultiReadHttpServletRequest mRequest = new MultiReadHttpServletRequest(hRequest);
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        List<FileItem> items;
        try {
          // 將 MultiReadHttpServletRequest 包裝在 ServletRequestContext 中
          ServletRequestContext requestContext = new ServletRequestContext(mRequest);
          items = upload.parseRequest(requestContext); // 傳遞 RequestContext
          Iterator<FileItem> iter = items.iterator();
          while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            if (item.isFormField()) {
              String name = item.getFieldName();
              String value = FileUtil.validFilePath(item.getString());
              value = new String(value.getBytes("UTF-8"), "ISO-8859-1");
              sb.append("\"").append(name).append("\":\"").append(value).append("\"/");
            }
          }
        } catch (UnsupportedEncodingException | FileUploadException e) {
          log.error(e);
        }
      }
    }

    if (jsonBody != null) {
      // --------------------------------------------
      // -- 針對 application/json 處理
      Map<String, Object> paraJsonMap = jsonBody;
      for (Entry<String, Object> para : paraJsonMap.entrySet()) {
        String paraName = para.getKey();
        String paraValue = "";
        if (para.getValue() != null) {
          paraValue = para.getValue().toString();
        }

        // ---------------------------------------------------
        // -- 變數名稱轉對應中文
        String paraNameAfter =
            this.getReqNameSetting(paraNameMapping, ldapInfo.getFunId(), paraName, 0);

        // ---------------------------------------------------
        // -- 遮碼處理
        String paraValueAfetr = paraValue;
        String maskClazz =
            this.getReqNameSetting(paraNameMapping, ldapInfo.getFunId(), paraName, 1);
        if (StringUtils.isNotBlank(maskClazz)) {
          IApLogMask mask = null;
          try {
            Class<?> c = Class.forName(maskClazz);
            mask = (IApLogMask) c.newInstance();
          } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            mask = new DefaultApLogMask();
          }
          paraValueAfetr = mask.mask(paraValueAfetr);
        }
        sb.append("\"").append(paraNameAfter).append("\":\"").append(paraValueAfetr).append("\"/");
      }
    }
    if (sb.length() > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }



  /**
   * - 取得陣列內容
   * 
   * @param array
   * @param i
   * @return
   */
  private String getArrayUtil(String[] array, int i) {
    if (array != null && array.length >= i) {
      return array[i];
    }
    return null;
  }

  public Map<String, List<String>> getCommonParaNameMapping() {
    return commonParaNameMapping;
  }

  public void setCommonParaNameMapping(Map<String, List<String>> commonParaNameMapping) {
    this.commonParaNameMapping = commonParaNameMapping;
  }

}