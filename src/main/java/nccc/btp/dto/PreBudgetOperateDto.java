package nccc.btp.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 預算編列作業項目查詢用 DTO (Request + Response)
 */
@Data
public class PreBudgetOperateDto {

    @Data
    public static class SearchReq implements Serializable {
        private static final long serialVersionUID = 1L;

        private String year;
        private String version;
        private List<String> ouCodes;
        private List<String> operateItemCodes;
    }

    @Data
    public static class Row implements Serializable {
        private static final long serialVersionUID = 1L;

        private String year;
        private String version;
        private String ouCode;
        private String ouName;
        private String accounting;
        private String subject;
        private BigDecimal budAmount;
        private String operateItemCode;
        private String operateItem;
        private BigDecimal operateAmt;
        private BigDecimal operateRatio;
    }
}
