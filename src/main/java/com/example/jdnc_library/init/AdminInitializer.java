package com.example.jdnc_library.init;

import com.example.jdnc_library.domain.member.model.Member;
import com.example.jdnc_library.domain.member.model.Role;
import com.example.jdnc_library.domain.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final String initAdminUsername;
    private final String initAdminPassword;

    @Autowired
    public AdminInitializer (
        MemberRepository memberRepository,
        PasswordEncoder passwordEncoder,
        @Value("${init.admin.username}") String adminUsername,
        @Value("${init.admin.password}") String adminPassword) {
        this.memberRepository = memberRepository;
        this.initAdminUsername = adminUsername;
        this.initAdminPassword = adminPassword;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initAdmin () {
        List<Member> memberList = memberRepository.findAllByRole(Role.ROLE_ADMIN);
        if (!memberList.isEmpty()) return;

        Member member = new Member(
            null,
            initAdminUsername,
            passwordEncoder.encode(initAdminPassword),
            "관리자",
            null,
            null,
            Role.ROLE_ADMIN
        );

        memberRepository.save(member);
        memberRepository.flush();
    }

}
