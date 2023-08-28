package com.example.jdnc_library.feature.book.DTO;

import com.example.jdnc_library.domain.book.model.BookGroup;
import com.example.jdnc_library.domain.book.model.BookInfo;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

    private String title;

    private String image;

    private String content;

    private String author;

    private String publisher;

    private String bookGroup;

    public BookInfo toEntity() {
        return new BookInfo(
                null,
                title,
                image,
                content,
                author,
                publisher,
                BookGroup.valueOf(bookGroup),
            null
        );
    }
}
