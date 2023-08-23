package com.example.jdnc_library.feature.book.controller;

import com.example.jdnc_library.domain.ResponseData;
import com.example.jdnc_library.feature.book.DTO.BookDetailDTO;
import com.example.jdnc_library.feature.book.DTO.BookListDTO;
import com.example.jdnc_library.feature.book.service.BookService;
import io.micrometer.common.util.StringUtils;
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

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<List<BookListDTO>> getBookList(
            @PageableDefault Pageable pageable,
            @RequestParam(value = "title", required = false) String title) {

        List<BookListDTO> bookList;
        if (StringUtils.isEmpty(title)) {
            bookList = bookService.searchAllBooks(pageable);
        } else {
            bookList = bookService.searchBooksByTitle(title, pageable);
        }

        return new ResponseData<>(bookList);

    }

    @GetMapping("/detail")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<BookDetailDTO> getBookDetail(
            @RequestParam(value = "id") long id){
        BookDetailDTO bookDetailDTO = bookService.getBookById(id);

        return new ResponseData<>(bookDetailDTO);
    }

    @GetMapping("/group")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<List<BookListDTO>> getBookGroupList(
            @RequestParam(value = "group") String group,
            @RequestParam(value = "title", required = false) String title,
            @PageableDefault Pageable pageable){

        List<BookListDTO> bookList;
        if(title.isEmpty()){
            bookList = bookService.searchBooksByGroup(group, pageable);
        }
        else {
            bookList = bookService.searchBooksByGroupAndTitle(group, title, pageable);
        }

        return new ResponseData<>(bookList);
    }


}
