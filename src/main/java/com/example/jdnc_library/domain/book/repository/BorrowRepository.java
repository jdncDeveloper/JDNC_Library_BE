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

    @EntityGraph(attributePaths = {"createdBy", "collectionInfo.bookInfo"})
    Optional<BorrowInfo> findById(long id);

    List<BorrowInfo> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    int countByAdminCheckIsFalse();

    int countByAdminCheckIsFalseAndReturnDateIsNotNull();
}
