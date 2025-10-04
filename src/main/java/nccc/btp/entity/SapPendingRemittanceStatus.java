package nccc.btp.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "SAP_PENDING_REMITTANCE_STATUS")
@NamedQuery(name = "SapPendingRemittanceStatus.findAll",
    query = "SELECT s FROM SapPendingRemittanceStatus s")
public class SapPendingRemittanceStatus implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "ID")
  private Long id;

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

  @Column(name = "CREATE_USER")
  private String createUser;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }
}
