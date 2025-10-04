package nccc.btp.dto;

import java.math.BigDecimal;

/**
 * 驗收單(一般驗收)
 */
public class AcceptanceGeneralAcceptance {

  // 項次
  private String itemNo;

  // 作業項目
  private String operationItem;

  // 耐用年限
  private String usefulLifeYears;

  // 可驗收數量
  private String acceptableQty;

  // 在途數量
  private String inTransitQty;

  // 數量/金額/百分比
  private BigDecimal qty;

  // 指定結案/最終驗收
  private String designatedCompletion;

  // 保留資產
  private String retainedAssets;

  // 複數保管人
  private String multipleCustodian;

  // 保管人
  private String custodian;

  // 置放地點
  private String location;

  // 驗收程序
  private String acceptanceProcedure;

  // 驗收結果
  private String acceptanceResult;

  // 交貨日期/驗收通知日期
  private String deliveryDate;

  // 展延驗收日期
  private String extensionDate;

  // 展延核准文號
  private String extensiondocumentNo;

  // 備註
  private String remark;

  // 固定資產編號
  private String fixedAssetNo;

  // 憑證日期
  private String certificateDate;

  // 憑證號碼
  private String certificateNo;

  // 憑證種類
  private String certificateType;

  // 賣方統一編號
  private String businessRegistrationNumber;

  // 待驗收金額
  private String acceptedAmount;

  // 申請金額
  private BigDecimal price;

  // 未稅金額
  private BigDecimal total;

  // 稅額
  private BigDecimal tax;

  // 免稅
  private String zeroTax;

  // 免稅
  private String taxFree;

  // 預算部門
  private String oucode;

  // 品號
  private String itemCode;

  // 品名
  private String itemName;

  // 指定付款日期
  private String specifypaymentDate;

  // 指定過帳日期
  private String specifypostingDate;

  // 項目內文
  private String itemContent;

  // 扣抵
  private String deduction;

  // 備忘
  private String memo;

  public String getItemNo() {
    return itemNo;
  }

  public void setItemNo(String itemNo) {
    this.itemNo = itemNo;
  }

  public String getOperationItem() {
    return operationItem;
  }

  public void setOperationItem(String operationItem) {
    this.operationItem = operationItem;
  }

  public String getUsefulLifeYears() {
    return usefulLifeYears;
  }

  public void setUsefulLifeYears(String usefulLifeYears) {
    this.usefulLifeYears = usefulLifeYears;
  }

  public String getAcceptableQty() {
    return acceptableQty;
  }

  public void setAcceptableQty(String acceptableQty) {
    this.acceptableQty = acceptableQty;
  }

  public String getInTransitQty() {
    return inTransitQty;
  }

  public void setInTransitQty(String inTransitQty) {
    this.inTransitQty = inTransitQty;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public String getDesignatedCompletion() {
    return designatedCompletion;
  }

  public void setDesignatedCompletion(String designatedCompletion) {
    this.designatedCompletion = designatedCompletion;
  }

  public String getRetainedAssets() {
    return retainedAssets;
  }

  public void setRetainedAssets(String retainedAssets) {
    this.retainedAssets = retainedAssets;
  }

  public String getMultipleCustodian() {
    return multipleCustodian;
  }

  public void setMultipleCustodian(String multipleCustodian) {
    this.multipleCustodian = multipleCustodian;
  }

  public String getCustodian() {
    return custodian;
  }

  public void setCustodian(String custodian) {
    this.custodian = custodian;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
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

  public String getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(String deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  public String getExtensionDate() {
    return extensionDate;
  }

  public void setExtensionDate(String extensionDate) {
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

  public String getFixedAssetNo() {
    return fixedAssetNo;
  }

  public void setFixedAssetNo(String fixedAssetNo) {
    this.fixedAssetNo = fixedAssetNo;
  }

  public String getCertificateDate() {
    return certificateDate;
  }

  public void setCertificateDate(String certificateDate) {
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

  public String getAcceptedAmount() {
    return acceptedAmount;
  }

  public void setAcceptedAmount(String acceptedAmount) {
    this.acceptedAmount = acceptedAmount;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  public String getZeroTax() {
    return zeroTax;
  }

  public void setZeroTax(String zeroTax) {
    this.zeroTax = zeroTax;
  }

  public String getTaxFree() {
    return taxFree;
  }

  public void setTaxFree(String taxFree) {
    this.taxFree = taxFree;
  }

  public String getOucode() {
    return oucode;
  }

  public void setOucode(String oucode) {
    this.oucode = oucode;
  }

  public String getItemCode() {
    return itemCode;
  }

  public void setItemCode(String itemCode) {
    this.itemCode = itemCode;
  }

  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public String getSpecifypaymentDate() {
    return specifypaymentDate;
  }

  public void setSpecifypaymentDate(String specifypaymentDate) {
    this.specifypaymentDate = specifypaymentDate;
  }

  public String getSpecifypostingDate() {
    return specifypostingDate;
  }

  public void setSpecifypostingDate(String specifypostingDate) {
    this.specifypostingDate = specifypostingDate;
  }

  public String getItemContent() {
    return itemContent;
  }

  public void setItemContent(String itemContent) {
    this.itemContent = itemContent;
  }

  public String getDeduction() {
    return deduction;
  }

  public void setDeduction(String deduction) {
    this.deduction = deduction;
  }

  public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }
}
