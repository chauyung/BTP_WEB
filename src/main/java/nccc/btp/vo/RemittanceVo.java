package nccc.btp.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RemittanceVo {

  @JsonProperty("fileName")
  private String fileName;

  @JsonProperty("LAUFD")
  private String LAUFD;

  @JsonProperty("LAUFI")
  private String LAUFI;

  @JsonProperty("SND_DATE")
  private String SND_DATE;
  
  public RemittanceVo () {}

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getLAUFD() {
    return LAUFD;
  }

  public void setLAUFD(String lAUFD) {
    LAUFD = lAUFD;
  }

  public String getLAUFI() {
    return LAUFI;
  }

  public void setLAUFI(String lAUFI) {
    LAUFI = lAUFI;
  }

  public String getSND_DATE() {
    return SND_DATE;
  }

  public void setSND_DATE(String sND_DATE) {
    SND_DATE = sND_DATE;
  }

}
