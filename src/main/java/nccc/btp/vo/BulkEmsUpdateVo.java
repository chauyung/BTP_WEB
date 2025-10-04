/**
 * 
 */
package nccc.btp.vo;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public class BulkEmsUpdateVo {

  private String wealthNo;

  private String ncccWealthNo;

  // 減損日期（只有大批減損會用到）
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate impairmentDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate postingDate;

  private String docNo;

  private String description;

  public String getWealthNo() {
    return wealthNo;
  }

  public void setWealthNo(String wealthNo) {
    this.wealthNo = wealthNo;
  }

  public String getNcccWealthNo() {
    return ncccWealthNo;
  }

  public void setNcccWealthNo(String ncccWealthNo) {
    this.ncccWealthNo = ncccWealthNo;
  }

  public LocalDate getImpairmentDate() {
    return impairmentDate;
  }

  public void setImpairmentDate(LocalDate impairmentDate) {
    this.impairmentDate = impairmentDate;
  }

  public LocalDate getPostingDate() {
    return postingDate;
  }

  public void setPostingDate(LocalDate postingDate) {
    this.postingDate = postingDate;
  }

  public String getDocNo() {
    return docNo;
  }

  public void setDocNo(String docNo) {
    this.docNo = docNo;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
