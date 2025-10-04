package nccc.btp.dto;

import java.util.ArrayList;
import java.util.List;
import nccc.btp.entity.NcccNssfvoueRecData;
import nccc.btp.entity.NcccNssfvoueRecHeader;

public class NcccNssfvoueRecDto {

  private NcccNssfvoueRecHeader ncccNssfvoueRecHeader;
  
  private List<NcccNssfvoueRecData> ncccNssfvoueRecDataList;

  public NcccNssfvoueRecDto() {
    this.ncccNssfvoueRecDataList = new ArrayList<>();
  }
  
  public NcccNssfvoueRecDto(NcccNssfvoueRecHeader ncccNssfvoueRecHeader) {
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
  
}
