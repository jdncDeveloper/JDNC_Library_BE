package com.example.jdnc_library.feature.book.model;

import com.example.jdnc_library.domain.book.model.BorrowInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowListDTO {

    private Long borrowId;

    private String borrowerName;

    private LocalDateTime borrowDate;

    private String image;

    private String title;

    private String content;

    private String author;

    private String publisher;

    private LocalDateTime returnDate;



    public static BorrowListDTO of(BorrowInfo borrowInfo) {
        BorrowListDTO borrowListDTO = new BorrowListDTO();
        borrowListDTO.setBorrowId(borrowInfo.getId());
        borrowListDTO.setBorrowerName(borrowInfo.getCreatedBy().getName());
        borrowListDTO.setBorrowDate(borrowInfo.getCreatedAt());
        borrowListDTO.setImage(borrowInfo.getCollectionInfo().getBookInfo().getImage());
        borrowListDTO.setContent(borrowInfo.getCollectionInfo().getBookInfo().getContent());
        borrowListDTO.setTitle(borrowInfo.getCollectionInfo().getBookInfo().getTitle());
        borrowListDTO.setAuthor(borrowInfo.getCollectionInfo().getBookInfo().getAuthor());
        borrowListDTO.setPublisher(borrowInfo.getCollectionInfo().getBookInfo().getPublisher());
        return borrowListDTO;
    }
}
