//package nccc.btp.entity;
//
//import java.io.Serializable;
//import java.time.LocalDate;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.PrePersist;
//import javax.persistence.PreUpdate;
//import javax.persistence.Table;
//import com.fasterxml.jackson.annotation.JsonFormat;
//
///**
// * 指派人員TABLE 以廢棄改用BPM_NCCC_MISSION_ASSIGNER
// */
//@Entity
//@Table(name = "SIGN_FLOW_ASSIGN")
//public class SignFlowAssign implements Serializable {
//
//  private static final long serialVersionUID = 1L;
//
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  @Column(name = "ID", nullable = false)
//  private Long id;
//
//  @Column(name = "LDAP_USER_ID", length = 50)
//  private String ldapUserId;
//
//  @Column(name = "HR_ACCOUNT", length = 20)
//  private String hrAccount;
//
//  @Column(name = "LDAP_USER_NAME", length = 30)
//  private String ldapUserName;
//
//  @Column(name = "LDAP_USER_EMAIL", length = 40)
//  private String ldapUserEmail;
//
//  @Column(name = "DEP_ID", length = 20)
//  private String depId;
//
//  @Column(name = "UPDATE_USER", length = 50)
//  private String updateUser;
//
//  @Column(name = "UPDATE_DATE")
//  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//  private LocalDate updateDate;
//
//  @PrePersist
//  @PreUpdate
//  protected void onUpdate() {
//    updateDate = LocalDate.now();
//  }
//
//  public SignFlowAssign() {}
//
//  public Long getId() {
//    return id;
//  }
//
//  public void setId(Long id) {
//    this.id = id;
//  }
//
//  public String getLdapUserId() {
//    return ldapUserId;
//  }
//
//  public void setLdapUserId(String ldapUserId) {
//    this.ldapUserId = ldapUserId;
//  }
//
//  public String getHrAccount() {
//    return hrAccount;
//  }
//
//  public void setHrAccount(String hrAccount) {
//    this.hrAccount = hrAccount;
//  }
//
//  public String getLdapUserName() {
//    return ldapUserName;
//  }
//
//  public void setLdapUserName(String ldapUserName) {
//    this.ldapUserName = ldapUserName;
//  }
//
//  public String getLdapUserEmail() {
//    return ldapUserEmail;
//  }
//
//  public void setLdapUserEmail(String ldapUserEmail) {
//    this.ldapUserEmail = ldapUserEmail;
//  }
//
//  public String getDepId() {
//    return depId;
//  }
//
//  public void setDepId(String depId) {
//    this.depId = depId;
//  }
//
//  public String getUpdateUser() {
//    return updateUser;
//  }
//
//  public void setUpdateUser(String updateUser) {
//    this.updateUser = updateUser;
//  }
//
//  public LocalDate getUpdateDate() {
//    return updateDate;
//  }
//
//  public void setUpdateDate(LocalDate updateDate) {
//    this.updateDate = updateDate;
//  }
//
//}
