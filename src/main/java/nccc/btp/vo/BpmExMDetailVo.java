package nccc.btp.vo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 費用申請單明細Vo
 */
public class BpmExMDetailVo {

    private Long index;

    private String exNo;

    private String exItemNo;

    private String copyExItemNo;

    private String copyExNo;

    private String certificateDate;

    private String certificateCode;

    private String certificateType;

    private String uniformNum;

    private BigDecimal applyAmount;

    private BigDecimal untaxAmount;

    private BigDecimal tax;

    private String taxCode;

    private String taxRate;

    private String itemName;

    private String itemCode;

    private String costCenter;

    private String description;

    private String itemText;

    private String deduction;

    private String hasIncomeTax;

    private String remark;

    private String carrierNumber;

    private String notificationNumber;

    private BigDecimal zeroTaxAmount;

    private BigDecimal dutyFreeAmount;

    private String chtCode;

    private BigDecimal chtCollectAmount;

    private String multiShare;

    private String year;

    private String ouCode;

    private String accounting;

    private  List<BpmExMDetailSplitVo> bpmExMDetailSplitVoList;

    private List<BpmExMDetailWHVo>  bpmExMDetailWHVoList;

    public BpmExMDetailVo(){

        this.applyAmount = new BigDecimal(0);
        this.untaxAmount = new BigDecimal(0);
        this.tax = new BigDecimal(0);
        this.zeroTaxAmount = new BigDecimal(0);
        this.dutyFreeAmount = new BigDecimal(0);
        this.chtCollectAmount = new BigDecimal(0);
        this.bpmExMDetailSplitVoList = new ArrayList<>();
        this.bpmExMDetailWHVoList = new ArrayList<>();
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getExNo() {
        return exNo;
    }

    public void setExNo(String exNo) {
        this.exNo = exNo;
    }

    public String getExItemNo() {
        return exItemNo;
    }

    public void setExItemNo(String exItemNo) {
        this.exItemNo = exItemNo;
    }

    public String getCopyExItemNo() {
        return copyExItemNo;
    }

    public void setCopyExItemNo(String copyExItemNo) {
        this.copyExItemNo = copyExItemNo;
    }

    public String getCopyExNo() {
        return copyExNo;
    }

    public void setCopyExNo(String copyExNo) {
        this.copyExNo = copyExNo;
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

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public BigDecimal getUntaxAmount() {
        return untaxAmount;
    }

    public void setUntaxAmount(BigDecimal untaxAmount) {
        this.untaxAmount = untaxAmount;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public String getHasIncomeTax() {
        return hasIncomeTax;
    }

    public void setHasIncomeTax(String hasIncomeTax) {
        this.hasIncomeTax = hasIncomeTax;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public BigDecimal getZeroTaxAmount() {
        return zeroTaxAmount;
    }

    public void setZeroTaxAmount(BigDecimal zeroTaxAmount) {
        this.zeroTaxAmount = zeroTaxAmount;
    }

    public BigDecimal getDutyFreeAmount() {
        return dutyFreeAmount;
    }

    public void setDutyFreeAmount(BigDecimal dutyFreeAmount) {
        this.dutyFreeAmount = dutyFreeAmount;
    }

    public String getChtCode() {
        return chtCode;
    }

    public void setChtCode(String chtCode) {
        this.chtCode = chtCode;
    }

    public BigDecimal getChtCollectAmount() {
        return chtCollectAmount;
    }

    public void setChtCollectAmount(BigDecimal chtCollectAmount) {
        this.chtCollectAmount = chtCollectAmount;
    }

    public String getMultiShare() {
        return multiShare;
    }

    public void setMultiShare(String multiShare) {
        this.multiShare = multiShare;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getOuCode() {
        return ouCode;
    }

    public void setOuCode(String ouCode) {
        this.ouCode = ouCode;
    }

    public String getAccounting() {
        return accounting;
    }

    public void setAccounting(String accounting) {
        this.accounting = accounting;
    }

    public List<BpmExMDetailSplitVo> getBpmExMDetailSplitVoList() {
        return bpmExMDetailSplitVoList;
    }

    public void setBpmExMDetailSplitVoList(List<BpmExMDetailSplitVo> bpmExMDetailSplitVoList) {
        this.bpmExMDetailSplitVoList = bpmExMDetailSplitVoList;
    }

    public List<BpmExMDetailWHVo> getBpmExMDetailWHVoList() {
        return bpmExMDetailWHVoList;
    }

    public void setBpmExMDetailWHVoList(List<BpmExMDetailWHVo> bpmExMDetailWHVoList) {
        this.bpmExMDetailWHVoList = bpmExMDetailWHVoList;
    }
}
