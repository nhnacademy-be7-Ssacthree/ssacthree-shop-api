package com.nhnacademy.ssacthree_shop_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 모든 요청에 대해 인증되지 않아도 접근할 수 있도록 허용
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/**", "/h2-console/**").permitAll()
                                .anyRequest().authenticated() // 추가: 인증되지 않은 요청은 인증 필요
                )
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .headers(headers -> headers.frameOptions().disable()); // H2-console을 사용하기 위해 추가

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
