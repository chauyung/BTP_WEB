package nccc.btp.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "BPM_PO_M_D1")
public class BpmPoMD1 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bpm_po_m_d1_seq")
    @SequenceGenerator(name = "bpm_po_m_d1_seq", sequenceName = "SEQ_BPM_PO_M_D1", allocationSize = 1)
    private Long id;

    @Column(name = "PO_NO", length = 12)
    private String poNo;   // 採購單號

    @Column(name = "PO_ITEM_NO", length = 5)
    private String poItemNo;  // 項次

    @Column(name = "ITEM_CODE", length = 10)
    private String itemCode;  // 品號

    @Column(name = "REMARK", length = 256)
    private String remark;

    @Column(name = "DEMAND_DATE")
    private LocalDateTime demandDate;   // 需用日期

    @Column(name = "LOCATION", length = 50)
    private String location;

    @Column(name = "QTY", precision = 13, scale = 2)
    private BigDecimal qty;

    @Column(name = "PRICE", precision = 13, scale = 2)
    private BigDecimal price;

    @Column(name = "TAX", precision = 13, scale = 2)
    private BigDecimal tax;

    @Column(name = "TOTAL", precision = 13, scale = 2)
    private BigDecimal total;   // 未稅金額

    @Column(name = "BP_NO", length = 256)
    private String bpNo;   // 供應商代號

    @Column(name = "BP_NAME", length = 256)
    private String bpName; // 供應商名稱

    @Column(name = "BP_QTY", precision = 13, scale = 2)
    private BigDecimal bpQty;

    @Column(name = "BP_PRICE", precision = 13, scale = 2)
    private BigDecimal bpPrice;

    @Column(name = "BP_TOTAL", precision = 13, scale = 2)
    private BigDecimal bpTotal;   // 採購金額(未稅)

    @Column(name = "BP_TAX", precision = 13, scale = 2)
    private BigDecimal bpTax;     // 採購稅額

    @Column(name = "YEAR", length = 4)
    private String year;   // 預算年度

    @Column(name = "OUCODE", length = 20)
    private String ouCode; // 預算部門

    @Column(name = "ACCOUNTING", length = 8)
    private String accounting;  // 預算項目代號

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;

    @Column(name = "CREATED_USER", length = 50)
    private String createdUser;

    @Column(name = "MODIFIED_USER", length = 50)
    private String modifiedUser;

    // === Getter / Setter ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPoNo() { return poNo; }
    public void setPoNo(String poNo) { this.poNo = poNo; }

    public String getPoItemNo() { return poItemNo; }
    public void setPoItemNo(String poItemNo) { this.poItemNo = poItemNo; }

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public LocalDateTime getDemandDate() { return demandDate; }
    public void setDemandDate(LocalDateTime demandDate) { this.demandDate = demandDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public BigDecimal getQty() { return qty; }
    public void setQty(BigDecimal qty) { this.qty = qty; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getTax() { return tax; }
    public void setTax(BigDecimal tax) { this.tax = tax; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getBpNo() { return bpNo; }
    public void setBpNo(String bpNo) { this.bpNo = bpNo; }

    public String getBpName() { return bpName; }
    public void setBpName(String bpName) { this.bpName = bpName; }

    public BigDecimal getBpQty() { return bpQty; }
    public void setBpQty(BigDecimal bpQty) { this.bpQty = bpQty; }

    public BigDecimal getBpPrice() { return bpPrice; }
    public void setBpPrice(BigDecimal bpPrice) { this.bpPrice = bpPrice; }

    public BigDecimal getBpTotal() { return bpTotal; }
    public void setBpTotal(BigDecimal bpTotal) { this.bpTotal = bpTotal; }

    public BigDecimal getBpTax() { return bpTax; }
    public void setBpTax(BigDecimal bpTax) { this.bpTax = bpTax; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getOuCode() { return ouCode; }
    public void setOuCode(String ouCode) { this.ouCode = ouCode; }

    public String getAccounting() { return accounting; }
    public void setAccounting(String accounting) { this.accounting = accounting; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(LocalDateTime modifiedDate) { this.modifiedDate = modifiedDate; }

    public String getCreatedUser() { return createdUser; }
    public void setCreatedUser(String createdUser) { this.createdUser = createdUser; }

    public String getModifiedUser() { return modifiedUser; }
    public void setModifiedUser(String modifiedUser) { this.modifiedUser = modifiedUser; }
}
