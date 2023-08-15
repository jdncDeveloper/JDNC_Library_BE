package com.example.jdnc_library.domain.book.repository;

import com.example.jdnc_library.domain.book.model.CollectionInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<CollectionInfo, Integer> {

    Optional<CollectionInfo> findByBookNumber(Long bookNumber);

    Page<CollectionInfo> findAllByBookInfo_TitleContaining(String title, Pageable pageable);
}
