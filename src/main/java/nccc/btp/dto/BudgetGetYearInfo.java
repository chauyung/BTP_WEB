package nccc.btp.dto;

import lombok.Getter;
import lombok.Setter;


/*
 * 預算版次管理_查詢版次資訊
 */
@Getter
@Setter
public class BudgetGetYearInfo {
    public Boolean version1Exists; // 版次1是否存在
    public Boolean version2Exists; // 版次2是否存在
}