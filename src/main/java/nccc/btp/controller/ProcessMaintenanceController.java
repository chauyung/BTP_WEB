package nccc.btp.controller;


import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nccc.btp.dto.ProcessMaintenanceReassignRequest;
import nccc.btp.response.ApiResponse;
import nccc.btp.service.ProcessMaintenanceService;

@RestController
@RequestMapping("/processMaintenance")
public class ProcessMaintenanceController {

  private final ProcessMaintenanceService processMaintenanceService;

  public ProcessMaintenanceController(ProcessMaintenanceService processMaintenanceService) {
    this.processMaintenanceService = processMaintenanceService;
  }

  @PostMapping("/reassign")
  public ApiResponse<Void> reassign(@RequestBody ProcessMaintenanceReassignRequest req,
      HttpServletRequest request) {
    processMaintenanceService.reassignByBusinessKey(req.getBusinessKey(), req.getTargetHrId(),
        request);
    return new ApiResponse<Void>();
  }
}
