package com.example.jdnc_library.domain.tutorial.repository;

import com.example.jdnc_library.domain.tutorial.model.Tutorial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {

    Page<Tutorial> findAll(Pageable pageable);

}
