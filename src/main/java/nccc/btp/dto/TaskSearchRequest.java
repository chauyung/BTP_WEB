package nccc.btp.dto;

import java.io.Serializable;

/**
 * 任務查詢請求 DTO（與前端查詢表單對齊）
 *
 * 對應前端欄位： - processName 流程名稱 - applicant 申請人 HRID（後端以流程變數 initiator 比對） - submissionDate 申請日期（字串
 * yyyy-MM-dd） - taskStatus 任務狀態（全部/處理中/已核可/已拒絕/已結束） - businessKey 單號 - keywords 關鍵字（比對
 * applicant、processName）
 */
public class TaskSearchRequest implements Serializable {
  private static final long serialVersionUID = 1L;

  /** 流程名稱 */
  private String processName;

  /** 申請人 HRID（對應流程變數 initiator） */
  private String applicant;

  /** 申請日期（字串 yyyy-MM-dd） */
  private String submissionDate;

  /** 任務狀態：全部 / 處理中 / 已核可 / 已拒絕 / 已結束 */
  private String taskStatus;

  /** 單號 */
  private String businessKey;

  /** 關鍵字（比對 applicant / processName） */
  private String keywords;

  public TaskSearchRequest() {}

  public String getProcessName() {
    return processName;
  }

  public void setProcessName(String processName) {
    this.processName = processName;
  }

  public String getApplicant() {
    return applicant;
  }

  public void setApplicant(String applicant) {
    this.applicant = applicant;
  }

  public String getSubmissionDate() {
    return submissionDate;
  }

  public void setSubmissionDate(String submissionDate) {
    this.submissionDate = submissionDate;
  }

  public String getTaskStatus() {
    return taskStatus;
  }

  public void setTaskStatus(String taskStatus) {
    this.taskStatus = taskStatus;
  }

  public String getBusinessKey() {
    return businessKey;
  }

  public void setBusinessKey(String businessKey) {
    this.businessKey = businessKey;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  @Override
  public String toString() {
    return "TaskSearchRequest{" + "processName='" + processName + '\'' + ", applicant='" + applicant
        + '\'' + ", submissionDate='" + submissionDate + '\'' + ", taskStatus='" + taskStatus + '\''
        + ", businessKey='" + businessKey + '\'' + ", keywords='" + keywords + '\'' + '}';
  }
}