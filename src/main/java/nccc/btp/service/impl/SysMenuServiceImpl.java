/**
 * 
 */
package nccc.btp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.SysMenu;
import nccc.btp.repository.SysMenuRepository;
import nccc.btp.service.SysMenuService;
import nccc.btp.util.SecurityUtil;
import nccc.btp.vo.MenuNodeVo;

@Service
@Transactional
public class SysMenuServiceImpl implements SysMenuService {

  @Autowired
  private SysMenuRepository sysMenuRepository;

  @Override
  public List<MenuNodeVo> buildMenuTree() {
    List<SysMenu> allMenus = sysMenuRepository.findByStatusAndOdrNumNot999("Y");
    Map<String, MenuNodeVo> idToNode = new HashMap<>();
    List<MenuNodeVo> roots = new ArrayList<>();
    NcccUserDto user = SecurityUtil.getCurrentUser();
    String appAuthString = user.getAppAuthString();

    // 1. 先切出「冒號後面」的部分
    String authPart = "";
    if (appAuthString != null && appAuthString.contains(":")) {
      authPart = appAuthString.substring(appAuthString.indexOf(':') + 1);
    }

    // 2. 去掉尾端多餘的點號 ('.') 或逗號(',')
    authPart = authPart.replaceAll("[\\.,]+$", "");

    // 3. 再用逗號切分
    String[] tokens = authPart.isEmpty() ? new String[0] : authPart.split(",");

    // 4. 轉成 int[]
    int[] authValues =
        IntStream.range(0, tokens.length).map(i -> Integer.parseInt(tokens[i])).toArray();

    // 先建立所有節點
    for (SysMenu menu : allMenus) {
      Integer ldapSn = menu.getLdapSn();
      if (ldapSn == null) {
        continue; // 沒有值就跳過
      }

      boolean needAdd = false;
      if (ldapSn == 999) {
        needAdd = true;
      } else {
        // 改為超出索引一律視為 0
        int authVal = 0;
        int idx = ldapSn - 1;
        if (authValues != null && idx >= 0 && idx < authValues.length) { // [CHANGED]
          authVal = authValues[idx];
        }
        if (authVal != 0) {
          needAdd = true;
        }
      }

      if (needAdd) {
        MenuNodeVo node = new MenuNodeVo();
        node.setNodeName(menu.getMenuName());
        node.setIconName(menu.getIconName());
        node.setUrl(menu.getPageUri());
        idToNode.put(menu.getMenuId(), node);
      }
    }

    // 建立樹狀結構
    for (SysMenu menu : allMenus) {
      String parentId = menu.getParentId();
      MenuNodeVo node = idToNode.get(menu.getMenuId());
      if (node == null) {
        continue; // 如果節點不存在，跳過
      }
      if (parentId == null || parentId.isEmpty()) {
        roots.add(node);
      } else {
        MenuNodeVo parent = idToNode.get(parentId);
        if (parent != null) {
          parent.getChildren().add(node);
        }
      }
    }

    // 過濾掉沒有 children 以及uri為null 的 root 節點(隱藏父類別選單)
    roots.removeIf(root -> root != null && (root.getChildren() == null || root.getChildren().isEmpty())&&root.getUrl()==null);

    return roots;
  }
}
