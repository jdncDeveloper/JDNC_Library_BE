package com.example.jdnc_library.domain.book.model;

import com.example.jdnc_library.domain.WriterEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "update collection_info set deleted_at = now() where id = ?")
@Where(clause = "deleted_at is null")
public class CollectionInfo extends WriterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private BookInfo bookInfo;

    @Column(unique = true, nullable = false)
    private Long bookNumber;

    @Column(nullable = false)
    private boolean available;

    @Column(nullable = false)
    private boolean lost;

    public CollectionInfo(BookInfo bookInfo, Long bookNumber) {
        this.bookInfo = bookInfo;
        this.bookNumber = bookNumber;
        this.available = true;
        this.lost = false;
    }

    public void updateAvailable(boolean availableChange) {
        this.available = availableChange;
    }

    public void lostBook(boolean lost) {
        this.lost = lost;
        this.available = !lost;
    }
}
