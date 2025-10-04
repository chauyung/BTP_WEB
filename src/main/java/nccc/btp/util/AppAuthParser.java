package nccc.btp.util;

import java.util.HashMap;
import java.util.Map;

public class AppAuthParser {

  // 47個功能代碼，順序要與權限字串一致
  public static final String[] FUNCTION_CODES = {"A001", "A002", "A003", "A004", "A005", "A006",
      "A007", "A008", "A009", "A010", "A011", "A012", "A013", "A014", "A015", "A016", "A017",
      "A018", "A019", "A020", "A021", "A022", "A023", "A024", "A025", "A026", "A027", "A028",
      "A029", "A030", "A031", "A032", "A033", "A034", "A035", "A036", "A037", "A038", "A039",
      "A040", "A041", "A042", "A043", "A044", "A045", "A046", "A047"};

  public static final int MODIFY = 1; // 修改
  public static final int DELETE = 2; // 刪除
  public static final int ADD = 4; // 新增
  public static final int QUERY = 8; // 查詢
  public static final int APPROVE = 16; // 覆核

  /**
   * 解析權限字串為 Map<功能代碼, 權限整數>
   */
  public static Map<String, Integer> parse(String appAuthString) {
    Map<String, Integer> map = new HashMap<>();
    if (appAuthString == null || !appAuthString.contains(":"))
      return map;
    String s = appAuthString.substring(appAuthString.indexOf(":") + 1).replace(".", "");
    String[] arr = s.split(",");
    for (int i = 0; i < FUNCTION_CODES.length; i++) {
      String code = FUNCTION_CODES[i];
      int val = 0;
      if (i < arr.length) {
        try {
          val = Integer.parseInt(arr[i].trim());
        } catch (Exception e) {
          val = 0;
        }
      }
      map.put(code, val);
    }
    return map;
  }

  /**
   * 判斷功能權限值是否包含某種權限
   * 
   * @param value 權限整數
   * @param permission 權限位元，例如 AppAuthParser.QUERY
   * @return 是否有該權限
   */
  public static boolean hasPermission(int value, int permission) {
    return (value & permission) > 0;
  }
}