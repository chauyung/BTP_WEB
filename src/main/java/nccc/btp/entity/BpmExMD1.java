package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.*;

@Entity
@IdClass(BpmExMD1.ConfigId.class)
@Table(name = "BPM_EX_M_D1")
public class BpmExMD1 implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "EX_NO", length = 10)
  private String exNo;

  @Id
  @Column(name = "EX_ITEM_NO", length = 10)
  private String exItemNo;

  @Column(name = "COPY_EX_ITEM_NO")
  private String copyExItemNo;

  @Column(name = "COPY_EX_NO", length = 20)
  private String copyExNo;

  @Column(name = "CERTIFICATE_DATE", length = 10)
  private String certificateDate;

  @Column(name = "CERTIFICATE_CODE", length = 20)
  private String certificateCode;

  @Column(name = "CERTIFICATE_TYPE", length = 10)
  private String certificateType;

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

  @Column(name = "ITEM_NAME", length = 20)
  private String itemName;

  @Column(name = "ITEM_CODE", length = 17)
  private String itemCode;

  @Column(name = "COST_CENTER", length = 10)
  private String costCenter;

  @Column(name = "DESCRIPTION", length = 50)
  private String description;

  @Column(name = "ITEM_TEXT", length = 50)
  private String itemText;

  @Column(name = "HAS_INCOME_TAX", length = 1)
  private String hasIncomeTax;

  @Column(name = "DEDUCTION", length = 1)
  private String deduction;

  @Column(name = "REMARK", length = 18)
  private String remark;

  @Column(name = "MULTI_SHARE", length = 20)
  private String multiShare;

  @Column(name = "ZERO_TAX_AMOUNT", precision = 13, scale = 2)
  private BigDecimal zeroTaxAmount;

  @Column(name = "DUTY_FREE_AMOUNT", precision = 13, scale = 2)
  private BigDecimal dutyFreeAmount;

  @Column(name = "CHT_CODE", length = 1)
  private String chtCode;

  @Column(name = "CARRIER_NUMBER", length = 30)
  private String carrierNumber;

  @Column(name = "NOTIFICATION_NUMBER", length = 30)
  private String notificationNumber;

  @Column(name = "CHT_COLLECT_AMOUNT", precision = 13, scale = 2)
  private BigDecimal chtCollectAmount;

  @Column(name = "CREATED_DATE")
  private LocalDate createdDate;

  @Column(name = "MODIFIED_DATE")
  private LocalDate modifiedDate;

  @Column(name = "CREATED_USER", length = 10)
  private String createdUser;

  @Column(name = "MODIFIED_USER", length = 10)
  private String modifiedUser;

  @Column(name = "YEAR", length = 4)
  private String year;

  @Column(name = "ACCOUNTING", length = 8)
  private String accounting;

  @Column(name = "OUCODE", length = 10)
  private String ouCode;

  public String getExNo() {
    return exNo;
  }

  public void setExNo(String exNo) {
    this.exNo = exNo;
  }

  public String getExItemNo() {
    return exItemNo;
  }

  public void setExItemNo(String exItemNo) {
    this.exItemNo = exItemNo;
  }

  public String getCopyExItemNo() {
    return copyExItemNo;
  }

  public void setCopyExItemNo(String copyExItemNo) {
    this.copyExItemNo = copyExItemNo;
  }

  public String getCopyExNo() {
    return copyExNo;
  }

  public void setCopyExNo(String copyExNo) {
    this.copyExNo = copyExNo;
  }

  public String getCertificateDate() {
    return certificateDate;
  }

  public void setCertificateDate(String certificateDate) {
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

  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public String getItemCode() {
    return itemCode;
  }

  public void setItemCode(String itemCode) {
    this.itemCode = itemCode;
  }

  public String getCostCenter() {
    return costCenter;
  }

  public void setCostCenter(String costCenter) {
    this.costCenter = costCenter;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getItemText() {
    return itemText;
  }

  public void setItemText(String itemText) {
    this.itemText = itemText;
  }

  public String getDeduction() {
    return deduction;
  }

  public void setDeduction(String deduction) {
    this.deduction = deduction;
  }

  public String getHasIncomeTax() {
    return hasIncomeTax;
  }

  public void setHasIncomeTax(String hasIncomeTax) {
    this.hasIncomeTax = hasIncomeTax;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getMultiShare() {
    return multiShare;
  }

  public void setMultiShare(String multiShare) {
    this.multiShare = multiShare;
  }

  public BigDecimal getZeroTaxAmount() {
    return zeroTaxAmount;
  }

  public void setZeroTaxAmount(BigDecimal zeroTaxAmount) {
    this.zeroTaxAmount = zeroTaxAmount;
  }

  public BigDecimal getDutyFreeAmount() {
    return dutyFreeAmount;
  }

  public void setDutyFreeAmount(BigDecimal dutyFreeAmount) {
    this.dutyFreeAmount = dutyFreeAmount;
  }

  public String getChtCode() {
    return chtCode;
  }

  public void setChtCode(String chtCode) {
    this.chtCode = chtCode;
  }

  public BigDecimal getChtCollectAmount() {
    return chtCollectAmount;
  }

  public void setChtCollectAmount(BigDecimal chtCollectAmount) {
    this.chtCollectAmount = chtCollectAmount;
  }

  public String getCarrierNumber() {
    return carrierNumber;
  }

  public void setCarrierNumber(String carrierNumber) {
    this.carrierNumber = carrierNumber;
  }

  public String getNotificationNumber() {
    return notificationNumber;
  }

  public void setNotificationNumber(String notificationNumber) {
    this.notificationNumber = notificationNumber;
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

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getAccounting() {
    return accounting;
  }

  public void setAccounting(String accounting) {
    this.accounting = accounting;
  }

  public String getOuCode() {
    return ouCode;
  }

  public void setOuCode(String ouCode) {
    this.ouCode = ouCode;
  }

  public static class ConfigId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String exNo;
    private String exItemNo;

    public ConfigId() {}

    public ConfigId(String exNo, String exItemNo) {
      this.exNo = exNo;
      this.exItemNo = exItemNo;
    }

    public String getExNo() {
      return exNo;
    }

    public void setExNo(String exNo) {
      this.exNo = exNo;
    }

    public String getExItemNo() {
      return exItemNo;
    }

    public void setExItemNo(String exItemNo) {
      this.exItemNo = exItemNo;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof BpmExMD1.ConfigId)) return false;
      BpmExMD1.ConfigId configId = (BpmExMD1.ConfigId) o;
      return Objects.equals(exNo, configId.exNo) &&
              Objects.equals(exItemNo, configId.exItemNo);
    }

    @Override
    public int hashCode() {
      return Objects.hash(exNo, exItemNo);
    }
  }
}
