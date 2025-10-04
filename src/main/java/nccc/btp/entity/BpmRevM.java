package nccc.btp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 驗收單主檔
 */
@Entity
@Table(name = "BPM_REV_M")
public class BpmRevM {

  /** 驗收單單號（PK） */
  @Id
  @Column(name = "REV_NO", length = 12)
  private String revNo;

  /** 申請人 */
  @Column(name = "APPLICANT", length = 20)
  private String applicant;

  /** 申請日期 */
  @Column(name = "APPLY_DATE", length = 10)
  private String applyDate;

  /** 建立時間 */
  @Column(name = "CREATED_TIME")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdTime;

  /** 建立者 */
  @Column(name = "CREATED_USER", length = 50)
  private String createdUser;

  /** 需求單編號 */
  @Column(name = "DEMAND_ORDER", length = 10)
  private String demandOrder;

  /** 部門 */
  @Column(name = "DEPARTMENT", length = 20)
  private String department;

  /** 請購公文文號 */
  @Column(name = "DOC_NO", length = 10)
  private String docNo;

  /** 議價核准公文文號 */
  @Column(name = "BARG_DOC_NO", length = 10)
  private String bargDocNo;

  /** 員工編號 */
  @Column(name = "EMP_NO", length = 20)
  private String empNo;

  /** 詢價金額 */
  @Column(name = "INQUIRY_AMOUNT", precision = 13, scale = 2)
  private BigDecimal inquiryAmount;

  /** 1:物聯網設備;2:購置電腦設備 */
  @Column(name = "IT_TYPE", length = 1)
  private String itType;

  /** 採購單單號 */
  @Column(name = "PO_NO", length = 12)
  private String poNo;

  /** 請購單單號 */
  @Column(name = "PR_NO", length = 12)
  private String prNo;

  /** 請購目的 */
  @Column(name = "REMARK", length = 100)
  private String remark;

  /** 任務編碼 */
  @Column(name = "TASKID", length = 10)
  private String taskId;

  /** 請購金額 */
  @Column(name = "TOTAL_AMOUNT", precision = 13, scale = 2)
  private BigDecimal totalAmount;

  /** 指定過帳日 */
  @Column(name = "POSTING_DATE", length = 10)
  private String postingDate;

  /** 修改者 */
  @Column(name = "MODIFIED_USER", length = 50)
  private String modifiedUser;

  /** 修改日期 */
  @Column(name = "MODIFIED_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate modifiedDate;

  /** 1.進行中2.已結案.3.已作廢 */
  @Column(name = "FLOW_STATUS", length = 2)
  private String flowStatus;

  /** 交貨地點 */
  // @Column(name = "DELIVERY_LOCATION", length = 20)
  // private String deliveryLocation;

  public String getRevNo() {
    return revNo;
  }

  public void setRevNo(String revNo) {
    this.revNo = revNo;
  }

  public String getApplicant() {
    return applicant;
  }

  public void setApplicant(String applicant) {
    this.applicant = applicant;
  }

  public String getApplyDate() {
    return applyDate;
  }

  public void setApplyDate(String applyDate) {
    this.applyDate = applyDate;
  }

  public LocalDateTime getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(LocalDateTime createdTime) {
    this.createdTime = createdTime;
  }

  public String getCreatedUser() {
    return createdUser;
  }

  public void setCreatedUser(String createdUser) {
    this.createdUser = createdUser;
  }

  public String getDemandOrder() {
    return demandOrder;
  }

  public void setDemandOrder(String demandOrder) {
    this.demandOrder = demandOrder;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getDocNo() {
    return docNo;
  }

  public void setDocNo(String docNo) {
    this.docNo = docNo;
  }

  public String getBargDocNo() {
    return bargDocNo;
  }

  public void setBargDocNo(String bargDocNo) {
    this.bargDocNo = bargDocNo;
  }

  public String getEmpNo() {
    return empNo;
  }

  public void setEmpNo(String empNo) {
    this.empNo = empNo;
  }

  public BigDecimal getInquiryAmount() {
    return inquiryAmount;
  }

  public void setInquiryAmount(BigDecimal inquiryAmount) {
    this.inquiryAmount = inquiryAmount;
  }

  public String getItType() {
    return itType;
  }

  public void setItType(String itType) {
    this.itType = itType;
  }

  public String getPoNo() {
    return poNo;
  }

  public void setPoNo(String poNo) {
    this.poNo = poNo;
  }

  public String getPrNo() {
    return prNo;
  }

  public void setPrNo(String prNo) {
    this.prNo = prNo;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getPostingDate() {
    return postingDate;
  }

  public void setPostingDate(String postingDate) {
    this.postingDate = postingDate;
  }

  public String getModifiedUser() {
    return modifiedUser;
  }

  public void setModifiedUser(String modifiedUser) {
    this.modifiedUser = modifiedUser;
  }

  public LocalDate getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(LocalDate modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  public String getFlowStatus() {
    return flowStatus;
  }

  public void setFlowStatus(String flowStatus) {
    this.flowStatus = flowStatus;
  }

  // public String getDeliveryLocation() {
  // return deliveryLocation;
  // }
  //
  // public void setDeliveryLocation(String deliveryLocation) {
  // this.deliveryLocation = deliveryLocation;
  // }

}
