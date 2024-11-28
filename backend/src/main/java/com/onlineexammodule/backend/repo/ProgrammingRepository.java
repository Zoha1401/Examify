package com.onlineexammodule.backend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlineexammodule.backend.model.ProgrammingQuestion;

@Repository
public interface ProgrammingRepository extends JpaRepository<ProgrammingQuestion, Long> {
     
    List<ProgrammingQuestion> findAllByDifficulty(String difficulty);
}
