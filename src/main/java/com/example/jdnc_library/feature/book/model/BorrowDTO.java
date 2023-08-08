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

    private String title;

    private LocalDateTime returnDate;

    //TODO: 저자 이미지 추가 필요 대출자능여부 boolean type만들기


    public static BorrowDTO of(BorrowInfo borrowInfo) {
        BorrowDTO borrowDTO = new BorrowDTO();
        borrowDTO.setBorrowId(borrowInfo.getId());
        borrowDTO.setBorrowerName(borrowInfo.getCreatedBy().toString());
        borrowDTO.setBorrowDate(borrowInfo.getCreatedAt());
        borrowDTO.setTitle(borrowInfo.getCollectionInfo().getBook().getTitle());
        return borrowDTO;
    }
}
