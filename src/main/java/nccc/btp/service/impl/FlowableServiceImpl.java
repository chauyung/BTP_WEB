package nccc.btp.service.impl;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.enums.AssigneeRole;
import nccc.btp.enums.ProcessDefinitionKey;
import nccc.btp.service.FlowableService;
import nccc.btp.util.SecurityUtil;
import nccc.btp.vo.DecisionVo;
import nccc.btp.vo.TaskHistoryVo;

/**
 * 
 */
@Service
@Transactional
public class FlowableServiceImpl implements FlowableService {

  @Autowired
  private TaskService taskService;

  @Autowired
  private HistoryService historyService;

  @Autowired
  private RepositoryService repositoryService;

  @Autowired
  private RuntimeService runtimeService;

  protected static Logger LOG = LoggerFactory.getLogger(FlowableServiceImpl.class);


  /**
   * TODO 起流程
   * 
   * @param processKey (模板名稱)
   * @param businessKey (單號)
   * @param vars
   * @return
   */
  @Override
  public String startProcess(String processKey, String businessKey, Map<String, Object> vars) {
    ProcessInstance processInstance =
        runtimeService.startProcessInstanceByKey(processKey, businessKey, vars);
    return processInstance.getId();
  }

  /**
   * TODO 查詢task
   * 
   * @param processInstanceId
   * @return
   */
  @Override
  public Task taskQuery(String processInstanceId) {
    Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
    if (task == null) {
      throw new RuntimeException("No active task found");
    }
    return task;
  }

  /**
   * TODO 簽核
   * 
   * @param decisionVo
   * @return
   */
  @Override
  public boolean completeTask(DecisionVo decisionVo) {
    Task task = taskQuery(decisionVo.getProcessId());
    NcccUserDto user = SecurityUtil.getCurrentUser();
    if (!task.getAssignee().equals(user.getHrid())) {
      LOG.error("簽核人員錯誤: {} != {}", task.getAssignee(), user.getHrid());
      throw new RuntimeException("簽核人員錯誤");
    }
    String nextAssignee =
        (String) runtimeService.getVariable(decisionVo.getProcessId(), "nextAssignee");
    if (nextAssignee != null && !nextAssignee.isEmpty()) {
      throw new RuntimeException("請指派簽核人員");
    }
    Map<String, Object> vars = new HashMap<>();
    @SuppressWarnings("unchecked")
    List<String> approvers =
        (List<String>) runtimeService.getVariable(decisionVo.getProcessId(), "approvers");
    // 代表一般同仁
    if (approvers != null && !approvers.isEmpty()) {
      Integer currentIndex =
          (Integer) runtimeService.getVariable(decisionVo.getProcessId(), "currentIndex");
      int newIndex = currentIndex;
      if (currentIndex == 0 && (decisionVo.getDecision().isApprove()
          || decisionVo.getDecision().isReturn() || decisionVo.getDecision().isBack())) {
        throw new RuntimeException("已到經辦 只能送審或作廢");
      }
      switch (decisionVo.getDecision()) {
        case SUBMIT:
          if (currentIndex == 0) {
            newIndex = currentIndex + 1;
          } else {
            throw new RuntimeException("只有經辦可以送審");
          }
          break;
        case APPROVE:
          newIndex = currentIndex + 1;
          break;
        case BACK:
          newIndex = currentIndex - 1;
          break;
        case RETURN:
          newIndex = 0;
          break;
        case INVALID:
          break;
        case END:
          break;
        default:
          throw new IllegalArgumentException("Invalid decision: " + decisionVo.getDecision());
      }
      if (currentIndex == 0 && (decisionVo.getDecision().isApprove()
          || decisionVo.getDecision().isReturn() || decisionVo.getDecision().isBack())) {
        throw new RuntimeException("已到申請人 只能送審或作廢");
      }
      vars.put("currentIndex", newIndex);
      if (newIndex <= approvers.size() && newIndex != 0) {
        vars.put("currentApprover", approvers.get(newIndex - 1));
      }
      // 代表主管以上
    } else {
      if (checkAtInitiatorTask(decisionVo.getProcessId()) && (decisionVo.getDecision().isApprove()
          || decisionVo.getDecision().isReturn() || decisionVo.getDecision().isBack())) {
        throw new RuntimeException("已到申請人 只能送審或作廢");
      }
    }
    if (decisionVo.getRejectionReason() != null && !decisionVo.getRejectionReason().isEmpty()) {
      taskService.setVariableLocal(task.getId(), "rejectionReason",
          decisionVo.getRejectionReason());
    }
    taskService.setVariableLocal(task.getId(), "decision",
        decisionVo.getDecision().getDescription());
    taskService.setVariableLocal(task.getId(), "assigneeName", user.getUserName());
    vars.put("decision", decisionVo.getDecision().getDescription());
    taskService.complete(task.getId(), vars);
    Task nextTask =
        taskService.createTaskQuery().processInstanceId(decisionVo.getProcessId()).singleResult();
    if (nextTask != null) {
      System.out.println("@@@通知: " + nextTask.getAssignee());
    }
    return true;
  }

