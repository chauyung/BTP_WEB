/**
 * 
 */
package nccc.btp.vo;

import java.util.ArrayList;
import java.util.List;
import nccc.btp.entity.SyncUser;
import nccc.btp.enums.Mode;

/**
 * 流程引擎 共用vo
 */
public class ProcessVo {

  // 模式
  private Mode mode;

  // 流程歷程
  private List<TaskHistoryVo> taskHistoryList;

  // 是否是指派任務
  private boolean isSetNextAssignee = false;

  // 是否是指派任務
  private boolean isSapUser = false;

  // 指派任務候選人
  private List<SyncUser> nextAssigneeList;

  // 使否可以使用退回上一人
  private boolean canBackToPrevious = false;

  // 是否可以使用退回申請人(目前只有經辦可以使用)
  private boolean canReturnToInitiator = false;

  public ProcessVo() {
    this.setTaskHistoryList(new ArrayList<>());
    this.setNextAssigneeList(new ArrayList<>());
  }

  public Mode getMode() {
    return mode;
  }

  public void setMode(Mode mode) {
    this.mode = mode;
  }


  public List<TaskHistoryVo> getTaskHistoryList() {
    return taskHistoryList;
  }

  public void setTaskHistoryList(List<TaskHistoryVo> taskHistoryList) {
    this.taskHistoryList = taskHistoryList;
  }

  public boolean isSetNextAssignee() {
    return isSetNextAssignee;
  }

  public void setSetNextAssignee(boolean isSetNextAssignee) {
    this.isSetNextAssignee = isSetNextAssignee;
  }

  public List<SyncUser> getNextAssigneeList() {
    return nextAssigneeList;
  }

  public void setNextAssigneeList(List<SyncUser> nextAssigneeList) {
    this.nextAssigneeList = nextAssigneeList;
  }

  public boolean isCanReturnToInitiator() {
    return canReturnToInitiator;
  }

  public void setCanReturnToInitiator(boolean canReturnToInitiator) {
    this.canReturnToInitiator = canReturnToInitiator;
  }

  public boolean isCanBackToPrevious() {
    return canBackToPrevious;
  }

  public void setCanBackToPrevious(boolean canBackToPrevious) {
    this.canBackToPrevious = canBackToPrevious;
  }

  public boolean isSapUser() {
    return isSapUser;
  }

  public void setSapUser(boolean isSapUser) {
    this.isSapUser = isSapUser;
  }

}
