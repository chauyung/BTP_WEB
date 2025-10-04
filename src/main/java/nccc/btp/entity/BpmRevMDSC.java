package nccc.btp.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 驗收單明細 - 分期
 */
@Entity
@Table(name = "BPM_REV_M_D_SC")
public class BpmRevMDSC {

    /*
     * 主鍵
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bpm_rev_m_d_sc")
    @SequenceGenerator(name = "seq_bpm_rev_m_d_sc", sequenceName = "SEQ_BPM_REV_M_D_SC", allocationSize = 1)
    @Column(name = "ID", nullable = false, precision = 38, scale = 0)
    private Long id;

    /**
     * 驗收單單號
     */
    @Column(name = "REV_NO", length = 12, nullable = false)
    private String revNo;

    /*
     * 驗收單明細主鍵
     */
    @Column(name = "D_ID", nullable = false, precision = 38, scale = 0)
    private Long dId;

    /** 處理識別鍵 */
    @Column(name = "HANDLE_IDENTIFICATION_KEY", length = 36, nullable = true)
    private String handleIdentificationKey;

    /**
     * 期數
     */
    @Column(name = "PERIOD", length = 3)
    private String period;

    /*
     * 數量 / 金額(原始)
     */
    @Column(name = "ORIGINAL_QTY", precision = 13, scale = 3)
    private BigDecimal originalQty;

    /*
     * 數量 / 金額
     */
    @Column(name = "QTY", precision = 13, scale = 3)
    private BigDecimal qty;

        /*
     * 餘料不驗數量/金額
     */
    @Column(name = "not_Accepted_Qty", precision = 13, scale = 3)
    private BigDecimal notAcceptedQty;

    /*
     * 餘料不驗
     */
    @Column(name = "Not_Accepted")
    private Boolean notAccepted;

    /**
     * 保留資產
     */
    @Column(name = "RETAINED_ASSETS")
    private Boolean retainedAssets;
    /*
     * 驗收程序
     */
    @Column(name = "ACCEPTANCE_PROCEDURE", length = 50)
    private String acceptanceProcedure;

    /*
     * 驗收結果
     */
    @Column(name = "ACCEPTANCE_RESULT", length = 50)
    private String acceptanceResult;

    /**
     * 交貨日期/驗收通知日期
     */
    @Column(name = "DELIVERY_DATE", nullable = true)
    private LocalDate deliveryDate;

    /**
     * 展延驗收日期
     */
    @Column(name = "EXTENSION_DATE", nullable = true)
    private LocalDate extensionDate;

    /*
     * 展延核准文號
     */
    @Column(name = "EXTENSIONDOCUMENT_NO", length = 10)
    private String extensiondocumentNo;

    /*
     * 備註
     */
    @Column(name = "REMARK", length = 50)
    private String remark;

    /*
     * 憑證日期
     */
    @Column(name = "CERTIFICATE_DATE", nullable = true)
    private LocalDate certificateDate;

    /*
     * 憑證號碼
     */
    @Column(name = "CERTIFICATE_NO", length = 10)
    private String certificateNo;

    /*
     * 憑證種類
     */
    @Column(name = "CERTIFICATE_TYPE", length = 10)
    private String certificateType;

    /*
     * 賣方統一編號
     */
    @Column(name = "BUSINESS_REGISTRATION_NUMBER", length = 8)
    private String businessRegistrationNumber;

    /**
     * 待驗收金額
     */
    @Column(name = "ACCEPTED_AMOUNT", precision = 13, scale = 3)
    private BigDecimal acceptedAmount;

    /**
     * 申請金額
     */
    @Column(name = "APPLICATION_AMOUNT", precision = 13, scale = 3)
    private BigDecimal applicationAmount;

    /**
     * 稅率
     */
    @Column(name = "TAX_RATE", precision = 13, scale = 3)
    private BigDecimal taxRate;

