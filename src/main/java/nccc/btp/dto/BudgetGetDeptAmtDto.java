package nccc.btp.dto;

import java.math.BigDecimal;

/**
 * 部門剩餘預算結果
 */
public class BudgetGetDeptAmtDto {
    public String year; // 年份
    public String departmentCode; // 部門
    public String departmentName;   // 部門
    public String budgetItemCode;   // 預算項
    public String budgetItemName;   // 預算項
    public BigDecimal remainAmount;     // 剩餘金額
}
