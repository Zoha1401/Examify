package com.onlineexammodule.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.onlineexammodule.backend.DTO.AnswerSubmissionRequest;
import com.onlineexammodule.backend.model.Answer;
import com.onlineexammodule.backend.model.Exam;
import com.onlineexammodule.backend.model.Examinee;
import com.onlineexammodule.backend.model.McqAnswer;
import com.onlineexammodule.backend.model.McqQuestion;
import com.onlineexammodule.backend.model.ProgrammingQuestion;
import com.onlineexammodule.backend.model.ProgrammingQuestionAnswer;
import com.onlineexammodule.backend.service.AnswerService;
import com.onlineexammodule.backend.service.ExamineeService;
import com.onlineexammodule.backend.service.JWTService;

import jakarta.servlet.http.HttpServletRequest;

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

    @Autowired
    private AnswerService answerService;
    
    @Autowired
    private JWTService jwtService;

    @PostMapping("/login")
    public String LoginExaminee(@RequestBody Examinee loginRequest) {
        
        System.out.println(loginRequest.getEmail() + loginRequest.getPassword());
        return examineeService.verify(loginRequest.getEmail(), loginRequest.getPassword());
       
    }

    @GetMapping("/getExamineeIdFromEmail")
    public Long getExamineeId(@RequestParam String examineeEmail) {
        return examineeService.getExamineeIdFromEmail(examineeEmail); 
    }
    

    @GetMapping("/getForExaminee")
    public String greet() {
        return "Examinee token validated hurrah";
    }

    @GetMapping("/getAllExams")
    public List<Exam> getAllExams(@RequestParam Long examineeId) {
        return examineeService.getAllexams(examineeId);
    }

    @GetMapping("/getAllMcqQuestions")
    public List<McqQuestion> getAllMcqQuestions(@RequestParam Long examId) {
        return examineeService.getAllMcqQuestions(examId);
    }
    
    @GetMapping("/getAllProgrammingQuestions")
    public List<ProgrammingQuestion> getAllProQuestions(@RequestParam Long examId) {
        return examineeService.getAllProgrammingQuestions(examId);
    }
    
    @PostMapping("/submitExam")
     public ResponseEntity<?> submitAnswer(@RequestParam Long examId,@RequestBody AnswerSubmissionRequest examineeAnswer, HttpServletRequest request) {
        System.out.println(examId);
        String token=request.getHeader("Authorization").substring(7);
        String examineeEmail=jwtService.extractEmail(token);
        System.out.println("Examinee that wants to submit an exam "+examineeEmail);
        System.out.println("Request body: " + examineeAnswer);
        List<McqAnswer> mcqAnswers=examineeAnswer.getMcqAnswers();
        List<ProgrammingQuestionAnswer> programmingQuestionAnswers=examineeAnswer.getProgrammingQuestionAnswers();
        Answer answer=examineeService.submitAnswer(mcqAnswers,programmingQuestionAnswers, examineeEmail, examId);
        return ResponseEntity.ok(answer);
    }
    
    @GetMapping("/getYourAnswer")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @GetMapping("/getExamById")
    public Exam getExamById(@RequestParam Long examId) {
        return examineeService.getExamById(examId);
    }

    @GetMapping("/getExamSpecificAnswer")
    public Answer getExamSpecificAnswer(@RequestParam Long examineeId, @RequestParam Long examId) {
        return answerService.getExamSpecificAnswers(examineeId, examId);
    }
    
    
    
    
}
