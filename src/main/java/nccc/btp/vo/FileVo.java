package nccc.btp.vo;

public class FileVo {

  private String fileName;
  
  private String base64;

  public FileVo() {}

  public FileVo(String fileName,String base64) {
    this.fileName = fileName;
    this.base64 = base64;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getBase64() {
    return base64;
  }

  public void setBase64(String base64) {
    this.base64 = base64;
  }

}
