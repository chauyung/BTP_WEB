package nccc.btp.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_MENU")
public class SysMenu implements Serializable {

  private static final long serialVersionUID = 633370055197008548L;

  @Id
  @Column(name = "MENU_ID", length = 10)
  private String menuId;

  @Column(name = "MENU_NAME", length = 128)
  private String menuName;

  @Column(name = "MENU_TYPE", length = 2)
  private String menuType;

  @Column(name = "PARENT_ID", length = 36)
  private String parentId;

  @Column(name = "FUN_ID", length = 36)
  private String funId;

  @Column(name = "ODR_NUM")
  private Integer odrNum;

  @Column(name = "STATUS", length = 1)
  private String status;

  @Column(name = "ICON_NAME", length = 20)
  private String iconName;

  @Column(name = "PAGE_URI", length = 128)
  private String pageUri;

  @Column(name = "LDAP_SN")
  private Integer ldapSn;

  // ===== Getter / Setter =====

  public String getMenuId() {
    return menuId;
  }

  public void setMenuId(String menuId) {
    this.menuId = menuId;
  }

  public String getMenuName() {
    return menuName;
  }

  public void setMenuName(String menuName) {
    this.menuName = menuName;
  }

  public String getMenuType() {
    return menuType;
  }

  public void setMenuType(String menuType) {
    this.menuType = menuType;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public String getFunId() {
    return funId;
  }

  public void setFunId(String funId) {
    this.funId = funId;
  }

  public Integer getOdrNum() {
    return odrNum;
  }

  public void setOdrNum(Integer odrNum) {
    this.odrNum = odrNum;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getIconName() {
    return iconName;
  }

  public void setIconName(String iconName) {
    this.iconName = iconName;
  }

  public String getPageUri() {
    return pageUri;
  }

  public void setPageUri(String pageUri) {
    this.pageUri = pageUri;
  }

  public Integer getLdapSn() {
    return ldapSn;
  }

  public void setLdapSn(Integer ldapSn) {
    this.ldapSn = ldapSn;
  }
}