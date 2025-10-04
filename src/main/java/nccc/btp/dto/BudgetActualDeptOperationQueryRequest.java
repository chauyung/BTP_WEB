package nccc.btp.dto;

import lombok.Data;

import java.util.List;

@Data
public class BudgetActualDeptOperationQueryRequest {
    private String yymm;
    private String version;
    private List<String> operateItemCodes;
    private List<String> ouCodes;
    private String approation;
}
