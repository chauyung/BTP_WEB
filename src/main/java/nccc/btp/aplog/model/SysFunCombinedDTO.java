package nccc.btp.aplog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysFunCombinedDTO {

  // From SysFun (Order 1-12)
    private String uuid; // 1
    private String menuLink; // 2
    private String funId; // 3
    private String funName; // 4
    private String uri; // 5
    private String pageEntry; // 6
    private String menuId; // 7 <-- 這個欄位將用於關聯
    private String ldapFunId; // 8
    private Integer ldapAttribute; // 9
    private Integer approvalLevel; // 10
    private String apiName; // 11
    private Integer apiGroup; // 12

    // From SysFunUri (Order 13-17)
    private String loginRequired; // 13
    private String apLog; // 14
    private String apLogAccessType; // 15
    private String apLogCount; // 16
    private String uriDescription; // 17

    // From SysMenu (Order 18-25)
    private String menuNameFromMenu; // 18
    private String menuType; // 19
    private String parentId; // 20
    private Integer odrNum; // 21
    private String status; // 22
    private String iconName; // 23
    private String pageUri; // 24
    private Integer ldapSn; // 25
  }
