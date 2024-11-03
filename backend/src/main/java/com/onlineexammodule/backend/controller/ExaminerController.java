package com.onlineexammodule.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineexammodule.backend.model.Examiner;
import com.onlineexammodule.backend.service.ExaminerService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api")
@CrossOrigin
public class ExaminerController {

    @Autowired
    private ExaminerService service;
     
    @PostMapping("/signin")
    public ResponseEntity<?> signInExaminer(@RequestBody Examiner examiner){
          
        try{
        Examiner new_examiner=service.signInExaminer(examiner);
        return new ResponseEntity<>(new_examiner, HttpStatus.CREATED);
        }
        catch(Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    
}
