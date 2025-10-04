package nccc.btp.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import nccc.btp.dto.TaskInfoDto;
import nccc.btp.dto.TaskSearchRequest;
import nccc.btp.enums.ProcessDefinitionKey;
import nccc.btp.service.ProcessInfoService; // ← 介面改名

@Service
public class ProcessInfoServiceImpl implements ProcessInfoService {

  @Autowired
  private RuntimeService runtimeService;
  @Autowired
  private TaskService taskService;
  @Autowired
  private HistoryService historyService;
  @Autowired
  private RepositoryService repositoryService;

  /** 解析/比對前端字串日期 "yyyy-MM-dd" */
  private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd");

  @Override
  public List<TaskInfoDto> searchTasks(TaskSearchRequest request) {
      // 讀取前端條件
      final String processName = trimOrNull(request.getProcessName());
      final String applicantHrid = trimOrNull(request.getApplicant());
      final String submissionStr = trimOrNull(request.getSubmissionDate()); // yyyy-MM-dd
      final Date submissionDay = parseDateQuietly(submissionStr);
      final String taskStatus = trimOrNull(request.getTaskStatus());
      final String businessKey = trimOrNull(request.getBusinessKey());
      final String keywords = trimOrNull(request.getKeywords());

      // 狀態篩選
      final boolean wantAll = !hasText(taskStatus) || "全部".equals(taskStatus);
      final boolean wantActive = "處理中".equals(taskStatus);
      final boolean wantFinished = "已結束".equals(taskStatus);

      List<TaskInfoDto> results = new ArrayList<>();

      /* 一、活動中任務 */
      if (wantAll || wantActive) {
          TaskQuery tq = taskService.createTaskQuery();

          if (hasText(businessKey)) {
              tq.processInstanceBusinessKey(businessKey);
          }
          if (hasText(applicantHrid)) {
              tq.processVariableValueEquals("initiator", applicantHrid);
          }

          List<Task> activeTasks = tq.list();
          for (Task t : activeTasks) {
              ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                      .processInstanceId(t.getProcessInstanceId()).singleResult();
              if (pi == null)
                  continue;

              if (submissionDay != null && !sameDay(pi.getStartTime(), submissionDay))
                  continue;

              String defName = getProcessDefinitionName(pi.getProcessDefinitionId());
              if (ProcessDefinitionKey.getDisplayNameByKey(defName) != null) {
                  defName = ProcessDefinitionKey.getDisplayNameByKey(defName);
              }
              if (hasText(processName) && !defName.contains(processName))
                  continue;

              Map<String, Object> vars = runtimeService.getVariables(pi.getId(),
                      Arrays.asList("initiator", "submissionDate", "applyDate"));
              String initiator = toStr(vars.get("initiator"));
              String submissionVal = firstNonEmpty(toStr(vars.get("submissionDate")),
                      toStr(vars.get("applyDate")), formatDate(pi.getStartTime()));
              if (hasText(applicantHrid) && !applicantHrid.equals(initiator))
                  continue;

              String statusDesc = hasText(t.getName()) ? t.getName() : toStr(t.getTaskDefinitionKey());

              TaskInfoDto dto = new TaskInfoDto();
              dto.setBusinessKey(pi.getBusinessKey());
              dto.setProcessName(defName);
              dto.setApplicant(initiator);
              dto.setSubmissionDate(submissionVal);
              dto.setStatus(statusDesc);
              results.add(dto);
          }
      }

      /* 二、已結束流程 */
      if (wantAll || wantFinished) {
          HistoricProcessInstanceQuery hq =
                  historyService.createHistoricProcessInstanceQuery().finished();

          if (hasText(businessKey)) {
              hq.processInstanceBusinessKey(businessKey);
          }
          if (hasText(processName)) {
              hq.processDefinitionName(processName);
          }
          if (hasText(applicantHrid)) {
              hq.variableValueEquals("initiator", applicantHrid);
          }

          List<HistoricProcessInstance> finished = hq.list();
          for (HistoricProcessInstance h : finished) {
              if (submissionDay != null && !sameDay(h.getStartTime(), submissionDay))
                  continue;

              String defName = getProcessDefinitionName(h.getProcessDefinitionId());
              if (ProcessDefinitionKey.getDisplayNameByKey(defName) != null) {
                  defName = ProcessDefinitionKey.getDisplayNameByKey(defName);
              }

              List<HistoricVariableInstance> varList = historyService
                      .createHistoricVariableInstanceQuery().processInstanceId(h.getId()).list();
              Map<String, String> hv = toNameValMap(varList);

              String initiator = hv.getOrDefault("initiator", "");
              String submissionVal = firstNonEmpty(hv.get("submissionDate"), hv.get("applyDate"),
                      formatDate(h.getStartTime()));
              if (hasText(applicantHrid) && !applicantHrid.equals(initiator))
                  continue;

              HistoricTaskInstance lastTask = historyService.createHistoricTaskInstanceQuery()
                      .processInstanceId(h.getId()).finished().orderByHistoricTaskInstanceEndTime().desc()
                      .listPage(0, 1).stream().findFirst().orElse(null);

              String statusDesc = null;
              if (lastTask != null && hasText(lastTask.getName())) {
                  statusDesc = lastTask.getName();
              }

              if (!hasText(statusDesc) && hasText(h.getEndActivityId())) {
                  HistoricActivityInstance hai = historyService.createHistoricActivityInstanceQuery()
                          .processInstanceId(h.getId()).activityId(h.getEndActivityId()).singleResult();
                  if (hai != null && hasText(hai.getActivityName())) {
                      statusDesc = hai.getActivityName();
                  }
              }

              if (!hasText(statusDesc) && hasText(h.getDeleteReason())) {
                  statusDesc = h.getDeleteReason();
              }

              if (!hasText(statusDesc)) {
                  statusDesc = "結束";
              }

              TaskInfoDto dto = new TaskInfoDto();
              dto.setBusinessKey(h.getBusinessKey());
              dto.setProcessName(defName);
              dto.setApplicant(initiator);
              dto.setSubmissionDate(submissionVal);
              dto.setStatus(statusDesc);
              results.add(dto);
          }
      }

      /* 三、關鍵字過濾 */
      if (hasText(keywords)) {
          final String kw = keywords.toLowerCase();
          results.removeIf(dto -> !containsIgnoreCase(dto.getApplicant(), kw)
                  && !containsIgnoreCase(dto.getProcessName(), kw));
      }

      return results;
  }

