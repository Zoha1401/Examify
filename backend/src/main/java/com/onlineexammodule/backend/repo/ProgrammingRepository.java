package com.onlineexammodule.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlineexammodule.backend.model.ProgrammingQuestion;

public interface ProgrammingRepository extends JpaRepository<ProgrammingQuestion, Long> {

}
