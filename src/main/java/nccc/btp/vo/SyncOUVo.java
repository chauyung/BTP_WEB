package nccc.btp.vo;

import javax.servlet.http.PushBuilder;

public class SyncOUVo {
    
    /**
     * 部門經辦
     */
    public interface OuManager {
        /**
         * 部門代碼
         */
        String getOUCODE();
        /**
         * 部門名稱
         */
        String getOUNAME();
        /**
         * 經辦帳號
         */
        String getUSERID();
        /**
         * 經辦名稱
         */
        String getUSERNAME();
    }

    public interface BudgetOuManager
    {
        /**
         * 部門代碼
         */
        String getOUCODE();
        /**
         * 部門名稱
         */
        String getOUNAME();
        /**
         * 經辦帳號
         */
        String getUSERID();
        /**
         * 經辦名稱
         */
        String getUSERNAME();
        /**
         * 預算部門代碼
         */
        String getBUDGETOUCODE();
    }
}