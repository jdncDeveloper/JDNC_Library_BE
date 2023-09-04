package com.example.jdnc_library.feature.book.controller;

import com.example.jdnc_library.feature.book.DTO.AdminRequest;
import com.example.jdnc_library.feature.book.DTO.BookNumberRequest;
import com.example.jdnc_library.feature.book.DTO.CreateCollectionDTO;
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
     * @param createCollectionDTO
     */
    //TODO: 프론트엔드 알림
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCollection(
            @RequestBody CreateCollectionDTO createCollectionDTO){
        bookService.addBookNumber(createCollectionDTO.getBookNumber(),createCollectionDTO.getBookId());
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
     * @param bookNumber
     */
    @PutMapping("/lost")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void lostBook(
            @RequestBody BookNumberRequest bookNumberRequest){
        bookService.lostBook(bookNumberRequest.getBookNumber());
    }
}
