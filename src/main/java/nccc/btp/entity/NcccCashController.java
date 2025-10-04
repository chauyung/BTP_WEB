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
 * The persistent class for the NCCC_CASH_CONTROLLER database table.
 * 
 */
@Entity
@Table(name = "NCCC_CASH_CONTROLLER")
@NamedQuery(name = "NcccCashController.findAll", query = "SELECT n FROM NcccCashController n")
public class NcccCashController implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "CASH_CONTROLLER_NO")
  private String cashControllerNo;

  @Column(name = "CASH_CONTROLLER_DES")
  private String cashControllerDes;

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

  public NcccCashController() {}

  public String getCashControllerNo() {
    return this.cashControllerNo;
  }

  public void setCashControllerNo(String cashControllerNo) {
    this.cashControllerNo = cashControllerNo;
  }

  public String getCashControllerDes() {
    return this.cashControllerDes;
  }

  public void setCashControllerDes(String cashControllerDes) {
    this.cashControllerDes = cashControllerDes;
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
