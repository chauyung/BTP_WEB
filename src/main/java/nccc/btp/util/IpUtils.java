package nccc.btp.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;

public class IpUtils {

  /**
   * 取得發送本次請求的 client IP (考慮 Proxy 轉發)
   */
  public static String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
      // 多個 proxy 時，取第一個
      if (ip.contains(",")) {
        ip = ip.split(",")[0];
      }
      return ip.trim();
    }
    ip = request.getHeader("Proxy-Client-IP");
    if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
      return ip.trim();
    }
    ip = request.getHeader("WL-Proxy-Client-IP");
    if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
      return ip.trim();
    }
    ip = request.getRemoteAddr();
    return ip;
  }

  /**
   * 取得本機 Server 的 IP
   */
  public static String getHostIp() {
    try {
      // 如果有多個網卡，會回傳預設的那個
      InetAddress inetAddress = InetAddress.getLocalHost();
      return inetAddress.getHostAddress();
    } catch (UnknownHostException e) {
      return "127.0.0.1";
    }
  }
}