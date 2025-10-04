package nccc.btp.entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "ZMMTSUPPLIER")
public class ZmmtSupplier implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "PARTNER", length = 10, nullable = false)
  private String partner;

  @Column(name = "MANDT", length = 3)
  private String mandt;

  @Column(name = "LAND1", length = 3)
  private String land1;

  @Column(name = "BU_SORT1", length = 20)
  private String buSort1;

  @Column(name = "BU_SORT2", length = 20)
  private String buSort2;

  @Column(name = "NAME_ORG1", length = 40)
  private String nameOrg1;

  @Column(name = "TAXTYPE", length = 4)
  private String taxtype;

  @Column(name = "ZTERM", length = 4)
  private String zterm;

  @Column(name = "AKONT", length = 10)
  private String akont;

  @Column(name = "TAXNUM", length = 20)
  private String taxnum;
  
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

  public ZmmtSupplier() {}

  public String getPartner() {
    return partner;
  }

  public void setPartner(String partner) {
    this.partner = partner;
  }

  public String getMandt() {
    return mandt;
  }

  public void setMandt(String mandt) {
    this.mandt = mandt;
  }

  public String getLand1() {
    return land1;
  }

  public void setLand1(String land1) {
    this.land1 = land1;
  }

  public String getBuSort1() {
    return buSort1;
  }

  public void setBuSort1(String buSort1) {
    this.buSort1 = buSort1;
  }

  public String getBuSort2() {
    return buSort2;
  }

  public void setBuSort2(String buSort2) {
    this.buSort2 = buSort2;
  }

  public String getNameOrg1() {
    return nameOrg1;
  }

  public void setNameOrg1(String nameOrg1) {
    this.nameOrg1 = nameOrg1;
  }

  public String getTaxtype() {
    return taxtype;
  }

  public void setTaxtype(String taxtype) {
    this.taxtype = taxtype;
  }

  public String getZterm() {
    return zterm;
  }

  public void setZterm(String zterm) {
    this.zterm = zterm;
  }

  public String getAkont() {
    return akont;
  }

  public void setAkont(String akont) {
    this.akont = akont;
  }

  public String getTaxnum() {
    return taxnum;
  }

  public void setTaxnum(String taxnum) {
    this.taxnum = taxnum;
  }

  public String getUpdateUser() {
    return updateUser;
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
