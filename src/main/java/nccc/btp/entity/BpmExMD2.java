package nccc.btp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BPM_EX_M_D2")
public class BpmExMD2 {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "EX_NO", length = 12)
    private String exNo;

    @Column(name = "COPY_EX_NO", length = 20)
    private String copyExNo;

    @Column(name = "ACCOUNTING", length = 17)
    private String accounting;

    @Column(name = "AMOUNT", precision = 13, scale = 2)
    private BigDecimal amount;

    @Column(name = "PAY_DATE", length = 8)
    private String payDate;

    @Column(name = "REMARK", length = 256)
    private String remark;

    @Column(name = "TEXT", length = 256)
    private String text;

    @Column(name = "CREATED_DATE")
    private LocalDate createdDate;

    @Column(name = "MODIFIED_DATE")
    private LocalDate modifiedDate;

    @Column(name = "CREATED_USER", length = 10)
    private String createdUser;

    @Column(name = "MODIFIED_USER", length = 10)
    private String modifiedUser;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getExNo() {
      return exNo;
    }

    public void setExNo(String exNo) {
      this.exNo = exNo;
    }

    public String getCopyExNo() {
      return copyExNo;
    }

    public void setCopyExNo(String copyExNo) {
      this.copyExNo = copyExNo;
    }

    public String getAccounting() {
      return accounting;
    }

    public void setAccounting(String accounting) {
      this.accounting = accounting;
    }

    public BigDecimal getAmount() {
      return amount;
    }

    public void setAmount(BigDecimal amount) {
      this.amount = amount;
    }

    public String getPayDate() {
      return payDate;
    }

    public void setPayDate(String payDate) {
      this.payDate = payDate;
    }

    public String getRemark() {
      return remark;
    }

    public void setRemark(String remark) {
      this.remark = remark;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }

    public LocalDate getCreatedDate() {
      return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
      this.createdDate = createdDate;
    }

    public LocalDate getModifiedDate() {
      return modifiedDate;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
      this.modifiedDate = modifiedDate;
    }

    public String getCreatedUser() {
      return createdUser;
    }

    public void setCreatedUser(String createdUser) {
      this.createdUser = createdUser;
    }

    public String getModifiedUser() {
      return modifiedUser;
    }

    public void setModifiedUser(String modifiedUser) {
      this.modifiedUser = modifiedUser;
    }

}
