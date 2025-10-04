package nccc.btp.aplog.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.repackaged.org.apache.commons.collections4.MapUtils;
import lombok.extern.log4j.Log4j2;
import nccc.btp.aplog.ApLogContext;
import nccc.btp.aplog.mask.DefaultApLogMask;
import nccc.btp.aplog.mask.IApLogMask;
import nccc.btp.aplog.model.ApLogDetail;
import nccc.btp.aplog.model.ApLogMaster;
import nccc.btp.aplog.model.LogFunctionCount;
import nccc.btp.aplog.service.ApLogService;
import nccc.btp.aplog.service.LdapInfo;
import nccc.btp.aplog.sql.ApLogDAO;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.util.IpUtils;

@Log4j2
public class ApLogFilter implements Filter {

  private String systemId;
  private String dbEncoding;
  private Map<String, LdapInfo> ldapInfos = new HashMap<>();

  /**
   * - 依Json格式存放變數設定值
   */
  private Map<String, Map<String, List<String>>> paraNameMapping;
  /**
   * applicaion/json 傳入
   */
  // private Map<String, Object> jsonBody;


  private ApLogDAO apLogDao = null;
  ApLogService apLogService;

  @Override
  public void destroy() {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;

    // ---------------------------------------------------------------
    // -- 僅 log , 不可 disable
    // -- 升級 spring 會發生抓不到 request parameter
    // -- 須加上以下的 code
    if (request.getParameterMap() == null) {
      log.info("request by parameter is null");
    } else {
      Enumeration<String> enumeration = request.getParameterNames();
      while (enumeration.hasMoreElements()) {
        // -----------------------------------------------
        // -- form post/get
        String paraName = enumeration.nextElement();
        // log.info("paraName:" + paraName + " " + request.getParameter(paraName));
        log.info("paraName:" + paraName);
      }
    }

    // ---------------------------------------------------

    boolean successFlag = true;
    HttpServletRequest hRequest = (HttpServletRequest) request;
    String url = hRequest.getServletPath();
    if (url == null || url.trim().length() <= 0) {
      url = hRequest.getPathInfo();
    }

    if (ApLogUtils.isExcludeUri(url)) {
      filterChain.doFilter(request, response);
      return;
    }

    // 處理 Request 的資料屬於 : application/json 傳入
    Map<String, Object> jsonBody = null;
    // ServletRequest servletRequest = this.parserJsonBody(request, jsonBody);
    ServletRequest servletRequest = null;
    ServletRequest requestWrapper = null;
    if (request instanceof HttpServletRequest) {
      requestWrapper = new ApLogHttpServletRequestWrapper((HttpServletRequest) request);
    }

    // ---------------------------------------------------------------
    // -- get json body
    String contentType = requestWrapper.getContentType();
    String paramJson = "";
    // if ("application/json; charset=UTF-8".equals(contentType)) {
    if (contentType != null && contentType.indexOf("json") >= 0) {
      String jsonString = apLogService.getJsonParam((HttpServletRequest) requestWrapper);
      // log.debug("json before sanitize : {}", jsonString);
      paramJson = com.google.json.JsonSanitizer.sanitize(jsonString);
      // log.debug("json after sanitize : {}", paramJson);
      // System.out.println(paramJson);
      // Map<String, Object> jsonMap = null;
      try {
        ObjectMapper objectMapper = new ObjectMapper();
        jsonBody = objectMapper.readValue(paramJson, Map.class);
      } catch (Exception x) {
        log.error(x.getMessage());
        jsonBody = new HashMap<String, Object>();
        paramJson = paramJson.replaceAll("\\n", "").replaceAll("\\r", "");
        jsonBody.put("queryInput", paramJson);
      }
      servletRequest = requestWrapper;
    } else {
      servletRequest = request;
    }



    // ----------------------------------------------------------
    // -- 取得輸入的設定
    LdapInfo ldapInfo = null;
    try {
      ldapInfo = apLogService.getQueryInputSetting(hRequest, url, ldapInfos);
    } catch (Exception x) {
      log.error(x.getMessage(), x);
    }

    if (ldapInfo != null) {
      // ------------------------------------------------------
      // -- 產生輸入的資料
      ThreadContext.put("apLogUuid", UUID.randomUUID().toString());
      try {
        ThreadContext.put("queryInput",
            apLogService.getQueryInput(hRequest, ldapInfo, jsonBody, paraNameMapping));
      } catch (Exception x) {
        log.error(x.getMessage(), x);
        ThreadContext.put("queryInput", "");
      }
    }

    try {

      // ------------------------------------------------------
      // -- 執行原功能
      if (ldapInfo == null) {
        filterChain.doFilter(servletRequest, response);
      } else {
        // --------------------------------------------------
        // -- 計算 logFunctionCount
        HttpServletResponse resp = (HttpServletResponse) response;
        ResponseWrapper mResp = new ResponseWrapper(resp);

        filterChain.doFilter(servletRequest, mResp);

        int statusCode = resp.getStatus();

        // ---------------------------------------
        // -- ApLog set functionCount
        // 獲取返回值
        byte[] bytes = mResp.getBytes();


        // if (statusCode >= 200 && statusCode < 299) {
        // if (resp.getContentType().toLowerCase().indexOf("application/json") >= 0) {
        //
        // String ciphertext = new String(bytes, "utf-8");
        // try {
        // // -------------------------------
        // // -- 判斷內容並計算筆數
        // JSONObject json = new JSONObject(ciphertext);
        //
        // int dataStatus = json.getInt("status");
        // if (dataStatus == 0) {
        // JSONArray data;
        // try {
        // data = json.getJSONArray("data");
        // ApLogUtils.setApLogFunctionCount(data.length());
        // } catch (Exception x) {
        // try {
        // data = json.getJSONObject("data").getJSONArray("data");
        // ApLogUtils.setApLogFunctionCount(data.length());
        // } catch (Exception x1) {
        // ApLogUtils.setApLogFunctionCount(1);
        // }
        // }
        // } else {
        // ApLogUtils.setApLogFunctionCount(0);
        // }
        // } catch (Exception e) {
        // log.error("APLog Parse content", e);
        // }
        // }
        // } else {
        // ApLogUtils.setApLogFunctionCount(0);
        // }

        if (statusCode >= 200 && statusCode < 299) {
          if (resp.getContentType() != null
              && resp.getContentType().toLowerCase().contains("application/json")) {

            String ciphertext = new String(bytes, "utf-8");
            try {
              int totalCount = 0;
              ciphertext = ciphertext.trim();

              if (ciphertext.startsWith("[")) {
                // JSON 陣列
                JSONArray jsonArray = new JSONArray(ciphertext);
                totalCount = jsonArray.length();

              } else if (ciphertext.startsWith("{")) {
                // JSON 物件
                JSONObject jsonObject = new JSONObject(ciphertext);
                for (String key : jsonObject.keySet()) {
                  Object value = jsonObject.get(key);
                  if (value instanceof JSONArray) {
                    totalCount += ((JSONArray) value).length();
                  }
                }
              }

              ApLogUtils.setApLogFunctionCount(totalCount);

            } catch (Exception e) {
              log.error("APLog Parse content", e);
              ApLogUtils.setApLogFunctionCount(0);
            }
          }
        } else {
          ApLogUtils.setApLogFunctionCount(0);
        }
        resp.getOutputStream().write(bytes);
      }
    } catch (Exception e) {
      log.error("ERROR", e);
      successFlag = false;
      throw e;
    } finally {
      if (ldapInfo != null) {
        try {
          processApLog(hRequest, ldapInfo, url, successFlag, ldapInfo.getAccessType(),
              ldapInfo.getFuctionCount());
        } catch (Exception e) {
          log.error("ERROR", e);
        }
      }
    }
  }

