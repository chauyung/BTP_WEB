package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.*;

@Entity
@IdClass(BpmTaskItem.ConfigId.class)
@Table(name = "BPM_TASK_ITEM")
public class BpmTaskItem implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "EX_NO", length = 20)
  private String exNo;

  @Id
  @Column(name = "EX_ITEM_NO", length = 20)
  private String exItemNo;

  @Id
  @Column(name = "EX_ITEM_SPLIT_NO", length = 12)
  private String exItemSplitNo;

  @Id
  @Column(name = "EX_ITEM_SPLIT_TASK_NO", length = 12)
  private String exItemSplitTaskNo;

  @Column(name = "OPERATE_ITEM_CODE", length = 20)
  private String operateItemCode;

  @Column(name = "UNTAX_AMOUNT", precision = 13, scale = 2)
  private BigDecimal untaxAmount;

  @Column(name = "OPERATE_RATIO", precision = 5, scale = 2)
  private BigDecimal operateRatio;

  @Column(name = "REMARK", length = 18)
  private String remark;

  @Column(name = "DESCRIPTION", length = 50)
  private String description;

  @Column(name = "ITEM_TEXT", length = 50)
  private String itemText;

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

  public String getExItemSplitTaskNo() {
    return exItemSplitTaskNo;
  }

  public void setExItemSplitTaskNo(String exItemSplitTaskNo) {
    this.exItemSplitTaskNo = exItemSplitTaskNo;
  }

  public String getOperateItemCode() {
    return operateItemCode;
  }

  public void setOperateItemCode(String operateItemCode) {
    this.operateItemCode = operateItemCode;
  }

  public BigDecimal getOperateRatio() {
    return operateRatio;
  }

  public void setOperateRatio(BigDecimal operateRatio) {
    this.operateRatio = operateRatio;
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

  public static class ConfigId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String exNo;
    private String exItemNo;
    private String exItemSplitNo;
    private String exItemSplitTaskNo;

    public ConfigId() {}

    public ConfigId(String exNo, String exItemNo, String exItemSplitNo, String exItemSplitTaskNo) {
      this.exNo = exNo;
      this.exItemNo = exItemNo;
      this.exItemSplitNo = exItemSplitNo;
      this.exItemSplitTaskNo = exItemSplitTaskNo;
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

    public String getExItemSplitTaskNo() {
      return exItemSplitTaskNo;
    }

    public void setExItemSplitTaskNo(String exItemSplitTaskNo) {
      this.exItemSplitTaskNo = exItemSplitTaskNo;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof BpmTaskItem.ConfigId)) return false;
      BpmTaskItem.ConfigId configId = (BpmTaskItem.ConfigId) o;
      return Objects.equals(exNo, configId.exNo) &&
              Objects.equals(exItemNo, configId.exItemNo)&&
              Objects.equals(exItemSplitNo, configId.exItemSplitNo)&&
              Objects.equals(exItemSplitTaskNo, configId.exItemSplitTaskNo);
    }

    @Override
    public int hashCode() {
      return Objects.hash(exNo, exItemNo,exItemSplitNo,exItemSplitTaskNo);
    }
  }
  
}
