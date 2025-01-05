package com.onlineexammodule.backend.repo;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlineexammodule.backend.model.McqQuestion;

@Repository
public interface McqRepository extends JpaRepository<McqQuestion, Long>{
    List<McqQuestion> findAllByCategory(String category);

    List<McqQuestion> findAllByCategoryAndDifficulty(String category, String difficulty);

    McqQuestion findByMcqQuestionText(String mcqQuestionText);

}
