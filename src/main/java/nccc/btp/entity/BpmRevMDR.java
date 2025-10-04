package nccc.btp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 驗收單紀錄
 */
@Entity
@IdClass(BpmRevMDRId.class)
@Table(name = "BPM_REV_M_D_R")
public class BpmRevMDR {

  // 驗收單單號
  @Id
  @Column(name = "REV_NO", length = 12)
  private String revNo;

  // 採購單項次
  @Id
  @Column(name = "PO_ITEM_NO", length = 5)
  private String poItemNo;

  // 驗收單項次
  @Id
  @Column(name = "REV_ITEM_NO", length = 5)
  private String revItemNo;

  // 驗收單項次
  @Id
  @Column(name = "REV_SEQ_NO", length = 5)
  private String revSeqNo;

  // 處理識別鍵
  @Column(name = "HANDLE_IDENTIFICATION_KEY", length = 36)
  private String handleIdentificationKey;

  // 數量 / 金額
  @Column(name = "QTY", precision = 13, scale = 3)
  private BigDecimal qty;

  // 餘料不驗數量/金額
  @Column(name = "NOT_ACCEPTED_QTY", precision = 13, scale = 3)
  private BigDecimal notAcceptedQty;

  // 餘料不驗
  @Column(name = "NOT_ACCEPTED")
  private Boolean notAccepted;

  // 保留資產
  @Column(name = "RETAINED_ASSETS")
  private Boolean retainedAssets;

  // 驗收程序
  @Column(name = "ACCEPTANCE_PROCEDURE", length = 50)
  private String acceptanceProcedure;

  // 驗收結果
  @Column(name = "ACCEPTANCE_RESULT", length = 50)
  private String acceptanceResult;

  // 交貨日期/驗收通知日期
  @Column(name = "DELIVERY_DATE")
  private LocalDate deliveryDate;

  // 展延驗收日期
  @Column(name = "EXTENSION_DATE")
  private LocalDate extensionDate;

  // 展延核准文號
  @Column(name = "EXTENSIONDOCUMENT_NO", length = 10)
  private String extensiondocumentNo;

  // 備註
  @Column(name = "REMARK", length = 50)
  private String remark;

  // 憑證日期
  @Column(name = "CERTIFICATE_DATE")
  private LocalDate certificateDate;

  // 憑證號碼
  @Column(name = "CERTIFICATE_NO", length = 10)
  private String certificateNo;

  // 憑證種類
  @Column(name = "CERTIFICATE_TYPE", length = 10)
  private String certificateType;

  // 賣方統一編號
  @Column(name = "BUSINESS_REGISTRATION_NUMBER", length = 8)
  private String businessRegistrationNumber;

  // 待驗收金額
  @Column(name = "ACCEPTED_AMOUNT", precision = 13, scale = 3)
  private BigDecimal acceptedAmount;

  // 申請金額
  @Column(name = "APPLICATION_AMOUNT", precision = 13, scale = 3)
  private BigDecimal applicationAmount;

  // 稅率
  @Column(name = "TAX_RATE", precision = 13, scale = 3)
  private BigDecimal taxRate;

  // 未稅金額
  @Column(name = "EXCLUDING_TAX_AMOUNT", precision = 13, scale = 3)
  private BigDecimal excludingTaxAmount;

  // 稅額
  @Column(name = "TAX", precision = 13, scale = 3)
  private BigDecimal tax;

  // 零稅
  @Column(name = "ZERO_TAX", precision = 13, scale = 3)
  private BigDecimal zeroTax;

  // 免稅
  @Column(name = "TAX_FREE", precision = 13, scale = 3)
  private BigDecimal taxFree;

  // 預算部門
  @Column(name = "BUDGET_DEPARTMENT", length = 10)
  private String budgetDepartment;

  // 指定付款日期
  @Column(name = "SPECIFYPAYMENT_DATE")
  private LocalDate specifypaymentDate;

  // 指定過帳日期
  @Column(name = "SPECIFYPOSTING_DATE")
  private LocalDate specifypostingDate;

  // 項目內文
  @Column(name = "ITEM_CONTENT", length = 50)
  private String item_content;

  // 指定結案/最終驗收
  @Column(name = "DESIGNATED_COMPLETION")
  private Boolean designatedCompletion;

  // 扣抵
  @Column(name = "DEDUCTION")
  private Boolean deduction;

  // 備忘
  @Column(name = "MEMO", length = 50)
  private String memo;

  // 是否代扣繳
  @Column(name = "WITHHOLDING")
  private Boolean withholding;

  // 計量單位
  @Column(name = "QTY_UNIT", length = 3)
  private String qtyUnit;

  public String getRevNo() {
    return revNo;
  }

  public void setRevNo(String revNo) {
    this.revNo = revNo;
  }

  public String getPoItemNo() {
    return poItemNo;
  }

  public void setPoItemNo(String poItemNo) {
    this.poItemNo = poItemNo;
  }

  public String getRevItemNo() {
    return revItemNo;
  }

  public void setRevItemNo(String revItemNo) {
    this.revItemNo = revItemNo;
  }

