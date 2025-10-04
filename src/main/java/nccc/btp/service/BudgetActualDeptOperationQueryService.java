package nccc.btp.service;

import nccc.btp.dto.BudgetActualDeptOperationQueryRequest;
import nccc.btp.entity.NcccBudgetActual;

import java.util.List;

public interface BudgetActualDeptOperationQueryService {
    List<NcccBudgetActual> search(BudgetActualDeptOperationQueryRequest req);
}
