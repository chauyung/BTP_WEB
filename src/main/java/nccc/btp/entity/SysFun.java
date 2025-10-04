package nccc.btp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_FUN")
public class SysFun {

  @Id
  @Column(name = "UUID", length = 36)
  private String uuid;

  @Column(name = "MENU_LINK", length = 1)
  private String menuLink;

  @Column(name = "FUN_ID", length = 36)
  private String funId;

  @Column(name = "FUN_NAME", length = 64)
  private String funName;

  @Column(name = "URI", length = 128)
  private String uri;

  @Column(name = "PAGE_ENTRY", length = 1)
  private String pageEntry;

  @Column(name = "MENU_ID", length = 10)
  private String menuId;

  @Column(name = "LDAP_FUN_ID", length = 10)
  private String ldapFunId;

  @Column(name = "LDAP_ATTRIBUTE")
  private Integer ldapAttribute;

  @Column(name = "APPROVAL_LEVEL")
  private Integer approvalLevel;

  @Column(name = "API_NAME", length = 1024)
  private String apiName;

  @Column(name = "API_GROUP")
  private Integer apiGroup;

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getMenuLink() {
    return menuLink;
  }

  public void setMenuLink(String menuLink) {
    this.menuLink = menuLink;
  }

  public String getFunId() {
    return funId;
  }

  public void setFunId(String funId) {
    this.funId = funId;
  }

  public String getFunName() {
    return funName;
  }

  public void setFunName(String funName) {
    this.funName = funName;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getPageEntry() {
    return pageEntry;
  }

  public void setPageEntry(String pageEntry) {
    this.pageEntry = pageEntry;
  }

  public String getMenuId() {
    return menuId;
  }

  public void setMenuId(String menuId) {
    this.menuId = menuId;
  }

  public String getLdapFunId() {
    return ldapFunId;
  }

  public void setLdapFunId(String ldapFunId) {
    this.ldapFunId = ldapFunId;
  }

  public Integer getLdapAttribute() {
    return ldapAttribute;
  }

  public void setLdapAttribute(Integer ldapAttribute) {
    this.ldapAttribute = ldapAttribute;
  }

  public Integer getApprovalLevel() {
    return approvalLevel;
  }

  public void setApprovalLevel(Integer approvalLevel) {
    this.approvalLevel = approvalLevel;
  }

  public String getApiName() {
    return apiName;
  }

  public void setApiName(String apiName) {
    this.apiName = apiName;
  }

  public Integer getApiGroup() {
    return apiGroup;
  }

  public void setApiGroup(Integer apiGroup) {
    this.apiGroup = apiGroup;
  }
}