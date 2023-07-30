package com.example.jdnc_library.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class WebMvcTestConfig {

    @Bean
    public ObjectMapper objectMapper () {
        return new ObjectMapper();
    }
}
