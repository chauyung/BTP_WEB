package nccc.btp.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RemittanceId implements Serializable {
  private static final long serialVersionUID = 1L;

  @Column(name = "LAUFD", length = 10, nullable = false)
  private String laufd;

  @Column(name = "LAUFI", length = 10, nullable = false)
  private String laufi;

  @Column(name = "SND_DATE", length = 10, nullable = false)
  private String sndDate;

  public RemittanceId() {}

  public RemittanceId(String laufd, String laufi, String sndDate) {
    this.laufd = laufd;
    this.laufi = laufi;
    this.sndDate = sndDate;
  }

  public String getLaufd() {
    return laufd;
  }

  public void setLaufd(String laufd) {
    this.laufd = laufd;
  }

  public String getLaufi() {
    return laufi;
  }

  public void setLaufi(String laufi) {
    this.laufi = laufi;
  }

  public String getSndDate() {
    return sndDate;
  }

  public void setSndDate(String sndDate) {
    this.sndDate = sndDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof RemittanceId))
      return false;
    RemittanceId that = (RemittanceId) o;
    return java.util.Objects.equals(laufd, that.laufd)
        && java.util.Objects.equals(laufi, that.laufi)
        && java.util.Objects.equals(sndDate, that.sndDate);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(laufd, laufi, sndDate);
  }
}
