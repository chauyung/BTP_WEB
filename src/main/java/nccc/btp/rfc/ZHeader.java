package nccc.btp.rfc;

public class ZHeader {

  // 來源系統
  private String ZSYSTEM;
  // 公司代碼
  private String BUKRS;
  // 文件日期
  private String BLDAT;
  // 過帳日期
  private String BUDAT;
  // 文件類型
  private String BLART;
  // 幣別
  private String WAERS;
  // 匯率
  private String KURSF;
  // 參考
  private String XBLNR;
  // 文件表頭內文
  private String BKTXT;
  // 文件表頭的參考碼 1 內部
  private String XREF1_HD;
  // 文件表頭的參考碼 2 內部
  private String XREF2_HD;

  public ZHeader() {}

  public ZHeader(String ZSYSTEM, String BUKRS, String BLDAT, String BUDAT, String BLART,
      String WAERS, String KURSF, String XBLNR, String BKTXT, String XREF1_HD, String XREF2_HD) {
    this.ZSYSTEM = ZSYSTEM;
    this.BUKRS = BUKRS;
    this.BLDAT = BLDAT;
    this.BUDAT = BUDAT;
    this.BLART = BLART;
    this.WAERS = WAERS;
    this.KURSF = KURSF;
    this.XBLNR = XBLNR;
    this.BKTXT = BKTXT;
    this.XREF1_HD = XREF1_HD;
    this.XREF2_HD = XREF2_HD;
  }

  public String getZSYSTEM() {
    return ZSYSTEM;
  }

  public void setZSYSTEM(String ZSYSTEM) {
    this.ZSYSTEM = ZSYSTEM;
  }

  public String getBUKRS() {
    return BUKRS;
  }

  public void setBUKRS(String BUKRS) {
    this.BUKRS = BUKRS;
  }

  public String getBLDAT() {
    return BLDAT;
  }

  public void setBLDAT(String BLDAT) {
    this.BLDAT = BLDAT;
  }

  public String getBUDAT() {
    return BUDAT;
  }

  public void setBUDAT(String BUDAT) {
    this.BUDAT = BUDAT;
  }

  public String getBLART() {
    return BLART;
  }

  public void setBLART(String BLART) {
    this.BLART = BLART;
  }

  public String getWAERS() {
    return WAERS;
  }

  public void setWAERS(String WAERS) {
    this.WAERS = WAERS;
  }

  public String getKURSF() {
    return KURSF;
  }

  public void setKURSF(String kURSF) {
    KURSF = kURSF;
  }

  public String getXBLNR() {
    return XBLNR;
  }

  public void setXBLNR(String XBLNR) {
    this.XBLNR = XBLNR;
  }

  public String getBKTXT() {
    return BKTXT;
  }

  public void setBKTXT(String BKTXT) {
    this.BKTXT = BKTXT;
  }


  public String getXREF1_HD() {
    return XREF1_HD;
  }

  public void setXREF1_HD(String XREF1_HD) {
    this.XREF1_HD = XREF1_HD;
  }

  public String getXREF2_HD() {
    return XREF2_HD;
  }

  public void setXREF2_HD(String XREF2_HD) {
    this.XREF2_HD = XREF2_HD;
  }

  @Override
  public String toString() {
    return "ZHeader [ZSYSTEM=" + ZSYSTEM + ", BUKRS=" + BUKRS + ", BLDAT=" + BLDAT + ", BUDAT="
        + BUDAT + ", BLART=" + BLART + ", WAERS=" + WAERS + ", KURSF=" + KURSF + ", XBLNR=" + XBLNR
        + ", BKTXT=" + BKTXT + ", XREF1_HD=" + XREF1_HD + ", XREF2_HD=" + XREF2_HD + "]";
  }

}
