package com.example.jdnc_library.domain.book.model;

import com.example.jdnc_library.domain.WriterEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "update borrow_info set deleted_at = now() where id = ?")
@Where(clause = "deleted_at is null")
public class BorrowInfo extends WriterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CollectionInfo collectionInfo;

    @Column
    private boolean adminCheck;

    @Column
    private LocalDateTime returnDate;

    @Column
    private Integer floor;

    public BorrowInfo(CollectionInfo collectionInfo){
        this.collectionInfo = collectionInfo;
        this.adminCheck = false;
        this.returnDate = null;
        this.floor = null;
    }

    public void returnBook(LocalDateTime returnDate, Integer floor){
        this.returnDate = returnDate;
        this.floor = floor;
    }

    public void updateAdminCheck(boolean newAdminCheck) {
        this.adminCheck = newAdminCheck;
    }

}
