package com.onlineexammodule.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.onlineexammodule.backend.model.ProgrammingQuestion;
import com.onlineexammodule.backend.model.TestCase;
import com.onlineexammodule.backend.service.ProgrammingService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/programmingQuestion")
@CrossOrigin(origins = "http://localhost:3000")
public class ProgrammingQuestionController {

    @Autowired
    private ProgrammingService programmingService;

    // Add programming question
    @PostMapping("/addProgrammingQuestion")
    public ProgrammingQuestion addProgrammingQuestion(@RequestBody ProgrammingQuestion ProgrammingQuestion,
            @RequestParam Long examId) {
        ProgrammingQuestion new_prog_ques = programmingService.addProgrammingQuestion(ProgrammingQuestion, examId);
        return new_prog_ques;
    }

    @GetMapping("/fetchTestCases")
    public List<TestCase> fetchTestCase(@RequestParam Long pro_id) {
        return programmingService.fetchTestCases(pro_id);
    }

    @PostMapping("/updateProgrammingQuestion")
    public ProgrammingQuestion updateProgrammingQuestion(@RequestBody ProgrammingQuestion ProgrammingQuestion,
            @RequestParam Long examId, @RequestParam Long proId) {
        ProgrammingQuestion up_prog_ques = programmingService.updateProgrammingQuestion(ProgrammingQuestion, examId,
                proId);
        return up_prog_ques;
    }

    @DeleteMapping("/deleteProgrammingQuestion")
    public ProgrammingQuestion deleteProgrammingQuestion(@RequestParam Long examId, @RequestParam Long proId) {
        ProgrammingQuestion up_prog_ques = programmingService.deleteProgrammingQuestion(examId, proId);
        return up_prog_ques;
    }

    @PostMapping("/addTestCase")
    public ResponseEntity<TestCase> addTestCase(@RequestBody TestCase testcase, Long examId, Long proId) {
        TestCase new_TestCase = programmingService.addTestCase(testcase, examId, proId);

        return new ResponseEntity<>(new_TestCase, HttpStatus.CREATED);
    }

    @PostMapping("/updateTestCase")
    public ResponseEntity<TestCase> updateTestCase(@RequestBody TestCase testcase, Long examId, Long proId,
            Long testcaseId) {
        TestCase upTestCase = programmingService.updateTestCase(testcase, examId, proId, testcaseId);
        return new ResponseEntity<>(upTestCase, HttpStatus.OK);
    }

    @DeleteMapping("/deleteTestCase")
    public ResponseEntity<String> deleteTestCase(Long examId, Long proId, Long testcaseId) {
        return programmingService.deleteTestCase(examId, proId, testcaseId);

    }

    // Get prog question By Id

    @GetMapping("/getProgrammingQuestionByDifficulty")
    public List<ProgrammingQuestion> getProgrammingQuestionByDifficulty(
            @RequestParam(required = false) String difficulty) {
        return programmingService.getProgrammingQuestionByDifficulty(difficulty);
    }

    @PostMapping("/addProgrammingQuestionPool")
    public String addProgrammingQuestionPool(@RequestBody List<ProgrammingQuestion> programmingQuestions) {
        return programmingService.addProgrammingQuestionPool(programmingQuestions);
    }

    // Get all programming question.
    @GetMapping("/getAllProgrammingQuestions")
    public List<ProgrammingQuestion> getAllProgrammingQuestions(@RequestParam Long examId) {
        return programmingService.getAllProgrammingQuestions(examId);
    }

    // To create a pool of questions

    @PostMapping("/addProgrammingQuestionList")
    public List<ProgrammingQuestion> addProgrammingQuestionList(Long examId,
            @RequestBody List<ProgrammingQuestion> listPro) {
        return programmingService.addProgrammingQuestionList(examId, listPro);
    }

    @PostMapping("/import-ProgrammingQuestions")
    public ResponseEntity<String> importMcqQuestions(@RequestParam("file") MultipartFile file,
            @RequestParam("examId") Long examId) {
        try {
            String result = programmingService.importProgrammingQuestionData(file, examId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to import questions: " + e.getMessage());
        }
    }
}