  public String getRevSeqNo() {
    return revSeqNo;
  }

  public void setRevSeqNo(String revSeqNo) {
    this.revSeqNo = revSeqNo;
  }

  public String getHandleIdentificationKey() {
    return handleIdentificationKey;
  }

  public void setHandleIdentificationKey(String handleIdentificationKey) {
    this.handleIdentificationKey = handleIdentificationKey;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public BigDecimal getNotAcceptedQty() {
    return notAcceptedQty;
  }

  public void setNotAcceptedQty(BigDecimal notAcceptedQty) {
    this.notAcceptedQty = notAcceptedQty;
  }

  public Boolean getNotAccepted() {
    return notAccepted;
  }

  public void setNotAccepted(Boolean notAccepted) {
    this.notAccepted = notAccepted;
  }

  public Boolean getRetainedAssets() {
    return retainedAssets;
  }

  public void setRetainedAssets(Boolean retainedAssets) {
    this.retainedAssets = retainedAssets;
  }

  public String getAcceptanceProcedure() {
    return acceptanceProcedure;
  }

  public void setAcceptanceProcedure(String acceptanceProcedure) {
    this.acceptanceProcedure = acceptanceProcedure;
  }

  public String getAcceptanceResult() {
    return acceptanceResult;
  }

  public void setAcceptanceResult(String acceptanceResult) {
    this.acceptanceResult = acceptanceResult;
  }

  public LocalDate getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(LocalDate deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  public LocalDate getExtensionDate() {
    return extensionDate;
  }

  public void setExtensionDate(LocalDate extensionDate) {
    this.extensionDate = extensionDate;
  }

  public String getExtensiondocumentNo() {
    return extensiondocumentNo;
  }

  public void setExtensiondocumentNo(String extensiondocumentNo) {
    this.extensiondocumentNo = extensiondocumentNo;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public LocalDate getCertificateDate() {
    return certificateDate;
  }

  public void setCertificateDate(LocalDate certificateDate) {
    this.certificateDate = certificateDate;
  }

  public String getCertificateNo() {
    return certificateNo;
  }

  public void setCertificateNo(String certificateNo) {
    this.certificateNo = certificateNo;
  }

  public String getCertificateType() {
    return certificateType;
  }

  public void setCertificateType(String certificateType) {
    this.certificateType = certificateType;
  }

  public String getBusinessRegistrationNumber() {
    return businessRegistrationNumber;
  }

  public void setBusinessRegistrationNumber(String businessRegistrationNumber) {
    this.businessRegistrationNumber = businessRegistrationNumber;
  }

  public BigDecimal getAcceptedAmount() {
    return acceptedAmount;
  }

  public void setAcceptedAmount(BigDecimal acceptedAmount) {
    this.acceptedAmount = acceptedAmount;
  }

  public BigDecimal getApplicationAmount() {
    return applicationAmount;
  }

  public void setApplicationAmount(BigDecimal applicationAmount) {
    this.applicationAmount = applicationAmount;
  }

  public BigDecimal getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(BigDecimal taxRate) {
    this.taxRate = taxRate;
  }

  public BigDecimal getExcludingTaxAmount() {
    return excludingTaxAmount;
  }

  public void setExcludingTaxAmount(BigDecimal excludingTaxAmount) {
    this.excludingTaxAmount = excludingTaxAmount;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  public BigDecimal getZeroTax() {
    return zeroTax;
  }

  public void setZeroTax(BigDecimal zeroTax) {
    this.zeroTax = zeroTax;
  }

  public BigDecimal getTaxFree() {
    return taxFree;
  }

  public void setTaxFree(BigDecimal taxFree) {
    this.taxFree = taxFree;
  }

  public String getBudgetDepartment() {
    return budgetDepartment;
  }

  public void setBudgetDepartment(String budgetDepartment) {
    this.budgetDepartment = budgetDepartment;
  }

  public LocalDate getSpecifypaymentDate() {
    return specifypaymentDate;
  }

  public void setSpecifypaymentDate(LocalDate specifypaymentDate) {
    this.specifypaymentDate = specifypaymentDate;
  }

  public LocalDate getSpecifypostingDate() {
    return specifypostingDate;
  }

  public void setSpecifypostingDate(LocalDate specifypostingDate) {
    this.specifypostingDate = specifypostingDate;
  }

  public String getItem_content() {
    return item_content;
  }

  public void setItem_content(String item_content) {
    this.item_content = item_content;
  }

  public Boolean getDesignatedCompletion() {
    return designatedCompletion;
  }

  public void setDesignatedCompletion(Boolean designatedCompletion) {
    this.designatedCompletion = designatedCompletion;
  }

  public Boolean getDeduction() {
    return deduction;
  }

  public void setDeduction(Boolean deduction) {
    this.deduction = deduction;
  }

  public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  public Boolean getWithholding() {
    return withholding;
  }

  public void setWithholding(Boolean withholding) {
    this.withholding = withholding;
  }

  public String getQtyUnit() {
    return qtyUnit;
  }

  public void setQtyUnit(String qtyUnit) {
    this.qtyUnit = qtyUnit;
  }

}
