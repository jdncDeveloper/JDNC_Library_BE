package com.example.jdnc_library.feature.book.DTO;

import com.example.jdnc_library.domain.book.model.CollectionInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowDetailDTO {

    private long id;

    private long bookNumber;

    private String title;

    private String image;

    private String content;

    private String author;

    private String publisher;

    private boolean available;

    public static BorrowDetailDTO of(CollectionInfo collectionInfo){
        BorrowDetailDTO borrowDetailDTO = new BorrowDetailDTO();
        borrowDetailDTO.setId(collectionInfo.getId());
        borrowDetailDTO.setBookNumber(collectionInfo.getBookNumber());
        borrowDetailDTO.setTitle(collectionInfo.getBookInfo().getTitle());
        borrowDetailDTO.setImage(collectionInfo.getBookInfo().getImage());
        borrowDetailDTO.setContent(collectionInfo.getBookInfo().getContent());
        borrowDetailDTO.setAuthor(collectionInfo.getBookInfo().getAuthor());
        borrowDetailDTO.setPublisher(collectionInfo.getBookInfo().getPublisher());
        borrowDetailDTO.setAvailable(collectionInfo.isAvailable());

        return borrowDetailDTO;
    }
}
