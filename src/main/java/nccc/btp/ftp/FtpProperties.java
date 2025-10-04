package nccc.btp.ftp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ftp")
public class FtpProperties {

  private Server ISDFTP;
  private Server NCCFTP;
  private Server SAP2ASS;

  public Server getISDFTP() {
    return ISDFTP;
  }

  public void setISDFTP(Server ISDFTP) {
    this.ISDFTP = ISDFTP;
  }

  public Server getNCCFTP() {
    return NCCFTP;
  }

  public void setNCCFTP(Server NCCFTP) {
    this.NCCFTP = NCCFTP;
  }

  public Server getSAP2ASS() {
    return SAP2ASS;
  }

  public void setSAP2ASS(Server sAP2ASS) {
    SAP2ASS = sAP2ASS;
  }

  public static class Server {
    private String server;
    private int port;
    private String user;
    private String password;
    private String path;

    public String getServer() {
      return server;
    }

    public void setServer(String server) {
      this.server = server;
    }

    public int getPort() {
      return port;
    }

    public void setPort(int port) {
      this.port = port;
    }

    public String getUser() {
      return user;
    }

    public void setUser(String user) {
      this.user = user;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public String getPath() {
      return path;
    }

    public void setPath(String path) {
      this.path = path;
    }
  }
}
