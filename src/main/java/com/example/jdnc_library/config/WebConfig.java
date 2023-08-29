package com.example.jdnc_library.config;

import com.example.jdnc_library.converter.mvc.BookGroupMvcConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new BookGroupMvcConverter());
    }

    @Bean
    public ObjectMapper objectMapper () {
        return new ObjectMapper();
    }

}
