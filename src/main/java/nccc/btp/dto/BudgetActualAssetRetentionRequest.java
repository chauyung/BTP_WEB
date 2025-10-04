package nccc.btp.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class BudgetActualAssetRetentionRequest implements Serializable {
    private String year;
    private java.util.List<String> ouCodes;
    public String getYear(){ return year; }
    public void setYear(String year){ this.year = year; }
    public java.util.List<String> getOuCodes(){ return ouCodes; }
    public void setOuCodes(java.util.List<String> ouCodes){ this.ouCodes = ouCodes; }
}
