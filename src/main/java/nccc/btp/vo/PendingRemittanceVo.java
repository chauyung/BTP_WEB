package nccc.btp.vo;

import nccc.btp.entity.NcccPendingRemittance;
import nccc.btp.entity.SapPendingRemittanceStatus;

public class PendingRemittanceVo {

  private NcccPendingRemittance ncccPendingRemittance;

  private SapPendingRemittanceStatus sapPendingRemittanceStatus;
  
  public PendingRemittanceVo() {}

  public PendingRemittanceVo(NcccPendingRemittance ncccPendingRemittance,
      SapPendingRemittanceStatus sapPendingRemittanceStatus) {
    this.ncccPendingRemittance = ncccPendingRemittance;
    this.sapPendingRemittanceStatus = sapPendingRemittanceStatus;
  }

  public NcccPendingRemittance getNcccPendingRemittance() {
    return ncccPendingRemittance;
  }

  public void setNcccPendingRemittance(NcccPendingRemittance ncccPendingRemittance) {
    this.ncccPendingRemittance = ncccPendingRemittance;
  }

  public SapPendingRemittanceStatus getSapPendingRemittanceStatus() {
    return sapPendingRemittanceStatus;
  }

  public void setSapPendingRemittanceStatus(SapPendingRemittanceStatus sapPendingRemittanceStatus) {
    this.sapPendingRemittanceStatus = sapPendingRemittanceStatus;
  }

}
