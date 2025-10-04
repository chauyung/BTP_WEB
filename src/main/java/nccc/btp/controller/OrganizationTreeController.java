package nccc.btp.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import nccc.btp.entity.SyncOU;
import nccc.btp.entity.SyncUser;
import nccc.btp.service.OrganizationTreeService;

@RestController
@RequestMapping("/organizationTree")
public class OrganizationTreeController {

  @Autowired
  OrganizationTreeService organizationTreeService;


  // 取得組織樹清單
  @PostMapping("/tree")
  public List<SyncOU> getTree() {
    return organizationTreeService.getOrganizationTree();
  }

  // 取得指定部門的使用者
  @PostMapping("/users")
  public List<SyncUser> getUsers(@RequestParam String ouCode) {
    return organizationTreeService.getUsersByOuCode(ouCode);
  }
}
