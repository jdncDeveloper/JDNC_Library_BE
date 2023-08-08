package com.example.jdnc_library.domain.book.repository;

import com.example.jdnc_library.domain.book.model.Book;
import com.example.jdnc_library.domain.book.model.BorrowInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<BorrowInfo, Integer> {

    Optional<BorrowInfo> findByCollectionInfo_BookNumber(Long bookNumber);
    List<Book> findBooksByBorrowerId(@Param("borrowerId") Long borrowerId);

    List<BorrowInfo> findByReturnDateIsNull();

    List<BorrowInfo> findAllByCreatedBy(Long id);

    List<BorrowInfo> findAllByCreatedByAndReturnDateIsNull(Long id);

    List<BorrowInfo> findByReturnDateBetween(LocalDate startOfMonth, LocalDate endOfMonth);
}
