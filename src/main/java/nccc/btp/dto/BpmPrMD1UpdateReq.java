package nccc.btp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BpmPrMD1UpdateReq {
    private String itemCode;
    private String remark;
    private String flowDemandDate;
    private LocalDate demandDate;
    private String location;
    private BigDecimal qty;
    private BigDecimal price;
    private BigDecimal tax;
    private BigDecimal total;

    // getter / setter
}
