package nccc.btp.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.SyncUser;
import nccc.btp.repository.SyncUserRepository;
import nccc.btp.service.ProcessMaintenanceService;

@Service
public class ProcessMaintenanceServiceImpl implements ProcessMaintenanceService {

  private final RuntimeService runtimeService;
  private final TaskService taskService;
  private final IdentityService identityService;
  private final SyncUserRepository syncUserRepository;

  public ProcessMaintenanceServiceImpl(RuntimeService runtimeService, TaskService taskService,
      IdentityService identityService, SyncUserRepository syncUserRepository) {
    this.runtimeService = runtimeService;
    this.taskService = taskService;
    this.identityService = identityService;
    this.syncUserRepository = syncUserRepository;
  }

  @Override
  @Transactional
  public void reassignByBusinessKey(String businessKey, String targetHrId,
      HttpServletRequest request) {
    if (!StringUtils.hasText(businessKey)) {
      throw new IllegalArgumentException("businessKey 不可為空");
    }
    if (!StringUtils.hasText(targetHrId)) {
      throw new IllegalArgumentException("targetHrId 不可為空");
    }

    ProcessInstance pi = runtimeService.createProcessInstanceQuery()
        .processInstanceBusinessKey(businessKey).singleResult();
    if (pi == null) {
      throw new IllegalStateException("找不到進行中流程，單號：" + businessKey);
    }

    List<Task> activeTasks =
        taskService.createTaskQuery().processInstanceId(pi.getId()).active().list();
    if (activeTasks.isEmpty()) {
      throw new IllegalStateException("目前無進行中任務可換簽");
    }
    if (activeTasks.size() > 1) {
      String names = activeTasks.stream().map(Task::getName).collect(Collectors.joining(", "));
      throw new IllegalStateException("同時有多個進行中任務，請指定節點。當前任務：" + names);
    }

    Task task = activeTasks.get(0);
    SyncUser target = syncUserRepository.findFirstByHrid(targetHrId)
        .orElseThrow(() -> new IllegalArgumentException("找不到使用者 hrId=" + targetHrId));
    String newAssignee = target.getAccount();

    // 取得當前的 authenticated user
    String originalAuth = Authentication.getAuthenticatedUserId();
    HttpSession session = request.getSession(false);
    NcccUserDto operatorUser = (NcccUserDto) session.getAttribute("SESSION_USER");
    if (operatorUser != null) {
      String operatorUserId = operatorUser.getHrid();
      try {
        if (StringUtils.hasText(operatorUserId)) {
          Authentication.setAuthenticatedUserId(operatorUserId);
        }
        String from = task.getAssignee();
        taskService.addComment(task.getId(), pi.getId(), "任務換簽：" + from + " → " + newAssignee);
        taskService.setAssignee(task.getId(), newAssignee);
      } finally {
        Authentication.setAuthenticatedUserId(originalAuth);
      }
    }

  }
}
