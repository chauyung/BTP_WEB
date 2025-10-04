package nccc.btp.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 取得所有預算編列規則 DTO
 */
public class BudgetGetRuleListDto {

    public BudgetGetRuleSearch search;
    public List<BudgetGetRuleListData> data;


    public static class BudgetGetRuleSearch{
        @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
        public LocalDate dateFrom;

        @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
        public LocalDate dateTo;

        public String department;

        public String year;

        public String version;

        public String budgetItemCode;
    }

    public static class BudgetGetRuleListData {
        public String budgetNo;
        public String applyDate;
        public String applyUser;
        public String applyUserName;
        public String departmentCode;
        public String departmentName;
        public String budgetDepartmentCode;
        public String budgetDepartmentName;
        public String assignDate;
        public String assignUserName;
        public String reviewDate;
        public String reviewUserName;
        public String assignment;
    }
}
