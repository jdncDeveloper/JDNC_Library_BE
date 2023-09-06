package com.example.jdnc_library.feature.book.repository;

import static com.example.jdnc_library.domain.book.model.QBookInfo.bookInfo;
import static com.example.jdnc_library.domain.book.model.QBorrowInfo.borrowInfo;
import static com.example.jdnc_library.domain.book.model.QCollectionInfo.collectionInfo;

import com.example.jdnc_library.domain.book.model.CollectionInfo;
import com.example.jdnc_library.feature.book.DTO.CollectionDetailDTO;
import com.example.jdnc_library.util.Querydsl4RepositorySupport;
import com.querydsl.core.types.Projections;
import org.springframework.stereotype.Repository;

@Repository
public class CollectionInfoQueryRepository extends Querydsl4RepositorySupport {

    public CollectionInfoQueryRepository() {
        super(CollectionInfo.class);
    }

    public CollectionDetailDTO getCollectionDetailByBookNumber(Long bookNumber) {
        return select(Projections.constructor(
            CollectionDetailDTO.class,
            collectionInfo.id,
            collectionInfo.bookNumber,
            bookInfo.title,
            bookInfo.image,
            bookInfo.content,
            bookInfo.author,
            bookInfo.publisher,
            collectionInfo.available
        )).from(collectionInfo)
            .join(collectionInfo.bookInfo, bookInfo)
            .where(collectionInfo.bookNumber.eq(bookNumber)
                .and(collectionInfo.deletedAt.isNull())
                .and(bookInfo.deletedAt.isNull()))
            .fetchOne();
    }
}
