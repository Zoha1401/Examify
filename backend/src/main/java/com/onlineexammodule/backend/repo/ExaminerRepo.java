package com.onlineexammodule.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlineexammodule.backend.model.Examiner;

public interface ExaminerRepo extends JpaRepository<Examiner, Long>{

}
