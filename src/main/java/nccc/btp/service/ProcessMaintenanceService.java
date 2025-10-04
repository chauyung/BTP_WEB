package nccc.btp.service;

import javax.servlet.http.HttpServletRequest;

public interface ProcessMaintenanceService {

  void reassignByBusinessKey(String businessKey, String targetHrId, HttpServletRequest request);

}
