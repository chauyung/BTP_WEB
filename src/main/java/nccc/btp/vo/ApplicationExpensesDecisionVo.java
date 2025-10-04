package nccc.btp.vo;

import java.util.ArrayList;
import java.util.List;

public class ApplicationExpensesDecisionVo extends DecisionVo
{
    private BpmExMVo bpmExMVo;

    public ApplicationExpensesDecisionVo()
    {
        this.setBpmExMVo(new BpmExMVo());
    }

    public BpmExMVo getBpmExMVo() {
        return bpmExMVo;
    }

    public void setBpmExMVo(BpmExMVo bpmExMVo) {
        this.bpmExMVo = bpmExMVo;
    }
}
