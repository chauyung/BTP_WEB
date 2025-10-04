package nccc.btp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import nccc.btp.vo.ProcessVo;

@Setter
@Getter
public class BudgetReserveDto extends ProcessVo {
    private String reserveNo;
    private String taskId;
    private String budgetYear;
    private String version;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDate applyDate;
    private String employeeCode;
    private String employeeName;
    private String deptCode;
    private String deptName;
    private String remark;
    private String status;
    private boolean isRetentionCostClerk;
    private List<BudgetReserveDetail> details;

    @Setter
    @Getter
    public static class BudgetReserveDetail {
        private String purchaseOrderNo;
        private String requestOrderNo;
        private String budgetItemCode;
        private String budgetItemName;
        private BigDecimal requestAmount;
        private BigDecimal purchaseAmount;
        private BigDecimal reserveAmount;
        private String purchaseRemark;
        private String reserveReason;
        private String requestDocNo;
        private String purchaseDocNo;

    }
}
