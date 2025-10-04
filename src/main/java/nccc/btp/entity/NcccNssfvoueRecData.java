package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "NCCC_NSSFVOUE_REC_DATA")
@NamedQuery(name = "NcccNssfvoueRecData.findAll", query = "SELECT n FROM NcccNssfvoueRecData n")
public class NcccNssfvoueRecData implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private NcccNssfvoueRecDataId id;

  @Column(name = "NSSFVOUE_REC_HEADER")
  private String nssfvoueRecHeader;

  @Column(name = "NSSFVOUE_DATA_SEQ")
  private String nssfvoueDataSeq;

  @Column(name = "NSSFVOUE_TYPE")
  private String nssfvoueType;

  @Column(name = "NSSFVOUE_ID")
  private String nssfvoueId;

  @Column(name = "NSSFVOUE_S_FID")
  private String nssfvoueSFid;

  @Column(name = "NSSFVOUE_FID")
  private String nssfvoueFid;

  @Column(name = "FILLER")
  private String filler;

  @Column(name = "NSSFVOUE_SIGN")
  private String nssfvoueSign;

  @Column(name = "NSSFVOUE_AMT")
  private BigDecimal nssfvoueAmt;
  
  @Column(name = "NSSFVOUE_US_SIGN")
  private String nssfvoueUSSign;

  @Column(name = "NSSFVOUE_US_AMT")
  private BigDecimal nssfvoueUSAmt;

  @Column(name = "NSSFVOUE_SUB_SUBJ_NAME_1")
  private String nssfvoueSubSubjName1;

  @Column(name = "NSSFVOUE_SUB_SUBJ_NAME_2")
  private String nssfvoueSubSubjName2;

  @Column(name = "NSSFVOUE_NOTE")
  private String nssfvoueNote;
  
  @Column(name = "SOURCE_FILE")
  private String sourceFile;

  public NcccNssfvoueRecData() {}

  public NcccNssfvoueRecDataId getId() {
    return id;
  }

  public void setId(NcccNssfvoueRecDataId id) {
    this.id = id;
  }

  public String getNssfvoueRecHeader() {
    return nssfvoueRecHeader;
  }

  public void setNssfvoueRecHeader(String nssfvoueRecHeader) {
    this.nssfvoueRecHeader = nssfvoueRecHeader;
  }

  public String getNssfvoueDataSeq() {
    return nssfvoueDataSeq;
  }

  public void setNssfvoueDataSeq(String nssfvoueDataSeq) {
    this.nssfvoueDataSeq = nssfvoueDataSeq;
  }

  public String getNssfvoueType() {
    return nssfvoueType;
  }

  public void setNssfvoueType(String nssfvoueType) {
    this.nssfvoueType = nssfvoueType;
  }

  public String getNssfvoueId() {
    return nssfvoueId;
  }

  public void setNssfvoueId(String nssfvoueId) {
    this.nssfvoueId = nssfvoueId;
  }

  public String getNssfvoueSFid() {
    return nssfvoueSFid;
  }

  public void setNssfvoueSFid(String nssfvoueSFid) {
    this.nssfvoueSFid = nssfvoueSFid;
  }

  public String getNssfvoueFid() {
    return nssfvoueFid;
  }

  public void setNssfvoueFid(String nssfvoueFid) {
    this.nssfvoueFid = nssfvoueFid;
  }

  public String getNssfvoueSign() {
    return nssfvoueSign;
  }

  public void setNssfvoueSign(String nssfvoueSign) {
    this.nssfvoueSign = nssfvoueSign;
  }

  public BigDecimal getNssfvoueAmt() {
    return nssfvoueAmt;
  }

  public void setNssfvoueAmt(BigDecimal nssfvoueAmt) {
    this.nssfvoueAmt = nssfvoueAmt;
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

  public String getNssfvoueSubSubjName1() {
    return nssfvoueSubSubjName1;
  }

  public void setNssfvoueSubSubjName1(String nssfvoueSubSubjName1) {
    this.nssfvoueSubSubjName1 = nssfvoueSubSubjName1;
  }

  public String getNssfvoueSubSubjName2() {
    return nssfvoueSubSubjName2;
  }

  public void setNssfvoueSubSubjName2(String nssfvoueSubSubjName2) {
    this.nssfvoueSubSubjName2 = nssfvoueSubSubjName2;
  }

  public String getNssfvoueNote() {
    return nssfvoueNote;
  }

  public void setNssfvoueNote(String nssfvoueNote) {
    this.nssfvoueNote = nssfvoueNote;
  }

  public String getFiller() {
    return filler;
  }

  public void setFiller(String filler) {
    this.filler = filler;
  }

  public String getSourceFile() {
    return sourceFile;
  }

  public void setSourceFile(String sourceFile) {
    this.sourceFile = sourceFile;
  }

}
