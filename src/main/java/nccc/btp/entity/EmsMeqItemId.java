package nccc.btp.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 報表管理：設備項目資料檔(Eitity)KEY
 * ------------------------------------------------------
 * 修訂人員: ChauYung
 * 修訂日期: 2025-10-08
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmsMeqItemId implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * 設備種類
   */
  @Column(name = "EQ_TYPE")
  private String eqType;

  /**
   * 採購流水編號
   */
  @Column(name = "WEALTH_NO")
  private String wealthNo;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
		return true;
	}
    if (!(o instanceof EmsMeqItemId)) {
		return false;
	}
    EmsMeqItemId that = (EmsMeqItemId) o;
    return Objects.equals(eqType, that.eqType) && Objects.equals(wealthNo, that.wealthNo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eqType, wealthNo);
  }
}
