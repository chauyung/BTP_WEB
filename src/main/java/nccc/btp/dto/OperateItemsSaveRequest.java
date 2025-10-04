package nccc.btp.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OperateItemsSaveRequest {

  @Data
  public static class Key {
    private String yymm;
    private String version;
    private String approation;
    private String ouCode;
    private String accounting;
  }

  @Data
  public static class CreateItem {
    private String operateItemCode;
    private String operateItem;
    private BigDecimal amount;
    private String remark;
  }

  @Data
  public static class UpdateItem {
    private Long id;
    private String operateItemCode;
    private BigDecimal amount;
    private String remark;
  }

  private Key key;
  private List<CreateItem> createItems;
  private List<UpdateItem> updateItems;
}