  /**
   * TODO 取得用戶待處理任務
   * 
   * @param user
   * @return
   */
  @Override
  public List<Task> getPendingTasksForUser(String user) {
    return taskService.createTaskQuery().taskAssignee(user).active().orderByTaskCreateTime().asc()
        .list();
  }

  /**
   * TODO 尋找當前任務
   * 
   * @param processInstanceId
   * @return
   */
  @Override
  public List<Task> findCurrentTasks(String processInstanceId) {
    return taskService.createTaskQuery().processInstanceId(processInstanceId).active()
        .orderByTaskCreateTime().asc().list();
  }


  /**
   * TODO 查詢歷史
   * 
   * @param processInstanceId
   * @return
   */
  @Override
  public List<HistoricTaskInstance> findHistoricTasks(String processInstanceId) {
    return historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
        .finished().orderByHistoricTaskInstanceEndTime().asc().list();
  }

  /**
   * TODO 用來判斷是在發起人
   * 
   * @param processInstanceId
   * @return
   */
  @Override
  public boolean checkAtInitiatorTask(String processInstanceId) {
    Task task = taskQuery(processInstanceId);
    return task.getTaskDefinitionKey().equals(AssigneeRole.INITIATOR.getKey() + "Task");
  }

  /**
   * TODO 用來判斷是在拋轉SAP人員
   * 
   * @param processInstanceId
   * @return
   */
  @Override
  public boolean checkAtSapUserTask(String processInstanceId) {
    Task task = taskQuery(processInstanceId);
    return task.getTaskDefinitionKey().equals(AssigneeRole.SAP_USER.getKey() + "Task");
  }

  /**
   * TODO 存變數
   * 
   * @param processInstanceId
   * @param key
   * @param value
   */
  @Override
  public void saveProcessVariable(String processInstanceId, String key, Object value) {
    runtimeService.setVariable(processInstanceId, key, value);
  }

  /**
   * TODO 取變數
   * 
   * @param <T>
   * @param processInstanceId
   * @param variableName
   * @param clazz
   * @return
   */
  @Override
  public <T> T getProcessVariable(String processInstanceId, String variableName, Class<T> clazz) {
    Object value = runtimeService.getVariable(processInstanceId, variableName);
    if (value != null && clazz.isInstance(value)) {
      return (T) value;
    }
    return null;
  }