  /* ===== 工具方法 ===== */

  private static Date parseDateQuietly(String s) {
    if (!hasText(s))
      return null;
    try {
      return DATE_FMT.parse(s);
    } catch (ParseException e) {
      return null;
    }
  }

  private static boolean sameDay(Date a, Date b) {
    if (a == null || b == null)
      return false;
    return DATE_FMT.format(a).equals(DATE_FMT.format(b));
  }

  private String getProcessDefinitionName(String processDefinitionId) {
    if (!hasText(processDefinitionId))
      return "";
    ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
        .processDefinitionId(processDefinitionId).singleResult();
    return pd != null ? pd.getName() : "";
  }

  private static Map<String, String> toNameValMap(List<HistoricVariableInstance> list) {
    Map<String, String> map = new HashMap<>();
    for (HistoricVariableInstance v : list) {
      map.put(v.getVariableName(), v.getValue() != null ? v.getValue().toString() : "");
    }
    return map;
  }

  private static boolean containsIgnoreCase(String src, String kwLower) {
    return hasText(src) && src.toLowerCase().contains(kwLower);
  }

  private static String toStr(Object o) {
    return o == null ? "" : o.toString();
  }

  private static boolean hasText(String s) {
    return StringUtils.hasText(s);
  }

  private static String trimOrNull(String s) {
    return s == null ? null : s.trim();
  }

  private static String formatDate(Date d) {
    return d == null ? "" : DATE_FMT.format(d);
  }

  /** 回傳第一個非空且有內容的字串，若全為空則回傳空字串 */
  private static String firstNonEmpty(String... values) {
    if (values != null) {
      for (String v : values) {
        if (StringUtils.hasText(v)) {
          return v;
        }
      }
    }
    return "";
  }
}