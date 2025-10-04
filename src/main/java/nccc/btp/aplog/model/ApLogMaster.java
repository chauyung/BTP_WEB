package nccc.btp.aplog.model;

public class ApLogMaster {
  private String guid;
  private String systemId;
  private String functionId;
  private String functionName;
  private String requestUrl;
  private String deptId;
  private String teamId;
  private String userId;
  private String userName;
  private String accessDate;
  private String accessTime;
  private String writeDate;
  private String writeTime;
  private String accessType;
  private String successFlag;
  private String sourceIp;
  private String targetIp;
  private String qureyInput;
  private String functionCount;
  private String timestamp; // 新增的 TIMESTAMP 欄位，名稱改為 timestamp

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getSystemId() {
    return systemId;
  }

  public void setSystemId(String systemId) {
    this.systemId = systemId;
  }

  public String getFunctionId() {
    return functionId;
  }

  public void setFunctionId(String functionId) {
    this.functionId = functionId;
  }

  public String getFunctionName() {
    return functionName;
  }

  public void setFunctionName(String functionName) {
    this.functionName = functionName;
  }

  public String getRequestUrl() {
    return requestUrl;
  }

  public void setRequestUrl(String requestUrl) {
    this.requestUrl = requestUrl;
  }

  public String getDeptId() {
    return deptId;
  }

  public void setDeptId(String deptId) {
    this.deptId = deptId;
  }

  public String getTeamId() {
    return teamId;
  }

