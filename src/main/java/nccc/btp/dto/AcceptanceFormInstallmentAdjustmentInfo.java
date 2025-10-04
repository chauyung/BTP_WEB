package nccc.btp.dto;

import java.util.List;

/**
 * 驗收單分期調整資料模型
 */
public class AcceptanceFormInstallmentAdjustmentInfo {

    /**
     * 驗收單單號
     */
    public String AcceptanceFormNo;

    /**
     * 主鍵
     */
    public Long Id;

  /*
     * 品號
     */
    public String ItemCode;

    /*
     * 品名
     */
    public String ItemName;

    /**
     * 分期
     */
    public List<AcceptanceFormAdjustmentInstallmentInfo> Installments;
}
