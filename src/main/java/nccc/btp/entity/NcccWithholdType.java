package nccc.btp.entity;

import java.io.Serializable;
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
 * The persistent class for the NCCC_WITHHOLD_TYPE database table.
 * 
 */
@Entity
@Table(name = "NCCC_WITHHOLD_TYPE")
@NamedQuery(name = "NcccWithholdType.findAll", query = "SELECT n FROM NcccWithholdType n")
public class NcccWithholdType implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private long id;

  @Column(name = "SAKNR", length = 10)
  private String saknr;

  @Column(name = "WITHHOLD_ACCOUNT", length = 10)
  private String withholdAccount;

  @Column(name = "RESULTING_CODE", length = 2)
  private String resultingCode;

  @Column(name = "INCOME_CATEGORY", length = 20)
  private String incomeCategory;

  @Column(name = "WITHHOLD_DESCRIPTION", length = 100)
  private String withholdDescription;

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

  public NcccWithholdType() {}

  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getSaknr() {
    return this.saknr;
  }

  public void setSaknr(String saknr) {
    this.saknr = saknr;
  }

  public String getWithholdAccount() {
    return this.withholdAccount;
  }

  public void setWithholdAccount(String withholdAccount) {
    this.withholdAccount = withholdAccount;
  }

  public String getResultingCode() {
    return resultingCode;
  }

  public void setResultingCode(String resultingCode) {
    this.resultingCode = resultingCode;
  }

  public String getIncomeCategory() {
    return incomeCategory;
  }

  public void setIncomeCategory(String incomeCategory) {
    this.incomeCategory = incomeCategory;
  }

  public String getWithholdDescription() {
    return withholdDescription;
  }

  public void setWithholdDescription(String withholdDescription) {
    this.withholdDescription = withholdDescription;
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
