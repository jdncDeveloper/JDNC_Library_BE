package com.example.jdnc_library.feature.book.model;

import com.example.jdnc_library.domain.book.model.BorrowInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowListDTO {

    private Long borrowId;

    private String borrowerName;

    /**
     * 날짜 표기 JSON변환설정
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate borrowDate;

    private long bookNumber;

    private String image;

    private String title;

    private String content;

    private String author;

    private String publisher;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;


    @JsonProperty("borrowDate")
    public String getBorrowDate() {
        if (borrowDate != null) {
            return borrowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return null;
    }

    @JsonProperty("returnDate")
    public String getReturnDate() {
        if (returnDate != null) {
            return returnDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return null;
    }

    public static BorrowListDTO of(BorrowInfo borrowInfo) {
        BorrowListDTO borrowListDTO = new BorrowListDTO();
        borrowListDTO.setBorrowId(borrowInfo.getId());
        borrowListDTO.setBorrowerName(borrowInfo.getCreatedBy().getName());
        borrowListDTO.setBorrowDate(LocalDate.from(borrowInfo.getCreatedAt()));
        borrowListDTO.setBookNumber(borrowInfo.getCollectionInfo().getBookNumber());
        borrowListDTO.setImage(borrowInfo.getCollectionInfo().getBookInfo().getImage());
        borrowListDTO.setContent(borrowInfo.getCollectionInfo().getBookInfo().getContent());
        borrowListDTO.setTitle(borrowInfo.getCollectionInfo().getBookInfo().getTitle());
        borrowListDTO.setAuthor(borrowInfo.getCollectionInfo().getBookInfo().getAuthor());
        borrowListDTO.setPublisher(borrowInfo.getCollectionInfo().getBookInfo().getPublisher());
        return borrowListDTO;
    }
}
