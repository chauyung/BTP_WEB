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
 * The persistent class for the NCCC_ASSETS_MANAGER database table.
 * 
 */
@Entity
@Table(name = "NCCC_ASSETS_MANAGER")
@NamedQuery(name = "NcccAssetsManager.findAll", query = "SELECT n FROM NcccAssetsManager n")
public class NcccAssetsManager implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "ASSETS_CODE")
  private String assetsCode;

  @Column(name = "ASSETS_DES")
  private String assetsDes;

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

  public NcccAssetsManager() {}

  public String getAssetsCode() {
    return this.assetsCode;
  }

  public void setAssetsCode(String assetsCode) {
    this.assetsCode = assetsCode;
  }

  public String getAssetsDes() {
    return this.assetsDes;
  }

  public void setAssetsDes(String assetsDes) {
    this.assetsDes = assetsDes;
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
