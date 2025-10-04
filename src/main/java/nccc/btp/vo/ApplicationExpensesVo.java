package nccc.btp.vo;

import java.util.ArrayList;
import java.util.List;

import nccc.btp.entity.*;
import nccc.btp.vo.BpmExMVo;
import nccc.btp.enums.Mode;

public class ApplicationExpensesVo extends ProcessVo {

  private List<NcccPayMethod> ncccPayMethodList;

  private List<NcccCurrency> ncccCurrencyList;

  private List<NcccReceiptType> ncccReceiptTypeList;

  private List<NcccCostCenter> ncccCostCenterList;
  
  private List<NcccExpenseCategoryNumber> ncccExpenseCategoryNumberList;
  
  private List<ZmmtSupplier> zmmtSupplierList;

  private List<BpmPrepaidOrder> prepaidOrderList;

  private List<FileVo> fileList;

  private List<TaskHistoryVo> taskHistory;

  //證號別
  private List<NcccIncomeTaxCategory> certificateCategoryList;

  //所得格式
  private List<NcccIncomeTaxCategory> incomeCategoryList;

  //所得註記
  private List<NcccIncomeTaxCategory> incomeNoteList;

  //軟體註記
  private List<NcccIncomeTaxCategory> softwareNoteList;

  //費用代號
  private List<NcccIncomeTaxCategory> chargeCodeList;

  //其他所得
  private List<NcccIncomeTaxCategory> otherIncomeList;

  //執行業務業別
  private List<NcccIncomeTaxCategory> businessSectorList;

  //國家代碼
  private List<NcccIncomeTaxCategory> countryCodeList;

  //錯誤註記
  private List<NcccIncomeTaxCategory> errorNoteList;

  //所得類別
  private List<NcccIncomeTaxCategory> revenueCategoryList;

  //代扣繳類別
  private List<NcccWithholdType> withholdTypeList;

  //部門對應表
  private List<NcccCostcenterOrgMapping>  costcenterOrgMappingList;

  private BpmExMVo  bpmExMVo;

  private boolean isPettyCashAllowance;

  public ApplicationExpensesVo() {
    this.setNcccPayMethodList(new ArrayList<>());
    this.setNcccCurrencyList(new ArrayList<>());
    this.setNcccReceiptTypeList(new ArrayList<>());
    this.setNcccCostCenterList(new ArrayList<>());
    this.setNcccExpenseCategoryNumberList(new ArrayList<>());
    this.setZmmtSupplierList(new ArrayList<>());
    this.setPrepaidOrderList(new ArrayList<>());
    this.setFileList(new ArrayList<>());
    this.setTaskHistory(new ArrayList<>());
    this.setCertificateCategoryList(new ArrayList<>());
    this.setIncomeCategoryList(new ArrayList<>());
    this.setIncomeNoteList(new ArrayList<>());
    this.setSoftwareNoteList(new ArrayList<>());
    this.setChargeCodeList(new ArrayList<>());
    this.setOtherIncomeList(new ArrayList<>());
    this.setBusinessSectorList(new ArrayList<>());
    this.setCountryCodeList(new ArrayList<>());
    this.setErrorNoteList(new ArrayList<>());
    this.setRevenueCategoryList(new ArrayList<>());
    this.setWithholdTypeList(new ArrayList<>());
    this.setPettyCashAllowance(false);
    this.setCostcenterOrgMappingList(new ArrayList<>());
  }

  public List<NcccPayMethod> getNcccPayMethodList() {
    return ncccPayMethodList;
  }

  public void setNcccPayMethodList(List<NcccPayMethod> ncccPayMethodList) {
    this.ncccPayMethodList = ncccPayMethodList;
  }

  public List<NcccCurrency> getNcccCurrencyList() {
    return ncccCurrencyList;
  }

  public void setNcccCurrencyList(List<NcccCurrency> ncccCurrencyList) {
    this.ncccCurrencyList = ncccCurrencyList;
  }

  public List<NcccReceiptType> getNcccReceiptTypeList() {
    return ncccReceiptTypeList;
  }

  public void setNcccReceiptTypeList(List<NcccReceiptType> ncccReceiptTypeList) {
    this.ncccReceiptTypeList = ncccReceiptTypeList;
  }

  public List<NcccCostCenter> getNcccCostCenterList() {
    return ncccCostCenterList;
  }

  public void setNcccCostCenterList(List<NcccCostCenter> ncccCostCenterList) {
    this.ncccCostCenterList = ncccCostCenterList;
  }

  public List<NcccExpenseCategoryNumber> getNcccExpenseCategoryNumberList() {
    return ncccExpenseCategoryNumberList;
  }

