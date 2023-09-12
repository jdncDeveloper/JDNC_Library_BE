package com.example.jdnc_library.domain.book.repository;

import com.example.jdnc_library.domain.book.model.BookInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookInfo, Integer> {

    Page<BookInfo> findAllByTitleContaining(String title, Pageable pageable);

    Page<BookInfo> findAll(Pageable pageable);

    Optional<BookInfo> findById(Long id);

    Optional<BookInfo> findByTitleAndAuthorAndPublisher(String title, String author, String publisher);

    Page<BookInfo> findByBookGroupAndTitleContaining(String group, String title, Pageable pageable);

    Page<BookInfo> findByBookGroup(String group, Pageable pageable);

    @EntityGraph(attributePaths = {"collectionInfos"})
    Optional<BookInfo> findWithBookCollectionById(Long id);

    Optional<BookInfo> findByTitle(String title);

    boolean existsByTitle(String title);
}
