package nccc.btp.entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "SAP_EMS_REC_STATUS")
@NamedQuery(name = "SapEmsRecStatus.findAll", query = "SELECT s FROM SapEmsRecStatus s")
public class SapEmsRecStatus implements Serializable {
  
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "WEALTH_NO")
  private String wealthNo;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "BUKRS")
  private String bukrs;

  @Column(name = "BELNR")
  private String belnr;

  @Column(name = "GJAHR")
  private String gjahr;

  @Column(name = "MESSAGE")
  private String message;

  @Column(name = "SEND_DATE")
  private LocalDate sendDate;

  public SapEmsRecStatus() {}

  public String getWealthNo() {
    return wealthNo;
  }

  public void setWealthNo(String wealthNo) {
    this.wealthNo = wealthNo;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getBukrs() {
    return bukrs;
  }

  public void setBukrs(String bukrs) {
    this.bukrs = bukrs;
  }

  public String getBelnr() {
    return belnr;
  }

  public void setBelnr(String belnr) {
    this.belnr = belnr;
  }

  public String getGjahr() {
    return gjahr;
  }

  public void setGjahr(String gjahr) {
    this.gjahr = gjahr;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public LocalDate getSendDate() {
    return sendDate;
  }

  public void setSendDate(LocalDate sendDate) {
    this.sendDate = sendDate;
  }

}
