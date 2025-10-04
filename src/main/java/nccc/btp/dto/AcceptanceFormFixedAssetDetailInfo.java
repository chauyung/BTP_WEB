package nccc.btp.dto;

import java.math.*;
import java.util.*;

/**
 * 驗收單固定資產明細資料模型
 */
public class AcceptanceFormFixedAssetDetailInfo {

    /*
     * 主鍵
     */
    public Long Id;

    /*
     * 多保管人
     */
    public Boolean MultipleCustodian;

    /**
     * 耐用年限
     */
    public BigDecimal Durability;

    /*
     * 保管人
     */
    public String Custodian;

    /*
     * 置放地點
     */
    public String Location;

    /*
     * 數量
     */
    public BigDecimal Quantity;

    /*
     * 起始編號(端末機)
     */
    public Integer StartingNumber;

    /*
     * 端末機
     */
    public List<TerminalInfo>  Terminals;

    /*
     * 已處理
     */
    public Boolean Processed;

}
