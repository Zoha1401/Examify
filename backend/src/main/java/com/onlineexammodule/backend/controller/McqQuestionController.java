package com.onlineexammodule.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.onlineexammodule.backend.DTO.MCQ;
import com.onlineexammodule.backend.model.McqQuestion;
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

    //When examiner adds an mcq question it gets saved in the pool which then can be used for other exams
    @PostMapping("/addMcqQuestion")
    public ResponseEntity<MCQ> addMcqQuestion(@RequestBody McqQuestion McqQuestion, @RequestParam Long examId) {
        MCQ new_mcq = mcqService.addMcqQuestion(McqQuestion, examId);
        return new ResponseEntity<>(new_mcq, HttpStatus.CREATED);
    }

    @PostMapping("/updateMcqQuestion")
    public ResponseEntity<McqQuestion> updateMcqQuestion(@RequestParam Long mcqId, @RequestBody McqQuestion McqQuestion,
            @RequestParam Long examId) {
        McqQuestion updated_mcq = mcqService.updateMcqQuestion(mcqId, McqQuestion, examId);
        return new ResponseEntity<>(updated_mcq, HttpStatus.OK);
    }

    @DeleteMapping("/deleteMcqQuestion")
    public String deleteMcqQuestion(@RequestParam Long mcqId, @RequestParam Long examId) {
        String string = mcqService.deleteMcqQuestion(mcqId, examId);
        return string;
    }

    // @PostMapping("/addOption")
    // public ResponseEntity<QuestionOption> addOption(@RequestBody QuestionOption
    // questionOption, @RequestParam Long mcqId) {
    // return mcqService.addOption(mcqId, questionOption);
    // }

    // @PostMapping("/updateOption")
    // public ResponseEntity<QuestionOption> updateOption(@RequestBody
    // QuestionOption questionOption, @RequestParam Long mcqId, @RequestParam Long
    // optionId) {
    // return mcqService.updateOption(mcqId, optionId, questionOption);
    // }

    // @DeleteMapping("/deleteOption")
    // public ResponseEntity<QuestionOption> deleteOption(@RequestParam Long mcqId,
    // @RequestParam Long optionId) {
    // return mcqService.deleteOption(mcqId, optionId);
    // }

    // Get Mcq by ID

    @GetMapping("/getMcqQuestionByDifficultyAndCategory")
    public List<McqQuestion> getMethodName(@RequestParam String category, @RequestParam String difficulty) {
        return mcqService.getMcqQuestionByDifficultyAndCategory(category, difficulty);
    }

    @PostMapping("/addMcqQuestionPool")
    public String addMcqQuestionPool(@RequestBody List<McqQuestion> mcqQuestions) {
        return mcqService.addMcqQuestionPool(mcqQuestions);
    }

 
    @GetMapping("/getAllMcqQuestions")
    public List<McqQuestion> getAllMcqQuestions(@RequestParam Long examId) {
        return mcqService.getAllMcqQuestions(examId);
    }

    @PostMapping("/addMcqQuestionList")
    public List<McqQuestion> addMcqQuestionList(Long examId, @RequestBody List<McqQuestion> listMcqs) {
        return mcqService.addMcqQuestionList(examId, listMcqs);
    }

    @GetMapping("/getMcqTechnical")
    public List<McqQuestion> getMcqTechnical(@RequestParam Long examId) {
        return mcqService.getMcqTechnical(examId);
    }

    @GetMapping("/getMcqAptitude")
    public List<McqQuestion> getMcqAptitude(@RequestParam Long examId) {
        return mcqService.getMcqAptitude(examId);
    }

    @PostMapping("/import-McqQuestions")
    public ResponseEntity<String> importMcqQuestions(@RequestParam("file") MultipartFile file,
            @RequestParam("examId") Long examId) {
        try {
            String result = mcqService.importMcqQuestionData(file, examId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to import questions: " + e.getMessage());
        }
    }

}
