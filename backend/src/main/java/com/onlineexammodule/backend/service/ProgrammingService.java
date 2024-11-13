package com.onlineexammodule.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.aspectj.weaver.ast.Test;
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

        // Step 2: Save the ProgrammingQuestion and its test cases
        
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
    
        // Save ProgrammingQuestion independently to ensure it has a generated ID
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
    public ProgrammingQuestion updateProgrammingQuestion(ProgrammingQuestion programmingQuestion, Long examId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProgrammingQuestion'");
    }
     //Delete Pro
    public ProgrammingQuestion deleteProgrammingQuestion(ProgrammingQuestion programmingQuestion, Long examId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteProgrammingQuestion'");
    }








 //Exam Management
 //Create*, update, Delete*

 //Question Management
 //Add*, Update* Delete MCQ*, 
 //Add*, Update Delete Programming

 //MCQ management
 //Update, Add, delete Options


 //----> Next week ----> Submit Exam -> Examinee submit answers
//              ----> Show Exam Result.

//Integration with frontend


    
}
