package com.example.jdnc_library.feature.book.model;

import com.example.jdnc_library.domain.book.model.Book;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    private Long bookNumber;

    private String title;

    private String image;

    private String content;

    private String author;

    private String publisher;

    public Book toEntity() {
        return new Book(
                null,
                title,
                image,
                content,
                author,
                publisher
        );
    }
}
