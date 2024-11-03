package com.onlineexammodule.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlineexammodule.backend.model.Examiner;
import java.util.Optional;

@Repository
public interface ExaminerRepo extends JpaRepository<Examiner, Long> {
    Examiner findByEmail(String email);
}
