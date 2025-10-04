package nccc.btp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * SyncSupervisor 對應資料表：SYNC_SUPERVISOR 複合主鍵：USER_OU_CODE, USER_ACCOUNT, SUPERVISOR_OU_CODE,
 * SUPERVISOR_USER_ACCOUNT
 */
@Entity
@Table(name = "SYNC_SUPERVISOR")
@IdClass(SyncSupervisorId.class)
public class SyncSupervisor {

  /** 人員所屬組織編號（主鍵之一） */
  @Id
  @Column(name = "USER_OU_CODE", length = 20)
  private String userOuCode;

  /** 人員帳號（主鍵之一） */
  @Column(name = "USER_ACCOUNT", length = 50)
  private String userAccount;

  /** 該成員的主管排序，從 0 開始 */
  @Id
  @Column(name = "ORDER_INDEX")
  private int orderIndex;

  /** 直屬主管人員所屬組織編號（主鍵之一） */
  @Column(name = "SUPERVISOR_OU_CODE", length = 20)
  private String supervisorOUCode;

  /** 直屬主管人員帳號（主鍵之一） */
  @Column(name = "SUPERVISOR_USER_ACCOUNT", length = 50)
  private String supervisorUserAccount;

  public String getUserOuCode() {
    return userOuCode;
  }

  public void setUserOuCode(String userOuCode) {
    this.userOuCode = userOuCode;
  }

  public String getUserAccount() {
    return userAccount;
  }

  public void setUserAccount(String userAccount) {
    this.userAccount = userAccount;
  }

  public int getOrderIndex() {
    return orderIndex;
  }

  public void setOrderIndex(int orderIndex) {
    this.orderIndex = orderIndex;
  }

  public String getSupervisorOUCode() {
    return supervisorOUCode;
  }

  public void setSupervisorOUCode(String supervisorOUCode) {
    this.supervisorOUCode = supervisorOUCode;
  }

  public String getSupervisorUserAccount() {
    return supervisorUserAccount;
  }

  public void setSupervisorUserAccount(String supervisorUserAccount) {
    this.supervisorUserAccount = supervisorUserAccount;
  }

}
