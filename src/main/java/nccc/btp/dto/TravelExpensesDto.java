package nccc.btp.dto;

import java.util.ArrayList;
import java.util.List;
import nccc.btp.entity.BpmBtM;
import nccc.btp.entity.BpmBtMD1;

public class TravelExpensesDto {

  private BpmBtM bpmBtM;

  private List<BpmBtMD1> bpmBtMD1List;

  public TravelExpensesDto() {
    this.setBpmBtMD1List(new ArrayList<>());
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

}
