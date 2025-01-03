package com.onlineexammodule.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlineexammodule.backend.DTO.ExamRequest;
import com.onlineexammodule.backend.model.Exam;
import com.onlineexammodule.backend.model.Examinee;
import com.onlineexammodule.backend.service.ExamService;
import com.onlineexammodule.backend.service.JWTService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/exam")
@CrossOrigin(origins = "http://localhost:3000")
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private JWTService jwtService;

     @PostMapping("/createExam")
     public Exam createExam(@RequestBody ExamRequest exam, HttpServletRequest request) {
        String token=request.getHeader("Authorization").substring(7);
        String examiner_email=jwtService.extractEmail(token);
        System.out.println("Examiner that wants to create an exam "+examiner_email);
        Exam new_exam=examService.createExam(exam, examiner_email);
        return new_exam;
     }

     @PostMapping("/updateExam")
     public Exam updateExam(@RequestBody ExamRequest exam, @RequestParam Long examId) {
         return examService.updateExam(exam, examId);
     }
     
     @DeleteMapping("/deleteExam")
     public String deleteExam(@RequestParam Long examId) {
         return examService.deleteExam(examId);
     }
    
     
     @GetMapping("/getExamById")
     public Exam  getExamById(@RequestParam Long examId) {
         return examService.getExamById(examId);
     }

     @GetMapping("/getPassedExaminees")
     public List<Examinee> getPassedExaminees(@RequestParam Long examId) {
         return examService.getPassedExaminees(examId);
     }

     @GetMapping("/getExaminees")
     public List<Examinee> getExaminees(@RequestParam Long examId) {
         return examService.getExaminees(examId);
     }
    
     
     
     
     
     
}
