package nccc.btp.vo;

import java.util.ArrayList;
import java.util.List;
import nccc.btp.entity.NcccIncomeTaxCategory;

public class NcccIncomePersonVo {

  private List<IncomePersonVo> ncccIncomePersonList;

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

  public NcccIncomePersonVo() {
    this.ncccIncomePersonList = new ArrayList<>();
    this.certificateCategoryList = new ArrayList<>();
    this.incomeCategoryList = new ArrayList<>();
    this.incomeNoteList = new ArrayList<>();
    this.softwareNoteList = new ArrayList<>();
    this.chargeCodeList = new ArrayList<>();
    this.otherIncomeList = new ArrayList<>();
    this.businessSectorList = new ArrayList<>();
    this.countryCodeList = new ArrayList<>();
    this.errorNoteList = new ArrayList<>();
  }

  public List<IncomePersonVo> getNcccIncomePersonList() {
    return ncccIncomePersonList;
  }

  public void setNcccIncomePersonList(List<IncomePersonVo> ncccIncomePersonList) {
    this.ncccIncomePersonList = ncccIncomePersonList;
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
}
