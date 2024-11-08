package com.onlineexammodule.backend.repo;

import java.util.List;

// import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlineexammodule.backend.model.Examinee;
import com.onlineexammodule.backend.model.Examiner;



@Repository
public interface ExamineeRepository extends JpaRepository<Examinee, Long> {

    Examinee findByEmail(String email);

}
