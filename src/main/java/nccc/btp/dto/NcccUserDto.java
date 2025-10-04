package nccc.btp.dto;

import java.io.Serializable;
import java.util.Set;

public class NcccUserDto implements Serializable {

  private static final long serialVersionUID = 7793461159743076383L;
  
  private String userId;
  private String userName;
  private String email;
  private String deptId;
  private String deptName;
  private String teamId;
  private String appAuthString;
  private String clientIp;
  private String hrAccount;
  private String hrid;
  private String ouCode;
  private String ouName;
  
  // 新增：Session 快取的 URI 清單
  private Set<String> permittedUris;

  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getDeptId() {
    return deptId;
  }
  public void setDeptId(String deptId) {
    this.deptId = deptId;
  }
  public String getDeptName() {
    return deptName;
  }
  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }
  public String getTeamId() {
    return teamId;
  }
  public void setTeamId(String teamId) {
    this.teamId = teamId;
  }
  public String getAppAuthString() {
    return appAuthString;
  }
  public void setAppAuthString(String appAuthString) {
    this.appAuthString = appAuthString;
  }
  public String getClientIp() {
    return clientIp;
  }
  public void setClientIp(String clientIp) {
    this.clientIp = clientIp;
  }
  public String getHrAccount() {
    return hrAccount;
  }
  public void setHrAccount(String hrAccount) {
    this.hrAccount = hrAccount;
  }
  public String getHrid() {
    return hrid;
  }
  public void setHrid(String hrid) {
    this.hrid = hrid;
  }

  public Set<String> getPermittedUris() {
    return permittedUris;
  }

  public void setPermittedUris(Set<String> permittedUris) {
    this.permittedUris = permittedUris;
  }

  public String getOuCode() {
    return ouCode;
  }

  public void setOuCode(String ouCode) {
    this.ouCode = ouCode;
  }

  public String getOuName() {
    return ouName;
  }

  public void setOuName(String ouName) {
    this.ouName = ouName;
  }
}
