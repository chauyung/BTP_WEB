package nccc.btp.rfc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *  application.yaml 中 sap.jco.*
 */
@Component
@ConfigurationProperties(prefix = "sap.jco")
public class SapJcoProperties {

    private String clientAshost;

    private String clientSysnr;

    private String clientClient;

    private String clientUser;

    private String clientPasswd;

    private String clientSysid;

    private String clientLang;

    private String poolCapacity;

    private String connectionIdleTimeout;

    private String clientMaxPoolWaitTime;

    // Getter & Setter
    public String getClientAshost() {
        return clientAshost;
    }

    public void setClientAshost(String clientAshost) {
        this.clientAshost = clientAshost;
    }

    public String getClientSysnr() {
        return clientSysnr;
    }

    public void setClientSysnr(String clientSysnr) {
        this.clientSysnr = clientSysnr;
    }

    public String getClientClient() {
        return clientClient;
    }

    public void setClientClient(String clientClient) {
        this.clientClient = clientClient;
    }

    public String getClientUser() {
        return clientUser;
    }

    public void setClientUser(String clientUser) {
        this.clientUser = clientUser;
    }

    public String getClientPasswd() {
        return clientPasswd;
    }

    public void setClientPasswd(String clientPasswd) {
        this.clientPasswd = clientPasswd;
    }

    public String getClientSysid() {
        return clientSysid;
    }

    public void setClientSysid(String clientSysid) {
        this.clientSysid = clientSysid;
    }

    public String getClientLang() {
        return clientLang;
    }

    public void setClientLang(String clientLang) {
        this.clientLang = clientLang;
    }

    public String getPoolCapacity() {
        return poolCapacity;
    }

    public void setPoolCapacity(String poolCapacity) {
        this.poolCapacity = poolCapacity;
    }

    public String getConnectionIdleTimeout() {
        return connectionIdleTimeout;
    }

    public void setConnectionIdleTimeout(String connectionIdleTimeout) {
        this.connectionIdleTimeout = connectionIdleTimeout;
    }

    public String getClientMaxPoolWaitTime() {
        return clientMaxPoolWaitTime;
    }

    public void setClientMaxPoolWaitTime(String clientMaxPoolWaitTime) {
        this.clientMaxPoolWaitTime = clientMaxPoolWaitTime;
    }
}
