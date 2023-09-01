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

    @ManyToOne(fetch = FetchType.LAZY)
    private CollectionInfo collectionInfo;

    @Column
    private boolean adminCheck;

    @Column
    private LocalDateTime returnDate;

    @Column
    private Integer state;

    public BorrowInfo(CollectionInfo collectionInfo){
        this.collectionInfo = collectionInfo;
        this.adminCheck = false;
        this.returnDate = null;
    }

    public void returnBook(LocalDateTime returnDate, int state){
        this.returnDate = returnDate;
        this.state = state;
    }

    public void updateAdminCheck(boolean newAdminCheck) {
        this.adminCheck = newAdminCheck;
    }

}
