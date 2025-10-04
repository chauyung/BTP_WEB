package nccc.btp.rfc;

public class ZInfo {

  // 來源系統
  private String ZSYSTEM;
  // 設備代號
  private String ZEDCNO;
  // 公司代碼
  private String BUKRS;
  // 資產號碼
  private String ANLN1;
  // 子資產號碼
  private String ANLN2;
  // 文件日期
  private String BLDAT;
  // 過帳日期
  private String BUDAT;
  // 資產起息日
  private String BZDAT;
  // 項目內文
  private String SGTXT;
  // 數量
  private String MENGE;
  // 前一年購置
  private String XAALT;
  // 自今年度購置
  private String XANEU;

  public ZInfo() {}

  public ZInfo(String ZSYSTEM, String ZEDCNO, String BUKRS, String ANLN1, String ANLN2,
      String BLDAT, String BUDAT, String BZDAT, String SGTXT, String MENGE, String XAALT,
      String XANEU) {
    this.ZSYSTEM = ZSYSTEM;
    this.ZEDCNO = ZEDCNO;
    this.BUKRS = BUKRS;
    this.ANLN1 = ANLN1;
    this.ANLN2 = ANLN2;
    this.BLDAT = BLDAT;
    this.BUDAT = BUDAT;
    this.BZDAT = BZDAT;
    this.SGTXT = SGTXT;
    this.MENGE = MENGE;
    this.XAALT = XAALT;
    this.XANEU = XANEU;
  }

  public String getZSYSTEM() {
    return ZSYSTEM;
  }

  public void setZSYSTEM(String zSYSTEM) {
    ZSYSTEM = zSYSTEM;
  }

  public String getZEDCNO() {
    return ZEDCNO;
  }

  public void setZEDCNO(String zEDCNO) {
    ZEDCNO = zEDCNO;
  }

  public String getBUKRS() {
    return BUKRS;
  }

  public void setBUKRS(String bUKRS) {
    BUKRS = bUKRS;
  }

  public String getANLN1() {
    return ANLN1;
  }

  public void setANLN1(String aNLN1) {
    ANLN1 = aNLN1;
  }

  public String getANLN2() {
    return ANLN2;
  }

  public void setANLN2(String aNLN2) {
    ANLN2 = aNLN2;
  }

  public String getBLDAT() {
    return BLDAT;
  }

  public void setBLDAT(String bLDAT) {
    BLDAT = bLDAT;
  }

  public String getBUDAT() {
    return BUDAT;
  }

  public void setBUDAT(String bUDAT) {
    BUDAT = bUDAT;
  }

  public String getBZDAT() {
    return BZDAT;
  }

  public void setBZDAT(String bZDAT) {
    BZDAT = bZDAT;
  }

  public String getSGTXT() {
    return SGTXT;
  }

  public void setSGTXT(String sGTXT) {
    SGTXT = sGTXT;
  }

  public String getMENGE() {
    return MENGE;
  }

  public void setMENGE(String mENGE) {
    MENGE = mENGE;
  }

  public String getXAALT() {
    return XAALT;
  }

  public void setXAALT(String xAALT) {
    XAALT = xAALT;
  }

  public String getXANEU() {
    return XANEU;
  }

  public void setXANEU(String xANEU) {
    XANEU = xANEU;
  }

  @Override
  public String toString() {
    return "ZInfo [ZSYSTEM=" + ZSYSTEM + ", ZEDCNO=" + ZEDCNO + ", BUKRS=" + BUKRS + ", ANLN1="
        + ANLN1 + ", ANLN2=" + ANLN2 + ", BLDAT=" + BLDAT + ", BUDAT=" + BUDAT + ", BZDAT=" + BZDAT
        + ", SGTXT=" + SGTXT + ", MENGE=" + MENGE + ", XAALT=" + XAALT + ", XANEU=" + XANEU + "]";
  }

}
