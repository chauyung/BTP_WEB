package nccc.btp.vo;
import java.math.BigDecimal;
import java.util.List;
/**
 * 費用申請單多重分攤vo
 */
public class BpmExMDetailSplitVo {

    private String year;

    private String accounting;

    private String ouCode;

    private BigDecimal untaxAmount;

    private String remark;

    private String description;

    private String itemText;

    private String allocationMethod;

    private List<BpmExMDetailSplitTaskItemVo> taskItemVoList;

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

    public String getOuCode() {
        return ouCode;
    }

    public void setOuCode(String ouCode) {
        this.ouCode = ouCode;
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

    public List<BpmExMDetailSplitTaskItemVo> getTaskItemVoList() {
        return taskItemVoList;
    }

    public void setTaskItemVoList(List<BpmExMDetailSplitTaskItemVo> taskItemVoList) {
        this.taskItemVoList = taskItemVoList;
    }


}
