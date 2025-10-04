package nccc.btp.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * SyncMember 的複合主鍵類別： 包含 userAccount, ouCode
 */
public class SyncMemberId implements Serializable {

  private static final long serialVersionUID = 1L;

  private String userAccount;
  private String ouCode;

  public SyncMemberId() {}

  public SyncMemberId(String userAccount, String ouCode) {
    this.userAccount = userAccount;
    this.ouCode = ouCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SyncMemberId)) {
      return false;
    }
    SyncMemberId that = (SyncMemberId) o;
    return Objects.equals(userAccount, that.userAccount) && Objects.equals(ouCode, that.ouCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userAccount, ouCode);
  }

  // ----------------------------------------------------------------
  // Getter / Setter
  // ----------------------------------------------------------------

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
}