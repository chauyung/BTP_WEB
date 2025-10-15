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
 * 報表管理：端末機機型資料檔(Eitity)
 * ------------------------------------------------------
 * 修訂人員: ChauYung
 * 修訂日期: 2025-10-08
 */
@Entity
@Table(name = "EMS_MTERM_MODEL")
@NamedQuery(name = "EmsMtermModel.findAll", query = "SELECT e FROM EmsMtermModel e")
public class EmsMtermModel implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * 機型代碼
   */
  @Id
  @Column(name = "MODEL_NO", length = 10, nullable = false)
  private String modelNo;

  /**
   * 端末機機型名稱
   */
  @Column(name = "MODEL_DESC", length = 50, nullable = false)
  private String modelDesc;

  /**
   * 端末機型種類
   */
  @Column(name = "MODEL_TYPE", length = 10)
  private String modelType;

  /**
   * 端末機前三或四碼
   */
  @Column(name = "TERM_START_NO", length = 4, nullable = false)
  private String termStartNo;

  /**
   * EDC帳單
   */
  @Column(name = "BILL_TYPE", length = 10)
  private String billType;

  /**
   * 虛擬端末機類型
   */
  @Column(name = "AUTH_ONLY", length = 10)
  private String authOnly;

  /**
   * AE Only 銀行
   */
  @Column(name = "AE_BANK", length = 10)
  private String aeBank;

  /**
   * 預設端末機版本
   */
  @Column(name = "VERSION_ID", length = 10)
  private String versionId;

  /**
   * 停止使用
   */
  @Column(name = "DEL_FLAG", length = 1)
  private String delFlag;

  /**
   * 是否為MPOS
   */
  @Column(name = "MPOS_FLAG", length = 1)
  private String mposFlag;

  /**
   * 是否為EDC
   */
  @Column(name = "EDC_FLAG", length = 1)
  private String edcFlag;

  /**
   * 是否為IC
   */
  @Column(name = "IC_FLAG", length = 1)
  private String icFlag;

  /**
   * 建檔人員
   */
  @Column(name = "CREATE_USER", length = 50)
  private String createUser;

  /**
   * 建檔日期
   */
  @Column(name = "CREATE_DATE", nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate createDate;

  /**
   * LAST_UPDATE_USER
   */
  @Column(name = "LAST_UPDATE_USER", length = 50)
  private String lastUpdateUser;

  /**
   * LAST_UPDATE_DATE
   */
  @Column(name = "LAST_UPDATE_DATE", nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate lastUpdateDate;

  /**
   * 
   */
  @Column(name = "MODEL_SHORT_NAME", length = 5)
  private String modelShortName;

  /**
   * Com Port數量
   */
  @Column(name = "COM_PORT_COUNT")
  private Integer comPortCount;

  @Column(name = "MASK_NO", length = 10)
  private String maskNo;

  /**
   * 平台業者特店代號
   */
  @Column(name = "PLATFORM_MCHT_NO", length = 15)
  private String platformMchtNo;

  /**
   * TMK把數
   */
  @Column(name = "TMK_COUNT")
  private Integer tmkCount;

  /**
   * 是否為mPOS READER
   */
  @Column(name = "MPOS_READER_FLAG", length = 1)
  private String mposReaderFlag;

  /**
   * 管制上架
   */
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
