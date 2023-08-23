package com.example.jdnc_library.feature.book.controller;

import com.example.jdnc_library.domain.ResponseData;
import com.example.jdnc_library.feature.book.DTO.BorrowDetailDTO;
import com.example.jdnc_library.feature.book.DTO.BorrowListDTO;
import com.example.jdnc_library.feature.book.service.BookService;
import com.example.jdnc_library.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/borrow")
public class BorrowController {
    /*@AuthenticationPrincipal PrincipalDetails principalDetails
    principalDetails.getMember() == 로그인한 사용자의 멤버엔티티
     */
    //TODO: @transactional 처리하기

    private final BookService bookService;

    /**
     * QR코드를 이용한 도서 대출 페이지 요청
     * @param bookNumber
     * @return
     */
    @GetMapping("/qrbook")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseData<BorrowDetailDTO> qrBook(
            @RequestParam(value = "bookNumber") long bookNumber){
        return new ResponseData<>(bookService.qrBook(bookNumber));
    }

    /**
     * 도서 대출 요청
     * @param bookNumber
     *
     */
    @GetMapping("/borrowbook")
    @ResponseStatus(HttpStatus.CREATED)
    public void borrowBook(
            @RequestParam(value = "bookNumber") long bookNumber){
        bookService.borrowBook(bookNumber);
    }

    /**
     * QR코드를 이용한 도서 반납 리스트 요청
     * @return List<BorrowDTO>
     */
    @GetMapping("/returnlist")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseData<List<BorrowListDTO>> returnList(
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
    public void returnBook(
            @RequestParam(value = "bookNumber") long bookNumber){
        bookService.returnBook(bookNumber);
    }
}
