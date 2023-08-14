package com.example.jdnc_library.feature.book.controller;

import com.example.jdnc_library.domain.ResponseData;
import com.example.jdnc_library.feature.book.model.BookDTO;
import com.example.jdnc_library.feature.book.model.BorrowDTO;
import com.example.jdnc_library.feature.book.service.BookService;
import com.example.jdnc_library.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/borrow")
public class BorrowController {
    /*@AuthenticationPrincipal PrincipalDetails principalDetails
    principalDetails.getMember() == 로그인한 사용자의 멤버엔티티
     */

    private final BookService bookService;

    /**
     * QR코드를 이용한 도서 대출 페이지 요청
     * @param bookNumber
     * @return
     */
    @GetMapping("/qrbook")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    private ResponseData<BookDTO> qrBook(
            @RequestParam(value = "bookNumber") Long bookNumber){
        return new ResponseData<>(bookService.qrBook(bookNumber));
    }

    /**
     * 도서 대출 요청
     * @param bookNumber
     *
     */
    @GetMapping("/borrowbook")
    @ResponseStatus(HttpStatus.CREATED)
    private void borrowBook(
            @RequestParam(value = "bookNumber") Long bookNumber){
        bookService.borrowBook(bookNumber);
    }

    /**
     * QR코드를 이용한 도서 반납 리스트 요청
     * @return List<BorrowDTO>
     */
    @GetMapping("/returnlist")
    @ResponseStatus(HttpStatus.OK)
    private ResponseData<List<BorrowDTO>> returnList(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault Pageable pageable){
        return new ResponseData<>(bookService.returnBookList(principalDetails.getMember(), pageable));
    }

    /**
     * 도서 반납 요청
     * @param bookNumber
     */
    @GetMapping("/return")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void returnBook(
            @RequestParam(value = "bookNumber") Long bookNumber){
        bookService.returnBook(bookNumber);
    }
}
