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

@Entity
@Table(name = "EMS_MEQ_ITEM")
@NamedQuery(name = "EmsMeqItem.findAll", query = "SELECT e FROM EmsMeqItem e")
public class EmsMeqItem implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private EmsMeqItemId id;

  @Column(name = "SERIAL_NO", length = 30)
  private String serialNo;

  @Column(name = "PUR_SEQ_NO", length = 10)
  private String purSeqNo;

  @Column(name = "EQ_NO", length = 15)
  private String eqNo;

  @Column(name = "MCHT_NO", length = 15)
  private String mchtNo;

  @Column(name = "MODEL_NO", length = 10)
  private String modelNo;

  @Column(name = "STATUS", length = 10)
  private String status;

  @Column(name = "LOCATION", length = 10)
  private String location;

  @Column(name = "PUR_DATE", length = 8)
  private String purDate;

  @Column(name = "STOCK_DATE", length = 8)
  private String stockDate;

  @Column(name = "VENDOR_ID", length = 10)
  private String vendorId;

  @Column(name = "WARR_MONTH")
  private Integer warrMonth;

  @Column(name = "WARR_ST_DATE", length = 8)
  private String warrStDate;

  @Column(name = "WARR_EN_DATE", length = 8)
  private String warrEnDate;

  @Column(name = "FEES_ST_DATE", length = 8)
  private String feesStDate;

  @Column(name = "CHARGE_FLAG", length = 1)
  private String chargeFlag;

  @Column(name = "SHARE_FLAG", length = 1)
  private String shareFlag;

  @Column(name = "START_DATE", length = 8)
  private String startDate;

  @Column(name = "END_DATE", length = 8)
  private String endDate;

  @Column(name = "REMARK", length = 500)
  private String remark;

  @Column(name = "CREATE_USER", length = 50)
  private String createUser;

  @Column(name = "CREATE_DATE")
  private LocalDate createDate;

  @Column(name = "LAST_UPDATE_USER", length = 50, nullable = false)
  private String lastUpdateUser;

  @Column(name = "LAST_UPDATE_DATE", nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate lastUpdateDate;

  @Column(name = "NCCC_WEALTH_NO", length = 20)
  private String ncccWealthNo;

  @Column(name = "U_FLAG", length = 1)
  private String uFlag;

  @Column(name = "V_FLAG", length = 1)
  private String vFlag;

  @Column(name = "M_FLAG", length = 1)
  private String mFlag;

  @Column(name = "J_FLAG", length = 1)
  private String jFlag;

  @Column(name = "AE_FLAG", length = 1)
  private String aeFlag;

  @Column(name = "WAIT_DISCARD", length = 1)
  private String waitDiscard;

  @Column(name = "LOAD_KEY", length = 1)
  private String loadKey;

  @Column(name = "WAIT_USE", length = 1)
  private String waitUse;

  @Column(name = "MAIN_VENDOR_ID", length = 10)
  private String mainVendorId;

  @Column(name = "BACKUP_FLAG", length = 1)
  private String backupFlag;

  @Column(name = "APPLY_CODE", length = 3)
  private String applyCode;

  @Column(name = "MFES_AUTO_DOWNLOAD_FLAG", length = 1)
  private String mfesAutoDownloadFlag;

  @Column(name = "KEY_COUNTS")
  private Integer keyCounts;

  @Column(name = "SP_FLAG", length = 1)
  private String spFlag;

  @Column(name = "C_FLAG", length = 1)
  private String cFlag;

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

  @Column(name = "EQ_VENDOR_ID", length = 10)
  private String eqVendorId;

  @Column(name = "SAM_1_ID", length = 20)
  private String sam1Id;

  @Column(name = "SAM_2_ID", length = 20)
  private String sam2Id;

  @Column(name = "SAM_3_ID", length = 20)
  private String sam3Id;

  @Column(name = "SAM_4_ID", length = 20)
  private String sam4Id;

  @Column(name = "CANCEL_REASON", length = 10)
  private String cancelReason;

  @Column(name = "DFS_FLAG", length = 1)
  private String dfsFlag;

  @Column(name = "MAIN_FEE_EDC_TYPE", length = 1)
  private String mainFeeEdcType;

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
