package nccc.btp.entity;

import java.io.Serializable;
import javax.persistence.*;

@Embeddable
public class NcccBudgetActualId implements Serializable {

    @Column(name = "YYMM", length = 6, nullable = false)
    private String yymm;

    @Column(name = "VERSION", length = 1, nullable = false)
    private String version;

    @Column(name = "APPROATION", length = 10, nullable = false)
    private String approation;

    @Column(name = "OUCODE", length = 250)
    private String ouCode;

    @Column(name = "ACCOUNTING", length = 8, nullable = false)
    private String accounting;

    @Column(name = "OPERATE_ITEM_CODE", length = 10, nullable = false)
    private String operateItemCode;

    public String getYymm() { return yymm; }
    public void setYymm(String yymm) { this.yymm = yymm; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getApproation() { return approation; }
    public void setApproation(String approation) { this.approation = approation; }

    public String getOuCode() { return ouCode; }
    public void setOuCode(String ouCode) { this.ouCode = ouCode; }

    public String getAccounting() { return accounting; }
    public void setAccounting(String accounting) { this.accounting = accounting; }

    public String getOperateItemCode() { return operateItemCode; }
    public void setOperateItemCode(String operateItemCode) { this.operateItemCode = operateItemCode; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NcccBudgetActualId)) return false;
        NcccBudgetActualId other = (NcccBudgetActualId) o;
        return java.util.Objects.equals(yymm, other.yymm)
            && java.util.Objects.equals(version, other.version)
            && java.util.Objects.equals(approation, other.approation)
            && java.util.Objects.equals(ouCode, other.ouCode)
            && java.util.Objects.equals(accounting, other.accounting)
            && java.util.Objects.equals(operateItemCode, other.operateItemCode);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(yymm, version, approation, ouCode, accounting, operateItemCode);
    }
}
