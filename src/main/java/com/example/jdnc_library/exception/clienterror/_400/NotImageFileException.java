package com.example.jdnc_library.exception.clienterror._400;

public class NotImageFileException extends BadRequestException{

    public NotImageFileException () {
        super("이미지 파일이 아닙니다");
    }
}
