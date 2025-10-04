package nccc.btp.service;

import java.util.List;
import nccc.btp.dto.TaskInfoDto;
import nccc.btp.dto.TaskSearchRequest;

/**
 * 定義針對 Flowable 查詢任務的 Service 介面
 */
public interface ProcessInfoService {

  /**
   * 根據查詢條件 TaskSearchRequest，查詢 Flowable 中符合的 Active Tasks 及 Historic ProcessInstances， 並回傳
   * List<TaskInfoDto>
   *
   * @param request 前端查詢條件
   * @return 多筆 TaskInfoDto 結果
   */
  List<TaskInfoDto> searchTasks(TaskSearchRequest request);
}