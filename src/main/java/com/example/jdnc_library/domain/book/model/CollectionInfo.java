package com.example.jdnc_library.domain.book.model;

import com.example.jdnc_library.domain.WriterEntity;
import jakarta.annotation.Priority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionInfo extends WriterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private BookInfo bookInfo;

    @Column(unique = true, nullable = false)
    private Long bookNumber;

    @Column(nullable = false)
    private boolean available;

    @Column(nullable = false)
    private boolean lost;

    public CollectionInfo(BookInfo bookInfo, Long bookNumber){
        this.bookInfo = bookInfo;
        this.bookNumber = bookNumber;
        this.available = true;
        this.lost = false;
    }

    public void updateAvailable(boolean availableChange){
        this.available = availableChange;
    }

    public void lostBook(boolean lost){
        this.lost = lost;
        this.available = !lost;
    }
}