  public void setNcccExpenseCategoryNumberList(
      List<NcccExpenseCategoryNumber> ncccExpenseCategoryNumberList) {
    this.ncccExpenseCategoryNumberList = ncccExpenseCategoryNumberList;
  }

  public List<ZmmtSupplier> getZmmtSupplierList() {
    return zmmtSupplierList;
  }

  public void setZmmtSupplierList(List<ZmmtSupplier> zmmtSupplierList) {
    this.zmmtSupplierList = zmmtSupplierList;
  }

  public List<BpmPrepaidOrder> getPrepaidOrderList() {
    return prepaidOrderList;
  }

  public void setPrepaidOrderList(List<BpmPrepaidOrder> prepaidOrderList) {
    this.prepaidOrderList = prepaidOrderList;
  }

  public List<FileVo> getFileList() {
    return fileList;
  }

  public void setFileList(List<FileVo> fileList) {
    this.fileList = fileList;
  }

  public BpmExMVo getBpmExMVo() {
    return bpmExMVo;
  }

  public void setBpmExMVo(BpmExMVo bpmExMVo) {
    this.bpmExMVo = bpmExMVo;
  }

  public List<TaskHistoryVo> getTaskHistory() {
    return taskHistory;
  }

  public void setTaskHistory(List<TaskHistoryVo> taskHistory) {
    this.taskHistory = taskHistory;
  }

  public List<NcccIncomeTaxCategory> getCertificateCategoryList() {
    return certificateCategoryList;
  }

  public void setCertificateCategoryList(List<NcccIncomeTaxCategory> certificateCategoryList) {
    this.certificateCategoryList = certificateCategoryList;
  }

  public List<NcccIncomeTaxCategory> getIncomeCategoryList() {
    return incomeCategoryList;
  }

  public void setIncomeCategoryList(List<NcccIncomeTaxCategory> incomeCategoryList) {
    this.incomeCategoryList = incomeCategoryList;
  }

  public List<NcccIncomeTaxCategory> getIncomeNoteList() {
    return incomeNoteList;
  }

  public void setIncomeNoteList(List<NcccIncomeTaxCategory> incomeNoteList) {
    this.incomeNoteList = incomeNoteList;
  }

  public List<NcccIncomeTaxCategory> getSoftwareNoteList() {
    return softwareNoteList;
  }

  public void setSoftwareNoteList(List<NcccIncomeTaxCategory> softwareNoteList) {
    this.softwareNoteList = softwareNoteList;
  }

  public List<NcccIncomeTaxCategory> getChargeCodeList() {
    return chargeCodeList;
  }

  public void setChargeCodeList(List<NcccIncomeTaxCategory> chargeCodeList) {
    this.chargeCodeList = chargeCodeList;
  }

  public List<NcccIncomeTaxCategory> getOtherIncomeList() {
    return otherIncomeList;
  }

  public void setOtherIncomeList(List<NcccIncomeTaxCategory> otherIncomeList) {
    this.otherIncomeList = otherIncomeList;
  }

  public List<NcccIncomeTaxCategory> getBusinessSectorList() {
    return businessSectorList;
  }

  public void setBusinessSectorList(List<NcccIncomeTaxCategory> businessSectorList) {
    this.businessSectorList = businessSectorList;
  }

  public List<NcccIncomeTaxCategory> getCountryCodeList() {
    return countryCodeList;
  }

  public void setCountryCodeList(List<NcccIncomeTaxCategory> countryCodeList) {
    this.countryCodeList = countryCodeList;
  }

  public List<NcccIncomeTaxCategory> getErrorNoteList() {
    return errorNoteList;
  }

  public void setErrorNoteList(List<NcccIncomeTaxCategory> errorNoteList) {
    this.errorNoteList = errorNoteList;
  }

  public List<NcccIncomeTaxCategory> getRevenueCategoryList() {
    return revenueCategoryList;
  }

  public void setRevenueCategoryList(List<NcccIncomeTaxCategory> revenueCategoryList) {
    this.revenueCategoryList = revenueCategoryList;
  }

  public List<NcccWithholdType> getWithholdTypeList() {
    return withholdTypeList;
  }

  public void setWithholdTypeList(List<NcccWithholdType> withholdTypeList) {
    this.withholdTypeList = withholdTypeList;
  }

  public boolean isPettyCashAllowance() {
    return isPettyCashAllowance;
  }

  public void setPettyCashAllowance(boolean pettyCashAllowance) {
    isPettyCashAllowance = pettyCashAllowance;
  }

  public List<NcccCostcenterOrgMapping> getCostcenterOrgMappingList() {
    return costcenterOrgMappingList;
  }

  public void setCostcenterOrgMappingList(List<NcccCostcenterOrgMapping> costcenterOrgMappingList) {
    this.costcenterOrgMappingList = costcenterOrgMappingList;
  }
}
