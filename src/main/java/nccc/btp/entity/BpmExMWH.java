package nccc.btp.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@IdClass(BpmExMWH.ConfigId.class)
@Table(name = "BPM_EX_M_WH")
public class BpmExMWH implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "EX_NO", length = 10)
    private String exNo;

    @Id
    @Column(name = "EX_WH_NO", length = 10)
    private String exWhNo;

    @Column(name = "NAME",  length = 20)
    private String name;

    @Column(name = "ACCOUNTING",  length = 8)
    private String accounting;

    @Column(name = "ACCOUNTING_NAME",  length = 30)
    private String accountingName;

    @Column(name = "AMOUNT", precision = 13, scale = 2)
    private BigDecimal amount;

    @Column(name = "PAY_DATE",  length = 10)
    private String payDate;

    @Column(name = "ITEM_WH_TEXT",  length = 30)
    private String itemWHText;

    public String getExNo() {
        return exNo;
    }

    public void setExNo(String exNo) {
        this.exNo = exNo;
    }

    public String getExWhNo() {
        return exWhNo;
    }

    public void setExWhNo(String exWhNo) {
        this.exWhNo = exWhNo;
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

    public String getItemWHText() {
        return itemWHText;
    }

    public void setItemWHText(String itemWHText) {
        this.itemWHText = itemWHText;
    }

    public static class ConfigId implements Serializable {
        private static final long serialVersionUID = 1L;

        private String exNo;
        private String exWhNo;

        public ConfigId() {}

        public ConfigId(String exNo, String exWhNo) {
            this.exNo = exNo;
            this.exWhNo = exWhNo;
        }

        public String getExNo() {
            return exNo;
        }

        public void setExNo(String exNo) {
            this.exNo = exNo;
        }

        public String getExWhNo() {
            return exWhNo;
        }

        public void setExWhNo(String exWhNo) {
            this.exWhNo = exWhNo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BpmExMWH.ConfigId)) return false;
            BpmExMWH.ConfigId configId = (BpmExMWH.ConfigId) o;
            return Objects.equals(exNo, configId.exNo) &&
                    Objects.equals(exWhNo, configId.exWhNo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(exNo, exWhNo);
        }
    }
}
