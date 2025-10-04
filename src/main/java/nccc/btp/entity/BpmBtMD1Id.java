/**
 * 
 */
package nccc.btp.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * 複合主鍵類別
 */
public class BpmBtMD1Id implements Serializable {

  private static final long serialVersionUID = 1L;

  private String btNo;

  private String btItemNo;

  public BpmBtMD1Id() {}

  public BpmBtMD1Id(String btNo, String btItemNo) {
    this.btNo = btNo;
    this.btItemNo = btItemNo;
  }


  public String getBtNo() {
    return btNo;
  }

  public void setBtNo(String btNo) {
    this.btNo = btNo;
  }

  public String getBtItemNo() {
    return btItemNo;
  }

  public void setBtItemNo(String btItemNo) {
    this.btItemNo = btItemNo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    BpmBtMD1Id that = (BpmBtMD1Id) o;
    return Objects.equals(btNo, that.btNo) && Objects.equals(btItemNo, that.btItemNo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(btNo, btItemNo);
  }
}
