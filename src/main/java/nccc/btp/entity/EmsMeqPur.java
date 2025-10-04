package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "EMS_MEQ_PUR")
@NamedQuery(name = "EmsMeqPur.findAll", query = "SELECT e FROM EmsMeqPur e")
public class EmsMeqPur implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "PUR_SEQ_NO", length = 10, nullable = false)
  private String purSeqNo;

  @Column(name = "PUR_DATE", length = 8)
  private String purDate;

  @Column(name = "VENDOR_ID", length = 10)
  private String vendorId;

  @Column(name = "EQ_TYPE", length = 10)
  private String eqType;

  @Column(name = "MODEL_NO", length = 10)
  private String modelNo;

  @Column(name = "WARR_MONTH")
  private Integer warrMonth;

  @Column(name = "WARR_ST_DATE", length = 8)
  private String warrStDate;

  @Column(name = "WARR_EN_DATE", length = 8)
  private String warrEnDate;

  @Column(name = "SHARE_FLAG", length = 1)
  private String shareFlag;

  @Column(name = "WEALTH_ST_NO", length = 15)
  private String wealthStNo;

  @Column(name = "WEALTH_EN_NO", length = 15)
  private String wealthEnNo;

  @Column(name = "REQ_DOC_NO", length = 30)
  private String reqDocNo;

  @Column(name = "OFFICE_DOC_NO", length = 30)
  private String officeDocNo;

  @Column(name = "PUR_QTY")
  private Integer purQty;

  @Column(name = "PUR_AMT", precision = 10)
  private BigDecimal purAmt;

  @Column(name = "FEES_ST_DATE", length = 8)
  private String feesStDate;

  @Column(name = "REMARK", length = 50)
  private String remark;

  @Column(name = "CREATE_USER", length = 50)
  private String createUser;

  @Column(name = "CREATE_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate createDate;

  @Column(name = "LAST_UPDATE_USER", length = 530)
  private String lastUpdateUser;

  @Column(name = "LAST_UPDATE_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate lastUpdateDate;

  @Column(name = "NCCC_WEALTH_NO", length = 20)
  private String ncccWealthNo;

  @Column(name = "MAIN_FEE_EDC_TYPE", length = 1)
  private String mainFeeEdcType;

  @PrePersist
  @PreUpdate
  protected void onUpdate() {
    lastUpdateDate = LocalDate.now();
  }

  public EmsMeqPur() {}

  public String getPurSeqNo() {
    return purSeqNo;
  }

  public void setPurSeqNo(String purSeqNo) {
    this.purSeqNo = purSeqNo;
  }

  public String getPurDate() {
    return purDate;
  }

  public void setPurDate(String purDate) {
    this.purDate = purDate;
  }

  public String getVendorId() {
    return vendorId;
  }

  public void setVendorId(String vendorId) {
    this.vendorId = vendorId;
  }

  public String getEqType() {
    return eqType;
  }

  public void setEqType(String eqType) {
    this.eqType = eqType;
  }

  public String getModelNo() {
    return modelNo;
  }

  public void setModelNo(String modelNo) {
    this.modelNo = modelNo;
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

  public String getShareFlag() {
    return shareFlag;
  }

  public void setShareFlag(String shareFlag) {
    this.shareFlag = shareFlag;
  }

  public String getWealthStNo() {
    return wealthStNo;
  }

  public void setWealthStNo(String wealthStNo) {
    this.wealthStNo = wealthStNo;
  }

  public String getWealthEnNo() {
    return wealthEnNo;
  }

  public void setWealthEnNo(String wealthEnNo) {
    this.wealthEnNo = wealthEnNo;
  }

  public String getReqDocNo() {
    return reqDocNo;
  }

  public void setReqDocNo(String reqDocNo) {
    this.reqDocNo = reqDocNo;
  }

  public String getOfficeDocNo() {
    return officeDocNo;
  }

  public void setOfficeDocNo(String officeDocNo) {
    this.officeDocNo = officeDocNo;
  }

  public Integer getPurQty() {
    return purQty;
  }

  public void setPurQty(Integer purQty) {
    this.purQty = purQty;
  }

  public BigDecimal getPurAmt() {
    return purAmt;
  }

  public void setPurAmt(BigDecimal purAmt) {
    this.purAmt = purAmt;
  }

  public String getFeesStDate() {
    return feesStDate;
  }

  public void setFeesStDate(String feesStDate) {
    this.feesStDate = feesStDate;
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

  public String getMainFeeEdcType() {
    return mainFeeEdcType;
  }

  public void setMainFeeEdcType(String mainFeeEdcType) {
    this.mainFeeEdcType = mainFeeEdcType;
  }

}
