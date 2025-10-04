package nccc.btp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BudgetQueryDto {

    @Getter
    @Setter
    public static class BudgetItem{
        /**
         * 年度
         */
        private String year;
        /**
         * 版次
         */
        private String version;
        /**
         * 部門
         */
        private String departmentCode;
        /**
         * 預算項目代碼
         */
        private String budgetItemCode;
        /**
         * 預算項目
         */
        private String budgetItemName;
        /**
         * 原始預算(A)
         */
        private BigDecimal originalAmount;
        /**
         * 保留預算(B)
         */
        private BigDecimal reservedAmount;
        /**
         * 勻入(C)
         */
        private BigDecimal allocIncreaseAmt;
        /**
         * 勻出(C')
         */
        private BigDecimal allocreduseAmt;
        /**
         * 可用預算(D)
         */
        private BigDecimal availableAmount;
        /**
         * 占用(E)
         */
        private BigDecimal occupiedAmount;
        /**
         * 動用(F)
         */
        private BigDecimal usedAmount;
        /**
         * 預算餘額
         */
        private BigDecimal remainingAmount;
    }

    /**
     * 預算交易明細
     */
    @Setter
    @Getter
    public static class BudgetTransactionItem {
        /**
         * 交易日期
         */
        @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
        private LocalDate transactionDate;
        /**
         * 來源名稱
         */
        private String sourceName;
        /**
         * 來源編號
         */
        private String sourceNumber;
        /**
         * 交易類別
         */
        private String transactionType;
        /**
         * 金額
         */
        private BigDecimal amount;
        /**
         * 供應商代號
         */
        private String bpNo;

        /**
         * 供應商名稱
         */
        private String bpName;

        /**
         * 簽呈號碼
         */
        private String docNo;
        /**
         * 摘要
         */
        private String remark;
        /**
         * 使用者
         */
        private String userName;
        /**
         * 建檔日期
         */
        @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createDate;
    }

}