package nccc.btp.vo;

import java.math.BigDecimal;

public class BpmExMDetailWHVo {

    private String ban;

    private String errorNote;

    private String certificateCategory;

    private String gainerName;

    private String gainerType;

    private String has183Days;

    private String countryCode;

    private String hasPassport;

    private String passportNo;

    private String hasAddress;

    private String address;

    private String contactAddress;

    private String contactPhone;

    private String paymentYear;

    private String paymentMonthSt;

    private String paymentMonthEd;

    private String incomeNote;

    private String incomeType;

    private BigDecimal grossPayment;

    private BigDecimal withholdingTaxRate;

    private BigDecimal withholdingTax;

    private BigDecimal nhRate;

    private String revenueCategory;

    private BigDecimal nhWithHolding;

    private BigDecimal netPayment;

    private String paymentDate;

    private String softwareNote;

    private String taxTreatyCode;

    private String taxCreditFlag;

    private String certificateIssueMethod;

    private String taxIdentificationNo;

    private String shareColumn1;

    private String shareColumn2;

    private String shareColumn3;

    private String shareColumn4;

    private String shareColumn5;

    private String remark;

    public BpmExMDetailWHVo(){

        this.setBan("");
        this.setErrorNote("");
        this.setCertificateCategory("");
        this.setGainerName("");
        this.setGainerType("");
        this.setHas183Days("");
        this.setCountryCode("");
        this.setHasPassport("");
        this.setPassportNo("");
        this.setHasAddress("");
        this.setAddress("");
        this.setContactAddress("");
        this.setContactPhone("");
        this.setPaymentYear("");
        this.setPaymentMonthSt("");
        this.setPaymentMonthEd("");
        this.setIncomeNote("");
        this.setIncomeType("");
        this.setGrossPayment(new BigDecimal("0.00"));
        this.setWithholdingTaxRate(new BigDecimal("0.00"));
        this.setWithholdingTax(new BigDecimal("0.00"));
        this.setNhRate(new BigDecimal("0.00"));
        this.setRevenueCategory("");
        this.setNhWithHolding(new BigDecimal("0.00"));
        this.setNetPayment(new BigDecimal("0.00"));
        this.setPaymentDate("");
        this.setSoftwareNote("");
        this.setTaxTreatyCode("");
        this.setTaxCreditFlag("");
        this.setCertificateIssueMethod("");
        this.setTaxIdentificationNo("");
        this.setShareColumn1("");
        this.setShareColumn2("");
        this.setShareColumn3("");
        this.setShareColumn4("");
        this.setShareColumn5("");
        this.setRemark("");
    }

    public String getBan() {
        return ban;
    }

    public void setBan(String ban) {
        this.ban = ban;
    }

    public String getErrorNote() {
        return errorNote;
    }

    public void setErrorNote(String errorNote) {
        this.errorNote = errorNote;
    }

    public String getCertificateCategory() {
        return certificateCategory;
    }

    public void setCertificateCategory(String certificateCategory) {
        this.certificateCategory = certificateCategory;
    }

    public String getGainerName() {
        return gainerName;
    }

    public void setGainerName(String gainerName) {
        this.gainerName = gainerName;
    }

    public String getGainerType() {
        return gainerType;
    }

    public void setGainerType(String gainerType) {
        this.gainerType = gainerType;
    }

    public String getHas183Days() {
        return has183Days;
    }

    public void setHas183Days(String has183Days) {
        this.has183Days = has183Days;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getHasPassport() {
        return hasPassport;
    }

    public void setHasPassport(String hasPassport) {
        this.hasPassport = hasPassport;
    }

    public String getHasAddress() {
        return hasAddress;
    }

    public void setHasAddress(String hasAddress) {
        this.hasAddress = hasAddress;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getPaymentYear() {
        return paymentYear;
    }

    public void setPaymentYear(String paymentYear) {
        this.paymentYear = paymentYear;
    }

    public String getPaymentMonthSt() {
        return paymentMonthSt;
    }

    public void setPaymentMonthSt(String paymentMonthSt) {
        this.paymentMonthSt = paymentMonthSt;
    }

    public String getPaymentMonthEd() {
        return paymentMonthEd;
    }

    public void setPaymentMonthEd(String paymentMonthEd) {
        this.paymentMonthEd = paymentMonthEd;
    }

    public String getIncomeNote() {
        return incomeNote;
    }

    public void setIncomeNote(String incomeNote) {
        this.incomeNote = incomeNote;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    public BigDecimal getGrossPayment() {
        return grossPayment;
    }

    public void setGrossPayment(BigDecimal grossPayment) {
        this.grossPayment = grossPayment;
    }

    public BigDecimal getWithholdingTaxRate() {
        return withholdingTaxRate;
    }

    public void setWithholdingTaxRate(BigDecimal withholdingTaxRate) {
        this.withholdingTaxRate = withholdingTaxRate;
    }

    public BigDecimal getWithholdingTax() {
        return withholdingTax;
    }

    public void setWithholdingTax(BigDecimal withholdingTax) {
        this.withholdingTax = withholdingTax;
    }

    public BigDecimal getNhRate() {
        return nhRate;
    }

    public void setNhRate(BigDecimal nhRate) {
        this.nhRate = nhRate;
    }

    public String getRevenueCategory() {
        return revenueCategory;
    }

    public void setRevenueCategory(String revenueCategory) {
        this.revenueCategory = revenueCategory;
    }

    public BigDecimal getNhWithHolding() {
        return nhWithHolding;
    }

    public void setNhWithHolding(BigDecimal nhWithHolding) {
        this.nhWithHolding = nhWithHolding;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getNetPayment() {
        return netPayment;
    }

    public void setNetPayment(BigDecimal netPayment) {
        this.netPayment = netPayment;
    }

    public String getSoftwareNote() {
        return softwareNote;
    }

    public void setSoftwareNote(String softwareNote) {
        this.softwareNote = softwareNote;
    }

    public String getTaxTreatyCode() {
        return taxTreatyCode;
    }

    public void setTaxTreatyCode(String taxTreatyCode) {
        this.taxTreatyCode = taxTreatyCode;
    }

    public String getTaxCreditFlag() {
        return taxCreditFlag;
    }

    public void setTaxCreditFlag(String taxCreditFlag) {
        this.taxCreditFlag = taxCreditFlag;
    }

    public String getCertificateIssueMethod() {
        return certificateIssueMethod;
    }

    public void setCertificateIssueMethod(String certificateIssueMethod) {
        this.certificateIssueMethod = certificateIssueMethod;
    }

    public String getTaxIdentificationNo() {
        return taxIdentificationNo;
    }

    public void setTaxIdentificationNo(String taxIdentificationNo) {
        this.taxIdentificationNo = taxIdentificationNo;
    }

    public String getShareColumn1() {
        return shareColumn1;
    }

    public void setShareColumn1(String shareColumn1) {
        this.shareColumn1 = shareColumn1;
    }

    public String getShareColumn2() {
        return shareColumn2;
    }

    public void setShareColumn2(String shareColumn2) {
        this.shareColumn2 = shareColumn2;
    }

    public String getShareColumn3() {
        return shareColumn3;
    }

    public void setShareColumn3(String shareColumn3) {
        this.shareColumn3 = shareColumn3;
    }

    public String getShareColumn4() {
        return shareColumn4;
    }

    public void setShareColumn4(String shareColumn4) {
        this.shareColumn4 = shareColumn4;
    }

    public String getShareColumn5() {
        return shareColumn5;
    }

    public void setShareColumn5(String shareColumn5) {
        this.shareColumn5 = shareColumn5;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
