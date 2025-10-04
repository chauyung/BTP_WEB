package nccc.btp.vo;

import java.util.ArrayList;
import java.util.List;
import nccc.btp.entity.NcccNssfvoueRecData;
import nccc.btp.entity.NcccNssfvoueRecHeader;
import nccc.btp.entity.SapNssfvoueRecStatus;

public class ReceivablesManagementVo {

  private NcccNssfvoueRecHeader ncccNssfvoueRecHeader;
  
  private List<NcccNssfvoueRecData> ncccNssfvoueRecDataList;
  
  private SapNssfvoueRecStatus sapNssfvoueRecStatus;

  public ReceivablesManagementVo() {
    this.ncccNssfvoueRecDataList = new ArrayList<>();
  }
  
  public ReceivablesManagementVo(NcccNssfvoueRecHeader ncccNssfvoueRecHeader) {
    this.ncccNssfvoueRecHeader = ncccNssfvoueRecHeader;
    this.ncccNssfvoueRecDataList = new ArrayList<>();
  }
  
  public NcccNssfvoueRecHeader getNcccNssfvoueRecHeader() {
    return ncccNssfvoueRecHeader;
  }

  public void setNcccNssfvoueRecHeader(NcccNssfvoueRecHeader ncccNssfvoueRecHeader) {
    this.ncccNssfvoueRecHeader = ncccNssfvoueRecHeader;
  }

  public List<NcccNssfvoueRecData> getNcccNssfvoueRecDataList() {
    return ncccNssfvoueRecDataList;
  }

  public void setNcccNssfvoueRecDataList(List<NcccNssfvoueRecData> ncccNssfvoueRecDataList) {
    this.ncccNssfvoueRecDataList = ncccNssfvoueRecDataList;
  }
  
  public void addNcccNssfvoueRecData(NcccNssfvoueRecData ncccNssfvoueRecData){
    ncccNssfvoueRecDataList.add(ncccNssfvoueRecData);
}

  public SapNssfvoueRecStatus getSapNssfvoueRecStatus() {
    return sapNssfvoueRecStatus;
  }

  public void setSapNssfvoueRecStatus(SapNssfvoueRecStatus sapNssfvoueRecStatus) {
    this.sapNssfvoueRecStatus = sapNssfvoueRecStatus;
  }
  
}
