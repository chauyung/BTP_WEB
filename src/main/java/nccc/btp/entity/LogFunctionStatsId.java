package nccc.btp.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogFunctionStatsId implements Serializable {

  @Column(name = "SYS_MONTH", length = 6)
  private String sysMonth;

  @Column(name = "SYSTEM_ID", length = 8)
  private String systemId;

  @Column(name = "FUNCTION_ID", length = 10)
  private String functionId;
}