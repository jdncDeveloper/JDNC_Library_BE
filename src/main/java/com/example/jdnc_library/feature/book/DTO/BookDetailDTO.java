package com.example.jdnc_library.feature.book.DTO;

import com.example.jdnc_library.domain.book.model.BookGroup;
import com.example.jdnc_library.domain.book.model.BookInfo;
import com.example.jdnc_library.domain.book.model.CollectionInfo;
import java.util.Collections;
import java.util.List;
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

    private BookGroup bookGroup;

    private boolean available;

    private List<Long> bookNumbers;

    public static BookDetailDTO of(BookInfo bookInfo) {
        List<CollectionInfo> collectionInfos = bookInfo.getCollectionInfos();

        boolean flag = false;

        for (CollectionInfo collectionInfo : collectionInfos) {
            if (collectionInfo.isAvailable()) {
                flag = true;
                break;
            }
        }

        return new BookDetailDTO(
            bookInfo.getId(),
            bookInfo.getTitle(),
            bookInfo.getImage(),
            bookInfo.getContent(),
            bookInfo.getAuthor(),
            bookInfo.getPublisher(),
            bookInfo.getBookGroup(),
            flag,
            collectionInfos.stream().map((CollectionInfo::getId)).toList()
        );
    }


    public static BookDetailDTO of (CollectionInfo  collectionInfo){
        BookDetailDTO bookDTO = new BookDetailDTO();
        bookDTO.setId(collectionInfo.getBookInfo().getId());
        bookDTO.setTitle(collectionInfo.getBookInfo().getTitle());
        bookDTO.setImage(collectionInfo.getBookInfo().getImage());
        bookDTO.setContent(collectionInfo.getBookInfo().getContent());
        bookDTO.setAuthor(collectionInfo.getBookInfo().getAuthor());
        bookDTO.setPublisher(collectionInfo.getBookInfo().getPublisher());
        bookDTO.setBookGroup(collectionInfo.getBookInfo().getBookGroup());
        bookDTO.setAvailable(collectionInfo.isAvailable());
        return bookDTO;
    }
}
