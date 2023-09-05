package com.example.jdnc_library.feature.book.repository;

import static com.example.jdnc_library.domain.book.model.QBorrowInfo.borrowInfo;
import static com.example.jdnc_library.domain.member.model.QMember.member;
import static com.example.jdnc_library.domain.book.model.QCollectionInfo.collectionInfo;
import static com.example.jdnc_library.domain.book.model.QBookInfo.bookInfo;

import com.example.jdnc_library.domain.book.model.BorrowInfo;
import com.example.jdnc_library.domain.book.model.QBookInfo;
import com.example.jdnc_library.domain.book.model.QBorrowInfo;
import com.example.jdnc_library.domain.book.model.QCollectionInfo;
import com.example.jdnc_library.domain.member.model.QMember;
import com.example.jdnc_library.feature.book.DTO.BorrowListDTO;
import com.example.jdnc_library.util.Querydsl4RepositorySupport;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class BorrowInfoQueryRepository extends Querydsl4RepositorySupport {

    public BorrowInfoQueryRepository() {
        super(BorrowInfo.class);
    }

    private ConstructorExpression<BorrowListDTO> getProjectionsBorrowList
        (QMember member1, QBorrowInfo borrowInfo1, QCollectionInfo collectionInfo1, QBookInfo bookInfo1) {

        return Projections.constructor(
            BorrowListDTO.class,
            borrowInfo1.id,
            member1.name,
            borrowInfo1.createdAt,
            collectionInfo1.bookNumber,
            bookInfo1.image,
            bookInfo1.title,
            bookInfo1.author,
            bookInfo1.publisher,
            borrowInfo1.returnDate,
            borrowInfo1.floor);
    }

    private JPAQuery<BorrowListDTO> getNotYetReturnListQuery(Long memberId) {
        return select(getProjectionsBorrowList(member, borrowInfo, collectionInfo, bookInfo)).from(borrowInfo)
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

    public Page<BorrowListDTO> getBorrowListDTOOfReturnInMonth(int year, int month, Pageable pageable) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int lastDay = yearMonth.lengthOfMonth();

        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = LocalDateTime.of(year, month, lastDay, 23, 59, 59);

        JPAQuery<BorrowListDTO> jpaQuery =
            select(getProjectionsBorrowList(member, borrowInfo, collectionInfo, bookInfo)).from(borrowInfo)
                .join(borrowInfo.createdBy, member)
                .join(borrowInfo.collectionInfo, collectionInfo)
                .join(collectionInfo.bookInfo, bookInfo)
                .where(borrowInfo.returnDate.between(startOfMonth, endOfMonth)
                    .or(borrowInfo.adminCheck.isFalse()));

        return applyPagination(pageable, jpaQuery);
    }

    public Page<BorrowListDTO> getBorrowListNonReturn(Pageable pageable) {
        JPAQuery<BorrowListDTO> jpaQuery =
            select(getProjectionsBorrowList(member, borrowInfo, collectionInfo, bookInfo)).from(borrowInfo)
                .join(borrowInfo.createdBy, member)
                .join(borrowInfo.collectionInfo, collectionInfo)
                .join(collectionInfo.bookInfo, bookInfo)
                .where(borrowInfo.adminCheck.isFalse());

        return applyPagination(pageable, jpaQuery);
    }
}
