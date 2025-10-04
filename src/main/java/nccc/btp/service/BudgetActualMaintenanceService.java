package nccc.btp.service;

import java.util.List;
import nccc.btp.vo.BudgetActualVo;
import nccc.btp.dto.OperateItemsSaveRequest;

public interface BudgetActualMaintenanceService {
    List<BudgetActualVo> search(BudgetActualVo vo);
    List<BudgetActualVo> saveOperateItems(OperateItemsSaveRequest req);
}
