package nccc.btp.aplog.model;

public class LogFunctionCount {
	private String accessDate;
	private String accessTime;
	private String systemId;
	private String functionId;
	private String functionName;

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
	
	@Override
	public String toString() {
		return "ApLogDetail [accessDate=" + accessDate + ", accessTime=" + accessTime + ", systemId=" + systemId
				+ ", functionId=" + functionId + ", functionName=" + functionName + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessDate == null) ? 0 : accessDate.hashCode());
		result = prime * result + ((accessTime == null) ? 0 : accessTime.hashCode());
		result = prime * result + ((systemId == null) ? 0 : systemId.hashCode());
		result = prime * result + ((functionId == null) ? 0 : functionId.hashCode());
		result = prime * result + ((functionName == null) ? 0 : functionName.hashCode());
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
		
		LogFunctionCount other = (LogFunctionCount) obj;
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
		if (systemId == null) {
			if (other.systemId != null)
				return false;
		} else if (!systemId.equals(other.systemId))
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
		
		return true;
	}
}
