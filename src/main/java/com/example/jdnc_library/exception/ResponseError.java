package com.example.jdnc_library.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseError<T> {

    private T message;

}
