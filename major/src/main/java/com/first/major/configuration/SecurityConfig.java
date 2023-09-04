package com.first.major.configuration;

import com.first.major.configuration.auth.CustomUserDetailService;
import com.first.major.configuration.oauth.PrincipalOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity // 이거 꼭 쓰기 3.0 넘어오면서 추가
public class SecurityConfig {

  private final CustomUserDetailService userDetailService;
  private final PrincipalOAuth2UserService principalOAuth2UserService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.csrf().disable(); // 모르겠음
    http
            .authorizeHttpRequests(request -> request
                    .requestMatchers("/", "/shop/**", "/register", "/resources/**", "/static/**", "/images/**", "/productimages/**", "/css/**", "/js/**", "/login").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(login -> login
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .failureUrl("/login?error= true")
                    .defaultSuccessUrl("/")
                    .usernameParameter("email")
                    .passwordParameter("password")
            )
            .oauth2Login(login -> login
                    .loginPage("/login").userInfoEndpoint()
                    .userService(principalOAuth2UserService)
            )
            .logout(logout -> logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            );
    return http.build();
  }
}
