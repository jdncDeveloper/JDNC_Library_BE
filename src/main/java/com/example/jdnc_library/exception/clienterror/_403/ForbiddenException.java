package com.example.jdnc_library.exception.clienterror._403;

import lombok.Data;
import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {

    private String key;

    private Long id;

    private Class domain;

    public ForbiddenException (String key, Long id ,Class domain) {
        super(String.format("key: %s, entity:%s not Found", key, domain.getName()));
        this.key = key;
        this.id = id;
        this.domain = domain;
    }

    public ForbiddenException (Long key, Long id ,Class domain) {
        super(String.format("key: %s, entity:%s not Found", key, domain.getName()));
        this.key = String.valueOf(key);
        this.id = id;
        this.domain = domain;
    }
}
