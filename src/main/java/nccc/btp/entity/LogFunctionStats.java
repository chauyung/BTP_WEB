package nccc.btp.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LOG_FUNCTION_STATS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogFunctionStats {

  @EmbeddedId
  private LogFunctionStatsId id;

  @Column(name = "FUNCTION_NAME", length = 100)
  private String functionName;

  @Column(name = "FUNCTION_USE_COUNT")
  private Long functionUseCount;
}