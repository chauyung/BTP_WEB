package nccc.btp.vo;

import java.math.BigDecimal;
import java.util.List;
import nccc.btp.entity.NcccBudgetActual;

public class BudgetActualVo {
    private String yymm;
    private String version;
    private String approation;
    private String ouCode;
    private String ouName;
    private String accounting;
    private String subject;
    private String operateItemCode;
    private String operateItem;
    private BigDecimal amount;
    private String remark;

    private String accountingStart;
    private String accountingEnd;
    private List<String> operateItemCodes;
    private List<String> ouCodes;

    public BudgetActualVo() {}

    public BudgetActualVo(NcccBudgetActual e) {
    	if (e.getId() != null) {
            this.yymm = e.getId().getYymm();
            this.version = e.getId().getVersion();
            this.approation = e.getId().getApproation();
            this.ouCode = e.getId().getOuCode();
            this.accounting = e.getId().getAccounting();
            this.operateItemCode = e.getId().getOperateItemCode();
        }
        this.ouName = e.getOuName();
        this.subject = e.getSubject();
        this.operateItem = e.getOperateItem();
        this.amount = e.getAmount();
        this.remark = e.getRemark();
    }

    public String getYymm() { return yymm; }
    public void setYymm(String yymm) { this.yymm = yymm; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getApproation() { return approation; }
    public void setApproation(String approation) { this.approation = approation; }
    public String getOuCode() { return ouCode; }
    public void setOuCode(String ouCode) { this.ouCode = ouCode; }
    public String getOuName() { return ouName; }
    public void setOuName(String ouName) { this.ouName = ouName; }
    public String getAccounting() { return accounting; }
    public void setAccounting(String accounting) { this.accounting = accounting; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getOperateItemCode() { return operateItemCode; }
    public void setOperateItemCode(String operateItemCode) { this.operateItemCode = operateItemCode; }
    public String getOperateItem() { return operateItem; }
    public void setOperateItem(String operateItem) { this.operateItem = operateItem; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getAccountingStart() { return accountingStart; }
    public void setAccountingStart(String accountingStart) { this.accountingStart = accountingStart; }

    public String getAccountingEnd() { return accountingEnd; }
    public void setAccountingEnd(String accountingEnd) { this.accountingEnd = accountingEnd; }

    public List<String> getOperateItemCodes() { return operateItemCodes; }
    public void setOperateItemCodes(List<String> operateItemCodes) { this.operateItemCodes = operateItemCodes; }

    public List<String> getOuCodes() { return ouCodes; }
    public void setOuCodes(List<String> ouCodes) { this.ouCodes = ouCodes; }
}
