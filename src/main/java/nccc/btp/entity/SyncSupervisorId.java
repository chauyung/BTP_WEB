package nccc.btp.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * SyncSupervisor 的複合主鍵類別： 包含 userOuCode, orderIndex
 */
public class SyncSupervisorId implements Serializable {

  private static final long serialVersionUID = 1L;

  private String userOuCode;
  private String orderIndex;

  public SyncSupervisorId() {}

  public SyncSupervisorId(String userOuCode, String orderIndex) {
    this.userOuCode = userOuCode;
    this.orderIndex = orderIndex;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SyncSupervisorId)) {
      return false;
    }
    SyncSupervisorId that = (SyncSupervisorId) o;
    return Objects.equals(userOuCode, that.userOuCode)
        && Objects.equals(orderIndex, that.orderIndex);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userOuCode, orderIndex);
  }


  public String getUserOuCode() {
    return userOuCode;
  }

  public void setUserOuCode(String userOuCode) {
    this.userOuCode = userOuCode;
  }

  public String getOrderIndex() {
    return orderIndex;
  }

  public void setOrderIndex(String orderIndex) {
    this.orderIndex = orderIndex;
  }

}
