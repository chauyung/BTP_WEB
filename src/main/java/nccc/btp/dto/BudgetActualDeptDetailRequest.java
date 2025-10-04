package nccc.btp.dto;

import org.springframework.util.StringUtils;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BudgetActualDeptDetailRequest {
    private String startYm;
    private String endYm;
    private List<String> deptCodes = new ArrayList<String>();
    private String accountFrom;
    private String accountTo;
    private List<Integer> expandLevels = new ArrayList<Integer>(Arrays.asList(8));
    private SourceMode sourceMode = SourceMode.BOTH;

    public void validate() {
        if (!StringUtils.hasText(startYm) || !StringUtils.hasText(endYm)) throw new IllegalArgumentException("年月起訖必填");
        if (startYm.compareTo(endYm) > 0) throw new IllegalArgumentException("起值不可大於迄值");
        parseYm(startYm); parseYm(endYm);
        if (expandLevels == null || expandLevels.isEmpty()) expandLevels = new ArrayList<Integer>(Arrays.asList(8));
        for (Integer lv : expandLevels) if (!(lv == 1 || lv == 4 || lv == 6 || lv == 8)) throw new IllegalArgumentException("展開層級只支援 1/4/6/8 碼");
    }
    private static YearMonth parseYm(String yyyymm) {
        if (yyyymm == null || yyyymm.length() != 6) throw new IllegalArgumentException("年月需為 6 碼");
        int y = Integer.parseInt(yyyymm.substring(0,4));
        int m = Integer.parseInt(yyyymm.substring(4,6));
        return YearMonth.of(y, m);
    }

    public String getStartYm() { return startYm; }
    public void setStartYm(String startYm) { this.startYm = startYm; }
    public String getEndYm() { return endYm; }
    public void setEndYm(String endYm) { this.endYm = endYm; }
    public List<String> getDeptCodes() { return deptCodes; }
    public void setDeptCodes(List<String> deptCodes) { this.deptCodes = deptCodes; }
    public String getAccountFrom() { return accountFrom; }
    public void setAccountFrom(String accountFrom) { this.accountFrom = accountFrom; }
    public String getAccountTo() { return accountTo; }
    public void setAccountTo(String accountTo) { this.accountTo = accountTo; }
    public List<Integer> getExpandLevels() { return expandLevels; }
    public void setExpandLevels(List<Integer> expandLevels) { this.expandLevels = expandLevels; }
    public SourceMode getSourceMode() { return sourceMode; }
    public void setSourceMode(SourceMode sourceMode) { this.sourceMode = sourceMode; }
}
