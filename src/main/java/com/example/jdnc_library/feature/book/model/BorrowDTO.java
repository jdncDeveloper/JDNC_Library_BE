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
public class BorrowDTO {

    private Long borrowId;

    private String borrowerName;

    private LocalDateTime borrowDate;

    private String image;

    private String title;

    private String author;

    private String publisher;

    private LocalDateTime returnDate;

    //TODO: 대출가능여부 boolean type만들기


    public static BorrowDTO of(BorrowInfo borrowInfo) {
        BorrowDTO borrowDTO = new BorrowDTO();
        borrowDTO.setBorrowId(borrowInfo.getId());
        borrowDTO.setBorrowerName(borrowInfo.getCreatedBy().getName());
        borrowDTO.setBorrowDate(borrowInfo.getCreatedAt());
        borrowDTO.setImage(borrowInfo.getCollectionInfo().getBookInfo().getImage());
        borrowDTO.setTitle(borrowInfo.getCollectionInfo().getBookInfo().getTitle());
        borrowDTO.setAuthor(borrowInfo.getCollectionInfo().getBookInfo().getAuthor());
        borrowDTO.setPublisher(borrowInfo.getCollectionInfo().getBookInfo().getPublisher());
        return borrowDTO;
    }
}
