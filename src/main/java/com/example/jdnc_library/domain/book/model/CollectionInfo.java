package com.example.jdnc_library.domain.book.model;

import com.example.jdnc_library.domain.WriterEntity;
import jakarta.annotation.Priority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionInfo extends WriterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private BookInfo bookInfo;

    @Column(unique = true, nullable = false)
    private Long bookNumber;

    public CollectionInfo(BookInfo bookInfo, Long bookNumber){
        this.bookInfo = bookInfo;
        this.bookNumber = bookNumber;
    }
}
