package com.onlineexammodule.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlineexammodule.backend.model.TestCase;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long>{
    
}
