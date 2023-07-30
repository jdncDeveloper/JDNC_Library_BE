package com.example.jdnc_library.exception.clienterror._400;

public class BadRequestException extends RuntimeException{

    public BadRequestException() {}

    public BadRequestException(String message) {
        super(message);
    }
}
