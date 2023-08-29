package com.example.jdnc_library.feature.book.controller;

import com.example.jdnc_library.domain.ResponseData;
import com.example.jdnc_library.feature.book.DTO.BookRequest;
import com.example.jdnc_library.feature.book.DTO.BorrowListDTO;
import com.example.jdnc_library.feature.book.service.BookService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Secured({"ROLE_ADMIN","ROLE_BOOKKEEPER"})
@RequiredArgsConstructor
@RequestMapping("/admin/book")
public class AdminBookController {

    private final BookService bookService;

    /**
     * 책 정보 추가
     * @param bookRequest
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBook(@RequestBody BookRequest bookRequest){
        bookService.saveBook(bookRequest);
    }

    /**
     * 책 정보 업데이트
     * @param id
     * @param bookRequest
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBook(
            @PathVariable @Positive Long id,
            @RequestBody BookRequest bookRequest){
        bookService.updateBook(id, bookRequest);
    }


    /**
     * 월간 대출 기록
     * @param year
     * @param month
     * @param pageable
     * @return
     */
    @GetMapping("/monthly")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<List<BorrowListDTO>> monthlyBorrow(
            @RequestParam(value = "year") @Positive int year,
            @RequestParam(value = "month") @Positive int month,
            @PageableDefault Pageable pageable){
        return new ResponseData<>(bookService.searchBooksBorrowedInMonth(year, month, pageable));
    }

    /**
     * 미반납인 책 리스트
     * @param pageable
     * @return
     */
    @GetMapping("/overdue")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<List<BorrowListDTO>> overdue(
            @PageableDefault Pageable pageable){
        return new ResponseData<>(bookService.searchNotReturnBooks(pageable));
    }


}
