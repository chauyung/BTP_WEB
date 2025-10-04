package nccc.btp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.*;

/**
 * 驗收單所得人
 */
@Entity
@Table(name = "BPM_REV_M_TP")
public class BpmRevMTP {

    /*
     * 主鍵
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bpm_rev_m_tp")
    @SequenceGenerator(name = "seq_bpm_rev_m_tp", sequenceName = "SEQ_BPM_REV_M_TP", allocationSize = 1)
    @Column(name = "ID", nullable = false, precision = 38, scale = 0)
    private Long id;

    /**
     * 驗收單單號
     */
    @Column(name = "REV_NO", length = 12)
    private String revNo;

    /*
     * 驗收單明細主鍵
     */
    @Column(name = "D_ID", nullable = false, precision = 38, scale = 0)
    private Long dId;

    /** 處理識別鍵 */
    @Column(name = "HANDLE_IDENTIFICATION_KEY", length = 36)
    private String handleIdentificationKey;

    /*
     * 身分證字號
     */
    @Column(name = "ID_NUMBER", length = 10)
    public String iDNumber;

    /*
     * 身分證錯誤註記
     */
    @Column(name = "ERROR_NOTE", length = 5)
    public String errorNote;

    /*
     * 證號別
     */
    @Column(name = "CERTIFICATE_CATEGORY", length = 5)
    public String certificateCategory;

    /*
     * 所得人名稱
     */
    @Column(name = "NAME", length = 10)
    public String name;

    /*
     * 所得人類別
     */
    @Column(name = "TYPE", length = 1)
    public String type;

    /*
     * 是否滿183天
     */
    @Column(name = "HAS_183_DAYS", length = 1)
    public String has183Days;

    /*
     * 居住國家代碼
     */
    @Column(name = "COUNTRY_CODE", length = 5)
    public String countryCode;

    /*
     * 是否持用護照
     */
    @Column(name = "HAS_PASSPORT", length = 1)
    public String HasPassport;

    /*
     * 護照號碼
     */
    @Column(name = "PASSPORT_NO", length = 10)
    public String passportNo;

    /*
     * 國內有無地址
     */
    @Column(name = "HAS_ADDRESS", length = 1)
    public String hasAddress;

    /*
     * 戶籍地址
     */
    @Column(name = "ADDRESS", length = 80)
    public String address;

    /*
     * 通訊地址
     */
    @Column(name = "CONTACT_ADDRESS", length = 80)
    public String contactAddress;

    /*
     * 聯絡電話
     */
    @Column(name = "Contact_Phone", length = 20)
    public String contactPhone;

    /*
     * 所得年度
     */
    @Column(name = "YEAR", length = 4)
    public String year;

    /*
     * 所得月份(起)
     */
    @Column(name = "BEGIN_MONTH", length = 2)
    public String beginMonth;

    /*
     * 所得月份(迄)
     */
    @Column(name = "END_MONTH", length = 2)
    public String endMonth;

    /*
     * 所得註記
     */
    @Column(name = "INCOME_NOTE", length = 5)
    public String incomeNote;

    /*
     * 所得格式
     */
    @Column(name = "INCOME_TYPE", length = 5)
    public String incomeType;

    /*
     * 給付總額
     */
    @Column(name = "TOTAL_AMOUNT_PAID", precision = 13, scale = 3)
    public BigDecimal totalAmountPaid;

    /*
     * 扣繳稅率
     */
    @Column(name = "WITHHOLDING_TAX_RATE", precision = 13, scale = 3)
    public BigDecimal withholdingTaxRate;

    /*
     * 扣繳稅額
     */             
    @Column(name = "WITHHOLDING_TAX", precision = 13, scale = 3)
    public BigDecimal wthholdingTax;

    /*
     * 代扣健保費率
     */
    @Column(name = "NHI_RATE", precision = 13, scale = 3)
    public BigDecimal nHIRate;

    /*
     * 代扣健保費
     */
    @Column(name = "NHI_PREMIUM", precision = 13, scale = 3)
    public BigDecimal nHIPremium;

    /*
     * 給付淨額
     */
    @Column(name = "NET_PAYMENT", precision = 13, scale = 3)
    public BigDecimal netPayment;

    /*
     * 給付日期
     */
    @Column(name = "PAYMENT_DATE", nullable = true)
    public LocalDate paymentDate;

