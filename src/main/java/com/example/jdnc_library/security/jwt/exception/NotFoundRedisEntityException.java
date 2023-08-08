package com.example.jdnc_library.security.jwt.exception;

import lombok.Getter;

@Getter
public class NotFoundRedisEntityException extends CustomJwtException {

    private final String key;
    private final String expect;


    public NotFoundRedisEntityException(String key) {
        super(String.format("RedisEntityNotFound key:%s", key));
        this.key = key;
        this.expect = "";
    }

    public NotFoundRedisEntityException(String key, String expect) {
        super(String.format("RedisEntityNotFound key:%s, expect:%s", key, expect));
        this.key = key;
        this.expect = expect;
    }


}
