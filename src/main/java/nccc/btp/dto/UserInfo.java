package nccc.btp.dto;

/*
 * 用戶資料模型
 */
public class UserInfo {

    /**
     * 代碼
     */
    public String Code;

    /*
     * 名稱
     */
    public String Name;

    /*
     * 成本中心 / 部門
     */
    public String CostCenter;

    /*
     * 成本中心 / 部門名稱
     */
    public String CostCenterName;

    public UserInfo(String Code, String Name, String CostCenter, String CostCenterName) {

        this.Code = Code;

        this.Name = Name;

        this.CostCenter = CostCenter;

        this.CostCenterName = CostCenterName;

    }

}
