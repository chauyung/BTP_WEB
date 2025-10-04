package nccc.btp.entity;

import javax.persistence.*;
import java.time.*;
import java.math.*;

/**
 * 驗收單 - 權責分攤
 */
@Entity
@Table(name = "BPM_REV_M_AR")
public class BpmRevMAR {

    /*
     * 主鍵
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bpm_rev_m_ar")
    @SequenceGenerator(name = "seq_bpm_rev_m_ar", sequenceName = "SEQ_BPM_REV_M_AR", allocationSize = 1)
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
    @Column(name = "D_ID")
    private Long dId;

    /**
     * 月份
     */
    @Column(name = "MONTH")
    private LocalDate month;

    /**
     * 數量 / 金額
     */
    @Column(name = "quantity")
    private BigDecimal quantity;

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

    public LocalDate getMonth() {
        return month;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
