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
 * The persistent class for the NCCC_TAX_CODE database table.
 * 
 */
@Entity
@Table(name = "NCCC_TAX_CODE")
@NamedQuery(name = "NcccTaxCode.findAll", query = "SELECT n FROM NcccTaxCode n")
public class NcccTaxCode implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "TAX_CODE")
  private String taxCode;

  @Column(name = "TAX_CODE_DES")
  private String taxCodeDes;

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

  public NcccTaxCode() {}

  public String getTaxCode() {
    return this.taxCode;
  }

  public void setTaxCode(String taxCode) {
    this.taxCode = taxCode;
  }

  public String getTaxCodeDes() {
    return this.taxCodeDes;
  }

  public void setTaxCodeDes(String taxCodeDes) {
    this.taxCodeDes = taxCodeDes;
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
