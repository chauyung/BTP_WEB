package nccc.btp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NCCC_INCOME_TAX_PARAMETER")
public class NcccIncomeTaxParameter {

    // 扣繳單位統一編號
    @Id
    @Column(name = "BAN", length = 10)
    private String ban;

    // 職稱
    @Column(name = "TAX_RATE", length = 5)
    private String taxRate;

    // 所得關帳年月
    @Column(name = "REPORTING_YEAR", length = 3)
    private String reportingYear;

    // 稽徵機關
    @Column(name = "AGENCY", length = 50)
    private String agency;

    // 稽徵機關
    @Column(name = "CLOSED_YEAR", length = 6)
    private String closedYear;

    // 扣繳義務人
    @Column(name = "AGENT", length = 50)
    private String agent;


    // 所得扣稅限額
    @Column(name = "TAX_DEDUCTION_LIMIT", length = 20)
    private String taxDeductionLimit;

    // 扣繳單位名稱
    @Column(name = "UNIT", length = 50)
    private String unit;

    // 扣繳單位地址
    @Column(name = "ADDRESS", length = 100)
    private String address;

    // 扣繳單位電話
    @Column(name = "MOBILE", length = 20)
    private String mobile;

    // 代扣健保限額
    @Column(name = "INSURANCE_DEDUCTION_LIMIT", length = 20)
    private String insuranceDeductionLimit;

    // 健保投保代號
    @Column(name = "INSURANCE_CODE", length = 20)
    private String insuranceCode;

    // 代扣健保費率
    @Column(name = "INSURANCE_RATE", length = 10)
    private String insuranceRate;

    // 備註
    @Column(name = "REMARK", length = 50)
    private String remark;

    public String getBan() {
        return ban;
    }

    public void setBan(String ban) {
        this.ban = ban;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getReportingYear() {
        return reportingYear;
    }

    public void setReportingYear(String reportingYear) {
        this.reportingYear = reportingYear;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getClosedYear() {
        return closedYear;
    }

    public void setClosedYear(String closedYear) {
        this.closedYear = closedYear;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getTaxDeductionLimit() {
        return taxDeductionLimit;
    }

    public void setTaxDeductionLimit(String taxDeductionLimit) {
        this.taxDeductionLimit = taxDeductionLimit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getInsuranceDeductionLimit() {
        return insuranceDeductionLimit;
    }

    public void setInsuranceDeductionLimit(String insuranceDeductionLimit) {
        this.insuranceDeductionLimit = insuranceDeductionLimit;
    }

    public String getInsuranceCode() {
        return insuranceCode;
    }

    public void setInsuranceCode(String insuranceCode) {
        this.insuranceCode = insuranceCode;
    }

    public String getInsuranceRate() {
        return insuranceRate;
    }

    public void setInsuranceRate(String insuranceRate) {
        this.insuranceRate = insuranceRate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
