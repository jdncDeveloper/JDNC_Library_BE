package com.example.jdnc_library.domain.book.repository;

import com.example.jdnc_library.domain.book.model.CollectionInfo;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<CollectionInfo, Long> {

    Optional<CollectionInfo> findByBookNumber(Long bookNumber);

    Optional<CollectionInfo> findById(Long id);

    @EntityGraph(attributePaths = {"bookInfo"})
    List<CollectionInfo> findAll();

    int countByAvailableTrue();

    int countByLostIsFalse();
}
