package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * The persistent class for the NCCC_RECEIPT_TYPE database table.
 * 
 */
@Entity
@Table(name = "NCCC_RECEIPT_TYPE")
@NamedQuery(name = "NcccReceiptType.findAll", query = "SELECT n FROM NcccReceiptType n")
public class NcccReceiptType implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private long id;

  @Column(name = "INCOMETAX", length = 1)
  private String incometax;

  @Column(name = "INTYP", length = 12)
  private String intyp;

  @Column(name = "MWSKZ", length = 2)
  private String mwskz;

  @Column(name = "TAXRATE", precision = 2, scale = 0)
  private BigDecimal taxrate;

  @Column(name = "TEXT1", length = 30)
  private String text1;

  @Column(name = "ZGUIFG", length = 1)
  private String zguifg;

  @Column(name = "ZFORM_CODE", precision = 2)
  private BigDecimal zformCode;

  @Column(name = "CUS_TYPE", length = 1)
  private String cusType;

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

  public NcccReceiptType() {}

  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getIncometax() {
    return this.incometax;
  }

  public void setIncometax(String incometax) {
    this.incometax = incometax;
  }

  public String getIntyp() {
    return this.intyp;
  }

  public void setIntyp(String intyp) {
    this.intyp = intyp;
  }

  public String getMwskz() {
    return this.mwskz;
  }

  public void setMwskz(String mwskz) {
    this.mwskz = mwskz;
  }


  public BigDecimal getTaxrate() {
    return this.taxrate;
  }

  public void setTaxrate(BigDecimal taxrate) {
    this.taxrate = taxrate;
  }

  public String getText1() {
    return this.text1;
  }

  public void setText1(String text1) {
    this.text1 = text1;
  }

  public String getZguifg() {
    return this.zguifg;
  }

  public void setZguifg(String zguifg) {
    this.zguifg = zguifg;
  }

  public BigDecimal getZformCode() {
    return zformCode;
  }

  public void setZformCode(BigDecimal zformCode) {
    this.zformCode = zformCode;
  }

  public String getCusType() {
    return cusType;
  }

  public void setCusType(String cusType) {
    this.cusType = cusType;
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
