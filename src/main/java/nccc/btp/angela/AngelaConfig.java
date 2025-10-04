package nccc.btp.angela;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AngelaConfig {
  @Bean
  public AngelaApiUtil angelaApiUtil(AngelaHsmProperties props)
      throws KeyManagementException, NoSuchAlgorithmException, Exception {
    return new AngelaApiUtil(props.getIp(), props.getSsoId(), props.getKeyName());
  }
}