    /*
     * 軟體註記
     */
    @Column(name = "SOFTWARE_NOTE", length = 5)
    public String softwareNote;

    /*
     * 租稅協定代碼
     */
    @Column(name = "TAX_TREATY_CODE", length = 2)
    public String taxTreatyCode;

    /*
     * 扣抵稅額註記
     */
    @Column(name = "TAX_CREDIT_FLAG", length = 12)
    public String taxCreditFlag;

    /*
     * 憑單填發方式
     */
    @Column(name = "CERTIFICATE_ISSUE_METHOD", length = 1)
    public String certificateIssueMethod;

    /*
     * 共用欄位1
     */
    @Column(name = "SHARE_COLUMN1", length = 12)
    public String shareColumn1;

    /*
     * 共用欄位2
     */
    @Column(name = "SHARE_COLUMN2", length = 12)
    public String ShareColumn2;

    /*
     * 共用欄位3
     */
    @Column(name = "SHARE_COLUMN3", length = 12)
    public String ShareColumn3;

    /*
     * 共用欄位4
     */
    @Column(name = "SHARE_COLUMN4", length = 12)
    public String ShareColumn4;

    /*
     * 共用欄位5
     */
    @Column(name = "SHARE_COLUMN5", length = 12)
    public String ShareColumn5;

    /*
     * 稅務識別碼
     */
    @Column(name = "TAX_IDENTIFICATION_NO", length = 12)
    public String taxIdentificationNo;

    /*
     * 備註
     */
    @Column(name = "REMARK", length = 50)
    public String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRevNo() {
        return revNo;
    }

    public void setRevNo(String revNo) {
        this.revNo = revNo;
    }

    public Long getdId() {
        return dId;
    }

    public void setdId(Long dId) {
        this.dId = dId;
    }

    public String getiDNumber() {
        return iDNumber;
    }

    public void setiDNumber(String iDNumber) {
        this.iDNumber = iDNumber;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        return HasPassport;
    }

    public void setHasPassport(String hasPassport) {
        HasPassport = hasPassport;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBeginMonth() {
        return beginMonth;
    }

    public void setBeginMonth(String beginMonth) {
        this.beginMonth = beginMonth;
    }

    public String getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(String endMonth) {
        this.endMonth = endMonth;
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

    public BigDecimal getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(BigDecimal totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public BigDecimal getWithholdingTaxRate() {
        return withholdingTaxRate;
    }

    public void setWithholdingTaxRate(BigDecimal withholdingTaxRate) {
        this.withholdingTaxRate = withholdingTaxRate;
    }

    public BigDecimal getWthholdingTax() {
        return wthholdingTax;
    }

    public void setWthholdingTax(BigDecimal wthholdingTax) {
        this.wthholdingTax = wthholdingTax;
    }

    public BigDecimal getnHIRate() {
        return nHIRate;
    }

    public void setnHIRate(BigDecimal nHIRate) {
        this.nHIRate = nHIRate;
    }

    public BigDecimal getnHIPremium() {
        return nHIPremium;
    }

    public void setnHIPremium(BigDecimal nHIPremium) {
        this.nHIPremium = nHIPremium;
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

    public String getShareColumn1() {
        return shareColumn1;
    }

    public void setShareColumn1(String shareColumn1) {
        this.shareColumn1 = shareColumn1;
    }

    public String getShareColumn2() {
        return ShareColumn2;
    }

    public void setShareColumn2(String shareColumn2) {
        ShareColumn2 = shareColumn2;
    }

    public String getShareColumn3() {
        return ShareColumn3;
    }

    public void setShareColumn3(String shareColumn3) {
        ShareColumn3 = shareColumn3;
    }

    public String getShareColumn4() {
        return ShareColumn4;
    }

    public void setShareColumn4(String shareColumn4) {
        ShareColumn4 = shareColumn4;
    }

    public String getShareColumn5() {
        return ShareColumn5;
    }

    public void setShareColumn5(String shareColumn5) {
        ShareColumn5 = shareColumn5;
    }

    public String getTaxIdentificationNo() {
        return taxIdentificationNo;
    }

    public void setTaxIdentificationNo(String taxIdentificationNo) {
        this.taxIdentificationNo = taxIdentificationNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHandleIdentificationKey() {
        return handleIdentificationKey;
    }

    public void setHandleIdentificationKey(String handleIdentificationKey) {
        this.handleIdentificationKey = handleIdentificationKey;
    }
}
