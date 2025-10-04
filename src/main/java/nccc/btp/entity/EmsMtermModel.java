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

@Entity
@Table(name = "EMS_MTERM_MODEL")
@NamedQuery(name = "EmsMtermModel.findAll", query = "SELECT e FROM EmsMtermModel e")
public class EmsMtermModel implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "MODEL_NO", length = 10, nullable = false)
  private String modelNo;

  @Column(name = "MODEL_DESC", length = 50, nullable = false)
  private String modelDesc;

  @Column(name = "MODEL_TYPE", length = 10)
  private String modelType;

  @Column(name = "TERM_START_NO", length = 4, nullable = false)
  private String termStartNo;

  @Column(name = "BILL_TYPE", length = 10)
  private String billType;

  @Column(name = "AUTH_ONLY", length = 10)
  private String authOnly;

  @Column(name = "AE_BANK", length = 10)
  private String aeBank;

  @Column(name = "VERSION_ID", length = 10)
  private String versionId;

  @Column(name = "DEL_FLAG", length = 1)
  private String delFlag;

  @Column(name = "MPOS_FLAG", length = 1)
  private String mposFlag;

  @Column(name = "EDC_FLAG", length = 1)
  private String edcFlag;

  @Column(name = "IC_FLAG", length = 1)
  private String icFlag;

  @Column(name = "CREATE_USER", length = 50)
  private String createUser;

  @Column(name = "CREATE_DATE", nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate createDate;

  @Column(name = "LAST_UPDATE_USER", length = 50)
  private String lastUpdateUser;

  @Column(name = "LAST_UPDATE_DATE", nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate lastUpdateDate;

  @Column(name = "MODEL_SHORT_NAME", length = 5)
  private String modelShortName;

  @Column(name = "COM_PORT_COUNT")
  private Integer comPortCount;

  @Column(name = "MASK_NO", length = 10)
  private String maskNo;

  @Column(name = "PLATFORM_MCHT_NO", length = 15)
  private String platformMchtNo;

  @Column(name = "TMK_COUNT")
  private Integer tmkCount;

  @Column(name = "MPOS_READER_FLAG", length = 1)
  private String mposReaderFlag;

  @Column(name = "CONTROL_FLAG", length = 1, nullable = false)
  private String controlFlag;
  
  @PrePersist
  @PreUpdate
  protected void onUpdate() {
    lastUpdateDate = LocalDate.now();
  }

  public EmsMtermModel() {}

  public String getModelNo() {
    return modelNo;
  }

  public void setModelNo(String modelNo) {
    this.modelNo = modelNo;
  }

  public String getModelDesc() {
    return modelDesc;
  }

  public void setModelDesc(String modelDesc) {
    this.modelDesc = modelDesc;
  }

  public String getModelType() {
    return modelType;
  }

  public void setModelType(String modelType) {
    this.modelType = modelType;
  }

  public String getTermStartNo() {
    return termStartNo;
  }

  public void setTermStartNo(String termStartNo) {
    this.termStartNo = termStartNo;
  }

  public String getBillType() {
    return billType;
  }

  public void setBillType(String billType) {
    this.billType = billType;
  }

  public String getAuthOnly() {
    return authOnly;
  }

  public void setAuthOnly(String authOnly) {
    this.authOnly = authOnly;
  }

  public String getAeBank() {
    return aeBank;
  }

  public void setAeBank(String aeBank) {
    this.aeBank = aeBank;
  }

  public String getVersionId() {
    return versionId;
  }

  public void setVersionId(String versionId) {
    this.versionId = versionId;
  }

  public String getDelFlag() {
    return delFlag;
  }

  public void setDelFlag(String delFlag) {
    this.delFlag = delFlag;
  }

  public String getMposFlag() {
    return mposFlag;
  }

  public void setMposFlag(String mposFlag) {
    this.mposFlag = mposFlag;
  }

  public String getEdcFlag() {
    return edcFlag;
  }

  public void setEdcFlag(String edcFlag) {
    this.edcFlag = edcFlag;
  }

  public String getIcFlag() {
    return icFlag;
  }

  public void setIcFlag(String icFlag) {
    this.icFlag = icFlag;
  }

  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }

  public LocalDate getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDate createDate) {
    this.createDate = createDate;
  }

  public String getLastUpdateUser() {
    return lastUpdateUser;
  }

  public void setLastUpdateUser(String lastUpdateUser) {
    this.lastUpdateUser = lastUpdateUser;
  }

  public LocalDate getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(LocalDate lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  public String getModelShortName() {
    return modelShortName;
  }

  public void setModelShortName(String modelShortName) {
    this.modelShortName = modelShortName;
  }

  public Integer getComPortCount() {
    return comPortCount;
  }

  public void setComPortCount(Integer comPortCount) {
    this.comPortCount = comPortCount;
  }

  public String getMaskNo() {
    return maskNo;
  }

  public void setMaskNo(String maskNo) {
    this.maskNo = maskNo;
  }

  public String getPlatformMchtNo() {
    return platformMchtNo;
  }

  public void setPlatformMchtNo(String platformMchtNo) {
    this.platformMchtNo = platformMchtNo;
  }

  public Integer getTmkCount() {
    return tmkCount;
  }

  public void setTmkCount(Integer tmkCount) {
    this.tmkCount = tmkCount;
  }

  public String getMposReaderFlag() {
    return mposReaderFlag;
  }

  public void setMposReaderFlag(String mposReaderFlag) {
    this.mposReaderFlag = mposReaderFlag;
  }

  public String getControlFlag() {
    return controlFlag;
  }

  public void setControlFlag(String controlFlag) {
    this.controlFlag = controlFlag;
  }

}
