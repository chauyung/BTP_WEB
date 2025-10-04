package nccc.btp.dto;

import java.math.BigDecimal;

public class ChtAccTaxCoCostDto {

    private String accounting;

    private String costCenter;

    private BigDecimal unTaxAmount;

    private BigDecimal zeroTaxAmount;

    private BigDecimal dutyFreeAmount;

    public ChtAccTaxCoCostDto()
    {
        this.setAccounting("");

        this.setCostCenter("");

        this.setUnTaxAmount(BigDecimal.ZERO);

        this.setDutyFreeAmount(BigDecimal.ZERO);

        this.setZeroTaxAmount(BigDecimal.ZERO);

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

    public BigDecimal getUnTaxAmount() {
        return unTaxAmount;
    }

    public void setUnTaxAmount(BigDecimal unTaxAmount) {
        this.unTaxAmount = unTaxAmount;
    }

    public BigDecimal getZeroTaxAmount() {
        return zeroTaxAmount;
    }

    public void setZeroTaxAmount(BigDecimal zeroTaxAmount) {
        this.zeroTaxAmount = zeroTaxAmount;
    }

    public BigDecimal getDutyFreeAmount() {
        return dutyFreeAmount;
    }

    public void setDutyFreeAmount(BigDecimal dutyFreeAmount) {
        this.dutyFreeAmount = dutyFreeAmount;
    }
}
