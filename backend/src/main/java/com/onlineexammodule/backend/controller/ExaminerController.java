package com.onlineexammodule.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineexammodule.backend.model.Examiner;
import com.onlineexammodule.backend.service.ExaminerService;

//import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
@CrossOrigin
public class ExaminerController {

    @Autowired
    private ExaminerService service;
    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);  //Can specify strength
    
    @PostMapping("/signin")
    public ResponseEntity<?> signInExaminer(@RequestBody Examiner examiner) {

        try {
        examiner.setPassword(encoder.encode(examiner.getPassword()));
        Examiner newExaminer = service.signInExaminer(examiner);
        return new ResponseEntity<>(newExaminer, HttpStatus.CREATED);
       
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
     @PostMapping("/examinerlogin")
     public String login(@RequestBody Examiner examiner) {
        return service.verify(examiner);
     }
     
    
    


    // @PostMapping("/signin")
    // public ResponseEntity<?> signInExaminer(@RequestBody Examiner examiner) {
    //     try {
    //         Examiner newExaminer = service.signInExaminer(examiner);
    //         return new ResponseEntity<>(newExaminer, HttpStatus.CREATED);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }
}
