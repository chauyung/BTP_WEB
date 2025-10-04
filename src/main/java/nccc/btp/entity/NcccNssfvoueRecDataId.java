package nccc.btp.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NcccNssfvoueRecDataId implements Serializable {
  private static final long serialVersionUID = 1L;

  @Column(name = "NSSFVOUE_DATA_BATCH")
  private String nssfvoueDataBatch;

  @Column(name = "NSSFVOUE_SEQ_NUM")
  private String nssfvoueSeqNum;

  public NcccNssfvoueRecDataId() {}

  public NcccNssfvoueRecDataId(String batch, String seq) {
    this.nssfvoueDataBatch = batch;
    this.nssfvoueSeqNum = seq;
  }

  public String getNssfvoueDataBatch() {
    return nssfvoueDataBatch;
  }

  public void setNssfvoueDataBatch(String nssfvoueDataBatch) {
    this.nssfvoueDataBatch = nssfvoueDataBatch;
  }

  public String getNssfvoueSeqNum() {
    return nssfvoueSeqNum;
  }

  public void setNssfvoueSeqNum(String nssfvoueSeqNum) {
    this.nssfvoueSeqNum = nssfvoueSeqNum;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof NcccNssfvoueRecDataId)) return false;
    NcccNssfvoueRecDataId that = (NcccNssfvoueRecDataId) o;
    return Objects.equals(nssfvoueDataBatch, that.nssfvoueDataBatch) &&
           Objects.equals(nssfvoueSeqNum, that.nssfvoueSeqNum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nssfvoueDataBatch, nssfvoueSeqNum);
  }
}
