package nccc.btp.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "FIRSTBANK_REMITTANCE")
public class FirstbankRemittance {

  @EmbeddedId
  private RemittanceId id;

  @Lob
  @Column(name = "DATA")
  private String data;

  public FirstbankRemittance() {}

  public FirstbankRemittance(RemittanceId id, String data) {
    this.id = id;
    this.data = data;
  }

  public RemittanceId getId() {
    return id;
  }

  public void setId(RemittanceId id) {
    this.id = id;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}
