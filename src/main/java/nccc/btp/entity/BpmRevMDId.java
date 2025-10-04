/**
 * 
 */
package nccc.btp.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * 驗收單明細 複合主鍵類別
 */
public class BpmRevMDId implements Serializable {

  private static final long serialVersionUID = 3675433962060953674L;

  private String revNo;

  private String poItemNo;

  private String revItemNo;

  public BpmRevMDId() {}

  public BpmRevMDId(String revNo, String poItemNo, String revItemNo) {
    this.revNo = revNo;
    this.poItemNo = poItemNo;
    this.revItemNo = revItemNo;
  }

  public String getRevNo() {
    return revNo;
  }

  public void setRevNo(String revNo) {
    this.revNo = revNo;
  }

  public String getPoItemNo() {
    return poItemNo;
  }

  public void setPoItemNo(String poItemNo) {
    this.poItemNo = poItemNo;
  }

  public String getRevItemNo() {
    return revItemNo;
  }

  public void setRevItemNo(String revItemNo) {
    this.revItemNo = revItemNo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    BpmRevMDId that = (BpmRevMDId) o;
    return Objects.equals(revNo, that.revNo) && Objects.equals(poItemNo, that.poItemNo)
        && Objects.equals(revItemNo, that.revItemNo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(revNo, poItemNo, revItemNo);
  }
}
