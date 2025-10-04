package nccc.btp.vo;

import nccc.btp.entity.*;

import java.util.ArrayList;
import java.util.List;

public class CApplicationExpensesVo extends ProcessVo{

    private List<FileVo> fileList;

    private List<TaskHistoryVo> taskHistory;

    private List<NcccCurrency> ncccCurrencyList;

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

    //研發委員
    private List<NcccCommitteeList> ncccCommitteeListList;

    //代扣繳類別
    private List<NcccWithholdType> withholdTypeList;

    //部門對應表
    private List<NcccCostcenterOrgMapping>  costcenterOrgMappingList;

    private BpmExMVo  bpmExMVo;

    public CApplicationExpensesVo() {
        this.setFileList(new ArrayList<>());
        this.setTaskHistory(new ArrayList<>());
        this.setNcccCurrencyList(new ArrayList<>());
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
        this.setNcccCommitteeListList(new ArrayList<>());
        this.setWithholdTypeList(new ArrayList<>());
        this.setCostcenterOrgMappingList(new ArrayList<>());
    }

    public List<FileVo> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileVo> fileList) {
        this.fileList = fileList;
    }

    public List<TaskHistoryVo> getTaskHistory() {
        return taskHistory;
    }

    public void setTaskHistory(List<TaskHistoryVo> taskHistory) {
        this.taskHistory = taskHistory;
    }

    public List<NcccCurrency> getNcccCurrencyList() {
        return ncccCurrencyList;
    }

    public void setNcccCurrencyList(List<NcccCurrency> ncccCurrencyList) {
        this.ncccCurrencyList = ncccCurrencyList;
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

    public List<NcccCommitteeList> getNcccCommitteeListList() {
        return ncccCommitteeListList;
    }

    public void setNcccCommitteeListList(List<NcccCommitteeList> ncccCommitteeListList) {
        this.ncccCommitteeListList = ncccCommitteeListList;
    }

    public BpmExMVo getBpmExMVo() {
        return bpmExMVo;
    }

    public void setBpmExMVo(BpmExMVo bpmExMVo) {
        this.bpmExMVo = bpmExMVo;
    }

    public List<NcccWithholdType> getWithholdTypeList() {
        return withholdTypeList;
    }

    public void setWithholdTypeList(List<NcccWithholdType> withholdTypeList) {
        this.withholdTypeList = withholdTypeList;
    }

    public List<NcccCostcenterOrgMapping> getCostcenterOrgMappingList() {
        return costcenterOrgMappingList;
    }

    public void setCostcenterOrgMappingList(List<NcccCostcenterOrgMapping> costcenterOrgMappingList) {
        this.costcenterOrgMappingList = costcenterOrgMappingList;
    }
}
