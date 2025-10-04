package nccc.btp.config;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.SyncUser;
import nccc.btp.repository.SyncUserRepository;
import nccc.btp.util.IpUtils;

@Component
public class HeaderPreAuthFilter extends OncePerRequestFilter {

  private static final Logger LOG = LoggerFactory.getLogger(HeaderPreAuthFilter.class);

  @Autowired
  private ApplicationProperties applicationProperties;

  @Autowired
  private SyncUserRepository syncUserRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, java.io.IOException {

    String path = request.getRequestURI().substring(request.getContextPath().length());
    
    if (path.startsWith("/assets/") || "/403Forbidden.html".equals(path)
        || "/amlupload/api/remittance".equals(path)) {
      chain.doFilter(request, response);
      return;
    }

    String headerUserId = request.getHeader("user_id");
    HttpSession session = request.getSession(false);
    NcccUserDto sessionUser =
        session != null ? (NcccUserDto) session.getAttribute("SESSION_USER") : null;

    boolean notLoggedIn = SecurityContextHolder.getContext().getAuthentication() == null;
    boolean firstEntry = path.equals("/");
    boolean userChanged = sessionUser == null || !headerUserId.equals(sessionUser.getUserId());


    if (notLoggedIn || firstEntry || userChanged) {
      String userId = headerUserId;
      String userName = request.getHeader("user_cn");
      if (!StringUtils.isEmpty(userName)) {
        try {
          userName = URLDecoder.decode(userName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
          LOG.error(e.getMessage());
        }
      }
      String appAuthString = request.getHeader("nccc_ap021");
      String email = request.getHeader("user_email");
      String deptId = request.getHeader("user_deptid");
      String deptName = request.getHeader("user_deptname");
      String hrAccount = "";
      String hrid = "";
      String ouCode = "";
      String ouName = "";
      if (!applicationProperties.isLocalMode()) {
        hrAccount = email.substring(0, email.indexOf('@'));
        SyncUser user = syncUserRepository.findByAccount(hrAccount);
        if (user == null) {
          LOG.info("User {} not found in SyncUser, redirecting to 403 page", hrAccount);
          response.sendRedirect(request.getContextPath() + "/403Forbidden.html");
          return;
        }
        hrid = user.getHrid();
        ouCode = user.getOuCode();
        ouName = user.getOuName();
      }
      if (!StringUtils.isEmpty(deptName)) {
        try {
          deptName = URLDecoder.decode(deptName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
          LOG.error(e.getMessage());
        }
      }
      String teamId = request.getHeader("user_teamid");
      String ip = IpUtils.getClientIp(request);
      // String ip1 = request.getHeader("wl-proxy-client-ip");
      // String ip2 = request.getHeader("x-forwarded-for");

      if (userId == null && applicationProperties.isLocalMode()) {
        LOG.info(" local test mode ENABLE ");
        userId = "locaTest";
        userName = "測試1";
        appAuthString =
            "AP021:15,15,15,15,15,15,15,15,15,15,15,15,15,15,63,63,63,63,63,9,4,31,4,63,4,8,15,15.";
        email = "Elolin.Li@email.com";
        deptId = "ASD";
        deptName = "資服部";
        teamId = "AP1";
        hrAccount = email.substring(0, email.indexOf('@'));
        hrid = "1090908";
        ouCode = "F404";
        ouName = "測試部";
      }
      LOG.info("appAuthString==========>" + appAuthString);
      if (StringUtils.length(appAuthString) <= 59) {
        LOG.info("User {}, {} : Access Denied. ", deptId, userId);
        response.sendRedirect(request.getContextPath() + "/403Forbidden.html");
        return;
      }

      LOG.info("appAuthString {}", appAuthString);

      try {
        // -- 去除多餘字元, 用來判斷是否有權限
        String s = appAuthString.substring(6, appAuthString.length() - 1);
        LOG.info("AuthString = " + s);

        String[] auths = s.split(",");

        // log.info("auths array size " + auths.length);
        boolean authFlag = false;
        for (String auth : auths) {
          if (StringUtils.isEmpty(auth)) {
            continue;
          }
          try {
            if (Integer.parseInt(auth) > 0) {
              authFlag = true;
              break;
            }
          } catch (Exception x) {
          }
        }
        // log.info("parse auth completed {}", authFlag);
        if (!authFlag) {
          // -- 權限全部為 0
          LOG.info("User {}, {} : Access Denied.. ", deptId, userId);
          response.sendRedirect(request.getContextPath() + "/403Forbidden.html");
          return;
        }
      } catch (Exception x) {
        LOG.error(x.getMessage());
      }

      // 建立你的 DTO
      NcccUserDto user = new NcccUserDto();
      user.setUserId(userId);
      user.setUserName(userName);
      user.setEmail(email);
      user.setDeptId(deptId);
      user.setDeptName(deptName);
      user.setTeamId(teamId);
      user.setAppAuthString(appAuthString);
      user.setHrAccount(hrAccount);
      user.setClientIp(ip);
      user.setHrid(hrid);
      user.setOuCode(ouCode);
      user.setOuName(ouName);

      request.getSession().setAttribute("SESSION_USER", user);

      JSONObject params = new JSONObject();
      params.put("userInfo", user);

      // modelMap.addAttribute("userInfo", user);
      // modelMap.addAttribute("version", applicationProperties.getVersion());
      // modelMap.addAttribute("startupTime", applicationProperties.getStartupTime());
      // 產生一個 Authentication 放到 SecurityContext
      // 這裡我們只給一個 ROLE_USER
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user,
          /* credentials */ null, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    chain.doFilter(request, response);
  }
}
