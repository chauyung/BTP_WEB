package nccc.btp.rfc;

import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.ext.Environment;
import nccc.btp.angela.AngelaApiUtil;
import nccc.btp.angela.AngelaHsmProperties;
import nccc.btp.config.ApplicationProperties;

/**
 * Spring 管理的 JCo 連線管理類別， 在建構子中完成 DestinationDataProvider 註冊， 提供 instance 方法取得 JCoDestination。
 */
@Component
public class RFCManager {
  private static final String DESTINATION_NAME = "SIT";
  private final Logger logger = LoggerFactory.getLogger(RFCManager.class);
  private volatile boolean initialized = false;
  private JCoDestination destination;

  @Autowired
  private ApplicationProperties applicationProperties;

  @Autowired
  private SapJcoProperties sapJcoProperties;

  @Autowired
  private AngelaHsmProperties angelaHsmProperties;

  public RFCManager() {}

  private void initDestination() {
    try {
      logger.info("Initializing SAP JCo Destination '{}'", DESTINATION_NAME);

      Properties connectProperties = new Properties();
      connectProperties.setProperty("jco.client.ashost", sapJcoProperties.getClientAshost());
      connectProperties.setProperty("jco.client.sysnr", sapJcoProperties.getClientSysnr());
      connectProperties.setProperty("jco.client.client", sapJcoProperties.getClientClient());
      connectProperties.setProperty("jco.client.user", sapJcoProperties.getClientUser());
      if (applicationProperties.isLocalMode()) {
        connectProperties.setProperty("jco.client.passwd", sapJcoProperties.getClientPasswd());
      } else {
        AngelaApiUtil angela = new AngelaApiUtil(angelaHsmProperties.getIp(),
            angelaHsmProperties.getSsoId(), angelaHsmProperties.getKeyName());
        connectProperties.setProperty("jco.client.passwd",
            angela.doNcccStrDecrypt(sapJcoProperties.getClientPasswd()));
      }
      connectProperties.setProperty("jco.client.sysid", sapJcoProperties.getClientSysid());
      connectProperties.setProperty("jco.client.lang", sapJcoProperties.getClientLang());
      connectProperties.setProperty("jco.pool_capacity", sapJcoProperties.getPoolCapacity());
      connectProperties.setProperty("jco.connection.idle.timeout",
          sapJcoProperties.getConnectionIdleTimeout());
      connectProperties.setProperty("jco.client.max_pool_wait_time",
          sapJcoProperties.getClientMaxPoolWaitTime());

      // 註冊 DestinationDataProvider
      RFCDestinationDataProvider provider = new RFCDestinationDataProvider();
      provider.addDestination(DESTINATION_NAME, connectProperties);
      Environment.registerDestinationDataProvider(provider);

      this.destination = JCoDestinationManager.getDestination(DESTINATION_NAME);
      logger.info("SAP JCo Destination '{}' initialized successfully", DESTINATION_NAME);
    } catch (Exception e) {
      logger.error("Error initializing SAP JCo Destination '{}'", DESTINATION_NAME, e);
      throw new RuntimeException("SAP JCo initialization failed", e);
    }
  }

  /**
   * 取得已初始化的 JCoDestination
   */
  public JCoDestination getDestination() {
    if (!initialized) {
      synchronized (this) {
        if (!initialized) {
          initDestination();
          initialized = true;
        }
      }
    }
    return destination;
  }
}
