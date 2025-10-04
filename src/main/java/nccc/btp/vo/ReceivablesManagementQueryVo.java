package nccc.btp.vo;

import java.time.LocalDate;

public class ReceivablesManagementQueryVo {

  private String nssfvoueHeaderBatch;

  private String nssfvoueDateStart;

  private String nssfvoueDateEnd;

  private LocalDate createDateStart;

  private LocalDate createDateEnd;
  
  private LocalDate assignDateStart;

  private LocalDate assignDateEnd;
  
  private LocalDate reviewDateStart;

  private LocalDate reviewDateEnd;
  
  private String assignUser;
  
  private String reviewUser;
  
  private String assignment;
  
  private String fileName;

  public String getNssfvoueHeaderBatch() {
    return nssfvoueHeaderBatch;
  }

  public void setNssfvoueHeaderBatch(String nssfvoueHeaderBatch) {
    this.nssfvoueHeaderBatch = nssfvoueHeaderBatch;
  }

  public String getNssfvoueDateStart() {
    return nssfvoueDateStart;
  }

  public void setNssfvoueDateStart(String nssfvoueDateStart) {
    this.nssfvoueDateStart = nssfvoueDateStart;
  }

  public String getNssfvoueDateEnd() {
    return nssfvoueDateEnd;
  }

  public void setNssfvoueDateEnd(String nssfvoueDateEnd) {
    this.nssfvoueDateEnd = nssfvoueDateEnd;
  }

  public LocalDate getCreateDateStart() {
    return createDateStart;
  }

  public void setCreateDateStart(LocalDate createDateStart) {
    this.createDateStart = createDateStart;
  }

  public LocalDate getCreateDateEnd() {
    return createDateEnd;
  }

  public void setCreateDateEnd(LocalDate createDateEnd) {
    this.createDateEnd = createDateEnd;
  }

  public LocalDate getAssignDateStart() {
    return assignDateStart;
  }

  public void setAssignDateStart(LocalDate assignDateStart) {
    this.assignDateStart = assignDateStart;
  }

  public LocalDate getAssignDateEnd() {
    return assignDateEnd;
  }

  public void setAssignDateEnd(LocalDate assignDateEnd) {
    this.assignDateEnd = assignDateEnd;
  }

  public LocalDate getReviewDateStart() {
    return reviewDateStart;
  }

  public void setReviewDateStart(LocalDate reviewDateStart) {
    this.reviewDateStart = reviewDateStart;
  }

  public LocalDate getReviewDateEnd() {
    return reviewDateEnd;
  }

  public void setReviewDateEnd(LocalDate reviewDateEnd) {
    this.reviewDateEnd = reviewDateEnd;
  }

  public String getAssignUser() {
    return assignUser;
  }

  public void setAssignUser(String assignUser) {
    this.assignUser = assignUser;
  }

  public String getReviewUser() {
    return reviewUser;
  }

  public void setReviewUser(String reviewUser) {
    this.reviewUser = reviewUser;
  }

  public String getAssignment() {
    return assignment;
  }

  public void setAssignment(String assignment) {
    this.assignment = assignment;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }


}