  /**
   * TODO 取得task歷程
   * 
   * @param processInstanceId
   * @return
   */
  @Override
  public List<TaskHistoryVo> getTaskHistory(String processInstanceId) {
    List<HistoricTaskInstance> history =
        historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
            .includeTaskLocalVariables().orderByHistoricTaskInstanceStartTime().asc().list();

    return history.stream().map(hti -> {
      TaskHistoryVo vo = new TaskHistoryVo();
      vo.setAssignee(hti.getAssignee());
      vo.setEnd(hti.getEndTime());

      HistoricVariableInstance decisionVar = historyService.createHistoricVariableInstanceQuery()
          .taskId(hti.getId()).variableName("decision").singleResult();
      vo.setDecision(decisionVar != null ? String.valueOf(decisionVar.getValue()) : null);

      HistoricVariableInstance rejectReasonVar =
          historyService.createHistoricVariableInstanceQuery().taskId(hti.getId())
              .variableName("rejectionReason").singleResult();
      vo.setRejectionReason(
          rejectReasonVar != null ? String.valueOf(rejectReasonVar.getValue()) : null);

      HistoricVariableInstance assigneeNameVar =
          historyService.createHistoricVariableInstanceQuery().taskId(hti.getId())
              .variableName("assigneeName").singleResult();
      vo.setAssigneeName(
          assigneeNameVar != null ? String.valueOf(assigneeNameVar.getValue()) : null);
      return vo;
    }).collect(Collectors.toList());
  }

  /**
   * TODO 判斷是否在塞下一個人的userTask
   * 
   * @param processInstanceId
   * @return
   */
  @Override
  public boolean isSetNextAssignee(String processInstanceId) {
    taskQuery(processInstanceId);
    String nextAssignee = (String) runtimeService.getVariable(processInstanceId, "nextAssignee");
    return nextAssignee != null && !nextAssignee.isEmpty();
  }

  /**
   * TODO 換人
   * 
   * @param processInstanceId
   * @param newUser
   */
  @Override
  public void reassignTask(String processInstanceId, String newUser) {
    Task task = taskQuery(processInstanceId);
    taskService.setAssignee(task.getId(), newUser);
  }

  /**
   * TODO 指派下一人員
   * 
   * @param processInstanceId
   * @param nextUser
   */
  @Override
  public void setNextAssignee(String processInstanceId, String nextUser) {
    // 需要成本經辦去拋SAP 的流程定義 key
    List<String> costClerkToSapUser =
        Arrays.asList(ProcessDefinitionKey.DOMESTIC_BUSINESS_TRIP_GENERAL_COLLEAGUES.getKey(),
            ProcessDefinitionKey.DOMESTIC_BUSINESS_TRIP_SUPERVISOR_AND_ABOVE.getKey(),
            ProcessDefinitionKey.APPLICATIONEXPENSES_UTILITYEXPENSES.getKey(),
            ProcessDefinitionKey.APPLICATIONEXPENSES_GENERALEXPENSESREQUESTPAYMENT_EXCLUSIVE
                .getKey(),
            ProcessDefinitionKey.APPLICATIONEXPENSES_GENERALEXPENSESREQUESTPAYMENT_GENERAL.getKey(),
            ProcessDefinitionKey.APPLICATIONEXPENSES_PREPAYMENTWITHAPPROVAL.getKey(),
            ProcessDefinitionKey.APPLICATIONEXPENSES_PREPAYMENTWITHOUTAPPROVAL.getKey(),
            ProcessDefinitionKey.APPLICATIONEXPENSES_GENERALEXPENSESREQUESTPAYMENT_SOCIAL.getKey());

    // 查當前任務
    Task task = taskQuery(processInstanceId);

    NcccUserDto user = SecurityUtil.getCurrentUser();
    if (!task.getAssignee().equals(user.getHrid())) {
      LOG.info("@@@task.getAssignee(): {}, user.getHrAccount(): {}", task.getAssignee(),
          user.getHrid());
      LOG.error("簽核人員錯誤: {} != {}", task.getAssignee(), user.getHrid());
      throw new RuntimeException("簽核人員錯誤");
    }

    // 準備流程變數
    Map<String, Object> vars = new HashMap<>();
    String nextAssignee = (String) runtimeService.getVariable(processInstanceId, "nextAssignee");
    vars.put(nextAssignee, nextUser);

    // 查 processDefinitionId（透過 runtimeService）
    ProcessInstance pi = runtimeService.createProcessInstanceQuery()
        .processInstanceId(processInstanceId).singleResult();

    if (pi == null) {
      throw new RuntimeException("找不到流程實例：" + processInstanceId);
    }

    String processDefinitionId = pi.getProcessDefinitionId();

    ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
        .processDefinitionId(processDefinitionId).singleResult();

    if (pd == null) {
      throw new RuntimeException("找不到流程定義：" + processDefinitionId);
    }

    // 若此流程屬於成本經辦需拋SAP的流程，設定 sapUser
    if (costClerkToSapUser.contains(pd.getKey())) {
      vars.put(AssigneeRole.SAP_USER.getKey(), nextUser);
    }

    String userTaskId = task.getTaskDefinitionKey();

    // 若是會計經辦節點，也一併設定 sapUser
    if (userTaskId.equals(AssigneeRole.ASSIGN_ACCOUNTING_CLERK.getKey() + "Task")) {
      vars.put(AssigneeRole.SAP_USER.getKey(), nextUser);
    }

    vars.put("decision", "指派人員");
    taskService.setVariableLocal(task.getId(), "decision", "指派人員");
    taskService.setVariableLocal(task.getId(), "assigneeName", user.getUserName());

    // 簽核完成並帶入變數
    taskService.complete(task.getId(), vars);
  }

