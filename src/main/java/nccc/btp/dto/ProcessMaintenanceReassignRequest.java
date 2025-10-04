package nccc.btp.dto;

public class ProcessMaintenanceReassignRequest {

  private String businessKey;
  private String targetHrId;

  public String getBusinessKey() {
    return businessKey;
  }

  public void setBusinessKey(String businessKey) {
    this.businessKey = businessKey;
  }

  public String getTargetHrId() {
    return targetHrId;
  }

  public void setTargetHrId(String targetHrId) {
    this.targetHrId = targetHrId;
  }

}
