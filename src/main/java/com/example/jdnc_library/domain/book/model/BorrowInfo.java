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

    @OneToOne
    private CollectionInfo collectionInfo;

    @Column
    private LocalDateTime returnDate;

    public BorrowInfo(CollectionInfo collectionInfo){
        this.collectionInfo = collectionInfo;
    }

    public BorrowInfo(CollectionInfo collectionInfo, LocalDateTime returnDate){
        this.collectionInfo = collectionInfo;
        this.returnDate = returnDate;
    }

    public void returnBook(LocalDateTime returnDate){
        this.returnDate = returnDate;
    }

}
