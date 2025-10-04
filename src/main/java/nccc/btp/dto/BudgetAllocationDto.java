package nccc.btp.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import nccc.btp.vo.ProcessVo;

@Getter
@Setter
public class BudgetAllocationDto extends ProcessVo {
    private String transferNo;
    private String taskId;
    private String budgetYear;
    private String version;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDate applyDate;
    private String employeeCode;
    private String employeeName;
    private String deptCode;
    private String deptName;
    private String purchaseOrderNo;
    private String purchasePurpose;
    private String transferRemark;
    private List<TransferDetail> transferDetails;

    
    @Getter
    @Setter
    public static class TransferDetail {
        private String itemNo;
        private String outDeptCode;
        private String outDeptName;
        private String outDeptUser;
        private String outBudgetItemCode;
        private String outBudgetItemName;
        private BigDecimal outBudgetBalance;
        private String inDeptCode;
        private String inDeptName;
        private String inDeptUser;
        private String inBudgetItemCode;
        private String inBudgetItemName;
        private BigDecimal inBudgetBalance;
        private BigDecimal amount;
        private List<OperationItem> operationItems;
    }

    
    @Setter
    @Getter
    public static class OperationItem {
        private String code;
        private String name;
        private BigDecimal amount;
        private BigDecimal ratio;
    }

}
