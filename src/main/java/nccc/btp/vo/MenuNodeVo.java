/**
 * 
 */
package nccc.btp.vo;

import java.util.ArrayList;
import java.util.List;

public class MenuNodeVo {

  private String nodeName;

  private String iconName;

  private String url;

  private List<MenuNodeVo> children;

  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  public String getIconName() {
    return iconName;
  }

  public void setIconName(String iconName) {
    this.iconName = iconName;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public List<MenuNodeVo> getChildren() {
    if (children == null) {
      children = new ArrayList<>();
    }
    return children;
  }

  public void setChildren(List<MenuNodeVo> children) {
    this.children = children;
  }

}
