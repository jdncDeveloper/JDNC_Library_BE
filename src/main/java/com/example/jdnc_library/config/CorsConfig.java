package com.example.jdnc_library.config;

import java.util.Arrays;
import java.util.List;
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
//        config.addAllowedOrigin("*"); // setAllowCredentials를 쓰면 AllowedOrignsPatterns 를 써야함
        config.setAllowedOriginPatterns(List.of("*"));

        //헤더 추출 가능
        config.setExposedHeaders(List.of("Authorization", "Authorization-Refresh"));

        //특정헤더 응답허용
        config.setAllowedHeaders(Arrays.asList("Authorization","Authorization-Refresh", "Cache-Control", "Content-Type"));

        //모든 요청 메소드 허용
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        source.registerCorsConfiguration("/**",config);
        return new CorsFilter(source);
    }


}
