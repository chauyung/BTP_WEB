package nccc.btp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import nccc.btp.vo.ProcessVo;

/**
 * 提列費用DTO
 */
@Setter
@Getter
public class BudgetExpenseDto extends ProcessVo {
    private String provisionNo;
    private String taskId;
    private String budgetYear;
    private String version;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDate applyDate;
    private String employeeCode;
    private String employeeName;
    private String deptCode;
    private String deptName;
    private String budgetItemCode;
    private String budgetItemName;
    private String purchaseOrderNo;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDate postingDate;
    private String sapDocumentNo;
    private String month;
    private BigDecimal totalAmount;
    private List<BudgetExpenseDetail> transferDetails;

    @Setter
    @Getter
    public static class BudgetExpenseDetail {
        private String budgetDeptCode;
        private String budgetDeptName;
        private BigDecimal provisionAmount;
        private String summary;
        private String allocateMethod;
        private List<BudgetExpenseOperationItem> operationItems;
    }

    @Setter
    @Getter
    public static class BudgetExpenseOperationItem {
        private String code;
        private String name;
        private BigDecimal amount;
        private BigDecimal ratio;
    }
}

