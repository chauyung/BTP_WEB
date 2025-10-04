package nccc.btp.vo;

import java.util.ArrayList;
import java.util.List;
import nccc.btp.entity.BpmBtM;
import nccc.btp.entity.BpmBtMD1;
import nccc.btp.entity.LevAgent;
import nccc.btp.entity.NcccCostcenterOrgMapping;
import nccc.btp.entity.NcccReceiptType;

public class TravelExpensesVo extends ProcessVo {

  private List<NcccReceiptType> ncccReceiptTypeList;

  private List<NcccCostcenterOrgMapping> ncccCostcenterOrgMappingList;

  private List<LevAgent> levAgentList;

  private BpmBtM bpmBtM;

  private List<BpmBtMD1> bpmBtMD1List;

  private List<FileVo> fileList;

  private String tripMemberName;

  private String payEmpNoName;

  public TravelExpensesVo() {
    this.setNcccCostcenterOrgMappingList(new ArrayList<>());
    this.setNcccReceiptTypeList(new ArrayList<>());
    this.setLevAgentList(new ArrayList<>());
    this.setBpmBtMD1List(new ArrayList<>());
    this.setFileList(new ArrayList<>());
  }

  public List<NcccCostcenterOrgMapping> getNcccCostcenterOrgMappingList() {
    return ncccCostcenterOrgMappingList;
  }

  public void setNcccCostcenterOrgMappingList(
      List<NcccCostcenterOrgMapping> ncccCostcenterOrgMappingList) {
    this.ncccCostcenterOrgMappingList = ncccCostcenterOrgMappingList;
  }

  public List<NcccReceiptType> getNcccReceiptTypeList() {
    return ncccReceiptTypeList;
  }

  public void setNcccReceiptTypeList(List<NcccReceiptType> ncccReceiptTypeList) {
    this.ncccReceiptTypeList = ncccReceiptTypeList;
  }

  public List<LevAgent> getLevAgentList() {
    return levAgentList;
  }

  public void setLevAgentList(List<LevAgent> levAgentList) {
    this.levAgentList = levAgentList;
  }

  public BpmBtM getBpmBtM() {
    return bpmBtM;
  }

  public void setBpmBtM(BpmBtM bpmBtM) {
    this.bpmBtM = bpmBtM;
  }

  public List<BpmBtMD1> getBpmBtMD1List() {
    return bpmBtMD1List;
  }

  public void setBpmBtMD1List(List<BpmBtMD1> bpmBtMD1List) {
    this.bpmBtMD1List = bpmBtMD1List;
  }

  public List<FileVo> getFileList() {
    return fileList;
  }

  public void setFileList(List<FileVo> fileList) {
    this.fileList = fileList;
  }

  public String getTripMemberName() {
    return tripMemberName;
  }

  public void setTripMemberName(String tripMemberName) {
    this.tripMemberName = tripMemberName;
  }

  public String getPayEmpNoName() {
    return payEmpNoName;
  }

  public void setPayEmpNoName(String payEmpNoName) {
    this.payEmpNoName = payEmpNoName;
  }

}
