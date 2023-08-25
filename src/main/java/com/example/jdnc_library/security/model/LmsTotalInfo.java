package com.example.jdnc_library.security.model;

import com.example.jdnc_library.domain.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LmsTotalInfo {
    private String mbNumber;

    private String encodedPassword;

    private String name;
    private String email;

    public static LmsTotalInfo of (Member member) {
        return new LmsTotalInfo (member.getUsername(), member.getPassword(), member.getName(), member.getEmail());
    }
}
