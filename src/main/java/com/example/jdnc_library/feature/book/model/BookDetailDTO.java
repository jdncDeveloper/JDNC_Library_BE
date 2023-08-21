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
public class BookDetailDTO {

    private long id;

    private String title;

    private String image;

    private String content;

    private String author;

    private String publisher;

    private boolean available;

    public static BookDetailDTO of(BookInfo bookInfo, boolean available) {
        return new BookDetailDTO(bookInfo.getId(), bookInfo.getTitle(), bookInfo.getImage(), bookInfo.getContent(), bookInfo.getAuthor(), bookInfo.getPublisher(), available);
    }


    public static BookDetailDTO of (CollectionInfo  collectionInfo){
        BookDetailDTO bookDTO = new BookDetailDTO();
        bookDTO.setId(collectionInfo.getBookInfo().getId());
        bookDTO.setTitle(collectionInfo.getBookInfo().getTitle());
        bookDTO.setImage(collectionInfo.getBookInfo().getImage());
        bookDTO.setContent(collectionInfo.getBookInfo().getContent());
        bookDTO.setAuthor(collectionInfo.getBookInfo().getAuthor());
        bookDTO.setPublisher(collectionInfo.getBookInfo().getPublisher());
        bookDTO.setAvailable(collectionInfo.isAvailable());
        return bookDTO;
    }
}
