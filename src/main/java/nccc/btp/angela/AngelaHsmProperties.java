/**
 * 
 */
package nccc.btp.angela;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "angela.hsm")
public class AngelaHsmProperties {
  
  private String ip;
  
  private String ssoId;
  
  private String keyName;

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getSsoId() {
    return ssoId;
  }

  public void setSsoId(String ssoId) {
    this.ssoId = ssoId;
  }

  public String getKeyName() {
    return keyName;
  }

  public void setKeyName(String keyName) {
    this.keyName = keyName;
  }

}
