/**
 * 
 */
package nccc.btp.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import nccc.btp.dto.AcceptanceGeneralAcceptance;

/**
 * 驗收單Vo
 */
public class AcceptanceFormVo {

  // 驗收單號
  private String revNo;

  // 員工編號
  private String hrid;

  // 申請人
  private String applicant;

  // 部門
  private String department;

  // 請購單號
  private String prNo;

  // 採購單號
  private String poNo;

  // 議價核准公文文號
  private String bargDocNo;

  // 本次驗收金額
  private BigDecimal thisAcceptanceAmount;

  // 請購目的
  private String remark;

  // 議價核准公文附件
  private String bargDocFile;

  // 一般驗收
  private List<AcceptanceGeneralAcceptance> generalAcceptanceList;

  public AcceptanceFormVo() {
    this.generalAcceptanceList = new ArrayList<>();
  }

  public String getRevNo() {
    return revNo;
  }

  public void setRevNo(String revNo) {
    this.revNo = revNo;
  }

  public String getHrid() {
    return hrid;
  }

  public void setHrid(String hrid) {
    this.hrid = hrid;
  }

  public String getApplicant() {
    return applicant;
  }

  public void setApplicant(String applicant) {
    this.applicant = applicant;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getPrNo() {
    return prNo;
  }

  public void setPrNo(String prNo) {
    this.prNo = prNo;
  }

  public String getPoNo() {
    return poNo;
  }

  public void setPoNo(String poNo) {
    this.poNo = poNo;
  }

  public String getBargDocNo() {
    return bargDocNo;
  }

  public void setBargDocNo(String bargDocNo) {
    this.bargDocNo = bargDocNo;
  }

  public BigDecimal getThisAcceptanceAmount() {
    return thisAcceptanceAmount;
  }

  public void setThisAcceptanceAmount(BigDecimal thisAcceptanceAmount) {
    this.thisAcceptanceAmount = thisAcceptanceAmount;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getBargDocFile() {
    return bargDocFile;
  }

  public void setBargDocFile(String bargDocFile) {
    this.bargDocFile = bargDocFile;
  }

  public List<AcceptanceGeneralAcceptance> getGeneralAcceptanceList() {
    return generalAcceptanceList;
  }

  public void setGeneralAcceptanceList(List<AcceptanceGeneralAcceptance> generalAcceptanceList) {
    this.generalAcceptanceList = generalAcceptanceList;
  }

}
