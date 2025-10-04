package nccc.btp.service;

import java.util.List;
import nccc.btp.dto.PreBudgetOperateDto;

public interface BudgetActualOperationQueryService {

    List<PreBudgetOperateDto.Row> search(PreBudgetOperateDto.SearchReq req);

}