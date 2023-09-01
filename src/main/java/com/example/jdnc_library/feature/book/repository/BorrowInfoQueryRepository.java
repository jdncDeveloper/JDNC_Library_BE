package com.example.jdnc_library.feature.book.repository;

import static com.example.jdnc_library.domain.book.model.QBorrowInfo.borrowInfo;
import static com.example.jdnc_library.domain.member.model.QMember.member;
import static com.example.jdnc_library.domain.book.model.QCollectionInfo.collectionInfo;
import static com.example.jdnc_library.domain.book.model.QBookInfo.bookInfo;

import com.example.jdnc_library.domain.book.model.BorrowInfo;
import com.example.jdnc_library.feature.book.DTO.BorrowListDTO;
import com.example.jdnc_library.util.Querydsl4RepositorySupport;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class BorrowInfoQueryRepository extends Querydsl4RepositorySupport {

    public BorrowInfoQueryRepository() {
        super(BorrowInfo.class);
    }

    private JPAQuery<BorrowListDTO> getNotYetReturnListQuery(Long memberId) {
        return select(
            Projections.constructor(
                BorrowListDTO.class,
                borrowInfo.id,
                member.name,
                borrowInfo.createdAt,
                collectionInfo.bookNumber,
                bookInfo.image,
                bookInfo.title,
                bookInfo.content,
                bookInfo.author,
                bookInfo.publisher,
                borrowInfo.returnDate)
        ).from(borrowInfo)
            .join(borrowInfo.collectionInfo, collectionInfo)
            .join(collectionInfo.bookInfo, bookInfo)
            .join(borrowInfo.createdBy, member)
            .where(member.id.eq(memberId)
                .and(borrowInfo.returnDate.isNull()));
    }

    public Page<BorrowListDTO> getNotYetReturnBookList(Long memberId, Pageable pageable) {
        JPAQuery<BorrowListDTO> query = getNotYetReturnListQuery(memberId);
        return applyPagination(pageable, query);
    }

    public int getNotYetReturnBookListSize(Long memberId) {
        return getNotYetReturnListQuery(memberId).fetch().size();
    }

    public Optional<BorrowInfo> getNonReturnBorrowInfo(Long bookNumber) {
        BorrowInfo borrowInfo1 = selectFrom(borrowInfo)
            .join(borrowInfo.collectionInfo, collectionInfo)
            .where(collectionInfo.bookNumber.eq(bookNumber)
                .and(borrowInfo.returnDate.isNull())).fetchJoin().fetchOne();

        return Optional.ofNullable(borrowInfo1);
    }

    public boolean isExistNonReturnBorrowInfo(Long bookNumber) {
        return getNonReturnBorrowInfo(bookNumber).isPresent();
    }
}
