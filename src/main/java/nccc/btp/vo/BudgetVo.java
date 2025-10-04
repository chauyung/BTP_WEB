package nccc.btp.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

public class BudgetVo {


    /**
     * 預算項目
     */
    public interface BudgetItem {
        /**
         * 取得單號
         * @return
         */
        String getBUDGET_NO();

        /**
         * 取得序號
         * @return
         */
        String getSEQ_NO();

        /**
         * 取得年度
         * @return
         */
        String getYEAR();
        /**
         * 取得版次
         * @return
         */
        String getVERSION();
        /**
         * 取得部門代碼
         * @return
         */
        String getOU_CODE();
        /**
         * 取得部門名稱
         * @return
         */
        String getOU_NAME();
        /**
         * 取得預算項目代碼
         * @return
         */
        String getACC_CODE();
        /**
         * 取得預算項目名稱
         * @return
         */
        String getACC_NAME();
        /**
         * 取得預算金額
         * @return
         */
        BigDecimal getBUD_AMOUNT();
        /**
         * 取得說明
         * @return
         */
        String getREMARK();
    }

    /**
     * 預算作業項目
     */
    public interface BudgetOperateItem extends BudgetItem {
        /**
         * 作業項目
         * @return
         */
        String getOperateItem();
        /**
         * 作業項目代碼
         * @return
         */
        String getOperateItemCode();
        /**
         * 取得作業項目金額
         * @return
         */
        BigDecimal getOperateAmt();
        /**
         * 取得作業項目比例
         * @return
         */
        BigDecimal getOperateRatio();
    }

    public interface BudgetDetailItem extends BudgetItem {
        /**
         * 取得原始預算
         * @return
         */
        BigDecimal getOriginalBudget();
        /**
         * 取得保留預算
         * @return
         */
        BigDecimal getReserveBudget();
        /**
         * 取得預算勻入
         * @return
         */
        BigDecimal getAllocIncreaseAmt();
        /**
         * 取得預算勻出
         * @return
         */
        BigDecimal getAllocReduseAmt();
        /**
         * 取得預算占用
         * @return
         */
        BigDecimal getOccupyAmt();
        /**
         * 取得預算動用
         * @return
         */
        BigDecimal getUseAmt();
        /**
         * 取得預算耗用
         * @return
         */
        BigDecimal getConsumeAmt();
    }

    /**
     * 開帳用預算保留檢視表
     */
    public interface OpenBudgetReserveView {
        /**
         * 取得年度
         * @return
         */
        String getYEAR();
        /**
         * 取得版次
         * @return
         */
        String getVERSION();

        /**
         * 取得預算保留單號
         * @return
         */
        String getRSV_NO();

        /**
         * 取得預算保留日期
         * @return
         */
        LocalDate getAPPLY_DATE();

        /**
         * 取得預算保留部門
         * @return
         */
        String getAPPLY_OUCODE();

        /**
         * 取得預算保留人
         * @return
         */
        String getAPPLY_USER();
        /**
         * 取得預算項目
         * @return
         */
        String getACCOUNTING();
        /**
         * 取得SAP文件編號
         * @return
         */
        String getDOC_NO();
        /**
         * 取得採購單號
         * @return
         */
        String getPO_NO();
        /**
         * 取得採購金額
         * @return
         */
        BigDecimal getPO_AMT();
        /**
         * 取得採購備註
         * @return
         */
        String getPO_REMARK();
        /**
         * 取得預算保留金額
         * @return
         */
        BigDecimal getRESERVER_AMT();
        /**
         * 取得預算保留原因
         * @return
         */
        String getRES_REASON();
    }

    /**
     * 開帳用預算保留資料
     */
    @Getter
    @Setter
    public static class OpenBudgetReserveData {
        private String APPLY_OUCODE;
        private String ACCOUNTING;
        private BigDecimal RESERVER_AMT;
    }


    @Getter
    @Setter
    public static class BudgetTranscation{
        /**
         * 年度
         */
        private String year;

        /**
         * 版次(固定2)
         */
        private String version;

        /**
         * 部門代碼
         */
        private String ouCode;

        /**
         * 預算會科(8碼)
         */
        private String accounting;

        /**
         * 交易單種類型
         */
        private String transcationSource;

        /**
         * 交易類型(動用,占用,勻入,勻出,保留預算,耗用)
         */
        private String transcationType;

