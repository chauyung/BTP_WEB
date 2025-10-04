package nccc.btp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 預算編列規則 DTO
 */
public class BudgetRuleDTO {
    private String documentNumber;
    private String budgetYear;
    private String version;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDate allocationDate;
    private String employeeCode;
    private String employeeName;
    private String departmentCode;
    private String departmentName;
    private String budgetDepartment;
    private String assignment;
    private Integer count;
    private List<BudgetDetail> budgetDetails;

    public BudgetRuleDTO() {
        this.setCount(0);
    }

    // Getters and Setters
    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getBudgetYear() {
        return budgetYear;
    }

    public void setBudgetYear(String budgetYear) {
        this.budgetYear = budgetYear;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDate getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(LocalDate allocationDate) {
        this.allocationDate = allocationDate;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getBudgetDepartment() {
        return budgetDepartment;
    }

    public void setBudgetDepartment(String budgetDepartment) {
        this.budgetDepartment = budgetDepartment;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<BudgetDetail> getBudgetDetails() {
        return budgetDetails;
    }

    public void setBudgetDetails(List<BudgetDetail> budgetDetails) {
        this.budgetDetails = budgetDetails;
    }

    public static class BudgetDetail {
        private boolean selected;
        private String seqNo;   
        private String budgetItemCode;
        private String budgetItemName;
        private BigDecimal budgetAmount;
        private String uploaderName;

        @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
        private LocalDate uploadDate;
        private String remark;
        private List<OperationItem> operationItems;

        // Getters and Setters
        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public String getSeqNo() {
            return seqNo;
        }

        public void setSeqNo(String seqNo) {
            this.seqNo = seqNo;
        }

        public String getBudgetItemCode() {
            return budgetItemCode;
        }

        public void setBudgetItemCode(String budgetItemCode) {
            this.budgetItemCode = budgetItemCode;
        }

        public String getBudgetItemName() {
            return budgetItemName;
        }

        public void setBudgetItemName(String budgetItemName) {
            this.budgetItemName = budgetItemName;
        }

        public BigDecimal getBudgetAmount() {
            return budgetAmount;
        }

        public void setBudgetAmount(BigDecimal budgetAmount) {
            this.budgetAmount = budgetAmount;
        }

        public String getUploaderName() {
            return uploaderName;
        }

        public void setUploaderName(String uploaderName) {
            this.uploaderName = uploaderName;
        }

        public LocalDate getUploadDate() {
            return uploadDate;
        }

        public void setUploadDate(LocalDate uploadDate) {
            this.uploadDate = uploadDate;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public List<OperationItem> getOperationItems() {
            return operationItems;
        }

        public void setOperationItems(List<OperationItem> operationItems) {
            this.operationItems = operationItems;
        }
    }

    public static class OperationItem {
        private String code;
        private String name;
        private BigDecimal amount;
        private BigDecimal ratio;

        // Getters and Setters
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getRatio() {
            return ratio;
        }

        public void setRatio(BigDecimal ratio) {
            this.ratio = ratio;
        }
    }
}