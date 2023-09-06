package com.example.jdnc_library.feature.book.controller;

import com.example.jdnc_library.feature.book.DTO.AdminRequest;
import com.example.jdnc_library.feature.book.DTO.BookNumberRequest;
import com.example.jdnc_library.feature.book.DTO.CountDTO;
import com.example.jdnc_library.feature.book.DTO.CreateCollectionDTO;
import com.example.jdnc_library.feature.book.service.BookService;
import com.example.jdnc_library.feature.book.service.BorrowService;
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
    private final BorrowService borrowService;

    /**
     * 책 고유번호 추가
     * @param createCollectionDTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCollection(
            @RequestBody CreateCollectionDTO createCollectionDTO){
        bookService.addBookNumber(createCollectionDTO.getBookNumber(),createCollectionDTO.getBookId());
    }

    /**
     * 책 번호 삭제 (사용불가)
     * @param id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCollection(
            @PathVariable @Positive Long id){
        bookService.deleteCollection(id);
    }

    @GetMapping("/notchecked")
    @ResponseStatus(HttpStatus.OK)
    public CountDTO getNotCheckedCount(){
        return borrowService.getNotChecked();
    }

    @GetMapping("/returned")
    @ResponseStatus(HttpStatus.OK)
    public CountDTO getReturnedCount(){
        return borrowService.getReturned();
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
     * @param bookNumberRequest
     */
    @PutMapping("/lost")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void lostBook(
            @RequestBody BookNumberRequest bookNumberRequest){
        bookService.lostBook(bookNumberRequest.getBookNumber());
    }
}
