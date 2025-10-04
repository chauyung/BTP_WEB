package nccc.btp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetActualOperationDto {
    private String year;
    private String version;
    private String operateItemCode;
    private String operateItem;
    private String ouCode;
    private String ouName;
    private String accounting;
    private String subject;
    private BigDecimal amount;
}
