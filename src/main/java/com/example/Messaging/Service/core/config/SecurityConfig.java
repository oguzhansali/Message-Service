package com.example.Messaging.Service.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/users/register", "/api/users/login").permitAll()  // Bu iki endpoint açık
                .requestMatchers("/api/users/**").permitAll()  // /api/users altındaki diğer tüm endpointler korumalı
                .requestMatchers("/api/users/block").permitAll()  // /api/users altındaki diğer tüm endpointler korumalı
                .requestMatchers("/api/messages/**").permitAll()
                .requestMatchers("/api/messages/*/sender").permitAll()
                .requestMatchers("/api/messages/*/receiver").permitAll()
                .requestMatchers("/api/messages/*/delete").permitAll()
                .requestMatchers("/api/messages/all").permitAll()
                .requestMatchers("/api/users/*/delete").permitAll()
                .requestMatchers("/api/blocks/block").permitAll()
                .requestMatchers("/api/blocks/*/blocked-users").permitAll()
                .requestMatchers("/api/activity-logs/*").permitAll()

                .and()
                .httpBasic();  // Basit HTTP Basic auth kullan

        return http.build();
    }
}
