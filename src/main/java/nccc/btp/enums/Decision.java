package nccc.btp.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Decision {
  APPROVE("approve"), // 核准
  BACK("backToPrevious"), // 退回上一層
  RETURN("returnToInitiator"), // 退回申請人
  SUBMIT("submit"), //送審
  INVALID("invalid"),// 作廢
  REJECT("reject"), // 駁回
  END("end") // 結束流程
  ;

  private final String description;

  Decision(String description) {
    this.description = description;
  }

  @JsonValue
  public String getDescription() {
    return description;
  }

  public boolean isApprove() {
    return this == APPROVE;
  }

  public boolean isBack() {
    return this == BACK;
  }

  public boolean isReturn() {
    return this == RETURN;
  }

  public boolean isSubmit() {
    return this == SUBMIT;
  }

  public boolean isInvalid() {
    return this == INVALID;
  }
  
  public boolean isReject() {
    return this == REJECT;
  }
  
  public boolean isEnd() {
    return this == END;
  }
}
