package nccc.btp.dto;

import java.math.BigDecimal;

/*
 * 所得稅參數資料模型
 */
public class IncomeTaxParameterInfo {

    /*
     * 年度
     */
    public Integer Year;

     /*
     * 代扣健保費率
     */
    public BigDecimal WithholdingTaxRate;

    /*
     * 代扣健保費率
     */
    public BigDecimal NHIRate;
    
}
