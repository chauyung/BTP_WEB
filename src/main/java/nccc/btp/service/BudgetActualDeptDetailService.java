package nccc.btp.service;

import java.util.Optional;

import nccc.btp.dto.BudgetActualDeptDetailRequest;

public interface BudgetActualDeptDetailService {
	Optional<byte[]> exportExcelMaybe(BudgetActualDeptDetailRequest req);
}
