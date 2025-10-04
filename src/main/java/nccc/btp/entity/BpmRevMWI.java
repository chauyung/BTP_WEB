package nccc.btp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.*;

/**
 * 驗收單 - 代扣繳項目
 */
@Entity
@Table(name = "BPM_REV_M_WI")
public class BpmRevMWI {

    /*
     * 主鍵
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bpm_rev_m_wi")
    @SequenceGenerator(name = "seq_bpm_rev_m_wi", sequenceName = "SEQ_BPM_REV_M_WI", allocationSize = 1)
    @Column(name = "ID", nullable = false, precision = 38, scale = 0)
    private Long id;

    /**
     * 驗收單單號
     */
    @Column(name = "REV_NO", length = 12)
    private String revNo;

    /** 處理識別鍵 */
    @Column(name = "HANDLE_IDENTIFICATION_KEY", length = 36)
    private String handleIdentificationKey;

    /*
     * 名稱
     */
    @Column(name = "NAME", length = 50)
    public String name;

    /**
     * 扣繳會科代碼
     */
    @Column(name = "ACCOUNT_CODE", length = 10)
    public String accountCode;

    /**
     * 扣繳會科名稱
     */
    @Column(name = "ACCOUNT_NAME", length = 50)
    public String accountName;

    /**
     * 預計付款日
     */
    @Column(name = "ESTIMATED_PAYMENT_DATE", nullable = true)
    private LocalDate estimatedPaymentDate;

    /**
     * 項目內文
     */
    @Column(name = "ITEM_CONTENT", length = 50)
    public String itemContent;

    /**
     * 金額
     */
    @Column(name = "AMOUNT", precision = 13, scale = 3)
    public BigDecimal amount;

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

    public String getHandleIdentificationKey() {
        return handleIdentificationKey;
    }

    public void setHandleIdentificationKey(String handleIdentificationKey) {
        this.handleIdentificationKey = handleIdentificationKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public LocalDate getEstimatedPaymentDate() {
        return estimatedPaymentDate;
    }

    public void setEstimatedPaymentDate(LocalDate estimatedPaymentDate) {
        this.estimatedPaymentDate = estimatedPaymentDate;
    }

    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
