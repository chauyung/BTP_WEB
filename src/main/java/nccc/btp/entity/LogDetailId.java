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
public class LogDetailId implements Serializable {

  @Column(name = "GUID", length = 60)
  private String guid;

  @Column(name = "WRITE_DATE", length = 8)
  private String writeDate;

  @Column(name = "WRITE_TIME", length = 8)
  private String writeTime;


}
