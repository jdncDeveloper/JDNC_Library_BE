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
public class BookListDTO {

    private long id;

    private String title;

    private String image;

    private String author;

    private String publisher;

    private boolean available;

    public static BookListDTO of (BookInfo bookInfo, boolean available){
        return new BookListDTO(bookInfo.getId(), bookInfo.getTitle(), bookInfo.getImage(), bookInfo.getAuthor(), bookInfo.getPublisher(), available);
    }

    public static BookListDTO of (CollectionInfo collectionInfo){
        BookListDTO bookListDTO = new BookListDTO();
        bookListDTO.setId(collectionInfo.getId());
        bookListDTO.setTitle(collectionInfo.getBookInfo().getTitle());
        bookListDTO.setAuthor(collectionInfo.getBookInfo().getAuthor());
        bookListDTO.setAvailable(collectionInfo.isAvailable());
        return bookListDTO;
    }
}
