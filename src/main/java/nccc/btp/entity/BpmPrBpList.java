package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "BPM_PR_BP_LIST")
public class BpmPrBpList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    /** 請購單編號 */
    @Column(name = "pr_no", length = 12)
    private String prNo;

    /** 項次 */
    @Column(name = "pr_item_no", length = 5)
    private String prItemNo;

    /** 詢價廠商 */
    @Column(name = "bp_name", length = 256)
    private String bpName;

    /** 採購金額(含稅) */
    @Column(name = "price", precision = 13, scale = 3)
    private BigDecimal price;

    /** 採購金額(未稅) */
    @Column(name = "total", precision = 13, scale = 3)
    private BigDecimal total;

    /** 採購稅額 */
    @Column(name = "tax", precision = 13, scale = 3)
    private BigDecimal tax;

    /** (附件)詢價單 */
    @Lob
    @Column(name = "attachment")
    private byte[] attachment;
    
    public BpmPrBpList() {}

    public String getPrNo() {
        return prNo;
    }

    public void setPrNo(String prNo) {
        this.prNo = prNo;
    }

    public String getPrItemNo() {
        return prItemNo;
    }

    public void setPrItemNo(String prItemNo) {
        this.prItemNo = prItemNo;
    }

    public String getBpName() {
        return bpName;
    }

    public void setBpName(String bpName) {
        this.bpName = bpName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }
}
