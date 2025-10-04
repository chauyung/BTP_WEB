package nccc.btp.rfc;

public class ZReturn {

  // 訊息類型
  private String TYPE;
  // 公司代碼
  private String BUKRS;
  // 會計文件號碼
  private String BELNR;
  // 會計年度
  private String GJAHR;
  // 訊息內文
  private String MESSAGE;

  public ZReturn() {}

  public ZReturn(String TYPE, String BUKRS, String BELNR, String GJAHR, String MESSAGE) {
    this.TYPE = TYPE;
    this.BUKRS = BUKRS;
    this.BELNR = BELNR;
    this.GJAHR = GJAHR;
    this.MESSAGE = MESSAGE;
  }

  public String getTYPE() {
    return TYPE;
  }

  public void setTYPE(String TYPE) {
    this.TYPE = TYPE;
  }

  public String getBUKRS() {
    return BUKRS;
  }

  public void setBUKRS(String BUKRS) {
    this.BUKRS = BUKRS;
  }

  public String getBELNR() {
    return BELNR;
  }

  public void setBELNR(String BELNR) {
    this.BELNR = BELNR;
  }

  public String getGJAHR() {
    return GJAHR;
  }

  public void setGJAHR(String GJAHR) {
    this.GJAHR = GJAHR;
  }

  public String getMESSAGE() {
    return MESSAGE;
  }

  public void setMESSAGE(String MESSAGE) {
    this.MESSAGE = MESSAGE;
  }

  @Override
  public String toString() {
    return "ZInvoice{" + "TYPE='" + TYPE + '\'' + ", BUKRS='" + BUKRS + '\'' + ", BELNR='" + BELNR
        + '\'' + ", GJAHR='" + GJAHR + '\'' + ", MESSAGE='" + MESSAGE + '\'' + '}';
  }
}
