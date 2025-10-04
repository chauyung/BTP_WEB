package nccc.btp.controller;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nccc.btp.dto.TaskInfoDto;
import nccc.btp.dto.TaskSearchRequest;
import nccc.btp.response.ApiResponse;
import nccc.btp.service.ProcessInfoService;

@RestController
@RequestMapping("/taskQuery")
public class ProcessInfoController {

  @Autowired
  private ProcessInfoService processInfoService;

  /**
   * POST /taskQuery/searchTasks 將查詢條件 (@RequestBody TaskSearchRequest) 傳給 Service 執行，並用 ApiResponse
   * 包裝回傳結果
   */
  @PostMapping(value = "/searchTasks", consumes = "application/json", produces = "application/json")
  public ApiResponse<List<TaskInfoDto>> searchTasks(@RequestBody TaskSearchRequest request) {
    List<TaskInfoDto> results = processInfoService.searchTasks(request);
    if (results == null) {
      results = Collections.emptyList(); // 避免回傳 null
    }
    return ApiResponse.success(results);
  }
}