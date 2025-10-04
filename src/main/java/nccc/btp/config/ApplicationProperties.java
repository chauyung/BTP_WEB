package nccc.btp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

  private boolean localMode = false;

  public boolean isLocalMode() {
    return localMode;
  }

  public void setLocalMode(boolean localMode) {
    this.localMode = localMode;
  }

}
