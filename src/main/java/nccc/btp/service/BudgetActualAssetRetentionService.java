package nccc.btp.service;

import java.util.Optional;

import nccc.btp.dto.BudgetActualAssetRetentionRequest;

public interface BudgetActualAssetRetentionService {
	Optional<byte[]> exportExcelMaybe(BudgetActualAssetRetentionRequest req);
}
