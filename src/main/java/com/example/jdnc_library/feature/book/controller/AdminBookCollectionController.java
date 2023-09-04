package com.example.jdnc_library.feature.book.controller;

import com.example.jdnc_library.feature.book.DTO.AdminRequest;
import com.example.jdnc_library.feature.book.service.BookService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@Secured({"ROLE_ADMIN","ROLE_BOOKKEEPER"})
@RequiredArgsConstructor
@RequestMapping("/admin/book/collection")
public class AdminBookCollectionController {

    private final BookService bookService;

    /**
     * 책 고유번호 추가
     * @param bookNumber
     * @param bookId
     */
    //TODO: 프론트엔드 알림
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCollection(
        @RequestParam(value = "bookNumber") @Positive long bookNumber,
        @RequestParam(value = "bookId") @Positive long bookId){
        bookService.addBookNumber(bookNumber,bookId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCollection(
            @PathVariable @Positive Long id){
        bookService.deleteCollection(id);
    }

    /**
     * 관리자의 반납확인
     * test용
     * @param adminRequest
     */
    //TODO: 프론트엔드 알림, 멘토링 질문
    @PutMapping("/return")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adminCheck(
        @RequestBody AdminRequest adminRequest){
        bookService.adminCheck(adminRequest);
    }

    /**
     * 소실 처리
     * @param adminRequest
     */
    @PutMapping("/lost")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    //TODO: 프론트엔드 알림, 멘토링 질문
    public void lostBook(
        @RequestBody AdminRequest adminRequest){
        bookService.lostBook(adminRequest);
    }
}
