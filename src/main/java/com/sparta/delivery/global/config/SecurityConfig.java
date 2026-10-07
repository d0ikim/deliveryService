package com.sparta.delivery.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))   // "세션안씀" 선언
                .authorizeHttpRequests(auth -> auth
                        // 이 URL들은 인증없이 통과
                        .requestMatchers("/error").permitAll()  // 예외발생시 403으로 묻히는문제 막기위함
                        .requestMatchers("/api/users", "/api/auth/login").permitAll()
                        .requestMatchers("/api/menus/**").permitAll() // TODO : 3단계에서 세분화
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}