/**
 * 
 */
package nccc.btp.rfc;

public class T202 {

  // X+買受人特店代號
  // X+買受人統一編號
  // (非特店，如參加機構)
  private String XC003;
  // 電子發票(輸入7)
  private String XC011;
  // 課稅別
  // 1.應稅內含B2C
  // 2.應稅外加B2B
  private String XC012;
  // 收入類別代號
  private String XD003;
  // 數量(輸入 1 )
  private String XD004;
  // 單價
  // (可空白或0)
  private String XD005;
  // 含稅金額(B2C)
  // 稅前金額(B2B)
  private String XD006;
  // 稅前金額
  private String XD007;
  // 稅額
  private String XD008;
  // 發票摘要
  private String XD009;
  // 扣帳日期 (可空白)
  private String XD011;
  // 發票備註 (手續費及商服費等皆列示特店代號)
  private String XD012;
  // 買受人統一編號 (B2C空白)
  private String TA007;
  // 客戶全名
  private String TA008;
  // 發票號碼
  private String TA015;
  // 發票日期
  private String TA016;
  // 隨機碼(空白)
  private String TA201;
  // 手機條碼載具
  // 是B2C特店才填入
  // 不是，則填空白
  private String TA204;
  // 捐贈碼
  // 是B2C特店才填入
  // 不是，則填空白
  private String TA205;
  // 底稿批號
  private String XA001;

  public T202() {}

  public T202(String xC003, String xC011, String xC012, String xD003, String xD004, String xD005,
      String xD006, String xD007, String xD008, String xD009, String xD011, String xD012,
      String tA007, String tA008, String tA015, String tA016, String tA201, String tA204,
      String tA205, String xA001) {
    super();
    XC003 = xC003;
    XC011 = xC011;
    XC012 = xC012;
    XD003 = xD003;
    XD004 = xD004;
    XD005 = xD005;
    XD006 = xD006;
    XD007 = xD007;
    XD008 = xD008;
    XD009 = xD009;
    XD011 = xD011;
    XD012 = xD012;
    TA007 = tA007;
    TA008 = tA008;
    TA015 = tA015;
    TA016 = tA016;
    TA201 = tA201;
    TA204 = tA204;
    TA205 = tA205;
    XA001 = xA001;
  }

  public String getXC003() {
    return XC003;
  }

  public void setXC003(String xC003) {
    XC003 = xC003;
  }

  public String getXC011() {
    return XC011;
  }

  public void setXC011(String xC011) {
    XC011 = xC011;
  }

  public String getXC012() {
    return XC012;
  }

  public void setXC012(String xC012) {
    XC012 = xC012;
  }

  public String getXD003() {
    return XD003;
  }

  public void setXD003(String xD003) {
    XD003 = xD003;
  }

  public String getXD004() {
    return XD004;
  }

  public void setXD004(String xD004) {
    XD004 = xD004;
  }

  public String getXD005() {
    return XD005;
  }

  public void setXD005(String xD005) {
    XD005 = xD005;
  }

  public String getXD006() {
    return XD006;
  }

  public void setXD006(String xD006) {
    XD006 = xD006;
  }

  public String getXD007() {
    return XD007;
  }

  public void setXD007(String xD007) {
    XD007 = xD007;
  }

  public String getXD008() {
    return XD008;
  }

  public void setXD008(String xD008) {
    XD008 = xD008;
  }

  public String getXD009() {
    return XD009;
  }

  public void setXD009(String xD009) {
    XD009 = xD009;
  }

  public String getXD011() {
    return XD011;
  }

  public void setXD011(String xD011) {
    XD011 = xD011;
  }

  public String getXD012() {
    return XD012;
  }

  public void setXD012(String xD012) {
    XD012 = xD012;
  }

  public String getTA007() {
    return TA007;
  }

  public void setTA007(String tA007) {
    TA007 = tA007;
  }

  public String getTA008() {
    return TA008;
  }

  public void setTA008(String tA008) {
    TA008 = tA008;
  }

  public String getTA015() {
    return TA015;
  }

  public void setTA015(String tA015) {
    TA015 = tA015;
  }

  public String getTA016() {
    return TA016;
  }

  public void setTA016(String tA016) {
    TA016 = tA016;
  }

  public String getTA201() {
    return TA201;
  }

  public void setTA201(String tA201) {
    TA201 = tA201;
  }

  public String getTA204() {
    return TA204;
  }

  public void setTA204(String tA204) {
    TA204 = tA204;
  }

  public String getTA205() {
    return TA205;
  }

  public void setTA205(String tA205) {
    TA205 = tA205;
  }

  public String getXA001() {
    return XA001;
  }

  public void setXA001(String xA001) {
    XA001 = xA001;
  }

  @Override
  public String toString() {
    return "T202 [XC003=" + XC003 + ", XC011=" + XC011 + ", XC012=" + XC012 + ", XD003=" + XD003
        + ", XD004=" + XD004 + ", XD005=" + XD005 + ", XD006=" + XD006 + ", XD007=" + XD007
        + ", XD008=" + XD008 + ", XD009=" + XD009 + ", XD011=" + XD011 + ", XD012=" + XD012
        + ", TA007=" + TA007 + ", TA008=" + TA008 + ", TA015=" + TA015 + ", TA016=" + TA016
        + ", TA201=" + TA201 + ", TA204=" + TA204 + ", TA205=" + TA205 + ", XA001=" + XA001 + "]";
  }

}
