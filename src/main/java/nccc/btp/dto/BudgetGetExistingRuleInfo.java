package nccc.btp.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 預算分攤規則資料查詢條件
 */
@Getter
@Setter
public class BudgetGetExistingRuleInfo {
    
    public String year; // 年份

    public String month; // 月份

    public String accounting; // 預算項目代號

    public String belongOuCode; // 預算來源部門
}
