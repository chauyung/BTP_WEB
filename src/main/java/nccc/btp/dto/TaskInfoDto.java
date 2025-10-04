package nccc.btp.dto;

import java.io.Serializable;

/**
 * 前端表格列所需 DTO 對應欄位： - businessKey 單號 - processName 流程名稱 - applicant 申請人（通常為 initiator） -
 * submissionDate 申請日期（字串 yyyy-MM-dd） - status 狀態（處理中 / 已核可 / 已拒絕 / 已結束）
 */
public class TaskInfoDto implements Serializable {
  private static final long serialVersionUID = 1L;

  /** 單號 */
  private String businessKey;

  /** 流程名稱 */
  private String processName;

  /** 申請人（流程變數 initiator） */
  private String applicant;

  /** 申請日期（字串格式 yyyy-MM-dd；若流程變數沒有則用流程啟動日） */
  private String submissionDate;

  /** 狀態：處理中 / 已核可 / 已拒絕 / 已結束 */
  private String status;

  public TaskInfoDto() {}

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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}