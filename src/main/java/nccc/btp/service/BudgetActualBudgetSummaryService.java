package nccc.btp.service;

import java.util.Optional;

import nccc.btp.dto.BudgetActualBudgetSummaryRequest;

public interface BudgetActualBudgetSummaryService {
	Optional<byte[]> exportExcelMaybe(BudgetActualBudgetSummaryRequest request);
}
