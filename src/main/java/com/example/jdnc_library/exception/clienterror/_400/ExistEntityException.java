package com.example.jdnc_library.exception.clienterror._400;

import lombok.Getter;

@Getter
public class ExistEntityException extends BadRequestException{

    private Object key;

    public ExistEntityException() {}

    public ExistEntityException (String key, Class domain) {
        super(String.format("key: %s, entity:%s is Exist", key, domain.getName()));
        this.key = key;
    }

    public ExistEntityException (Long key, Class domain) {
        super(String.format("key: %d, entity:%s is Exist", key, domain.getName()));
        this.key = key;
    }
}
