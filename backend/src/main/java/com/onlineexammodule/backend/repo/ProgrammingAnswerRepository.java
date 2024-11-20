package com.onlineexammodule.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlineexammodule.backend.model.ProgrammingQuestionAnswer;

@Repository
public interface ProgrammingAnswerRepository extends JpaRepository<ProgrammingQuestionAnswer, Long> {
    
}
