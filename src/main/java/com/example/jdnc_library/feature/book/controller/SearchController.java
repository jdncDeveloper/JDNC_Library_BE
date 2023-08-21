package com.example.jdnc_library.feature.book.controller;

import com.example.jdnc_library.domain.ResponseData;
import com.example.jdnc_library.feature.book.model.BookDetailDTO;
import com.example.jdnc_library.feature.book.model.BookListDTO;
import com.example.jdnc_library.feature.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final BookService bookService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    private ResponseData<List<BookListDTO>> getBookList(
            @PageableDefault Pageable pageable,
            @RequestParam(value = "title", required = false) String title) {
        return new ResponseData<>(bookService.searchBooks(title, pageable));
    }

//    @GetMapping("/")
//    @ResponseStatus(HttpStatus.OK)
//    private ResponseData<>


}
