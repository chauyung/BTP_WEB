package nccc.btp.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateUtil {

  // 默認日期時間格式
  private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


  // 將字串轉換為LocalDateTime
  public static LocalDateTime parse(String dateTimeStr) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
    return LocalDateTime.parse(dateTimeStr, formatter);
  }

  // 將LocalDateTime轉換為字串
  public static String format(LocalDateTime dateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
    return dateTime.format(formatter);
  }

  // 獲取當前時間的字串表示
  public static String now() {
    return format(LocalDateTime.now());
  }

  public static String convertRocToGregorian(String rocDate) {
    if (rocDate == null || rocDate.length() < 6) {
      throw new IllegalArgumentException("無效的民國年日期: " + rocDate);
    }
    // 年份部分：總長度 - 4
    int len = rocDate.length();
    String rocYearStr = rocDate.substring(0, len - 4);
    String monthStr = rocDate.substring(len - 4, len - 2);
    String dayStr = rocDate.substring(len - 2);

    int rocYear;
    try {
      rocYear = Integer.parseInt(rocYearStr);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("民國年必須為數字: " + rocYearStr, e);
    }

    int adYear = rocYear + 1911;
    // 輸出為 4 碼西元年 + 月 + 日
    return String.format("%04d%s%s", adYear, monthStr, dayStr);
  }

  /**
   * 將指定的日期物件格式化為 "YYYYMM" (年月份) 的字串形式。
   *
   * @param date 要格式化的日期物件
   * @return 格式化後的 "YYYYMM" 字串，如果輸入為 null 則回傳 null。
   */
  public static String getYyyyMm(Date date) {
    if (date == null) {
      return null; // 或者可以回傳空字串 ""
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
    return sdf.format(date);
  }

  /**
   * 檢查
   * @param dateStr
   * @return
   */
  public static boolean isValidDate(String dateStr) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    try {
      LocalDate.parse(dateStr, formatter);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  public static String changeRocDateToNormalDate(String dateStr) {

    String result = "";

    if (dateStr.matches("\\d{7}"))
    {
      try
      {
        // 解析民國年月日
        int rocYear = Integer.parseInt(dateStr.substring(0, 3));
        int month = Integer.parseInt(dateStr.substring(3, 5));
        int day = Integer.parseInt(dateStr.substring(5, 7));

        int gregorianYear = rocYear + 1911;

        LocalDate date = LocalDate.of(gregorianYear, month, day);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        result = date.format(formatter);
      }
      catch (RuntimeException e)
      {

      }
    }

    return result;
  }

  public static String toYyyyMmDdString(LocalDate date) {
    return date != null ? date.format(DATE_FORMATTER) : null;
  }

}
