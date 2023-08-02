package com.example.jdnc_library.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LmsLoginInfo {

    private String userNumber;

    private String password;

    @Override
    public String toString() {
        return String.format("{mb_id: %s}");
    }
}
