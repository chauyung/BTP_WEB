/**
 * 
 */
package nccc.btp.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nccc.btp.service.SysMenuService;
import nccc.btp.vo.MenuNodeVo;

@RestController
@RequestMapping("/menu")
public class SysMenuController {
  
  @Autowired
  private SysMenuService sysMenuService;

  @GetMapping("/getMenuTree")
  public List<MenuNodeVo> getMenuTree() {
    return sysMenuService.buildMenuTree();
  }
}
