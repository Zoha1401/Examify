package com.onlineexammodule.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.onlineexammodule.backend.model.Exam;
import com.onlineexammodule.backend.model.Examinee;

// import com.onlineexammodule.backend.model.Examiner;
import com.onlineexammodule.backend.repo.ExamineeRepository;
// import org.springframework.security.core.AuthenticationException;



@Service
public class ExamineeService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ExamineeRepository examineeRepository;

    public String verify(String email)
    {
        Examinee examinee=examineeRepository.findByEmail(email);
        System.out.println("Finding examinee by email"+email);
        if (examinee == null) {
            System.out.println("Examinee not found for the provided email and examiner ID.");
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(email);
        
       
    }

    public List<Exam> getAllexams(Long examineeId) {

        //Get examinee and return exams.
        Examinee examinee=examineeRepository.findById(examineeId)
                          .orElseThrow(()-> new IllegalArgumentException("Examinee not found"));
        
        return examinee.getExams();
    }

    public Long getExamineeIdFromEmail(String examineeEmail) {
        Examinee existingExaminee=examineeRepository.findByEmail(examineeEmail);
        return existingExaminee.getExamineeId();
        
    }


}
    