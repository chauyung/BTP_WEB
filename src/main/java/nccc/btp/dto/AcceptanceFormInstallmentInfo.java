package nccc.btp.dto;

import java.math.*;
import java.time.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 驗收單分期資料模型
 */
public class AcceptanceFormInstallmentInfo {

    /*
     * 處理狀態
     */
    public Boolean HandleStatus;

    /*
     * 品號
     */
    public String ItemCode;

    /*
     * 品名
     */
    public String ItemName;

    /**
     * 主鍵
     */
    public Long Id;

    /**
     * 期數
     */
    public String Period;

    /**
     * 原始數量 / 金額
     */
    public BigDecimal AcceptableQuantity;

    /*
     * 驗收數量
     */
    public BigDecimal AcceptedQuantity;

    /*
     * 餘料不驗數量/金額
     */
    public BigDecimal NotAcceptedQuantity;

    /*
     * 餘料不驗
     */
    public Boolean NotAccepted;

    /**
     * 保留資產
     */
    public Boolean RetainedAssets;

    /*
     * 驗收程序
     */
    public String AcceptanceProcedure;

    /*
     * 驗收結果
     */
    public String AcceptanceResult;

    /*
     * 交貨日期/驗收通知日期
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    public LocalDate DeliveryDate;

    /*
     * 展延驗收日期
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    public LocalDate ExtensionDate;

    /*
     * 展延核准文號
     */
    public String ExtensionDocumentNo;

    /*
     * 備註
     */
    public String Remark;

    /*
     * 憑證日期
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    public LocalDate CertificateDate;

    /*
     * 憑證號碼
     */
    public String CertificateNo;

    /*
     * 憑證類型
     */
    public String CertificateType;

    /*
     * 賣方統一編號
     */
    public String BusinessRegistrationNumber;

    /**
     * 申請金額
     */
    public BigDecimal ApplicationAmount;

    /**
     * 稅率
     */
    public BigDecimal TaxRate;

    /**
     * 未稅金額
     */
    public BigDecimal ExcludingTaxAmount;

    /**
     * 稅額
     */
    public BigDecimal Tax;

    /**
     * 零稅
     */
    public BigDecimal ZeroTax;

    /**
     * 免稅
     */
    public BigDecimal TaxFree;

    /*
     * 預算部門
     */
    public String BudgetDepartment;

    /*
     * 指定付款日期
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    public LocalDate SpecifyPaymentDate;

    /*
     * 指定過帳日期
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    public LocalDate SpecifyPostingDate;

    /*
     * 項目內文
     */
    public String ItemContent;

    /**
     * 預付設備款
     */
    public Boolean PrepaymentsEquipment;

    /**
     * 指定結案
     */
    public Boolean DesignatedCompletion;

    /*
     * 扣抵
     */
    public Boolean Deduction;

    /*
     * 備忘
     */
    public String Memo;

    /**
     * 已處理
     */
    public Boolean Processed;

    /*
     * 是否代扣繳
     */
    public Boolean Withholding;

    /*
     * 所得人清冊
     */
    public List<TaxpayerInfo> Taxpayers;

        /*
     * 本次驗收項目
     */
    public Boolean CurrentAcceptance;
}
