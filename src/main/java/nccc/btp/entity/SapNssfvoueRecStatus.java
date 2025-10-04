package nccc.btp.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "SAP_NSSFVOUE_REC_STATUS")
@NamedQuery(name = "SapNssfvoueRecStatus.findAll", query = "SELECT s FROM SapNssfvoueRecStatus s")
public class SapNssfvoueRecStatus implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "NSSFVOUE_HEADER_BATCH")
  private String nssfvoueHeaderBatch;

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
  
  @Column(name = "SAP_TIME")
  private LocalDateTime sapTime;

  public SapNssfvoueRecStatus() {}

  public String getNssfvoueHeaderBatch() {
    return nssfvoueHeaderBatch;
  }

  public void setNssfvoueHeaderBatch(String nssfvoueHeaderBatch) {
    this.nssfvoueHeaderBatch = nssfvoueHeaderBatch;
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

  public LocalDateTime getSapTime() {
    return sapTime;
  }

  public void setSapTime(LocalDateTime sapTime) {
    this.sapTime = sapTime;
  }
  
}
