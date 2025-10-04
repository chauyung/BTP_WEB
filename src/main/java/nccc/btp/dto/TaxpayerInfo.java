package nccc.btp.dto;

import java.math.BigDecimal;
import java.time.*;

import com.fasterxml.jackson.annotation.JsonFormat;

/*
 * 所得人資料模型
 */
public class TaxpayerInfo {

    /*
     * 主鍵
     */
    public Long Id;

    /*
     * 身分證字號
     */
    public String IDNumber;

    /*
     * 身分證錯誤註記
     */
    public String ErrorNote;

    /*
     * 證號別
     */
    public String CertificateCategory;

    /*
     * 所得人名稱
     */
    public String Name;

    /*
     * 所得人類別
     */
    public String Type;

    /*
     * 是否滿183天
     */
    public String Has183Days;

    /*
     * 居住國家代碼
     */
    public String CountryCode;

    /*
     * 是否持用護照
     */
    public String HasPassport;

    /*
     * 護照號碼
     */
    public String PassportNo;

    /*
     * 國內有無地址
     */
    public String HasAddress;

    /*
     * 戶籍地址
     */
    public String Address;

    /*
     * 通訊地址
     */
    public String ContactAddress;

    /*
     * 聯絡電話
     */
    public String ContactPhone;

    /*
     * 所得年度
     */
    public String Year;

    /*
     * 所得月份(起)
     */
    public String BeginMonth;

    /*
     * 所得月份(迄)
     */
    public String EndMonth;

    /*
     * 所得註記
     */
    public String IncomeNote;

    /*
     * 所得格式
     */
    public String IncomeType;

    /*
     * 給付總額
     */
    public BigDecimal TotalAmountPaid;

    /*
     * 扣繳稅率
     */
    public BigDecimal WithholdingTaxRate;

    /*
     * 扣繳稅額
     */
    public BigDecimal WithholdingTax;

    /*
     * 代扣健保費率
     */
    public BigDecimal NHIRate;

    /*
     * 代扣健保費
     */
    public BigDecimal NHIPremium;

    /*
     * 給付淨額
     */
    public BigDecimal NetPayment;

    /*
     * 給付日期
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    public LocalDate PaymentDate;

    /*
     * 軟體註記
     */
    public String SoftwareNote;

    /*
     * 租稅協定代碼
     */
    public String TaxTreatyCode;

    /*
     * 扣抵稅額註記
     */
    public String TaxCreditFlag;

    /*
     * 憑單填發方式
     */
    public String CertificateIssueMethod;

    /*
     * 共用欄位1
     */
    public String ShareColumn1;

    /*
     * 共用欄位2
     */
    public String ShareColumn2;

    /*
     * 共用欄位3
     */
    public String ShareColumn3;

    /*
     * 共用欄位4
     */
    public String ShareColumn4;

    /*
     * 共用欄位5
     */
    public String ShareColumn5;

    /*
     * 稅務識別碼
     */
    public String TaxIdentificationNo;

    /*
     * 備註
     */
    public String Remark;
}
