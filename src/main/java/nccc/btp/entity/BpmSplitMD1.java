package nccc.btp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;

@Entity
@Table(name = "BPM_SPLIT_M_D1")
public class BpmSplitMD1 {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bpm_split_m_d1")
  @SequenceGenerator(name = "seq_bpm_split_m_d1", sequenceName = "SEQ_BPM_SPLIT_M_D1", allocationSize = 1)
  @Column(name = "ID", nullable = false, precision = 38, scale = 0)
  private Long id;

  /** 外鍵，對應到 BPM_SPLIT_M.ID */
  @Column(name = "M_ID", nullable = false)
  private Long mId;

  @Column(name = "ACCOUNTING", length = 17)
  private String accounting;

  @Column(name = "COST_CENTER", length = 10)
  private String costCenter;

  @Column(name = "ITEM_TEXT", length = 50)
  private String itemText;

  @Column(name = "UNTAX_AMOUNT", precision = 13, scale = 2)
  private BigDecimal untaxAmount;

  @Column(name = "REMARK", length = 18)
  private String remark;

  @Column(name = "DESCRIPTION", length = 50)
  private String description;

  @Column(name = "CREATED_DATE")
  private LocalDate createdDate;

  @Column(name = "MODIFIED_DATE")
  private LocalDate modifiedDate;

  @Column(name = "CREATED_USER", length = 50)
  private String createdUser;

  @Column(name = "MODIFIED_USER", length = 50)
  private String modifiedUser;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getMId() {
    return mId;
  }

  public void setMId(Long mId) {
    this.mId = mId;
  }

  public String getAccounting() {
    return accounting;
  }

  public void setAccounting(String accounting) {
    this.accounting = accounting;
  }

  public String getCostCenter() {
    return costCenter;
  }

  public void setCostCenter(String costCenter) {
    this.costCenter = costCenter;
  }

  public String getItemText() {
    return itemText;
  }

  public void setItemText(String itemText) {
    this.itemText = itemText;
  }

  public BigDecimal getUntaxAmount() {
    return untaxAmount;
  }

  public void setUntaxAmount(BigDecimal untaxAmount) {
    this.untaxAmount = untaxAmount;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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
