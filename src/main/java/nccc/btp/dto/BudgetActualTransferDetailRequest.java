package nccc.btp.dto;

import lombok.Data;
import java.util.List;

@Data
public class BudgetActualTransferDetailRequest {
    private String year;
    private String version;
    private String transferDateStart;
    private String transferDateEnd;
    private List<String> deptCodes;
    private String budgetItemCodeStart;
    private String budgetItemCodeEnd;
    private String transferOrderNo;
}
