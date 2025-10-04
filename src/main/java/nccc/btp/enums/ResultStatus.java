package nccc.btp.enums;

public enum ResultStatus {

  SUCCESS(0, "Success"), 
  PAGE_FORBIDDEN(403, "PAGE_FORBIDDEN"), 
  OPERATION_FORBIDDEN(403, "OPERATION_FORBIDDEN"), 
  NOT_LOGIN(401,"Not logged in"), 
  SERVER_ERROR(500, "Server error");

  private final int code;
  private final String message;

  ResultStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

}
