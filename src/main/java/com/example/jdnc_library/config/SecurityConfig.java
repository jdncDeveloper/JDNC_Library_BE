package com.example.jdnc_library.config;

import com.example.jdnc_library.domain.member.repository.MemberRepository;
import com.example.jdnc_library.security.filter.JwtAuthenticationFilter;
import com.example.jdnc_library.security.filter.JwtAuthorizationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final CorsFilter corsFilter;

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig (
        CorsFilter corsFilter,
        JwtAuthenticationFilter jwtAuthenticationFilter,
        JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.corsFilter = corsFilter;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;

    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                .ignoring().requestMatchers
                (new AntPathRequestMatcher("/h2-console/**"),
                    new AntPathRequestMatcher("/favicon.ico"));
    }

    @Bean
    protected SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        //우리가 만든 필터는 시큐리티 필터보다 항상 늦게 실행되는데
        //시큐리티 필터보다 먼저 실행되게 하고싶으면 addFilterBefore 같은걸로 securityFilter 보다 먼저해야함
        http.csrf(AbstractHttpConfigurer::disable);

        http.addFilter(jwtAuthorizationFilter);

        http.addFilter(jwtAuthorizationFilter);

        http.authorizeHttpRequests((registry)-> registry.anyRequest().permitAll());

        http.httpBasic(AbstractHttpConfigurer::disable);

        http.formLogin(AbstractHttpConfigurer::disable);

        http.addFilter(corsFilter);

        return http.build();
    }
}
