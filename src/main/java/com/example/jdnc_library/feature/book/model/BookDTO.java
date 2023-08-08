package com.example.jdnc_library.feature.book.model;

import com.example.jdnc_library.domain.book.model.Book;
import com.example.jdnc_library.domain.book.model.CollectionInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private Long id;

    private String title;

    private String image;

    private String content;

    private String author;

    private String publisher;

    public static BookDTO of(Book book) {
        return new BookDTO(book.getId(), book.getTitle(), book.getImage(), book.getContent(), book.getAuthor(), book.getPublisher());
    }


    public static BookDTO of (CollectionInfo  collectionInfo){
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle(collectionInfo.getBook().getTitle());
        bookDTO.setImage(collectionInfo.getBook().getImage());
        bookDTO.setContent(collectionInfo.getBook().getContent());
        bookDTO.setAuthor(collectionInfo.getBook().getAuthor());
        bookDTO.setPublisher(collectionInfo.getBook().getPublisher());
        return bookDTO;
    }
}
