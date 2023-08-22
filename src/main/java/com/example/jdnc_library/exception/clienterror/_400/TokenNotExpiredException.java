package com.example.jdnc_library.exception.clienterror._400;

import com.auth0.jwt.exceptions.TokenExpiredException;

public class TokenNotExpiredException extends BadRequestException {

    public TokenNotExpiredException() {
        super("Access Token is Not Expire");
    }
}
