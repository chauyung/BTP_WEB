package nccc.btp.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import lombok.extern.log4j.Log4j2;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.SysFun;
import nccc.btp.entity.SysMenu;
import nccc.btp.repository.SysFunRepository;
import nccc.btp.repository.SysMenuRepository;

@Log4j2
@Service
public class SecurityService {

  @Autowired
  private SysFunRepository sysFunRepo;
  @Autowired
  private SysMenuRepository sysMenuRepository;

  // 從 application.yml 注入不需比對的 URI 列表
  @Value("${security.uri.whitelist}")
  private List<String> uriWhitelist;

  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  private boolean isWhitelisted(String uri) {
    if (uriWhitelist == null)
      return false;
    for (String pattern : uriWhitelist) {
      if (pathMatcher.match(pattern, uri)) {
        log.debug("URI '{}' matched whitelist '{}', permit directly", uri, pattern);
        return true;
      }
    }
    return false;
  }

  /**
   * 一次解析 appAuthString + 全表掃描，算出所有可存取的 URI
   */
  private Set<String> computePermittedUris(String appAuthString) {

    // 1. 先切出「冒號後面」的部分
    String authPart = "";
    if (appAuthString != null && appAuthString.contains(":")) {
      authPart = appAuthString.substring(appAuthString.indexOf(':') + 1);
    }

    // 2. 去掉尾端多餘的點號 ('.') 或逗號(',')
    authPart = authPart.replaceAll("[\\.,]+$", "");

    // 3. 再用逗號切分
    String[] tokens = authPart.isEmpty() ? new String[0] : authPart.split(",");

    // 4. 轉成 int[]
    int[] authValues =
        IntStream.range(0, tokens.length).map(i -> Integer.parseInt(tokens[i])).toArray();

    // 5. 全表取出 SysFun
    List<SysFun> allFun = sysFunRepo.findAll();

    // 一次撈出所有 SysMenu 做快取 Map
    Map<String, SysMenu> sysMenuMap =
        sysMenuRepository.findAll().stream().filter(m -> m.getMenuId() != null)
            .collect(Collectors.toMap(SysMenu::getMenuId, m -> m, (a, b) -> a));

    // 6. 位元運算決定哪些 URI 加入清單
    Set<String> uris = new HashSet<>();
    for (SysFun fun : allFun) {
      int required = fun.getLdapAttribute();
      // 公開功能直接放行
      if (required == 0) {
        uris.add(trimTrailingSlash(fun.getUri()));
        continue;
      }

      // 用 Map 取代 findByMenuId
      SysMenu sysMenu = sysMenuMap.get(fun.getMenuId());
      if (sysMenu == null) {
        log.warn("menuId not found in SysMenu: {}", fun.getMenuId());
        continue;
      }

      if (sysMenu.getLdapSn() != 999) {
        int idx = sysMenu.getLdapSn() - 1;
        if (idx < 0 || idx >= authValues.length) {
          log.warn("menuId index out of range: {}", sysMenu.getLdapSn());
          continue;
        }
        int userMask = authValues[idx];
        if ((userMask & required) != 0) {
          uris.add(trimTrailingSlash(fun.getUri()));
        }
      }

    }
    return uris;
  }

  private String trimTrailingSlash(String uri) { // NEW
    if (uri == null)
      return null;
    if (uri.length() > 1 && uri.endsWith("/")) {
      return uri.substring(0, uri.length() - 1);
    }
    return uri;
  }

  /**
   * 查詢該URL有權限使用，第一次呼叫時做一次全表計算，之後都用 Set.contains()
   * 
   * @param uri, request
   * @return
   */
  public boolean isPermited(String uri, HttpServletRequest request) {

    // --- 白名單檢查，符合就直接放行 ---
    if (isWhitelisted(uri)) {
      return true;
    }

    HttpSession session = request.getSession(false);
    if (session == null) {
      throw new IllegalStateException("Session not found");
    }
    NcccUserDto user = (NcccUserDto) session.getAttribute("SESSION_USER");
    if (user == null) {
      throw new IllegalStateException("User not logged in");
    }

    Set<String> permitted = user.getPermittedUris();
    if (permitted == null) {
      permitted = computePermittedUris(user.getAppAuthString());
      user.setPermittedUris(permitted);
    }

    String path = trimTrailingSlash(uri);

    boolean allowed = permitted.stream()
        .anyMatch(p -> pathMatcher.match(p, path) || path.equals(p) || path.startsWith(p + "/"));

    if (!allowed) {
      log.info("Access denied: user={} URI={}", user, uri);
    }
    return allowed;
  }

  /**
   * 如需手動重算（例如管理後台變更權限後），呼叫此方法清除緩存
   */
  public void refreshPermittedUris(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null)
      return;
    NcccUserDto user = (NcccUserDto) session.getAttribute("SESSION_USER");
    if (user != null) {
      user.setPermittedUris(null);
    }
  }

}
