package com.example.jdnc_library.feature.book.repository;

import static com.example.jdnc_library.domain.book.model.QBookInfo.bookInfo;
import static com.example.jdnc_library.domain.book.model.QCollectionInfo.collectionInfo;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.example.jdnc_library.domain.book.model.BookInfo;
import com.example.jdnc_library.feature.book.DTO.BookListDTO;
import com.example.jdnc_library.util.Querydsl4ExpressionUtil;
import com.example.jdnc_library.util.Querydsl4RepositorySupport;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class BookInfoQueryRepository extends Querydsl4RepositorySupport {

    public BookInfoQueryRepository() {
        super(BookInfo.class);
    }

    public Page<BookListDTO> getBookListDTOs(String title, Pageable pageable) {
        JPAQuery<BookListDTO> jpaQuery = select(
            Projections.constructor(
                BookListDTO.class,
                bookInfo.id,
                bookInfo.title,
                bookInfo.image,
                bookInfo.author,
                bookInfo.publisher,
                collectionInfo.id.count().gt(0L)
            ))
            .from(bookInfo)
            .leftJoin(collectionInfo).on(bookInfo.id.eq(collectionInfo.bookInfo.id)
                .and(collectionInfo.available.isTrue())
            )
            .where(Querydsl4ExpressionUtil.contains(bookInfo.title, title))
            .groupBy(collectionInfo.bookInfo.id);

        return applyPagination(pageable, jpaQuery);
    }
}
