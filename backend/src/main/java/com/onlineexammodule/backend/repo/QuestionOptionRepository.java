package com.onlineexammodule.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlineexammodule.backend.model.QuestionOption;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {
    
}
