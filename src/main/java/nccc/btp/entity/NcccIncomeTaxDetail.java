package nccc.btp.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "NCCC_INCOME_TAX_DETAIL")
public class NcccIncomeTaxDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_nccc_income_tax_detail")
    @SequenceGenerator(name = "seq_nccc_income_tax_detail", sequenceName = "SEQ_NCCC_INCOME_TAX_DETAIL", allocationSize = 1)
    @Column(name = "ID", nullable = false, precision = 38, scale = 0)
    private Long id;

    // 所得人主鍵
    @Column(name = "PK_M_ID", nullable = false, precision = 38, scale = 0)
    private Long pkMId;

    // 所得給付年度
    @Column(name = "PAYMENT_YEAR", length = 4)
    private String paymentYear;

    // 所得給付月份(起)
    @Column(name = "PAYMENT_MONTH_ST", length = 2)
    private String paymentMonthSt;

    // 所得給付月份(迄)
    @Column(name = "PAYMENT_MONTH_ED", length = 2)
    private String paymentMonthEd;

    // 軟體註記
    @Column(name = "SOFTWARE_NOTE", length = 1)
    private String softwareNote;

    // 所得格式
    @Column(name = "INCOME_TYPE", length = 2)
    private String incomeType;

    // 所得註記
    @Column(name = "INCOME_NOTE", length = 1)
    private String incomeNote;

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
    @Column(name = "PAYMENT_DATE")
    private LocalDate paymentDate;

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

    // 居住地國或地區代碼
    @Column(name = "COUNTRY_CODE", length = 2)
    private String countryCode;

    // 是否满183天
    @Column(name = "HAS_183_DAYS", length = 1)
    private String has183Days;

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

    // 相關單號
    @Column(name = "BTP_ORDER_NO", length = 20)
    private String btpOrderNo;

    // SAP會計文件號碼
    @Column(name = "SAP_DOC_NO", length = 20)
    private String sapDocNo;

    // 資料來源檔
    @Column(name = "SOURCE_FILE", length = 1)
    private String sourceFile ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPkMId() {
        return pkMId;
    }

    public void setPkMId(Long pkMId) {
        this.pkMId = pkMId;
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

    public String getSoftwareNote() {
        return softwareNote;
    }

    public void setSoftwareNote(String softwareNote) {
        this.softwareNote = softwareNote;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    public String getIncomeNote() {
        return incomeNote;
    }

    public void setIncomeNote(String incomeNote) {
        this.incomeNote = incomeNote;
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

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getHas183Days() {
        return has183Days;
    }

    public void setHas183Days(String has183Days) {
        this.has183Days = has183Days;
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

    public String getBtpOrderNo() {
        return btpOrderNo;
    }

    public void setBtpOrderNo(String btpOrderNo) {
        this.btpOrderNo = btpOrderNo;
    }

    public String getSapDocNo() {
        return sapDocNo;
    }

    public void setSapDocNo(String sapDocNo) {
        this.sapDocNo = sapDocNo;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }
}
