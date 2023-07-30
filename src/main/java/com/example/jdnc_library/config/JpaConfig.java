package com.example.jdnc_library.config;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {

        // 익명 클래스를 이용
        return new AuditorAware<Long>() {
            @Override
            public Optional<Long> getCurrentAuditor() {
                return Optional.empty();
            }
        };
    }

}
