package nccc.btp.service;

import java.util.Optional;

import nccc.btp.dto.BudgetActualTransferDetailRequest;

public interface BudgetActualTransferDetailService {
	Optional<byte[]> exportExcelMaybe(BudgetActualTransferDetailRequest req);
}
