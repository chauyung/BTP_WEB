/**
 * 
 */
package nccc.btp.ftp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FtpsConfig {

  private final FtpProperties ftpProperties;

  public FtpsConfig(FtpProperties ftpProperties) {
      this.ftpProperties = ftpProperties;
  }

  @Bean("nccFtpsUtil")
  public FtpsUtil nccFtpsUtil() {
      return new FtpsUtil(ftpProperties.getNCCFTP());
  }

  @Bean("isdFtpsUtil")
  public FtpsUtil isdFtpsUtil() {
      return new FtpsUtil(ftpProperties.getISDFTP());
  }
  
  @Bean("sap2assFtpsUtil")
  public FtpsUtil sap2assFtpsUtil() {
      return new FtpsUtil(ftpProperties.getSAP2ASS());
  }
  
}
