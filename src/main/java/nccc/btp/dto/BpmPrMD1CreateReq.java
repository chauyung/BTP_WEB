package nccc.btp.dto;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BpmPrMD1CreateReq {
    @NotBlank
    private String prNo;
    @NotBlank
    private String prItemNo;
    private String itemCode;
    private String remark;
    private String flowDemandDate;
    private LocalDate demandDate;
    private String location;
    @DecimalMin("0.000")
    private BigDecimal qty;
    @DecimalMin("0.000")
    private BigDecimal price;
    @DecimalMin("0.000")
    private BigDecimal tax;
    @DecimalMin("0.00")
    private BigDecimal total;

    // getter / setter
}
