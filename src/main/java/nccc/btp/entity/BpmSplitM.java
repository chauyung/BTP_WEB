package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.*;

@Entity
@IdClass(BpmSplitM.ConfigId.class)
@Table(name = "BPM_SPLIT_M")
public class BpmSplitM implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "EX_NO", length = 12)
  private String exNo;

  @Id
  @Column(name = "EX_ITEM_NO", length = 12)
  private String exItemNo;

  @Id
  @Column(name = "EX_ITEM_SPLIT_NO", length = 12)
  private String exItemSplitNo;

  @Column(name = "OUCODE", length = 20)
  private String ouCode;

  @Column(name = "YEAR", length = 4)
  private String year;

  @Column(name = "ACCOUNTING", length = 8)
  private String accounting;

  @Column(name = "UNTAX_AMOUNT", precision = 13, scale = 2)
  private BigDecimal untaxAmount;

  @Column(name = "REMARK", length = 100)
  private String remark;

  @Column(name = "DESCRIPTION", length = 50)
  private String description;

  @Column(name = "ITEM_TEXT", length = 50)
  private String itemText;

  @Column(name = "ALLOCATION_METHOD", length = 1)
  private String allocationMethod;

  @Column(name = "CREATED_DATE")
  private LocalDate createdDate;

  @Column(name = "MODIFIED_DATE")
  private LocalDate modifiedDate;

  @Column(name = "CREATED_USER", length = 50)
  private String createdUser;

  @Column(name = "MODIFIED_USER", length = 50)
  private String modifiedUser;

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

  public String getExItemSplitNo() {
    return exItemSplitNo;
  }

  public void setExItemSplitNo(String exItemSplitNo) {
    this.exItemSplitNo = exItemSplitNo;
  }

  public String getOuCode() {
    return ouCode;
  }

  public void setOuCode(String ouCode) {
    this.ouCode = ouCode;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getAccounting() {
    return accounting;
  }

  public void setAccounting(String accounting) {
    this.accounting = accounting;
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

  public String getItemText() {
    return itemText;
  }

  public void setItemText(String itemText) {
    this.itemText = itemText;
  }

  public String getAllocationMethod() {
    return allocationMethod;
  }

  public void setAllocationMethod(String allocationMethod) {
    this.allocationMethod = allocationMethod;
  }

  public static class ConfigId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String exNo;
    private String exItemNo;
    private String exItemSplitNo;

    public ConfigId() {}

    public ConfigId(String exNo, String exItemNo, String exItemSplitNo) {
      this.exNo = exNo;
      this.exItemNo = exItemNo;
      this.exItemSplitNo = exItemSplitNo;
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

    public String getExItemSplitNo() {
      return exItemSplitNo;
    }

    public void setExItemSplitNo(String exItemSplitNo) {
      this.exItemSplitNo = exItemSplitNo;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof BpmSplitM.ConfigId)) return false;
      BpmSplitM.ConfigId configId = (BpmSplitM.ConfigId) o;
      return Objects.equals(exNo, configId.exNo) &&
              Objects.equals(exItemNo, configId.exItemNo)&&
              Objects.equals(exItemSplitNo, configId.exItemSplitNo);
    }

    @Override
    public int hashCode() {
      return Objects.hash(exNo, exItemNo,exItemSplitNo);
    }
  }
}
