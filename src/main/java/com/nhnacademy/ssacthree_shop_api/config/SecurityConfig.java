package com.nhnacademy.ssacthree_shop_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // TODO : 우선은 모든 요청에 대해 인증되지 않아도 접근할 수 있도록 허용해놨음. 나중에 고쳐야 함.
        http.authorizeHttpRequests(authorizeRequests ->
            authorizeRequests.requestMatchers("/api/**").permitAll());

        http.csrf().disable();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