  public void setTeamId(String teamId) {
    this.teamId = teamId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getAccessDate() {
    return accessDate;
  }

  public void setAccessDate(String accessDate) {
    this.accessDate = accessDate;
  }

  public String getAccessTime() {
    return accessTime;
  }

  public void setAccessTime(String accessTime) {
    this.accessTime = accessTime;
  }

  public String getAccessType() {
    return accessType;
  }

  public void setAccessType(String accessType) {
    this.accessType = accessType;
  }

  public String getSuccessFlag() {
    return successFlag;
  }

  public void setSuccessFlag(String successFlag) {
    this.successFlag = successFlag;
  }

  public String getSourceIp() {
    return sourceIp;
  }

  public void setSourceIp(String sourceIp) {
    this.sourceIp = sourceIp;
  }

  public String getTargetIp() {
    return targetIp;
  }

  public void setTargetIp(String targetIp) {
    this.targetIp = targetIp;
  }

  public String getQureyInput() {
    return qureyInput;
  }

  public void setQureyInput(String qureyInput) {
    this.qureyInput = qureyInput;
  }

  public String getFunctionCount() {
    return functionCount;
  }

  public void setFunctionCount(String functionCount) {
    this.functionCount = functionCount;
  }


  public String getWriteDate() {
    return writeDate;
  }

  public void setWriteDate(String writeDate) {
    this.writeDate = writeDate;
  }

  public String getWriteTime() {
    return writeTime;
  }

  public void setWriteTime(String writeTime) {
    this.writeTime = writeTime;
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
    return "ApLogMaster [guid=" + guid + ", systemId=" + systemId + ", functionId=" + functionId
        + ", functionName=" + functionName + ", requestUrl=" + requestUrl + ", deptId=" + deptId
        + ", teamId=" + teamId + ", userId=" + userId + ", userName=" + userName + ", accessDate="
        + accessDate + ", accessTime=" + accessTime + ", accessType=" + accessType + ", sucessFlag="
        + successFlag + ", sourceIp=" + sourceIp + ", targetIp=" + targetIp + ", qureyInput="
        + qureyInput + ", functionCount=" + functionCount + "" + ", writeDate=" + writeDate
        + ", writeTime=" + writeTime + ", timestamp=" + timestamp + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((accessDate == null) ? 0 : accessDate.hashCode());
    result = prime * result + ((accessTime == null) ? 0 : accessTime.hashCode());
    result = prime * result + ((accessType == null) ? 0 : accessType.hashCode());
    result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
    result = prime * result + ((functionId == null) ? 0 : functionId.hashCode());
    result = prime * result + ((functionName == null) ? 0 : functionName.hashCode());
    result = prime * result + ((guid == null) ? 0 : guid.hashCode());
    result = prime * result + ((qureyInput == null) ? 0 : qureyInput.hashCode());
    result = prime * result + ((requestUrl == null) ? 0 : requestUrl.hashCode());
    result = prime * result + ((sourceIp == null) ? 0 : sourceIp.hashCode());
    result = prime * result + ((successFlag == null) ? 0 : successFlag.hashCode());
    result = prime * result + ((systemId == null) ? 0 : systemId.hashCode());
    result = prime * result + ((targetIp == null) ? 0 : targetIp.hashCode());
    result = prime * result + ((teamId == null) ? 0 : teamId.hashCode());
    result = prime * result + ((userId == null) ? 0 : userId.hashCode());
    result = prime * result + ((userName == null) ? 0 : userName.hashCode());
    result = prime * result + ((functionCount == null) ? 0 : functionCount.hashCode());
    result = prime * result + ((writeDate == null) ? 0 : writeDate.hashCode());
    result = prime * result + ((writeTime == null) ? 0 : writeTime.hashCode());
    result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode()); // 更新 hashCode
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
    ApLogMaster other = (ApLogMaster) obj;
    if (accessDate == null) {
      if (other.accessDate != null)
        return false;
    } else if (!accessDate.equals(other.accessDate))
      return false;
    if (accessTime == null) {
      if (other.accessTime != null)
        return false;
    } else if (!accessTime.equals(other.accessTime))
      return false;
    if (accessType == null) {
      if (other.accessType != null)
        return false;
    } else if (!accessType.equals(other.accessType))
      return false;
    if (deptId == null) {
      if (other.deptId != null)
        return false;
    } else if (!deptId.equals(other.deptId))
      return false;
    if (functionId == null) {
      if (other.functionId != null)
        return false;
    } else if (!functionId.equals(other.functionId))
      return false;
    if (functionName == null) {
      if (other.functionName != null)
        return false;
    } else if (!functionName.equals(other.functionName))
      return false;
    if (guid == null) {
      if (other.guid != null)
        return false;
    } else if (!guid.equals(other.guid))
      return false;
    if (qureyInput == null) {
      if (other.qureyInput != null)
        return false;
    } else if (!qureyInput.equals(other.qureyInput))
      return false;
    if (requestUrl == null) {
      if (other.requestUrl != null)
        return false;
    } else if (!requestUrl.equals(other.requestUrl))
      return false;
    if (sourceIp == null) {
      if (other.sourceIp != null)
        return false;
    } else if (!sourceIp.equals(other.sourceIp))
      return false;
    if (successFlag == null) {
      if (other.successFlag != null)
        return false;
    } else if (!successFlag.equals(other.successFlag))
      return false;
    if (systemId == null) {
      if (other.systemId != null)
        return false;
    } else if (!systemId.equals(other.systemId))
      return false;
    if (targetIp == null) {
      if (other.targetIp != null)
        return false;
    } else if (!targetIp.equals(other.targetIp))
      return false;
    if (teamId == null) {
      if (other.teamId != null)
        return false;
    } else if (!teamId.equals(other.teamId))
      return false;
    if (userId == null) {
      if (other.userId != null)
        return false;
    } else if (!userId.equals(other.userId))
      return false;
    if (userName == null) {
      if (other.userName != null)
        return false;
    } else if (!userName.equals(other.userName))
      return false;
    if (functionCount == null) {
      if (other.functionCount != null)
        return false;
    } else if (!functionCount.equals(other.functionCount))
      return false;
    if (writeDate == null) {
      if (other.writeDate != null)
        return false;
    } else if (!writeDate.equals(other.writeDate))
      return false;
    if (writeTime == null) {
      if (other.writeTime != null)
        return false;
    } else if (!writeTime.equals(other.writeTime))
      return false;
    if (timestamp == null) { // 更新 equals 檢查
      if (other.timestamp != null)
        return false;
    } else if (!timestamp.equals(other.timestamp))
      return false;
    return true;
  }
}