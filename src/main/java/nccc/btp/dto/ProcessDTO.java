package nccc.btp.dto;

import java.util.Date;

public class ProcessDTO {
  
  private String businessKey; // 單號
  private String processName; // 流程名稱
  private String summary; // 摘要資訊
  private String applicant; // 申請人
  private Date applyDate; // 申請日期
  private String status; // 狀態 (進行中 / 已結束)

  public ProcessDTO() {}

  public ProcessDTO(String businessKey, String processName, String summary, String applicant,
      Date applyDate, String status) {
    this.businessKey = businessKey;
    this.processName = processName;
    this.summary = summary;
    this.applicant = applicant;
    this.applyDate = applyDate;
    this.status = status;
  }

  // Getter & Setter
  public String getBusinessKey() {
    return businessKey;
  }

  public void setBusinessKey(String businessKey) {
    this.businessKey = businessKey;
  }

  public String getProcessName() {
    return processName;
  }

  public void setProcessName(String processName) {
    this.processName = processName;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getApplicant() {
    return applicant;
  }

  public void setApplicant(String applicant) {
    this.applicant = applicant;
  }

  public Date getApplyDate() {
    return applyDate;
  }

  public void setApplyDate(Date applyDate) {
    this.applyDate = applyDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
