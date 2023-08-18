package com.example.jdnc_library.domain.book.repository;

import com.example.jdnc_library.domain.book.model.BookInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookInfo, Integer> {

    Page<BookInfo> findAllByTitleContaining(String title, Pageable pageable);

    Optional<BookInfo> findById(Long id);

    Optional<BookInfo> findByTitleAndAuthorAndPublisher(String title, String author, String publisher);
}