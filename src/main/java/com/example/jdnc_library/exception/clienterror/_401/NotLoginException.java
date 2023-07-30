package com.example.jdnc_library.exception.clienterror._401;

import com.example.jdnc_library.exception.clienterror._401.UnauthorizedException;

public class NotLoginException extends UnauthorizedException {
    public NotLoginException() {}

    public NotLoginException(String message) {
        super(message);
    }
}
