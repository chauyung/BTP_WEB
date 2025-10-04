package nccc.btp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "BPM_EX_M")
public class BpmExM {

  @Column(name = "TASKID", length = 40)
  private String taskId;

  @Id
  @Column(name = "EX_NO", length = 10)
  private String exNo;

  @Column(name = "APPLY_DATE", length = 10)
  private String applyDate;

  @Column(name = "APPLY_TYPE", length = 1)
  private String applyType;

  @Column(name = "APPLICANT", length = 20)
  private String applicant;

  @Column(name = "EMP_NO", length = 20)
  private String empNo;

  @Column(name = "DEPARTMENT", length = 20)
  private String department;

  @Column(name = "PAY_EMP_NO", length = 20)
  private String payEmpNo;

  @Column(name = "PAY_NAME", length = 20)
  private String payName;

  @Column(name = "PAY_WAY", length = 20)
  private String payWay;

  @Column(name = "SPECIAL_PAY_DATE", length = 10)
  private String specialPayDate;

  @Column(name = "APPLY_REASON", length = 25)
  private String applyReason;

  @Column(name = "TRADE_CURRENCY", length = 5)
  private String tradeCurrency;

  @Column(name = "TOTAL", precision = 13, scale = 2)
  private BigDecimal total;

  @Column(name = "CURRENCY_RATE", precision = 13, scale = 2)
  private BigDecimal currencyRate;

  @Column(name = "PREDICT_TWD", precision = 13, scale = 2)
  private BigDecimal predictTwd;

  @Column(name = "PREPAY_NO", length = 256)
  private String prepayNo;

  @Column(name = "PAY_AMOUNT", precision = 13, scale = 2)
  private BigDecimal payAmount;

  @Column(name = "REFUND_AMOUNT", precision = 13, scale = 2)
  private BigDecimal refundAmount;

  @Column(name = "REFUND_DATE", length = 10)
  private String refundDate;

  @Column(name = "BANK_ACCOUNT", length = 10)
  private String bankAccount;

  @Column(name = "APPROVAL_METHOD", length = 20)
  private String approvalMethod;

  @Column(name = "APPROVAL_NO", length = 10)
  private String approvalNo;

  @Column(name = "PAY_WAY_METHOD", length = 20)
  private String payWayMethod;

  @Column(name = "WITHHOLD_TOTAL", length = 14)
  private String withholdTotal;

  @Column(name = "POSTING_DATE", length = 10)
  private String postingDate;

  @Lob
  @Column(name = "ATTACHMENT")
  private byte[] attachment;

  @Column(name = "CREATED_DATE")
  private LocalDate createdDate;

  @Column(name = "MODIFIED_DATE")
  private LocalDate modifiedDate;

  @Column(name = "CREATED_USER", length = 50)
  private String createdUser;

  @Column(name = "MODIFIED_USER", length = 50)
  private String modifiedUser;

  /** 流程狀態 */
  @Column(name = "FLOW_STATUS", length = 2)
  private String flowStatus;

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public String getExNo() {
    return exNo;
  }

  public void setExNo(String exNo) {
    this.exNo = exNo;
  }

  public String getApplyDate() {
    return applyDate;
  }

  public void setApplyDate(String applyDate) {
    this.applyDate = applyDate;
  }

  public String getApplyType() {
    return applyType;
  }

  public void setApplyType(String applyType) {
    this.applyType = applyType;
  }

  public String getApplicant() {
    return applicant;
  }

  public void setApplicant(String applicant) {
    this.applicant = applicant;
  }

  public String getEmpNo() {
    return empNo;
  }

  public void setEmpNo(String empNo) {
    this.empNo = empNo;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getPayEmpNo() {
    return payEmpNo;
  }

  public void setPayEmpNo(String payEmpNo) {
    this.payEmpNo = payEmpNo;
  }

  public String getPayName() {
    return payName;
  }

  public void setPayName(String payName) {
    this.payName = payName;
  }

  public String getPayWay() {
    return payWay;
  }

  public void setPayWay(String payWay) {
    this.payWay = payWay;
  }

  public String getSpecialPayDate() {
    return specialPayDate;
  }

  public void setSpecialPayDate(String specialPayDate) {
    this.specialPayDate = specialPayDate;
  }

  public String getApplyReason() {
    return applyReason;
  }

  public void setApplyReason(String applyReason) {
    this.applyReason = applyReason;
  }

  public String getTradeCurrency() {
    return tradeCurrency;
  }

  public void setTradeCurrency(String tradeCurrency) {
    this.tradeCurrency = tradeCurrency;
  }

  public BigDecimal getCurrencyRate() {
    return currencyRate;
  }

  public void setCurrencyRate(BigDecimal currencyRate) {
    this.currencyRate = currencyRate;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public BigDecimal getPredictTwd() {
    return predictTwd;
  }

  public void setPredictTwd(BigDecimal predictTwd) {
    this.predictTwd = predictTwd;
  }

  public String getPrepayNo() {
    return prepayNo;
  }

  public void setPrepayNo(String prepayNo) {
    this.prepayNo = prepayNo;
  }

  public String getWithholdTotal() {
    return withholdTotal;
  }

  public void setWithholdTotal(String withholdTotal) {
    this.withholdTotal = withholdTotal;
  }

  public String getPostingDate() {
    return postingDate;
  }

  public void setPostingDate(String postingDate) {
    this.postingDate = postingDate;
  }

  public byte[] getAttachment() {
    return attachment;
  }

  public void setAttachment(byte[] attachment) {
    this.attachment = attachment;
  }

  public LocalDate getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDate createdDate) {
    this.createdDate = createdDate;
  }

  public LocalDate getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(LocalDate modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  public String getCreatedUser() {
    return createdUser;
  }

  public void setCreatedUser(String createdUser) {
    this.createdUser = createdUser;
  }

  public String getModifiedUser() {
    return modifiedUser;
  }

  public void setModifiedUser(String modifiedUser) {
    this.modifiedUser = modifiedUser;
  }

  public String getApprovalMethod() {
    return approvalMethod;
  }

  public void setApprovalMethod(String approvalMethod) {
    this.approvalMethod = approvalMethod;
  }

  public String getApprovalNo() {
    return approvalNo;
  }

  public void setApprovalNo(String approvalNo) {
    this.approvalNo = approvalNo;
  }

  public String getPayWayMethod() {
    return payWayMethod;
  }

  public void setPayWayMethod(String payWayMethod) {
    this.payWayMethod = payWayMethod;
  }

  public BigDecimal getPayAmount() {
    return payAmount;
  }

  public void setPayAmount(BigDecimal payAmount) {
    this.payAmount = payAmount;
  }

  public BigDecimal getRefundAmount() {
    return refundAmount;
  }

  public void setRefundAmount(BigDecimal refundAmount) {
    this.refundAmount = refundAmount;
  }

  public String getRefundDate() {
    return refundDate;
  }

  public void setRefundDate(String refundDate) {
    this.refundDate = refundDate;
  }

  public String getBankAccount() {
    return bankAccount;
  }

  public void setBankAccount(String bankAccount) {
    this.bankAccount = bankAccount;
  }

  public String getFlowStatus() {
    return flowStatus;
  }

  public void setFlowStatus(String flowStatus) {
    this.flowStatus = flowStatus;
  }
}