package nccc.btp.rfc;

public class ZItem {

  // 過帳碼
  private String BSCHL;
  // 科目
  private String NEWKO;
  // 總帳科目
  private String HKONT;
  // 文件幣別金額
  private String WRBTR;
  // 本國貨幣金額
  private String DMBTR;
  // 稅碼
  private String MWSKZ;
  // 稅基
  private String TXBFW;
  // 到期日
  private String ZFBDT;
  // 成本中心
  private String KOSTL;
  // 指派
  private String ZUONR;
  // 內文
  private String SGTXT;
  // 參考碼 1
  private String XREF1;
  // 參考碼 2
  private String XREF2;
  // 參考碼 3
  private String XREF3;

  public ZItem() {}

  public ZItem(String BSCHL, String NEWKO, String HKONT, String WRBTR, String DMBTR, String MWSKZ,
      String TXBFW, String ZFBDT, String KOSTL, String ZUONR, String SGTXT, String XREF1,
      String XREF2, String XREF3) {
    this.BSCHL = BSCHL;
    this.NEWKO = NEWKO;
    this.HKONT = HKONT;
    this.WRBTR = WRBTR;
    this.DMBTR = DMBTR;
    this.MWSKZ = MWSKZ;
    this.TXBFW = TXBFW;
    this.ZFBDT = ZFBDT;
    this.KOSTL = KOSTL;
    this.ZUONR = ZUONR;
    this.SGTXT = SGTXT;
    this.XREF1 = XREF1;
    this.XREF2 = XREF2;
    this.XREF3 = XREF3;
  }

  public String getBSCHL() {
    return BSCHL;
  }

  public void setBSCHL(String BSCHL) {
    this.BSCHL = BSCHL;
  }

  public String getNEWKO() {
    return NEWKO;
  }

  public void setNEWKO(String NEWKO) {
    this.NEWKO = NEWKO;
  }

  public String getHKONT() {
    return HKONT;
  }

  public void setHKONT(String HKONT) {
    this.HKONT = HKONT;
  }

  public String getWRBTR() {
    return WRBTR;
  }

  public void setWRBTR(String WRBTR) {
    this.WRBTR = WRBTR;
  }

  public String getDMBTR() {
    return DMBTR;
  }

  public void setDMBTR(String DMBTR) {
    this.DMBTR = DMBTR;
  }

  public String getMWSKZ() {
    return MWSKZ;
  }

  public void setMWSKZ(String MWSKZ) {
    this.MWSKZ = MWSKZ;
  }

  public String getTXBFW() {
    return TXBFW;
  }

  public void setTXBFW(String TXBFW) {
    this.TXBFW = TXBFW;
  }

  public String getZFBDT() {
    return ZFBDT;
  }

  public void setZFBDT(String ZFBDT) {
    this.ZFBDT = ZFBDT;
  }

  public String getKOSTL() {
    return KOSTL;
  }

  public void setKOSTL(String KOSTL) {
    this.KOSTL = KOSTL;
  }

  public String getZUONR() {
    return ZUONR;
  }

  public void setZUONR(String ZUONR) {
    this.ZUONR = ZUONR;
  }

  public String getSGTXT() {
    return SGTXT;
  }

  public void setSGTXT(String SGTXT) {
    this.SGTXT = SGTXT;
  }


  public String getXREF1() {
    return XREF1;
  }

  public void setXREF1(String XREF1) {
    this.XREF1 = XREF1;
  }

  public String getXREF2() {
    return XREF2;
  }

  public void setXREF2(String XREF2) {
    this.XREF2 = XREF2;
  }

  public String getXREF3() {
    return XREF3;
  }

  public void setXREF3(String XREF3) {
    this.XREF3 = XREF3;
  }

  @Override
  public String toString() {
    return "ZItem [BSCHL=" + BSCHL + ", NEWKO=" + NEWKO + ", HKONT=" + HKONT + ", WRBTR=" + WRBTR
        + ", DMBTR=" + DMBTR + ", MWSKZ=" + MWSKZ + ", TXBFW=" + TXBFW + ", ZFBDT=" + ZFBDT
        + ", KOSTL=" + KOSTL + ", ZUONR=" + ZUONR + ", SGTXT=" + SGTXT + ", XREF1=" + XREF1
        + ", XREF2=" + XREF2 + ", XREF3=" + XREF3 + "]";
  }
}
