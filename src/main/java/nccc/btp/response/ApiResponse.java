package nccc.btp.response;

public class ApiResponse<T> {
  private int code;
  private String status;
  private String MSG;
  private T data;

  public ApiResponse() {}

  public ApiResponse(int code, String MSG, T data) {
    this.code = code;
    this.MSG = MSG;
    this.data = data;
  }

  public ApiResponse(String status, String MSG) {
    this.status = status;
    this.MSG = MSG;
  }

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(200, "成功", data);
  }

  public static <T> ApiResponse<T> error(String MSG) {
    return new ApiResponse<>(500, MSG, null);
  }

  public static <T> ApiResponse<T> ok(String MSG) {
    return new ApiResponse<>("Ok", MSG);
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }


  public String getMSG() {
    return MSG;
  }

  public void setMSG(String mSG) {
    MSG = mSG;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

}

