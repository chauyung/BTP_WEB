package nccc.btp.dto;

import java.math.BigDecimal;

/**
 * 憑證類型資料模型
 */
public class ReceiptTypeInfo {

    public ReceiptTypeInfo(String Id, String SerialNumber, String Name, String Illustrate, String MWSKZ,
            BigDecimal TaxRate, Boolean Zguifg, Boolean IncomeTax) {
        this.Id = Id;

        this.SerialNumber = SerialNumber;

        this.Name = Name;

        this.Illustrate = Illustrate;

        this.MWSKZ = MWSKZ;

        this.TaxRate = TaxRate;

        this.Zguifg = Zguifg;

        this.IncomeTax = IncomeTax;
    }

    /*
     * 主鍵
     */
    public String Id;

    /**
     * 序號
     */
    public String SerialNumber;

    /*
     * 名稱
     */
    public String Name;

    /**
     * 說明
     */
    public String Illustrate;

    /**
     * 稅碼
     */
    public String MWSKZ;

    /*
     * 稅率
     */
    public BigDecimal TaxRate;

    /**
     * 扣抵否
     */
    public Boolean Zguifg;

    /*
     * 是否納入所得稅
     */
    public Boolean IncomeTax;

}
