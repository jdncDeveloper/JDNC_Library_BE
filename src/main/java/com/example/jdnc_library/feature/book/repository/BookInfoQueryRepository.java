package com.example.jdnc_library.feature.book.repository;

import static com.example.jdnc_library.domain.book.model.QBookInfo.bookInfo;
import static com.example.jdnc_library.domain.book.model.QBorrowInfo.borrowInfo;
import static com.example.jdnc_library.domain.book.model.QCollectionInfo.collectionInfo;

import com.example.jdnc_library.domain.book.model.BookGroup;
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

    public Page<BookListDTO> getBookListDTOs(String title, BookGroup bookGroup,Pageable pageable) {
        JPAQuery<BookListDTO> jpaQuery = select(
            Projections.constructor(
                BookListDTO.class,
                bookInfo.id,
                bookInfo.title,
                bookInfo.image,
                bookInfo.author,
                bookInfo.publisher,
                collectionInfo.available,
                bookInfo.bookGroup
            ))
            .from(bookInfo)
            .leftJoin(collectionInfo).on(bookInfo.id.eq(collectionInfo.bookInfo.id)
                .and(collectionInfo.available.isTrue())
            )
            .where(Querydsl4ExpressionUtil.contains(bookInfo.title, title)
                .and(collectionInfo.deletedAt.isNull())
                .and(bookInfo.deletedAt.isNull()))
            .groupBy(bookInfo.id);

        //collectionInfo.available = 이미 left join에서 필터링햇기때문에 해당 컬럼을 조회 하는 건 문제가 없다
        //group by - id로 그룹 지어줘서 bookInfo에는 문제가 없다.


        if (bookGroup != null) {
            jpaQuery.where(bookInfo.bookGroup.eq(bookGroup));
        }

        return applyPagination(pageable, jpaQuery);
    }

}
