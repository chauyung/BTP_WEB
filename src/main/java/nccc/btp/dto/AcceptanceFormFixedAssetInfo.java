package nccc.btp.dto;

import java.math.BigDecimal;
import java.util.*;

/**
 * 驗收單固定資產資料模型
 */
public class AcceptanceFormFixedAssetInfo {

    /*
     * 主鍵
     */
    public Long Id;

    /*
     * 是否為端末機
     */
    public Boolean IsTerminal;

    /*
     * 品號
     */
    public String ItemCode;

    /*
     * 品名
     */
    public String ItemName;

    /**
     * 取得固定資產編號
     */
    public Boolean TakeFixedAssetNo;

    /**
     * 固定資產編號
     */
    public String FixedAssetNo;

    /**
     * 耐用年限
     */
    public BigDecimal Durability;

    /*
     * 資產
     */
    public List<AcceptanceFormFixedAssetDetailInfo> Assets;

}
