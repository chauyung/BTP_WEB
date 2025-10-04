package nccc.btp.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * SyncUser 對應資料表：SYNC_USER
 */
@Entity
@IdClass(SyncUserId.class)
@Table(name = "SYNC_USER")
public class SyncUser {

  /** 人員帳號（主鍵） */
  @Id
  @Column(name = "ACCOUNT", length = 50, nullable = false)
  private String account;

  /** 姓名 */
  @Id
  @Column(name = "DISPLAY_NAME", length = 100)
  private String displayName;

  /** 人員編號 */
  @Id
  @Column(name = "HRID", length = 10)
  private String hrid;

  /** 帳號是否禁用，1=禁用、0=啟用 */
  @Column(name = "DISABLED", length = 1)
  private String disabled;

  /** 到職日 */
  @Column(name = "DATE_HIRED")
  private LocalDateTime dateHired;

  /** 成本中心 / 部門 */
  @Column(name = "COST_CENTER", length = 20)
  private String costCenter;

  /** 電話 */
  @Column(name = "OFFICE_PHONE", length = 20)
  private String officePhone;

  /** 電子郵件 */
  @Column(name = "EMAIL", length = 100)
  private String email;

  @Column(name = "OU_CODE", length = 20)
  private String ouCode;

  @Column(name = "OU_NAME", length = 80)
  private String ouName;

  @Column(name = "USER_DEFAULT_ROLE", length = 1)
  private String userDefaultRole;

  @Column(name = "LEADER_TITLE_ID", length = 100)
  private Long leaderTitleId;

  @Column(name = "LEADER_TITLE", length = 100)
  private String leaderTitle;

  @Column(name = "LEVEL_ID", length = 100)
  private Long levelId;

  @Column(name = "LEVEL_NAME", length = 100)
  private String levelName;

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getHrid() {
    return hrid;
  }

  public void setHrid(String hrid) {
    this.hrid = hrid;
  }

  public String getDisabled() {
    return disabled;
  }

  public void setDisabled(String disabled) {
    this.disabled = disabled;
  }

  public LocalDateTime getDateHired() {
    return dateHired;
  }

  public void setDateHired(LocalDateTime dateHired) {
    this.dateHired = dateHired;
  }

  public String getCostCenter() {
    return costCenter;
  }

  public void setCostCenter(String costCenter) {
    this.costCenter = costCenter;
  }

  public String getOfficePhone() {
    return officePhone;
  }

  public void setOfficePhone(String officePhone) {
    this.officePhone = officePhone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public String getUserDefaultRole() {
    return userDefaultRole;
  }

  public void setUserDefaultRole(String userDefaultRole) {
    this.userDefaultRole = userDefaultRole;
  }

  public Long getLeaderTitleId() {
    return leaderTitleId;
  }

  public void setLeaderTitleId(Long leaderTitleId) {
    this.leaderTitleId = leaderTitleId;
  }

  public String getLeaderTitle() {
    return leaderTitle;
  }

  public void setLeaderTitle(String leaderTitle) {
    this.leaderTitle = leaderTitle;
  }

  public Long getLevelId() {
    return levelId;
  }

  public void setLevelId(Long levelId) {
    this.levelId = levelId;
  }

  public String getLevelName() {
    return levelName;
  }

  public void setLevelName(String levelName) {
    this.levelName = levelName;
  }

}