        /**
         * 交易日期
         */
        private LocalDate transcationDate;

        /**
         * 單號
         */
        private String transcationNo;

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
         * 建立日期
         */
        private LocalDateTime createDate;

        /**
         * 建立者
         */
        private String createUser;
    }

    /**
     * 責任分攤 SAP資料
     */
    public interface NcccAllocationSAPData {
        String getACCOUNTING();
        String getSUBJECT();
        String getITEM_TEXT();
        String getPO_ITEM_NO();
        String getOUCODE();
        String getOUNAME();
        String getPO_NO();
        String getPLAN();
        String getYEAR();
        String getMONTH();
        BigDecimal getFINAL_TOTAL();
        String getSAP_DOC_NO();
        LocalDate getPOST_DATE();
    }

    /**
     * 預算採購單資料
     */
    public interface BudgetPurchaseOrderData {
        String getPurchaseOrderNo();
        String getPrNo();
        String getDeptCode();
        String getdeptName();
        String getAccountingCode();
        String getAccountingName();
        BigDecimal getPurchaseAmount();
        BigDecimal getRequestAmount();
        BigDecimal getBudgetBalance();
        String getPurchasePurpose();
        String getPurchaseDocNo();
        String getApplicant();
        String getRequestDocNo();
    }

    public interface BudgetManagementPurchaseOrderIncludeDetailData {
        String getPurchaseOrderNo();
        String getPurchasePurpose();
        String getApplicant();
        String getEmpNo();
        String getDeptCode();
        String getDeptName();
        String getAccountingCode();
        String getAccountingName();
        BigDecimal getPurchaseAmount();
        BigDecimal getBudgetBalance();
    }

    /**
     * 預算請購單資料
     */
    public interface BudgetRequestOrderData {
        String getRequestOrder();
        LocalDate getRequestDate();
        String getAccountingCode();
        String getAccountingName();
        String getRequestDocNo();
        String getAmount();
    }

    /**
     * 預算資料
     */
    public interface BudgetBalanceData {
        /**
         * 年度
         */
        String getYear();

        /**
         * 版本
         */
        String getVersion();

        /**
         * 會計科目代碼
         */
        String getAccountingCode();

        /**
         * 會計科目名稱
         */
        String getAccountingName();

        /**
         * 部門代碼
         */
        String getDeptCode();

        /**
         * 部門名稱
         */
        String getDeptName();

        /**
         * 原始預算
         */
        BigDecimal getOriginalBudget();

        /**
         * 保留預算
         */
        BigDecimal getReserveBudget();

        /**
         * 勻入
         */
        BigDecimal getAllocIncreaseAmt();

        /**
         * 勻出
         */
        BigDecimal getAllocReduseAmt();

        /**
         * 占用
         */
        BigDecimal getOccupyAmt();

        /**
         * 動用
         */
        BigDecimal getUseAmt();

        /**
         * 耗用
         */
        BigDecimal getConsumeAmt();

        /**
         * 可用預算
         */
        BigDecimal getAvailableBudget();

        /**
         * 預算餘額
         */
        BigDecimal getBudgetBalance();

    }

    /**
     * 查詢預算餘額
     */
    @Getter
    @Setter
    public static class SearchBudgetBalanceData {
        private String year;
        private String accounting;
        private String ouCode;
    }

    /**
     * 查詢多筆預算餘額
     */
    @Getter
    @Setter
    public static class SearchBudgetBalanceListData{

        private List<SearchBudgetBalanceData> searchList;
    }

    /**
     * 合併sap固資相關
     */
    public interface BudgetSAPDepreciationAssetAccountingOuCodeData
    {
        /**
         * 資產編號
         */
        String getAssetsCode();

        /**
         * 資產名稱
         */
        String getAssetsName();

        /**
         * 資產類別
         */
        String getAssetsType();

        /**
         * 預算科目
         */
        String getAccounting();

        /**
         * 預算科目名稱
         */
        String getAccountingName();

        /**
         * 預算部門
         */
        String getOuCode();
    }

    public interface BudgetSAPDepreciationAssetOperateData
    {
        /**
         * 資產編號
         */
        String getAssetsCode();

        /**
         * 作業項目代嗎
         */
        String getOperateItemCode();

        /**
         * 作業項目
         */
        String getOperateItem();

        /**
         * 比例
         */
        BigDecimal getOperateRatio();


    }
}
