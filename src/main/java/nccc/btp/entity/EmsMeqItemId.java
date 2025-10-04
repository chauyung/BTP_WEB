package nccc.btp.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EmsMeqItemId implements Serializable {
  
  private static final long serialVersionUID = 1L;

  @Column(name = "EQ_TYPE")
  private String eqType;

  @Column(name = "WEALTH_NO")
  private String wealthNo;

  public EmsMeqItemId() {}

  public EmsMeqItemId(String eqType, String wealthNo) {
    this.eqType = eqType;
    this.wealthNo = wealthNo;
  }

  public String getEqType() {
    return eqType;
  }

  public void setEqType(String eqType) {
    this.eqType = eqType;
  }

  public String getWealthNo() {
    return wealthNo;
  }

  public void setWealthNo(String wealthNo) {
    this.wealthNo = wealthNo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof EmsMeqItemId))
      return false;
    EmsMeqItemId that = (EmsMeqItemId) o;
    return Objects.equals(eqType, that.eqType) && Objects.equals(wealthNo, that.wealthNo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eqType, wealthNo);
  }
}
