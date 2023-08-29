package com.example.jdnc_library.feature.book.controller;

import com.example.jdnc_library.domain.ResponseData;
import com.example.jdnc_library.domain.book.model.BookGroup;
import com.example.jdnc_library.feature.book.DTO.BookDetailDTO;
import com.example.jdnc_library.feature.book.DTO.BookListDTO;
import com.example.jdnc_library.feature.book.service.SearchService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final SearchService searchService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<List<BookListDTO>> getBookList(
            @PageableDefault Pageable pageable,
            @RequestParam(value = "group", required = false) BookGroup bookGroup,
            @RequestParam(value = "title", required = false) String title) {

        List <BookListDTO> bookList = searchService.getBookListDTOs(title, bookGroup ,pageable);
        return new ResponseData<>(bookList);

    }

    @GetMapping("/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<BookDetailDTO> getBookDetail(
            @PathVariable(value = "bookId") @Positive long id){
        BookDetailDTO bookDetailDTO = searchService.getBookById(id);

        return new ResponseData<>(bookDetailDTO);
    }
}
