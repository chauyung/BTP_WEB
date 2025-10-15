package nccc.btp.config;

import java.util.Locale;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import nccc.btp.aplog.filter.ApLogFilter;
import nccc.btp.aplog.sql.ApLogDataSource;

/**
 * Database(EMS) 連線設置
 * ------------------------------------------------------
 * 修訂人員: ChauYung
 * 修訂日期: 2025-10-08
 */
@Configuration
public class DataSourceConfig implements WebMvcConfigurer {

  private final String locale;

  @Autowired
  public DataSourceConfig(@Value("${locale}") String locale) {
    this.locale = locale;
  }

  @Bean
  public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
    ObjectMapper objectMapper = builder.build();
    // 註冊支援 Java 8 日期時間
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
    return objectMapper;
  }

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:messages-en");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver localeResolver = new SessionLocaleResolver();
    localeResolver.setDefaultLocale(new Locale(locale));
    return localeResolver;
  }

  @Bean
  public LocalValidatorFactoryBean validator() {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource());
    return bean;
  }

  @Bean
  public MethodValidationPostProcessor methodValidationPostProcessor() {
    return new MethodValidationPostProcessor();
  }

  @Override
  public org.springframework.validation.Validator getValidator() {
    return validator();
  }

  /**
   * 註冊 AP Log Filter
   * 
   * @return
   */
  @Bean
  public FilterRegistrationBean registerApLogFilter() {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new ApLogFilter());
    registrationBean.addUrlPatterns("/*");
    registrationBean.setOrder(999);
    registrationBean.setEnabled(true);
    return registrationBean;
  }

  
  /**
   * 主要資料庫連線(BTP)
   * @return - BTP DataSource
   */
  @Bean
  @Qualifier("dataSourceBTP")
  @Primary // this will override the datasource autoconfiguration and use your own everywhere
  DataSource dataSource() {
	  return lookupDataSource("jdbc/btp_ds", "java:comp/env/jdbc/btp_ds");
  }

  /**
   * 次要資料庫連線(EMS)
   * @return - EMS DataSource
   */
  @Bean
  @Qualifier("dataSourceEMS")
  DataSource dataSourceEMS() {
	  return lookupDataSource("jdbc/ems_ds", "java:comp/env/jdbc/ems_ds");
  }
  
  /**
   * 使用 JNDI 取得 DataSource
   * @param wasJndiName - WAS JNDI Name
   * @param tomcatJndiName - Tomcat JNDI Name
   * @return - DataSource
   */
  private DataSource lookupDataSource(String wasJndiName, String tomcatJndiName) {
      try {
          Context ctx = new InitialContext();
          try {
              return new ApLogDataSource((DataSource) ctx.lookup(wasJndiName));
          } catch (NamingException e) {
              return new ApLogDataSource((DataSource) ctx.lookup(tomcatJndiName));
          }
      } catch (Exception e) {
          throw new IllegalStateException("Failed to lookup JNDI: " + wasJndiName, e);
      }
  }
}