    /**
     * 未稅金額
     */
    @Column(name = "EXCLUDING_TAX_AMOUNT", precision = 13, scale = 3)
    private BigDecimal excludingTaxAmount;

    /**
     * 稅額
     */
    @Column(name = "TAX", precision = 13, scale = 3)
    private BigDecimal tax;

    /**
     * 零稅
     */
    @Column(name = "ZERO_TAX", precision = 13, scale = 3)
    private BigDecimal zeroTax;

    /**
     * 免稅
     */
    @Column(name = "TAX_FREE", precision = 13, scale = 3)
    private BigDecimal taxFree;

    /*
     * 預算部門
     */
    @Column(name = "BUDGET_DEPARTMENT", length = 10)
    private String budgetDepartment;

    /*
     * 品號
     */
    @Column(name = "ITEM_CODE", length = 17)
    private String itemCode;

    /*
     * 品名
     */
    @Column(name = "ITEM_NAME", length = 17)
    private String itemName;

    /*
     * 指定付款日期
     */
    @Column(name = "SPECIFYPAYMENT_DATE", nullable = true)
    private LocalDate specifypaymentDate;

    /*
     * 指定過帳日期
     */
    @Column(name = "SPECIFYPOSTING_DATE", nullable = true)
    private LocalDate specifypostingDate;

    /*
     * 項目內文
     */
    @Column(name = "ITEM_CONTENT", length = 50)
    private String item_content;

    /**
     * 預付設備款
     */
    @Column(name = "PREPAYMENTS_EQUIPMENT")
    private Boolean prepaymentsEquipment;

    /**
     * 指定結案/最終驗收
     */
    @Column(name = "DESIGNATED_COMPLETION")
    private Boolean designatedCompletion;

    /**
     * 扣抵
     */
    @Column(name = "DEDUCTION")
    private Boolean deduction;

        /*
     * 是否代扣繳
     */
    @Column(name = "WITHHOLDING")
    private Boolean withholding;

    /*
     * 備忘
     */
    @Column(name = "MEMO", length = 50)
    private String memo;

    /*
     * 端末機(起)
     */
    @Column(name = "TERMINAL_MACHINE_BEGIN", nullable = false, precision = 5, scale = 0)
    private Integer terminalMachineBegin;

    /*
     * 端末機(迄)
     */
    @Column(name = "TERMINAL_MACHINE_END", nullable = false, precision = 5, scale = 0)
    private Integer terminalMachineEnd;

    /*
     * 已處理
     */
    @Column(name = "PROCESSED")
    private Boolean processed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRevNo() {
        return revNo;
    }

    public void setRevNo(String revNo) {
        this.revNo = revNo;
    }

    public Long getdId() {
        return dId;
    }

