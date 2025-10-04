package nccc.btp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import nccc.btp.vo.ProcessVo;

/**
 * 權責分擔DTO
 */
public class BudgetResponseibilityAllocationDto {

    /**
     * 查詢權責分攤單號
     */
    @Getter
    @Setter
    public static class QueryBudgetAllocation {
        /**
         * 權責分攤單號
         */
        public String allocationNo;
    }

    @Getter
    @Setter
    public static class ReqDetailByPoNo {
        /**
         * 採購單號
         */
        public List<String> purchaseOrderNo;
    }

    /**
     * 採購單資料
     */
    @Setter
    @Getter
    public static class POData {
        /**
         * 採購單號碼
         */
        public String purchaseOrderNo;
        /**
         * 預算部門代碼
         */
        public String budgetDeptCode;
        /**
         * 預算部門名稱
         */
        public String budgetDeptName;
        /**
         * 預算會計科目代碼
         */
        public String budgetAccountCode;
        /**
         * 預算會計科目名稱
         */
        public String budgetAccountName;
        /**
         * 金額(未稅)
         */
        public BigDecimal untaxAmount;
        /**
         * 餘量未收金額
         */
        public BigDecimal remainingAmount;
        /**
         * 可權責金額
         */
        public BigDecimal allocatableAmount;
    }

    /**
     * 權責分攤資料
     */
    @Setter
    @Getter
    public static class allocationData extends ProcessVo {

        /**
         * 任務碼
         */
        public String taskId;

        /**
         * 權責分攤單號
         */
        public String allocationNo;

        /**
         * 日期
         */
        @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
        public LocalDate allocationDate;

        /**
         * 經辦人代碼
         */
        public String employeeCode;

        /**
         * 經辦人姓名
         */
        public String employeeName;

        /**
         * 部門代碼
         */
        public String deptCode;

        /**
         * 部門名稱
         */
        public String deptName;

        /**
         * 採購單號碼
         */
        public String purchaseOrderNo;

        /**
         * details
         */
        public List<DetailData> details;

    }

    /**
     * 權責分攤明細資料
     */
    @Setter
    @Getter
    public static class DetailData {
        /**
         * 採購單號碼
         */
        public String purchaseOrderNo;
        /**
         * 項次
         */
        public String purchaseItemNo;

        /**
         * 供應商名稱
         */
        public String supplierName;

        /**
         * 供應商代碼
         */
        public String supplierNo;

        /**
         * 預算部門代碼
         */
        public String budgetDeptCode;
        /**
         * 預算部門名稱
         */
        public String budgetDeptName;
        /**
         * 預算會計科目代碼
         */
        public String budgetAccountCode;
        /**
         * 預算會計科目名稱
         */
        public String budgetAccountName;
        /**
         * 金額(未稅)
         */
        public BigDecimal untaxAmount;
        /**
         * 項目內文
         */
        public String itemText;
        /**
         * 餘量未收金額
         */
        public BigDecimal remainingAmount;
        /**
         * 可權責金額
         */
        public BigDecimal allocatableAmount;

        /**
         * 權責分攤子資料
         */
        public List<allocationSubData> allocationSubData;

        /**
         * 作業清單
         */
        public List<operationItems> operationItems;
    }

    /**
     * 權責明細
     */
    @Setter
    @Getter
    public static class allocationSubData {
        /**
         * 期數
         */
        public String planNo;
        /**
         * 年度
         */
        public String year;
        /**
         * 月份
         */
        public String month;
        /**
         * 調整金額
         */
        public BigDecimal adjustAmount;
        /**
         * 權責金額
         */
        public BigDecimal allocationAmount;
        /**
         * 調整後權責金額
         */
        public BigDecimal finalAmount;
        /**
         * SAP會計文件號碼
         */
        public String sapDocNo;
        /**
         * 指定過帳日
         */
        @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
        public LocalDate postDate;
    }

    /**
     * 作業清單
     */
    @Setter
    @Getter
    public static class operationItems {
        /**
         * 作業項目代號
         */
        public String code;
        /**
         * 作業項目名稱
         */
        public String name;
        /**
         * 金額
         */
        public BigDecimal amount;
        /**
         * 比例
         */
        public BigDecimal ratio;
    }

    /**
     * 查詢權責分攤SAP資料單號
     */
    @Getter
    @Setter
    public static class QueryBudgetAllocationSAPData {
        /**
         * 年度
         */
        public String year;

        /**
         * 月份
         */
        public String month;

        /**
         * 指定過帳日
         */
        @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
        public LocalDate postDate;
    }

    /**
     * 權責明細
     */
    @Setter
    @Getter
    public static class SAPallocationSubData {

        /**
         * 期數
         */
        public String planNo;

        /**
         * 採購單明細代碼
         */
        public String poItemNo;

        /**
         * 採購單號碼
         */
        public String purchaseOrderNo;

        /**
         * 預算部門代碼
         */
        public String budgetDeptCode;

        /**
         * 預算部門名稱
         */
        public String budgetDeptName;

        /**
         * 預算會計科目代碼
         */
        public String budgetAccountCode;

        /**
         * 預算會計科目名稱
         */
        public String budgetAccountName;

        /**
         * 項目內文
         */
        public String itemText;

        /**
         * 年度
         */
        public String year;

        /**
         * 月份
         */
        public String month;

        /**
         * 調整後權責金額
         */
        public BigDecimal finalAmount;
    }

    @Setter
    @Getter
    public static class transferAllocationToSAPReq {
        public List<transferAllocationToSAPItem> items;
    }

    @Setter
    @Getter
    public static class transferAllocationToSAPItem {

        /**
         * 期數
         */
        public String planNo;

        /**
         * 採購單明細代碼
         */
        public String poItemNo;

        /**
         * 採購單單號
         */
        public String poNo;
    }
}
