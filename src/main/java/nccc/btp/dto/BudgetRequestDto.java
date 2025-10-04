package nccc.btp.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

public class BudgetRequestDto {


    /*
    * 預算版次
    */
    @Setter
    @Getter
    public static class YearVersion {
        
        public String year; // 年份

        public String version; // 版次
    }

    /**
     * 取得部門剩餘預算
     */
    @Setter
    @Getter
    public static class GetBudgetRemainByOuCode extends YearVersion {
        public String ouCode; //組織代碼

    }

    /**
     * 取得部門預算項目
     */
    @Setter
    @Getter
    public static class GetBudgetItemByOuCode extends YearVersion {
        public String ouCode; //組織代碼

    }

    /**
    * 預算版次管理_複製版次
    */
    @Setter
    @Getter
    public static class CopyYear {

        public String sourceYear; // 來源年份

        public String sourceVersion; // 來源版次

        public String targetYear; // 目標年份

        public String targetVersion; // 目標版次
    }


    /**
     * 取得預算編列規則 DTO
     */
    @Getter
    @Setter
    public static class GetRuleDto {
        
        private String documentNumber;
        private String budgetYear;
        private String version;

    }

    /**
     * 取得預算調撥單
     */
    @Getter
    @Setter
    public static class GetBudgetAllocation {
        private String adjNo;
    }

    
    /*
    * 預算查詢
    */
    @Setter
    @Getter
    public static class QueryBudget {
        
        public String startYear; // 起_年份

        public String endYear; // 迄_年份

        public String version; // 版次

        public List<String> ouCode; // 部門

        public List<String> budgetItemCode; // 項目
    }

    /**
     * 預算明細查詢
     */
    @Getter
    @Setter
    public static class QueryBudgetTransaction {

        @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
        public LocalDate dateStart;

        @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
        public LocalDate dateEnd;

        public String budgetItemCode;

        public String ouCode;

        public String year;

        public String version;

        /**
         * 來源名稱
         */
        public List<String> sourceTypes;

        /**
         * 交易類別
         */
        public List<String> transactionTypes;
    }

    
    /**
     * 決策模型
     */
    @Setter
    @Getter
    public static class DecisionInfo {
            
        /*
        * 單號
        */
        public String No;

    }

    /**
     * 取得預算保留單
     */
    @Getter
    @Setter
    public static class QueryBudgetReserve {
        private String RsvNo;
    }

    /**
     * 查詢費用提列
     */
    @Getter
    @Setter
    public static class QueryBudgetExpense {
        private String provisionNo;
    }

    /**
     * 查詢請購單
     */
    @Getter
    @Setter
    public static class QueryBudgetRequestOrder {

        /**
         * 年份
         */
        private String year;    

    }

    /**
     * 查詢採購單
     */
    @Getter
    @Setter
    public static class QueryBudgetPurchaseOrder {

        /**
         * 年份
         */
        private String year;    

    }

    /**
     * 下載預算編列檔案
     */
    @Getter
    @Setter
    public static class DownloadBudgetRuleFile {

        private String year; // 年份
        private String department; // 部門
        private String version; // 版次
        private Boolean includeZeroAmount; // 是否包含金額為0的項目
    }
}
