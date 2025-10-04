package nccc.btp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(BpmRevMDId.class)
@Table(name = "BPM_REV_M_D")
public class BpmRevMD {

  /*
   * 驗收單單號
   */
  @Id
  @Column(name = "REV_NO", length = 12)
  private String revNo;

  /*
   * 項次
   */
  @Id
  @Column(name = "PO_ITEM_NO", length = 5)
  private String poItemNo;

  /*
   * 驗收單項次
   */
  @Id
  @Column(name = "REV_ITEM_NO", length = 5)
  private String revItemNo;

  /*
   * 品號
   */
  @Column(name = "ITEM_CODE", length = 10)
  private String itemCode;

  /*
   * 品名
   */
  @Column(name = "ITEM_NAME", length = 50)
  private String itemName;

  /*
   * 說明
   */
  @Column(name = "REMARK", length = 256)
  private String remark;

  /*
   * 需用日期
   */
  @Column(name = "DEMAND_DATE")
  private LocalDate demandDate;

  /*
   * 交貨地點
   */
  @Column(name = "LOCATION", length = 50)
  private String location;

  /*
   * 採購數量（金額）
   */
  @Column(name = "QTY", precision = 13, scale = 3)
  private BigDecimal qty;

  /*
   * 申請金額
   */
  @Column(name = "PRICE", precision = 13, scale = 3)
  private BigDecimal price;

  /*
   * 稅額
   */
  @Column(name = "TAX", precision = 13, scale = 3)
  private BigDecimal tax;

  /*
   * 未稅金額
   */
  @Column(name = "TOTAL", precision = 13, scale = 2)
  private BigDecimal total;

  /*
   * 是否為固定資產
   */
  @Column(name = "IS_FIXED_ASSET")
  private Boolean isFixedAsset;

  /*
   * 是否為端末機
   */
  @Column(name = "IS_TERMINAL")
  private Boolean isTerminal;

  /*
   * 固定資產編號
   */
  @Column(name = "FIXED_ASSET_NO", length = 50)
  private String fixedAssetNo;

  /*
   * 耐用年限
   */
  @Column(name = "USEFUL_LIFE_YEARS", length = 10)
  public String usefulLifeYears;

  /*
   * 建立日期
   */
  @Column(name = "CREATED_DATE")
  private LocalDate createdDate;

  /*
   * 修改日期
   */
  @Column(name = "MODIFIED_DATE")
  private LocalDate modifiedDate;

  /*
   * 建立者
   */
  @Column(name = "CREATED_USER", length = 50)
  private String createdUser;

  /*
   * 修改者
   */
  @Column(name = "MODIFIED_USER", length = 50)
  private String modifiedUser;

  /*
   * 資產保留
   */
  @Column(name = "ASSET_RETAINED", length = 1)
  private String assetRetained;

  /*
   * 分攤方式 原比例/指定 1:指定2:原比例"
   */
  @Column(name = "ALLOCATION_METHOD", length = 1)
  private String allocationMethod;

  /*
   * 預算年度
   */
  @Column(name = "YEAR", length = 4)
  private String year;

  /*
   * 預算部門
   */
  @Column(name = "OUCODE", length = 20)
  private String oucode;

  /*
   * 預算項目代號
   */
  @Column(name = "ACCOUNTING", length = 8)
  private String accounting;

  /*
   * 是否權責分攤 Y:是N:否
   */
  @Column(name = "IS_RESPONSIBILITY", length = 1)
  private String isResponsibility;

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

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public LocalDate getDemandDate() {
    return demandDate;
  }

  public void setDemandDate(LocalDate demandDate) {
    this.demandDate = demandDate;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public Boolean getIsFixedAsset() {
    return isFixedAsset;
  }

  public void setIsFixedAsset(Boolean isFixedAsset) {
    this.isFixedAsset = isFixedAsset;
  }

  public Boolean getIsTerminal() {
    return isTerminal;
  }

  public void setIsTerminal(Boolean isTerminal) {
    this.isTerminal = isTerminal;
  }

  public String getFixedAssetNo() {
    return fixedAssetNo;
  }

  public void setFixedAssetNo(String fixedAssetNo) {
    this.fixedAssetNo = fixedAssetNo;
  }

  public String getUsefulLifeYears() {
    return usefulLifeYears;
  }

  public void setUsefulLifeYears(String usefulLifeYears) {
    this.usefulLifeYears = usefulLifeYears;
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

  public String getAssetRetained() {
    return assetRetained;
  }

  public void setAssetRetained(String assetRetained) {
    this.assetRetained = assetRetained;
  }

  public String getAllocationMethod() {
    return allocationMethod;
  }

  public void setAllocationMethod(String allocationMethod) {
    this.allocationMethod = allocationMethod;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getOucode() {
    return oucode;
  }

  public void setOucode(String oucode) {
    this.oucode = oucode;
  }

  public String getAccounting() {
    return accounting;
  }

  public void setAccounting(String accounting) {
    this.accounting = accounting;
  }

  public String getIsResponsibility() {
    return isResponsibility;
  }

  public void setIsResponsibility(String isResponsibility) {
    this.isResponsibility = isResponsibility;
  }

}

