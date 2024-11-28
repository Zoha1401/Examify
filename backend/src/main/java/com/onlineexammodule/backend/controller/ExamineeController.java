package com.onlineexammodule.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineexammodule.backend.DTO.ExamineeLoginRequest;
import com.onlineexammodule.backend.model.Exam;
import com.onlineexammodule.backend.model.Examinee;
import com.onlineexammodule.backend.service.ExamineeService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;





@RestController
@RequestMapping("/api/examinee")
@CrossOrigin(origins = "http://localhost:3000")
public class ExamineeController {
     
    @Autowired
    private ExamineeService examineeService;

    @PostMapping("/login")
    public String LoginExaminee(@RequestBody ExamineeLoginRequest loginRequest) {
        
        System.out.println(loginRequest.getEmail());
        return examineeService.verify(loginRequest.getEmail(), loginRequest.getExaminerId());
       
    }

    @GetMapping("/getForExaminee")
    public String greet() {
        return "Examinee token validated hurrah";
    }

    @GetMapping("/getAllExams")
    public List<Exam> getAllExams(@RequestParam Long examineeId) {
        return examineeService.getAllexams(examineeId);
    }
    
   
    

    //AnswerMCQ

    //AnswerProgramming
    
    
}
