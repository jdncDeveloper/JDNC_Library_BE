package com.example.jdnc_library.domain.member.model;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_BOOKKEEPER,
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
