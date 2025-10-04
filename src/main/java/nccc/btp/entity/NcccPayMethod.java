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
 * The persistent class for the NCCC_PAY_METHOD database table.
 * 
 */
@Entity
@Table(name = "NCCC_PAY_METHOD")
@NamedQuery(name = "NcccPayMethod.findAll", query = "SELECT n FROM NcccPayMethod n")
public class NcccPayMethod implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private String zlsch;

  private String text1;

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

  public NcccPayMethod() {}

  public String getZlsch() {
    return this.zlsch;
  }

  public void setZlsch(String zlsch) {
    this.zlsch = zlsch;
  }

  public String getText1() {
    return this.text1;
  }

  public void setText1(String text1) {
    this.text1 = text1;
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
