package nccc.btp.dto;

import java.util.List;


public class PurchaseDetailDto {
  
  
  private String prItemNo;
  private String itemCode;
  private String detailRemark;
  private String demandDateByFlow;
  private String demandDate;
  private String location;
  private String qty;
  private String unitOfMeasurement;
  private String estimatedAmount;
  private String total;
  private String tax;
  private boolean autoTax;
  private String budgetDepartment;
  private List<VendorQuoteDto> vendorQuotes;

  public String getPrItemNo() {
    return prItemNo;
  }

  public void setPrItemNo(String prItemNo) {
    this.prItemNo = prItemNo;
  }

  public String getItemCode() {
    return itemCode;
  }

  public void setItemCode(String itemCode) {
    this.itemCode = itemCode;
  }

  public String getDetailRemark() {
    return detailRemark;
  }

  public void setDetailRemark(String detailRemark) {
    this.detailRemark = detailRemark;
  }

  public String getDemandDate() {
    return demandDate;
  }

  public void setDemandDate(String demandDate) {
    this.demandDate = demandDate;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getQty() {
    return qty;
  }

  public void setQty(String qty) {
    this.qty = qty;
  }

  public String getUnitOfMeasurement() {
    return unitOfMeasurement;
  }

  public void setUnitOfMeasurement(String unitOfMeasurement) {
    this.unitOfMeasurement = unitOfMeasurement;
  }

  public String getEstimatedAmount() {
    return estimatedAmount;
  }

  public void setEstimatedAmount(String estimatedAmount) {
    this.estimatedAmount = estimatedAmount;
  }

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public String getTax() {
    return tax;
  }

  public void setTax(String tax) {
    this.tax = tax;
  }

  public String getBudgetDepartment() {
    return budgetDepartment;
  }

  public void setBudgetDepartment(String budgetDepartment) {
    this.budgetDepartment = budgetDepartment;
  }

  public List<VendorQuoteDto> getVendorQuotes() {
    return vendorQuotes;
  }

  public void setVendorQuotes(List<VendorQuoteDto> vendorQuotes) {
    this.vendorQuotes = vendorQuotes;
  }

  public String getDemandDateByFlow() {
    return demandDateByFlow;
  }

  public void setDemandDateByFlow(String demandDateByFlow) {
    this.demandDateByFlow = demandDateByFlow;
  }

  public boolean getAutoTax() {
    return autoTax;
  }

  public void setAutoTax(boolean autoTax) {
    this.autoTax = autoTax;
  }
  
}
