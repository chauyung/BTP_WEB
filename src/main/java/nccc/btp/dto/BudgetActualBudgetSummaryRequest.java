package nccc.btp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.util.StringUtils;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetActualBudgetSummaryRequest {
    private String startYm;
    private String endYm;
    private List<String> deptCodes = new ArrayList<String>();
    private String accountFrom;
    private String accountTo;
    private List<Integer> expandLevels = new ArrayList<Integer>(Arrays.asList(8));

    public void validate() {
        if (!StringUtils.hasText(startYm) || !StringUtils.hasText(endYm)) throw new IllegalArgumentException("年月起訖必填");
        if (startYm.compareTo(endYm) > 0) throw new IllegalArgumentException("起值不可大於迄值");
        parseYm(startYm); parseYm(endYm);
        if (expandLevels == null || expandLevels.isEmpty()) expandLevels = new ArrayList<Integer>(Arrays.asList(8));
        for (Integer lv : expandLevels) if (!(lv == 1 || lv == 4 || lv == 6 || lv == 8)) throw new IllegalArgumentException("展開層級只支援 1/4/6/8 碼");
        if (StringUtils.hasText(accountFrom) && StringUtils.hasText(accountTo) && accountFrom.compareTo(accountTo) > 0)
            throw new IllegalArgumentException("科目代號起不可大於迄");
    }

    private static YearMonth parseYm(String yyyymm) {
        if (yyyymm == null || yyyymm.length() != 6) throw new IllegalArgumentException("年月需為 6 碼");
        int y = Integer.parseInt(yyyymm.substring(0, 4));
        int m = Integer.parseInt(yyyymm.substring(4, 6));
        return YearMonth.of(y, m);
    }

    public String getStartYm() { return startYm; }
    public void setStartYm(String v) { this.startYm = v; }
    public String getEndYm() { return endYm; }
    public void setEndYm(String v) { this.endYm = v; }
    public List<String> getDeptCodes() { return deptCodes; }
    public void setDeptCodes(List<String> v) { this.deptCodes = v; }
    public String getAccountFrom() { return accountFrom; }
    public void setAccountFrom(String v) { this.accountFrom = v; }
    public String getAccountTo() { return accountTo; }
    public void setAccountTo(String v) { this.accountTo = v; }
    public List<Integer> getExpandLevels() { return expandLevels; }
    public void setExpandLevels(List<Integer> v) { this.expandLevels = v; }
}
