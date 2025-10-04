package nccc.btp.vo;

import java.util.ArrayList;
import java.util.List;
import nccc.btp.entity.BpmBtM;
import nccc.btp.entity.BpmBtMD1;


// 因應特殊會計人員修改完跑簽核 額外拉一個出來
public class TravelExpensesAccountingDecisionVo extends DecisionVo {

  private BpmBtM bpmBtM;

  private List<BpmBtMD1> bpmBtMD1List;

  public TravelExpensesAccountingDecisionVo() {
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
