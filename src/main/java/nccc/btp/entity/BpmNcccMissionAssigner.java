/**
 * 
 */
package nccc.btp.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BPM_NCCC_MISSION_ASSIGNER")
public class BpmNcccMissionAssigner {
  
  @Id
  @Column(name = "OU_CODE", length = 10)
  private String ouCode;

  @Column(name = "HRID", length = 10)
  private String hrid;

  @Column(name = "NAME", length = 100)
  private String name;

  @Column(name = "DESCRIPTION", length = 255)
  private String description;

  @Column(name = "CREATE_USER", length = 50)
  private String createUser;

  @Column(name = "CREATE_DATE")
  private LocalDateTime createDate;

  @Column(name = "UPDATE_USER", length = 50)
  private String updateUser;

  @Column(name = "UPDATE_DATE")
  private LocalDateTime updateDate;


  public String getOuCode() {
    return ouCode;
  }

  public void setOuCode(String ouCode) {
    this.ouCode = ouCode;
  }

  public String getHrid() {
    return hrid;
  }

  public void setHrid(String hrid) {
    this.hrid = hrid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  public String getUpdateUser() {
    return updateUser;
  }

  public void setUpdateUser(String updateUser) {
    this.updateUser = updateUser;
  }

  public LocalDateTime getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(LocalDateTime updateDate) {
    this.updateDate = updateDate;
  }
}
