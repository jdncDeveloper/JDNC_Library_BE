package com.example.jdnc_library.feature.book.controller;

import com.example.jdnc_library.feature.book.model.BookRequest;
import com.example.jdnc_library.feature.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/book")
public class BookController {

    private final BookService bookService;

    /**
     * 책 정보 추가
     * @param bookRequest
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createBook(@RequestBody BookRequest bookRequest){
        bookService.saveBook(bookRequest);
    }

    /**
     * 책 정보 업데이트
     * @param id
     * @param bookRequest
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBook(
            @RequestParam(value = "id") Long id,
            @RequestBody BookRequest bookRequest){
        bookService.updateBook(id, bookRequest);
    }

    /**
     * 책 고유번호 추가
     * @param bookNumber
     * @param id
     */
    @PostMapping("/addnumber")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCollection(
            @RequestParam(value = "bookNumber") Long bookNumber,
            @RequestParam(value = "id") Long id){
        bookService.addBookNumber(bookNumber,id);
    }


}
