package com.onlineexammodule.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlineexammodule.backend.DTO.MCQ;
import com.onlineexammodule.backend.model.McqQuestion;
import com.onlineexammodule.backend.model.QuestionOption;
import com.onlineexammodule.backend.service.McqService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/mcqQuestion")
@CrossOrigin(origins = "http://localhost:3000")
public class McqQuestionController {
    
     
    @Autowired
    private McqService mcqService;

    @PostMapping("/addMcqQuestion")
     public ResponseEntity<MCQ> addMcqQuestion(@RequestBody McqQuestion McqQuestion, @RequestParam Long examId) {
        MCQ new_mcq=mcqService.addMcqQuestion(McqQuestion, examId);
        return new ResponseEntity<>(new_mcq, HttpStatus.CREATED);
     }

     @PostMapping("/updateMcqQuestion")
     public ResponseEntity<MCQ> updateMcqQuestion(@RequestParam Long mcqId, @RequestBody McqQuestion McqQuestion, @RequestParam Long examId) {
         MCQ updated_mcq=mcqService.updateMcqQuestion(mcqId, McqQuestion, examId);
         return new ResponseEntity<>(updated_mcq, HttpStatus.OK);
     }

     @DeleteMapping("/deleteMcqQuestion")
     public String deleteMcqQuestion(@RequestParam Long mcqId, @RequestParam Long examId){
         String string=mcqService.deleteMcqQuestion(mcqId, examId);
         return string;
     }

     @PostMapping("/addOption")
     public ResponseEntity<QuestionOption> addOption(@RequestBody QuestionOption questionOption, @RequestParam Long mcqId) {
         return mcqService.addOption(mcqId, questionOption);
     }

     
     @PostMapping("/updateOption")
     public ResponseEntity<QuestionOption> updateOption(@RequestBody QuestionOption questionOption, @RequestParam Long mcqId, @RequestParam Long optionId) {
         return mcqService.updateOption(mcqId, optionId, questionOption);
     }

     @DeleteMapping("/deleteOption")
     public ResponseEntity<QuestionOption> deleteOption(@RequestParam Long mcqId, @RequestParam Long optionId) {
        return mcqService.deleteOption(mcqId, optionId);
    }

    
    //Get Mcq by ID
    @GetMapping("/getMcqById")
    public McqQuestion getMcqById(@RequestParam Long mcqId) {
        return mcqService.getMcqById(mcqId);
    }
    
     
     

    
}