  private void processApLog(HttpServletRequest hRequest, LdapInfo ldapInfo, String url,
      boolean successFlag, String accessType, String functionCount)
      throws UnsupportedEncodingException {

    // ------------------------------------------------------------
    // -- For Logout : session invalidate, catch from ThreadContext
    String apLogDeptNo = ThreadContext.get("apLogDeptNo");
    String apLogUserId = ThreadContext.get("apLogUserId");
    String apLogUserName = ThreadContext.get("apLogUserName");
    String apLogTeamId = ThreadContext.get("apLogTeamId");
    if (StringUtils.isAnyBlank(apLogDeptNo, apLogUserId, apLogUserName)) {
      NcccUserDto userMaster = (NcccUserDto) hRequest.getSession().getAttribute("SESSION_USER");
      if (userMaster == null) {
        return;
      }
      apLogDeptNo = userMaster.getDeptId();
      apLogUserId = userMaster.getUserId();
      apLogUserName = userMaster.getUserName();
      apLogTeamId = userMaster.getTeamId();
    }

    String uuid = ThreadContext.get("apLogUuid");
    ApLogMaster master = new ApLogMaster();
    log.info("============save aplog start============");
    master.setGuid(uuid);
    master.setSystemId(this.systemId);
    master.setFunctionId(ldapInfo.getFunId());
    master.setFunctionName(ldapInfo.getFunName());
    master.setRequestUrl(url);
    master.setDeptId(apLogDeptNo);
    master.setTeamId(apLogTeamId);
    master.setUserId(apLogUserId);
    master.setUserName(apLogUserName);
    master.setSourceIp(IpUtils.getClientIp(hRequest));

    master.setTargetIp(IpUtils.getHostIp());

    Date now = new Date(System.currentTimeMillis());
    SimpleDateFormat dsdf = new SimpleDateFormat(ApLogContext.date_format);
    master.setAccessDate(dsdf.format(now));
    SimpleDateFormat tsdf = new SimpleDateFormat(ApLogContext.time_format);
    master.setAccessTime(tsdf.format(now));
    master.setWriteDate(master.getAccessDate());
    master.setWriteTime(master.getAccessTime());
    master.setTimestamp(master.getAccessDate() + " " + master.getAccessTime());
    master.setAccessType(accessType);
    master.setQureyInput(ThreadContext.get("queryInput"));

    String apLogfunctionCount = "0";
    try {
      apLogfunctionCount = "" + Integer.parseInt(ThreadContext.get("apLogfunctionCount"));
    } catch (Exception x) {
    }

    master.setFunctionCount(apLogfunctionCount);

    Set<ApLogDetail> details = new HashSet<>();
    if (ThreadContext.containsKey("detailCount")) {
      String detailCountS = ThreadContext.get("detailCount");
      int detailCount = Integer.parseInt(detailCountS);
      log.debug("detailCountS:" + detailCountS);
      for (int i = 0; i <= detailCount; i++) {
        ApLogDetail detail = new ApLogDetail();
        detail.setGuid(uuid);
        if (ThreadContext.containsKey(i + ":selectCount")) {
          detail.setQueryCount(ThreadContext.get(i + ":selectCount"));
        }

        if (ThreadContext.containsKey(i + ":beforeImage")) {
          detail.setBeforeImage(ThreadContext.get(i + ":beforeImage"));
        }

        if (ThreadContext.containsKey(i + ":afterImage")) {
          detail.setAfterImage(ThreadContext.get(i + ":afterImage"));
        }
        String sql = ThreadContext.get(i + ":sql");
        String sql1 = null, sql2 = null;
        if (sql != null) {
          byte[] sqlByte = sql.getBytes(dbEncoding);
          if (sqlByte.length >= 4000) {
            sql1 = new String(sqlByte, 0, 4000, dbEncoding);
            if (sqlByte.length > 8000) {
              sql2 = new String(sqlByte, 4000, 4000, dbEncoding);
            } else {
              sql2 = new String(sqlByte, 4000, sqlByte.length - 4000, dbEncoding);
            }
          } else {
            sql1 = sql;
          }
        }

        String sqlCode = ThreadContext.get(i + ":sqlCode");
        if (StringUtils.isNotBlank(sqlCode)) {
          detail.setSqlCode(sqlCode);
        } else {
          detail.setSqlCode("0");
        }

        // if (ThreadContext.containsKey(i + "isApLogError")) {
        // if (!Boolean.parseBoolean(ThreadContext.get(i + ":isApLogError"))) {
        // successFlag = false;
        // }
        // detail.setSqlCode(ThreadContext.get(i + ":errorCode"));
        // }
        detail.setSqlStatement1(sql1);
        detail.setSqlStatement2(sql2);
        String _d = ThreadContext.get(i + ":date");
        String _t = ThreadContext.get(i + ":time");
        String writeDate =
            (org.apache.commons.lang3.StringUtils.isNotBlank(_d)) ? _d : master.getAccessDate();
        String writeTime =
            (org.apache.commons.lang3.StringUtils.isNotBlank(_t)) ? _t : master.getAccessTime();
        detail.setWriteDate(writeDate);
        detail.setWriteTIme(writeTime);
        detail.setTimestamp(buildTimestamp(writeDate, writeTime));

        details.add(detail);
        // log.debug("detail:" + detail);
      }
    } else {
      log.debug(uuid + ": detailCount 0");
    }
    LogFunctionCount lfc = null;
    if ("Y".equalsIgnoreCase(functionCount)) {
      lfc = new LogFunctionCount();
      lfc.setAccessDate(master.getAccessDate());
      lfc.setAccessTime(master.getAccessTime());
      lfc.setFunctionId(ldapInfo.getFunId());
      lfc.setFunctionName(ldapInfo.getFunName());
      lfc.setSystemId(this.systemId);
    }

    master.setSuccessFlag(successFlag ? "Y" : "N");
    log.debug("master:" + master);
    if (apLogDao == null) {
      apLogDao = new ApLogDAO();
    }
    log.info("apLog guid:" + master.getGuid());
    apLogDao.save(master, details, lfc);
    log.info("============save aplog end============");
    ApLogContext.clearCurrentSqlCode();
    ApLogContext.clearCurrentSqlIndex();
    ThreadContext.clearAll();
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

    ServletContext servletContext = filterConfig.getServletContext();
    WebApplicationContext wac =
        WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    apLogService = wac.getBean(ApLogService.class);

    // ----------------------------------------------------
    // -- 取得AP Log 設定
    Properties aplogProps = new Properties();
    try {
      aplogProps.load(ApLogFilter.class.getClassLoader().getResourceAsStream("aplog.properties"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    systemId = aplogProps.getProperty("systemId", "APLOG");
    dbEncoding = aplogProps.getProperty("db.encoding", "utf8");

    ApLogContext.setJndiName(aplogProps.getProperty("jndi.name", ""));

    // ----------------------------------------------------
    // -- 取得所有的 funId 以 "," 隔開
    Map<String, Map<String, Object>> funMaps = apLogService.getFunIdForApLog();
    for (Map.Entry<String, Map<String, Object>> funMap : funMaps.entrySet()) {
      Map<String, Object> o = funMap.getValue();
      String funId = funMap.getKey();
      LdapInfo lInfo = new LdapInfo();
      // URI, MENU_NAME, FUN_NAME, FUN_ID, AP_LOG_ACCESS_TYPE
      // 改用 menuId 因logMaster.funId 欄位大小
      // lInfo.setFunId(funId);
      String ldapFunId = MapUtils.getString(o, "LDAP_FUN_ID", "NO_FOUND");
      if (!"NO_FOUND".equals(ldapFunId) && ldapFunId.length() >= 4) {
        // 取前 4 碼，例如 A01801 → A018
        ldapFunId = ldapFunId.substring(0, 4);
      }
      lInfo.setFunId(ldapFunId);

      // String menuName = MapUtils.getString(o, "MENU_NAME");
      String funName = MapUtils.getString(o, "FUN_NAME");
      lInfo.setFunName(funName);
      // if (menuName.equals(funName)) {
      // lInfo.setFunName(funName);
      // } else {
      // lInfo.setFunName(menuName + "-" + funName);
      // }

      @SuppressWarnings("unchecked")
      List<String> uriList = (List<String>) MapUtils.getObject(o, "URI_LIST");
      lInfo.setUrl(uriList.toArray(new String[uriList.size()]));

      // ---------------------------------------------
      // -- 無法使用 QueryId > QueryPara 因為傳入 JSON 格式, 直接整個存入
      // lInfo.setQueryIds(queryIdArrays);

      // ---------------------------------------------
      // -- 針對每個 URI的 accessType
      @SuppressWarnings("unchecked")
      List<String> accessTypeList = (List<String>) MapUtils.getObject(o, "AP_LOG_ACCESS_TYPE_LIST");
      lInfo.setAccType(accessTypeList.toArray(new String[accessTypeList.size()]));

      @SuppressWarnings("unchecked")
      List<String> needCountList = (List<String>) MapUtils.getObject(o, "AP_LOG_COUNT_LIST");
      lInfo.setNeedCount(needCountList.toArray(new String[needCountList.size()]));

      // ---------------------------------------------
      // -- 僅針對FUN ID 其uri 非 json 傳入者(parameterMap)
      String ldapPrefix = "ldap." + funId + ".";
      String queryIds = aplogProps.getProperty(ldapPrefix + "queryId", "");

      if (StringUtils.isNotBlank(queryIds)) {
        apLogService.buildQueryParameter(ldapPrefix, queryIds, lInfo, aplogProps);
      }
      ldapInfos.put(funId, lInfo);
    }

    String tableMasks = aplogProps.getProperty("table.mask", "");
    String[] tableMaskArrays = tableMasks.split(",");
    for (String tableMask : tableMaskArrays) {
      String columnPrefix = "table." + tableMask + ".";
      String columnMasks = aplogProps.getProperty(columnPrefix + "column", "");
      String[] columnMaskArrays = columnMasks.split(",");
      String columnMaskTypes = aplogProps.getProperty(columnPrefix + "maskType", "");
      String[] columnMaskTypeArrays = columnMaskTypes.split(",");
      Map<String, IApLogMask> maskMap = new HashMap<>();
      for (int i = 0; i < columnMaskArrays.length; i++) {
        IApLogMask mask = null;
        if (columnMaskTypeArrays.length > i && columnMaskTypeArrays[i] != null
            && !columnMaskTypeArrays[i].equals("")) {
          try {
            Class<?> c = Class.forName(columnMaskTypeArrays[i]);
            mask = (IApLogMask) c.newInstance();
          } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            mask = new DefaultApLogMask();
          }
        } else {
          mask = new DefaultApLogMask();
        }
        maskMap.put(columnMaskArrays[i], mask);
      }
      ApLogContext.getTableMasks().put(tableMask, maskMap);
    }

    // ----------------------------------------------------
    // -- 將 apLog.properties 定義之 name mapping 中文名稱依 funId 存入
    // -- reqNameMapping<String, Map<String, String>
    this.setParaNameMapping(apLogService.loadJsonFileNameMapping("aplogMapping.json"));

    // 暫時註解Tim
    // apLogService.setCommonParaNameMapping(MapUtils.getMap(this.getParaNameMapping(),
    // "ACS_COMMON"));
  }



  /**
   * - 取得request 的 json 資料
   * 
   * @param request
   * @return
   * @throws IOException
   */
  public ServletRequest parserJsonBody(ServletRequest request, Map<String, Object> jsonBody)
      throws IOException {
    ServletRequest requestWrapper = null;
    if (request instanceof HttpServletRequest) {
      requestWrapper = new ApLogHttpServletRequestWrapper((HttpServletRequest) request);
    }

    // get json body
    String contentType = requestWrapper.getContentType();
    String paramJson = "";
    // if ("application/json; charset=UTF-8".equals(contentType)) {
    if (contentType != null && contentType.indexOf("json") >= 0) {
      paramJson = com.google.json.JsonSanitizer
          .sanitize(apLogService.getJsonParam((HttpServletRequest) requestWrapper));
      // System.out.println(paramJson);
      // Map<String, Object> jsonMap = null;
      try {
        ObjectMapper objectMapper = new ObjectMapper();

        jsonBody = objectMapper.readValue(paramJson, Map.class);

      } catch (Exception x) {
        log.error(x.getMessage());
        jsonBody = new HashMap<String, Object>();
        paramJson = paramJson.replaceAll("\\n", "").replaceAll("\\r", "");
        jsonBody.put("queryInput", paramJson);
      }
      return requestWrapper;
    }
    return request;

  }



  public Map<String, Map<String, List<String>>> getParaNameMapping() {
    return paraNameMapping;
  }

  public void setParaNameMapping(Map<String, Map<String, List<String>>> paraNameMapping) {
    this.paraNameMapping = paraNameMapping;
  }
  //
  // public Map<String, Object> getJsonBody() {
  // return jsonBody;
  // }
  //
  // public void setJsonBody(Map<String, Object> jsonBody) {
  // this.jsonBody = jsonBody;
  // }

  // ===== helper: timestamp null-safe =====
  private String buildTimestamp(String date, String time) {
    boolean hasDate = date != null && !date.trim().isEmpty();
    boolean hasTime = time != null && !time.trim().isEmpty();
    if (hasDate && hasTime)
      return date.trim() + " " + time.trim();
    java.util.Date now = new java.util.Date();
    SimpleDateFormat dsdf = new SimpleDateFormat(ApLogContext.date_format);
    SimpleDateFormat tsdf = new SimpleDateFormat(ApLogContext.time_format);
    if (!hasDate && !hasTime) {
      return dsdf.format(now) + " " + tsdf.format(now);
    }
    if (!hasDate)
      return dsdf.format(now) + " " + (time == null ? "" : time.trim());
    return (date == null ? "" : date.trim()) + " " + tsdf.format(now);
  }

}
