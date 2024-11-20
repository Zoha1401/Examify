package com.onlineexammodule.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlineexammodule.backend.model.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long>{
    
}
