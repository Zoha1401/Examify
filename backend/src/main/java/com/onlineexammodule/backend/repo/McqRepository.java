package com.onlineexammodule.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlineexammodule.backend.model.McqQuestion;

@Repository
public interface McqRepository extends JpaRepository<McqQuestion, Long>{
    
}
