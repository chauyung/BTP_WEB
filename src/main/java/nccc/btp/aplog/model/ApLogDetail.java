package nccc.btp.aplog.model;

public class ApLogDetail {
  private String guid;
  private String writeDate;
  private String writeTIme;
  private String sqlStatement1;
  private String sqlStatement2;
  private String beforeImage;
  private String afterImage;
  private String sqlCode;
  private String queryCount;
  private String timestamp; // 新增的 TIMESTAMP 欄位

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getWriteDate() {
    return writeDate;
  }

  public void setWriteDate(String writeDate) {
    this.writeDate = writeDate;
  }

  public String getWriteTIme() {
    return writeTIme;
  }

  public void setWriteTIme(String writeTIme) {
    this.writeTIme = writeTIme;
  }

  public String getSqlStatement1() {
    return sqlStatement1;
  }

  public void setSqlStatement1(String sqlStatement1) {
    this.sqlStatement1 = sqlStatement1;
  }

  public String getSqlStatement2() {
    return sqlStatement2;
  }

  public void setSqlStatement2(String sqlStatement2) {
    this.sqlStatement2 = sqlStatement2;
  }

  public String getBeforeImage() {
    return beforeImage;
  }

  public void setBeforeImage(String beforeImage) {
    this.beforeImage = beforeImage;
  }

  public String getAfterImage() {
    return afterImage;
  }

  public void setAfterImage(String afterImage) {
    this.afterImage = afterImage;
  }

  public String getSqlCode() {
    return sqlCode;
  }

  public void setSqlCode(String sqlCode) {
    this.sqlCode = sqlCode;
  }

  public String getQueryCount() {
    return queryCount;
  }

  public void setQueryCount(String queryCount) {
    this.queryCount = queryCount;
  }

  // Getter and Setter for the new 'timestamp' field
  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "ApLogDetail [guid=" + guid + ", writeDate=" + writeDate + ", writeTIme=" + writeTIme
        + ", sqlStatement1=" + sqlStatement1 + ", sqlStatement2=" + sqlStatement2 + ", beforeImage="
        + beforeImage + ", afterImage=" + afterImage + ", sqlCode=" + sqlCode + ", queryCount="
        + queryCount + ", timestamp=" + timestamp + "]"; // 更新 toString()
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((afterImage == null) ? 0 : afterImage.hashCode());
    result = prime * result + ((beforeImage == null) ? 0 : beforeImage.hashCode());
    result = prime * result + ((guid == null) ? 0 : guid.hashCode());
    result = prime * result + ((queryCount == null) ? 0 : queryCount.hashCode());
    result = prime * result + ((sqlCode == null) ? 0 : sqlCode.hashCode());
    result = prime * result + ((sqlStatement1 == null) ? 0 : sqlStatement1.hashCode());
    result = prime * result + ((sqlStatement2 == null) ? 0 : sqlStatement2.hashCode());
    result = prime * result + ((writeDate == null) ? 0 : writeDate.hashCode());
    result = prime * result + ((writeTIme == null) ? 0 : writeTIme.hashCode());
    result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode()); // 更新 hashCode()
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ApLogDetail other = (ApLogDetail) obj;
    if (afterImage == null) {
      if (other.afterImage != null)
        return false;
    } else if (!afterImage.equals(other.afterImage))
      return false;
    if (beforeImage == null) {
      if (other.beforeImage != null)
        return false;
    } else if (!beforeImage.equals(other.beforeImage))
      return false;
    if (guid == null) {
      if (other.guid != null)
        return false;
    } else if (!guid.equals(other.guid))
      return false;
    if (queryCount == null) {
      if (other.queryCount != null)
        return false;
    } else if (!queryCount.equals(other.queryCount))
      return false;
    if (sqlCode == null) {
      if (other.sqlCode != null)
        return false;
    } else if (!sqlCode.equals(other.sqlCode))
      return false;
    if (sqlStatement1 == null) {
      if (other.sqlStatement1 != null)
        return false;
    } else if (!sqlStatement1.equals(other.sqlStatement1))
      return false;
    if (sqlStatement2 == null) {
      if (other.sqlStatement2 != null)
        return false;
    } else if (!sqlStatement2.equals(other.sqlStatement2))
      return false;
    if (writeDate == null) {
      if (other.writeDate != null)
        return false;
    } else if (!writeDate.equals(other.writeDate))
      return false;
    if (writeTIme == null) {
      if (other.writeTIme != null)
        return false;
    } else if (!writeTIme.equals(other.writeTIme))
      return false;
    if (timestamp == null) { // 更新 equals() 檢查
      if (other.timestamp != null)
        return false;
    } else if (!timestamp.equals(other.timestamp))
      return false;
    return true;
  }
}