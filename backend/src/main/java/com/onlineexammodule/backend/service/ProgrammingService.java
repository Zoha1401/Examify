package com.onlineexammodule.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.onlineexammodule.backend.model.Exam;
import com.onlineexammodule.backend.model.ProgrammingQuestion;
import com.onlineexammodule.backend.model.TestCase;
import com.onlineexammodule.backend.repo.ExamRepository;
import com.onlineexammodule.backend.repo.ProgrammingRepository;
import com.onlineexammodule.backend.repo.TestCaseRepository;

@Service
public class ProgrammingService {

    private final ProgrammingRepository programmingRepository;
    private final ExamRepository examRepository;
    private final TestCaseRepository testCaseRepository;

    public ProgrammingService(ProgrammingRepository programmingRepository, ExamRepository examRepository, TestCaseRepository testCaseRepository)
    {
        this.programmingRepository=programmingRepository;
        this.examRepository=examRepository;
        this.testCaseRepository=testCaseRepository;
    }
      public ProgrammingQuestion addProgrammingQuestion(ProgrammingQuestion programmingQuestion, Long examId) {
        Exam exam = examRepository.findById(examId).orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        
        System.out.println("Test case details before saving:");
         for (TestCase testCase : programmingQuestion.getTestCases()) {
         System.out.println("Input: " + testCase.getInput());
         System.out.println("Expected Output: " + testCase.getExpectedOutput());
        }

        System.out.println(programmingQuestion.getTestCases().size());
        List<TestCase> savedTestCases = new ArrayList<>();
        for (TestCase testCase : programmingQuestion.getTestCases()) {
            savedTestCases.add(testCaseRepository.save(testCase));
            System.out.println(testCase);
        }
        programmingQuestion.setTestCases(savedTestCases);
    
        programmingQuestion = programmingRepository.save(programmingQuestion);
    
        // Step 3: Now link the ProgrammingQuestion to the Exam
        exam.getProgrammingQuestions().add(programmingQuestion);
        programmingQuestion.getExams().add(exam);
    
        // Save Exam to persist the relationship
        examRepository.save(exam);
    
        return programmingQuestion;
    }
    public List<TestCase> fetchTestCases(Long pro_id) {
       
        if(!programmingRepository.existsById(pro_id))
        {
            throw new IllegalArgumentException("Programming question does not exist "+pro_id);
        }
      
        ProgrammingQuestion gProgrammingQuestion=programmingRepository.getReferenceById(pro_id);

        List<TestCase> gTestCases=gProgrammingQuestion.getTestCases();

        return gTestCases;
       
       
    }
     // Update pro
    public ProgrammingQuestion updateProgrammingQuestion(ProgrammingQuestion programmingQuestion, Long examId, Long proId) {

        Exam exam = examRepository.findById(examId).orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        List<ProgrammingQuestion> pro_Questions=exam.getProgrammingQuestions();
        ProgrammingQuestion toBeUpdated=new ProgrammingQuestion();

        for(ProgrammingQuestion programmingQuestion2: pro_Questions)
        {
            if(programmingQuestion2.getProgrammingQuestionId()==proId)
            {
                toBeUpdated=programmingQuestion2;
            }
        }

        if(toBeUpdated.getProgrammingQuestionId()==null)
        throw new IllegalArgumentException("Programming question not found");

        if(programmingQuestion.getDifficulty_level()!=null)
        toBeUpdated.setDifficulty_level(programmingQuestion.getDifficulty_level());

        if(programmingQuestion.getProgrammingQuestionText()!=null)
        toBeUpdated.setProgrammingQuestionText(programmingQuestion.getProgrammingQuestionText());

        return programmingRepository.save(toBeUpdated);



    }
     //Delete Pro
    public ProgrammingQuestion deleteProgrammingQuestion(Long examId, Long proId) {
        Exam exam = examRepository.findById(examId).orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        ProgrammingQuestion toBeDeleted = exam.getProgrammingQuestions().stream()
            .filter(progQuestion -> progQuestion.getProgrammingQuestionId().equals(proId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Programming Question not found in this Exam"));

        
        exam.deleteProgrammingQuestion(toBeDeleted);
        examRepository.save(exam);
        
        toBeDeleted.getExams().remove(exam);

        if(toBeDeleted.getExams().isEmpty())
        programmingRepository.delete(toBeDeleted);

        return toBeDeleted;
    }

    //Update TestCase

    public TestCase updateTestCase(TestCase testcase, Long examId, Long proId, Long testcaseId)
    {
        Exam exam = examRepository.findById(examId).orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        ProgrammingQuestion programmingQuestion = exam.getProgrammingQuestions().stream()
            .filter(progQuestion -> progQuestion.getProgrammingQuestionId().equals(proId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Programming Question not found in this Exam"));

        TestCase toBeUpdatedTestCase = programmingQuestion.getTestCases().stream()
            .filter(tc -> tc.getTestcaseId().equals(testcaseId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Test Case not found in this Programming Question"));

            toBeUpdatedTestCase.setInput(testcase.getInput());
            toBeUpdatedTestCase.setExpectedOutput(testcase.getExpectedOutput());

        programmingRepository.save(programmingQuestion);
        return toBeUpdatedTestCase;

    }

    public TestCase addTestCase(TestCase newTestCase, Long examId, Long proId) {
      
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));
    
       
        ProgrammingQuestion programmingQuestion = exam.getProgrammingQuestions().stream()
                .filter(progQuestion -> progQuestion.getProgrammingQuestionId().equals(proId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Programming Question not found in this Exam"));
    
        programmingQuestion.getTestCases().add(newTestCase);
        programmingRepository.save(programmingQuestion);
        testCaseRepository.save(newTestCase);
        return newTestCase;
    }


    public ResponseEntity<String> deleteTestCase(Long examId, Long proId, Long testcaseId) {
     
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));
    
        ProgrammingQuestion programmingQuestion = exam.getProgrammingQuestions().stream()
                .filter(progQuestion -> progQuestion.getProgrammingQuestionId().equals(proId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Programming Question not found in this Exam"));
    

        TestCase testCaseToDelete = programmingQuestion.getTestCases().stream()
                .filter(tc -> tc.getTestcaseId().equals(testcaseId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Test Case not found in this Programming Question"));
    
       
        programmingQuestion.getTestCases().remove(testCaseToDelete);
    
        programmingRepository.save(programmingQuestion);
    
       
        testCaseRepository.deleteById(testcaseId);
        return ResponseEntity.ok("Test Case deleted successfully");
    }
    public ProgrammingQuestion getProgrammingQuestionById(Long proId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProgrammingQuestionById'");
    }
    
    











    
}
