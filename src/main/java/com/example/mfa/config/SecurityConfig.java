package com.example.mfa.config;

import com.example.mfa.service.CustomOneTimePasswordService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authorization.EnableMultiFactorAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 安全配置
 */
@Configuration
@EnableWebSecurity
@EnableMultiFactorAuthentication(authorities = { // 启用多因素认证-全局
        FactorGrantedAuthority.PASSWORD_AUTHORITY,
        FactorGrantedAuthority.OTT_AUTHORITY
})
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/h2-console/**", "/login-ott", "/ott-submit", "/ott/sent").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // 用户名密码认证
//                .formLogin(Customizer.withDefaults()) // 默认配置
                .formLogin(form -> form // 自定义登录配置
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                // OTT认证
//                .oneTimeTokenLogin(Customizer.withDefaults()) // 默认配置
                .oneTimeTokenLogin(ottLogin -> ottLogin // 自定义配置
                        .loginPage("/login-ott")
                        .loginProcessingUrl("/login-ott")
                        .failureUrl("/login-ott?error=true")
                        .defaultSubmitPageUrl("/ott-submit")
                        .showDefaultSubmitPage(false))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)); // 支持H2控制台
        return http.build();
    }

    @Bean
    public CustomOttSuccessHandler oneTimeTokenSuccessHandler() {
        return new CustomOttSuccessHandler();
    }

    @Bean
    public OneTimeTokenService oneTimeTokenService() {
        return new CustomOneTimePasswordService();
    }
}