// nccc/btp/dto/BudgetActualAllocationBatchRequest.java
package nccc.btp.dto;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class BudgetActualAllocationBatchRequest {
    @NotBlank
    @Pattern(regexp = "^[0-9]{6}$", message = "YYYYMM")
    private String budgetYm;
    
    @NotBlank
    @Pattern(regexp = "^[0-9A-Za-z]{1,4}$")
    private String version;

    private String operator;
    
    private Boolean forceClear;
    
    
}
