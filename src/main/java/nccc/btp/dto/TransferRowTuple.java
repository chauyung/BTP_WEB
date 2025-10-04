package nccc.btp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class TransferRowTuple {
    private String year;
    private String version;
    private String transferOrderNo;
    private Integer seq;
    private String fromDeptCode;
    private String toDeptCode;
    private LocalDate transferDate;
    private String outAccounting;
    private String inAccounting;
    private BigDecimal outBalance; 
    private BigDecimal adjustAmount;

    public TransferRowTuple(String year,
                            String version,
                            String transferOrderNo,
                            String fromDeptCode,
                            String toDeptCode,
                            LocalDate transferDate,
                            String outAccounting,
                            String inAccounting,
                            BigDecimal outBalance,
                            BigDecimal adjustAmount) {
        this.year = year;
        this.version = version;
        this.transferOrderNo = transferOrderNo;
        this.fromDeptCode = fromDeptCode;
        this.toDeptCode = toDeptCode;
        this.transferDate = transferDate;
        this.outAccounting = outAccounting;
        this.inAccounting = inAccounting;
        this.outBalance = outBalance;
        this.adjustAmount = adjustAmount;
    }
}
