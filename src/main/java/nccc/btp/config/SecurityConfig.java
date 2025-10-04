package nccc.btp.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

  @Autowired
  private HeaderPreAuthFilter headerPreAuthFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors().configurationSource(corsConfigurationSource()).and().csrf()
        .ignoringAntMatchers("/**").and()
        // 加入 Header SSO 的 Pre-Auth Filter
        .addFilterBefore(headerPreAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
        .antMatchers("/assets/**", "/403Forbidden.html", "/amlupload/api/remittance").permitAll()
        .anyRequest().authenticated().and()
        // 拋 401/403 時都跳到 403Forbidden.html
        .exceptionHandling()
        .authenticationEntryPoint(
            (req, res, authEx) -> res.sendRedirect(req.getContextPath() + "/403Forbidden.html"))
        .accessDeniedHandler(
            (req, res, deniedEx) -> res.sendRedirect(req.getContextPath() + "/403Forbidden.html"))
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
        .headers().httpStrictTransportSecurity().includeSubDomains(true).maxAgeInSeconds(31536000)
        .and().contentSecurityPolicy("script-src 'self' 'unsafe-inline'").and().xssProtection()
        .block(true).and().contentTypeOptions().and().frameOptions().sameOrigin();

    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    return manager;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(
        Arrays.asList("https://portal-sit.nccc.com.tw", "http://localhost:5173"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
