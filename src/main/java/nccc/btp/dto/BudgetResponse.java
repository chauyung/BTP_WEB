package nccc.btp.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BudgetResponse {
    
    public Boolean success; // 操作是否成功

    public String msg; // 操作結果訊息

    public Object data; // 附加資料

    public BudgetResponse() {
        this.success = true;
        this.msg = "操作成功";
    }

    public BudgetResponse(Boolean success) {
        this.success = success;
        this.msg = success ? "操作成功" : "操作失敗";
    }

    public BudgetResponse(Boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public BudgetResponse(Boolean success, String msg, Object data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }
}
