package com.example.jdnc_library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class AuthenticationManagerConfig {

    @Bean
    public AuthenticationManager authenticationManager (HttpSecurity httpSecurity) {
        return httpSecurity.getSharedObject(AuthenticationManager.class);
    }

}
