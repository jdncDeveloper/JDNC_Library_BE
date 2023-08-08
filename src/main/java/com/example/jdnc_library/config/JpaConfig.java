package com.example.jdnc_library.config;

import com.example.jdnc_library.domain.member.model.Member;
import com.example.jdnc_library.security.model.PrincipalDetails;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    @Bean
    public AuditorAware<Member> auditorProvider() {

        // 익명 클래스를 이용
        return new AuditorAware<Member>() {
            @Override
            public Optional<Member> getCurrentAuditor() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null) return Optional.empty();
                if (!(authentication.getPrincipal() instanceof PrincipalDetails principalDetails)) return Optional.empty();
                return Optional.of(principalDetails.getMember());
            }
        };
    }

}
