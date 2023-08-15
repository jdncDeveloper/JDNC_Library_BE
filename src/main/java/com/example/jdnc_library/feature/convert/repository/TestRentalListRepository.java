package com.example.jdnc_library.feature.convert.repository;

import com.example.jdnc_library.feature.convert.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRentalListRepository extends JpaRepository<TestEntity, Long> {

}
