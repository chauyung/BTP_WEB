package nccc.btp.dto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 預算分攤規則資料
 */
public class BudgetApportionmentRuleDTO {
    
    // MainDTO 內部類別
    public static class MainDTO {
        private String year;
        private String description;
        private String month;
        private String accounting;
        private String subject;
        private String belongOuCode;
        private String belongOuName;
        private boolean actualQtyFlag;
        private String operationType;
        // Getters and Setters
        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getAccounting() {
            return accounting;
        }

        public void setAccounting(String accounting) {
            this.accounting = accounting;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBelongOuCode() {
            return belongOuCode;
        }

        public void setBelongOuCode(String belongOuCode) {
            this.belongOuCode = belongOuCode;
        }

        public String getBelongOuName() {
            return belongOuName;
        }

        public void setBelongOuName(String belongOuName) {
            this.belongOuName = belongOuName;
        }

        public boolean isActualQtyFlag() {
            return actualQtyFlag;
        }

        public void setActualQtyFlag(boolean actualQtyFlag) {
            this.actualQtyFlag = actualQtyFlag;
        }

        public String getOperationType() {
            return operationType;
        }

        public void setOperationType(String operationType) {
            this.operationType = operationType;
        }
    }

    // DetailDTO 內部類別
    public static class DetailDTO {
        private String ouCode;
        private String ouName;
        private BigDecimal unitQty;
        private String remark;

        // Getters and Setters
        public String getOuCode() {
            return ouCode;
        }

        public void setOuCode(String ouCode) {
            this.ouCode = ouCode;
        }

        public String getOuName() {
            return ouName;
        }

        public void setOuName(String ouName) {
            this.ouName = ouName;
        }

        public BigDecimal getUnitQty() {
            return unitQty;
        }

        public void setUnitQty(BigDecimal unitQty) {
            this.unitQty = unitQty;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    // RequestDTO 的屬性
    private MainDTO main;
    private List<DetailDTO> details;

    // Getters and Setters
    public MainDTO getMain() {
        return main;
    }

    public void setMain(MainDTO main) {
        this.main = main;
    }

    public List<DetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<DetailDTO> details) {
        this.details = details;
    }

    public BudgetApportionmentRuleDTO() {
        // 預設建構子
        this.main = new MainDTO();
        this.details = new ArrayList<DetailDTO>();
    }
}