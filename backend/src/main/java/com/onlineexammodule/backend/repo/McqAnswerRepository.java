package com.onlineexammodule.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlineexammodule.backend.model.McqAnswer;

@Repository
public interface McqAnswerRepository extends JpaRepository<McqAnswer, Long> {
    
}
