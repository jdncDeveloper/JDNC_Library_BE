package com.example.jdnc_library.domain.book.repository;

import com.example.jdnc_library.domain.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findAllByTitleContaining(String title);

    Optional<Book> findById(Long id);
}
