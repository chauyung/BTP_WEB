package nccc.btp.auth;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import nccc.btp.enums.ResultStatus;


/**
 * 透過此 Filter來管控每個Request是否有權限使用
 *
 */
@Log4j2
@Component
public class AuthorizationFilter extends OncePerRequestFilter {


  @Autowired
  HttpSession httpSession;

  @Autowired
  SecurityService securityService;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String servletName = normalizePath(request);
    if (servletName == null || servletName.trim().length() <= 0) {
      // it is empty when deploy on WAS getServletPath
      // need to retrieve again;
      servletName = request.getPathInfo();
    }
    // ----------------------------------------------------------------------------------
    // -- in scope : AuthorizationFilter
    ThreadContext.put("ACTION_URI", servletName);
    // -------------------------------------------------------
    // -- 該URL有權限使用
    try {
      if (securityService.isPermited(servletName, request)) {
        log.debug("uri allow (" + servletName + ")");

        filterChain.doFilter(request, response);
      } else {
        JsonObject errMsg = new JsonObject();
        errMsg.addProperty("status", ResultStatus.OPERATION_FORBIDDEN.getCode());
        errMsg.addProperty("errorCode", ResultStatus.OPERATION_FORBIDDEN.getMessage());
        log.debug("uri deny (" + servletName + ")", errMsg);

        printMsg(response, errMsg);
        return;
      }
    } catch (Exception e) {
      log.info("error message=========>" + e.getMessage());

      JsonObject errMsg = new JsonObject();
      errMsg.addProperty("status", ResultStatus.SERVER_ERROR.getCode());
      printMsg(response, errMsg);
      return;
    }
  }

  private String normalizePath(HttpServletRequest request) {
    String uri = request.getRequestURI();
    if (uri == null)
      uri = "";

    String ctx = request.getContextPath();
    if (ctx == null)
      ctx = "";

    if (!ctx.isEmpty() && uri.startsWith(ctx)) {
      int boundary = ctx.length();
      boolean boundaryOk = (uri.length() == boundary) || (uri.charAt(boundary) == '/');
      if (boundaryOk) {
        uri = uri.substring(boundary);
      }
    }

    if (uri.isEmpty()) {
      uri = "/";
    }

    if (uri.length() > 1 && uri.endsWith("/")) {
      uri = uri.substring(0, uri.length() - 1);
    }

    return uri;
  }


  /**
   * Response Message as formating
   * 
   * @param response
   * @param errMsgJson
   * @throws IOException
   */
  private void printMsg(ServletResponse response, JsonObject errMsgJson) throws IOException {
    HttpServletResponse httpResp = (HttpServletResponse) response;

    int statusCode = 500; // 預設錯誤碼

    if (errMsgJson.has("status")) {
      statusCode = errMsgJson.get("status").getAsInt();
    }

    httpResp.setStatus(statusCode);
    httpResp.setContentType("application/json;charset=UTF-8");

    try (PrintWriter printWriter = httpResp.getWriter()) {
      printWriter.print(errMsgJson.toString());
    }
  }

}
