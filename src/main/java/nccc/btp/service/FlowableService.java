package nccc.btp.service;

import java.util.List;
import java.util.Map;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import nccc.btp.vo.DecisionVo;
import nccc.btp.vo.TaskHistoryVo;

public interface FlowableService {

  String startProcess(String processKey, String businessKey, Map<String, Object> vars);
  
  Task taskQuery(String processInstanceId);

  public boolean completeTask(DecisionVo decisionVo);

  List<Task> getPendingTasksForUser(String user);

  List<Task> findCurrentTasks(String processInstanceId);

  List<HistoricTaskInstance> findHistoricTasks(String processInstanceId);

  boolean checkAtInitiatorTask(String processInstanceId);
  
  boolean checkAtSapUserTask(String processInstanceId);

  <T> T getProcessVariable(String processInstanceId, String variableName, Class<T> clazz);

  void saveProcessVariable(String processInstanceId, String key, Object value);

  List<TaskHistoryVo> getTaskHistory(String processInstanceId);
  
  boolean isSetNextAssignee(String processInstanceId);
  
  void reassignTask(String processInstanceId, String newUser);
  
  void setNextAssignee(String processInstanceId, String nextUser);
  
  String getNextDepId(String processInstanceId);
  
  boolean canReturnToInitiator(String processInstanceId);

  boolean isRetentionCostClerk(String processInstanceId);

  boolean isFinish(String processInstanceId);

  boolean checkTaskAssignee(String processInstanceId, String hrId);

  boolean isProcessFinishedByHistory(String processInstanceId);
}
