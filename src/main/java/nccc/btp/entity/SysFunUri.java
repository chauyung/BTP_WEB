package nccc.btp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_FUN_URI")
public class SysFunUri {

  @Id
  @Column(name = "URI", length = 128)
  private String uri;

  @Column(name = "FUN_ID", length = 36)
  private String funId;

  @Column(name = "LOGIN_REQUIRED", length = 1)
  private String loginRequired;

  @Column(name = "AP_LOG", length = 1)
  private String apLog;

  @Column(name = "AP_LOG_ACCESS_TYPE", length = 2)
  private String apLogAccessType;

  @Column(name = "AP_LOG_COUNT", length = 1)
  private String apLogCount;

  @Column(name = "URI_DESCRIPTION", length = 128)
  private String uriDescription;

  // Getter & Setter

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getFunId() {
    return funId;
  }

  public void setFunId(String funId) {
    this.funId = funId;
  }

  public String getLoginRequired() {
    return loginRequired;
  }

  public void setLoginRequired(String loginRequired) {
    this.loginRequired = loginRequired;
  }

  public String getApLog() {
    return apLog;
  }

  public void setApLog(String apLog) {
    this.apLog = apLog;
  }

  public String getApLogAccessType() {
    return apLogAccessType;
  }

  public void setApLogAccessType(String apLogAccessType) {
    this.apLogAccessType = apLogAccessType;
  }

  public String getApLogCount() {
    return apLogCount;
  }

  public void setApLogCount(String apLogCount) {
    this.apLogCount = apLogCount;
  }

  public String getUriDescription() {
    return uriDescription;
  }

  public void setUriDescription(String uriDescription) {
    this.uriDescription = uriDescription;
  }
}