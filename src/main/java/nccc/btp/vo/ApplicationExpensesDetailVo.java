package nccc.btp.vo;

public class ApplicationExpensesDetailVo {

    /**
     * 項次
     */
    private String index;

    /**
     * 憑證日期
     */
    private String certificateDate;

    /**
     * 憑證號碼
     */
    private String certificateCode;

    /**
     * 憑證種類
     */
    private String certificateType;

    /**
     * 統一編號
     */
    private String uniformNum;

    /**
     * 申請金額
     */
    private String applyAmount;

    /**
     * 未稅金額
     */
    private String untaxAmount;

    /**
     * 稅額
     */
    private String taxAmount;

    /**
     * 零稅
     */
    private String zeroTax;

    /**
     * 免稅
     */
    private String dutyFree;

    /**
     * 品號
     */
    private String accounting;

    /**
     * 品名
     */
    private String costType;

    /**
     * 部門
     */
    private String costCenter;

    /**
     * 說明
     */
    private String description;

    /**
     * 項目內文
     */
    private String itemText;

    /**
     * 載具號碼
     */
    private String carrierNumber;

    /**
     * 通知單號碼
     */
    private  String notificationNumber;

    public ApplicationExpensesDetailVo(){

    }

    public String getIndex() {
        return index;
    }
    public void setIndex(String index) {
        this.index = index;
    }
    public String getCertificateDate() {
        return certificateDate;
    }
    public void setCertificateDate(String certificateDate) {
        this.certificateDate = certificateDate;
    }
    public String getCertificateCode() {
        return certificateCode;
    }
    public void setCertificateCode(String certificateCode) {
        this.certificateCode = certificateCode;
    }
    public String getCertificateType() {
        return certificateType;
    }
    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }
    public String getUniformNum() {
        return uniformNum;
    }
    public void setUniformNum(String uniformNum) {
        this.uniformNum = uniformNum;
    }
    public String getApplyAmount() {
        return applyAmount;
    }
    public void setApplyAmount(String applyAmount) {
        this.applyAmount = applyAmount;
    }
    public String getUntaxAmount() {
        return untaxAmount;
    }
    public void setUntaxAmount(String untaxAmount) {
        this.untaxAmount = untaxAmount;
    }
    public String getTaxAmount() {
        return taxAmount;
    }
    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }
    public String getZeroTax() {
        return zeroTax;
    }
    public void setZeroTax(String zeroTax) {
        this.zeroTax = zeroTax;
    }
    public String getDutyFree() {
        return dutyFree;
    }
    public void setDutyFree(String dutyFree) {
        this.dutyFree = dutyFree;
    }
    public String getAccounting() {
        return accounting;
    }
    public void setAccounting(String accounting) {
        this.accounting = accounting;
    }
    public String getCostType() {
        return costType;
    }
    public void setCostType(String costType) {
        this.costType = costType;
    }
    public String getCostCenter() {
        return costCenter;
    }
    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getItemText() {
        return itemText;
    }
    public void setItemText(String itemText) {
        this.itemText = itemText;
    }
    public String getCarrierNumber() {
        return carrierNumber;
    }
    public void setCarrierNumber(String carrierNumber) {
        this.carrierNumber = carrierNumber;
    }
    public String getNotificationNumber() {
        return notificationNumber;
    }
    public void setNotificationNumber(String notificationNumber) {
        this.notificationNumber = notificationNumber;
    }
}
