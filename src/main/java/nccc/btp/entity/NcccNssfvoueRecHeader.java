package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "NCCC_NSSFVOUE_REC_HEADER")
@NamedQuery(name = "NcccNssfvoueRecHeader.findAll", query = "SELECT n FROM NcccNssfvoueRecHeader n")
public class NcccNssfvoueRecHeader implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "NSSFVOUE_HEADER_BATCH", length = 11)
  private String nssfvoueHeaderBatch;

  @Column(name = "NSSFVOUE_REC_HEADER", length = 2)
  private String nssfvoueRecHeader;

  @Column(name = "NSSFVOUE_HEADER_SEQ", length = 4)
  private String nssfvoueHeaderSeq;

  @Column(name = "NSSFVOUE_DATE", length = 7)
  private String nssfvoueDate;

  @Column(name = "NSSFVOUE_C_SIGN", length = 1)
  private String nssfvoueCSign;

  @Column(name = "NSSFVOUE_C_AMT", length = 14)
  private BigDecimal nssfvoueCAmt;

  @Column(name = "NSSFVOUE_D_SIGN", length = 1)
  private String nssfvoueDSign;

  @Column(name = "NSSFVOUE_D_AMT", length = 14)
  private BigDecimal nssfvoueDAmt;

  @Column(name = "NSSFVOUE_US_SIGN", length = 1)
  private String nssfvoueUSSign;

  @Column(name = "NSSFVOUE_US_AMT", length = 14)
  private BigDecimal nssfvoueUSAmt;

  @Column(name = "NSSFVOUE_PROC_DATE", length = 7)
  private String nssfvoueProcDate;

  @Column(name = "FILLER", length = 140)
  private String filler;

  @Column(name = "CREATE_USER", length = 50)
  private String createUser;

  @Column(name = "CREATE_DATE")
  private LocalDate createDate;

  @Column(name = "UPDATE_USER", length = 50)
  private String updateUser;

  @Column(name = "UPDATE_DATE")
  private LocalDate updateDate;

  @Column(name = "ASSIGNMENT", length = 255)
  private String assignment;

  @Column(name = "ASSIGN_USER", length = 50)
  private String assignUser;

  @Column(name = "ASSIGN_DATE")
  private LocalDate assignDate;

  @Column(name = "REVIEW_USER", length = 50)
  private String reviewUser;

  @Column(name = "REVIEW_DATE")
  private LocalDate reviewDate;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "NSSFVOUE_HEADER_BATCH", referencedColumnName = "NSSFVOUE_HEADER_BATCH",
      insertable = false, updatable = false)
  private SapNssfvoueRecStatus status;

  public NcccNssfvoueRecHeader() {}


  public String getNssfvoueHeaderBatch() {
    return nssfvoueHeaderBatch;
  }


  public void setNssfvoueHeaderBatch(String nssfvoueHeaderBatch) {
    this.nssfvoueHeaderBatch = nssfvoueHeaderBatch;
  }

  public String getNssfvoueRecHeader() {
    return nssfvoueRecHeader;
  }

  public void setNssfvoueRecHeader(String nssfvoueRecHeader) {
    this.nssfvoueRecHeader = nssfvoueRecHeader;
  }

  public String getNssfvoueHeaderSeq() {
    return nssfvoueHeaderSeq;
  }

  public void setNssfvoueHeaderSeq(String nssfvoueHeaderSeq) {
    this.nssfvoueHeaderSeq = nssfvoueHeaderSeq;
  }

  public String getNssfvoueDate() {
    return nssfvoueDate;
  }

  public void setNssfvoueDate(String nssfvoueDate) {
    this.nssfvoueDate = nssfvoueDate;
  }

  public String getNssfvoueCSign() {
    return nssfvoueCSign;
  }

  public void setNssfvoueCSign(String nssfvoueCSign) {
    this.nssfvoueCSign = nssfvoueCSign;
  }

  public BigDecimal getNssfvoueCAmt() {
    return nssfvoueCAmt;
  }

  public void setNssfvoueCAmt(BigDecimal nssfvoueCAmt) {
    this.nssfvoueCAmt = nssfvoueCAmt;
  }

  public String getNssfvoueDSign() {
    return nssfvoueDSign;
  }

  public void setNssfvoueDSign(String nssfvoueDSign) {
    this.nssfvoueDSign = nssfvoueDSign;
  }

  public BigDecimal getNssfvoueDAmt() {
    return nssfvoueDAmt;
  }

  public void setNssfvoueDAmt(BigDecimal nssfvoueDAmt) {
    this.nssfvoueDAmt = nssfvoueDAmt;
  }

  public String getNssfvoueUSSign() {
    return nssfvoueUSSign;
  }

  public void setNssfvoueUSSign(String nssfvoueUSSign) {
    this.nssfvoueUSSign = nssfvoueUSSign;
  }

  public BigDecimal getNssfvoueUSAmt() {
    return nssfvoueUSAmt;
  }

  public void setNssfvoueUSAmt(BigDecimal nssfvoueUSAmt) {
    this.nssfvoueUSAmt = nssfvoueUSAmt;
  }

  public String getNssfvoueProcDate() {
    return nssfvoueProcDate;
  }


  public void setNssfvoueProcDate(String nssfvoueProcDate) {
    this.nssfvoueProcDate = nssfvoueProcDate;
  }


  public String getFiller() {
    return filler;
  }

  public void setFiller(String filler) {
    this.filler = filler;
  }

  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }

  public LocalDate getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDate createDate) {
    this.createDate = createDate;
  }

  public String getUpdateUser() {
    return updateUser;
  }

  public void setUpdateUser(String updateUser) {
    this.updateUser = updateUser;
  }


  public LocalDate getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(LocalDate updateDate) {
    this.updateDate = updateDate;
  }


  public String getAssignment() {
    return assignment;
  }


  public void setAssignment(String assignment) {
    this.assignment = assignment;
  }


  public String getAssignUser() {
    return assignUser;
  }


  public void setAssignUser(String assignUser) {
    this.assignUser = assignUser;
  }


  public LocalDate getAssignDate() {
    return assignDate;
  }


  public void setAssignDate(LocalDate assignDate) {
    this.assignDate = assignDate;
  }


  public String getReviewUser() {
    return reviewUser;
  }


  public void setReviewUser(String reviewUser) {
    this.reviewUser = reviewUser;
  }


  public LocalDate getReviewDate() {
    return reviewDate;
  }


  public void setReviewDate(LocalDate reviewDate) {
    this.reviewDate = reviewDate;
  }

  public SapNssfvoueRecStatus getStatus() {
    return status;
  }

  public void setStatus(SapNssfvoueRecStatus status) {
    this.status = status;
  }
}
