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
public class CollectionDetailDTO {

    private long id;

    private long bookNumber;

    private String title;

    private String image;

    private String content;

    private String author;

    private String publisher;

    private boolean available;

    public static CollectionDetailDTO of(CollectionInfo collectionInfo){
        CollectionDetailDTO collectionDetailDTO = new CollectionDetailDTO();
        collectionDetailDTO.setId(collectionInfo.getId());
        collectionDetailDTO.setBookNumber(collectionInfo.getBookNumber());
        collectionDetailDTO.setTitle(collectionInfo.getBookInfo().getTitle());
        collectionDetailDTO.setImage(collectionInfo.getBookInfo().getImage());
        collectionDetailDTO.setContent(collectionInfo.getBookInfo().getContent());
        collectionDetailDTO.setAuthor(collectionInfo.getBookInfo().getAuthor());
        collectionDetailDTO.setPublisher(collectionInfo.getBookInfo().getPublisher());
        collectionDetailDTO.setAvailable(collectionInfo.isAvailable());

        return collectionDetailDTO;
    }
}
