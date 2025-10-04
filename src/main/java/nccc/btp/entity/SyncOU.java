package nccc.btp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * SyncOU 對應資料表：SyncOU
 */
@Entity
@IdClass(SyncOUId.class)
@Table(name = "SYNC_OU")
public class SyncOU {

  /** 組織編號 */
  @Id
  @Column(name = "OU_CODE", length = 20)
  private String ouCode;

  /** 組織名稱 */
  @Id
  @Column(name = "OU_NAME", length = 40)
  private String ouName;

  /** 組織等級，如：公司、處、部 */
  @Column(name = "OU_LEVEL", length = 100)
  private String ouLevel;

  /** 上層組織編號，若為最上層可保留為 NULL 或留空字串 */
  @Column(name = "PARENT_OU_CODE", length = 20)
  private String parentOUCode;

  /** 該組織在所屬組織內的排序，從 0 開始 */
  @Column(name = "ORDER_INDEX")
  private int orderIndex;

  /** 主管帳號 */
  @Column(name = "MGR_ACCOUNT", length = 50)
  private String mgrAccount;

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

  public String getOuLevel() {
    return ouLevel;
  }

  public void setOuLevel(String ouLevel) {
    this.ouLevel = ouLevel;
  }

  public String getParentOUCode() {
    return parentOUCode;
  }

  public void setParentOUCode(String parentOUCode) {
    this.parentOUCode = parentOUCode;
  }

  public int getOrderIndex() {
    return orderIndex;
  }

  public void setOrderIndex(int orderIndex) {
    this.orderIndex = orderIndex;
  }

  public String getMgrAccount() {
    return mgrAccount;
  }

  public void setMgrAccount(String mgrAccount) {
    this.mgrAccount = mgrAccount;
  }

}
