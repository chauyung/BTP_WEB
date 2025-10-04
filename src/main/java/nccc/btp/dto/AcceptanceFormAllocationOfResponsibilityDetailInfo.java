package nccc.btp.dto;

import java.math.*;
import java.time.*;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AcceptanceFormAllocationOfResponsibilityDetailInfo {

    /**
     * 月份
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    public LocalDate Month;

    /**
     * 數量 / 金額
     */
    public BigDecimal Quantity;

}
