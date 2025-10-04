package nccc.btp.rfc;

public class TInput {

  // 文件日期
  private String BLDAT;
  // 申報日期
  private String VATDATE;
  // 發票號碼
  private String XBLNR;
  // 稅格式
  private String ZFORM_CODE;
  // 統一編號
  private String STCEG;
  // 銷售金額
  private String HWBAS;
  // 銷售稅額
  private String HWSTE;
  // 稅類型
  private String TAX_TYPE;
  // 憑證類型
  private String CUS_TYPE;
  // 扣抵代號
  private String AM_TYPE;

  public TInput() {}

  public TInput(String BLDAT, String VATDATE, String XBLNR, String ZFORM_CODE, String STCEG,
      String HWBAS, String HWSTE, String TAX_TYPE, String CUS_TYPE, String AM_TYPE) {
    this.BLDAT = BLDAT;
    this.VATDATE = VATDATE;
    this.XBLNR = XBLNR;
    this.ZFORM_CODE = ZFORM_CODE;
    this.STCEG = STCEG;
    this.HWBAS = HWBAS;
    this.HWSTE = HWSTE;
    this.TAX_TYPE = TAX_TYPE;
    this.CUS_TYPE = CUS_TYPE;
    this.AM_TYPE = AM_TYPE;
  }

  public String getBLDAT() {
    return BLDAT;
  }

  public void setBLDAT(String bLDAT) {
    BLDAT = bLDAT;
  }

  public String getVATDATE() {
    return VATDATE;
  }

  public void setVATDATE(String vATDATE) {
    VATDATE = vATDATE;
  }

  public String getXBLNR() {
    return XBLNR;
  }

  public void setXBLNR(String xBLNR) {
    XBLNR = xBLNR;
  }

  public String getZFORM_CODE() {
    return ZFORM_CODE;
  }

  public void setZFORM_CODE(String zFORM_CODE) {
    ZFORM_CODE = zFORM_CODE;
  }

  public String getSTCEG() {
    return STCEG;
  }

  public void setSTCEG(String sTCEG) {
    STCEG = sTCEG;
  }

  public String getHWBAS() {
    return HWBAS;
  }

  public void setHWBAS(String hWBAS) {
    HWBAS = hWBAS;
  }

  public String getHWSTE() {
    return HWSTE;
  }

  public void setHWSTE(String hWSTE) {
    HWSTE = hWSTE;
  }

  public String getTAX_TYPE() {
    return TAX_TYPE;
  }

  public void setTAX_TYPE(String tAX_TYPE) {
    TAX_TYPE = tAX_TYPE;
  }

  public String getCUS_TYPE() {
    return CUS_TYPE;
  }

  public void setCUS_TYPE(String cUS_TYPE) {
    CUS_TYPE = cUS_TYPE;
  }

  public String getAM_TYPE() {
    return AM_TYPE;
  }

  public void setAM_TYPE(String aM_TYPE) {
    AM_TYPE = aM_TYPE;
  }

  @Override
  public String toString() {
    return "TInput [BLDAT=" + BLDAT + ", VATDATE=" + VATDATE + ", XBLNR=" + XBLNR + ", ZFORM_CODE="
        + ZFORM_CODE + ", STCEG=" + STCEG + ", HWBAS=" + HWBAS + ", HWSTE=" + HWSTE + ", TAX_TYPE="
        + TAX_TYPE + ", CUS_TYPE=" + CUS_TYPE + ", AM_TYPE=" + AM_TYPE + "]";
  }

}
