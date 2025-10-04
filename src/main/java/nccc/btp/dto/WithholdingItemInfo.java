package nccc.btp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

/*
 * 代扣繳項目資料模型
 */
public class WithholdingItemInfo {

    /*
     * 主鍵
     */
    public Long Id;

    /*
     * 所得格式代碼
     */
    public String IncomeType;

    /*
     * 所得(收入)類別
     */
    public String IncomeFromProfessionalPracticeType;

    /*
     * 扣抵名稱
     */
    public String Name;

    /*
     * 會計科目代碼
     */
    public String AccountCode;

    /*
     * 會計科目名稱
     */
    public String AccountName;

    /*
     * 預計付款日
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    public LocalDate EstimatedPaymentDate;

    /*
     * 項目內文
     */
    public String  ItemContent;

    /*
     * 金額
     */
    public BigDecimal Amount;

}
