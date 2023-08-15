package com.example.jdnc_library.config;

import com.example.jdnc_library.domain.member.repository.MemberRepository;
import com.example.jdnc_library.security.filter.JwtAuthenticationFilter;
import com.example.jdnc_library.security.filter.JwtAuthorizationFilter;
import com.example.jdnc_library.security.service.LmsCrawlerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final CorsFilter corsFilter;

    private final ObjectMapper objectMapper;

    private final MemberRepository memberRepository;

    private final String access;

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final LmsCrawlerService lmsCrawlerService;

    public SecurityConfig (
        CorsFilter corsFilter,
        @Value("${jwt.access}") String access,
        MemberRepository memberRepository,
        ObjectMapper objectMapper,
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder,
        LmsCrawlerService lmsCrawlerService){
        this.memberRepository = memberRepository;
        this.corsFilter = corsFilter;
        this.objectMapper = objectMapper;
        this.access = access;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.lmsCrawlerService = lmsCrawlerService;
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
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests((registry)-> registry.anyRequest().permitAll());

        http.httpBasic(AbstractHttpConfigurer::disable);

        http.formLogin(AbstractHttpConfigurer::disable);

        http.apply(new CustomFilterConfigure());

        return http.build();
    }

    public class CustomFilterConfigure extends AbstractHttpConfigurer<CustomFilterConfigure, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(
                AuthenticationManager.class);

            http.addFilter(corsFilter);
            http.addFilter(new JwtAuthenticationFilter(authenticationManager, objectMapper, access, lmsCrawlerService));
            http.addFilter(new JwtAuthorizationFilter(authenticationManager, access, memberRepository));
        }
    }
}
