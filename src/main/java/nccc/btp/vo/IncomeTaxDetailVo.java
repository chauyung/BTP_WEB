package nccc.btp.vo;

import java.math.BigDecimal;

public class IncomeTaxDetailVo {

    private Long id;

    // 所得人主鍵
    private Long pkMId;

    // 證號
    private String ban;

    // 姓名
    private String name;

    // 所得給付年度
    private String paymentYear;

    // 所得給付月份(起)
    private String paymentMonthSt;

    // 所得給付月份(迄)
    private String paymentMonthEd;

    // 軟體註記
    private String softwareNote;

    // 所得格式
    private String incomeType;

    // 所得註記
    private String incomeNote;

    // 給付總額
    private BigDecimal grossPayment;

    // 扣繳稅率
    private BigDecimal withholdingTaxRate;

    // 扣繳稅額
    private BigDecimal withholdingTax;

    // 代扣健保費率
    private BigDecimal nhRate;

    // 代扣健保費
    private BigDecimal nhWithHolding;

    // 給付淨額
    private BigDecimal netPayment;

    // 給付日期
    private String paymentDate;

    // 租稅協定代碼
    private String taxTreatyCode;

    // 扣抵稅額註記
    private String taxCreditFlag;

    // 憑單填發方式
    private String certificateIssueMethod;

    // 稅務識別碼
    private String taxIdentificationNo;

    // 居住地國或地區代碼
    private String countryCode;

    // 是否满183天
    private String has183Days;

    //共用欄位1
    private String shareColumn1;

    //共用欄位2
    private String shareColumn2;

    //共用欄位3
    private String shareColumn3;

    //共用欄位4
    private String shareColumn4;

    //共用欄位5
    private String shareColumn5;

    // 備註
    private String remark;

    // 相關單號
    private String btpOrderNo;

    // SAP會計文件號碼
    private String sapDocNo;

    // 資料來源檔
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

    public String getBan() {
        return ban;
    }

    public void setBan(String ban) {
        this.ban = ban;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
