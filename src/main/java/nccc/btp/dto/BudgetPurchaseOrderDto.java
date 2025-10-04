package nccc.btp.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * 預算採購單資料
 */
@Getter
@Setter
public class BudgetPurchaseOrderDto {
    /**
     * 採購單號
     */
    private String purchaseOrderNo;
    /**
     * 部門代碼
     */
    private String deptCode;
    /**
     * 部門名稱
     */
    private String deptName;
    /**
     * 會計科目代碼
     */
    private String accountingCode;
    /**
     * 會計科目名稱
     */
    private String accountingName;
    /**
     * 採購金額
     */
    private BigDecimal purchaseAmount;
    /**
     * 預算餘額
     */
    private BigDecimal budgetBalance;
    /**
     * 採購目的
     */
    private String purchasePurpose;

    /**
     * 採購公文文號
     */
    private String purchaseDocNo;

}