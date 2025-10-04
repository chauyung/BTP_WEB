package nccc.btp.entity;

import java.io.Serializable;

public class NcccOuOperateDId implements Serializable {
    private String year;
    private String version;
    private String oucode;
    private String gradeId;
    private String operateItemCode;

    public NcccOuOperateDId() {}
    public NcccOuOperateDId(String year, String version, String oucode, String gradeId, String operateItemCode) {
        this.year = year; this.version = version; this.oucode = oucode; this.gradeId = gradeId; this.operateItemCode = operateItemCode;
    }

    // getters/setters
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getOucode() { return oucode; }
    public void setOucode(String oucode) { this.oucode = oucode; }
    public String getGradeId() { return gradeId; }
    public void setGradeId(String gradeId) { this.gradeId = gradeId; }
    public String getOperateItemCode() { return operateItemCode; }
    public void setOperateItemCode(String operateItemCode) { this.operateItemCode = operateItemCode; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NcccOuOperateDId)) return false;
        NcccOuOperateDId that = (NcccOuOperateDId) o;
        return year.equals(that.year) && version.equals(that.version) && oucode.equals(that.oucode)
                && gradeId.equals(that.gradeId) && operateItemCode.equals(that.operateItemCode);
    }
    @Override public int hashCode() {
        int r = year.hashCode();
        r = 31*r + version.hashCode();
        r = 31*r + oucode.hashCode();
        r = 31*r + gradeId.hashCode();
        r = 31*r + operateItemCode.hashCode();
        return r;
    }
}
