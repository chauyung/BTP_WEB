package nccc.btp.service;

import java.io.IOException;
import java.util.List;
import org.springframework.core.io.Resource;
import nccc.btp.entity.NcccEmsMgm;
import nccc.btp.vo.BulkEmsUpdateVo;
import nccc.btp.vo.TerminalManagementVo;

public interface TerminalManagementService {
  List<TerminalManagementVo> search(String wealthNo, String ncccWealthNo);

  String update(List<NcccEmsMgm> ncccEmsMgmList);

  String toSAP(List<NcccEmsMgm> ncccEmsMgmList);

  Resource getTerminalManagementCsvSample() throws IOException;

  String ledgerToScrap(List<BulkEmsUpdateVo> voList);
  
  String ledgerToListed(List<BulkEmsUpdateVo> voList);
  
  String listedImpair(List<BulkEmsUpdateVo> voList);
}
