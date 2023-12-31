package com.example.jdnc_library.domain.member.repository;

import com.example.jdnc_library.domain.member.model.Member;
import com.example.jdnc_library.domain.member.model.Role;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String mbNumber);

    List<Member> findAllByName(String username);
    List<Member> findAllByRole(Role role);

}
