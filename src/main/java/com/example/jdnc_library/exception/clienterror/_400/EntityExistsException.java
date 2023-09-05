package com.example.jdnc_library.exception.clienterror._400;

import lombok.Getter;

@Getter
public class EntityExistsException extends BadRequestException{

    private Object key;

    public EntityExistsException() {}

    public EntityExistsException(String key, Class domain) {
        super(String.format("key: %s, entity:%s is Exist", key, domain.getName()));
        this.key = key;
    }

    public EntityExistsException(Long key, Class domain) {
        super(String.format("key: %d, entity:%s is Exist", key, domain.getName()));
        this.key = key;
    }
}