    public void setdId(Long dId) {
        this.dId = dId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Boolean getRetainedAssets() {
        return retainedAssets;
    }

    public void setRetainedAssets(Boolean retainedAssets) {
        this.retainedAssets = retainedAssets;
    }

    public String getAcceptanceProcedure() {
        return acceptanceProcedure;
    }

    public void setAcceptanceProcedure(String acceptanceProcedure) {
        this.acceptanceProcedure = acceptanceProcedure;
    }

    public String getAcceptanceResult() {
        return acceptanceResult;
    }

    public void setAcceptanceResult(String acceptanceResult) {
        this.acceptanceResult = acceptanceResult;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public LocalDate getExtensionDate() {
        return extensionDate;
    }

    public void setExtensionDate(LocalDate extensionDate) {
        this.extensionDate = extensionDate;
    }

    public String getExtensiondocumentNo() {
        return extensiondocumentNo;
    }

    public void setExtensiondocumentNo(String extensiondocumentNo) {
        this.extensiondocumentNo = extensiondocumentNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDate getCertificateDate() {
        return certificateDate;
    }

    public void setCertificateDate(LocalDate certificateDate) {
        this.certificateDate = certificateDate;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getBusinessRegistrationNumber() {
        return businessRegistrationNumber;
    }

    public void setBusinessRegistrationNumber(String businessRegistrationNumber) {
        this.businessRegistrationNumber = businessRegistrationNumber;
    }

    public BigDecimal getAcceptedAmount() {
        return acceptedAmount;
    }

    public void setAcceptedAmount(BigDecimal acceptedAmount) {
        this.acceptedAmount = acceptedAmount;
    }

    public BigDecimal getApplicationAmount() {
        return applicationAmount;
    }

    public void setApplicationAmount(BigDecimal applicationAmount) {
        this.applicationAmount = applicationAmount;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getExcludingTaxAmount() {
        return excludingTaxAmount;
    }

    public void setExcludingTaxAmount(BigDecimal excludingTaxAmount) {
        this.excludingTaxAmount = excludingTaxAmount;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getZeroTax() {
        return zeroTax;
    }

    public void setZeroTax(BigDecimal zeroTax) {
        this.zeroTax = zeroTax;
    }

    public BigDecimal getTaxFree() {
        return taxFree;
    }

    public void setTaxFree(BigDecimal taxFree) {
        this.taxFree = taxFree;
    }

    public String getBudgetDepartment() {
        return budgetDepartment;
    }

    public void setBudgetDepartment(String budgetDepartment) {
        this.budgetDepartment = budgetDepartment;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public LocalDate getSpecifypaymentDate() {
        return specifypaymentDate;
    }

    public void setSpecifypaymentDate(LocalDate specifypaymentDate) {
        this.specifypaymentDate = specifypaymentDate;
    }

    public LocalDate getSpecifypostingDate() {
        return specifypostingDate;
    }

    public void setSpecifypostingDate(LocalDate specifypostingDate) {
        this.specifypostingDate = specifypostingDate;
    }

    public String getItem_content() {
        return item_content;
    }

    public void setItem_content(String item_content) {
        this.item_content = item_content;
    }

    public Boolean getPrepaymentsEquipment() {
        return prepaymentsEquipment;
    }

    public void setPrepaymentsEquipment(Boolean prepaymentsEquipment) {
        this.prepaymentsEquipment = prepaymentsEquipment;
    }

    public Boolean getDesignatedCompletion() {
        return designatedCompletion;
    }

    public void setDesignatedCompletion(Boolean designatedCompletion) {
        this.designatedCompletion = designatedCompletion;
    }

    public Boolean getDeduction() {
        return deduction;
    }

    public void setDeduction(Boolean deduction) {
        this.deduction = deduction;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getTerminalMachineBegin() {
        return terminalMachineBegin;
    }

    public void setTerminalMachineBegin(Integer terminalMachineBegin) {
        this.terminalMachineBegin = terminalMachineBegin;
    }

    public Integer getTerminalMachineEnd() {
        return terminalMachineEnd;
    }

    public void setTerminalMachineEnd(Integer terminalMachineEnd) {
        this.terminalMachineEnd = terminalMachineEnd;
    }

    public BigDecimal getOriginalQty() {
        return originalQty;
    }

    public void setOriginalQty(BigDecimal originalQty) {
        this.originalQty = originalQty;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public BigDecimal getNotAcceptedQty() {
        return notAcceptedQty;
    }

    public void setNotAcceptedQty(BigDecimal notAcceptedQty) {
        this.notAcceptedQty = notAcceptedQty;
    }

    public Boolean getNotAccepted() {
        return notAccepted;
    }

    public void setNotAccepted(Boolean notAccepted) {
        this.notAccepted = notAccepted;
    }

    public String getHandleIdentificationKey() {
        return handleIdentificationKey;
    }

    public void setHandleIdentificationKey(String handleIdentificationKey) {
        this.handleIdentificationKey = handleIdentificationKey;
    }

    public Boolean getWithholding() {
        return withholding;
    }

    public void setWithholding(Boolean withholding) {
        this.withholding = withholding;
    }
}
