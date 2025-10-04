package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.PublicKey;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.*;

//零用金
@Entity
@IdClass(BpmExPcD1.ConfigId.class)
@Table(name = "BPM_EX_PC_D1")
public class BpmExPcD1 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "EX_NO", length = 10)
    private String exNo;

    @Id
    @Column(name = "EX_ITEM_NO", length = 10)
    private String exItemNo;

    @Column(name = "CERTIFICATE_DATE", length = 10)
    private String certificateDate;

    @Column(name = "CERTIFICATE_CODE", length = 20)
    private String certificateCode;

    @Column(name = "CERTIFICATE_TYPE", length = 10)
    private String certificateType;

    @Column(name = "UNIFORM_NUM", length = 10)
    private String uniformNum;

    @Column(name = "APPLY_AMOUNT", precision = 13, scale = 2)
    private BigDecimal applyAmount;

    @Column(name = "UNTAX_AMOUNT", precision = 13, scale = 2)
    private BigDecimal untaxAmount;

    @Column(name = "TAX", precision = 13, scale = 2)
    private BigDecimal tax;

    @Column(name = "TAX_CODE", length = 2)
    private String taxCode;

    @Column(name = "TAX_RATE", length = 10)
    private String taxRate;

    @Column(name = "COST_TYPE", length = 20)
    private String costType;

    @Column(name = "ACCOUNTING", length = 17)
    private String accounting;

    @Column(name = "COST_CENTER", length = 10)
    private String costCenter;

    @Column(name = "DESCRIPTION", length = 50)
    private String description;

    @Column(name = "ITEM_TEXT", length = 50)
    private String itemText;

    @Column(name = "HAS_INCOME_TAX", length = 1)
    private String hasIncomeTax;

    @Column(name = "DEDUCTION", length = 10)
    private String deduction;

    @Column(name = "REMARK", length = 18)
    private String remark;

    @Column(name = "FINISH", length = 1)
    private String finish;

    @Column(name = "MODIFIED_DATE")
    private LocalDate modifiedDate;

    public String getExItemNo() {
        return exItemNo;
    }

    public void setExItemNo(String exItemNo) {
        this.exItemNo = exItemNo;
    }

    public String getExNo() {
        return exNo;
    }

    public void setExNo(String exNo) {
        this.exNo = exNo;
    }

    public String getCertificateDate() {
        return certificateDate;
    }

    public void setCertificateDate(String certificateDate) {
        this.certificateDate = certificateDate;
    }

    public String getCertificateCode() {
        return certificateCode;
    }

    public void setCertificateCode(String certificateCode) {
        this.certificateCode = certificateCode;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getUniformNum() {
        return uniformNum;
    }

    public void setUniformNum(String uniformNum) {
        this.uniformNum = uniformNum;
    }

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public BigDecimal getUntaxAmount() {
        return untaxAmount;
    }

    public void setUntaxAmount(BigDecimal untaxAmount) {
        this.untaxAmount = untaxAmount;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public String getAccounting() {
        return accounting;
    }

    public void setAccounting(String accounting) {
        this.accounting = accounting;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getHasIncomeTax() {
        return hasIncomeTax;
    }

    public void setHasIncomeTax(String hasIncomeTax) {
        this.hasIncomeTax = hasIncomeTax;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
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
            if (!(o instanceof BpmExPcD1.ConfigId)) return false;
            BpmExPcD1.ConfigId configId = (BpmExPcD1.ConfigId) o;
            return Objects.equals(exNo, configId.exNo) &&
                    Objects.equals(exItemNo, configId.exItemNo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(exNo, exItemNo);
        }
    }
}
