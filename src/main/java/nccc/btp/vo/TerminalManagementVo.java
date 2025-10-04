package nccc.btp.vo;

import nccc.btp.entity.NcccEmsMgm;
import nccc.btp.entity.SapEmsRecStatus;

public class TerminalManagementVo {

  private NcccEmsMgm ncccEmsMgm;

  private SapEmsRecStatus sapEmsRecStatus;
  
  public TerminalManagementVo() {}

  public TerminalManagementVo(NcccEmsMgm ncccEmsMgm, SapEmsRecStatus sapEmsRecStatus) {
    this.ncccEmsMgm = ncccEmsMgm;
    this.sapEmsRecStatus = sapEmsRecStatus;
  }

  public NcccEmsMgm getNcccEmsMgm() {
    return ncccEmsMgm;
  }

  public void setNcccEmsMgm(NcccEmsMgm ncccEmsMgm) {
    this.ncccEmsMgm = ncccEmsMgm;
  }

  public SapEmsRecStatus getSapEmsRecStatus() {
    return sapEmsRecStatus;
  }

  public void setSapEmsRecStatus(SapEmsRecStatus sapEmsRecStatus) {
    this.sapEmsRecStatus = sapEmsRecStatus;
  }

}
