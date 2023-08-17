package com.example.jdnc_library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter () {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        //내 서버가 응답을 할때 json을 자바스크립트에서 처리
        config.setAllowCredentials(true);

        //모든 ip 응답 허용
//        config.addAllowedOrigin("*"); // setAllowCredentials랑 같이 쓰지 못함
        config.addAllowedOriginPattern("*");

        //모든 header 응답 허용
        config.addAllowedHeader("*");

        //모든 요청 메소드 허용
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**",config);
        return new CorsFilter(source);
    }


}
