package com.example.jdnc_library.feature.member.model;

import com.example.jdnc_library.domain.member.model.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
    private String mbNumber;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

}
