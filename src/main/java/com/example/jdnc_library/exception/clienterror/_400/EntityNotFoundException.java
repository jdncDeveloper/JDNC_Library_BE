package com.example.jdnc_library.exception.clienterror._400;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends BadRequestException {

    private Object key;

    public EntityNotFoundException () {}

    public EntityNotFoundException (String key, Class domain) {
        super(String.format("key: %s, entity:%s not Found", key, domain.getName()));
        this.key = key;
    }

    public EntityNotFoundException (Long key, Class domain) {
        super(String.format("key: %d, entity:%s not Found", key, domain.getName()));
        this.key = key;
    }
}
