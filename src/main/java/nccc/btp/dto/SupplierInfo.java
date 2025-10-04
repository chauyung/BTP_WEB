package nccc.btp.dto;

/**
 * 供應商資料模型
 */
public class SupplierInfo {

    /**
     * 國家代碼
     */
    public String CountryCode;

    /*
     * 代碼
     */
    public String Code;

    /**
     * 名稱
     */
    public String Name;

    /*
     * 統一編號
     */
    public String BusinessIdNumber;

    public SupplierInfo(String CountryCode, String Code, String Name, String BusinessIdNumber){

        this.CountryCode = CountryCode;

        this.Code = Code;

        this.Name = Name;

        this.BusinessIdNumber = BusinessIdNumber;
        
    }
    
}
