package nccc.btp.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class StringUtil {

  // 判斷字串是否為空或僅包含空白字符
  public static boolean isEmpty(String str) {
      return str == null || str.trim().isEmpty();
  }

  // 判斷字串是否為有效的電子郵件格式
  public static boolean isValidEmail(String email) {
      String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
      Pattern pat = Pattern.compile(emailRegex);
      return email != null && pat.matcher(email).matches();
  }

  // 將首字母大寫
  public static String capitalize(String str) {
      if (isEmpty(str)) {
          return str;
      }
      return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
  }

  /**
   * 移除千分位符號，並轉為 BigDecimal。
   *
   * @param text 含千分位的數字字串，例如 "1,234,567.89"
   * @return 轉換後的 BigDecimal
   * @throws NumberFormatException 如果移除逗號後仍非合法數字
   */
  public static BigDecimal parseByReplace(String text) {
    if (text == null || text.isEmpty()) {
      return BigDecimal.ZERO; // 視需求可改成拋例外
    }
    // 移除所有逗號
    String sanitized = text.replace(",", "");
    // 用字串建構子，避免精度問題
    return new BigDecimal(sanitized);
  }

  public static String bigDecimalToString(BigDecimal amount) {

    if (amount == null) {
      return "0.00";
    }
    if (amount.compareTo(BigDecimal.ZERO) < 0) {
      return "[錯誤] 金額不可為負數";
    }

    amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);

    DecimalFormat df = new DecimalFormat("#,##0.00");
    return df.format(amount);

  }
}
