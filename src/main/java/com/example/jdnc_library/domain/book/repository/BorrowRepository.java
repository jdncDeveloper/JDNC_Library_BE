package com.example.jdnc_library.domain.book.repository;

import com.example.jdnc_library.domain.book.model.BorrowInfo;
import com.example.jdnc_library.domain.member.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<BorrowInfo, Integer> {

    Optional<BorrowInfo> findByCollectionInfo_BookNumber(long bookNumber);
    Optional<BorrowInfo> findByCollectionInfo_BookNumberAndReturnDateIsNull(long bookNumber);
    Page<BorrowInfo> findByReturnDateIsNull(Pageable pageable);

    Page<BorrowInfo> findAllByCreatedBy(Member member, Pageable pageable);

    Page<BorrowInfo> findAllByCreatedByAndReturnDateIsNull(Member member, Pageable pageable);

    Page<BorrowInfo> findByReturnDateBetween(LocalDate startOfMonth, LocalDate endOfMonth, Pageable pageable);
}
