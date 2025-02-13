package com.onlineexammodule.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlineexammodule.backend.model.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

}
