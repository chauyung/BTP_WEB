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
 * The persistent class for the NCCC_COST_CENTER database table.
 * 
 */
@Entity
@Table(name = "NCCC_COST_CENTER")
@NamedQuery(name = "NcccCostCenter.findAll", query = "SELECT n FROM NcccCostCenter n")
public class NcccCostCenter implements Serializable {
  private static final long serialVersionUID = 1L;

  //成本中心
  @Id
  private String kostl;

  //成本控制範圍
  private String kokrs;

  //成本中心種類
  private String kosar;

  //一般名稱 
  private String ktext;

  @Column(name = "UPDATE_USER", length = 50)
  private String updateUser;

  @Column(name = "UPDATE_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate updateDate;
  
  @PrePersist
  @PreUpdate
  protected void onUpdate() {
    updateDate = LocalDate.now();
  }

  public NcccCostCenter() {}

  public String getKostl() {
    return this.kostl;
  }

  public void setKostl(String kostl) {
    this.kostl = kostl;
  }

  public String getKokrs() {
    return this.kokrs;
  }

  public void setKokrs(String kokrs) {
    this.kokrs = kokrs;
  }

  public String getKosar() {
    return this.kosar;
  }

  public void setKosar(String kosar) {
    this.kosar = kosar;
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
