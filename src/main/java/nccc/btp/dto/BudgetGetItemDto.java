package nccc.btp.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 取得預算項目 DTO
 */
@Getter
@Setter
public class BudgetGetItemDto {
    private String code;
    private String name;
    private String deptCode;
    private String deptName;

}
