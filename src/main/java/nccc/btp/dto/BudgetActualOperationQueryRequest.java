package nccc.btp.dto;

import lombok.Data;

import java.util.List;

@Data
public class BudgetActualOperationQueryRequest {
    private String year;
    private String version;
    private List<String> operateItemCodes;
    private List<String> ouCodes;
    private String Approation;
}
