package com.example.jdnc_library.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LmsLoginInfo {

    private String username;

    private String password;

    @Override
    public String toString() {
        return "{mb_id: %s}";
    }
}
