package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.*;

//費用申請單扣繳清單資料
@Entity
@IdClass(BpmExDWH.ConfigId.class)
@Table(name = "BPM_EX_D_WH")
public class BpmExDWH implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "EX_NO", length = 20)
    private String exNo;

    @Id
    @Column(name = "EX_ITEM_NO", length = 20)
    private String exItemNo;

    @Id
    @Column(name = "EX_ITEM_WH_NO", length = 20)
    private String exItemWhNo;

    // 證號
    @Column(name = "BAN", length = 10)
    private String ban;

    // 錯誤註記
    @Column(name = "ERROR_NOTE", length = 1)
    private String errorNote;

    //證號別
    @Column(name = "CERTIFICATE_CATEGORY", length = 1)
    private String certificateCategory;

    // 姓名
    @Column(name = "GAINER_NAME", length = 500)
    private String gainerName;

    // 所得人類別
    @Column(name = "GAINER_TYPE", length = 1)
    private String gainerType;

    // 是否满183天
    @Column(name = "HAS_183_DAYS", length = 1)
    private String has183Days;

    // 居住地國或地區代碼
    @Column(name = "COUNTRY_CODE", length = 2)
    private String countryCode;

    // 是否有護照
    @Column(name = "HAS_PASSPORT", length = 1)
    private String hasPassport;

    // 護照
    @Column(name = "PASSPORT_NO", length = 10)
    private String passportNo;

    // 是否有地址
    @Column(name = "HAS_ADDRESS", length = 1)
    private String hasAddress;

    // 地址
    @Column(name = "ADDRESS", length = 80)
    private String address;

    // 通訊地址
    @Column(name = "CONTACT_ADDRESS", length = 80)
    private String contactAddress;

    // 通訊電話
    @Column(name = "CONTACT_PHONE", length = 20)
    private String contactPhone;

    // 所得給付年度
    @Column(name = "PAYMENT_YEAR", length = 4)
    private String paymentYear;

    // 所得給付月份(起)
    @Column(name = "PAYMENT_MONTH_ST", length = 2)
    private String paymentMonthSt;

    // 所得給付月份(迄)
    @Column(name = "PAYMENT_MONTH_ED", length = 2)
    private String paymentMonthEd;

    // 所得註記
    @Column(name = "INCOME_NOTE", length = 1)
    private String incomeNote;

    // 所得格式
    @Column(name = "INCOME_TYPE", length = 2)
    private String incomeType;

    // 給付總額
    @Column(name = "GROSS_PAYMENT", precision = 13, scale = 2)
    private BigDecimal grossPayment;

    // 扣繳稅率
    @Column(name = "WITHHOLDING_TAX_RATE", precision = 13, scale = 2)
    private BigDecimal withholdingTaxRate;

    // 扣繳稅額
    @Column(name = "WITHHOLDING_TAX", precision = 13, scale = 2)
    private BigDecimal withholdingTax;

    // 代扣健保費率
    @Column(name = "NH_RATE", precision = 13, scale = 2)
    private BigDecimal nhRate;

    // 收入類別
    @Column(name = "REVENUE_CATEGORY", length = 2)
    private String revenueCategory;

    // 代扣健保費
    @Column(name = "NH_WITH_HOLDING", precision = 13, scale = 2)
    private BigDecimal nhWithHolding;

    // 給付淨額
    @Column(name = "NET_PAYMENT", precision = 13, scale = 2)
    private BigDecimal netPayment;

    // 給付日期
    @Column(name = "PAYMENT_DATE", length = 10)
    private String paymentDate;

    // 軟體註記
    @Column(name = "SOFTWARE_NOTE", length = 1)
    private String softwareNote;

    // 租稅協定代碼
    @Column(name = "TAX_TREATY_CODE", length = 2)
    private String taxTreatyCode;

    // 扣抵稅額註記
    @Column(name = "TAX_CREDIT_FLAG", length = 1)
    private String taxCreditFlag;

    // 憑單填發方式
    @Column(name = "CERTIFICATE_ISSUE_METHOD", length = 1)
    private String certificateIssueMethod;

    // 稅務識別碼
    @Column(name = "TAX_IDENTIFICATION_NO", length = 30)
    private String taxIdentificationNo;

    //共用欄位1
    @Column(name = "SHARE_COLUMN_1", length = 12)
    private String shareColumn1;

    //共用欄位2
    @Column(name = "SHARE_COLUMN_2", length = 12)
    private String shareColumn2;

    //共用欄位3
    @Column(name = "SHARE_COLUMN_3", length = 12)
    private String shareColumn3;

    //共用欄位4
    @Column(name = "SHARE_COLUMN_4", length = 12)
    private String shareColumn4;

    //共用欄位5
    @Column(name = "SHARE_COLUMN_5", length = 10)
    private String shareColumn5;

    // 備註
    @Column(name = "REMARK", length = 20)
    private String remark;

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

    public String getExItemWhNo() {
        return exItemWhNo;
    }

    public void setExItemWhNo(String exItemWhNo) {
        this.exItemWhNo = exItemWhNo;
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

    public String getHasPassport() {
        return hasPassport;
    }

    public void setHasPassport(String hasPassport) {
        this.hasPassport = hasPassport;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getHasAddress() {
        return hasAddress;
    }

    public void setHasAddress(String hasAddress) {
        this.hasAddress = hasAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
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

    public BigDecimal getNetPayment() {
        return netPayment;
    }

    public void setNetPayment(BigDecimal netPayment) {
        this.netPayment = netPayment;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
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

    public static class ConfigId implements Serializable {
        private static final long serialVersionUID = 1L;

        private String exNo;
        private String exItemNo;
        private String exItemWhNo;

        public ConfigId(){}

        public ConfigId(String exNo, String exItemNo, String exItemWhNo) {
            this.exNo = exNo;
            this.exItemNo = exItemNo;
            this.exItemWhNo = exItemWhNo;
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

        public String getExItemWhNo() {
            return exItemWhNo;
        }

        public void setExItemWhNo(String exItemWhNo) {
            this.exItemWhNo = exItemWhNo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BpmExDWH.ConfigId)) return false;
            BpmExDWH.ConfigId configId = (BpmExDWH.ConfigId) o;
            return Objects.equals(exNo, configId.exNo) &&
                    Objects.equals(exItemNo, configId.exItemNo)&&
                    Objects.equals(exItemWhNo, configId.exItemWhNo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(exNo, exItemNo,exItemWhNo);
        }
    }
}
