package nccc.btp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@IdClass(BpmBtMD1Id.class)
@Table(name = "BPM_BT_M_D1")
public class BpmBtMD1 {

  @Id
  @Column(name = "BT_NO", length = 12)
  private String btNo;

  @Id
  @Column(name = "BT_ITEM_NO", length = 5)
  private String btItemNo;

  @Column(name = "ACCOUNTING", length = 8)
  private String accounting;
  
  @Column(name = "CERTIFICATE_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate certificateDate;

  @Column(name = "CERTIFICATE_CODE", length = 20)
  private String certificateCode;

  @Column(name = "CERTIFICATE_TYPE", length = 10)
  private String certificateType;

  @Column(name = "MWSKZ", length = 2)
  private String mwskz;

  @Column(name = "OUCODE", length = 20)
  private String ouCode;
  
  @Column(name = "UNIFORM_NUM", length = 10)
  private String uniformNum;

  @Column(name = "APPLY_AMOUNT", precision = 13, scale = 2)
  private BigDecimal applyAmount;

  @Column(name = "UNTAX_AMOUNT", precision = 13, scale = 2)
  private BigDecimal untaxAmount;

  @Column(name = "TAX", precision = 13, scale = 2)
  private BigDecimal tax;

  @Column(name = "TAX_CODE", length = 2)
  private String taxCode;

  @Column(name = "TAX_RATE", length = 10)
  private String taxRate;

  @Column(name = "COST_CENTER", length = 20)
  private String costCenter;

  @Column(name = "TRAFFIC", length = 50)
  private String traffic;

  @Column(name = "LOCATION", length = 50)
  private String location;

  @Column(name = "LOCATION_END", length = 50)
  private String locationEnd;

  @Column(name = "TRAVEL_MEMBER", length = 50)
  private String travelMember;

  @Column(name = "DEDUCTION", length = 10)
  private String deduction;

  @Column(name = "REASON", length = 256)
  private String reason;

  @Column(name = "SPECIAL_REASON", length = 50)
  private String specialReason;

  @Column(name = "REMARK", length = 10)
  private String remark;

  @Column(name = "SAP_PRICE", precision = 13, scale = 2)
  private BigDecimal sapPrice;

  @Column(name = "YEAR")
  private Integer year;
  
  @Column(name = "ZFORM_CODE", precision = 2, scale = 0)
  private BigDecimal zformCode;

  @Column(name = "CUS_TYPE", length = 1)
  private String cusType;

  @Column(name = "CREATED_DATE")
  private LocalDate createdDate;

  @Column(name = "MODIFIED_DATE")
  private LocalDate modifiedDate;

  @Column(name = "CREATED_USER", length = 50)
  private String createdUser;

  @Column(name = "MODIFIED_USER", length = 50)
  private String modifiedUser;

  public String getAccounting() {
	return accounting;
}

  public void setAccounting(String accounting) {
	this.accounting = accounting;
  }

  public String getOuCode() {
	return ouCode;
  }

  public void setOucode(String ouCode) {
	this.ouCode = ouCode;
  }

  public Integer  getYear() {
	return year;
  }

  public void setYear(Integer  year) {
	this.year = year;
  }

  public String getBtNo() {
    return btNo;
  }

  public void setBtNo(String btNo) {
    this.btNo = btNo;
  }

  
  public String getBtItemNo() {
    return btItemNo;
  }

  public void setBtItemNo(String btItemNo) {
    this.btItemNo = btItemNo;
  }

  public LocalDate getCertificateDate() {
    return certificateDate;
  }

  public void setCertificateDate(LocalDate certificateDate) {
    this.certificateDate = certificateDate;
  }

  public String getCertificateCode() {
    return certificateCode;
  }

  public void setCertificateCode(String certificateCode) {
    this.certificateCode = certificateCode;
  }

  public String getCertificateType() {
    return certificateType;
  }

  public void setCertificateType(String certificateType) {
    this.certificateType = certificateType;
  }

  public String getMwskz() {
    return mwskz;
  }

  public void setMwskz(String mwskz) {
    this.mwskz = mwskz;
  }

  public String getUniformNum() {
    return uniformNum;
  }

  public void setUniformNum(String uniformNum) {
    this.uniformNum = uniformNum;
  }

  public BigDecimal getApplyAmount() {
    return applyAmount;
  }

  public void setApplyAmount(BigDecimal applyAmount) {
    this.applyAmount = applyAmount;
  }

  public BigDecimal getUntaxAmount() {
    return untaxAmount;
  }

  public void setUntaxAmount(BigDecimal untaxAmount) {
    this.untaxAmount = untaxAmount;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  public String getTaxCode() {
    return taxCode;
  }

  public void setTaxCode(String taxCode) {
    this.taxCode = taxCode;
  }

  public String getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(String taxRate) {
    this.taxRate = taxRate;
  }

  public String getCostCenter() {
    return costCenter;
  }

  public void setCostCenter(String costCenter) {
    this.costCenter = costCenter;
  }

  public String getTraffic() {
    return traffic;
  }

  public void setTraffic(String traffic) {
    this.traffic = traffic;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getLocationEnd() {
    return locationEnd;
  }

  public void setLocationEnd(String locationEnd) {
    this.locationEnd = locationEnd;
  }

  public String getTravelMember() {
    return travelMember;
  }

  public void setTravelMember(String travelMember) {
    this.travelMember = travelMember;
  }

  public String getDeduction() {
    return deduction;
  }

  public void setDeduction(String deduction) {
    this.deduction = deduction;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getSpecialReason() {
    return specialReason;
  }

  public void setSpecialReason(String specialReason) {
    this.specialReason = specialReason;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public BigDecimal getSapPrice() {
    return sapPrice;
  }

  public void setSapPrice(BigDecimal sapPrice) {
    this.sapPrice = sapPrice;
  }

  public BigDecimal getZformCode() {
    return zformCode;
  }

  public void setZformCode(BigDecimal zformCode) {
    this.zformCode = zformCode;
  }

  public String getCusType() {
    return cusType;
  }

  public void setCusType(String cusType) {
    this.cusType = cusType;
  }

  public LocalDate getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDate createdDate) {
    this.createdDate = createdDate;
  }

  public LocalDate getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(LocalDate modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  public String getCreatedUser() {
    return createdUser;
  }

  public void setCreatedUser(String createdUser) {
    this.createdUser = createdUser;
  }

  public String getModifiedUser() {
    return modifiedUser;
  }

  public void setModifiedUser(String modifiedUser) {
    this.modifiedUser = modifiedUser;
  }

}
