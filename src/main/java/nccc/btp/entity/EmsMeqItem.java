package nccc.btp.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 報表管理：設備項目資料檔(Eitity)
 * ------------------------------------------------------
 * 修訂人員: ChauYung
 * 修訂日期: 2025-10-08
 */
@Entity
@Table(name = "EMS_MEQ_ITEM")
@NamedQuery(name = "EmsMeqItem.findAll", query = "SELECT e FROM EmsMeqItem e")
public class EmsMeqItem implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private EmsMeqItemId id;

  /**
   * SERIAL_NO或SAM卡號碼
   */
  @Column(name = "SERIAL_NO", length = 30)
  private String serialNo;

  /**
   * SEQ NO
   */
  @Column(name = "PUR_SEQ_NO", length = 10)
  private String purSeqNo;

  /**
   * 設備代號
   */
  @Column(name = "EQ_NO", length = 15)
  private String eqNo;

  /**
   * 特店代號
   */
  @Column(name = "MCHT_NO", length = 15)
  private String mchtNo;

  /**
   * 機型代號
   */
  @Column(name = "MODEL_NO", length = 10)
  private String modelNo;

  /**
   * 庫存狀態(EQ_STATUS)
   */
  @Column(name = "STATUS", length = 10)
  private String status;

  /**
   * 位置
   */
  @Column(name = "LOCATION", length = 10)
  private String location;

  /**
   * 進貨日期
   */
  @Column(name = "PUR_DATE", length = 8)
  private String purDate;

  /**
   * 最後入庫日期
   */
  @Column(name = "STOCK_DATE", length = 8)
  private String stockDate;

  /**
   * 廠商代號
   */
  @Column(name = "VENDOR_ID", length = 10)
  private String vendorId;

  /**
   * 保固期限
   */
  @Column(name = "WARR_MONTH")
  private Integer warrMonth;

  /**
   * 保固開始日期
   */
  @Column(name = "WARR_ST_DATE", length = 8)
  private String warrStDate;

  /**
   * 保固到期日期
   */
  @Column(name = "WARR_EN_DATE", length = 8)
  private String warrEnDate;

  /**
   * 計費開始日期
   */
  @Column(name = "FEES_ST_DATE", length = 8)
  private String feesStDate;

  /**
   * CHARGE FLAG
   */
  @Column(name = "CHARGE_FLAG", length = 1)
  private String chargeFlag;

  /**
   * 是否共用
   */
  @Column(name = "SHARE_FLAG", length = 1)
  private String shareFlag;

  /**
   * 啟用日期
   */
  @Column(name = "START_DATE", length = 8)
  private String startDate;

  /**
   * 停用日期
   */
  @Column(name = "END_DATE", length = 8)
  private String endDate;

  /**
   * 備註
   */
  @Column(name = "REMARK", length = 500)
  private String remark;

  /**
   * 建檔人員
   */
  @Column(name = "CREATE_USER", length = 50)
  private String createUser;

  /**
   * 建檔日期
   */
  @Column(name = "CREATE_DATE")
  private LocalDate createDate;

  /**
   * LAST_UPDATE_USER
   */
  @Column(name = "LAST_UPDATE_USER", length = 50, nullable = false)
  private String lastUpdateUser;

  /**
   * LAST_UPDATE_DATE
   */
  @Column(name = "LAST_UPDATE_DATE", nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate lastUpdateDate;

  /**
   * 行管部財編
   */
  @Column(name = "NCCC_WEALTH_NO", length = 20)
  private String ncccWealthNo;

  /**
   * CONTACT LESS U
   */
  @Column(name = "U_FLAG", length = 1)
  private String uFlag;

  /**
   * CONTACT LESS V
   */
  @Column(name = "V_FLAG", length = 1)
  private String vFlag;

  /**
   * CONTACT LESS M
   */
  @Column(name = "M_FLAG", length = 1)
  private String mFlag;

  /**
   * CONTACT LESS J
   */
  @Column(name = "J_FLAG", length = 1)
  private String jFlag;

  /**
   * CONTACT LESS AE
   */
  @Column(name = "AE_FLAG", length = 1)
  private String aeFlag;

  /**
   * Load Key flag
   */
  @Column(name = "LOAD_KEY", length = 1)
  private String loadKey;
  
  /**
   * 備機
   */
  @Column(name = "WAIT_USE", length = 1)
  private String waitUse;

  /**
   * 待報廢
   */
  @Column(name = "WAIT_DISCARD", length = 1)
  private String waitDiscard;

  /**
   * 維護廠商
   */
  @Column(name = "MAIN_VENDOR_ID", length = 10)
  private String mainVendorId;

  /**
   * 是否為備援機(MFES)
   */
  @Column(name = "BACKUP_FLAG", length = 1)
  private String backupFlag;

  /**
   * FES設備請款代碼
   */
  @Column(name = "APPLY_CODE", length = 3)
  private String applyCode;

  /**
   * 啟用MFES自動下載
   */
  @Column(name = "MFES_AUTO_DOWNLOAD_FLAG", length = 1)
  private String mfesAutoDownloadFlag;

  /**
   * KEY把數
   */
  @Column(name = "KEY_COUNTS")
  private Integer keyCounts;

  /**
   * SmartPay Flag
   */
  @Column(name = "SP_FLAG", length = 1)
  private String spFlag;

  /**
   * CUP Flag
   */
  @Column(name = "C_FLAG", length = 1)
  private String cFlag;

  /**
   * 設備購買廠商
   */
  @Column(name = "EQ_VENDOR_ID", length = 10)
  private String eqVendorId;

  /**
   * SAM 1 ID
   */
  @Column(name = "SAM_1_ID", length = 20)
  private String sam1Id;

  /**
   * SAM 2 ID
   */
  @Column(name = "SAM_2_ID", length = 20)
  private String sam2Id;

  /**
   * SAM 3 ID
   */
  @Column(name = "SAM_3_ID", length = 20)
  private String sam3Id;

  /**
   * SAM 4 ID
   */
  @Column(name = "SAM_4_ID", length = 20)
  private String sam4Id;

  /**
   * 停用原因
   */
  @Column(name = "CANCEL_REASON", length = 10)
  private String cancelReason;

  /**
   * DFS卡別
   */
  @Column(name = "DFS_FLAG", length = 1)
  private String dfsFlag;

  /**
   * EDC計價
   */
  @Column(name = "MAIN_FEE_EDC_TYPE", length = 1)
  private String mainFeeEdcType;
  
  
  @Column(name = "EDC_SAM_ID", length = 20)
  private String edcSamId;

  @Column(name = "EDC_UPDATE_DATE")
  private LocalDate edcUpdateDate;

  @Column(name = "EDC_UPDATE_USER", length = 50)
  private String edcUpdateUser;

  @Column(name = "VENDOR_SAM_ID", length = 20)
  private String vendorSamId;

  @Column(name = "VENDOR_UPDATE_DATE")
  private LocalDate vendorUpdateDate;

  @Column(name = "VENDOR_UPDATE_USER", length = 50)
  private String vendorUpdateUser;

  
  @PrePersist
  @PreUpdate
  protected void onUpdate() {
    lastUpdateDate = LocalDate.now();
  }

  public EmsMeqItem() {}

  public EmsMeqItemId getId() {
    return id;
  }

  public void setId(EmsMeqItemId id) {
    this.id = id;
  }

  public String getSerialNo() {
    return serialNo;
  }

  public void setSerialNo(String serialNo) {
    this.serialNo = serialNo;
  }

  public String getPurSeqNo() {
    return purSeqNo;
  }

  public void setPurSeqNo(String purSeqNo) {
    this.purSeqNo = purSeqNo;
  }

  public String getEqNo() {
    return eqNo;
  }

  public void setEqNo(String eqNo) {
    this.eqNo = eqNo;
  }

  public String getMchtNo() {
    return mchtNo;
  }

  public void setMchtNo(String mchtNo) {
    this.mchtNo = mchtNo;
  }

  public String getModelNo() {
    return modelNo;
  }

  public void setModelNo(String modelNo) {
    this.modelNo = modelNo;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getPurDate() {
    return purDate;
  }

  public void setPurDate(String purDate) {
    this.purDate = purDate;
  }

  public String getStockDate() {
    return stockDate;
  }

  public void setStockDate(String stockDate) {
    this.stockDate = stockDate;
  }

  public String getVendorId() {
    return vendorId;
  }

  public void setVendorId(String vendorId) {
    this.vendorId = vendorId;
  }

  public Integer getWarrMonth() {
    return warrMonth;
  }

  public void setWarrMonth(Integer warrMonth) {
    this.warrMonth = warrMonth;
  }

  public String getWarrStDate() {
    return warrStDate;
  }

  public void setWarrStDate(String warrStDate) {
    this.warrStDate = warrStDate;
  }

  public String getWarrEnDate() {
    return warrEnDate;
  }

  public void setWarrEnDate(String warrEnDate) {
    this.warrEnDate = warrEnDate;
  }

  public String getFeesStDate() {
    return feesStDate;
  }

  public void setFeesStDate(String feesStDate) {
    this.feesStDate = feesStDate;
  }

  public String getChargeFlag() {
    return chargeFlag;
  }

  public void setChargeFlag(String chargeFlag) {
    this.chargeFlag = chargeFlag;
  }

  public String getShareFlag() {
    return shareFlag;
  }

  public void setShareFlag(String shareFlag) {
    this.shareFlag = shareFlag;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
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

  public String getNcccWealthNo() {
    return ncccWealthNo;
  }

  public void setNcccWealthNo(String ncccWealthNo) {
    this.ncccWealthNo = ncccWealthNo;
  }

  public String getuFlag() {
    return uFlag;
  }

  public void setuFlag(String uFlag) {
    this.uFlag = uFlag;
  }

  public String getvFlag() {
    return vFlag;
  }

  public void setvFlag(String vFlag) {
    this.vFlag = vFlag;
  }

  public String getmFlag() {
    return mFlag;
  }

  public void setmFlag(String mFlag) {
    this.mFlag = mFlag;
  }

  public String getjFlag() {
    return jFlag;
  }

  public void setjFlag(String jFlag) {
    this.jFlag = jFlag;
  }

  public String getAeFlag() {
    return aeFlag;
  }

  public void setAeFlag(String aeFlag) {
    this.aeFlag = aeFlag;
  }

  public String getLoadKey() {
    return loadKey;
  }

  public void setLoadKey(String loadKey) {
    this.loadKey = loadKey;
  }

  public String getWaitUse() {
    return waitUse;
  }

  public void setWaitUse(String waitUse) {
    this.waitUse = waitUse;
  }

  public String getWaitDiscard() {
    return waitDiscard;
  }

  public void setWaitDiscard(String waitDiscard) {
    this.waitDiscard = waitDiscard;
  }

  public String getMainVendorId() {
    return mainVendorId;
  }

  public void setMainVendorId(String mainVendorId) {
    this.mainVendorId = mainVendorId;
  }

  public String getBackupFlag() {
    return backupFlag;
  }

  public void setBackupFlag(String backupFlag) {
    this.backupFlag = backupFlag;
  }

  public String getApplyCode() {
    return applyCode;
  }

  public void setApplyCode(String applyCode) {
    this.applyCode = applyCode;
  }

  public String getMfesAutoDownloadFlag() {
    return mfesAutoDownloadFlag;
  }

  public void setMfesAutoDownloadFlag(String mfesAutoDownloadFlag) {
    this.mfesAutoDownloadFlag = mfesAutoDownloadFlag;
  }

  public Integer getKeyCounts() {
    return keyCounts;
  }

  public void setKeyCounts(Integer keyCounts) {
    this.keyCounts = keyCounts;
  }

  public String getSpFlag() {
    return spFlag;
  }

  public void setSpFlag(String spFlag) {
    this.spFlag = spFlag;
  }

  public String getEdcSamId() {
    return edcSamId;
  }

  public void setEdcSamId(String edcSamId) {
    this.edcSamId = edcSamId;
  }

  public LocalDate getEdcUpdateDate() {
    return edcUpdateDate;
  }

  public void setEdcUpdateDate(LocalDate edcUpdateDate) {
    this.edcUpdateDate = edcUpdateDate;
  }

  public String getEdcUpdateUser() {
    return edcUpdateUser;
  }

  public void setEdcUpdateUser(String edcUpdateUser) {
    this.edcUpdateUser = edcUpdateUser;
  }

  public String getVendorSamId() {
    return vendorSamId;
  }

  public void setVendorSamId(String vendorSamId) {
    this.vendorSamId = vendorSamId;
  }

  public LocalDate getVendorUpdateDate() {
    return vendorUpdateDate;
  }

  public void setVendorUpdateDate(LocalDate vendorUpdateDate) {
    this.vendorUpdateDate = vendorUpdateDate;
  }

  public String getVendorUpdateUser() {
    return vendorUpdateUser;
  }

  public void setVendorUpdateUser(String vendorUpdateUser) {
    this.vendorUpdateUser = vendorUpdateUser;
  }

  public String getcFlag() {
    return cFlag;
  }

  public void setcFlag(String cFlag) {
    this.cFlag = cFlag;
  }

  public String getEqVendorId() {
    return eqVendorId;
  }

  public void setEqVendorId(String eqVendorId) {
    this.eqVendorId = eqVendorId;
  }

  public String getSam1Id() {
    return sam1Id;
  }

  public void setSam1Id(String sam1Id) {
    this.sam1Id = sam1Id;
  }

  public String getSam2Id() {
    return sam2Id;
  }

  public void setSam2Id(String sam2Id) {
    this.sam2Id = sam2Id;
  }

  public String getSam3Id() {
    return sam3Id;
  }

  public void setSam3Id(String sam3Id) {
    this.sam3Id = sam3Id;
  }

  public String getSam4Id() {
    return sam4Id;
  }

  public void setSam4Id(String sam4Id) {
    this.sam4Id = sam4Id;
  }

  public String getCancelReason() {
    return cancelReason;
  }

  public void setCancelReason(String cancelReason) {
    this.cancelReason = cancelReason;
  }

  public String getDfsFlag() {
    return dfsFlag;
  }

  public void setDfsFlag(String dfsFlag) {
    this.dfsFlag = dfsFlag;
  }

  public String getMainFeeEdcType() {
    return mainFeeEdcType;
  }

  public void setMainFeeEdcType(String mainFeeEdcType) {
    this.mainFeeEdcType = mainFeeEdcType;
  }

}
