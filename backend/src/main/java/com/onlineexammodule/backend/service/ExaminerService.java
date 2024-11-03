package com.onlineexammodule.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlineexammodule.backend.model.Examiner;
import com.onlineexammodule.backend.repo.ExaminerRepo;

@Service
public class ExaminerService {
    
    @Autowired
    private ExaminerRepo repo;

    public Examiner signInExaminer(Examiner examiner) {
        
        return repo.save(examiner);

    }

}
