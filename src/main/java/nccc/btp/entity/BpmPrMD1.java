package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BPM_PR_M_D1")
public class BpmPrMD1 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "PR_NO", length = 12)
    private String prNo;

    @Column(name = "PR_ITEM_NO", length = 5)
    private String prItemNo;

    // 需求文件：TIMESTAMP → 用 LocalDateTime
    @Column(name = "DEMAND_DATE")
    private LocalDate demandDate;

    @Column(name = "FLOW_DEMAND_DATE", length = 1)
    private String flowDemandDate;

    @Column(name = "LOCATION", length = 50)
    private String location;

    // 2025-08-25：DECIMAL(13,2)
    @Column(name = "QTY", precision = 13, scale = 2)
    private BigDecimal qty;

    @Column(name = "PRICE", precision = 13, scale = 2)
    private BigDecimal price;

    @Column(name = "TAX", precision = 13, scale = 2)
    private BigDecimal tax;

    // 未稅金額
    @Column(name = "TOTAL", precision = 13, scale = 2)
    private BigDecimal total;

    // 預算年度 / 部門 / 項目代號
    @Column(name = "YEAR", length = 4)
    private String year;

    @Column(name = "OUCODE", length = 20)
    private String ouCode;

    @Column(name = "ACCOUNTING", length = 8)
    private String accounting;

    @Column(name = "ITEM_CODE", length = 10)
    private String itemCode;

    @Column(name = "ITEM_NAME", length = 50)
    private String itemName;

    // 主要說明（表定長度 256）
    @Column(name = "REMARK", length = 256)
    private String remark;

    @Column(name = "QTY_UNIT", length = 3)
    private String qtyUnit;

    @Column(name = "CREATED_DATE")
    private LocalDate createdDate;

    @Column(name = "UPDATE_DATE")
    private LocalDate updateDate;

    @Column(name = "CREATED_USER", length = 50)
    private String createdUser;

    @Column(name = "UPDATE_USER", length = 50)
    private String updateUser;

    public BpmPrMD1() {}

    // ======= getters / setters =======

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPrNo() { return prNo; }
    public void setPrNo(String prNo) { this.prNo = prNo; }

    public String getPrItemNo() { return prItemNo; }
    public void setPrItemNo(String prItemNo) { this.prItemNo = prItemNo; }

    public LocalDate getDemandDate() {
      return demandDate;
    }

    public void setDemandDate(LocalDate demandDate) {
      this.demandDate = demandDate;
    }

    public String getFlowDemandDate() { return flowDemandDate; }
    public void setFlowDemandDate(String flowDemandDate) { this.flowDemandDate = flowDemandDate; }

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

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getOuCode() { return ouCode; }
    public void setOuCode(String ouCode) { this.ouCode = ouCode; }

    public String getAccounting() { return accounting; }
    public void setAccounting(String accounting) { this.accounting = accounting; }

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getQtyUnit() { return qtyUnit; }
    public void setQtyUnit(String qtyUnit) { this.qtyUnit = qtyUnit; }

    public LocalDate getCreatedDate() {
      return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
      this.createdDate = createdDate;
    }

    public LocalDate getUpdateDate() {
      return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
      this.updateDate = updateDate;
    }

    public String getCreatedUser() { return createdUser; }
    public void setCreatedUser(String createdUser) { this.createdUser = createdUser; }

    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
}
