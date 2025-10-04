package nccc.btp.ftp;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import nccc.btp.angela.AngelaApiUtil;
import nccc.btp.angela.AngelaHsmProperties;
import nccc.btp.config.ApplicationProperties;

public class FtpsUtil {

  protected static Logger LOG = LoggerFactory.getLogger(FtpsUtil.class);

  FtpProperties.Server serverInfo;

  @Autowired
  AngelaHsmProperties angelaHsmProperties;

  @Autowired
  ApplicationProperties applicationProperties;

  List<String> ftpIpList = new LinkedList<String>();

  public FtpsUtil(FtpProperties.Server serverInfo) {
    this.serverInfo = serverInfo;
  }

  private void connectAndLogin(FTPSClient client) throws IOException {
    client.connect(serverInfo.getServer(), serverInfo.getPort());
    if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
      throw new IOException("FTP 連線失敗: " + client.getReplyCode());
    }
    String pwd = serverInfo.getPassword();
    if (!applicationProperties.isLocalMode()) {
      try {
        pwd = new AngelaApiUtil(angelaHsmProperties.getIp(), angelaHsmProperties.getSsoId(),
            angelaHsmProperties.getKeyName()).doNcccStrDecrypt(pwd);
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
        e.printStackTrace();
      }
    }
    if (!client.login(serverInfo.getUser(), pwd)) {
      throw new IOException("FTP 登錄失敗: " + serverInfo.getUser());
    }
    client.execPBSZ(0);
    client.execPROT("P");
    client.setFileType(FTP.BINARY_FILE_TYPE);
  }

  private void safeDisconnect(FTPSClient client) {
    if (client != null && client.isConnected()) {
      try {
        client.logout();
      } catch (Exception ignored) {
      }
      try {
        client.disconnect();
      } catch (Exception ignored) {
      }
    }
  }

  public boolean changeFolder(String pathName) throws Exception {
    FTPSClient client = new FTPSClient(false);
    try {
      connectAndLogin(client);
      client.enterLocalPassiveMode();
      if (client.changeWorkingDirectory(pathName)) {
        LOG.info(String.format("changeFolder sucessful! [%s]", pathName));
        return true;
      } else {
        LOG.info(String.format("changeFolder fail! [%s]", pathName));
        return false;
      }
    } catch (IOException e) {
      // EventLogService.sendLog(serverInfo.getServer(), EventType.FTP_CONN_FAIL,
      // serverInfo.getServer() + ":" + serverInfo.getPort());
      LOG.error("change ftp folder error.", e);
      return false;
    }
  }


  public InputStream readFileStream(String remotePath) throws Exception {
    FTPSClient client = new FTPSClient(false);
    try {
      connectAndLogin(client);
      client.enterLocalPassiveMode();

      FTPFile[] files = client.listFiles(remotePath);
      if (files == null || files.length == 0) {
        throw new IOException("檔案不存在: " + remotePath);
      }

      InputStream is = client.retrieveFileStream(remotePath);
      if (is == null) {
        client.abort();
        throw new IOException("無法開啟檔案: " + client.getReplyString());
      }
      LOG.info("成功取得遠端檔案: {}", remotePath);

      return new FilterInputStream(is) {
        @Override
        public void close() throws IOException {
          try {
            super.close();
          } finally {
            safeDisconnect(client);
          }
        }
      };

    } catch (IOException e) {
      safeDisconnect(client);
      throw e;
    }
  }

  public boolean uploadFile(String localPath, String remoteName) throws IOException {
    try (InputStream fis = new FileInputStream(localPath)) {
      FTPSClient client = new FTPSClient(false);
      try {
        connectAndLogin(client);
        client.enterLocalPassiveMode();
        return client.storeFile(remoteName, fis);
      } finally {
        safeDisconnect(client);
      }
    }
  }

  public boolean uploadFile(String remoteName, byte[] fileContent) throws IOException {
    try (ByteArrayInputStream bais = new ByteArrayInputStream(fileContent)) {
      FTPSClient client = new FTPSClient(false);
      try {
        connectAndLogin(client);
        client.enterLocalPassiveMode();
        client.setFileType(FTP.BINARY_FILE_TYPE);
        return client.storeFile(remoteName, bais);
      } finally {
        safeDisconnect(client);
      }
    }
  }

  /**
   * List files in remote directory
   */
  public FTPFile[] listFiles(String path) throws IOException {
    FTPSClient client = new FTPSClient(false);
    try {
      connectAndLogin(client);
      client.enterLocalPassiveMode();
      client.configure(new FTPClientConfig("org.apache.commons.net.ftp.parser.UnixFTPEntryParser"));
      return client.listFiles(path);
    } finally {
      safeDisconnect(client);
    }
  }

  /**
   * Delete remote file
   */
  public boolean deleteFile(String remoteName) throws IOException {
    FTPSClient client = new FTPSClient(false);
    try {
      connectAndLogin(client);
      client.enterLocalPassiveMode();
      return client.deleteFile(remoteName);
    } finally {
      safeDisconnect(client);
    }
  }

}
