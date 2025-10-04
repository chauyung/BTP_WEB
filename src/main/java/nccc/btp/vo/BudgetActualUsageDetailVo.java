package nccc.btp.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;

public class BudgetActualUsageDetailVo {

	@Getter
	public static class TxnRow {
	    private final String year;
	    private final String version;
	    private final String ouCode;
	    private final String accounting;
	    private final String accountingName;
	    private final BigDecimal originalBudget;
	    private final BigDecimal reserveBudget;
	    private final BigDecimal allocIncreaseAmt;
	    private final BigDecimal allocReduseAmt;
	    private final BigDecimal occupyAmt;
	    private final BigDecimal useAmt;
	    private final LocalDate transcationDate;
	    private final String transcationNo;
	    private final String transcationType;
	    private final BigDecimal amount;
	    private final String docNo;
	    private final String transcationSource;
	    private final String bpNo;
	    private final String bpName;

	    public TxnRow(
	        String year, String version, String ouCode, String accounting, String accountingName,
	        BigDecimal originalBudget, BigDecimal reserveBudget,
	        BigDecimal allocIncreaseAmt, BigDecimal allocReduseAmt,
	        BigDecimal occupyAmt, BigDecimal useAmt,
	        LocalDate transcationDate, String transcationNo, String transcationType,
	        BigDecimal amount, String docNo, String transcationSource,
	        String bpNo, String bpName
	    ) {
	        this.year = year;
	        this.version = version;
	        this.ouCode = ouCode;
	        this.accounting = accounting;
	        this.accountingName = accountingName;
	        this.originalBudget = originalBudget;
	        this.reserveBudget = reserveBudget;
	        this.allocIncreaseAmt = allocIncreaseAmt;
	        this.allocReduseAmt = allocReduseAmt;
	        this.occupyAmt = occupyAmt;
	        this.useAmt = useAmt;
	        this.transcationDate = transcationDate;
	        this.transcationNo = transcationNo;
	        this.transcationType = transcationType;
	        this.amount = amount;
	        this.docNo = docNo;
	        this.transcationSource = transcationSource;
	        this.bpNo = bpNo;
	        this.bpName = bpName;
	    }

	    public BigDecimal getOriginalPlusReserve() {
	        return (originalBudget == null ? BigDecimal.ZERO : originalBudget)
	             .add(reserveBudget == null ? BigDecimal.ZERO : reserveBudget);
	    }
	}
}