  /**
   * 取得下一個指派經辦的部門
   * 
   * @param processInstanceId
   * @return
   */
  @Override
  public String getNextDepId(String processInstanceId) {
    taskQuery(processInstanceId);
    String nextAssignee = (String) runtimeService.getVariable(processInstanceId, "nextAssignee");
    return AssigneeRole.fromKey(nextAssignee).getCode();
  }

  /**
   * TODO 判斷使否可以使用退回申請人(目前只有經辦可以使用)
   * 
   * @param processInstanceId
   * @return
   */
  @Override
  public boolean canReturnToInitiator(String processInstanceId) {
    Task task = taskQuery(processInstanceId);
    return task.getTaskDefinitionKey().equals(AssigneeRole.ACCOUNTING_CLERK.getKey() + "Task")
        || task.getTaskDefinitionKey().equals(AssigneeRole.COST_CLERK.getKey() + "Task");
  }

  /**
   * TODO 判斷是否為保留簽的成本經辦
   *
   * @param processInstanceId
   * @return
   */
  @Override
  public boolean isRetentionCostClerk(String processInstanceId) {
    Task task = taskQuery(processInstanceId);
    return task.getTaskDefinitionKey().equals(AssigneeRole.RETENTION_COST_CLERK.getKey() + "Task");
  }

  /**
   * TODO 判斷是否已經結束流程
   * 
   * @param processInstanceId
   * @return
   */
  @Override
  public boolean isFinish(String processInstanceId) {
    List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
        .processInstanceId(processInstanceId).list();


    if (tasks.stream().allMatch(task -> task.getEndTime() != null)) {
      // 代表流程已結束
      return true;
    } else {
      return false;
    }
  }

  /**
   * TODO 判斷是否屬於這個任務
   * 
   * @param processInstanceId
   * @param hrId
   * @return
   */
  @Override
  public boolean checkTaskAssignee(String processInstanceId, String hrId) {
    Task task = taskService.createTaskQuery().taskId(processInstanceId).singleResult();

    if (task != null && task.getAssignee() != null && task.getAssignee().equals(hrId)) {
      return true;
    } else {
      return false;
    }

  }

