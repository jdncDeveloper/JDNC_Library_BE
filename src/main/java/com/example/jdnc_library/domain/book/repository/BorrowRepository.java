package com.example.jdnc_library.domain.book.repository;

import com.example.jdnc_library.domain.book.model.BorrowInfo;
import com.example.jdnc_library.domain.member.model.Member;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<BorrowInfo, Integer> {

    Optional<BorrowInfo> findById(long id);
    Optional<BorrowInfo> findByCollectionInfo_BookNumber(long bookNumber);
    Optional<BorrowInfo> findByCollectionInfo_BookNumberAndReturnDateIsNull(long bookNumber);
    Page<BorrowInfo> findByAdminCheckIsFalse(Pageable pageable);

    Page<BorrowInfo> findAllByCreatedBy(Member member, Pageable pageable);

    Page<BorrowInfo> findAllByCreatedByAndReturnDateIsNull(Member member, Pageable pageable);

    Page<BorrowInfo> findByCreatedAtBetweenOrAdminCheckIsFalse(LocalDateTime startOfMonth, LocalDateTime endOfMonth, Pageable pageable);

    List<BorrowInfo> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @EntityGraph(attributePaths = {"createdBy", "collectionInfo.bookInfo"})
    List<BorrowInfo> findAllByAdminCheckIsFalse();
}
