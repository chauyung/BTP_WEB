/**
 * 
 */
package nccc.btp.service;

import java.util.List;
import nccc.btp.vo.MenuNodeVo;

public interface SysMenuService {
  
  List<MenuNodeVo> buildMenuTree();

}
