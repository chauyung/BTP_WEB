package nccc.btp.util;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CsvUtil {

  private static final Charset BIG5 = Charset.forName("Big5");

  private CsvUtil() {}

  /** 產生 Big5 CSV 位元組（不含 BOM） */
  public static byte[] toCsvBytes(String[] labels, String[] keys, List<Map<String, Object>> rows) {
    String csv = toCsvString(labels, keys, rows);
    return csv.getBytes(BIG5); // 直接用 Big5 編碼
  }

  /** 產出純 CSV 字串 */
  public static String toCsvString(String[] labels, String[] keys, List<Map<String, Object>> rows) {
    StringBuilder sb = new StringBuilder();

    // 表頭
    writeLine(sb, labels);
    sb.append("\r\n");

    // 內容
    if (rows != null) {
      for (Map<String, Object> r : rows) {
        String[] cols = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
          String v = Objects.toString(r.get(keys[i]), "");
          cols[i] = esc(safe(v));
        }
        writeLine(sb, cols);
        sb.append("\r\n");
      }
    }
    return sb.toString();
  }

  // ===== 小工具 =====

  private static void writeLine(StringBuilder sb, String[] cols) {
    for (int i = 0; i < cols.length; i++) {
      if (i > 0)
        sb.append(',');
      sb.append(cols[i] == null ? "" : cols[i]);
    }
  }

  private static String esc(String v) {
    if (v == null)
      return "";
    boolean needQuote = v.contains(",") || v.contains("\"") || v.contains("\r") || v.contains("\n");
    String out = v.replace("\"", "\"\"");
    return needQuote ? ("\"" + out + "\"") : out;
  }

  private static String safe(String v) {
    if (v == null || v.isEmpty())
      return "";
    char c = v.charAt(0);
    return (c == '=' || c == '+' || c == '-' || c == '@') ? "'" + v : v;
  }
}
