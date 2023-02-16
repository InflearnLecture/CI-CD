package com.develonity.common.security.config;

import com.develonity.common.jwt.AdminAuthFilter;
import com.develonity.common.jwt.JwtUtil;
import com.develonity.common.jwt.UserAuthFilter;
import com.develonity.common.security.exceptionHandler.CustomAccessDeniedHandler;
import com.develonity.common.security.exceptionHandler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

  private final JwtUtil jwtUtil;


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
        .requestMatchers(PathRequest.toH2Console())
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }


  @Bean
  protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.httpBasic().disable()
        .csrf().disable()
        .formLogin().disable();

    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션이 필요하면 생성하도록 셋팅

    http.authorizeHttpRequests()
        .antMatchers("/api/register").permitAll()
        .antMatchers("/api/login").permitAll()
        .antMatchers("/api/admins/login").permitAll()
        .antMatchers("/api/reissue").permitAll()
//        .antMatchers("/api/users/signout").permitAll()
//        .antMatchers("/api/users/**").hasRole("CUSTOMER")
//        .antMatchers("/api/sellers/**").hasRole("SELLER")
        .anyRequest().authenticated()
        .and()
        .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        .and()
        .addFilterBefore(new UserAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new AdminAuthFilter(jwtUtil), UserAuthFilter.class);

    return http.build();
  }
}