  // 判斷流程是否結束
  @Override
  public boolean isProcessFinishedByHistory(String processInstanceId) {
    return historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId)
        .finished().singleResult() != null;
  }

  /**
   * 驗收用：啟動主流程 firstModule: General(一般)/ FixedAsset(固定資產)/PrepaidEquipmentAccounting(預付設備款) /
   * Designation(指定結案(餘量不收)) /Installment(分期付款變更)/finish(結束)
   * 
   * @param businessKey
   * @param firstModule
   * @return
   */
  public String startAcceptanceFormMain(String businessKey, String firstModule) {

    Map<String, Object> vars = new HashMap<>();
    vars.put("businessKey", businessKey);

    vars.put("nextModule", firstModule);

    vars.put("amount", new BigDecimal("5000000"));
    vars.put("rejectedCount", 0);
    vars.put("nextAssignee", null);
    vars.put("contractor", "申請單位-經辦");

    List<String> approvalApprovers = Arrays.asList("申請單位-科長", "申請單位-經理", "申請單位-協理", "申請單位-單位主管");
    vars.put("approvers", approvalApprovers);
    vars.put("approvalApprovers", approvalApprovers);
    vars.put("currentIndex", 0);
    vars.put("currentApprover", approvalApprovers.get(0));

    vars.put("countersignApprovers", null);

    vars.put("taskAssignment1", "指派人員(成本)");
    vars.put("taskAssignment2", "指派人員(會計)");
    vars.put("accountingClerk", null);
    vars.put("accountingSectionChief", "會計科長");
    vars.put("accountingManager", "會計經理");
    vars.put("accountingAssociate", "會計協理");
    vars.put("accountingSeniorAssociate", "會計資協");
    vars.put("sapUser", "sapuser");

    vars.put("incomeTaxProcessor", "所得稅處理人");
    vars.put("auditOffice", "稽核室");
    vars.put("administrativeManager", "行館部主管");
    vars.put("vicePresident", "副總");
    vars.put("president", "總經理");
    vars.put("assetManager", "資產管理人");
    vars.put("assetManagerSupervisor", "資產管理人主管");

    ProcessInstance pi =
        runtimeService.startProcessInstanceByKey("AcceptanceForm-Main", businessKey, vars);
    return pi.getProcessInstanceId();
  }

  /**
   * 驗收用：完成任務（會簽 or 非會簽）
   * 
   * @param decisionVo
   * @return
   */
  public boolean acceptanceFormCompleteTask(DecisionVo decisionVo) {

    final String processId = decisionVo.getProcessId();
    final NcccUserDto user = SecurityUtil.getCurrentUser();

    // 父流程是否停在 waitNext
    Execution wait = runtimeService.createExecutionQuery().processInstanceId(processId)
        .activityId("waitNext").singleResult();
    if (wait != null) {
      throw new RuntimeException("主流程目前停在 waitNext，請先用 nextModule 設定下一個子流程或 finish");
    }

    // 先找父 PI 的任務，找不到就搜子流程
    List<Task> active = taskService.createTaskQuery().processInstanceId(processId).active().list();
    if (active.isEmpty()) {
      List<String> subtree = collectRuntimeSubtreeIds(processId);
      if (subtree.size() > 1) {
        active = taskService.createTaskQuery().processInstanceIdIn(subtree).active().list();
      }
    }
    if (active.isEmpty()) {
      throw new RuntimeException("No active task found（父流程可能只有 gateway/receiveTask，或子流程已結束）");
    }

    // 會簽 or 非會簽 → 鎖定要完成的那顆 task
    boolean hasCountersign =
        active.stream().anyMatch(t -> "countersignTask".equals(t.getTaskDefinitionKey()));

    Task task;
    if (hasCountersign) {
      task = active.stream().filter(t -> "countersignTask".equals(t.getTaskDefinitionKey()))
          .filter(t -> user.getHrAccount().equals(t.getAssignee())).findFirst().orElse(null);
      if (task == null) {
        throw new RuntimeException("找不到你的會簽任務，請確認你在 countersignApprovers 名單中");
      }
    } else {
      if (active.size() > 1) {
        throw new RuntimeException("流程中有多筆非會簽任務，無法自動判斷，請指定 taskId 完成：\n" + formatTaskList(active));
      }
      task = active.get(0);
      if (task.getAssignee() != null && !user.getHrAccount().equals(task.getAssignee())) {
        throw new RuntimeException("簽核人員錯誤");
      }
    }

    // 指派檢查（taskLocal → 子流程 → 父流程）
    String taskPi = task.getProcessInstanceId();
    String nextAssignee = getCascadeVar("nextAssignee", task.getId(), taskPi, processId);
    if (nextAssignee != null && !nextAssignee.isEmpty()) {
      throw new RuntimeException("請指派簽核人員");
    }

    // 取審核鏈（以任務所屬子流程 ID 為準）
    @SuppressWarnings("unchecked")
    List<String> approvers = (List<String>) runtimeService.getVariable(taskPi, "approvers");
    if (approvers == null)
      approvers = Collections.emptyList();
    int size = approvers.size();

    String currAppr = (String) runtimeService.getVariable(taskPi, "currentApprover");

    // 依 DecisionVo 驗證與計算
    Map<String, Object> vars = new HashMap<>();

    // 退回理由帶 task-local
    if (decisionVo.getRejectionReason() != null && !decisionVo.getRejectionReason().isEmpty()) {
      taskService.setVariableLocal(task.getId(), "rejectionReason",
          decisionVo.getRejectionReason());
    }

    // 會簽任務
    if ("countersignTask".equals(task.getTaskDefinitionKey())) {

      if (!(decisionVo.getDecision().isApprove() || decisionVo.getDecision().isReject())) {
        throw new RuntimeException("會簽任務只能使用 APPROVE 或 REJECT");
      }

      // 累計 rejectedCount
      Integer rejectedCount = (Integer) runtimeService.getVariable(taskPi, "rejectedCount");
      if (rejectedCount == null)
        rejectedCount = 0;
      if (decisionVo.getDecision().isReject()) {
        vars.put("rejectedCount", rejectedCount + 1);
      }

      int derived = (currAppr == null) ? 0 : (approvers.indexOf(currAppr) + 1);
      vars.put("currentIndex", Math.max(0, derived));

      // 記錄 decision 與簽核者
      taskService.setVariableLocal(task.getId(), "decision",
          decisionVo.getDecision().getDescription());
      taskService.setVariableLocal(task.getId(), "assigneeName", user.getUserName());
      vars.put("decision", decisionVo.getDecision().getDescription());

      taskService.complete(task.getId(), vars);

    } else if ("approvalTask".equals(task.getTaskDefinitionKey())) {
      // 一般審核任務：index 推進/退回/回申請人等

      Integer currentIndex = (Integer) runtimeService.getVariable(taskPi, "currentIndex");
      if (currentIndex == null)
        currentIndex = 0;
      int newIndex = currentIndex;

      // 到申請人（index==0）時的限制
      if (currentIndex == 0 && (decisionVo.getDecision().isApprove()
          || decisionVo.getDecision().isReturn() || decisionVo.getDecision().isBack())) {
        throw new RuntimeException("已到申請人 只能送審或作廢");
      }

      switch (decisionVo.getDecision()) {
        case SUBMIT:
          if (currentIndex == 0) {
            newIndex = currentIndex + 1;
          } else {
            throw new RuntimeException("只有申請人可以送審");
          }
          break;
        case APPROVE:
          newIndex = currentIndex + 1;
          break;
        case BACK:
          newIndex = currentIndex - 1;
          break;
        case RETURN:
          newIndex = 0;
          break;
        case INVALID:
        case END:
          break;
        default:
          throw new IllegalArgumentException("Invalid decision: " + decisionVo.getDecision());
      }

      // 二次保護（訊息一致）
      if (currentIndex == 0 && (decisionVo.getDecision().isApprove()
          || decisionVo.getDecision().isReturn() || decisionVo.getDecision().isBack())) {
        throw new RuntimeException("已到申請人 只能送審或作廢");
      }

      vars.put("currentIndex", newIndex);
      if (newIndex <= size && newIndex != 0) {
        vars.put("currentApprover", approvers.get(newIndex - 1));
      } else if (newIndex == 0) {
        vars.put("currentApprover", null); // 回申請人可清空
      }

      // 記錄 decision 與簽核者
      taskService.setVariableLocal(task.getId(), "decision",
          decisionVo.getDecision().getDescription());
      taskService.setVariableLocal(task.getId(), "assigneeName", user.getUserName());
      vars.put("decision", decisionVo.getDecision().getDescription());

      // 變數寫在任務所屬子流程上
      runtimeService.setVariables(taskPi, vars);

      // 任務可能被 listener 先處理，這裡保險檢查
      if (taskService.createTaskQuery().taskId(task.getId()).singleResult() == null) {
        return true;
      }
      taskService.complete(task.getId(), vars);

    } else {
      // 其他任務：帶上基本變數完成
      taskService.setVariableLocal(task.getId(), "decision",
          decisionVo.getDecision().getDescription());
      taskService.setVariableLocal(task.getId(), "assigneeName", user.getUserName());
      vars.put("decision", decisionVo.getDecision().getDescription());
      taskService.complete(task.getId(), vars);
    }

    // 完成後通知下一步（同父+子）
    List<String> subtree = collectRuntimeSubtreeIds(processId);
    Task nextTask =
        taskService.createTaskQuery().processInstanceIdIn(subtree).active().singleResult();
    if (nextTask != null) {
      System.out
          .println("@@@通知: " + (nextTask.getAssignee() == null ? "(未指派)" : nextTask.getAssignee()));
    }

    return true;
  }

  /** 從 taskLocal → 子流程 → 父流程 取變數（維持你原本的查找順序與語意） */
  private String getCascadeVar(String varName, String taskId, String taskPi, String rootPi) {
    String v = (String) taskService.getVariableLocal(taskId, varName);
    if (v != null && !v.isEmpty())
      return v;

    v = (String) runtimeService.getVariable(taskPi, varName);
    if (v != null && !v.isEmpty())
      return v;

    if (!taskPi.equals(rootPi)) {
      v = (String) runtimeService.getVariable(rootPi, varName);
    }
    return v;
  }


  /**
   * 驗收用：設定下一個模組並觸發流程繼續
   * 
   * @param processInstanceId
   * @param nextModule
   * @return
   */
  public String setNextModuleAndTrigger(String processInstanceId, String nextModule) {
    runtimeService.setVariable(processInstanceId, "nextModule", nextModule);

    List<Execution> execs = runtimeService.createExecutionQuery()
        .processInstanceId(processInstanceId).activityId("waitNext").list();
    if (execs.isEmpty())
      return "目前沒有 waitNext，流程可能在子流程或已結束";
    if (execs.size() > 1)
      return "偵測到多個 waitNext execution，請檢查流程定義";

    runtimeService.trigger(execs.get(0).getId());
    return "OK: 已設定 nextModule=" + nextModule + " 並觸發流程";
  }

  /**
   * 驗收用：指派任務
   * 
   * @param processInstanceId
   * @param nextUser
   * @return
   */
  public String acceptanceFormSetNextAssignee(String processInstanceId, String nextUser) {

    // 找任務：先用父PI找，找不到就往所有子流程找；若多顆避免誤判
    List<Task> active =
        taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
    if (active.isEmpty()) {
      List<String> subtree = collectRuntimeSubtreeIds(processInstanceId);
      if (subtree.size() > 1) {
        active = taskService.createTaskQuery().processInstanceIdIn(subtree).active().list();
      }
    }
    if (active.isEmpty())
      return "No active task found";

    // 尋找有 nextAssignee
    Task target = null;
    for (Task t : active) {
      // 先看 task-local
      String naLocal = (String) taskService.getVariableLocal(t.getId(), "nextAssignee");
      // 再看該任務所屬子流程
      String tPi = t.getProcessInstanceId();
      String naPi = (String) runtimeService.getVariable(tPi, "nextAssignee");
      // 最後看父流程f
      String naParent = processInstanceId.equals(tPi) ? null
          : (String) runtimeService.getVariable(processInstanceId, "nextAssignee");

      String cand = firstNonEmpty(naLocal, naPi, naParent);
      if (cand != null) {
        target = t;
        break;
      }
    }

    if (target == null) {
      // 如果無法判斷哪顆是「指派」任務，但只有 1 顆任務，就先用它；否則請指定 taskId
      if (active.size() == 1) {
        target = active.get(0);
      } else {
        return "偵測到多筆待辦且無 nextAssignee 線索，請指定 taskId 完成：\n" + formatTaskList(active);
      }
    }

    String taskPi = target.getProcessInstanceId();

    // 取得 nextAssignee 欄位名，依序查 task-local → 子流程 → 父流程
    String nextAssignee =
        firstNonEmpty((String) taskService.getVariableLocal(target.getId(), "nextAssignee"),
            (String) runtimeService.getVariable(taskPi, "nextAssignee"),
            processInstanceId.equals(taskPi) ? null
                : (String) runtimeService.getVariable(processInstanceId, "nextAssignee"));

    if (nextAssignee == null || nextAssignee.isEmpty()) {
      return "找不到 nextAssignee 線索，無法判斷要指派哪個變數。請確認流程在指派節點有設定 nextAssignee。";
    }

    Map<String, Object> vars = new HashMap<>();

    // 指派：支援 countersign 與多位指派
    if ("countersignApprovers".equals(nextAssignee)) {
      // 支援一次輸入多位：A,B,C
      List<String> countersignApprovers = Arrays.asList("會簽A", "會簽B", "會簽C");
      vars.put(nextAssignee, countersignApprovers);
    } else {
      // 一般指派（單一字串）
      vars.put(nextAssignee, nextUser);
    }

    // 4) 重置 rejectedCount
    vars.put("rejectedCount", 0);

    // 5) 標記此任務為「指派人員」並完成
    vars.put("decision", "指派人員");
    taskService.setVariableLocal(target.getId(), "decision", "指派人員");

    try {
      // 把變數設在該任務PI，避免設錯到父流程
      runtimeService.setVariables(taskPi, vars);
      taskService.complete(target.getId());
    } catch (FlowableException e) {
      return "指派失敗：" + e.getMessage();
    }

    return "指派完成（變數：" + nextAssignee + "）";
  }

  // 收集父＋所有「還在跑」的子流程 IDs
  private List<String> collectRuntimeSubtreeIds(String rootPi) {
    List<String> all = new ArrayList<>();
    ArrayDeque<String> q = new ArrayDeque<>();
    all.add(rootPi);
    q.add(rootPi);
    while (!q.isEmpty()) {
      String pid = q.poll();
      List<ProcessInstance> children =
          runtimeService.createProcessInstanceQuery().superProcessInstanceId(pid).list();
      for (ProcessInstance c : children) {
        all.add(c.getId());
        q.add(c.getId());
      }
    }
    return all;
  }

  // 回傳第一個非空字串
  private static String firstNonEmpty(String... vals) {
    if (vals == null)
      return null;
    for (String s : vals) {
      if (s != null && !s.isEmpty())
        return s;
    }
    return null;
  }

  // 列出多顆任務供人工指定
  private String formatTaskList(List<Task> tasks) {
    StringBuilder sb = new StringBuilder();
    for (Task t : tasks) {
      sb.append(String.format("taskId=%s, key=%s, assignee=%s%n", t.getId(),
          t.getTaskDefinitionKey(), t.getAssignee() == null ? "(未指派)" : t.getAssignee()));
    }
    return sb.toString();
  }
}
