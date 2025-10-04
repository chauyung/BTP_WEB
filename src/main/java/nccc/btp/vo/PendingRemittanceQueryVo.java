package nccc.btp.vo;

import java.util.List;

public class PendingRemittanceQueryVo {

  private String bankNo;

  private String startDate;

  private String endDate;

  private String depositAmount;

  private String writeOffAmount;

  private String depositDesc;

  private String belnr;

  private String type;

  private List<Long> ids;

  private String checkoutDate;

  public String getBankNo() {
    return bankNo;
  }

  public void setBankNo(String bankNo) {
    this.bankNo = bankNo;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getDepositAmount() {
    return depositAmount;
  }

  public void setDepositAmount(String depositAmount) {
    this.depositAmount = depositAmount;
  }

  public String getWriteOffAmount() {
    return writeOffAmount;
  }

  public void setWriteOffAmount(String writeOffAmount) {
    this.writeOffAmount = writeOffAmount;
  }

  public String getDepositDesc() {
    return depositDesc;
  }

  public void setDepositDesc(String depositDesc) {
    this.depositDesc = depositDesc;
  }

  public String getBelnr() {
    return belnr;
  }

  public void setBelnr(String belnr) {
    this.belnr = belnr;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<Long> getIds() {
    return ids;
  }

  public void setIds(List<Long> ids) {
    this.ids = ids;
  }

  public String getCheckoutDate() {
    return checkoutDate;
  }

  public void setCheckoutDate(String checkoutDate) {
    this.checkoutDate = checkoutDate;
  }

}
