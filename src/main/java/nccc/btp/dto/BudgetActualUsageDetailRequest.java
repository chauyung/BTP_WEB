package nccc.btp.dto;

import java.util.List;
import lombok.Data;

@Data
public class BudgetActualUsageDetailRequest {

    private String startYm;

    private String endYm;

    private String version;

    private List<String> deptCodes;

    private String accountFrom;

    private String accountTo;
}
