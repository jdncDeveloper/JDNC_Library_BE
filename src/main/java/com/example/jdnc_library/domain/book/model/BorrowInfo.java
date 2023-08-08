package com.example.jdnc_library.domain.book.model;

import com.example.jdnc_library.domain.WriterEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowInfo extends WriterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long borrowerId;

    @OneToOne
    private CollectionInfo collectionInfo;

    @Column
    private LocalDateTime returnDate;

    public BorrowInfo(Long borrowerId, CollectionInfo collectionInfo){
        this.borrowerId = borrowerId;
        this.collectionInfo = collectionInfo;
    }

    public BorrowInfo(Long borrowerId, CollectionInfo collectionInfo, LocalDateTime returnDate){
        this.borrowerId = borrowerId;
        this.collectionInfo = collectionInfo;
        this.returnDate = returnDate;
    }

    public void returnBook(LocalDateTime returnDate){
        this.returnDate = returnDate;
    }

}
