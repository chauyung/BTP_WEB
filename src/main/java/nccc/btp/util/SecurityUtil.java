package nccc.btp.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import nccc.btp.dto.NcccUserDto;

public class SecurityUtil {

  private SecurityUtil() {}

  public static NcccUserDto getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getPrincipal() instanceof NcccUserDto) {
      return (NcccUserDto) auth.getPrincipal();
    }
    return null;
  }
}
