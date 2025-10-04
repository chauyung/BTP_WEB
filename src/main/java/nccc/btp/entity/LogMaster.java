package nccc.btp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LOG_MASTER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogMaster {

  @Id
  @Column(name = "GUID", length = 60)
  private String guid;

  @Column(name = "WRITE_DATE", length = 8)
  private String writeDate;

  @Column(name = "WRITE_TIME", length = 8)
  private String writeTime;

  @Column(name = "SYSTEM_ID", length = 8)
  private String systemId;

  @Column(name = "FUNCTION_ID", length = 10)
  private String functionId;

  @Column(name = "FUNCTION_NAME", length = 100)
  private String functionName;

  @Column(name = "REQUEST_URL", length = 100)
  private String requestUrl;

  @Column(name = "DEPT_ID", length = 10)
  private String deptId;

  @Column(name = "TEAM_ID", length = 12)
  private String teamId;

  @Column(name = "USER_ID", length = 50)
  private String userId;

  @Column(name = "USER_NAME", length = 30)
  private String userName;

  @Column(name = "ACCESS_DATE", length = 8)
  private String accessDate;

  @Column(name = "ACCESS_TIME", length = 8)
  private String accessTime;

  @Column(name = "ACCESS_TYPE", length = 2)
  private String accessType;

  @Column(name = "SUCCESS_FLAG", length = 1)
  private String successFlag;

  @Column(name = "SOURCE_IP", length = 50)
  private String sourceIp;

  @Column(name = "TARGET_IP", length = 50)
  private String targetIp;

  @Column(name = "QUERY_INPUT", length = 2000)
  private String queryInput;

  @Column(name = "FUNCTION_COUNT", length = 12)
  private String functionCount;

  @Column(name = "TIMESTAMP", length = 27)
  private String timeStamp;

}