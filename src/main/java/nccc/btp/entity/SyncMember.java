package nccc.btp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * SyncMember 對應資料表：SYNCMEMBER 複合主鍵：USERACCOUNT, OUCODE
 */
@Entity
@Table(name = "SYNCMEMBER")
@IdClass(SyncMemberId.class)
public class SyncMember {

  /** 人員帳號（主鍵之一） */
  @Id
  @Column(name = "USERACCOUNT", length = 50, nullable = false)
  private String userAccount;

  /** 所屬組織編號（主鍵之一） */
  @Id
  @Column(name = "OUCODE", length = 50, nullable = false)
  private String ouCode;

  /** 該人在所屬組織內的排序，從 0 開始 */
  @Column(name = "ORDERINDEX", nullable = false)
  private Integer orderIndex;

  /** 該身份是否為主要身分，1=是、0=否 */
  @Column(name = "USERDEFAULTROLE", nullable = false)
  private Boolean userDefaultRole;

  /** 職稱 */
  @Column(name = "LEADERTITLE", length = 30)
  private String leaderTitle;

  /** 職級 */
  @Column(name = "GRADELEVEL", nullable = true)
  private Integer gradeLevel;

  public String getUserAccount() {
    return userAccount;
  }

  public void setUserAccount(String userAccount) {
    this.userAccount = userAccount;
  }

  public String getOuCode() {
    return ouCode;
  }

  public void setOuCode(String ouCode) {
    this.ouCode = ouCode;
  }

  public Integer getOrderIndex() {
    return orderIndex;
  }

  public void setOrderIndex(Integer orderIndex) {
    this.orderIndex = orderIndex;
  }

  public Boolean getUserDefaultRole() {
    return userDefaultRole;
  }

  public void setUserDefaultRole(Boolean userDefaultRole) {
    this.userDefaultRole = userDefaultRole;
  }

  public String getLeaderTitle() {
    return leaderTitle;
  }

  public void setLeaderTitle(String leaderTitle) {
    this.leaderTitle = leaderTitle;
  }

  public Integer getGradeLevel() {
    return gradeLevel;
  }

  public void setGradeLevel(Integer gradeLevel) {
    this.gradeLevel = gradeLevel;
  }
}