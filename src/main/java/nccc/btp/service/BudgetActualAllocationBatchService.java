package nccc.btp.service;


import nccc.btp.dto.BudgetActualAllocationBatchRequest;


public interface BudgetActualAllocationBatchService {
	int run(BudgetActualAllocationBatchRequest req);
}