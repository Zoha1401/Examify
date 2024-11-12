package com.onlineexammodule.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlineexammodule.backend.model.McqQuestion;

public interface McqRepository extends JpaRepository<McqQuestion, Long>{
    
}
