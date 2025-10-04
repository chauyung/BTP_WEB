package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the NCCC_COST_ACCOUNT database table.
 * 
 */
@Entity
@Table(name = "NCCC_COST_ACCOUNT")
@NamedQuery(name = "NcccCostAccount.findAll", query = "SELECT n FROM NcccCostAccount n")
public class NcccCostAccount implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private long id;

  private String extyp;

  private String saknr;

  @Column(name = "SERIAL_NO")
  private BigDecimal serialNo;

  private String sgtxt;

  private String text1;

  private String zguifg;

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

  public NcccCostAccount() {}

  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getExtyp() {
    return this.extyp;
  }

  public void setExtyp(String extyp) {
    this.extyp = extyp;
  }

  public String getSaknr() {
    return this.saknr;
  }

  public void setSaknr(String saknr) {
    this.saknr = saknr;
  }

  public BigDecimal getSerialNo() {
    return this.serialNo;
  }

  public void setSerialNo(BigDecimal serialNo) {
    this.serialNo = serialNo;
  }

  public String getSgtxt() {
    return this.sgtxt;
  }

  public void setSgtxt(String sgtxt) {
    this.sgtxt = sgtxt;
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
