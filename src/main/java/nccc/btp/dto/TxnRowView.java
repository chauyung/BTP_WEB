package nccc.btp.dto;

import java.math.BigDecimal;
import java.util.Date;

public interface TxnRowView {
    String getYm();
    String getVersion();
    String getOuCode();
    String getOuName();
    String getAccCode();
    String getAccName();
    String getApproation();
    BigDecimal getAmount();
    String getRemark();
    Date getCreateDate();
    String getVendorCode();
    String getVendorName();
    String getSourceName();
    String getTxnType();
}
