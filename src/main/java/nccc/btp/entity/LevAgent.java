package nccc.btp.entity;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "LEV_AGENT")
public class LevAgent {

  @Column(name = "CMP_CODE", length = 20)
  private String cmpCode;

  @Column(name = "EMP_ID", length = 10)
  private String empId;

  @Id
  @Column(name = "LE_APPLY_NO", length = 18)
  private String leApplyNo;

  @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm") 
  @Column(name = "SDATE")
  private LocalDateTime sdate;

  @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm") 
  @Column(name = "EDATE")
  private LocalDateTime edate;

  @Column(name = "LEV_TYPE_COD", length = 5)
  private String levTypeCod;

  @Column(name = "LEV_CNAME", length = 60)
  private String levCname;

  @Column(name = "AGT_EMP_ID", length = 10)
  private String agtEmpId;

  @Column(name = "LEV_AGT_DEP_CODE", length = 20)
  private String levAgtDepCode;

  @Column(name = "LEV_AGT_DEP_CNAME", length = 40)
  private String levAgtDepCname;

  @Column(name = "EFORM_STATUS_COD", length = 100)
  private String eformStatusCod;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
  @Column(name = "CRE_DTE")
  @Temporal(TemporalType.DATE)
  private Date creDte;

  @Column(name = "IDENTITY_FIELD", precision = 38, scale = 0)
  private Long identityField;

  @Column(name = "REASON", length = 300)
  private String reason;

  @Column(name = "BT_STATUS", length = 1)
  private String btStatus;

  // 1.在途，2.已使用
  @Column(name = "BT_NO", length = 12)
  private String btNo;

  public String getCmpCode() {
    return cmpCode;
  }

  public void setCmpCode(String cmpCode) {
    this.cmpCode = cmpCode;
  }

  public String getEmpId() {
    return empId;
  }

  public void setEmpId(String empId) {
    this.empId = empId;
  }

  public String getLeApplyNo() {
    return leApplyNo;
  }

  public void setLeApplyNo(String leApplyNo) {
    this.leApplyNo = leApplyNo;
  }

  public LocalDateTime getSdate() {
    return sdate;
  }

  public void setSdate(LocalDateTime sdate) {
    this.sdate = sdate;
  }

  public LocalDateTime getEdate() {
    return edate;
  }

  public void setEdate(LocalDateTime edate) {
    this.edate = edate;
  }

  public String getLevTypeCod() {
    return levTypeCod;
  }

  public void setLevTypeCod(String levTypeCod) {
    this.levTypeCod = levTypeCod;
  }

  public String getLevCname() {
    return levCname;
  }

  public void setLevCname(String levCname) {
    this.levCname = levCname;
  }

  public String getAgtEmpId() {
    return agtEmpId;
  }

  public void setAgtEmpId(String agtEmpId) {
    this.agtEmpId = agtEmpId;
  }

  public String getLevAgtDepCode() {
    return levAgtDepCode;
  }

  public void setLevAgtDepCode(String levAgtDepCode) {
    this.levAgtDepCode = levAgtDepCode;
  }

  public String getLevAgtDepCname() {
    return levAgtDepCname;
  }

  public void setLevAgtDepCname(String levAgtDepCname) {
    this.levAgtDepCname = levAgtDepCname;
  }

  public String getEformStatusCod() {
    return eformStatusCod;
  }

  public void setEformStatusCod(String eformStatusCod) {
    this.eformStatusCod = eformStatusCod;
  }

  public Date getCreDte() {
    return creDte;
  }

  public void setCreDte(Date creDte) {
    this.creDte = creDte;
  }

  public Long getIdentityField() {
    return identityField;
  }

  public void setIdentityField(Long identityField) {
    this.identityField = identityField;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getBtStatus() {
    return btStatus;
  }

  public void setBtStatus(String btStatus) {
    this.btStatus = btStatus;
  }

  public String getBtNo() {
    return btNo;
  }

  public void setBtNo(String btNo) {
    this.btNo = btNo;
  }

}
