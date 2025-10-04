/**
 * 
 */
package nccc.btp.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * 驗收紀錄 複合主鍵類別
 */
public class BpmRevMDRId implements Serializable {

  private static final long serialVersionUID = -2731504746846831263L;

  private String revNo;

  private String poItemNo;

  private String revItemNo;

  private String revSeqNo;

  public BpmRevMDRId() {}

  public BpmRevMDRId(String revNo, String poItemNo, String revItemNo, String revSeqNo) {
    this.revNo = revNo;
    this.poItemNo = poItemNo;
    this.revItemNo = revItemNo;
    this.revSeqNo = revSeqNo;
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

  public String getRevSeqNo() {
    return revSeqNo;
  }

  public void setRevSeqNo(String revSeqNo) {
    this.revSeqNo = revSeqNo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    BpmRevMDRId that = (BpmRevMDRId) o;
    return Objects.equals(revNo, that.revNo) && Objects.equals(poItemNo, that.poItemNo)
        && Objects.equals(revItemNo, that.revItemNo) && Objects.equals(revSeqNo, that.revSeqNo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(revNo, poItemNo, revItemNo, revSeqNo);
  }
}
