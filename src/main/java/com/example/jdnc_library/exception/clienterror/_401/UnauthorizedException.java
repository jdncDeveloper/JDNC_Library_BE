package com.example.jdnc_library.exception.clienterror._401;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {}

    public UnauthorizedException(String message) {
        super(message);
    }
}
