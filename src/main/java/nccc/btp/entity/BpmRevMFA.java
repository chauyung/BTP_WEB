package nccc.btp.entity;

import java.math.BigDecimal;

import javax.persistence.*;

/*
 * 驗收單 - 固定資產
 */
@Entity
@Table(name = "BPM_REV_M_FA")
public class BpmRevMFA {

    /*
     * 主鍵
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bpm_rev_m_fa")
    @SequenceGenerator(name = "seq_bpm_rev_m_fa", sequenceName = "SEQ_BPM_REV_M_FA", allocationSize = 1)
    @Column(name = "ID", nullable = false, precision = 38, scale = 0)
    private Long id;

    /**
     * 驗收單單號
     */
    @Column(name = "REV_NO", length = 12)
    private String revNo;

    /*
     * 驗收單明細主鍵
     */
    @Column(name = "D_ID", nullable = false, precision = 38, scale = 0)
    private Long dId;

    /** 處理識別鍵 */
    @Column(name = "HANDLE_IDENTIFICATION_KEY", length = 36)
    private String handleIdentificationKey;

    /*
     * 多保管人
     */
    @Column(name = "MULTIPLE_CUSTODIAN")
    public Boolean multipleCustodian;

    /*
     * 保管人
     */
    @Column(name = "CUSTODIAN")
    private String custodian;

    /*
     * 置放地點
     */
    @Column(name = "LOCATION")
    private String location;

    /**
     * 耐用年限
     */
    @Column(name = "USEFUL_LIFE_YEARS", precision = 13, scale = 2)
    private BigDecimal durability;

    /*
     * 數量
     */
    @Column(name = "QTY", precision = 13, scale = 3)
    private BigDecimal qty;

      /*
     * 起始編號
     */
    @Column(name = "STARTING_NUMBER", nullable = false, precision = 5, scale = 0)
    private Integer startingNumber;

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

    public Boolean getMultipleCustodian() {
        return multipleCustodian;
    }

    public void setMultipleCustodian(Boolean multipleCustodian) {
        this.multipleCustodian = multipleCustodian;
    }

    public String getCustodian() {
        return custodian;
    }

    public void setCustodian(String custodian) {
        this.custodian = custodian;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getDurability() {
        return durability;
    }

    public void setDurability(BigDecimal durability) {
        this.durability = durability;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public String getHandleIdentificationKey() {
        return handleIdentificationKey;
    }

    public void setHandleIdentificationKey(String handleIdentificationKey) {
        this.handleIdentificationKey = handleIdentificationKey;
    }

    public Integer getStartingNumber() {
        return startingNumber;
    }

    public void setStartingNumber(Integer startingNumber) {
        this.startingNumber = startingNumber;
    }
}
