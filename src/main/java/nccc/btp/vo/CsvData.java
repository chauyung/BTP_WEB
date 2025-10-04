package nccc.btp.vo;

import java.util.List;
import java.util.Map;

/**
 * 產生 CSV 檔案的資料結構
 */
public class CsvData {

  private List<Map<String, String>> headers;

  private List<Map<String, Object>> rows;

  public List<Map<String, String>> getHeaders() {
    return headers;
  }

  public void setHeaders(List<Map<String, String>> headers) {
    this.headers = headers;
  }

  public List<Map<String, Object>> getRows() {
    return rows;
  }

  public void setRows(List<Map<String, Object>> rows) {
    this.rows = rows;
  }

}
