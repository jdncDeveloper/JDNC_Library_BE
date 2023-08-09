package com.example.jdnc_library.feature.book.model;

import com.example.jdnc_library.domain.book.model.BookInfo;
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

    public static BookDTO of(BookInfo bookInfo) {
        return new BookDTO(bookInfo.getId(), bookInfo.getTitle(), bookInfo.getImage(), bookInfo.getContent(), bookInfo.getAuthor(), bookInfo.getPublisher());
    }


    public static BookDTO of (CollectionInfo  collectionInfo){
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle(collectionInfo.getBookInfo().getTitle());
        bookDTO.setImage(collectionInfo.getBookInfo().getImage());
        bookDTO.setContent(collectionInfo.getBookInfo().getContent());
        bookDTO.setAuthor(collectionInfo.getBookInfo().getAuthor());
        bookDTO.setPublisher(collectionInfo.getBookInfo().getPublisher());
        return bookDTO;
    }
}
