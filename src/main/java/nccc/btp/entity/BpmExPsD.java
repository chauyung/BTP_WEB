package nccc.btp.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

// 費用申請單人員明細(董監事及研發委員用)
@Entity
@IdClass(BpmExPsD.ConfigId.class)
@Table(name = "BPM_EX_PS_D")
public class BpmExPsD implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "EX_NO", length = 10)
    private String exNo;

    @Id
    @Column(name = "EX_ITEM_NO", length = 10)
    private String exItemNo;

    @Column(name = "PPL_ID", length = 10)
    private String pplId;

    // 姓名
    @Column(name = "NAME", length = 50)
    private String name;

    // 是否為公務員
    @Column(name = "IS_CIVIL", length = 1)
    private String isCivil;

    // 職稱
    @Column(name = "JOB_TITLE", length = 20)
    private String jobTitle;

    // 單位與部門
    @Column(name = "UNIT", length = 50)
    private String unit;

    @Column(name = "AMOUNT_1", precision = 13, scale = 2)
    private BigDecimal amount1;

    @Column(name = "AMOUNT_2", precision = 13, scale = 2)
    private BigDecimal amount2;

    @Column(name = "AMOUNT_3", precision = 13, scale = 2)
    private BigDecimal amount3;

    @Column(name = "AMOUNT_4", precision = 13, scale = 2)
    private BigDecimal amount4;

    @Column(name = "TOTAL_AMOUNT", precision = 13, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "YEAR", length = 4)
    private String year;

    @Column(name = "OUCODE", length = 10)
    private String ouCode;

    @Column(name = "ACCOUNTING_1", length = 8)
    private String accounting1;

    @Column(name = "ACCOUNTING_2", length = 8)
    private String accounting2;

    @Column(name = "ACCOUNTING_3", length = 8)
    private String accounting3;

    @Column(name = "ACCOUNTING_4", length = 8)
    private String accounting4;

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

    public String getPplId() {
        return pplId;
    }

    public void setPplId(String pplId) {
        this.pplId = pplId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsCivil() {
        return isCivil;
    }

    public void setIsCivil(String isCivil) {
        this.isCivil = isCivil;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getAmount1() {
        return amount1;
    }

    public void setAmount1(BigDecimal amount1) {
        this.amount1 = amount1;
    }

    public BigDecimal getAmount2() {
        return amount2;
    }

    public void setAmount2(BigDecimal amount2) {
        this.amount2 = amount2;
    }

    public BigDecimal getAmount3() {
        return amount3;
    }

    public void setAmount3(BigDecimal amount3) {
        this.amount3 = amount3;
    }

    public BigDecimal getAmount4() {
        return amount4;
    }

    public void setAmount4(BigDecimal amount4) {
        this.amount4 = amount4;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getOuCode() {
        return ouCode;
    }

    public void setOuCode(String ouCode) {
        this.ouCode = ouCode;
    }

    public String getAccounting1() {
        return accounting1;
    }

    public void setAccounting1(String accounting1) {
        this.accounting1 = accounting1;
    }

    public String getAccounting2() {
        return accounting2;
    }

    public void setAccounting2(String accounting2) {
        this.accounting2 = accounting2;
    }

    public String getAccounting3() {
        return accounting3;
    }

    public void setAccounting3(String accounting3) {
        this.accounting3 = accounting3;
    }

    public String getAccounting4() {
        return accounting4;
    }

    public void setAccounting4(String accounting4) {
        this.accounting4 = accounting4;
    }

    public static class ConfigId implements Serializable {
        private static final long serialVersionUID = 1L;

        private String exNo;
        private String exItemNo;

        public ConfigId() {}

        public ConfigId(String exNo, String exItemNo) {
            this.exNo = exNo;
            this.exItemNo = exItemNo;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BpmExPsD.ConfigId)) return false;
            BpmExPsD.ConfigId configId = (BpmExPsD.ConfigId) o;
            return Objects.equals(exNo, configId.exNo) &&
                    Objects.equals(exItemNo, configId.exItemNo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(exNo, exItemNo);
        }
    }
}
