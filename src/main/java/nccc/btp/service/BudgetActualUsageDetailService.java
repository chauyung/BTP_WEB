package nccc.btp.service;

import java.util.Optional;

import nccc.btp.dto.BudgetActualUsageDetailRequest;

public interface BudgetActualUsageDetailService {
	Optional<byte[]> exportExcelMaybe(BudgetActualUsageDetailRequest request);
}
