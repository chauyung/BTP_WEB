package nccc.btp.angela;

import java.io.Serializable;

public class ResponseVo implements Serializable {


  private static final long serialVersionUID = 3665947567423209042L;
  /**
   * sample json { "result": 1, "data":
   * "3eada3ce701aea4c21f117e1e95b32b2acd0a01dd03e7e57b02d141f5f9c7808" }
   */
  private int result;
  private int errorCode;
  private String data;
  private String errorMessage;

  public ResponseVo() {}

  public int getResult() {
    return result;
  }

  public void setResult(int result) {
    this.result = result;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
