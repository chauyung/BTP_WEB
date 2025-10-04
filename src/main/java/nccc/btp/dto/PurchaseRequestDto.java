package nccc.btp.dto;

import java.util.List;
import nccc.btp.vo.FileVo;
import nccc.btp.vo.ProcessVo;


public class PurchaseRequestDto extends ProcessVo {
  
  private String prNo;
  private String applyDate;
  private String empNo;
  private String applicant;
  private String title;
  private String department;
  private String poNo;
  private String revNo;
  private String radio1;
  private String radio2;
  private String remark;
  private String accountComment;
  private String docNo;
  private String demandOrder;
  private String decisionDate;
  private String totalAmount;
  private String procurementHandler;
  private String inquiryAmount;
  private String itCommentRemark;
  private List<FileVo> attachment;
  private List<FileVo> quotationFiles;
  private List<PurchaseDetailDto> details;

  public String getPrNo() {
    return prNo;
  }

  public void setPrNo(String prNo) {
    this.prNo = prNo;
  }

  public String getApplyDate() {
    return applyDate;
  }

  public void setApplyDate(String applyDate) {
    this.applyDate = applyDate;
  }

  public String getEmpNo() {
    return empNo;
  }

  public void setEmpNo(String empNo) {
    this.empNo = empNo;
  }

  public String getApplicant() {
    return applicant;
  }

  public void setApplicant(String applicant) {
    this.applicant = applicant;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getPoNo() {
    return poNo;
  }

  public void setPoNo(String poNo) {
    this.poNo = poNo;
  }

  public String getRevNo() {
    return revNo;
  }

  public void setRevNo(String revNo) {
    this.revNo = revNo;
  }

  public String getRadio1() {
    return radio1;
  }

  public void setRadio1(String radio1) {
    this.radio1 = radio1;
  }

  public String getRadio2() {
    return radio2;
  }

  public void setRadio2(String radio2) {
    this.radio2 = radio2;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getDocNo() {
    return docNo;
  }

  public void setDocNo(String docNo) {
    this.docNo = docNo;
  }

  public String getDemandOrder() {
    return demandOrder;
  }

  public void setDemandOrder(String demandOrder) {
    this.demandOrder = demandOrder;
  }

  public String getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(String totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getInquiryAmount() {
    return inquiryAmount;
  }

  public void setInquiryAmount(String inquiryAmount) {
    this.inquiryAmount = inquiryAmount;
  }

  public String getItCommentRemark() {
    return itCommentRemark;
  }

  public void setItCommentRemark(String itCommentRemark) {
    this.itCommentRemark = itCommentRemark;
  }

  public List<PurchaseDetailDto> getDetails() {
    return details;
  }

  public void setDetails(List<PurchaseDetailDto> details) {
    this.details = details;
  }

  public String getAccountComment() {
    return accountComment;
  }

  public void setAccountComment(String accountComment) {
    this.accountComment = accountComment;
  }

  public String getDecisionDate() {
    return decisionDate;
  }

  public void setDecisionDate(String decisionDate) {
    this.decisionDate = decisionDate;
  }

  public String getProcurementHandler() {
    return procurementHandler;
  }

  public void setProcurementHandler(String procurementHandler) {
    this.procurementHandler = procurementHandler;
  }

  public List<FileVo> getAttachment() {
    return attachment;
  }

  public void setAttachment(List<FileVo> attachment) {
    this.attachment = attachment;
  }

  public List<FileVo> getQuotationFiles() {
    return quotationFiles;
  }

  public void setQuotationFiles(List<FileVo> quotationFiles) {
    this.quotationFiles = quotationFiles;
  }

}
