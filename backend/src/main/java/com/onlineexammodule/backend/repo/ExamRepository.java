package com.onlineexammodule.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlineexammodule.backend.model.Exam;

public interface ExamRepository extends JpaRepository<Exam, Long> {

}
