package nccc.btp.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LOG_DETAIL")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogDetail {

  @EmbeddedId
  private LogDetailId id;

  @Column(name = "SQL_STATEMENT_1", length = 4000)
  private String sqlStatement1;

  @Column(name = "SQL_STATEMENT_2", length = 4000)
  private String sqlStatement2;

  @Column(name = "BEFORE_IMAGE", length = 4000)
  private String beforeImage;

  @Column(name = "AFTER_IMAGE", length = 4000)
  private String afterImage;

  @Column(name = "SQL_CODE", length = 20)
  private String sqlCode;

  @Column(name = "QUERY_COUNT", length = 10)
  private String queryCount;

  @Column(name = "TIMESTAMP", length = 27)
  private String timeStamp;
}
