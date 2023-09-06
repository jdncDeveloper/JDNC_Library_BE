package com.example.jdnc_library.domain.book.model;

import com.example.jdnc_library.converter.jpa.BookGroupJpaConverter;
import com.example.jdnc_library.domain.WriterEntity;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "update book_info set deleted_at = now() where id = ?")
@Where(clause = "deleted_at is null")
public class BookInfo extends WriterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String image;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    @Convert(converter = BookGroupJpaConverter.class)
    private BookGroup bookGroup;

    @OneToMany(mappedBy = "bookInfo")
    private List<CollectionInfo> collectionInfos;

    public void update(String title, String image, String content, String author, String publisher, BookGroup bookGroup){
        this.title = title;
        this.image = image;
        this.content = content;
        this.author = author;
        this.publisher = publisher;
        this.bookGroup = bookGroup;
    }
}
