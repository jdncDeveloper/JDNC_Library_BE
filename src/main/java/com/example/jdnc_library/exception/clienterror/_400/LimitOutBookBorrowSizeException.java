package com.example.jdnc_library.exception.clienterror._400;

public class LimitOutBookBorrowSizeException extends BadRequestException{

    public LimitOutBookBorrowSizeException(Long memberId) {
        super(String.format("id:%d는 대출 한도에 도달 했습니다. 책을 빌릴수 없습니다", memberId));
    }

}
