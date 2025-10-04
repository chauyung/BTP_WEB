package nccc.btp.entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * The persistent class for the NCCC_CURRENCY database table.
 * 
 */
@Entity
@Table(name = "NCCC_CURRENCY")
@NamedQuery(name = "NcccCurrency.findAll", query = "SELECT n FROM NcccCurrency n")
public class NcccCurrency implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private String waers;

  private String ktext;

  @Column(name = "UPDATE_USER", length = 50)
  private String updateUser;

  @Column(name = "UPDATE_DATE", nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate updateDate;
  
  @PrePersist
  @PreUpdate
  protected void onUpdate() {
    updateDate = LocalDate.now();
  }

  public NcccCurrency() {}

  public String getWaers() {
    return this.waers;
  }

  public void setWaers(String waers) {
    this.waers = waers;
  }

  public String getKtext() {
    return this.ktext;
  }

  public void setKtext(String ktext) {
    this.ktext = ktext;
  }

  public String getUpdateUser() {
    return this.updateUser;
  }

  public void setUpdateUser(String updateUser) {
    this.updateUser = updateUser;
  }

  public LocalDate getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(LocalDate updateDate) {
    this.updateDate = updateDate;
  }

}
