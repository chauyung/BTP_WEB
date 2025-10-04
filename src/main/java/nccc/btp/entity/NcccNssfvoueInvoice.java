/**
 * 
 */
package nccc.btp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NCCC_NSSFVOUE_INVOICE")
public class NcccNssfvoueInvoice {

  /** 發票號碼 */
  @Id
  @Column(name = "TA015", length = 10)
  private String ta015;

  /** X+買受人特店代號; X+買受人統一編號 */
  @Column(name = "XC003", length = 9)
  private String xc003;

  /** 電子發票(輸入7) */
  @Column(name = "XC011", length = 1)
  private String xc011;

  /** 課稅別：1.應稅內含 B2C；2.應稅外加 B2B */
  @Column(name = "XC012", length = 1)
  private String xc012;

  /** 收入類別代號 */
  @Column(name = "XD003", length = 10)
  private String xd003;

  /** 數量 (輸入1) */
  @Column(name = "XD004", length = 255)
  private String xd004;

  /** 單價 (可空白或0) */
  @Column(name = "XD005", length = 255)
  private String xd005;

  /** 含稅金額(B2C) / 稅前金額(B2B) */
  @Column(name = "XD006", length = 255)
  private String xd006;

  /** 稅前金額 */
  @Column(name = "XD007", length = 255)
  private String xd007;

  /** 稅額 */
  @Column(name = "XD008", length = 255)
  private String xd008;

  /** 發票摘要 */
  @Column(name = "XD009", length = 255)
  private String xd009;

  /** 折帳日期 (可空白) */
  @Column(name = "XD011", length = 8)
  private String xd011;

  /** 發票備註 (手續費及商店費等皆列示特店代號) */
  @Column(name = "XD012", length = 255)
  private String xd012;

  /** 買受人統一編號 (B2C 空白) */
  @Column(name = "TA007", length = 10)
  private String ta007;

  /** 客戶全名 */
  @Column(name = "TA008", length = 255)
  private String ta008;

  /** 發票日期 */
  @Column(name = "TA016", length = 8)
  private String ta016;

  /** 隨機碼 (可空白) */
  @Column(name = "TA201", length = 10)
  private String ta201;

  /** 手機條碼載具，是 B2C 特店才填入 */
  @Column(name = "TA204", length = 255)
  private String ta204;

  /** 捐贈碼，是 B2C 特店才填入 */
  @Column(name = "TA205", length = 255)
  private String ta205;

  /** 底稿批號 */
  @Column(name = "XA001", length = 11)
  private String xa001;

  public NcccNssfvoueInvoice() {}

  public String getTa015() {
    return ta015;
  }

  public void setTa015(String ta015) {
    this.ta015 = ta015;
  }

  public String getXc003() {
    return xc003;
  }

  public void setXc003(String xc003) {
    this.xc003 = xc003;
  }

  public String getXc011() {
    return xc011;
  }

  public void setXc011(String xc011) {
    this.xc011 = xc011;
  }

  public String getXc012() {
    return xc012;
  }

  public void setXc012(String xc012) {
    this.xc012 = xc012;
  }

  public String getXd003() {
    return xd003;
  }

  public void setXd003(String xd003) {
    this.xd003 = xd003;
  }

  public String getXd004() {
    return xd004;
  }

  public void setXd004(String xd004) {
    this.xd004 = xd004;
  }

  public String getXd005() {
    return xd005;
  }

  public void setXd005(String xd005) {
    this.xd005 = xd005;
  }

  public String getXd006() {
    return xd006;
  }

  public void setXd006(String xd006) {
    this.xd006 = xd006;
  }

  public String getXd007() {
    return xd007;
  }

  public void setXd007(String xd007) {
    this.xd007 = xd007;
  }

  public String getXd008() {
    return xd008;
  }

  public void setXd008(String xd008) {
    this.xd008 = xd008;
  }

  public String getXd009() {
    return xd009;
  }

  public void setXd009(String xd009) {
    this.xd009 = xd009;
  }

  public String getXd011() {
    return xd011;
  }

  public void setXd011(String xd011) {
    this.xd011 = xd011;
  }

  public String getXd012() {
    return xd012;
  }

  public void setXd012(String xd012) {
    this.xd012 = xd012;
  }

  public String getTa007() {
    return ta007;
  }

  public void setTa007(String ta007) {
    this.ta007 = ta007;
  }

  public String getTa008() {
    return ta008;
  }

  public void setTa008(String ta008) {
    this.ta008 = ta008;
  }

  public String getTa016() {
    return ta016;
  }

  public void setTa016(String ta016) {
    this.ta016 = ta016;
  }

  public String getTa201() {
    return ta201;
  }

  public void setTa201(String ta201) {
    this.ta201 = ta201;
  }

  public String getTa204() {
    return ta204;
  }

  public void setTa204(String ta204) {
    this.ta204 = ta204;
  }

  public String getTa205() {
    return ta205;
  }

  public void setTa205(String ta205) {
    this.ta205 = ta205;
  }

  public String getXa001() {
    return xa001;
  }

  public void setXa001(String xa001) {
    this.xa001 = xa001;
  }
  
}
