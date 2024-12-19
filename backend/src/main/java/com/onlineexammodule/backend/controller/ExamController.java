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
import com.onlineexammodule.backend.model.Answer;
import com.onlineexammodule.backend.model.Exam;
import com.onlineexammodule.backend.model.McqQuestion;
import com.onlineexammodule.backend.model.ProgrammingQuestion;
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

     //Get all MCQ questions.
     @GetMapping("/getAllMcqQuestions")
     public List<McqQuestion> getAllMcqQuestions(@RequestParam Long examId) {
         return examService.getAllMcqQuestions(examId);
     }
     

     //Get all programming question.
     @GetMapping("/getAllProgrammingQuestions")
     public List<ProgrammingQuestion> getAllProgrammingQuestions(@RequestParam Long examId) {
         return examService.getAllProgrammingQuestions(examId);
     }
     
     
     //To create a pool of questions
     @PostMapping("/addMcqQuestionList")
     public List<McqQuestion> addMcqQuestionList(Long examId, @RequestBody List<McqQuestion> listMcqs) {
         return examService.addMcqQuestionList(examId, listMcqs);
     }
     
     
     @PostMapping("/addProgrammingQuestionList")
     public List<ProgrammingQuestion> addProgrammingQuestionList(Long examId, @RequestBody List<ProgrammingQuestion> listPro) {
          return examService.addProgrammingQuestionList(examId, listPro);
     }
     
     @GetMapping("/getExamById")
     public Exam  getExamById(@RequestParam Long examId) {
         return examService.getExamById(examId);
     }

     @GetMapping("/getMcqTechnical")
     public List<McqQuestion> getMcqTechnical(@RequestParam Long examId) {
         return examService.getMcqTechnical(examId);
     }

     @GetMapping("/getMcqAptitude")
     public List<McqQuestion> getMcqAptitude(@RequestParam Long examId) {
         return examService.getMcqAptitude(examId);
     }
     
     @GetMapping("/getAllAnswers")
     public List<Answer> getAnswers(@RequestParam Long examId) {
         return examService.getAllAnswers(examId);
     }
     
     
     
}
