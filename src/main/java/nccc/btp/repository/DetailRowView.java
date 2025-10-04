package nccc.btp.repository;

import java.math.BigDecimal;

public interface DetailRowView {
    String getYymm();
    String getVersion();
    String getApproation();
    String getOuCode();
    String getOuName();
    String getAccounting();
    String getSubject();
    String getOperateItemCode();
    String getOperateItem();
    BigDecimal getAmount();
}
