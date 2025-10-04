package nccc.btp.dto;

import java.math.BigDecimal;
import java.util.*;

/**
 * 驗收單分期驗收明細資料模型
 */
public class AcceptanceFormStagedDetailInfo {

    /**
     * 主鍵
     */
    public Long Id;

    /**
     * 期數
     */
    public Integer Period;

    /**
     * 數量金額
     */
    public BigDecimal Quantity;

    /*
     * 品號
     */
    public String ItemCode;

    /*
     * 品名
     */
    public String ItemName;

    /*
     * 分期
     */
    public List<AcceptanceFormInstallmentInfo> Installments;

        /*
     * 是否為固定資產
     */
    public Boolean IsFixedAsset;

    /*
     * 是否為端末機
     */
    public Boolean IsTerminal;
}
