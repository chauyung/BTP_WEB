package nccc.btp.service;

import java.io.IOException;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import nccc.btp.vo.AssignTasksVo;
import nccc.btp.vo.DecisionVo;
import nccc.btp.vo.TravelExpensesAccountingDecisionVo;
import nccc.btp.vo.TravelExpensesVo;

public interface TravelExpensesService {

  TravelExpensesVo init();

  String startProcess(TravelExpensesVo vo, List<MultipartFile> files);

  TravelExpensesVo query(String btNo);

  String decision(DecisionVo vo);
  
  String accountingDecision(TravelExpensesAccountingDecisionVo vo);
  
  String update(TravelExpensesVo vo, List<MultipartFile> files);

  String toSAP(TravelExpensesVo vo);
  
  String setNextAssignee(AssignTasksVo assignTasksVo);
  
  Resource getTravelExpensesCsvSample() throws IOException;

  byte[] getVoucherStrickerWord(String btNo);

}
