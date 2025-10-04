package nccc.btp.dto;

public enum SourceMode {
    BEFORE("BEFORE"), AFTER("AFTER"), BOTH("*");
    private final String dbValue;
    SourceMode(String v){ this.dbValue = v; }
    public String getDbValue(){ return dbValue; }
}
