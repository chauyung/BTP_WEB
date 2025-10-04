package nccc.btp.rfc;

public class ZInvoice {

  // 發票號碼
  private String INVNO;
  // 發票日期
  private String GUIDATE;
  // 稅基
  private String TXBHW;
  // 稅額
  private String MWSTS;
  // 稅格式
  private String GUIFORMAT;
  // 統一編號
  private String VATCODE;

  public ZInvoice() {}

  public ZInvoice(String INVNO, String GUIDATE, String TXBHW, String MWSTS, String GUIFORMAT,
      String VATCODE) {
    this.INVNO = INVNO;
    this.GUIDATE = GUIDATE;
    this.TXBHW = TXBHW;
    this.MWSTS = MWSTS;
    this.GUIFORMAT = GUIFORMAT;
    this.VATCODE = VATCODE;
  }

  public String getINVNO() {
    return INVNO;
  }

  public void setINVNO(String INVNO) {
    this.INVNO = INVNO;
  }

  public String getGUIDATE() {
    return GUIDATE;
  }

  public void setGUIDATE(String GUIDATE) {
    this.GUIDATE = GUIDATE;
  }

  public String getTXBHW() {
    return TXBHW;
  }

  public void setTXBHW(String TXBHW) {
    this.TXBHW = TXBHW;
  }

  public String getMWSTS() {
    return MWSTS;
  }

  public void setMWSTS(String MWSTS) {
    this.MWSTS = MWSTS;
  }

  public String getGUIFORMAT() {
    return GUIFORMAT;
  }

  public void setGUIFORMAT(String GUIFORMAT) {
    this.GUIFORMAT = GUIFORMAT;
  }

  public String getVATCODE() {
    return VATCODE;
  }

  public void setVATCODE(String VATCODE) {
    this.VATCODE = VATCODE;
  }

  @Override
  public String toString() {
    return "ZInvoice{" + "INVNO='" + INVNO + '\'' + ", GUIDATE='" + GUIDATE + '\'' + ", TXBHW='"
        + TXBHW + '\'' + ", MWSTS='" + MWSTS + '\'' + ", GUIFORMAT='" + GUIFORMAT + '\''
        + ", VATCODE='" + VATCODE + '\'' + '}';
  }
}
