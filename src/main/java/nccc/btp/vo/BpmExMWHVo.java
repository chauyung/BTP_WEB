package nccc.btp.vo;

import java.math.BigDecimal;

public class BpmExMWHVo {

    private String name;

    private String accounting;

    private String accountingName;

    private String payDate;

    private String itemWHText;

    private BigDecimal amount;

    public BpmExMWHVo()
    {
        this.setName("");
        this.setAccounting("");
        this.setAccountingName("");
        this.setPayDate("");
        this.setItemWHText("");
        this.setAmount(new BigDecimal("0.00"));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccounting() {
        return accounting;
    }

    public void setAccounting(String accounting) {
        this.accounting = accounting;
    }

    public String getAccountingName() {
        return accountingName;
    }

    public void setAccountingName(String accountingName) {
        this.accountingName = accountingName;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getItemWHText() {
        return itemWHText;
    }

    public void setItemWHText(String itemWHText) {
        this.itemWHText = itemWHText;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
