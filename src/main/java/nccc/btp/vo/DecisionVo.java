package nccc.btp.vo;

import javax.validation.constraints.NotNull;
import nccc.btp.enums.Decision;

public class DecisionVo {
  /** 流程 ID */
  @NotNull(message = "processId 不能為空")
  private String processId;
  
  /** 決策 */
  @NotNull(message = "decision 不能為空")
  private Decision decision;
  
  private String rejectionReason;

  public String getProcessId() {
    return processId;
  }

  public void setProcessId(String processId) {
    this.processId = processId;
  }

  public Decision getDecision() {
    return decision;
  }

  public void setDecision(Decision decision) {
    this.decision = decision;
  }

  public String getRejectionReason() {
    return rejectionReason;
  }

  public void setRejectionReason(String rejectionReason) {
    this.rejectionReason = rejectionReason;
  }
  
}
