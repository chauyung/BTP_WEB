package nccc.btp.service;

import java.util.Optional;

import nccc.btp.dto.BudgetActualCenterSummaryRequest;

public interface BudgetActualCenterSummaryService {
	Optional<byte[]> exportExcelMaybe(BudgetActualCenterSummaryRequest req);
}
