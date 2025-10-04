/**
 * 
 */
package nccc.btp.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * 複合主鍵類別，對應 VIEW 的主鍵欄位：ouCode, ouName
 */
public class SyncOUId implements Serializable {

  private static final long serialVersionUID = 1L;

  private String ouCode;
  private String ouName;

  public SyncOUId() {}

  public SyncOUId(String ouCode, String ouName) {
    this.ouCode = ouCode;
    this.ouName = ouName;
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


  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    SyncOUId that = (SyncOUId) o;
    return Objects.equals(ouCode, that.ouCode) && Objects.equals(ouName, that.ouName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ouCode, ouName);
  }

}
